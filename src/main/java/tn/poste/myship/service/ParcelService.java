package tn.poste.myship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.poste.myship.entity.*;
import tn.poste.myship.repo.ParcelRepo;

@Service
public class ParcelService {
    @Autowired
    ParcelRepo parcelRepo;
    //Création d'un nouveau envoi ou bien mettre à jours si existant
    public void createOrUpdateParcel(Parcel parcel){
        Parcel parcel1=parcelRepo.findByTrackingNumber(parcel.getTrackingNumber());
        try{
        if (parcel1 != null){
            parcel1.setHeight(parcel.getHeight());
            parcel1.setWeight(parcel.getWeight());
            parcel1.setWidth(parcel.getWidth());
            parcel1.setLenght(parcel.getLenght());
            parcel1.setSender(parcel.getSender());
            parcel1.setReceiver(parcel.getReceiver());
            parcel1.setPrice(parcel.getPrice());
            parcelRepo.save(parcel1);


        }else
            parcelRepo.save(parcel);
    }catch (Exception e) {
            throw new RuntimeException(e);
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

}


