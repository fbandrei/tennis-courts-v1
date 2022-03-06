package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;

    private final GuestMapper guestMapper;

    public GuestDTO addGuest(GuestDTO guestDTO) {
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guestDTO)));
    }

    public GuestDTO findGuestById(Long id) {
        return guestRepository.findById(id).map(guestMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Guest not found by ID");
        });
    }

    public GuestDTO findGuestByName(String name) {
        return guestRepository.findByName(name).map(guestMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Guest not found by name");
        });
    }

    public GuestDTO updateGuestName(Long id, final String name) {
        Guest guest = guestRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Guest not found");
        });
        guest.setName(name);
        guestRepository.save(guest);
        return guestMapper.map(guest);
    }

    public GuestDTO deleteGuest(Long id) {
        Guest guest = guestRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Guest not found");
        });
        guestRepository.deleteById(id);
        return guestMapper.map(guest);
    }

    public List<GuestDTO> findAllGuests() {
        return guestRepository.findAll().stream().map(guestMapper::map).collect(Collectors.toList());
    }
}
