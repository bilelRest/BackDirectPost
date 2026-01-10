package tn.poste.myship.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import tn.poste.myship.entity.Operation;
import tn.poste.myship.entity.Parcel;

public interface OperationRepo extends JpaRepository<Operation, Long>{

    Operation findByFormattedId(String operationId);
//@Query("DELETE FROM Operation o WHERE o.parcel.trackingNumber.formattedParcelId = :track")
//    void deleteParcelFromCurrentOperation(String track);
}
