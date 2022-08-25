package com.ym.projectManager.repository;

import com.ym.projectManager.model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {
    Optional<Parcel>  findFirstByDeliveredIsFalseAndTrackNumberIsNotNullOrderByLastUpdateDesc();
    Set<Parcel> findAllByDeliveredIsFalse();
    Parcel findFirstByTrackNumber(String trackNum);


}
