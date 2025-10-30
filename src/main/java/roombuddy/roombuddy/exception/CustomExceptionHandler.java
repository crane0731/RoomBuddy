package roombuddy.roombuddy.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roombuddy.roombuddy.dto.api.ApiResponseDto;

/**
 * 커스텀 예외 핸들러
 */
@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MemberCustomException.class)
    public ResponseEntity<Object> handleLoginCustomException(MemberCustomException ex) {
        log.error("LoginCustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler(JwtCustomException.class)
    public ResponseEntity<Object> handleJwtCustomException(JwtCustomException ex) {
        log.error("JwtCustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler(RoomCustomException.class)
    public ResponseEntity<Object> handleRoomCustomException(RoomCustomException ex) {
        log.error("RoomCustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler(ReservationCustomException.class)
    public ResponseEntity<Object> handleReservationCustomException(ReservationCustomException ex) {
        log.error("ReservationCustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponseDto.error(ex.getMessage()));
    }

    @ExceptionHandler(BlackoutCustomException.class)
    public ResponseEntity<Object> handleBlackoutCustomException(BlackoutCustomException ex) {
        log.error("BlackoutCustomException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponseDto.error(ex.getMessage()));
    }

}
