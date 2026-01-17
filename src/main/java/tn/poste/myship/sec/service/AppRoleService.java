package tn.poste.myship.sec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.poste.myship.sec.entity.AppRole;
import tn.poste.myship.sec.repo.RoleRepository;

@Service
public class AppRoleService {
    @Autowired
    RoleRepository roleRepository;
    public AppRole addRole(String role){
        AppRole appRole=roleRepository.findByRoleName(role);
        if (appRole.getRoleName().equals(role)){
            return null;
        }else {
            return roleRepository.save(new AppRole(role));
        }
    }
public AppRole findByRoleName(String role){
        return roleRepository.findByRoleName(role);
}
}
