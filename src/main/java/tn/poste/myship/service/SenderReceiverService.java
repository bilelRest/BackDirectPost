package tn.poste.myship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.poste.myship.entity.Receiver;
import tn.poste.myship.entity.Sender;
import tn.poste.myship.repo.ReceiverRepo;
import tn.poste.myship.repo.SenderRepo;

import java.util.List;

@Service
public class SenderReceiverService {
    @Autowired
    SenderRepo senderRepo;
    @Autowired
    ReceiverRepo receiverRepo;
    public List<Sender> getSenderList(){
        return senderRepo.findAll();
    }
    public List<Receiver> getReceiverList(){
        return receiverRepo.findAll();
    }
}
