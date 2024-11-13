package com.code.duan1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class PostsDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String image;
    private String title;
    private String nameAdmin;

    public PostsDTO(Long id, String content, LocalDateTime createdAt, String image, String title, String nameAdmin) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.image = image;
        this.title = title;
        this.nameAdmin = nameAdmin;
    }
}
