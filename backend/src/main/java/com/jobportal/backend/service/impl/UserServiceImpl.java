package com.jobportal.backend.service.impl;

import com.jobportal.backend.dto.*;
import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.Profile;
import com.jobportal.backend.model.User;
import com.jobportal.backend.model.enums.UserRole;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.ProfileRepository;
import com.jobportal.backend.repository.UserRepository;
import com.jobportal.backend.security.JwtTokenProvider;
import com.jobportal.backend.security.SecurityUserPrincipal;
import com.jobportal.backend.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final JobRepository jobRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public UserServiceImpl(UserRepository userRepository,
                           ProfileRepository profileRepository,
                           JobRepository jobRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.jobRepository = jobRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public User register(SignupRequest request) {
        Optional<User> existing = userRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            throw new EntityExistsException("Email already registered");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() == null ? UserRole.JOB_SEEKER : request.getRole());
        return userRepository.save(user);
    }

    @Override
    public JwtResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityUserPrincipal principal = (SecurityUserPrincipal) authentication.getPrincipal();
        String token = tokenProvider.generateToken(principal.getUsername());
        User user = principal.getUser();
        return new JwtResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    @Override
    public Profile updateProfile(Long userId, ProfileRequest request, String resumeUrl) {
        User user = getUser(userId);
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
        }
        profile.setBio(request.getBio());
        profile.setSkills(request.getSkills());
        profile.setExperience(request.getExperience());
        profile.setLocation(request.getLocation());
        if (resumeUrl != null) {
            profile.setResumeUrl(resumeUrl);
        }
        Profile saved = profileRepository.save(profile);
        user.setProfile(saved);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Profile getProfile(Long userId) {
        User user = getUser(userId);
        return user.getProfile();
    }

    @Override
    public Set<Job> toggleFavorite(Long userId, Long jobId) {
        User user = getUser(userId);
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));
        Set<Job> favorites = user.getFavoriteJobs();
        if (favorites == null) {
            favorites = new HashSet<>();
            user.setFavoriteJobs(favorites);
        }
        if (favorites.contains(job)) {
            favorites.remove(job);
        } else {
            favorites.add(job);
        }
        userRepository.save(user);
        return favorites;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Job> getFavorites(Long userId) {
        return new HashSet<>(getUser(userId).getFavoriteJobs());
    }
}