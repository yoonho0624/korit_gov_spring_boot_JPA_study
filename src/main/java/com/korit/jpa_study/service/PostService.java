package com.korit.jpa_study.service;

import com.korit.jpa_study.dto.AddPostReqDto;
import com.korit.jpa_study.dto.ApiRespDto;
import com.korit.jpa_study.dto.EditPostReqDto;
import com.korit.jpa_study.entity.Post;
import com.korit.jpa_study.entity.User;
import com.korit.jpa_study.repository.PostRepository;
import com.korit.jpa_study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public ApiRespDto<?> addPost(AddPostReqDto addPostReqDto) {
        Optional<User> foundUser = userRepository.findById(addPostReqDto.getUserId());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않는 회원정보입니다.", null);
        }
        Optional<Post> foundPost = postRepository.findByTitle(addPostReqDto.getTitle());
        if (foundPost.isPresent()) {
            return new ApiRespDto<>("failed", "이미 존재하는 제목입니다.", addPostReqDto.getTitle());
        }
        return new ApiRespDto<>("success", "추가 성공", postRepository.save(addPostReqDto.toEntity()));
    }
    public ApiRespDto<?> getPostAll() {
        return new ApiRespDto<>("success", "전체 조회 성공", postRepository.findAll());
    }
    public ApiRespDto<?> getPostByPostId(Integer postId) {
        Optional<Post> foundPost = postRepository.findById(postId);
        if (foundPost.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 게시물이 존재하지 않습니다", null);
        }
        return new ApiRespDto<>("success", "단건 조회 성공", foundPost.get());
    }
    public ApiRespDto<?> getPostListByUserId(Integer userId) {
        return new ApiRespDto<>("success", "유저 게시물 조회 완료", postRepository.findAllByUserId(userId));
    }
    public ApiRespDto<?> editPost(EditPostReqDto editPostReqDto) {
        Optional<Post> foundPost = postRepository.findById(editPostReqDto.getPostId());
        if (foundPost.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 게시물이 존재하지 않습니다", null);
        }
        Post post = foundPost.get();
        post.setTitle(editPostReqDto.getTitle());
        post.setContent(editPostReqDto.getContent());
        post.setUpdateDt(LocalDateTime.now());
        return new ApiRespDto<>("success", "수정 성공", postRepository.save(post));
    }
    public ApiRespDto<?> removePost(Integer postId) {
        Optional<Post> foundPost = postRepository.findById(postId);
        if (foundPost.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 게시물이 존재하지 않습니다.", null);
        }
        postRepository.deleteById(postId);
        return new ApiRespDto<>("success", "삭제 성공", null);
    }
}
