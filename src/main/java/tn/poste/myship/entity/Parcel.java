package tn.poste.myship.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import tn.poste.myship.sec.entity.AppUser;

import java.time.LocalDate;

@Entity
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parcelId;
    private LocalDate createdAt=LocalDate.now();
    private Double width;
    private Double height;
    private Double lenght;
    private Double price;
    private Double weight;
    private Boolean deleted = false;
    @ManyToOne
    @JoinColumn(name = "op_id")    
    private Operation operation;
    @OneToOne
    private AppUser appUser;
   // PLUSIEURS colis peuvent aller vers UN SEUL destinataire
    @ManyToOne
    private Receiver receiver;

    // PLUSIEURS colis peuvent être envoyés par UN SEUL expéditeur
    @ManyToOne
    private Sender sender;

    // UN colis a UN SEUL numéro de suivi (Ici OneToOne est correct)
    @OneToOne
    private TrackingNumber trackingNumber;

    public Long getParcelId() {
        return parcelId;
    }

    public void setParcelId(Long parcelId) {
        this.parcelId = parcelId;
    }

    public Parcel() {
    }

    public Parcel(Operation operation,AppUser appUser, Double width, Double height, Double lenght, Double price, Double weight, Receiver receiver, Sender sender, TrackingNumber trackingNumber, Boolean deleted) {
        this.operation=operation; 
        this.deleted=deleted;
        this.width = width;
        this.height = height;
        this.lenght = lenght;
        this.price = price;
        this.weight = weight;
        this.receiver = receiver;
        this.sender = sender;
        this.trackingNumber = trackingNumber;
        this.appUser = appUser;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getLenght() {
        return lenght;
    }

    public void setLenght(Double lenght) {
        this.lenght = lenght;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public TrackingNumber getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(TrackingNumber trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Operation getOperationId() {
        return operation;
    }

    public void setOperationId(Operation operation) {
        this.operation = operation;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
