package com.zatec.technical_challenge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zatec.technical_challenge.dto.ErrorResponse;
import com.zatec.technical_challenge.dto.JokesDto;
import com.zatec.technical_challenge.dto.JokesResponse;
import com.zatec.technical_challenge.exception.ZatecException;
import com.zatec.technical_challenge.rest.ChuckFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

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

    public JokesDto fetchRandomJokeByCategory(String category) throws ZatecException {
        try {
            return chuckFeignClient.randomJoke(category);
        } catch (FeignException e) {
            log.error("Unable to fetch random joke ", e);
            Optional<ErrorResponse> errorResponseOptional = getErrorResponseObject(e);
            if (errorResponseOptional.isPresent()) {
                ErrorResponse errorResponse = errorResponseOptional.get();
                throw new ZatecException(HttpStatus.resolve(Integer.parseInt(errorResponse.getStatus())), errorResponse.getMessage());
            }
            throw new ZatecException(HttpStatus.EXPECTATION_FAILED, "Unable to fetch random joke");
        }
    }

    private Optional<ErrorResponse> getErrorResponseObject(FeignException e) {
        if (StringUtils.hasText(e.contentUTF8()) && e.contentUTF8().contains("{")) {
            try {
                return Optional.of(new ObjectMapper().readValue(e.contentUTF8(), ErrorResponse.class));
            } catch (JsonProcessingException ex) {
                log.error("Unable to convert response to errorObject : {}", e.contentUTF8());
            }
        }

        return Optional.empty();
    }

    private void validateQuery(String query) throws ZatecException {
        if (!StringUtils.hasText(query) || (query.length() < 3 || query.length() > 20)) {
            throw new ZatecException(HttpStatus.BAD_REQUEST, "Invalid query, must be between 3 and 20 characters");
        }
    }

}
