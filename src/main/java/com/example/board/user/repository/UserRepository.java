package com.example.board.user.repository;

import static com.example.board.common.entity.BaseEntity.*;
import com.example.board.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndState(String username, State state);
    List<User> findByState(State state);

    User findByUsername(String username);
}
