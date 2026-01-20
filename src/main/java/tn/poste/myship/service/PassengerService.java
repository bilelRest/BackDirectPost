package tn.poste.myship.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import tn.poste.myship.entity.Operation;
import tn.poste.myship.entity.Parcel;
import tn.poste.myship.entity.Pochette;
import tn.poste.myship.entity.Situation;
import tn.poste.myship.repo.*;
import tn.poste.myship.sec.config.JwtUtils;
import tn.poste.myship.sec.entity.AppUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PassengerService {
    @Autowired
    OperationRepo operationRepo;
@Autowired
TrackingService trackingService;
@Autowired
private SituationRepo situationRepo;
@Autowired
ParcelService parcelService;
@Autowired
PochetteService pochetteService;
@Autowired
CheckClient checkClient;
@Autowired
    JwtUtils jwtUtils;
@Autowired
    UserDetailsService userDetailsService;
//Verification si operation deja existante pour eviter le declenchement des ops eu cours de rafraichissement
    public Operation checkOps(Long id){
        return operationRepo.findById(id).isPresent()?operationRepo.findById(id).get():null;
    }
    //Select tous les operation avec user / agence au future
    public List<Operation> getAllOps(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser=(AppUser) authentication.getPrincipal();
        return operationRepo.findByAppUser(appUser);
    }
//Creation d'une nouvelle operation
public Operation NewOperation() {
    Operation operation = new Operation();
    operation.setParcel(new ArrayList<>());
    operation.setPochette(new ArrayList<>());

    // 1. Récupérer l'utilisateur actuellement connecté via le contexte de sécurité
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // 2. Extraire l'objet AppUser (Cast sécurisé)
    if (authentication != null && authentication.getPrincipal() instanceof AppUser appUser) {
        operation.setAppUser((AppUser) authentication.getPrincipal());
    } else {
        throw new IllegalStateException("Utilisateur non authentifié");
    }

    return operationRepo.save(operation);
}
    //definir une opeartion comme annulé aprés payment
    public Operation setCancelled(String operationId){
        Operation operation=operationRepo.findByFormattedId(operationId);
        if (operation != null){
            operation.setCancelled(true);

            return operationRepo.save(operation);
        }else return null;

    }
    //definir une operation comme brouillon avant la validation du monatant
    public Boolean setDraft(String operationId){
        Operation operation=operationRepo.findByFormattedId(operationId);
        if (operation != null){
            operation.setValidated(false);
            operationRepo.save(operation);
            return true;
        }else return false;

    }
    //definir une operation comme validé et encaisser
    public Operation setValidated(String operationId, String banque, String cheque) {
        Operation operation = operationRepo.findByFormattedId(operationId);
        if (operation != null) {
            Double total=0.0;
            for (Pochette pochette:operation.getPochette()){
                if (!pochette.getDeleted()){
                total+=pochette.getTotalPrice();}
            }
            for (Parcel parcel:operation.getParcel()){
                if (!parcel.getDeleted()){
                total+=parcel.getPrice();}
            }
            operation.setTotal(total);
            System.out.println("Total coté serveurs : "+total);
            if (!"none".equals(banque) && !"none".equals(cheque)) {
                operation.setBanque(banque);
                operation.setCheque(cheque);
            }
            operation.setValidated(true);


            // --- LA SOLUTION ICI ---
            // On vide manuellement les listes avant de renvoyer l'objet JSON
            // Cela empêche Jackson de descendre dans les colis et de boucler.
//        savedOp.setParcel(operation.getParcel());
//         savedOp.setPochette(operation.getPochette());
System.out.println("Traitement terminé en voies des resulata");
            return operationRepo.save(operation);
        }
        return null;
    }

    //ajouter un colis a l'opeartion en cours
    public Parcel addParcel(String operationId, Parcel parcel) {
        Operation operation = operationRepo.findByFormattedId(operationId);
        if (operation == null) throw new RuntimeException("Opération introuvable");
        if(parcel.getTrackingNumber().getFormattedParcelId()!=null){
Parcel updatedParcel=parcelService.getByTrackingNumebr(parcel.getTrackingNumber().getFormattedParcelId());
if (updatedParcel !=null){
    updatedParcel.setReceiver(checkClient.checkReceiver(parcel.getReceiver()));
    updatedParcel.setSender(checkClient.checkSender(parcel.getSender()));
    updatedParcel.setPrice(parcel.getPrice());
    updatedParcel.setWeight(parcel.getWeight());
    updatedParcel.setHeight(parcel.getHeight());
    updatedParcel.setLenght(parcel.getLenght());
    updatedParcel.setWidth(parcel.getWidth());

    return parcelService.createOrUpdateParcel(updatedParcel);

}
        }
        parcel.setTrackingNumber(trackingService.generateTrackingNumber());
        parcel.setSender(checkClient.checkSender(parcel.getSender()));
        parcel.setReceiver(checkClient.checkReceiver(parcel.getReceiver()));

        // On récupère l'instance gérée (managed) après le save
        Parcel savedParcel = parcelService.createOrUpdateParcel(parcel);

        // Mise à jour de la liste côté opération
        operation.getParcel().add(savedParcel);
        operationRepo.save(operation);

        return savedParcel; // On retourne l'objet qui contient toutes les relations
    }
    //ajouter une pochette à l'operation en cours
    public Pochette addPochete(String operationId, Pochette pochette) {
        // 1. Vérifier si l'opération existe
        Operation operation = operationRepo.findByFormattedId(operationId);
        if (operation == null) throw new RuntimeException("Operation introuvable");

        // 2. Vérifier si c'est une MISE À JOUR (ID présent) ou une CRÉATION (ID null)
        if (pochette.getId() != null && pochette.getId() != 0) {
            return pochetteService.pochetteRepo.findById(pochette.getId())
                    .map(updatedPochett -> {
                        updatedPochett.setSender(checkClient.checkSender(pochette.getSender()));
                        updatedPochett.setTypePochette(pochette.getTypePochette());
                        updatedPochett.setQuantite(pochette.getQuantite());
                        updatedPochett.setTotalPrice(pochette.getTotalPrice());
                        return pochetteService.pochetteRepo.save(updatedPochett);
                    })
                    .orElseGet(() -> {
                        // Si l'ID était fourni mais n'existe plus en base, on crée une nouvelle
                        return createNewPochette(operationId, pochette);
                    });
        } else {
            // 3. Cas de la CRÉATION (ID est null)
            return createNewPochette(operationId, pochette);
        }
    }

    // Petite méthode utilitaire pour la création
    private Pochette createNewPochette(String operationId, Pochette pochette) {
        pochette.setSender(checkClient.checkSender(pochette.getSender()));
        return pochetteService.addPochete(operationId, pochette);
    }

    //consulter les details de operation
    public Operation getOperationContent(String formattedId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
AppUser appUser=(AppUser) authentication.getPrincipal();
        Operation operation = operationRepo.findByFormattedIdAndAppUser(formattedId,appUser);
        List<Parcel> activeParcels=new ArrayList<>();
        List<Pochette> activePochettes=new ArrayList<>();
if (operation.getParcel()!=null) {
    // 1. On filtre les listes (ton code actuel)
     activeParcels = operation.getParcel().stream()
            .filter(p -> !p.getDeleted())
            .collect(Collectors.toList());
}if (operation.getPochette() != null) {
            activePochettes = operation.getPochette().stream()
                    .filter(p -> !p.getDeleted())
                    .collect(Collectors.toList());
        }
        // 2. LA SOLUTION : On coupe le lien retour vers l'opération
        // Cela empêche Jackson de boucler, mais ne change rien en base de données


        // 3. On affecte les listes "nettoyées" à l'objet de réponse
        operation.setParcel(activeParcels);
        operation.setPochette(activePochettes);
        Double total=0.0;
        for (Pochette pochette:operation.getPochette()){
            total+=pochette.getTotalPrice();
        }
        for (Parcel parcel:operation.getParcel()){
            total+=parcel.getPrice();
        }
        operation.setTotal(total);

        return operation;
    }
//Supprimer un colis de operation en cours
    public Parcel deleteParcelFromCurrentOpeartion(String tracking){

return parcelService.deleteByTrackingNumber(tracking);

    }
    //Supprimer une pochette de operation en cours
    public Pochette deletePochetteFromCurrentOperation(Pochette pochette){


           Optional<Pochette> pochette1=pochetteService.pochetteRepo.findById(pochette.getId());
           if (pochette1.isPresent()) {
               pochette1.get().setDeleted(true);
               return pochetteService.pochetteRepo.save(pochette1.get());
           }else

return null;

    }

    @Transactional // Essential for data consistency
    public List<Operation> closeSituationAgent() {
        // 1. Get current Agent
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = (AppUser) authentication.getPrincipal();

        // 2. Fetch pending operations
        List<Operation> operations = operationRepo.findByAppUserAndValidatedTrueAndCancelledFalseAndDeletedFalseAndClosedFalse(appUser);

        if (operations.isEmpty()) {
            return null;
        }

        // 3. Initialize the Situation object
        Situation situation = new Situation();
        situation.setAppUser(appUser);
        situation.setAgence(appUser.getAgence());

        // 4. Calculate total and update operations in one loop
        Double totalAmount = 0.0;
        for (Operation op : operations) {
            op.setClosed(true);
            totalAmount += (op.getTotal() != null) ? op.getTotal() : 0.0;
        }

        situation.setTotal(totalAmount);
        situation.setOperations(operations);

        // 5. Save the Situation.
        // Because of CascadeType.ALL or the relationship,
        // saving the situation can update the operations.
        Situation savedSituation = situationRepo.save(situation);

        // 6. Update operations with the new Situation ID (if using the manual ID field)
        for (Operation op : operations) {
            op.setSituationId(savedSituation.getId());
        }

        return operationRepo.saveAll(operations);
    }
    public List<Operation> getNonClosedOperationContentByAgent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser=(AppUser) authentication.getPrincipal();
        return operationRepo.findByAppUserAndValidatedTrueAndCancelledFalseAndDeletedFalseAndClosedFalse(appUser);

    }

    public List<Situation>  getSituation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser=(AppUser) authentication.getPrincipal();
        return situationRepo.findByAppUser(appUser);

    }
}
