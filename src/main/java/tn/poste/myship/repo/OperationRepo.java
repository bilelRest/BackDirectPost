package tn.poste.myship.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import tn.poste.myship.entity.Operation;
import tn.poste.myship.entity.Parcel;
import tn.poste.myship.sec.entity.AppUser;

import java.util.List;

public interface OperationRepo extends JpaRepository<Operation, Long>{

    Operation findByFormattedId(String operationId);

    Operation findByFormattedIdAndAppUser(String formattedId, AppUser appUser);

    List<Operation> findByAppUser(AppUser appUser);
//@Query("DELETE FROM Operation o WHERE o.parcel.trackingNumber.formattedParcelId = :track")
//    void deleteParcelFromCurrentOperation(String track);
}
