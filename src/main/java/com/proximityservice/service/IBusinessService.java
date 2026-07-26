package com.proximityservice.service;

import com.proximityservice.dto.BusinessDTO;

import java.util.List;

public interface IBusinessService {
    BusinessDTO getBusinessById(Long businessId);

    BusinessDTO addBusiness(BusinessDTO businessDTO);

    BusinessDTO updateBusiness(Long businessId, BusinessDTO businessDTO);

    void deleteBusiness(Long businessId);

    List<BusinessDTO> searchNearby(Double latitude, Double longitude, double radiusKm);
}
