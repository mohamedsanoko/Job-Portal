package com.jobportal.backend.controller;

import com.jobportal.backend.dto.PagedResponse;
import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.enums.JobStatus;
import com.jobportal.backend.security.SecurityUserPrincipal;
import com.jobportal.backend.service.JobService;
import com.jobportal.backend.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;
    private final UserService userService;

    public JobController(JobService jobService, UserService userService) {
        this.jobService = jobService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<Job>> searchJobs(@RequestParam(value = "q", required = false) String keyword,
                                                         @RequestParam(value = "status", required = false) JobStatus status,
                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(jobService.searchJobs(keyword, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJob(id));
    }

    @PostMapping("/{id}/favorite")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<?> toggleFavorite(@PathVariable Long id,
                                            @AuthenticationPrincipal SecurityUserPrincipal principal) {
        return ResponseEntity.ok(userService.toggleFavorite(principal.getUser().getId(), id));
    }

    @GetMapping("/favorites")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<?> favorites(@AuthenticationPrincipal SecurityUserPrincipal principal) {
        return ResponseEntity.ok(userService.getFavorites(principal.getUser().getId()));
    }
}