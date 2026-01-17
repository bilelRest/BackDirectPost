package tn.poste.myship.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import tn.poste.myship.sec.entity.AppUser;

@Entity
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long opId;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "operation", referencedColumnName = "formattedId")
    private List<Parcel> parcel;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "operation", referencedColumnName = "formattedId")
    private List<Pochette> pochette;

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @ManyToOne // Changé de @OneToOne à @ManyToOne
    @JoinColumn(name = "app_user_user_id", nullable = false)
    private AppUser appUser;
    private String formattedId;
    private Boolean validated = false;
    private Boolean cancelled = false;
    private LocalDate createdAt;
    private Boolean deleted=false;
    private String banque;
    private String cheque;
    private Boolean closed=false;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    private Double total;

    /**
     * S'exécute AVANT l'insertion en base de données.
     * On initialise la date ici pour garantir qu'elle n'est pas nulle lors du calcul de l'ID formaté.
     */
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDate.now();
        }
    }

    public String getBanque() {
        return banque;
    }

    public void setBanque(String banque) {
        this.banque = banque;
    }

    public String getCheque() {
        return cheque;
    }

    public void setCheque(String cheque) {
        this.cheque = cheque;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    /**
     * S'exécute APRES l'insertion. L'ID (opId) est maintenant disponible.
     */
    @PostPersist
    public void generateFormattedIdAfterPersist() {
        updateFormattedIdLogic();
    }

    /**
     * S'exécute lors du chargement depuis la base.
     */
    @PostLoad
    public void onPostLoad() {
        updateFormattedIdLogic();
    }

    /**
     * Logique centrale de formatage pour éviter la duplication de code et les NPE.
     */
    private void updateFormattedIdLogic() {
        if (this.opId != null && this.createdAt != null) {
            int yearTwoDigits = this.createdAt.getYear() % 100;
            String month = String.format("%02d", this.createdAt.getMonthValue());
            // Format: OP-0000000001MMYY
            this.formattedId = String.format("OP-%010d%s%02d", this.opId, month, yearTwoDigits);
        }
    }

    // --- CONSTRUCTEURS ---
    public Operation() {
        // Initialisation par défaut pour éviter les listes nulles
        this.parcel = new ArrayList<>();
        this.pochette = new ArrayList<>();
    }

    public Operation(Boolean deleted, List<Parcel> parcel, List<Pochette> pochette, AppUser appUser, String banque, String cheque, Boolean closed, Double total) {
        this.parcel = parcel;
        this.deleted=deleted;
        this.pochette = pochette;
        this.appUser = appUser;
        this.banque = banque;
        this.cheque = cheque;
        this.closed = closed;
        this.createdAt = LocalDate.now();
        this.total=total;
    }

    // --- GETTERS ET SETTERS ---
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}