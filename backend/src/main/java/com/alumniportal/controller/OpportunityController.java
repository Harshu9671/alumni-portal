package com.alumniportal.controller;

import com.alumniportal.model.Opportunity;
import com.alumniportal.model.User;
import com.alumniportal.repository.OpportunityRepository;
import com.alumniportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    @Autowired private OpportunityRepository opportunityRepository;
    @Autowired private UserRepository userRepository;

    @GetMapping
    public List<Opportunity> getAllOpportunities() {
        return opportunityRepository.findAllByOrderByPostedAtDesc();
    }

    @PostMapping
    public ResponseEntity<?> postOpportunity(Authentication auth, @RequestBody Map<String, String> body) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Opportunity opportunity = new Opportunity();
        opportunity.setPostedBy(user);
        opportunity.setTitle(body.get("title"));
        opportunity.setDescription(body.get("description"));

        return ResponseEntity.ok(opportunityRepository.save(opportunity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOpportunity(Authentication auth, @PathVariable Long id) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return opportunityRepository.findById(id).map(opp -> {
            if (!opp.getPostedBy().getId().equals(user.getId())) {
                return ResponseEntity.status(403).body(Map.of("error", "Not your post"));
            }
            opportunityRepository.delete(opp);
            return ResponseEntity.ok(Map.of("message", "Deleted"));
        }).orElse(ResponseEntity.notFound().build());
    }
}
