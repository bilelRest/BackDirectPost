package tn.poste.myship.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import tn.poste.myship.sec.entity.AppUser;

import java.time.LocalDate;

@Entity
public class Pochette {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String typePochette;
    private int quantite;
    private Double totalPrice;
    private Boolean deleted=false;
    private final LocalDate createdAt=LocalDate.now();
    @ManyToOne
    @JoinColumn(name = "op_id")
    @JsonBackReference(value = "op-pochette") // <--- Doit correspondre à Operation
    @JsonIgnoreProperties({"parcel", "pochette"})
    private Operation operation;
    @ManyToOne
    private Sender sender;
    @OneToOne
    private AppUser appUser;
    public Double getTotalPrice() {
        return totalPrice;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Pochette(Operation operation, AppUser appUser) {
        this.operation = operation;
        this.appUser = appUser;
    }
    public Pochette(String typePochette, int quantite, Sender sender, Double totalPrice, Operation operation, AppUser appUser) {
        this.typePochette = typePochette;
        this.quantite = quantite;
        this.sender = sender;
        this.totalPrice = totalPrice;
        this.operation = operation;
        this.appUser = appUser;
    }

    public Pochette() {
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Long getId() {
        return id;

    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTypePochette() {
        return typePochette;
    }
    public void setTypePochette(String typePochette) {
        this.typePochette = typePochette;

    }
    public int getQuantite() {
        return quantite;
    }
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    public Sender getSender() {
        return sender;
    }
    public void setSender(Sender sender) {
        this.sender = sender;
    }
    public Double getPrixTotal() {
        return totalPrice;
    }
    public void setPrixTotal(Double prixTotal) {
        this.totalPrice = prixTotal;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}

