package com.example.program.controllers;


import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
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


    @GetMapping("/navbar")
    public Map<String, Object> getNavbarLinks() {
        return Map.of(
                "links", new String[] {
                        "Home", "About Us", "FAQ", "Contact"
                }
        );
    }

    @GetMapping("/footer")
    public Map<String, Object> getFooterInfo() {
        return Map.of(
                "company", "MAXX Energy",
                "copyright", "© 2025 MAXX Energy. All rights reserved.",
                "supportEmail", "support@maxxenergy.com"
        );
    }

    @GetMapping("/faq")
    public Map<String, Object>[] getFAQList() {
        return new Map[] {
                Map.of(
                        "question", "What is MAXX Energy?",
                        "answer", "A platform for visualizing and analyzing solar plant performance."
                ),
                Map.of(
                        "question", "How often is the data updated?",
                        "answer", "Plant data is updated in real-time or near real-time."
                )
        };
    }

    @PostMapping("/contact")
    public Map<String, Object> submitContactForm(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String email = body.get("email");
        String message = body.get("message");

        // You'd normally save this or send an email. This is just a placeholder response.
        return Map.of(
                "status", "success",
                "message", "Thanks " + name + ", we received your message and will respond shortly."
        );
    }
}

