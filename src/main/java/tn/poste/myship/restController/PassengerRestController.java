package tn.poste.myship.restController;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tn.poste.myship.controller.OperationController;
import tn.poste.myship.dto.DtoParcel;
import tn.poste.myship.dto.Payment;
import tn.poste.myship.entity.*;
import tn.poste.myship.service.PassengerService;
import tn.poste.myship.service.SenderReceiverService;
import tn.poste.myship.service.TrackingService;

import java.util.List;

@RestController
@RequestMapping("/api/operation/passenger")
public class PassengerRestController {
    @Autowired
    PassengerService passengerService;
    @Autowired
    SenderReceiverService senderReceiverService;
    @Autowired
    TrackingService trackingService;

//    @GetMapping("/parcels")
//    public Operation getParcelByOpId(@RequestParam (value = "op") String op){
//        System.out.println("Formatted Id recu au controleur "+op);
//        return passengerService.getOperationContent(op);
    //}
    @PostMapping("/payment")
    public ResponseEntity<?> validerPayment(@RequestParam(value = "op")String op,@RequestBody Payment payment){

        return ResponseEntity.ok( passengerService.setValidated(op,payment.banque, payment.cheque));
    }
    @GetMapping("/parcels")
    public ResponseEntity<?> getParcelByOpId(@RequestParam(value = "op") String op) {
        Operation operation = passengerService.getOperationContent(op);
        // On ne renvoie qu'un String pour tester si le service plante ou si c'est le JSON
        return ResponseEntity.ok(operation);
    }
    @PostMapping("/addparcel")
    public Parcel addParcel(@RequestBody Parcel parcel,@RequestParam(value = "op")String op){

        parcel.setTrackingNumber(trackingService.generateTrackingNumber());

      Parcel saved=   passengerService.addParcel(op, parcel);
          passengerService.getOperationContent(op);

return saved;

    }
    @PostMapping("/addpochette")
    public Operation addParcel(@RequestBody Pochette pochette, @RequestParam(value = "op")String op){



        passengerService.addPochete(op,pochette);
        return passengerService.getOperationContent(op);



    }
    @GetMapping("/new")
    public Operation newOperation(@RequestParam(value = "op", defaultValue = "new") String op) {
        System.out.println("Operation recu "+op);
        // Si op est "new" ou vide/null, on crée une nouvelle opération
        if (!StringUtils.hasText(op) || "new".equalsIgnoreCase(op)) {
            return passengerService.NewOpeartion();
        }

        // Sinon, on récupère le contenu de l'opération existante
        return passengerService.getOperationContent(op);
    }

    @GetMapping("/senders")
    public List<Sender> findAllSenders(){
        return senderReceiverService.getSenderList();
    }
    @GetMapping("/receivers")
    public List<Receiver> findAllReceivers(){
        return senderReceiverService.getReceiverList();
    }

}
