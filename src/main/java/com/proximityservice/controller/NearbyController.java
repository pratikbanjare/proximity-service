package com.proximityservice.controller;

import com.proximityservice.dto.BusinessDTO;
import com.proximityservice.service.IBusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nearby")
public class NearbyController {

    private final IBusinessService businessService;
    private static final String DEFAULT_RADIUS_KM = "5.0";

    public NearbyController(IBusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/search/{latitude}/{longitude}")
    public ResponseEntity<List<BusinessDTO>> searchNearby(
            @PathVariable Double latitude,
            @PathVariable Double longitude,
            @RequestParam(defaultValue = DEFAULT_RADIUS_KM) Double radius) {
        
        List<BusinessDTO> nearbyBusinesses = businessService.searchNearby(latitude, longitude, radius);
        return ResponseEntity.ok(nearbyBusinesses);
    }

}
