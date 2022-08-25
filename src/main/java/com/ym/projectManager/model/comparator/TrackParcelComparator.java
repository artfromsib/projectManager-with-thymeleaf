package com.ym.projectManager.model.comparator;

import com.ym.projectManager.model.TrackParcel;

import java.util.Comparator;

public class TrackParcelComparator implements Comparator<TrackParcel> {

    @Override
    public int compare(TrackParcel o1, TrackParcel o2) {
        return o1.getDate().compareTo(o2.getDate());
    }

    @Override
    public Comparator<TrackParcel> reversed() {
        return Comparator.super.reversed();
    }
}
