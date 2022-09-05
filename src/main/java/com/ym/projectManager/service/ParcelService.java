package com.ym.projectManager.service;

import com.ym.projectManager.model.Parcel;

public interface ParcelService {

    Parcel getParcel(long id);

    void updateParcelsTracking();

}
