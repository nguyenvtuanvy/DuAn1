package com.code.duan1.service.posts;

import com.code.duan1.dto.PostsDTO;
import com.code.duan1.entity.Posts;
import com.code.duan1.repository.AdminRepository;
import com.code.duan1.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final AdminRepository adminRepository;
    @Override
    @Transactional(readOnly = true)
    public Set<PostsDTO> getAllPost() {
        try{
            Set<Posts> posts = new HashSet<>(postRepository.findAll());

            return  posts.stream()
                    .map(this::toGalleryImageDTO)
                    .collect(Collectors.toSet());
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    PostsDTO toGalleryImageDTO(Posts posts){
        return new PostsDTO(posts.getId(), posts.getContent(), posts.getCreatedAt(), posts.getImage(), posts.getTitle(), adminRepository.getNameById(posts.getAdmin().getId()));
    }

    @Override
    public PostsDTO getPostById(Long id) {
        try{
            Posts post = postRepository.findById(id).orElseThrow();

            return  toGalleryImageDTO(post);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
