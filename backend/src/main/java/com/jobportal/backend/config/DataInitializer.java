package com.jobportal.backend.config;

import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.Profile;
import com.jobportal.backend.model.User;
import com.jobportal.backend.model.enums.JobStatus;
import com.jobportal.backend.model.enums.UserRole;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.ProfileRepository;
import com.jobportal.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository,
                                       ProfileRepository profileRepository,
                                       JobRepository jobRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User("Admin", "admin@jobportal.com", passwordEncoder.encode("password"), UserRole.ADMIN);
                userRepository.save(admin);

                User employer = new User("Employer Co", "employer@jobportal.com", passwordEncoder.encode("password"), UserRole.EMPLOYER);
                userRepository.save(employer);

                User seeker = new User("Job Seeker", "seeker@jobportal.com", passwordEncoder.encode("password"), UserRole.JOB_SEEKER);
                userRepository.save(seeker);

                Profile seekerProfile = new Profile();
                seekerProfile.setUser(seeker);
                seekerProfile.setSkills("Angular, Spring Boot");
                seekerProfile.setExperience("5 years");
                seekerProfile.setBio("Full stack engineer looking for new challenges.");
                profileRepository.save(seekerProfile);

                Job job = new Job();
                job.setEmployer(employer);
                job.setTitle("Senior Java Developer");
                job.setDescription("Build scalable services and mentor junior engineers.");
                job.setLocation("Remote");
                job.setSalary("$120k - $140k");
                job.setRequirements("Java, Spring Boot, REST APIs");
                job.setStatus(JobStatus.PUBLISHED);
                jobRepository.save(job);
            }
        };
    }
}