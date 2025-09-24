package com.jobportal.backend.service;

import com.jobportal.backend.dto.ApplicationRequest;
import com.jobportal.backend.model.JobApplication;
import com.jobportal.backend.model.User;
import com.jobportal.backend.model.enums.ApplicationStatus;

import java.util.List;

public interface ApplicationService {
    JobApplication apply(User seeker, ApplicationRequest request);

    JobApplication updateStatus(Long applicationId, ApplicationStatus status, User employer);

    List<JobApplication> getApplicationsForJob(Long jobId, User employer);

    List<JobApplication> getApplicationsForSeeker(User seeker);
}