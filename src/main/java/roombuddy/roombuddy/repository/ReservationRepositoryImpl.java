package roombuddy.roombuddy.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import roombuddy.roombuddy.dto.reservation.SearchReservationCondDto;
import roombuddy.roombuddy.enums.ActiveStatus;
import roombuddy.roombuddy.enums.CreatedType;
import roombuddy.roombuddy.jpadomain.QMember;
import roombuddy.roombuddy.jpadomain.QReservation;
import roombuddy.roombuddy.jpadomain.Reservation;

import java.time.LocalDate;
import java.util.List;

/**
 *  예약 레파지토리 커스텀 구현체
 */
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public ReservationRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<Reservation> findAllReservationsByRoomAndCond(Long roomId, SearchReservationCondDto cond, Pageable pageable) {

        QReservation reservation = QReservation.reservation;
        QMember member = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(reservation.activeStatus.eq(ActiveStatus.ACTIVE));

        builder.and(reservation.member.id.eq(roomId));

        if(cond.getMemberEmail() != null) {
            builder.and(member.email.containsIgnoreCase(cond.getMemberEmail()));
        }
        if(cond.getStatus() != null) {
            builder.and(reservation.status.eq(cond.getStatus()));
        }
        if (cond.getDate() != null) {
            builder.and(
                    Expressions.dateTemplate(
                            LocalDate.class,        // 반환 타입
                            "DATE({0})",            // SQL 함수 표현식
                            reservation.startAt     // 적용할 컬럼
                    ).eq(cond.getDate())
            );
        }

        OrderSpecifier<?> orderSpecifier =
                (cond.getCreated() == null || cond.getCreated() == CreatedType.DESC)
                        ? reservation.createdAt.desc()
                        : reservation.createdAt.asc();

        List<Reservation> content = query
                .select(reservation)
                .from(reservation)
                .join(reservation.member, member).fetchJoin()
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query
                .select(reservation.count())
                .from(reservation)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total!=null ? total : 0);


    }
}
