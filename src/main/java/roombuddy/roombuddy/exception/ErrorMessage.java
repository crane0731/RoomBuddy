package roombuddy.roombuddy.exception;

/**
 * 커스텀 에러 메시지
 */
public final class ErrorMessage {

    public static final String NOT_FOUND_MEMBER = "회원을 찾을 수 없습니다.";
    public static final String NOT_FOUND_REFRESH_TOKEN = "리프레쉬 토큰을 찾을 수 없습니다.";
    public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";
    public static final String DUPLICATED_EMAIL = "이메일이 이미 존재합니다.";
    public static final String DUPLICATED_PHONE = "전화번호가 이미 존재합니다.";
    public static final String PASSWORD_MISMATCH = "비밀번호가 일치하지 않습니다.";
    public static final String EXPIRED_TOKEN = "토큰이 만료되었습니다.";
    public static final String LOGOUT_TOKEN = "로그아웃 된 토큰입니다.";
    public static final String ALREADY_LOGOUT_MEMBER = "이미 로그아웃된 회원입니다.";
    public static final String FAILED_VALIDATE_EMAIL="이메일 인증에 실패했습니다.";
    public static final String NO_PERMISSION = "권한이 없습니다.";

    /**
     * 스터디룸
     */
    public static final String NOT_FOUND_ROOM = "스터디룸을 찾을 수 없습니다.";
    public static final String ALREADY_EXISTS_ROOM = "이미 스터디룸이 존재합니다.";
    public static final String ALREADY_DELETED_ROOM="이미 삭제된 스터디룸입니다.";

    /**
     * 예약
     */
    public static final String NOT_FOUND_RESERVATION = "예약 찾을 수 없습니다.";
    public static final String ALREADY_RESERVED = "이미 예약된 자리입니다.";

    /**
     * 블랙 아웃
     */
    public static final String BLACKOUT_TIME = "블랙 아웃된 시간 입니다.";
}