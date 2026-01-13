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
    @Column(name = "operation")
    private String operationId; //

    public Pochette() {
    }

    @ManyToOne
    private Sender sender;

    @OneToOne
    @JsonIgnoreProperties({"password", "roles"}) // ← Ignorer les infos sensibles
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

    public Pochette(String typePochette, int quantite, String operationId, Sender sender, Double totalPrice , AppUser appUser) {
        this.typePochette = typePochette;
        this.quantite = quantite;
        this.operationId = operationId;
        this.sender = sender;
        this.totalPrice = totalPrice;
      this.appUser = appUser;
    }

    public Pochette(String operationId) {
        this.operationId = operationId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
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

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}

