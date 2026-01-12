package tn.poste.myship.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import tn.poste.myship.entity.Receiver;
import tn.poste.myship.entity.Sender;
import tn.poste.myship.entity.TrackingNumber;

public class DtoParcel {
    private Double width;
    private Double height;
    private Double lenght;
    private Double price;
    private Double weight;
    private Receiver receiver;
    private Sender sender;
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String trackingNumber;
    private String opIdDto;

    public DtoParcel(Double price, Receiver receiver, Sender sender, String opIdDto, Double weight, Double lenght, Double height, Double width) {
        this.price = price;
        this.receiver = receiver;
        this.sender = sender;
        this.opIdDto = opIdDto;
        this.weight = weight;
        this.lenght = lenght;
        this.height = height;
        this.width = width;
    }

    public DtoParcel() {
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

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getOpIdDto() {
        return opIdDto;
    }

    public void setOpIdDto(String opIdDto) {
        this.opIdDto = opIdDto;
    }

    public DtoParcel(Double width, Double height, Double lenght, Double price, Double weight, Receiver receiver, Sender sender, String trackingNumber, String opIdDto) {
        this.width = width;
        this.height = height;
        this.lenght = lenght;
        this.price = price;
        this.weight = weight;
        this.receiver = receiver;
        this.sender = sender;
        this.trackingNumber = trackingNumber;
        this.opIdDto = opIdDto;
    }
}
