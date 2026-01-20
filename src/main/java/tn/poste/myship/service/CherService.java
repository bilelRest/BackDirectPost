package tn.poste.myship.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tn.poste.myship.entity.Situation;
import tn.poste.myship.repo.SituationRepo;
import tn.poste.myship.sec.entity.AppRole;
import tn.poste.myship.sec.entity.AppUser;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CherService {
    @Autowired
    SituationRepo situationRepo;
    public List<Situation> chefSituation(){
        System.out.println("Entree dans la methode ");
        AppUser appUser=extractUser();
        boolean isChef=false;
        if (appUser !=null && !appUser.getAppRoles().isEmpty()){
            System.out.println("appuser non null ");

            for (AppRole appRole: appUser.getAppRoles()){
                if (!appRole.getRoleName().equals("CHEF")) {
                    isChef = true;
                    break;
                }
            }
            if (isChef){
                return situationRepo.findByAgenceAndDate(appUser.getAgence(), LocalDate.now());
            }
        }
        System.out.println("appuser  null ");

            return null;

    }
    public AppUser extractUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Extraire l'objet AppUser (Cast sécurisé)
        if (authentication != null && authentication.getPrincipal() instanceof AppUser appUser) {
            return (AppUser) authentication.getPrincipal();
        } else {
            throw new IllegalStateException("Utilisateur non authentifié");
        }
    }
}
