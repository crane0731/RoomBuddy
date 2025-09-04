package roombudy.roombudy.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import roombudy.roombudy.domain.Room;
import roombudy.roombudy.dto.room.RoomInfoResponseDto;
import roombudy.roombudy.dto.room.RoomListResponseDto;
import roombudy.roombudy.dto.room.UpdateRoomRequestDto;

import java.util.List;
import java.util.Optional;

/**
 * 스터디룸 매퍼
 */
@Mapper
public interface RoomMapper {


    int save(Room room);

    Optional<Room> findById(Long id);

    void softDelete(Long id);

    void update(@Param("id") Long id, @Param("dto") UpdateRoomRequestDto dto);

    Optional<RoomInfoResponseDto> findRoomInfoById(@Param("id") Long id);

    List<RoomListResponseDto> findRoomList();


}
