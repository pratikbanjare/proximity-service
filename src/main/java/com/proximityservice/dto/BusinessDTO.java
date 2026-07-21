package com.proximityservice.dto;

public class BusinessDTO {

    private Long businessId;
    private String businessName;
    private Double latitude;
    private Double longitude;

    public BusinessDTO() {
    }

    public BusinessDTO(Long businessId, String businessName, Double latitude, Double longitude) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
