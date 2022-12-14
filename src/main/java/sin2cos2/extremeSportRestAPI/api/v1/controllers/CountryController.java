package sin2cos2.extremeSportRestAPI.api.v1.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sin2cos2.extremeSportRestAPI.api.v1.dtos.CountryDto;
import sin2cos2.extremeSportRestAPI.services.CountryService;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;

    @Operation(summary = "Get all countries",
            description = """
                    By default param page will be set with 1.
                                        
                    Specify param name to get set of countries by name.
                    Max size of set is 10.
                    """)
    @GetMapping
    public Set<CountryDto> getAllCountries(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(required = false) String name) {

        if (name != null)
            return countryService.getCountriesByName(name);

        return countryService.getAllCountries(page);
    }

    @Operation(summary = "Get country by id")
    @GetMapping("/{countryId}")
    public CountryDto getCountryById(@PathVariable String countryId) {
        return countryService.getCountryDtoById(Long.valueOf(countryId));
    }

    @Operation(summary = "Save new country")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CountryDto saveCountry(@RequestBody CountryDto countryDto) {
        return countryService.saveCountry(countryDto);
    }

    @Operation(summary = "Update an existing country by id",
            description = "In case of wrong id exception will be throned")
    @PutMapping("/{countryId}")
    public CountryDto updateCountry(@PathVariable String countryId,
                                    @RequestBody CountryDto countryDto) {
        return countryService.updateCountry(Long.valueOf(countryId), countryDto);
    }

    @Operation(summary = "Delete country by id")
    @DeleteMapping("/{countryId}")
    public void deleteCountry(@PathVariable String countryId) {
        countryService.deleteCountry(Long.valueOf(countryId));
    }


}

