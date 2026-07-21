package com.proximityservice.service;

import com.proximityservice.dto.BusinessDTO;
import com.proximityservice.entity.Business;
import com.proximityservice.entity.Geohash;
import com.proximityservice.repository.BusinessRepository;
import com.proximityservice.repository.GeohashRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final GeohashRepository geohashRepository;

    public BusinessService(BusinessRepository businessRepository, GeohashRepository geohashRepository) {
        this.businessRepository = businessRepository;
        this.geohashRepository = geohashRepository;
    }

    public BusinessDTO getBusinessById(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new IllegalArgumentException("Business not found with ID: " + businessId));
        return convertToDTO(business);
    }

    public BusinessDTO addBusiness(BusinessDTO businessDTO) {
        Business business = convertToEntity(businessDTO);
        Business savedBusiness = businessRepository.save(business);
        
        // Generate and store geohash (simple encoding of coordinates)
        String geohashValue = generateSimpleGeohash(
                savedBusiness.getLatitude(),
                savedBusiness.getLongitude()
        );
        
        Geohash geohash = new Geohash();
        geohash.setBusinessId(savedBusiness.getBusinessId());
        geohash.setGeohashValue(geohashValue);
        geohashRepository.save(geohash);
        
        return convertToDTO(savedBusiness);
    }

    public BusinessDTO updateBusiness(Long businessId, BusinessDTO businessDTO) {
        Business existingBusiness = businessRepository.findById(businessId)
                .orElse(null);
        
        if (existingBusiness != null) {
            existingBusiness.setBusinessName(businessDTO.getBusinessName());
            existingBusiness.setLatitude(businessDTO.getLatitude());
            existingBusiness.setLongitude(businessDTO.getLongitude());
            Business updatedBusiness = businessRepository.save(existingBusiness);
            
            // Update geohash if location changed
            String newGeohashValue = generateSimpleGeohash(
                    updatedBusiness.getLatitude(),
                    updatedBusiness.getLongitude()
            );
            
            List<Geohash> geohashes = geohashRepository.findByBusinessId(businessId);
            if (!geohashes.isEmpty()) {
                geohashes.get(0).setGeohashValue(newGeohashValue);
                geohashRepository.save(geohashes.get(0));
            }
            
            return convertToDTO(updatedBusiness);
        } else {
            return addBusiness(businessDTO);
        }
    }

    public void deleteBusiness(Long businessId) {
        if (!businessRepository.existsById(businessId)) {
            throw new IllegalArgumentException("Business not found with ID: " + businessId);
        }
        
        // Delete associated geohash entries
        List<Geohash> geohashes = geohashRepository.findByBusinessId(businessId);
        geohashRepository.deleteAll(geohashes);
        
        // Delete business
        businessRepository.deleteById(businessId);
    }

    public List<BusinessDTO> searchNearby(Double latitude, Double longitude, double radiusKm) {
        // Get all businesses and filter by distance
        List<Business> allBusinesses = businessRepository.findAll();
        
        return allBusinesses.stream()
                .filter(business -> calculateDistance(
                        latitude,
                        longitude,
                        business.getLatitude(),
                        business.getLongitude()
                ) <= radiusKm)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Calculate distance between two geographic points using Haversine formula
     * Returns distance in kilometers
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Generate a simple geohash by combining latitude and longitude into a string
     */
    private String generateSimpleGeohash(Double latitude, Double longitude) {
        // Simple geohash representation: lat_lon with fixed precision
        return String.format("%.2f_%.2f", latitude, longitude);
    }

    private BusinessDTO convertToDTO(Business business) {
        return new BusinessDTO(
                business.getBusinessId(),
                business.getBusinessName(),
                business.getLatitude(),
                business.getLongitude()
        );
    }

    private Business convertToEntity(BusinessDTO businessDTO) {
        Business business = new Business();
        business.setBusinessId(businessDTO.getBusinessId());
        business.setBusinessName(businessDTO.getBusinessName());
        business.setLatitude(businessDTO.getLatitude());
        business.setLongitude(businessDTO.getLongitude());
        return business;
    }

}
