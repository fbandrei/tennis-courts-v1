package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
@Api(value = "GuestController")
@RequestMapping("/guests")
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @ApiOperation(value = "Add a new guest", tags = "addGuest")
    @PostMapping()
    public ResponseEntity<Void> addGuest(@ApiParam(value = "DTO for creating a guest") @RequestBody GuestDTO guestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.addGuest(guestDTO).getId())).build();
    }

    @ApiOperation(value = "Retrieve guest by id", response = GuestDTO.class, tags = "findGuestById")
    @GetMapping("/id/{guestId}")
    public ResponseEntity<GuestDTO> findGuestById(@PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.findGuestById(guestId));
    }

    @ApiOperation(value = "Retrieve guest by name", response = GuestDTO.class, tags = "findGuestByName")
    @GetMapping("/name/{guestName}")
    public ResponseEntity<GuestDTO> findGuestByName(@PathVariable String guestName) {
        return ResponseEntity.ok(guestService.findGuestByName(guestName));
    }

    @ApiOperation(value = "Delete guest by id", response = GuestDTO.class, tags = "deleteGuest")
    @DeleteMapping("/{guestId}")
    public ResponseEntity<GuestDTO> deleteGuest(@PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.deleteGuest(guestId));
    }

    @ApiOperation(value = "Update guest's name", response = GuestDTO.class, tags = "updateGuest")
    @PatchMapping("/{guestId}/{guestName}")
    public ResponseEntity<GuestDTO> updateGuest(@PathVariable Long guestId, @PathVariable String guestName) {
        return ResponseEntity.ok(guestService.updateGuestName(guestId, guestName));
    }

    @ApiOperation(value = "Retrieve all guests", response = Iterable.class, tags = "getAllGuests")
    @GetMapping()
    public ResponseEntity<List<GuestDTO>> getAllGuests() {
        return ResponseEntity.ok(guestService.findAllGuests());
    }
}
