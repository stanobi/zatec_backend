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
import org.springframework.http.HttpStatus;

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
        ZatecException zatecException = Assertions.assertThrows(ZatecException.class, () -> chuckService.fetchJokeCategories());
        Assertions.assertEquals(HttpStatus.EXPECTATION_FAILED, zatecException.getHttpStatus());
    }

    @Test
    void given_emptySearchQuery_when_searchJokes_should_throwException() {
        ZatecException zatecException = Assertions.assertThrows(ZatecException.class, () -> chuckService.searchJokes(""));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, zatecException.getHttpStatus());
    }

    @Test
    void given_InvalidSearchQuery_when_searchJokes_should_throwException() {
        ZatecException zatecException = Assertions.assertThrows(ZatecException.class, () -> chuckService.searchJokes("1"));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, zatecException.getHttpStatus());
        ZatecException anotherZatecException = Assertions.assertThrows(ZatecException.class, () -> chuckService.searchJokes("12345678901234567890123"));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, anotherZatecException.getHttpStatus());
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
        Mockito.when(chuckFeignClient.searchJokes(Mockito.anyString())).thenThrow(new FeignException.InternalServerError( "Internal Server Error", Request.create(Request.HttpMethod.GET,"/", new HashMap<>(), null, null,null), null, new HashMap<>()));
        ZatecException zatecException = Assertions.assertThrows(ZatecException.class, () -> chuckService.searchJokes("12345"));
        Assertions.assertEquals(HttpStatus.EXPECTATION_FAILED, zatecException.getHttpStatus());
    }

    @Test
    void given_validCategoryAsQuery_when_randomJoke_andApiReturns200_OK_should_returnRandomJoke() throws ZatecException {
        JokesDto jokesDto = new JokesDto();
        jokesDto.setCategories(List.of("animal"));
        jokesDto.setCreatedAt("2020-01-05 13:42:19.576875");
        jokesDto.setIconUrl("https://assets.chucknorris.host/img/avatar/chuck-norris.png");
        jokesDto.setId("xwjic1sws_yohsfefndaiw");
        jokesDto.setUpdatedAt("2020-01-05 13:42:19.576875");
        jokesDto.setUrl("https://api.chucknorris.io/jokes/xwjic1sws_yohsfefndaiw");
        jokesDto.setValue("Chuck Norris once kicked a horse in the chin. Its decendants are known today as Giraffes.");
        Mockito.when(chuckFeignClient.randomJoke(Mockito.anyString())).thenReturn(jokesDto);

        JokesDto randomJoke = chuckService.fetchRandomJokeByCategory("animal");
        Assertions.assertNotNull(randomJoke);
        Assertions.assertEquals(jokesDto.getValue(), randomJoke.getValue());
    }

    @Test
    void given_noCategoryAsQuery_when_randomJoke_andApiReturns200_OK_should_returnRandomJoke() throws ZatecException {
        JokesDto jokesDto = new JokesDto();
        jokesDto.setCategories(List.of("animal"));
        jokesDto.setCreatedAt("2020-01-05 13:42:19.576875");
        jokesDto.setIconUrl("https://assets.chucknorris.host/img/avatar/chuck-norris.png");
        jokesDto.setId("xwjic1sws_yohsfefndaiw");
        jokesDto.setUpdatedAt("2020-01-05 13:42:19.576875");
        jokesDto.setUrl("https://api.chucknorris.io/jokes/xwjic1sws_yohsfefndaiw");
        jokesDto.setValue("Chuck Norris once kicked a horse in the chin. Its decendants are known today as Giraffes.");
        Mockito.when(chuckFeignClient.randomJoke(Mockito.nullable(String.class))).thenReturn(jokesDto);

        JokesDto randomJoke = chuckService.fetchRandomJokeByCategory(null);
        Assertions.assertNotNull(randomJoke);
        Assertions.assertEquals(jokesDto.getValue(), randomJoke.getValue());
    }

    @Test
    void given_InvalidCategoryAsQuery_when_randomJoke_ApiReturns404_NotFound_should_throwException() {
        String responseObject = "{\n" +
                "    \"timestamp\": \"2022-04-17T12:24:49.485Z\",\n" +
                "    \"status\": 404,\n" +
                "    \"error\": \"Not Found\",\n" +
                "    \"message\": \"No jokes for category \\\"an\\\" found.\",\n" +
                "    \"path\": \"/jokes/random\"\n" +
                "}";
        Mockito.when(chuckFeignClient.randomJoke(Mockito.anyString())).thenThrow(new FeignException.NotFound( "Not found", Request.create(Request.HttpMethod.GET,"/", new HashMap<>(), null, null,null), responseObject.getBytes(), new HashMap<>()));
        ZatecException zatecException = Assertions.assertThrows(ZatecException.class, () -> chuckService.fetchRandomJokeByCategory("ani"));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, zatecException.getHttpStatus());
        Assertions.assertEquals("No jokes for category \"an\" found.", zatecException.getMessage());
    }

    @Test
    void given_validCategoryAsQuery_when_randomJoke_andApiReturns500_InternalServerError_should_throwException() {
        Mockito.when(chuckFeignClient.randomJoke(Mockito.anyString())).thenThrow(new FeignException.InternalServerError( "Internal Server Error", Request.create(Request.HttpMethod.GET,"/", new HashMap<>(), null, null,null), null, new HashMap<>()));
        ZatecException zatecException = Assertions.assertThrows(ZatecException.class, () -> chuckService.fetchRandomJokeByCategory("ani"));
        Assertions.assertEquals(HttpStatus.EXPECTATION_FAILED, zatecException.getHttpStatus());
    }
}