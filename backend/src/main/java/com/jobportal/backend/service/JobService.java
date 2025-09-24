package com.jobportal.backend.service;

import com.jobportal.backend.dto.JobRequest;
import com.jobportal.backend.dto.PagedResponse;
import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.User;
import com.jobportal.backend.model.enums.JobStatus;
import org.springframework.data.domain.Pageable;

public interface JobService {
    Job createJob(User employer, JobRequest request);

    Job updateJob(Long jobId, User employer, JobRequest request);

    void deleteJob(Long jobId, User employer);

    Job updateJobStatus(Long jobId, JobStatus status, User employer);

    PagedResponse<Job> searchJobs(String keyword, JobStatus status, Pageable pageable);

    PagedResponse<Job> getEmployerJobs(User employer, Pageable pageable);

    Job getJob(Long id);
}