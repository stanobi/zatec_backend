package com.zatec.technical_challenge.rest;

import com.zatec.technical_challenge.dto.JokesDto;
import com.zatec.technical_challenge.dto.JokesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "chuck-feign-client", url = "https://api.chucknorris.io/jokes")
public interface ChuckFeignClient {

    @GetMapping("/categories")
    List<String> fetchJokesCategories();

    @GetMapping("/search")
    JokesResponse searchJokes(@RequestParam String query);
    
    @GetMapping("/random")
    JokesDto randomJoke(@RequestParam String category);

}
