package com.proximityservice.service;

import com.proximityservice.constants.ApplicationConstants;
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
public class BusinessService implements IBusinessService {

    private final BusinessRepository businessRepository;
    private final GeohashRepository geohashRepository;
    private final IGeohashGenerator geohashGenerator;
    private final IDistanceCalculator distanceCalculator;

    public BusinessService(BusinessRepository businessRepository,
                           GeohashRepository geohashRepository,
                           IGeohashGenerator geohashGenerator,
                           IDistanceCalculator distanceCalculator) {
        this.businessRepository = businessRepository;
        this.geohashRepository = geohashRepository;
        this.geohashGenerator = geohashGenerator;
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public BusinessDTO getBusinessById(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new IllegalArgumentException(ApplicationConstants.BUSINESS_NOT_FOUND_MESSAGE + businessId));
        return convertToDTO(business);
    }

    @Override
    public BusinessDTO addBusiness(BusinessDTO businessDTO) {
        Business business = convertToEntity(businessDTO);
        Business savedBusiness = businessRepository.save(business);

        String geohashValue = geohashGenerator.generate(
                savedBusiness.getLatitude(),
                savedBusiness.getLongitude()
        );

        Geohash geohash = Geohash.builder()
                .businessId(savedBusiness.getBusinessId())
                .geohashValue(geohashValue)
                .build();
        geohashRepository.save(geohash);

        return convertToDTO(savedBusiness);
    }

    @Override
    public BusinessDTO updateBusiness(Long businessId, BusinessDTO businessDTO) {
        Business existingBusiness = businessRepository.findById(businessId)
                .orElse(null);

        if (existingBusiness != null) {
            existingBusiness.setBusinessName(businessDTO.getBusinessName());
            existingBusiness.setLatitude(businessDTO.getLatitude());
            existingBusiness.setLongitude(businessDTO.getLongitude());
            Business updatedBusiness = businessRepository.save(existingBusiness);

            String newGeohashValue = geohashGenerator.generate(
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

    @Override
    public void deleteBusiness(Long businessId) {
        if (!businessRepository.existsById(businessId)) {
            throw new IllegalArgumentException(ApplicationConstants.BUSINESS_NOT_FOUND_MESSAGE + businessId);
        }

        List<Geohash> geohashes = geohashRepository.findByBusinessId(businessId);
        geohashRepository.deleteAll(geohashes);

        businessRepository.deleteById(businessId);
    }

    @Override
    public List<BusinessDTO> searchNearby(Double latitude, Double longitude, double radiusKm) {
        List<Business> allBusinesses = businessRepository.findAll();

        return allBusinesses.stream()
                .filter(business -> distanceCalculator.calculate(
                        latitude,
                        longitude,
                        business.getLatitude(),
                        business.getLongitude()
                ) <= radiusKm)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BusinessDTO convertToDTO(Business business) {
        return BusinessDTO.builder()
                .businessId(business.getBusinessId())
                .businessName(business.getBusinessName())
                .latitude(business.getLatitude())
                .longitude(business.getLongitude())
                .build();
    }

    private Business convertToEntity(BusinessDTO businessDTO) {
        return Business.builder()
                .businessId(businessDTO.getBusinessId())
                .businessName(businessDTO.getBusinessName())
                .latitude(businessDTO.getLatitude())
                .longitude(businessDTO.getLongitude())
                .build();
    }

}
