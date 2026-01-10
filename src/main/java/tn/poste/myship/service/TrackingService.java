package tn.poste.myship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.poste.myship.entity.TrackingNumber;
import tn.poste.myship.repo.TrackingNumberRepo;

@Service
public class TrackingService {
    @Autowired
    TrackingNumberRepo trackingNumberRepo;
    public String generateTrackingNumber(){
        TrackingNumber trackingNumber=new TrackingNumber();
        trackingNumberRepo.save(trackingNumber);
        return trackingNumber.getFormattedParcelId();
    }
}
