package com.doit.univplanner.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    DUPLICATE_USERNAME("이미 존재하는 사용자 이름입니다."),
    PASSWORD_MISMATCH("비밀번호가 일치하지 않습니다."),
    INVALID_PLAN("잘못된 계획 정보입니다."),
    COURSE_TIME_CONFLICT("강의 시간이 중복됩니다."),
    INVALID_COURSE("잘못된 강의 정보입니다."),
    LOGIN_FAILED("아이디 또는 비밀번호가 맞지 않습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}