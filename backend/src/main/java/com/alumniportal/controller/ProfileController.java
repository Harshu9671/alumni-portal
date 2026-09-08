package com.alumniportal.controller;

import com.alumniportal.model.Profile;
import com.alumniportal.model.User;
import com.alumniportal.repository.ProfileRepository;
import com.alumniportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired private ProfileRepository profileRepository;
    @Autowired private UserRepository userRepository;

    // Returns the logged-in user's own profile (email comes from validated JWT, not client input)
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return profileRepository.findById(user.getId())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(Map.of("message", "No profile yet — create one")));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(Authentication auth, @RequestBody Profile profileData) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findById(user.getId()).orElse(new Profile());
        profile.setUser(user);
        profile.setUserId(user.getId());
        profile.setGraduationYear(profileData.getGraduationYear());
        profile.setCompany(profileData.getCompany());
        profile.setDesignation(profileData.getDesignation());
        profile.setBio(profileData.getBio());
        profile.setProfilePicUrl(profileData.getProfilePicUrl());

        return ResponseEntity.ok(profileRepository.save(profile));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long userId) {
        return profileRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
