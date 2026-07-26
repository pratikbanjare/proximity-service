package com.proximityservice.initializer;

import com.proximityservice.constants.ApplicationConstants;
import com.proximityservice.entity.Business;
import com.proximityservice.entity.Geohash;
import com.proximityservice.repository.BusinessRepository;
import com.proximityservice.repository.GeohashRepository;
import com.proximityservice.service.IGeohashGenerator;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class DataInitializer implements ApplicationRunner {

    private final BusinessRepository businessRepository;
    private final GeohashRepository geohashRepository;
    private final IGeohashGenerator geohashGenerator;

    public DataInitializer(BusinessRepository businessRepository,
                           GeohashRepository geohashRepository,
                           IGeohashGenerator geohashGenerator) {
        this.businessRepository = businessRepository;
        this.geohashRepository = geohashRepository;
        this.geohashGenerator = geohashGenerator;
    }

    @Override
    public void run(org.springframework.boot.ApplicationArguments args) {
        // Only initialize if database is empty
        if (businessRepository.count() == 0) {
            initializeDummyData();
        }
    }

    private void initializeDummyData() {
        ClassPathResource resource = new ClassPathResource(ApplicationConstants.INITIAL_DATASET_RESOURCE_PATH);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] row = line.split(ApplicationConstants.CSV_DELIMITER);
                if (row.length < 3) {
                    throw new IllegalStateException("Invalid business CSV row: " + line);
                }
                Business business = Business.builder()
                        .businessName(row[0].trim())
                        .latitude(Double.parseDouble(row[1].trim()))
                        .longitude(Double.parseDouble(row[2].trim()))
                        .build();

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
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize business data from CSV", e);
        }
    }
}
