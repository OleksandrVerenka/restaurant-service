package com.example.restaurantservice.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class Controller {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/restaurant-reservations")
    public List<String> getAllReservations() {
        ResponseEntity<Object[]> entity = restTemplate.getForEntity("http://reservation-service/reservations", Object[].class);
        Object[] objects = entity.getBody();
        ObjectMapper mapper = new ObjectMapper();
        List<String> reservationNames = Arrays.stream(objects)
                .map(object -> mapper.convertValue(object, Reservation.class))
                .filter(r -> !r.isDone())
                .map(Reservation::getName)
                .collect(Collectors.toList());

        return reservationNames;
    }

    @GetMapping("/restaurant-reservations/{id}")
    public String getReservationById(@PathVariable("id") Long id) {
        Reservation reservation = restTemplate.getForObject("http://reservation-service/reservations/" + id, Reservation.class);
        return reservation.getName();
    }

    @GetMapping("/restaurant-reservations/done/{id}")
    public String doneReservation(@PathVariable("id") Long id) {
        Reservation reservation = restTemplate.getForObject("http://reservation-service/reservations/done/" + id, Reservation.class);
        return reservation.getName();
    }
}
