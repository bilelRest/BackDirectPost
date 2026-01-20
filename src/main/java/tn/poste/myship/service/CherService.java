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
        AppUser appUser = extractUser();

        // Vérifier si l'utilisateur possède le rôle CHEF
        boolean isChef = appUser.getAppRoles().stream()
                .anyMatch(role -> role.getRoleName().equals("CHEF"));

        if (isChef) {
            List<Situation> results = situationRepo.findByAgenceAndDateAndCloturedFalse(appUser.getAgence(), LocalDate.now());
            // ✅ NE JAMAIS retourner null, retourner une liste vide []
            return (results != null) ? results : new java.util.ArrayList<>();
        }

        return new java.util.ArrayList<>();
    }
    public List<Situation> chefClotured(){
        AppUser appUser = extractUser();
        boolean isChef = appUser.getAppRoles().stream()
                .anyMatch(role -> role.getRoleName().equals("CHEF"));

        if (isChef) {
            List<Situation> situationList = situationRepo.findByAgenceAndDateAndCloturedFalse(appUser.getAgence(), LocalDate.now());
            for(Situation situation : situationList){
                situation.setClotured(true);
                situationRepo.save(situation);
            }
            return situationList;
        }
        return new java.util.ArrayList<>();
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
