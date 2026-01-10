package tn.poste.myship.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class TrackingNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parcelId;

    private LocalDate createdAt = LocalDate.now();

    // On stocke l'ID formaté en base pour des recherches ultra-rapides
    @Column(unique = true)
    private String formattedParcelId;

    // Constructeur par défaut requis par JPA
    public TrackingNumber() {}

    /**
     * Cette méthode est le "cerveau" de l'automatisation.
     * Elle s'exécute automatiquement APRES que l'ID a été généré par la DB.
     */
    @PostPersist
    public void updateFormattedId() {
        if (this.parcelId != null) {
            this.formattedParcelId = "EN" + String.format("%09d", this.parcelId) + "TN";
        }
    }

    // --- GETTERS ET SETTERS ---

    public Long getParcelId() {
        return parcelId;
    }

    public void setParcelId(Long parcelId) {
        this.parcelId = parcelId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedParcelId() {
        return formattedParcelId;
    }

    // On laisse le setter au cas où, mais l'automatisation @PostPersist prime
    public void setFormattedParcelId(String formattedParcelId) {
        this.formattedParcelId = formattedParcelId;
    }
}