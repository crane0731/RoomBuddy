package roombudy.roombudy.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import roombudy.roombudy.domain.Blackout;
import roombudy.roombudy.dto.blackout.BlackoutDto;
import roombudy.roombudy.dto.blackout.BlackoutListResponseDto;


import java.time.LocalDate;
import java.util.List;

/**
 * 블랙 아웃 매퍼
 */
@Mapper
public interface BlackoutMapper {

    int save(Blackout blackout);

    void softDelete(Long id);


    List<BlackoutListResponseDto> findAllByRoomId(@Param("roomId")Long roomId,@Param("offset") int offset, @Param("limit") int limit);


    Long countByRoomId(Long roomId);

    List<BlackoutDto> findBlackoutsByDate(@Param("roomId") Long roomId, @Param("targetDate") LocalDate targetDate);
}
