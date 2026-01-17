package tn.poste.myship.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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
    private Boolean normal=true;

    public Parcel() {
    }


    @Column(name = "operation")
    private String operationId; //

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Receiver receiver;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Sender sender;

    @OneToOne(cascade = CascadeType.ALL)
    private TrackingNumber trackingNumber;

    public Long getParcelId() {
        return parcelId;
    }

    public void setParcelId(Long parcelId) {
        this.parcelId = parcelId;
    }



    public Parcel(Boolean normal, Double width, Double height, Double lenght, Double price, Double weight, Receiver receiver, Sender sender, TrackingNumber trackingNumber, Boolean deleted, String operationId) {
        this.deleted=deleted;
        this.normal=normal;
        this.width = width;
       // this.normal=normal;
        this.height = height;
        this.lenght = lenght;
        this.price = price;
        this.weight = weight;
        this.receiver = receiver;
        this.sender = sender;
        this.trackingNumber = trackingNumber;
        this.operationId = operationId;
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





    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }


    public Boolean getNormal() {
        return normal;
    }

    public void setNormal(Boolean normal) {
        this.normal = normal;
    }
}
