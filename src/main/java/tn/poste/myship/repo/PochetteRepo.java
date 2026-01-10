package tn.poste.myship.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import tn.poste.myship.entity.Pochette;
public interface PochetteRepo extends JpaRepository<Pochette, Long> {
    Pochette findByTypePochette(String type);

    void deleteByTypePochette(String type);
}