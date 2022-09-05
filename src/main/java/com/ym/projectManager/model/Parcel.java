package com.ym.projectManager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ym.projectManager.model.comparator.TrackParcelComparator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(name = "parcel")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Parcel {
    @Id
    @Column(name = "parcel_id", nullable = false)
    @SequenceGenerator(name = "parcel_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parcel_id_seq")
    private Long parcelId;
    private String trackNumber;
    private String lastStatus;

    @OneToMany(mappedBy = "parcel", fetch = FetchType.LAZY)
    private Set<TrackParcel> trackParcels;

    private Boolean delivered = false;
    private Date lastUpdate = new Date();

    @OneToMany(mappedBy = "parcel", fetch = FetchType.LAZY)
    private Set<Order> parcelOrders = new HashSet<>();

    public Parcel() {
    }

    public Parcel(String trackNumber) {
        this.trackNumber = trackNumber;
    }


    public Parcel(Long parcelId, String trackNumber, String lastStatus, Set<TrackParcel> trackParcels, Boolean delivered, Date lastUpdate, Set<Order> parcelOrders) {
        this.parcelId = parcelId;
        this.trackNumber = trackNumber;
        this.lastStatus = lastStatus;
        this.trackParcels = trackParcels;
        this.delivered = delivered;
        this.lastUpdate = lastUpdate;
        this.parcelOrders = parcelOrders;
    }

    public Parcel(String trackNumber, String lastStatus, Date lastUpdate, boolean delivered) {
        this.trackNumber = trackNumber;
        this.lastStatus = lastStatus;
        this.lastUpdate = lastUpdate;
        this.delivered = delivered;
    }

    public Parcel(Long parcelId, String trackNumber) {
        this.parcelId = parcelId;
        this.trackNumber = trackNumber;
    }

    public Long getParcelId() {
        return parcelId;
    }

    public void setParcelId(Long id) {
        this.parcelId = id;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Set<TrackParcel> getTrackParcels() {
        return this.trackParcels.stream().sorted(new TrackParcelComparator().reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void setTrackParcels(Set<TrackParcel> trackParcels) {
        this.trackParcels = trackParcels;
    }

    public Set<Order> getParcelOrders() {
        return parcelOrders;
    }

    public void setParcelOrders(Set<Order> parcelOrders) {
        this.parcelOrders = parcelOrders;
    }


    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }
}
