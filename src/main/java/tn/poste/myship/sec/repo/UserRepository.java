package tn.poste.myship.sec.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.poste.myship.sec.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser,Long> {
    AppUser findByUsername(String username);
}
