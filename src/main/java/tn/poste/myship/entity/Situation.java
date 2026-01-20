package tn.poste.myship.entity;

import jakarta.persistence.*;
import tn.poste.myship.sec.entity.AppUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Situation {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "situation", referencedColumnName = "id")
private List<Operation> operations=new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id") // This creates the foreign key in the 'situation' table
    private AppUser appUser;
    private String agence;
private Double total;
private final LocalDate date=LocalDate.now();

    public Situation() {
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Situation(Long id, AppUser appUser , List<Operation> list, String agence, Double total) {
        this.id = id;
        this.operations=list;
        this.appUser = appUser;
        this.agence = agence;
        this.total = total;
    }

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }


    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public LocalDate getDate() {
        return date;
    }
}
