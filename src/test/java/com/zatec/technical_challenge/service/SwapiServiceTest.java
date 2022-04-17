package com.zatec.technical_challenge.service;

import com.zatec.technical_challenge.dto.PeopleDto;
import com.zatec.technical_challenge.dto.PeopleResponse;
import com.zatec.technical_challenge.exception.ZatecException;
import com.zatec.technical_challenge.rest.SwapiFeignClient;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class SwapiServiceTest {

    @Mock
    private SwapiFeignClient swapiFeignClient;
    private SwapiService swapiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        swapiService = new SwapiService(swapiFeignClient);
    }

    @Test
    void when_fetchStarWarsPeople_andApiReturns200_OK_should_returnAllStarWarsPeople() throws ZatecException {
        PeopleResponse peopleResponse = new PeopleResponse();
        peopleResponse.setCount(1);
        peopleResponse.setNext(null);
        peopleResponse.setPrevious(null);
        PeopleDto peopleDto = new PeopleDto();
        peopleDto.setName("sampleName");
        peopleResponse.setResults(Collections.singletonList(peopleDto));
        Mockito.when(swapiFeignClient.fetchPeople(Mockito.nullable(Integer.class))).thenReturn(peopleResponse);

        List<PeopleDto> peopleList = swapiService.fetchStarWarsPeople();
        Assertions.assertFalse(peopleList.isEmpty());
    }

    @Test
    void when_fetchStarWarsPeopleWithPageNumber_andApiReturns200_OK_should_returnAllStarWarsPeople() throws ZatecException {
        PeopleResponse peopleResponse = new PeopleResponse();
        peopleResponse.setCount(1);
        peopleResponse.setNext("http://url.com?page=2");
        peopleResponse.setPrevious(null);
        PeopleDto peopleDto = new PeopleDto();
        peopleDto.setName("sampleName");
        peopleResponse.setResults(Collections.singletonList(peopleDto));
        Mockito.when(swapiFeignClient.fetchPeople(null)).thenReturn(peopleResponse);

        PeopleResponse peopleResponsePage2 = new PeopleResponse();
        peopleResponsePage2.setNext(null);
        peopleResponsePage2.setPrevious(null);
        peopleResponsePage2.setCount(1);
        peopleResponsePage2.setResults(Collections.singletonList(peopleDto));
        Mockito.when(swapiFeignClient.fetchPeople(2)).thenReturn(peopleResponsePage2);

        List<PeopleDto> peopleList = swapiService.fetchStarWarsPeople();
        Assertions.assertFalse(peopleList.isEmpty());
        Assertions.assertEquals(2, peopleList.size());
    }

    @Test
    void when_fetchStarWarsPeople_andApiReturns500_InternalServerError_should_throwException() {
        Mockito.when(swapiFeignClient.fetchPeople(Mockito.nullable(Integer.class))).thenThrow(new FeignException.InternalServerError( "Internal Server Error", Request.create(Request.HttpMethod.GET,"/", new HashMap<>(), null, null,null), null, new HashMap<>()));
        ZatecException zatecException = Assertions.assertThrows(ZatecException.class, () -> swapiService.fetchStarWarsPeople());
        Assertions.assertEquals(HttpStatus.EXPECTATION_FAILED, zatecException.getHttpStatus());
    }

    @Test
    void when_searchStarWarsPeople_andApiReturn200_OK_should_returnSearchResult() throws ZatecException {
        PeopleResponse peopleResponse = new PeopleResponse();
        peopleResponse.setCount(1);
        peopleResponse.setNext(null);
        peopleResponse.setPrevious(null);
        PeopleDto peopleDto = new PeopleDto();
        peopleDto.setName("sampleName");
        peopleResponse.setResults(Collections.singletonList(peopleDto));
        Mockito.when(swapiFeignClient.searchPeople(Mockito.nullable(String.class), Mockito.nullable(Integer.class))).thenReturn(peopleResponse);

        List<PeopleDto> searchResult = swapiService.searchStarWarsPeople("ani");
        Assertions.assertFalse(searchResult.isEmpty());
    }

    @Test
    void when_searchStarWarsPeopleWithPageNumber_andApiReturn200_OK_should_returnSearchResult() throws ZatecException {
        PeopleResponse peopleResponse = new PeopleResponse();
        peopleResponse.setCount(1);
        peopleResponse.setNext("http://url.com?page=2");
        peopleResponse.setPrevious(null);
        PeopleDto peopleDto = new PeopleDto();
        peopleDto.setName("sampleName");
        peopleResponse.setResults(Collections.singletonList(peopleDto));
        Mockito.when(swapiFeignClient.searchPeople(Mockito.nullable(String.class), Mockito.eq(null))).thenReturn(peopleResponse);

        PeopleResponse peopleResponsePage2 = new PeopleResponse();
        peopleResponsePage2.setNext(null);
        peopleResponsePage2.setPrevious(null);
        peopleResponsePage2.setCount(1);
        peopleResponsePage2.setResults(Collections.singletonList(peopleDto));
        Mockito.when(swapiFeignClient.searchPeople(Mockito.nullable(String.class), Mockito.eq(2))).thenReturn(peopleResponsePage2);

        List<PeopleDto> searchResult = swapiService.searchStarWarsPeople("ani");
        Assertions.assertFalse(searchResult.isEmpty());
        Assertions.assertEquals(2, searchResult.size());
    }

    @Test
    void when_searchStarWarsPeopleWithPageNumber_andApiReturn500_InternalServerError_should_throwException() {
        Mockito.when(swapiFeignClient.searchPeople(Mockito.nullable(String.class), Mockito.nullable(Integer.class))).thenThrow(new FeignException.InternalServerError( "Internal Server Error", Request.create(Request.HttpMethod.GET,"/", new HashMap<>(), null, null,null), null, new HashMap<>()));
        ZatecException zatecException = Assertions.assertThrows(ZatecException.class, () -> swapiService.searchStarWarsPeople(null));
        Assertions.assertEquals(HttpStatus.EXPECTATION_FAILED, zatecException.getHttpStatus());
    }

}