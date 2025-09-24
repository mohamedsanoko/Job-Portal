package com.jobportal.backend.service.impl;

import com.jobportal.backend.dto.ApplicationRequest;
import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.JobApplication;
import com.jobportal.backend.model.User;
import com.jobportal.backend.model.enums.ApplicationStatus;
import com.jobportal.backend.repository.JobApplicationRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.service.ApplicationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    public ApplicationServiceImpl(JobApplicationRepository applicationRepository, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public JobApplication apply(User seeker, ApplicationRequest request) {
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));
        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setSeeker(seeker);
        application.setResumeUrl(request.getResumeUrl());
        application.setCoverLetter(request.getCoverLetter());
        application.setStatus(ApplicationStatus.APPLIED);
        return applicationRepository.save(application);
    }

    @Override
    public JobApplication updateStatus(Long applicationId, ApplicationStatus status, User employer) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found"));
        if (!application.getJob().getEmployer().getId().equals(employer.getId())) {
            throw new SecurityException("Not authorized to update this application");
        }
        application.setStatus(status);
        return applicationRepository.save(application);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplication> getApplicationsForJob(Long jobId, User employer) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));
        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new SecurityException("Not authorized to view applications");
        }
        return applicationRepository.findByJob(job);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplication> getApplicationsForSeeker(User seeker) {
        return applicationRepository.findBySeeker(seeker);
    }
}