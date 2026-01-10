package tn.poste.myship.sec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.poste.myship.sec.config.JwtUtils;
import tn.poste.myship.sec.entity.AppUser;
import tn.poste.myship.sec.repo.UserRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        String jwt = jwtUtils.generateToken((UserDetails) auth.getPrincipal());

        // On crée une réponse structurée
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        //response.put("type", "Bearer"); // Indique au client comment utiliser le token

        return ResponseEntity.ok(response);
    }
    @PostMapping("/new")
    public LoginRequest addUser(@RequestBody LoginRequest loginRequest){
         try {
             AppUser appUser = new AppUser();
             System.out.println("Login recu "+loginRequest.getUsername()+"\n password recu "+loginRequest.getPassword());

             appUser.setUsername(loginRequest.getUsername());
             appUser.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
             userRepository.save(appUser);
             return loginRequest;
         }catch (Exception e){
             return null;
         }
    }
}
