package com.doit.univplanner.service;

import com.doit.univplanner.dto.UserDto;
import com.doit.univplanner.entity.User;
import com.doit.univplanner.exception.CustomException;
import com.doit.univplanner.exception.ErrorCode;
import com.doit.univplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public String registerUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        if (!userDto.getPassword().equals(userDto.getPasswordCheck())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = userDto.toEntity(encodedPassword);

        userRepository.save(user);
        return "새로운 유저가 되신 것을 환영합니다!";
    }

    public boolean loginUser(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return passwordEncoder.matches(userDto.getPassword(), user.getPassword());
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }
}
