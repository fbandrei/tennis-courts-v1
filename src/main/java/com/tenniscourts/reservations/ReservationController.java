package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@Api(value = "ReservationController")
@RequestMapping("/reservations")
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @ApiOperation(value = "Book a reservation on a tennis court", tags = "bookReservation")
    @PostMapping()
    public ResponseEntity<Void> bookReservation(@ApiParam(value = "DTO for creating a reservation request") @RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        System.out.println(createReservationRequestDTO.getGuestId());
        System.out.println(createReservationRequestDTO.getScheduleId());
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @ApiOperation(value = "Get a booked reservation", response = ReservationDTO.class, tags = "findReservation")
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @ApiOperation(value = "Cancel a reservation", response = ReservationDTO.class, tags = "cancelReservation")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @ApiOperation(value = "Reschedule a reservation", response = ReservationDTO.class, tags = "rescheduleReservation")
    @PutMapping("/{reservationId}/{scheduleId}")
    public ResponseEntity<ReservationDTO> rescheduleReservation(@PathVariable Long reservationId, @PathVariable Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
