package tn.poste.myship.restController;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tn.poste.myship.controller.OperationController;
import tn.poste.myship.dto.DtoParcel;
import tn.poste.myship.dto.Payment;
import tn.poste.myship.entity.*;
import tn.poste.myship.sec.config.JwtUtils;
import tn.poste.myship.sec.entity.AppUser;
import tn.poste.myship.service.PassengerService;
import tn.poste.myship.service.SenderReceiverService;
import tn.poste.myship.service.TrackingService;

import java.util.List;

import static org.springframework.web.servlet.function.ServerResponse.ok;

@RestController
@RequestMapping("/api/operation/passenger")
public class PassengerRestController {
    @Autowired
    PassengerService passengerService;
    @Autowired
    SenderReceiverService senderReceiverService;
    @Autowired
    TrackingService trackingService;
    @Autowired
    JwtUtils jwtUtils;

//    @GetMapping("/parcels")
//    public Operation getParcelByOpId(@RequestParam (value = "op") String op){
//        System.out.println("Formatted Id recu au controleur "+op);
//        return passengerService.getOperationContent(op);
    //}
    @GetMapping("/operations")
    public List<Operation> GetAllOperationByUser(){

        return passengerService.getAllOps();
    }
    @PostMapping("/payment")
    public Operation validerPayment(@RequestParam(value = "op")String op, @RequestBody Payment payment){
        System.out.println("debut de traitement ...");

        Operation operation= passengerService.setValidated(op,payment.banque, payment.cheque);
        if (operation != null){
            return operation;
        }else {
            return null;
        }
    }
    @GetMapping("/parcels")
    public ResponseEntity<?> getParcelByOpId(@RequestParam(value = "op") String op) {
        Operation operation = passengerService.getOperationContent(op);

        // On ne renvoie qu'un String pour tester si le service plante ou si c'est le JSON
        return ResponseEntity.ok(operation);
    }
    @GetMapping("/deleteop")
    public ResponseEntity<?> deleteop(@RequestParam(value = "op")String op){
         passengerService.setCancelled(op);
         return ResponseEntity.ok().build();
    }
    @PostMapping("/deleteparcel")
    public Parcel deleteparcel(@RequestBody Parcel parcel){
        return passengerService.deleteParcelFromCurrentOpeartion(parcel.getTrackingNumber().getFormattedParcelId());
    }
    @PostMapping("/addparcel")
    public Parcel addParcel(@RequestBody Parcel parcel,@RequestParam(value = "op")String op){



      Parcel saved=   passengerService.addParcel(op, parcel);
          passengerService.getOperationContent(op);

return saved;

    }
    @PostMapping("deletepochette")
    public Pochette deletePochett(@RequestBody Pochette pochette){
        return passengerService.deletePochetteFromCurrentOperation(pochette);
    }
    @PostMapping("/addpochette")
    public Operation addPochette(@RequestBody Pochette pochette, @RequestParam(value = "op")String op){



        passengerService.addPochete(op,pochette);
        return passengerService.getOperationContent(op);



    }
    @GetMapping("/new")
    public Operation newOperation(@RequestParam(value = "op", defaultValue = "new") String op) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Auth object: " + auth);
        if (auth != null) {
            System.out.println("Principal type: " + auth.getPrincipal().getClass().getName());
        }
        System.out.println("Operation recu "+op);
        // Si op est "new" ou vide/null, on crée une nouvelle opération
        if (!StringUtils.hasText(op) || "new".equalsIgnoreCase(op)) {
            return passengerService.NewOperation();
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
