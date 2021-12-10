package com.mygroup.simplecommunity.service;

import com.mygroup.simplecommunity.domain.user.User;
import com.mygroup.simplecommunity.domain.user.UserRepository;
import com.mygroup.simplecommunity.exception.*;
import com.mygroup.simplecommunity.security.TokenProvider;
import com.mygroup.simplecommunity.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mygroup.simplecommunity.exception.ErrorType.DUPLICATE_PROPERTY;

@RequiredArgsConstructor
@Service
public class UserService {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public UserDto signup(UserDto userDto) {
        // dto에서 null property가 존재하는지 확인
        // 존재하면 MissingMandatoryException 발생
        if(userDto.getEmail() == null)
            throw new MissingMandatoryPropertyException("Email is mandatory");
        if(userDto.getPassword() == null)
            throw new MissingMandatoryPropertyException("Password is mandatory");
        if(userDto.getName() == null)
            throw new MissingMandatoryPropertyException("Name is mandatory");
        if(userDto.getPhone() == null)
            throw new MissingMandatoryPropertyException("Phone number is mandatory");

        User user = userDto.toEntity();

        // 이메일이 중복됬는지 확인
        if(userRepository.existsByEmail(user.getEmail()))
            throw new DuplicatePropertyException(DUPLICATE_PROPERTY, "Email is duplicated");

        // 비밀번호 암호화
        user.encodePassword();

        // db에 유저 정보 저장
        userRepository.save(user);

        return UserDto.builder().build();
    }

    public UserDto login(UserDto userDto) throws Exception {
        // dto에서 email, password가 존재하는지 확인
        // 존재하지 않다면 MissingMandatoryException 발생
        if(userDto.getEmail() == null)
            throw new MissingMandatoryPropertyException("Email is mandatory");
        else if(userDto.getPassword() == null)
            throw new MissingMandatoryPropertyException("Password is mandatory");

        // email로 유저 정보 가져오기
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
                new LoginException("Email is invalid"));

        // dto로 가져온 password와 user의 password 비교
        if(!user.matchPassword(userDto.getPassword()))
            throw new LoginException("Password is invalid");

        // user id로 토큰 생성
        String token = tokenProvider.create(user.getId());

        return UserDto.builder().token(token).build();
    }

    public UserDto findEmailByNameAndPhone(UserDto userDto) {
        if(userDto.getName() == null)
            throw new MissingMandatoryPropertyException("Name is mandatory");
        if(userDto.getPhone() == null)
            throw new MissingMandatoryPropertyException("Phone number is mandatory");

        User user = userRepository.findByNameAndPhone(userDto.getName(), userDto.getPhone()).orElseThrow(() ->
                new UserNotFoundException("User cannot be found"));
        return UserDto.builder().email(user.getEmail()).build();
    }

    public UserDto findEmailByEmailAndPhone(UserDto requestDto) {
        if(requestDto.getEmail() == null)
            throw new MissingMandatoryPropertyException("Email is mandatory");
        if(requestDto.getPhone() == null)
            throw new MissingMandatoryPropertyException("Phone number is mandatory");

        if(!userRepository.existsByEmailAndPhone(requestDto.getEmail(), requestDto.getPhone()))
            throw new UserNotFoundException("User cannot be found");

        return UserDto.builder().email(requestDto.getEmail()).build();
    }

    @Transactional
    public UserDto modifyPassword(UserDto requestDto) {
        if(requestDto.getEmail() == null)
            throw new MissingMandatoryPropertyException("Email is mandatory");
        if(requestDto.getPassword() == null)
            throw new MissingMandatoryPropertyException("Password is mandatory");
        if(requestDto.getNewPassword() == null)
            throw new MissingMandatoryPropertyException("New Password is mandatory");
        if(requestDto.getRepeatPassword() == null)
            throw new MissingMandatoryPropertyException("Repeat Password is mandatory");
        if(!requestDto.getNewPassword().equals(requestDto.getRepeatPassword()))
            throw new MismatchPasswordException("Password is mismatched");

        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() ->
                new UserNotFoundException("User cannot be found"));

        if(!user.matchPassword(requestDto.getPassword()))
            throw new InvalidPasswordException("Password is invalid");

        user.modifyPassword(requestDto.getNewPassword());
        user.encodePassword();

        return UserDto.builder().build();
    }

    @Transactional
    public UserDto modifyPassword(String userId, UserDto requestDto) {
        User user = userRepository.findById(userId).get();
        user.modifyPassword(requestDto.getNewPassword());
        user.encodePassword();
        return UserDto.builder().build();
    }
}
