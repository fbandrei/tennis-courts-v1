package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        final Reservation r = reservationMapper.map(createReservationRequestDTO);
        r.setValue(BigDecimal.valueOf(10));
        return reservationMapper.map(reservationRepository.saveAndFlush(r));
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    /*
        Small remark about this task:
        "9. As a Tennis Court Admin, I want to keep 25% of the reservation fee if the User cancels or reschedules between 12:00 and 23:59 hours in advance,
        50% between 2:00 and 11:59 in advance, and 75% between 0:01 and 2:00 in advance."

        In the last case for 75% the upper bound should be 1:59 (because 2:00 is already covered at previous case 50%)
     */

    public BigDecimal getRefundValue(Reservation reservation) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours >= 24) {
            return reservation.getValue();
        } else if (hours >= 12 && hours <= 23) {
            BigDecimal fee = reservation.getValue().multiply(BigDecimal.valueOf(25)).divide(BigDecimal.valueOf(100));
            reservation.setCancelOrRescheduleFee(fee);
            return reservation.getValue().subtract(fee);
        } else if (hours >= 2 && hours <= 11) {
            BigDecimal fee = reservation.getValue().multiply(BigDecimal.valueOf(50)).divide(BigDecimal.valueOf(100));
            reservation.setCancelOrRescheduleFee(fee);
            return reservation.getValue().subtract(fee);
        } else if (hours >= 0 && hours <= 1) {
            BigDecimal fee = reservation.getValue().multiply(BigDecimal.valueOf(75)).divide(BigDecimal.valueOf(100));
            reservation.setCancelOrRescheduleFee(fee);
            return reservation.getValue().subtract(fee);
        }

        return BigDecimal.ZERO;
    }

    /*TODO: This method actually not fully working, find a way to fix the issue when it's throwing the error:
            "Cannot reschedule to the same slot.*/

    /*
        I could not figure out what exactly it's the problem with this method.
        Is it that it should not throw the exception at all and handle that scenario differently?
        Meaning that rescheduling to the same slot is perfectly fine?
     */
    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        Reservation previousReservation = cancel(previousReservationId);

        if (scheduleId.equals(previousReservation.getSchedule().getId())) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }

        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }
}
