package tn.poste.myship.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.poste.myship.entity.Situation;
import tn.poste.myship.sec.entity.AppUser;

import java.time.LocalDate;
import java.util.List;

public interface SituationRepo extends JpaRepository<Situation,Long> {
   List< Situation> findByAppUser(AppUser appUser);

    List<Situation> findByAgenceAndDate(String agence, LocalDate now);
}
