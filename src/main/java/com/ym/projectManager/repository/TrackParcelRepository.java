package com.ym.projectManager.repository;

import com.ym.projectManager.model.TrackParcel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TrackParcelRepository extends JpaRepository<TrackParcel, Long> {

    TrackParcel findFirstByParcelOrderByTrackParcelIdDesc(Long id);
    Set<TrackParcel> findAllByParcelOrderByTrackParcelIdDesc(Long id);
    Set<TrackParcel> findAllByParcelParcelIdOrderByTrackParcelIdDesc(Long id);




}
