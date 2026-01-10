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
    public Boolean setValidated(String operationId){
        Operation operation=operationRepo.findByFormattedId(operationId);
        if (operation != null){
            operation.setValidated(true);
            operationRepo.save(operation);
            return true;
        }else return false;

    }
    //ajouter un colis a l'opeartion en cours
    public Boolean addParcel(String operationId, Parcel parcel){
        Operation operation=operationRepo.findByFormattedId(operationId);
        if (operation != null){
            operation.getParcel().add(parcel);
            operationRepo.save(operation);
            return true;
        }else return false;

    }
    //ajouter une pochette à l'operation en cours
    public Boolean addPochete(String operationId, Pochette pochette){
        Operation operation=operationRepo.findByFormattedId(operationId);
        if(operation != null){
            operation.getPochette().add(pochette);
            return true;
        }else return false;
    }
    //consulter les details de operation
    public Operation getOperationContent(String formattedId) {
        Operation operation = operationRepo.findByFormattedId(formattedId);

        // On filtre les listes pour ne renvoyer que ce qui n'est pas "deleted"
        List<Parcel> activeParcels = operation.getParcel().stream()
                .filter(p -> !p.getDeleted())
                .collect(Collectors.toList());

        List<Pochette> activePochettes = operation.getPochette().stream()
                .filter(p -> !p.getDeleted())
                .collect(Collectors.toList());

        // On remplace les listes uniquement dans l'objet de réponse
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
