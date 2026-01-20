package tn.poste.myship.sec.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.poste.myship.sec.config.JwtUtils;
import tn.poste.myship.sec.entity.AppUser;
import tn.poste.myship.sec.repo.UserRepository;
import tn.poste.myship.sec.service.AppUserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AppUserService userService;
    @Autowired
    UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        String jwt = jwtUtils.generateToken((UserDetails) auth.getPrincipal());
        String refresh=jwtUtils.generateRefreshToken((UserDetails)auth.getPrincipal());

        // On crée une réponse structurée
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        response.put("refresh",refresh);
        //response.put("type", "Bearer"); // Indique au client comment utiliser le token

        return ResponseEntity.ok(response);
    }
   // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String addUser(@RequestBody LoginRequest loginRequest){
         try {
             AppUser appUser = new AppUser();
             System.out.println("Login recu "+loginRequest.getUsername()+"\n password recu "+loginRequest.getPassword());

             appUser.setUsername(loginRequest.getUsername());
             appUser.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
             userRepository.save(appUser);
             return loginRequest.getUsername();
         }catch (Exception e){
             return null;
         }
    }
    @PreAuthorize("hasRole('ADMIN')") // Ensure only admins can call this    @PostMapping("/addrole")
    public ResponseEntity<?> addrole(@RequestBody UserRole userRole) {
        try {
            userService.addRoleToUser(userRole.getUsername(), userRole.getRole());
            return ResponseEntity.ok("Role added successfully to " + userRole.getUsername());
        } catch (EntityNotFoundException e) {
            // Specifically catch if the user or role doesn't exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        // Utilise "refresh" (minuscule, sans Token à la fin) pour matcher Angular
        String refreshToken = request.get("refresh");

        if (refreshToken != null && jwtUtils.validateToken(refreshToken)) {
            String username = jwtUtils.getUsernameFromToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtUtils.generateToken(userDetails);

            Map<String, String> response = new HashMap<>();
            response.put("token", newAccessToken);
            //response.put("refresh", refreshToken); // On renvoie le même ou un nouveau

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expirée");
    }
    }


