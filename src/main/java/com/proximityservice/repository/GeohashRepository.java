package com.proximityservice.repository;

import com.proximityservice.entity.Geohash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeohashRepository extends JpaRepository<Geohash, Long> {
    List<Geohash> findByBusinessId(Long businessId);
}
