package roombuddy.roombuddy.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import roombuddy.roombuddy.mybatisdomain.Blackout;
import roombuddy.roombuddy.dto.blackout.BlackoutDto;
import roombuddy.roombuddy.dto.blackout.BlackoutListResponseDto;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 블랙 아웃 매퍼
 */
@Mapper
public interface BlackoutMapper {

    int save(Blackout blackout);

    void softDelete(Long id);

    List<BlackoutListResponseDto> findActiveBlackouts(@Param("offset") int offset, @Param("limit") int limit);

    Long countActiveBlackouts();

    List<BlackoutListResponseDto> findAllByRoomId(@Param("roomId")Long roomId,@Param("offset") int offset, @Param("limit") int limit);

    Long countByRoomId(Long roomId);

    List<BlackoutDto> findBlackoutsByDate(@Param("roomId") Long roomId, @Param("targetDate") LocalDate targetDate);

    List<Blackout>findOverlappingBlackouts(@Param("roomId")Long roomId, @Param("startAt")LocalDateTime startAt, @Param("endAt")LocalDateTime endAt);

}
