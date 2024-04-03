package com.example.cms.serviceimpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.cms.dto.UserRequest;
import com.example.cms.entity.User;
import com.example.cms.exception.UserAlreadyExistByEmailException;
import com.example.cms.repository.UserRepository;
import com.example.cms.responseDto.UserResponse;
import com.example.cms.service.UserService;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	private UserRepository repository;
	private ResponseStructure<UserResponse> responseStructure;
	private PasswordEncoder passwordEncoder;

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) {
		if (repository.existsByUserEmail(userRequest.getUserEmail())) {
				throw new UserAlreadyExistByEmailException("Failed to register User");
		}

		User user = repository.save(mapToUser(userRequest, new User()));
		return ResponseEntity.ok(responseStructure.setStatusCode(HttpStatus.OK.value())
				.setMessage("User Registered Sucessfully").setData(mapToResponse(user)));
	}

	private UserResponse mapToResponse(User user) {
		return UserResponse.builder().userId(user.getUserId()).userEmail(user.getUserEmail())
				.userName(user.getUserName())
				.createdAt(user.getCreatedAt())
				.lastModifiedsAt(user.getLastModifiedAt())
				.build();
	}

	private User mapToUser(UserRequest userRequest, User user) {
		user.setUserName(userRequest.getUserName());
		user.setUserEmail(userRequest.getUserEmail());
		user.setUserPassword(passwordEncoder.encode(userRequest.getUserPassword()));
		return user;
	}

}
