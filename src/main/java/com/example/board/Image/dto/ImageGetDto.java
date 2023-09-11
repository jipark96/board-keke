package com.example.board.Image.dto;

import com.example.board.Image.entity.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageGetDto {
    private Long id;
    private String imageUrl;
    private String imageName;

    public ImageGetDto(Image image) {
        this.id = image.getId();
        this.imageUrl = image.getImageUrl();
        this.imageName = image.getImageName();
    }
}
