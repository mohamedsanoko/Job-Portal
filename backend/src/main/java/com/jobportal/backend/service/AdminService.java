package com.jobportal.backend.service;

import com.jobportal.backend.dto.ReportResponse;
import com.jobportal.backend.dto.UserSummary;
import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.enums.JobStatus;

import java.util.List;

public interface AdminService {
    List<UserSummary> getAllUsers();

    UserSummary toggleUserActivation(Long userId, boolean active);

    void deleteUser(Long userId);

    Job updateJobStatus(Long jobId, JobStatus status);

    void deleteJob(Long jobId);

    ReportResponse generateReport();
}