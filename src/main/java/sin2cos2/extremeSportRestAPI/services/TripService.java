package sin2cos2.extremeSportRestAPI.services;

import sin2cos2.extremeSportRestAPI.api.v1.dtos.TripDto;

import java.time.LocalDate;
import java.util.Set;

public interface TripService {
    Set<TripDto> getAllTrips(Long locationId, Long sportId, LocalDate startDate, LocalDate endDate, Integer page);

    TripDto getTripById(Long tripId);

    TripDto saveTrip(Long locationId, Long sportId, TripDto tripDto);

    TripDto updateTrip(Long tripId, TripDto tripDto);

    TripDto patchTrip(Long tripId, TripDto tripDto);

    void deleteTrip(Long tripId);

    void deleteAllTripsByParams(Long locationId, Long sportId, LocalDate startDate, LocalDate endDate);
}
