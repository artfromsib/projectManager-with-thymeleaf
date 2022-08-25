package com.ym.projectManager.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RunAfterStartup {
    private final ParcelService parcelService;

    @Autowired
    public RunAfterStartup(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional(readOnly = false)
    public void runAfterStartup() {
        parcelService.updateParcelsTracking();
    }

}