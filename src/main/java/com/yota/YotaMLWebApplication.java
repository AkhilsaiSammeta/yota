package com.ayota;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AYOTA ML WEB APPLICATION
 * 
 * Purpose: Spring Boot main application class
 * Think of it like: The engine that starts the web server
 * 
 * Real-life analogy: Like starting up a restaurant - this gets everything ready
 */
@SpringBootApplication
public class YotaMLWebApplication {

    public static void main(String[] args) {
        System.out.println("🚀 Starting AYOTA ML Web Interface...");
        System.out.println("📊 WEKA-like ML Engine with Web UI");
        System.out.println("=======================================");
        
        SpringApplication.run(YotaMLWebApplication.class, args);
        
        System.out.println("✅ AYOTA ML Web Server Started!");
        System.out.println("🌐 Open browser: http://localhost:8080");
        System.out.println("=======================================");
    }
}