package com.jobportal.backend.dto;

import com.jobportal.backend.model.enums.ApplicationStatus;
import com.jobportal.backend.model.enums.JobStatus;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {
    private ApplicationStatus applicationStatus;
    private JobStatus jobStatus;
    @NotNull
    private Long targetId;

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}