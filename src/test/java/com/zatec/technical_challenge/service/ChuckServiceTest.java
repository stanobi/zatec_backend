package com.zatec.technical_challenge.service;

import com.zatec.technical_challenge.dto.JokesDto;
import com.zatec.technical_challenge.dto.JokesResponse;
import com.zatec.technical_challenge.exception.ZatecException;
import com.zatec.technical_challenge.rest.ChuckFeignClient;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class ChuckServiceTest {

    @Mock
    private ChuckFeignClient chuckFeignClient;
    private ChuckService chuckService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        chuckService = new ChuckService(chuckFeignClient);
    }

    @Test
    void when_fetchJokeCategories_andApiReturns200_OK_should_returnAllJokeCategories() throws ZatecException {
        Mockito.when(chuckFeignClient.fetchJokesCategories()).thenReturn(Arrays.asList("animal", "career", "celebrity"));
        List<String> jokeCategories = chuckService.fetchJokeCategories();
        Assertions.assertFalse(jokeCategories.isEmpty());
        Assertions.assertTrue(jokeCategories.contains("animal"));
    }

    @Test
    void when_fetchJokesCategories_andApiReturn500_InternalServerError_should_throwException() {
        Mockito.when(chuckFeignClient.fetchJokesCategories()).thenThrow(new FeignException.InternalServerError( "Internal Server Error", Request.create(Request.HttpMethod.GET,"/", new HashMap<>(), null, null,null), null, new HashMap<>()));
        Assertions.assertThrows(ZatecException.class, () -> chuckService.fetchJokeCategories());
    }

    @Test
    void given_emptySearchQuery_when_searchJokes_should_throwException() {
        Assertions.assertThrows(ZatecException.class, () -> chuckService.searchJokes(""));
    }

    @Test
    void given_InvalidSearchQuery_when_searchJokes_should_throwException() {
        Assertions.assertThrows(ZatecException.class, () -> chuckService.searchJokes("1"));
        Assertions.assertThrows(ZatecException.class, () -> chuckService.searchJokes("12345678901234567890123"));
    }

    @Test
    void given_validSearchQuery_when_searchJokes_andApiReturns200_OK_should_returnSearchResult() throws ZatecException {
        JokesResponse jokesResponse = new JokesResponse();
        JokesDto jokesDto = new JokesDto();
        jokesDto.setCategories(Arrays.asList("12345", "test"));
        jokesResponse.setResult(Collections.singletonList(jokesDto));
        Mockito.when(chuckFeignClient.searchJokes(Mockito.anyString())).thenReturn(jokesResponse);

        List<JokesDto> searchResult = chuckService.searchJokes("12345");
        Assertions.assertFalse(searchResult.isEmpty());
    }

    @Test
    void given_validSearchQuery_when_searchJokes_andApiReturns500_InternalServerError_should_returnSearchResult() {
        Mockito.when(chuckFeignClient.fetchJokesCategories()).thenThrow(new FeignException.InternalServerError( "Internal Server Error", Request.create(Request.HttpMethod.GET,"/", new HashMap<>(), null, null,null), null, new HashMap<>()));
        Assertions.assertThrows(ZatecException.class, () -> chuckService.searchJokes("12345"));
    }
}