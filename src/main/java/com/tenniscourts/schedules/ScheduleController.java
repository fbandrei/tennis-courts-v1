package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@Controller
@Api(value = "ScheduleController")
@RequestMapping("/schedules")
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @ApiOperation(value = "Add a schedule for a tennis court", tags = "addScheduleTennisCourt")
    @PostMapping()
    public ResponseEntity<Void> addScheduleTennisCourt(@ApiParam(value = "DTO for creating a schedule request") @RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {
        System.out.println(createScheduleRequestDTO.getStartDateTime());
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO).getId())).build();
    }

    @ApiOperation(value = "Get a list of schedules using dates", response = Iterable.class, tags = "findSchedulesByDates")
    @GetMapping("/{startDate}/{endDate}")
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(@PathVariable LocalDate startDate,
                                                                  @PathVariable LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0)), LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    @ApiOperation(value = "Get one schedule using a schedule id", response = ScheduleDTO.class, tags = "findByScheduleId")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
}
