package com.mygroup.simplecommunity.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygroup.simplecommunity.web.dto.ErrorResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (InvalidTokenException e){
            setErrorResponse(response, e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, InvalidTokenException e) {
        response.setStatus(e.getErrorType().getHttpStatus().value());
        response.setContentType("application/json");
        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .type(e.getErrorType().getType()).message(e.getMessage()).build();
        try{
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseDto));
        } catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
