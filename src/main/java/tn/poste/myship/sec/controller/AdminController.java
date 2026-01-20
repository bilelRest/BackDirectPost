package tn.poste.myship.sec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.poste.myship.sec.entity.AppRole;
import tn.poste.myship.sec.entity.AppUser;
import tn.poste.myship.sec.service.AppUserService;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")

public class AdminController {
    @Autowired
    AppUserService appUserService;
    @PostMapping("/adduser")
    public AppUser addUser(@RequestBody AppUser appUser){
       return appUserService.addUser(appUser);
    }
    @PostMapping("deleteuser")
    public ResponseEntity<?> deleteUser(@RequestBody AppUser appUser){

         if (appUserService.removeUser(appUser)){
             return ResponseEntity.ok().build();
         }
         return ResponseEntity.notFound().build();
    }
    @GetMapping("addroletouser")
    public AppUser addRoleTouser(@RequestParam(value = "user") String appUser, @RequestParam(value = "role") String appRole){
        return appUserService.addRoleToUser(appUser,appRole);
    }
    @PostMapping("deleteroletouser")
    public AppUser deleteRoleToUser(@RequestBody AppUser appUser,@RequestBody AppRole appRole){
        return appUserService.removeRoleForUser(appUser.getUsername(),appRole.getRoleName());
    }
}
