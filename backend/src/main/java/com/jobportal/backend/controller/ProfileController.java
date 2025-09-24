package com.jobportal.backend.controller;

import com.jobportal.backend.dto.ProfileRequest;
import com.jobportal.backend.model.Profile;
import com.jobportal.backend.security.SecurityUserPrincipal;
import com.jobportal.backend.service.StorageService;
import com.jobportal.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserService userService;
    private final StorageService storageService;

    public ProfileController(UserService userService, StorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('JOB_SEEKER','EMPLOYER','ADMIN')")
    public ResponseEntity<Profile> getProfile(@AuthenticationPrincipal SecurityUserPrincipal principal) {
        return ResponseEntity.ok(userService.getProfile(principal.getUser().getId()));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('JOB_SEEKER','EMPLOYER','ADMIN')")
    public ResponseEntity<Profile> updateProfile(@AuthenticationPrincipal SecurityUserPrincipal principal,
                                                 @Valid @RequestBody ProfileRequest request) {
        Profile profile = userService.updateProfile(principal.getUser().getId(), request, null);
        return ResponseEntity.ok(profile);
    }

    @PostMapping(value = "/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('JOB_SEEKER','EMPLOYER','ADMIN')")
    public ResponseEntity<Profile> uploadResume(@AuthenticationPrincipal SecurityUserPrincipal principal,
                                                @RequestParam("file") MultipartFile file) {
        String resumeUrl = storageService.storeResume(file);
        ProfileRequest request = new ProfileRequest();
        Profile existing = userService.getProfile(principal.getUser().getId());
        if (existing != null) {
            request.setBio(existing.getBio());
            request.setSkills(existing.getSkills());
            request.setExperience(existing.getExperience());
            request.setLocation(existing.getLocation());
        }
        Profile profile = userService.updateProfile(principal.getUser().getId(), request, resumeUrl);
        return ResponseEntity.ok(profile);
    }
}