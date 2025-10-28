package roombuddy.roombuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roombuddy.roombuddy.enums.ActiveStatus;
import roombuddy.roombuddy.jpadomain.Room;

import java.util.List;

/**
 * Jpa 스터디룸 레파지토리
 */
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r " +
            "FROM Room r " +
            "where r.activeStatus=:status")
    List<Room> findActiveRooms(ActiveStatus status);
}
