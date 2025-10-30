package roombuddy.roombuddy.controller.room;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roombuddy.roombuddy.dto.api.ApiResponseDto;
import roombuddy.roombuddy.jpaservice.room.JpaRoomService;
import roombuddy.roombuddy.service.room.RoomService;

/**
 * 스터디룸 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roombuddy/rooms")
public class RoomController {

    private final RoomService roomService;//스터디룸 서비스

    private final JpaRoomService jpaRoomService;//Jpa 스터디룸 서비스

    /**
     * [컨트롤러]
     * 스터디룸 목록 조회
     * @return List<RoomListResponseDto>
     */
    @GetMapping("")
    public ResponseEntity<ApiResponseDto<?>> getAllRooms() {
        return ResponseEntity.ok(ApiResponseDto.success(jpaRoomService.getRoomListForToday()));
    }

    /**
     * [컨트롤러]
     * 스터디룸 상세 조회
     * @param id 룸 아이디
     * @return RoomInfoResponseDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> getRoomInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponseDto.success(jpaRoomService.findRoomInfo(id)));

    }


}
