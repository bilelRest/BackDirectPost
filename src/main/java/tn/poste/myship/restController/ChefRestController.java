package tn.poste.myship.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.poste.myship.entity.Situation;
import tn.poste.myship.service.CherService;

import java.util.List;

@RestController
@RequestMapping("/api/chef")
@PreAuthorize("hasRole('CHEF')")
public class ChefRestController {
    @Autowired
    CherService cherService;
@GetMapping("/situationchef")
    public List<Situation> situationChef(){
    System.out.println("Extracted user "+cherService.extractUser().getAppRoles());
    return cherService.chefSituation();
}
@GetMapping("setclotured")
    public List<Situation> setClotured(){
    return cherService.chefClotured();
}
}
