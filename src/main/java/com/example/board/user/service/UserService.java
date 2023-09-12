package com.example.board.user.service;

import com.example.board.board.dto.GetBoardDto;
import com.example.board.board.dto.GetBoardListResponseDto;
import com.example.board.board.entity.Board;
import com.example.board.board.repository.BoardRepository;
import com.example.board.comment.entity.Comment;
import com.example.board.comment.repository.CommentRepository;
import com.example.board.common.exceptions.BaseException;
import com.example.board.file.entity.File;
import com.example.board.file.repository.FileRepository;
import com.example.board.user.dto.*;
import com.example.board.user.entity.Role;
import com.example.board.user.entity.User;
import com.example.board.user.repository.UserRepository;
import com.example.board.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.board.common.entity.BaseEntity.State.ACTIVE;
import static com.example.board.common.response.BaseResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final FileRepository fileRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    //[회원 가입]
    public JoinResponseDto createUser(JoinRequestDto joinRequestDto) {
        //[아이디 중복 체크]
        Optional<User> checkUser = userRepository.findByUsernameAndState(joinRequestDto.getUsername(), ACTIVE);
        if (checkUser.isPresent() == true) {
            throw new BaseException(POST_USERS_EXISTS_USERNAME);
        }

        User userToSave = joinRequestDto.toEntity(bCryptPasswordEncoder);
        userToSave.setRole(Role.USER);

        User saveUser = userRepository.save(userToSave);
        String jwtToken = jwtService.createJwt(saveUser.getId());


        return JoinResponseDto.builder()
                .id(saveUser.getId())
                .email(saveUser.getEmail())
                .username(saveUser.getUsername())
                .name(saveUser.getName())
                .jwtToken(jwtToken)
                .role(saveUser.getRole().name())
                .build();
    }

    //[관리자 아이디 생성]
    public JoinResponseDto createAdmin(JoinRequestDto joinRequestDto) {

        User userToSave = joinRequestDto.toEntity(bCryptPasswordEncoder);
        userToSave.setRole(Role.ADMIN);

        User saveUser = userRepository.save(userToSave);
        String jwtToken = jwtService.createJwt(saveUser.getId());


        return JoinResponseDto.builder()
                .id(saveUser.getId())
                .email(saveUser.getEmail())
                .username(saveUser.getUsername())
                .name(saveUser.getName())
                .jwtToken(jwtToken)
                .role(saveUser.getRole().name())
                .build();
    }

    //[아이디 중복 확인]
    public boolean isUsernameExists(String username) {
        return userRepository.findByUsernameAndState(username, ACTIVE).isPresent();
    }

    //[회원 조회]
    @Transactional(readOnly = true)
    public List<GetUserDto> getUsers() {
        List<GetUserDto> getUserDtoList = userRepository.findByState(ACTIVE).stream()
                .map(GetUserDto::new)
                .collect(Collectors.toList());
        return getUserDtoList;
    }

    //[로그인]
    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {

        // 입력한 아이디로 활성화된 사용자 정보 조회
        User user = userRepository.findByUsernameAndState(loginRequestDto.getUsername(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        // 입력한 비밀번호를 해싱한 후에 저장된 해시된 비밀번호와 비교
        if(!bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new BaseException(FAILED_TO_PASSWORD);
        }

        String jwtToken = jwtService.createJwt(user.getId());

        return LoginResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .jwtToken(jwtToken)
                .role(user.getRole().name())
                .build();
    }

    //[회원 탈퇴]
    @Transactional
    public void deleteUser(Long userId) {
        // 활성화된 사용자 정보 조회 및 삭제 처리
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        user.deleteUser();
    }

    //[회원 상세 정보 조회]
    @Transactional(readOnly = true)
    public GetUserDto getUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        return new GetUserDto(user);
    }

    //[회원 수정]
    public void modifyUser(Long userId, PatchUserDto patchUserDto) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 비밀번호 수정 시 암호화 및 기타 정보 업데이트
        String newPassword = patchUserDto.getPassword();
        if (newPassword != null && !newPassword.isEmpty()) {
            user.updatePassword(bCryptPasswordEncoder.encode(newPassword));
        }

        user.updateNameAndPassword(
                patchUserDto.getName(),
                patchUserDto.getEmail(),
                user.getPassword()
        );
    }

    //[내 글 리스트 조회]
    public GetBoardListResponseDto getUserBoard(Long userId, int page, int size, String keyword, String sortType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Sort sort;

        if (sortType.equals("id")) {
            sort = Sort.by(Sort.Direction.DESC,"id");
        } else if (sortType.equals("view")) {
            sort = Sort.by(Sort.Direction.DESC,"view");
        } else { // 기본 정렬은 id 최신순
            sort = Sort.by(Sort.Direction.DESC, "id");
        }

        Pageable pageable = PageRequest.of(page,size, sort);
        Page<Board> boardPage;

        if (keyword == null) {
            boardPage = boardRepository.findByUserId(userId, pageable);
        } else {
            boardPage = boardRepository.findByUserIdAndTitleContaining(userId, keyword, pageable);
        }

        List<GetBoardDto> getBoardDtoList = boardPage.get().map(GetBoardDto::new).collect(Collectors.toList());

        long totalCount = boardPage.getTotalElements();

        return new GetBoardListResponseDto(getBoardDtoList, totalCount);
    }

    //[내가 쓴 댓글 게시글 조회]
    public GetBoardListResponseDto getUserCommentBoard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 사용자가 작성한 모든 댓글 가져오기
        List<Comment> userComments = commentRepository.findByUserId(userId);

        // 각각의 댓글이 속한 게시물 ID 추출
        List<Long> boardIds = userComments.stream()
                .map(comment -> comment.getBoard().getId())
                .distinct() // 중복 제거
                .collect(Collectors.toList());

        //추출된 ID로 게시물 가져오기
        List<Board> boardList = boardRepository.findByIdIn(boardIds);

        List<GetBoardDto> getBoardDtoList = boardList.stream().map(GetBoardDto::new).collect(Collectors.toList());

        return new GetBoardListResponseDto(getBoardDtoList, getBoardDtoList.size());

    }

}
