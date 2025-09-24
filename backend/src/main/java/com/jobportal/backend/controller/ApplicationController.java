package com.jobportal.backend.controller;

import com.jobportal.backend.dto.ApplicationRequest;
import com.jobportal.backend.model.JobApplication;
import com.jobportal.backend.model.enums.ApplicationStatus;
import com.jobportal.backend.security.SecurityUserPrincipal;
import com.jobportal.backend.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<JobApplication> apply(@AuthenticationPrincipal SecurityUserPrincipal principal,
                                                @Valid @RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(applicationService.apply(principal.getUser(), request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<List<JobApplication>> myApplications(@AuthenticationPrincipal SecurityUserPrincipal principal) {
        return ResponseEntity.ok(applicationService.getApplicationsForSeeker(principal.getUser()));
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<List<JobApplication>> applicationsForJob(@PathVariable Long jobId,
                                                                   @AuthenticationPrincipal SecurityUserPrincipal principal) {
        return ResponseEntity.ok(applicationService.getApplicationsForJob(jobId, principal.getUser()));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobApplication> updateStatus(@PathVariable Long id,
                                                       @AuthenticationPrincipal SecurityUserPrincipal principal,
                                                       @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.updateStatus(id, status, principal.getUser()));
    }
}