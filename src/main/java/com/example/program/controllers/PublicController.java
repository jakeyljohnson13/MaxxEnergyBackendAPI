package com.example.program.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {


    @GetMapping("/home")
    public Map<String, Object> getHomepageData() {
        return Map.of(
                "headline", "Clean Energy for a Brighter Tomorrow",
                "subheadline", "MaxxEnergy leads innovation in renewable power.",
                "ctaText", "Get Started",
                "heroImageUrl", "/assets/images/hero-banner.jpg"
        );
    }

@GetMapping("/about")
    public Map<String, Object> getAboutPage() {
    return Map.of(
    "headline","about MaxxEnergy",
    "mission", "Power the world",
    "values", List.of("sustainabilty", "innovation", "etc"),
    "imageurl", "/assets/images/image1"
            );
}


// FAQ
// Contact Form
// Navbar
// Footer

}

