package tn.poste.myship.sec.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.poste.myship.sec.entity.AppUser;
import tn.poste.myship.sec.repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Injectez votre UserRepository ici
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username);
        if (appUser == null) throw new UsernameNotFoundException("Utilisateur non trouvé");

        // Conversion de vos AppRole en GrantedAuthority
        List<SimpleGrantedAuthority> authorities = appUser.getAppRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());

        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}