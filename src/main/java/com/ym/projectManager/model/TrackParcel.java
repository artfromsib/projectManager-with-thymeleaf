package com.ym.projectManager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "track_parcel")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TrackParcel implements Comparable<TrackParcel>{
    @Id
    @Column( nullable = false)
    @SequenceGenerator(name = "track_parcel_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "track_parcel_id_seq")
    private Long trackParcelId;
    private String status = "";
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parcel_id")
    private Parcel parcel;

    public TrackParcel(Long trackParcelId, String status, Date date) {
        this.trackParcelId = trackParcelId;
        this.status = status;
        this.date = date;
    }

    public TrackParcel() {
    }

    public TrackParcel(String status, Date date) {
        this.status = status;
        this.date = date;

    }

    public Long getTrackParcelId() {
        return trackParcelId;
    }

    public void setTrackParcelId(Long id) {
        this.trackParcelId = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    @Override
    public int compareTo(TrackParcel o) {
        return this.getDate().compareTo(o.getDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackParcel that = (TrackParcel) o;

        return status.equals(that.status) && date.getTime()==(that.date.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, date);
    }
}
