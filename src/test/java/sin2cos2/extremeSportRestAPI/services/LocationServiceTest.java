package sin2cos2.extremeSportRestAPI.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sin2cos2.extremeSportRestAPI.api.v1.dtos.LocationDto;
import sin2cos2.extremeSportRestAPI.entities.Location;
import sin2cos2.extremeSportRestAPI.repositories.CountryRepository;
import sin2cos2.extremeSportRestAPI.repositories.LocationRepository;
import sin2cos2.extremeSportRestAPI.repositories.RegionRepository;
import sin2cos2.extremeSportRestAPI.repositories.TripRepository;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LocationServiceTest extends ServiceTest{

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private TripRepository tripRepository;

    private LocationService locationService;

    @BeforeEach
    void setUp() {
        CountryService countryService = new CountryServiceImpl(countryRepository);
        RegionService regionService = new RegionServiceImpl(regionRepository, countryService);
        locationService = new LocationServiceImpl(locationRepository, regionService, countryService);
    }

    @Test
    void getLocationsByRegion() {
        Set<LocationDto> locationDtoSet = locationService.getLocationsByRegion(1L);

        assertThat(locationDtoSet.size()).isEqualTo(2);
    }

    @Test
    void getLocationsByCountry() {
        Set<LocationDto> locationDtoSet = locationService.getLocationsByCountry(3L);

        assertThat(locationDtoSet.size()).isEqualTo(3);
    }

    @Test
    void getAllLocations() {
        Set<LocationDto> locationDtoSet = locationService.getAllLocations();

        assertThat(locationDtoSet.size()).isEqualTo(7);
    }

    @Test
    void getLocationDtoById() {
        LocationDto locationDto = locationService.getLocationDtoById(10L);

        assertThat(locationDto.getName()).isEqualTo("LocationTest1");
        assertTrue(locationDto.getRegionURI().contains("/regions/7"));
        assertTrue(locationDto.getCountryURI().contains("/countries/3"));
    }

    @Test
    void getLocationById() {
        Location location = locationService.getLocationById(1L);

        assertThat(location.getName()).isEqualTo("Falesti");
        assertThat(location.getId()).isEqualTo(1L);
        assertThat(location.getCountry().getId()).isEqualTo(2L);
        assertThat(location.getRegion().getId()).isEqualTo(1L);
        assertThat(location.getTrips().size()).isEqualTo(2);
    }

    @Test
    void saveLocation() {
        long locationCount = locationRepository.count();
        LocationDto locationDto = LocationDto.builder().name("testLocation").build();

        LocationDto saved = locationService.saveLocation(locationDto, 1L, 2L);

        assertThat(locationCount).isLessThan(locationRepository.count());
        assertThat(saved.getName()).isEqualTo("testLocation");
        assertThat(saved.getLocationURI()).isNotNull();
        assertTrue(saved.getRegionURI().contains("/regions/1"));
        assertTrue(saved.getCountryURI().contains("/countries/2"));
    }

    @Test
    void updateLocation() {
        LocationDto toUpdate = LocationDto.builder().name("UpdatedHimki").build();

        LocationDto updated = locationService.updateLocation(toUpdate, 4L);

        assertThat(updated.getLocationURI()).isNotNull();
        assertThat(updated.getName()).isEqualTo("UpdatedHimki");
        assertTrue(updated.getRegionURI().contains("/regions/2"));
        assertTrue(updated.getCountryURI().contains("/countries/1"));
    }

    @Test
    void deleteLocation() {
        long count = locationRepository.count();

        locationService.deleteLocation(10L);

        assertThat(count).isGreaterThan(locationRepository.count());
        assertThat(tripRepository.findAllByLocationId(10L).size()).isEqualTo(0);
    }

    @Test
    void deleteLocationsByRegion() {

        locationService.deleteLocationsByRegion(2L);
        assertThat(locationRepository.getLocationByRegionId(2L).size()).isEqualTo(0);
        assertThat(tripRepository.findAllByLocationId(3L).size()).isEqualTo(0);
        assertThat(tripRepository.findAllByLocationId(4L).size()).isEqualTo(0);
    }

    @Test
    void deleteLocationsByCountry() {

        locationService.deleteLocationsByCountry(3L);
        assertThat(locationRepository.getLocationByCountryId(3L).size()).isEqualTo(0);
        assertThat(tripRepository.findAllByLocationId(10L).size()).isEqualTo(0);
    }
}