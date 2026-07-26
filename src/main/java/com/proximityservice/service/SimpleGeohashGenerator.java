package com.proximityservice.service;

import com.proximityservice.constants.ApplicationConstants;
import org.springframework.stereotype.Component;

@Component
public class SimpleGeohashGenerator implements IGeohashGenerator {

    @Override
    public String generate(Double latitude, Double longitude) {
        return String.format(ApplicationConstants.GEOHASH_FORMAT, latitude, longitude);
    }
}
