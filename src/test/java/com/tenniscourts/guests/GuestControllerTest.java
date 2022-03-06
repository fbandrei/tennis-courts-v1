package com.tenniscourts.guests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class GuestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GuestService guestService;

    @Autowired
    ObjectMapper mapper;

    GuestDTO guest_1 = new GuestDTO(1L, "John");
    GuestDTO guest_2 = new GuestDTO(2L, "Jack");
    GuestDTO guest_3 = new GuestDTO(3L, "James");

    @Test
    public void getAllGuestsTest() throws Exception {
        List<GuestDTO> guests = new ArrayList<>(Arrays.asList(guest_1, guest_2));

        Mockito.when(guestService.findAllGuests()).thenReturn(guests);

        mockMvc.perform(MockMvcRequestBuilders.get("/guests"))
                .andExpect(jsonPath("$[0].name", is("John")))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk());
    }

    @Test
    public void findGuestByIdTest() throws Exception {

        Mockito.when(guestService.findGuestById(1L)).thenReturn(this.guest_1);

        mockMvc.perform(MockMvcRequestBuilders.get("/guests/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("John")));
    }

    @Test
    public void findGuestByNameTest() throws Exception {

        Mockito.when(guestService.findGuestByName("Jack")).thenReturn(this.guest_2);

        mockMvc.perform(MockMvcRequestBuilders.get("/guests/name/Jack"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Jack")));
    }

    @Test
    public void addGuestTest() throws Exception {

        Mockito.when(guestService.addGuest(this.guest_2)).thenReturn(this.guest_2);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(this.guest_2));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void updateGuestNameTest() throws Exception {

        Mockito.when(guestService.updateGuestName(3L, "James")).thenReturn(this.guest_3);

        mockMvc.perform(MockMvcRequestBuilders.patch("/guests/3/James"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("James")));
    }

}
