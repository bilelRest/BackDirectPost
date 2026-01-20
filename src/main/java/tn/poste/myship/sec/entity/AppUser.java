package tn.poste.myship.sec.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)    private String password;
    private String nomPrenom;
    private String email;
    @OneToMany(fetch = FetchType.EAGER)
    List<AppRole> appRoles;
    private String agence;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.appRoles.stream()
                .map(role -> {
                    String name = role.getRoleName();
                    // Si le nom ne commence pas par ROLE_, on l'ajoute
                    if (!name.startsWith("ROLE_")) {
                        name = "ROLE_" + name;
                    }
                    return new SimpleGrantedAuthority(name);
                })
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

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    @Override
    public boolean isEnabled() { return true; }
    public AppUser() {
    }
    public AppUser(String username, String password, String nomPrenom, String email, List<AppRole> appRoles, String agence, List<Situation> situation) {
        this.username = username;
        this.password = password;
        this.nomPrenom = nomPrenom;
        this.email = email;
        this.appRoles = appRoles;
        this.agence = agence;
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


    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }
}
