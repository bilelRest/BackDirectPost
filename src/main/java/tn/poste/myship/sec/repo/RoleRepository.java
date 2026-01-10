package tn.poste.myship.sec.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.poste.myship.sec.entity.AppRole;

import java.beans.JavaBean;

public interface RoleRepository extends JpaRepository<AppRole,Long> {
}
