package com.ym.projectManager.repository;

import com.ym.projectManager.model.TrackParcel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TrackParcelRepository extends JpaRepository<TrackParcel, Long> {
    //TrackParcel findFirstByParcelIdOrderByIdDesc(Long id);
    TrackParcel findFirstByParcelOrderByTrackParcelIdDesc(Long id);
    //Set<TrackParcel> findAllByParcelIdOrderByIdDesc(Long id);
    Set<TrackParcel> findAllByParcelOrderByTrackParcelIdDesc(Long id);


}
