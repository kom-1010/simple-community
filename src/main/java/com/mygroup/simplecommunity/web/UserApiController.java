package com.mygroup.simplecommunity.web;

import com.mygroup.simplecommunity.exception.ErrorType;
import com.mygroup.simplecommunity.exception.MissingMandatoryPropertyException;
import com.mygroup.simplecommunity.service.UserService;
import com.mygroup.simplecommunity.web.dto.ErrorResponseDto;
import com.mygroup.simplecommunity.web.dto.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.mygroup.simplecommunity.exception.ErrorType.MISSING_MANDATORY_PROPERTY;
import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserApiController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto requestDto){
        UserDto responseDto = userService.signup(requestDto);
        return ResponseEntity.status(CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto requestDto) throws Exception {
        UserDto responseDto = userService.login(requestDto);
        return ResponseEntity.status(CREATED).body(responseDto);
    }

    @GetMapping("/find-email")
    public ResponseEntity<?> findEmailByNameAndPhone(@RequestBody UserDto requestDto){
        UserDto responseDto = userService.findEmailByNameAndPhone(requestDto);
        return ResponseEntity.status(OK).body(responseDto);
    }

    @GetMapping("/find-pw")
    public ResponseEntity<?> findEmailByEmailAndPhone(@RequestBody UserDto requestDto){
        UserDto responseDto = userService.findEmailByEmailAndPhone(requestDto);
        return ResponseEntity.status(OK).body(responseDto);
    }

    @PutMapping("/find-pw")
    public ResponseEntity<?> modifyPassword(@RequestBody UserDto requestDto){
        UserDto responseDto = userService.modifyPassword(requestDto);
        return ResponseEntity.status(CREATED).body(responseDto);
    }

    @PutMapping
    public ResponseEntity<?> modify(@AuthenticationPrincipal String userId, @RequestBody UserDto requestDto){
        UserDto responseDto;
        // dto에 password가 있다면 modifyPassword 실행
        if(requestDto.getPassword() != null)
            responseDto = userService.modifyPassword(userId, requestDto);
        // dto에 password가 없다면 modifyProfile 실행
        else if(requestDto.getName() != null)
            responseDto = userService.modifyProfile(userId, requestDto);
        else
            throw new MissingMandatoryPropertyException("Property is null");

        return ResponseEntity.status(CREATED).body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<?> remove(@AuthenticationPrincipal String userId){
        UserDto responseDto = userService.remove(userId);
        return ResponseEntity.status(OK).body(responseDto);
    }
}
