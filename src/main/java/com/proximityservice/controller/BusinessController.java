package com.proximityservice.controller;

import com.proximityservice.dto.BusinessDTO;
import com.proximityservice.service.IBusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/business")
public class BusinessController {

    private final IBusinessService businessService;

    public BusinessController(IBusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessDTO> getBusinessById(@PathVariable Long id) {
        try {
            BusinessDTO business = businessService.getBusinessById(id);
            return ResponseEntity.ok(business);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<BusinessDTO> addBusiness(@PathVariable Long id, @RequestBody BusinessDTO businessDTO) {
        businessDTO.setBusinessId(id);
        BusinessDTO savedBusiness = businessService.addBusiness(businessDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBusiness);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessDTO> updateBusiness(@PathVariable Long id, @RequestBody BusinessDTO businessDTO) {
        businessDTO.setBusinessId(id);
        BusinessDTO updatedBusiness = businessService.updateBusiness(id, businessDTO);
        return ResponseEntity.ok(updatedBusiness);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable Long id) {
        try {
            businessService.deleteBusiness(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
