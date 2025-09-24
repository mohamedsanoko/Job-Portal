package com.jobportal.backend.service;

import com.jobportal.backend.dto.*;
import com.jobportal.backend.model.Job;
import com.jobportal.backend.model.Profile;
import com.jobportal.backend.model.User;

import java.util.Set;

public interface UserService {
    User register(SignupRequest request);

    JwtResponse login(AuthRequest request);

    Profile updateProfile(Long userId, ProfileRequest request, String resumeUrl);

    Profile getProfile(Long userId);

    Set<Job> toggleFavorite(Long userId, Long jobId);

    User getUser(Long userId);

    Set<Job> getFavorites(Long userId);
}