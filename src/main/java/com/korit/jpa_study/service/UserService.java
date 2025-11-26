package com.korit.jpa_study.service;

import com.korit.jpa_study.dto.AddUserReqDto;
import com.korit.jpa_study.dto.ApiRespDto;
import com.korit.jpa_study.dto.EditUserReqDto;
import com.korit.jpa_study.entity.User;
import com.korit.jpa_study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ApiRespDto<?> addUser(AddUserReqDto addUserReqDto) {
        Optional<User> foundUser = userRepository.findByUsername(addUserReqDto.getUsername());
        if (foundUser.isPresent()) {
            return new ApiRespDto<>("failed", "이미 존재하는 username입니다.", addUserReqDto.getUsername());
        }
        return new ApiRespDto<>("success", "추가 성공", userRepository.save(addUserReqDto.toEntity()));
    }
    public ApiRespDto<?> getUserAll() {
        return new ApiRespDto<>("success", "전체 조회 성공", userRepository.findAll());
    }
    public ApiRespDto<?> getUserByUserId(Integer userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 id가 없습니다.", null);
        }
        return new ApiRespDto<>("success", "단건조회 성공", foundUser.get());
    }
    public ApiRespDto<?> getUserByUsername(String username) {
        Optional<User> foundUser = userRepository.findByUsername(username);
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 username이 없습니다.", null);
        }
        return new ApiRespDto<>("success", "단건조회 성공", foundUser.get());
    }
    public ApiRespDto<?> editUser(EditUserReqDto editUserReqDto) {
        Optional<User> foundUser = userRepository.findById(editUserReqDto.getUserId());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "가입된 회원정보가 없습니다.", null);
        }
        User user = foundUser.get();
        user.setUsername(editUserReqDto.getUsername());
        user.setPassword(editUserReqDto.getPassword());
        user.setUpdateDt(LocalDateTime.now());
        return new ApiRespDto<>("success", "수정 성공", userRepository.save(user));
    }
    public ApiRespDto<?> removeUser(Integer userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 id가 없습니다.", null);
        }
        userRepository.deleteById(userId);
        return new ApiRespDto<>("success", "삭제 성공", null);
    }
}
