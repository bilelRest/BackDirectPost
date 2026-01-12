package tn.poste.myship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.poste.myship.entity.*;
import tn.poste.myship.repo.ParcelRepo;

import java.util.List;

@Service
public class ParcelService {
    @Autowired
    ParcelRepo parcelRepo;
    //Création d'un nouveau envoi ou bien mettre à jours si existant
    public Parcel createOrUpdateParcel(Parcel parcel) {
        Parcel parcelExist = parcelRepo.findByTrackingNumber(parcel.getTrackingNumber());
        if (parcelExist != null) {
            // Mise à jour de l'existant
            parcelExist.setHeight(parcel.getHeight());
            parcelExist.setWeight(parcel.getWeight());
            parcelExist.setWidth(parcel.getWidth());
            parcelExist.setLenght(parcel.getLenght());
            parcelExist.setSender(parcel.getSender());
            parcelExist.setReceiver(parcel.getReceiver());
            parcelExist.setPrice(parcel.getPrice());
            return parcelRepo.save(parcelExist); // Retourne l'objet à jour
        } else {
            // Création
            return parcelRepo.save(parcel);
        }
    }
    //Supprimer par le tracking number
public void deleteByTrackingNumber(String tracking){
        try {
            Parcel parcel=parcelRepo.findByTrackingNumberString(tracking);
            if (parcel != null){
                parcel.setDeleted(true);

            }
        }catch (Exception e){
            throw new RuntimeException();
        }
}
//Selectionner les envois
    public Page<Parcel> getParcels(Pageable pageable ){
        return parcelRepo.findAll(pageable);
}
//Selectionner par num tracking
    public Parcel getByTrackingNumebr(String tracking){
        return parcelRepo.findByTrackingNumberString(tracking);
    }
    //select par numero operation
    public List<Parcel> getByOpId(String opId){
        return parcelRepo.findByOperationId(opId);
    }

}


