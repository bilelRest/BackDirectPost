package tn.poste.myship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tn.poste.myship.sec.entity.AppRole;
import tn.poste.myship.sec.repo.RoleRepository;
import tn.poste.myship.sec.repo.UserRepository;
import tn.poste.myship.sec.service.AppUserService;

@SpringBootApplication
public class MyshipApplication {
@Autowired
	UserRepository userRepository;
@Autowired
	RoleRepository roleRepository;
@Autowired
	AppUserService appUserService;

	public static void main(String[] args) {
		SpringApplication.run(MyshipApplication.class, args);
	}
@Bean
	public CommandLineRunner start(){return args -> {
//	roleRepository.save(new AppRole("ADMIN"));
//	roleRepository.save(new AppRole("CHEF"));
//	roleRepository.save(new AppRole("GUICHET"));
//appUserService.addRoleToUser("guichet","GUICHET");

//appUserService.addRoleToUser("bilel","ADMIN");
//
};}
}
