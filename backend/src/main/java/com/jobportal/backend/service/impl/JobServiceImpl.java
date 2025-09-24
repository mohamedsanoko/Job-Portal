package com.jobportal.backend.service.impl;

import com.jobportal.backend.dto.JobRequest;
import com.jobportal.backend.dto.PagedResponse;
import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.User;
import com.jobportal.backend.model.enums.JobStatus;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.service.JobService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Job createJob(User employer, JobRequest request) {
        Job job = new Job();
        job.setEmployer(employer);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setRequirements(request.getRequirements());
        job.setStatus(request.getStatus() != null ? request.getStatus() : JobStatus.DRAFT);
        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long jobId, User employer, JobRequest request) {
        Job job = getJob(jobId);
        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new SecurityException("Not authorized to update this job");
        }
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setRequirements(request.getRequirements());
        if (request.getStatus() != null) {
            job.setStatus(request.getStatus());
        }
        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long jobId, User employer) {
        Job job = getJob(jobId);
        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new SecurityException("Not authorized to delete this job");
        }
        jobRepository.delete(job);
    }

    @Override
    public Job updateJobStatus(Long jobId, JobStatus status, User employer) {
        Job job = getJob(jobId);
        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new SecurityException("Not authorized to update this job");
        }
        job.setStatus(status);
        return jobRepository.save(job);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<Job> searchJobs(String keyword, JobStatus status, Pageable pageable) {
        Page<Job> page = jobRepository.searchJobs(keyword, status, pageable);
        return new PagedResponse<>(page.getContent(), page.getTotalElements(), page.getTotalPages(),
                page.getNumber(), page.getSize());
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<Job> getEmployerJobs(User employer, Pageable pageable) {
        Page<Job> page = jobRepository.findByEmployer(employer, pageable);
        return new PagedResponse<>(page.getContent(), page.getTotalElements(), page.getTotalPages(),
                page.getNumber(), page.getSize());
    }

    @Override
    @Transactional(readOnly = true)
    public Job getJob(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));
    }
}