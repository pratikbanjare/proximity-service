package com.proximityservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "geohash")
public class Geohash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "geohash_id")
    private Long geohashId;

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "geohash_value")
    private String geohashValue;

    public Geohash() {
    }

    public Geohash(Long geohashId, Long businessId, String geohashValue) {
        this.geohashId = geohashId;
        this.businessId = businessId;
        this.geohashValue = geohashValue;
    }

    public Long getGeohashId() {
        return geohashId;
    }

    public void setGeohashId(Long geohashId) {
        this.geohashId = geohashId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getGeohashValue() {
        return geohashValue;
    }

    public void setGeohashValue(String geohashValue) {
        this.geohashValue = geohashValue;
    }
}
