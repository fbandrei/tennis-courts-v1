package com.tenniscourts.reservations;

import com.tenniscourts.schedules.Schedule;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Test
    public void getRefundValueFullRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal(10));
    }

    @Test
    public void getRefundValuePartialRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusHours(15);

        schedule.setStartDateTime(startDateTime);

        Reservation reservation = Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build();

        // 25% fee for canceling or rescheduling (12:00 < 15 < 23:59)
        Assert.assertEquals(reservationService.getRefundValue(reservation), new BigDecimal(7.5));

        startDateTime = startDateTime.minusHours(5);
        schedule.setStartDateTime(startDateTime);
        reservation.setSchedule(schedule);

        // 50% fee for canceling or rescheduling (2:00 < 10 < 11:59)
        Assert.assertEquals(reservationService.getRefundValue(reservation), new BigDecimal(5L));

        startDateTime = startDateTime.minusHours(9);
        schedule.setStartDateTime(startDateTime);
        reservation.setSchedule(schedule);

        // 75% fee for canceling or rescheduling (0:00 < 1 < 1:59)
        Assert.assertEquals(reservationService.getRefundValue(reservation), new BigDecimal(2.5));
    }
}