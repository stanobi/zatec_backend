package com.zatec.technical_challenge.service;

import com.zatec.technical_challenge.dto.JokesDto;
import com.zatec.technical_challenge.dto.PeopleDto;
import com.zatec.technical_challenge.dto.SearchResponseDataDto;
import com.zatec.technical_challenge.exception.ZatecException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

class SearchServiceTest {

    @Mock
    private ChuckService chuckService;

    @Mock
    private SwapiService swapiService;

    private SearchService searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        searchService = new SearchService(chuckService, swapiService);
    }

    @Test
    void given_EmptySearchQuery_when_search_should_returnSearchResult() throws ZatecException {

        PeopleDto peopleDto = new PeopleDto();
        peopleDto.setName("sample");
        Mockito.when(swapiService.searchStarWarsPeople(Mockito.nullable(String.class))).thenReturn(Collections.singletonList(peopleDto));

        JokesDto jokesDto = new JokesDto();
        jokesDto.setCategories(Collections.singletonList("animal"));
        Mockito.when(chuckService.searchJokes(Mockito.nullable(String.class))).thenReturn(Collections.singletonList(jokesDto));

        SearchResponseDataDto searchResponseDataDto = searchService.search(null);
        Assertions.assertNotNull(searchResponseDataDto);
        Assertions.assertTrue(searchResponseDataDto.getJokes().isEmpty());
        Assertions.assertFalse(searchResponseDataDto.getPeople().isEmpty());

    }

    @Test
    void given_validSearchQueryButLessThan3Chars_when_search_should_returnSearchResult() throws ZatecException {

        PeopleDto peopleDto = new PeopleDto();
        peopleDto.setName("sample");
        Mockito.when(swapiService.searchStarWarsPeople(Mockito.nullable(String.class))).thenReturn(Collections.singletonList(peopleDto));

        JokesDto jokesDto = new JokesDto();
        jokesDto.setCategories(Collections.singletonList("animal"));
        Mockito.when(chuckService.searchJokes(Mockito.nullable(String.class))).thenReturn(Collections.singletonList(jokesDto));

        SearchResponseDataDto searchResponseDataDto = searchService.search("12");
        Assertions.assertNotNull(searchResponseDataDto);
        Assertions.assertTrue(searchResponseDataDto.getJokes().isEmpty());
        Assertions.assertFalse(searchResponseDataDto.getPeople().isEmpty());

    }

    @Test
    void given_validSearchQueryButGreaterThan2Chars_when_search_should_returnSearchResult() throws ZatecException {

        PeopleDto peopleDto = new PeopleDto();
        peopleDto.setName("sample");
        Mockito.when(swapiService.searchStarWarsPeople(Mockito.nullable(String.class))).thenReturn(Collections.singletonList(peopleDto));

        JokesDto jokesDto = new JokesDto();
        jokesDto.setCategories(Collections.singletonList("animal"));
        Mockito.when(chuckService.searchJokes(Mockito.nullable(String.class))).thenReturn(Collections.singletonList(jokesDto));

        SearchResponseDataDto searchResponseDataDto = searchService.search("123");
        Assertions.assertNotNull(searchResponseDataDto);
        Assertions.assertFalse(searchResponseDataDto.getJokes().isEmpty());
        Assertions.assertFalse(searchResponseDataDto.getPeople().isEmpty());

    }
}