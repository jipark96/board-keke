package com.example.board.user.entity;

import com.example.board.board.entity.Board;
import com.example.board.comment.entity.Comment;
import com.example.board.common.entity.BaseEntity;

import com.example.board.like.entity.Like;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User extends BaseEntity implements UserDetails {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Board> boardList = new ArrayList<Board>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Comment> commentList = new ArrayList<Comment>();

    @Enumerated(EnumType.STRING)
    private Role role;
    
    @Builder
    public User(String username, String password, String auth) {
        this.username = username;
        this.password = password;
        this.role = Role.USER; // 기본 역할 설정
    }

    //[권한 반환]
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    //[사용자의 id 반환(고유한 값)]
    @Override
    public String getUsername() {
        return username;
    }
    //[모든 계정이 유효한 것으로 가정]
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //[사용자 패스워드 반환]
    @Override
    public String getPassword() {
        return password;
    }
    //[계정 잠금 여부 반환]
    @Override
    public boolean isAccountNonLocked() {
        return true; // true : 잠금 X
    }
    //[패스워드 만료 여부 반환]
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // true : 만료 X
    }
    //[계정 사용 가능 여부 반환]
    @Override
    public boolean isEnabled() {
        return true;  // true : 사용 가능
    }

    //[회원 수정]
    public void updateNameAndPassword(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //[비밀번호 수정]
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    //[회원 탈퇴]
    public void deleteUser() {

        this.state = State.INACTIVE;
    }


}
