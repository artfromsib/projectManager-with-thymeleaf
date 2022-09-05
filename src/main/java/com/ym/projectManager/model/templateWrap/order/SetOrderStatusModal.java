package com.ym.projectManager.model.templateWrap.order;

public class SetOrderStatusModal {

    private Long orderId;
    private String status;
    private String trackNum;

    public SetOrderStatusModal(Long orderId, String status, String trackNum) {
        this.orderId = orderId;
        this.status = status;
        this.trackNum = trackNum;
    }

    public SetOrderStatusModal() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrackNum() {
        return trackNum;
    }

    public void setTrackNum(String trackNum) {
        this.trackNum = trackNum;
    }
}
