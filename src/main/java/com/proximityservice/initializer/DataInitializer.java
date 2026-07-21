package com.proximityservice.initializer;

import com.proximityservice.entity.Business;
import com.proximityservice.entity.Geohash;
import com.proximityservice.repository.BusinessRepository;
import com.proximityservice.repository.GeohashRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final BusinessRepository businessRepository;
    private final GeohashRepository geohashRepository;

    public DataInitializer(BusinessRepository businessRepository, GeohashRepository geohashRepository) {
        this.businessRepository = businessRepository;
        this.geohashRepository = geohashRepository;
    }

    @Override
    public void run(org.springframework.boot.ApplicationArguments args) {
        // Only initialize if database is empty
        if (businessRepository.count() == 0) {
            initializeDummyData();
        }
    }

    private void initializeDummyData() {
        // Create dummy businesses with realistic locations (San Francisco Bay Area)
        Business[] businesses = new Business[]{
                new Business(null, "Starbucks Downtown", 37.7749, -122.4194),
                new Business(null, "Pizza Palace", 37.7849, -122.4094),
                new Business(null, "Tech Hub Cafe", 37.7649, -122.4294),
                new Business(null, "Golden Gate Coffee", 37.8099, -122.4794),
                new Business(null, "Silicon Valley Deli", 37.3382, -121.8863),
                new Business(null, "Bay View Restaurant", 37.7549, -122.3994),
                new Business(null, "Mountain View Bakery", 37.3852, -122.0849),
                new Business(null, "Palo Alto Fitness", 37.4419, -122.1430)
        };

        // Save businesses and create corresponding geohashes
        for (Business business : businesses) {
            Business savedBusiness = businessRepository.save(business);

            // Generate a simple geohash based on latitude and longitude
            String geohashValue = generateSimpleGeohash(business.getLatitude(), business.getLongitude());

            Geohash geohash = new Geohash(null, savedBusiness.getBusinessId(), geohashValue);
            geohashRepository.save(geohash);
        }

        System.out.println("✓ Database initialized with 8 dummy businesses and geohashes");
    }

    /**
     * Generate a simplified geohash from latitude and longitude.
     * This is a basic implementation for demonstration purposes.
     */
    private String generateSimpleGeohash(Double latitude, Double longitude) {
        // Convert lat/lon to a simple geohash-like string
        long latBits = Double.doubleToLongBits(latitude);
        long lonBits = Double.doubleToLongBits(longitude);
        long combined = latBits ^ lonBits;
        return Long.toHexString(combined).substring(0, 8);
    }
}

