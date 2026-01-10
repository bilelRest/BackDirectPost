package tn.poste.myship.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;

@Entity
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long opId;

    @OneToMany(cascade = CascadeType.ALL) // Garde votre structure simple
    private List<Parcel> parcel;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Pochette> pochette;

    private String formattedId;
    private Boolean validated = false;
    private Boolean cancelled = false;
    private LocalDate createdAt = LocalDate.now();

    // --- LA CORRECTION EST ICI ---
    @PostPersist // S'exécute juste après le save() en base (quand l'ID existe)
    @PostLoad    // S'exécute quand on récupère l'objet de la base
    public void generateFormattedId() {
        if (this.opId != null) {
            int yearTwoDigits = this.createdAt.getYear() % 100;
            String month = String.format("%02d", this.createdAt.getMonthValue());
            // On remplit le champ formattedId avec l'ID enfin généré
            this.formattedId = String.format("OP-%010d%s%02d", this.opId, month, yearTwoDigits);
        }
    }

    public Operation() {}

    public Operation(List<Parcel> parcel, List<Pochette> pochette) {
        this.parcel = parcel;
        this.pochette = pochette;
    }

    // Getters et Setters standards
    public Long getOpId() { return opId; }
    public void setOpId(Long opId) { this.opId = opId; }

    public List<Parcel> getParcel() { return parcel; }
    public void setParcel(List<Parcel> parcel) { this.parcel = parcel; }

    public List<Pochette> getPochette() { return pochette; }
    public void setPochette(List<Pochette> pochette) { this.pochette = pochette; }

    public String getFormattedId() { return formattedId; }
    public void setFormattedId(String formattedId) { this.formattedId = formattedId; }

    public Boolean getValidated() { return validated; }
    public void setValidated(Boolean validated) { this.validated = validated; }

    public Boolean getCancelled() { return cancelled; }
    public void setCancelled(Boolean cancelled) { this.cancelled = cancelled; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}