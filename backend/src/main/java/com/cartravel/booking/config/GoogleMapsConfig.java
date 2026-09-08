package com.cartravel.booking.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
public class GoogleMapsConfig {
    @Value("${app.google.maps.api-key:YOUR_GOOGLE_MAPS_API_KEY}") private String apiKey;
    @Value("${app.google.maps.enabled:false}") private boolean enabled;
    public String getApiKey() { return apiKey; }
    public boolean isEnabled() { return enabled; }
}