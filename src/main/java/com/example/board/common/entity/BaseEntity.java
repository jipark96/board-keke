package com.example.board.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseEntity {

        @CreationTimestamp
        @Column(name = "createAt", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updateAt", nullable = false)
        private LocalDateTime updatedAt;

        @Enumerated(EnumType.STRING)
        @Column(name = "state", nullable = false, length = 10)
        protected State state = State.ACTIVE;

        public enum State  {
            ACTIVE, INACTIVE
        }
    }

