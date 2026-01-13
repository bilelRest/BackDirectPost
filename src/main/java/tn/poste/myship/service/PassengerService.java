package tn.poste.myship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.poste.myship.entity.Operation;
import tn.poste.myship.entity.Parcel;
import tn.poste.myship.entity.Pochette;
import tn.poste.myship.repo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassengerService {
    @Autowired
    OperationRepo operationRepo;
@Autowired
TrackingService trackingService;
@Autowired
ParcelService parcelService;
@Autowired
PochetteService pochetteService;
@Autowired
CheckClient checkClient;
//Verification si operation deja existante pour eviter le declenchement des ops eu cours de rafraichissement
    public Operation checkOps(Long id){
        return operationRepo.findById(id).isPresent()?operationRepo.findById(id).get():null;
    }
//Creation d'une nouvelle operation
    public Operation NewOpeartion(){
        Operation operation=new Operation();
        operation.setParcel(new ArrayList<>());
        operation.setPochette(new ArrayList<>());
        operationRepo.save(operation);
        return operation;
    }
    //definir une opeartion comme annulé aprés payment
    public Boolean setCancelled(String operationId){
        Operation operation=operationRepo.findByFormattedId(operationId);
        if (operation != null){
            operation.setCancelled(true);
            operationRepo.save(operation);
            return true;
        }else return false;

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
            if (!"none".equals(banque) && !"none".equals(cheque)) {
                operation.setBanque(banque);
                operation.setCheque(cheque);
            }
            operation.setValidated(true);

            Operation savedOp = operationRepo.save(operation);

            // --- LA SOLUTION ICI ---
            // On vide manuellement les listes avant de renvoyer l'objet JSON
            // Cela empêche Jackson de descendre dans les colis et de boucler.
            savedOp.setParcel(new ArrayList<>());
            savedOp.setPochette(new ArrayList<>());

            return savedOp;
        }
        return null;
    }

    //ajouter un colis a l'opeartion en cours
    public Parcel addParcel(String operationId, Parcel parcel) {
        Operation operation = operationRepo.findByFormattedId(operationId);
        if (operation == null) throw new RuntimeException("Opération introuvable");

        parcel.setOperationId(operation);
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
    public Pochette addPochete(String operationId, Pochette pochette){
        Operation operation=operationRepo.findByFormattedId(operationId);
        if(operation == null)throw new RuntimeException("Operation ontrouvable");
        pochette.setOperation(operation);
        pochette.setSender(checkClient.checkSender(pochette.getSender()));
        Pochette savedPochette=pochetteService.addPochete(pochette);
        operation.getPochette().add(savedPochette);
        return savedPochette;
    }
    //consulter les details de operation
    public Operation getOperationContent(String formattedId) {
        Operation operation = operationRepo.findByFormattedId(formattedId);

        // 1. On filtre les listes (ton code actuel)
        List<Parcel> activeParcels = operation.getParcel().stream()
                .filter(p -> !p.getDeleted())
                .collect(Collectors.toList());

        List<Pochette> activePochettes = operation.getPochette().stream()
                .filter(p -> !p.getDeleted())
                .collect(Collectors.toList());

        // 2. LA SOLUTION : On coupe le lien retour vers l'opération
        // Cela empêche Jackson de boucler, mais ne change rien en base de données
        activeParcels.forEach(p -> p.setOperationId(null));
        activePochettes.forEach(p -> p.setOperation(null));

        // 3. On affecte les listes "nettoyées" à l'objet de réponse
        operation.setParcel(activeParcels);
        operation.setPochette(activePochettes);

        return operation;
    }
//Supprimer un colis de operation en cours
    public void deleteParcelFromCurrentOpeartion(String tracking){

parcelService.deleteByTrackingNumber(tracking);
    }
    //Supprimer une pochette de operation en cours
    public void deletePochetteFromCurrentOperation(String opId,String typePochette){
       Operation operation=operationRepo.findByFormattedId(opId);
       for (Pochette pochette :operation.getPochette()){
           if (pochette.getTypePochette().equals(typePochette)){
               pochette.setDeleted(true);
               pochetteService.addPochete(pochette);
           }
       }


    }
    

}
