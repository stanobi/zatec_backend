package com.zatec.technical_challenge.rest;

import com.zatec.technical_challenge.dto.PeopleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "swapi-feign-client", url = "https://swapi.dev/api/people/")
public interface SwapiFeignClient {

    @GetMapping
    PeopleResponse fetchPeople(@RequestParam Integer page);

    @GetMapping
    PeopleResponse searchPeople(@RequestParam String search, @RequestParam Integer page);

}
