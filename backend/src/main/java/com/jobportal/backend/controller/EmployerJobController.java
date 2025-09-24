package com.jobportal.backend.controller;

import com.jobportal.backend.dto.JobRequest;
import com.jobportal.backend.dto.PagedResponse;
import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.enums.JobStatus;
import com.jobportal.backend.security.SecurityUserPrincipal;
import com.jobportal.backend.service.JobService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employer/jobs")
@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerJobController {

    private final JobService jobService;

    public EmployerJobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@AuthenticationPrincipal SecurityUserPrincipal principal,
                                         @Valid @RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.createJob(principal.getUser(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id,
                                         @AuthenticationPrincipal SecurityUserPrincipal principal,
                                         @Valid @RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.updateJob(id, principal.getUser(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id,
                                          @AuthenticationPrincipal SecurityUserPrincipal principal) {
        jobService.deleteJob(id, principal.getUser());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Job> updateStatus(@PathVariable Long id,
                                            @AuthenticationPrincipal SecurityUserPrincipal principal,
                                            @RequestParam JobStatus status) {
        return ResponseEntity.ok(jobService.updateJobStatus(id, status, principal.getUser()));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<Job>> listEmployerJobs(@AuthenticationPrincipal SecurityUserPrincipal principal,
                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(jobService.getEmployerJobs(principal.getUser(), pageable));
    }
}
