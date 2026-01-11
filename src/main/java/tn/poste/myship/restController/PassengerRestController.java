package tn.poste.myship.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.poste.myship.controller.OperationController;
import tn.poste.myship.entity.Operation;
import tn.poste.myship.service.PassengerService;

@RestController
@RequestMapping("/api/operation/passenger")
public class PassengerRestController {
    @Autowired
    PassengerService passengerService;
    @GetMapping("/new")
    public Operation newOperation(){
        return passengerService.NewOpeartion();
    }

}
