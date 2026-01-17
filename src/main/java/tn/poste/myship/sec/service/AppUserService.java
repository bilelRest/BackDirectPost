package tn.poste.myship.sec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.poste.myship.sec.entity.AppRole;
import tn.poste.myship.sec.entity.AppUser;
import tn.poste.myship.sec.repo.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service

public class AppUserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AppRoleService roleService;
    public AppUser addUser(AppUser user){
        AppUser appUser=userRepository.findByUsername(user.getUsername());
        if (appUser != null){
            return null;
        }else {
            return userRepository.save(user);
        }
    }
    public AppUser addRoleToUser(String username,String role){
        AppUser appUser1=userRepository.findByUsername(username);
        AppRole role1=roleService.findByRoleName(role);
        if (appUser1 == null || role1 == null){
            return null;
        }else {
            appUser1.getAppRoles().add(role1);
            return userRepository.save(appUser1);
        }
    }
    public AppUser removeRoleForUser(String username,String role){
        AppUser appUser1=userRepository.findByUsername(username);
        AppRole role1=roleService.findByRoleName(role);
        if (appUser1 == null || role1 == null){
            return null;
        }else {
            appUser1.getAppRoles().remove(role1);
            return userRepository.save(appUser1);
        }
    }
}
