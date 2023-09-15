package com.example.board.user.service;

import com.example.board.Image.entity.UserImage;
import com.example.board.Image.repository.ImageRepository;
import com.example.board.board.dto.GetBoardDto;
import com.example.board.board.dto.GetBoardListResponseDto;
import com.example.board.board.entity.Board;
import com.example.board.board.repository.BoardRepository;
import com.example.board.comment.entity.Comment;
import com.example.board.comment.repository.CommentRepository;
import com.example.board.common.exceptions.BaseException;
import com.example.board.file.entity.File;
import com.example.board.file.repository.FileRepository;
import com.example.board.like.entity.Like;
import com.example.board.like.repository.LikeRepository;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private final LikeRepository likeRepository;

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
    @Transactional
    public void modifyUser(Long userId, PatchUserDto patchUserDto) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 비밀번호 수정 시 암호화 및 기타 정보 업데이트
        String newPassword = patchUserDto.getPassword();
        if (newPassword != null && !newPassword.isEmpty()) {
            user.updatePassword(bCryptPasswordEncoder.encode(newPassword));
        }

        // 프로필 이미지 업데이트 - 실제 파일 저장 후 URL 획득
        MultipartFile image = patchUserDto.getImage();

        if (image != null && !image.isEmpty()) {
            String imagePath = "src/main/resources/uploadedImages";
            String imageName = StringUtils.cleanPath(image.getOriginalFilename());

            try {
                Path targetPath = Paths.get(imagePath).resolve(imageName);
                Files.copy(image.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                UserImage userImage;

                // 기존에 등록된 사진이 없으면 새로 추가
                if (user.getUserImage() == null) {
                    userImage = new UserImage();
                    user.setUserImage(userImage);
                    user.getUserImage().setUser(user);
                } else {
                    // 기존 사진이 있으면 갱신
                    deleteUserimage(user.getUserImage().getImageName());

                    userImage= user.getUserImage();
                }

                userImage.setImageUrl("/uploadedImages/" + imageName);

            } catch (IOException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("이미지를 저장하는 과정에서 문제가 발생하였습니다.");
            }
        }

        user.updateUserInfo(
                patchUserDto.getName(),
                patchUserDto.getEmail(),
                user.getPassword(),
                user.getUserImage()
        );
    }

    //[이미지 삭제]
    private void deleteUserimage(String imageName) {
        String imagePath = "src/main/resources/uploadedImages";

        try {
            imageName = imageName.replace("\"", "");
            imageName = imageName.replace("/uploadedImages/", "");

            Path targetPath = Paths.get(imagePath).resolve(imageName);

            Files.deleteIfExists(targetPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("이미지를 삭제하는 과정에서 문제가 발생하였습니다.");
        }
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

    //[좋아요 누른 게시글 조회]
    public GetBoardListResponseDto getLikedBoard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 좋아요 누른 게시물 가져오기
        List<Like> likes = likeRepository.findByUserId(userId);

        // 각각의 좋아요한 게시물 추출 후 GetBoardDto로 변환
        List<GetBoardDto> boardDtoList = likes.stream()
                .map(like -> new GetBoardDto(like.getBoard()))
                .collect(Collectors.toList());

        return new GetBoardListResponseDto(boardDtoList, boardDtoList.size());
    }
}
