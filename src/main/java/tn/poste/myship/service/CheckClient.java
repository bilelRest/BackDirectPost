package tn.poste.myship.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tn.poste.myship.entity.Receiver;
import tn.poste.myship.entity.Sender;
import tn.poste.myship.repo.ReceiverRepo;
import tn.poste.myship.repo.SenderRepo;
@Service
@Transactional
public class CheckClient {
    @Autowired
    SenderRepo senderRepo;
    @Autowired
    ReceiverRepo receiverRepo;

    public Sender checkSender(Sender sender) {
        // Recherche par téléphone pour vérifier l'existence
        Sender existingSender = senderRepo.findBySendTel(sender.getSendTel());

        if (existingSender != null) {
            // Comparaison et Mise à jour dynamique
            updateSenderFields(existingSender, sender);
            // Grâce à @Transactional, la mise à jour est automatique en fin de méthode
            return senderRepo.save(existingSender);
        } else {
            // CRITIQUE : Sécurité pour éviter l'erreur Sender#0
            sender.setSendId(null);
            return senderRepo.save(sender);
        }
    }

    public Receiver checkReceiver(Receiver receiver) {
        // Recherche par téléphone
        Receiver existingReceiver = receiverRepo.findByRecTel(receiver.getRecTel());

        if (existingReceiver != null) {
            // Comparaison et Mise à jour dynamique
            updateReceiverFields(existingReceiver, receiver);
            return receiverRepo.save(existingReceiver);
        } else {
            // CRITIQUE : Sécurité pour éviter l'erreur Receiver#0
            receiver.setRecId(null);
            return receiverRepo.save(receiver);
        }
    }

    private void updateSenderFields(Sender target, Sender source) {
        if (source.getAdress() != null) target.setAdress(source.getAdress());
        if (source.getCity() != null) target.setCity(source.getCity());
        if (source.getPostalCode() != null) target.setPostalCode(source.getPostalCode());
        if (source.getSendEmail() != null) target.setSendEmail(source.getSendEmail());
        if (source.getCountry() != null) target.setCountry(source.getCountry());
        if (source.getSendName() != null) target.setSendName(source.getSendName());
        if (source.getSendSocialReason() != null) target.setSendSocialReason(source.getSendSocialReason());
    }

    private void updateReceiverFields(Receiver target, Receiver source) {
        if (source.getAdress() != null) target.setAdress(source.getAdress());
        if (source.getCity() != null) target.setCity(source.getCity());
        if (source.getPostalCode() != null) target.setPostalCode(source.getPostalCode());
        if (source.getRecEmail() != null) target.setRecEmail(source.getRecEmail());
        if (source.getCountry() != null) target.setCountry(source.getCountry());
        if (source.getRecName() != null) target.setRecName(source.getRecName());
        if (source.getRecSocialReason() != null) target.setRecSocialReason(source.getRecSocialReason());
    }
}