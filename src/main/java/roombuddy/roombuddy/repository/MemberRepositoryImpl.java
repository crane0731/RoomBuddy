package roombuddy.roombuddy.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import roombuddy.roombuddy.dto.member.MemberListResponseDto;
import roombuddy.roombuddy.dto.member.SearchMemberCondDto;
import roombuddy.roombuddy.enums.ActiveStatus;
import roombuddy.roombuddy.enums.ReservationStatus;
import roombuddy.roombuddy.jpadomain.QMember;
import roombuddy.roombuddy.jpadomain.QReservation;

import java.util.List;

/**
 * 커스텀 회원 레파지토리 구현체
 */
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<MemberListResponseDto> finAllByCond(SearchMemberCondDto cond, Pageable pageable) {
        QMember member = QMember.member;
        QReservation reservation = QReservation.reservation;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(member.activeStatus.eq(ActiveStatus.ACTIVE));

        if (cond.getName() != null && !cond.getName().isEmpty()) {
            builder.and(member.name.containsIgnoreCase(cond.getName()));
        }
        if (cond.getEmail() != null && !cond.getEmail().isEmpty()) {
            builder.and(member.email.containsIgnoreCase(cond.getEmail()));
        }

        // 예약 수 계산
        NumberExpression<Long> reservationCount = reservation.id.count().coalesce(0L);

        // 정렬 조건 설정
        OrderSpecifier<?> orderSpecifier;

        switch (cond.getSortType()) {
            case HIGH_RESERVATION -> orderSpecifier = reservationCount.desc();
            case LOW_RESERVATION  -> orderSpecifier = reservationCount.asc();
            case CREATED_ASC      -> orderSpecifier = member.createdAt.asc();
            case CREATED_DESC     -> orderSpecifier = member.createdAt.desc();
            default               -> orderSpecifier = member.createdAt.desc(); // 기본 정렬
        }

        // 메인 쿼리
        List<MemberListResponseDto> content = query
                .select(Projections.constructor(MemberListResponseDto.class,
                        member.id,
                        member.email,
                        member.name,
                        reservationCount.as("reservationCount"),
                        member.createdAt
                ))
                .from(member)
                .leftJoin(reservation).on(reservation.member.eq(member)
                        .and(reservation.activeStatus.eq(ActiveStatus.ACTIVE))
                        .and(reservation.status.eq(ReservationStatus.COMPLETED)))
                .where(builder)
                .groupBy(member.id, member.email, member.name, member.createdAt)
                .orderBy(orderSpecifier, member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query.select(member.count())
                .from(member)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }



}
