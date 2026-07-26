package com.proximityservice.service;

public interface IDistanceCalculator {
    double calculate(double lat1, double lon1, double lat2, double lon2);
}
