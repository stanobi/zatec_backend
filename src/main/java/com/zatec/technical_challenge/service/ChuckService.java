package com.zatec.technical_challenge.service;

import com.zatec.technical_challenge.dto.JokesDto;
import com.zatec.technical_challenge.dto.JokesResponse;
import com.zatec.technical_challenge.exception.ZatecException;
import com.zatec.technical_challenge.rest.ChuckFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChuckService {

    private final ChuckFeignClient chuckFeignClient;

    public List<String> fetchJokeCategories() throws ZatecException {
        try {
            return chuckFeignClient.fetchJokesCategories();
        } catch (Exception  e) {
            log.error("Unable to process request to fetch all joke categories ", e);
            throw new ZatecException(HttpStatus.EXPECTATION_FAILED, "Unable to fetch all joke categories");
        }
    }

    public List<JokesDto> searchJokes(String query) throws ZatecException {
        validateQuery(query);
        try {
            JokesResponse jokesResponse = chuckFeignClient.searchJokes(query);
            return jokesResponse.getResult();
        } catch (Exception e) {
            log.error("Unable to process request to search jokes", e);
            throw new ZatecException(HttpStatus.EXPECTATION_FAILED, "Unable to search jokes");
        }
    }

    private void validateQuery(String query) throws ZatecException {
        if (!StringUtils.hasText(query) || (query.length() < 3 || query.length() > 20)) {
            throw new ZatecException(HttpStatus.BAD_REQUEST, "Invalid query, must be between 3 and 20 characters");
        }
    }

}
