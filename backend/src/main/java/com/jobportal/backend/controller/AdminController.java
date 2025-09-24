package com.jobportal.backend.controller;

import com.jobportal.backend.dto.ReportResponse;
import com.jobportal.backend.dto.UserSummary;
import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.enums.JobStatus;
import com.jobportal.backend.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserSummary>> listUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<UserSummary> updateUserActivation(@PathVariable Long id,
                                                            @RequestParam boolean active) {
        return ResponseEntity.ok(adminService.toggleUserActivation(id, active));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/jobs/{id}/status")
    public ResponseEntity<Job> updateJobStatus(@PathVariable Long id,
                                               @RequestParam JobStatus status) {
        return ResponseEntity.ok(adminService.updateJobStatus(id, status));
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        adminService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reports")
    public ResponseEntity<ReportResponse> report() {
        return ResponseEntity.ok(adminService.generateReport());
    }
}