package com.jobportal.backend.service.impl;

import com.jobportal.backend.dto.ReportResponse;
import com.jobportal.backend.dto.UserSummary;
import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.User;
import com.jobportal.backend.model.enums.JobStatus;
import com.jobportal.backend.repository.JobApplicationRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;
import com.jobportal.backend.service.AdminService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;

    public AdminServiceImpl(UserRepository userRepository,
                            JobRepository jobRepository,
                            JobApplicationRepository applicationRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummary> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserSummary(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.isActive()))
                .collect(Collectors.toList());
    }

    @Override
    public UserSummary toggleUserActivation(Long userId, boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setActive(active);
        User saved = userRepository.save(user);
        return new UserSummary(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole(), saved.isActive());
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Job updateJobStatus(Long jobId, JobStatus status) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));
        job.setStatus(status);
        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long jobId) {
        jobRepository.deleteById(jobId);
    }

    @Override
    public ReportResponse generateReport() {
        long users = userRepository.count();
        long jobs = jobRepository.count();
        long applications = applicationRepository.count();
        return new ReportResponse(users, jobs, applications);
    }
}