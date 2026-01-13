package tn.poste.myship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.poste.myship.entity.Pochette;
import tn.poste.myship.repo.PochetteRepo;

@Service
public class PochetteService {
    @Autowired
    PochetteRepo pochetteRepo;
    //Charger pochette par type
    public Pochette selectionnerPochetteParType(String type){
       return pochetteRepo.findByTypePochette(type);
    }
    //Ajouter  une pochette
    public Pochette addPochete(String op,Pochette pochette){
        pochette.setOperationId(op);
        return  pochetteRepo.save(pochette);

             }
    //Mettre a jours

}
