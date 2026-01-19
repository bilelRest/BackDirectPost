package tn.poste.myship.sec.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tn.poste.myship.entity.Situation;

@Entity
public class AppUser implements UserDetails {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;    
    private String username;
    @JsonIgnore
    private String password;  
    private String email;
    @OneToMany(fetch = FetchType.EAGER)
    List<AppRole> appRoles;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convertit vos AppRole en list de SimpleGrantedAuthority pour Spring
        return this.appRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }
    // In AppUser.java
    @OneToMany(mappedBy = "appUser")
    @JsonIgnore // This stops Jackson from trying to load the situations list
    private List<Situation> situation;

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
    public AppUser() {
    }
    public AppUser(String username, String password, String email, List<AppRole> appRoles, List<Situation> situation) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.appRoles = appRoles;
        this.situation = situation;
    }
    public Long getUserId() {
        return userId;
    }

    public List<Situation> getSituation() {
        return situation;
    }

    public void setSituation(List<Situation> situation) {
        this.situation = situation;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AppRole> getAppRoles() {
        return appRoles;
    }

    public void setAppRoles(List<AppRole> appRoles) {
        this.appRoles = appRoles;
    }

    
}
