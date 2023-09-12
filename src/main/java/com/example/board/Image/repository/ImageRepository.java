package com.example.board.Image.repository;

import com.example.board.Image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    void deleteByImageName(String ImageName);
}
