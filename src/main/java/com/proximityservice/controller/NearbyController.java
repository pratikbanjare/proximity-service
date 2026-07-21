package com.proximityservice.controller;

import com.proximityservice.dto.BusinessDTO;
import com.proximityservice.service.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nearby")
public class NearbyController {

    private final BusinessService businessService;
    private static final double DEFAULT_RADIUS_KM = 5.0;

    public NearbyController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/search/{latitude}/{longitude}")
    public ResponseEntity<List<BusinessDTO>> searchNearby(
            @PathVariable Double latitude,
            @PathVariable Double longitude,
            @RequestParam(defaultValue = "5.0") Double radius) {
        
        List<BusinessDTO> nearbyBusinesses = businessService.searchNearby(latitude, longitude, radius);
        return ResponseEntity.ok(nearbyBusinesses);
    }

}
