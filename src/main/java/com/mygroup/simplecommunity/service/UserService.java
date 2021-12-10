package com.mygroup.simplecommunity.service;

import com.mygroup.simplecommunity.domain.user.User;
import com.mygroup.simplecommunity.domain.user.UserRepository;
import com.mygroup.simplecommunity.exception.DuplicatePropertyException;
import com.mygroup.simplecommunity.exception.ErrorType;
import com.mygroup.simplecommunity.exception.LoginFailException;
import com.mygroup.simplecommunity.exception.MissingMandatoryPropertyException;
import com.mygroup.simplecommunity.security.TokenProvider;
import com.mygroup.simplecommunity.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mygroup.simplecommunity.exception.ErrorType.DUPLICATE_PROPERTY;
import static com.mygroup.simplecommunity.exception.ErrorType.MISSING_MANDATORY_PROPERTY;

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

    @Transactional
    public UserDto login(UserDto userDto) throws Exception {
        // dto에서 email, password가 존재하는지 확인
        // 존재하지 않다면 MissingMandatoryException 발생
        if(userDto.getEmail() == null)
            throw new MissingMandatoryPropertyException("Email is mandatory");
        else if(userDto.getPassword() == null)
            throw new MissingMandatoryPropertyException("Password is mandatory");

        // email로 유저 정보 가져오기
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
                new LoginFailException("Email is invalid"));

        // dto로 가져온 password와 user의 password 비교
        if(!user.matchPassword(userDto.getPassword()))
            throw new LoginFailException("Password is invalid");

        // user id로 토큰 생성
        String token = tokenProvider.create(user.getId());

        return UserDto.builder().token(token).build();
    }
}
