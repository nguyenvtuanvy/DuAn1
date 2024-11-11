package com.code.duan1.service.posts;

import com.code.duan1.dto.PostsDTO;

import java.util.Set;

public interface PostService {
    Set<PostsDTO> getAllPost();

    PostsDTO getPostById(Long id);
}
