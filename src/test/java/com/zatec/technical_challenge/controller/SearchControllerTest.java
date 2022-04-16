package com.zatec.technical_challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zatec.technical_challenge.dto.JokesDto;
import com.zatec.technical_challenge.dto.PeopleDto;
import com.zatec.technical_challenge.dto.SearchResponseDataDto;
import com.zatec.technical_challenge.dto.SearchResponseDto;
import com.zatec.technical_challenge.exception.ZatecException;
import com.zatec.technical_challenge.service.SearchService;
import com.zatec.technical_challenge.util.Constant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static com.zatec.technical_challenge.controller.ChuckControllerTest.getMockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_when_search_ReturnsSearchResult() throws Exception {

        SearchResponseDataDto searchResponseDataDto = new SearchResponseDataDto();
        PeopleDto peopleDto = new PeopleDto();
        peopleDto.setName("SampleName");
        searchResponseDataDto.setPeople(Collections.singletonList(peopleDto));
        JokesDto jokesDto = new JokesDto();
        jokesDto.setCategories(Collections.singletonList("animal"));
        searchResponseDataDto.setJokes(Collections.singletonList(jokesDto));
        Mockito.when(searchService.search(Mockito.anyString())).thenReturn(searchResponseDataDto);

        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.get("/search?query=samplequery"));
        Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

        SearchResponseDto searchResponseDto = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), SearchResponseDto.class);
        Assertions.assertNotNull(searchResponseDto);
        Assertions.assertEquals(Constant.SUCCESS_MESSAGE, searchResponseDto.getMessage());
        Assertions.assertNotNull(searchResponseDto.getData());
        Assertions.assertFalse(searchResponseDto.getData().getPeople().isEmpty());
        Assertions.assertFalse(searchResponseDto.getData().getJokes().isEmpty());
    }

    @Test
    void test_when_search_throwException() throws Exception {

        Mockito.when(searchService.search(Mockito.anyString())).thenThrow(new ZatecException(HttpStatus.EXPECTATION_FAILED, "Exception occurred"));

        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.get("/search?query=samplequery"));
        Assertions.assertEquals(HttpStatus.EXPECTATION_FAILED.value(), mockHttpServletResponse.getStatus());

        SearchResponseDto searchResponseDto = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), SearchResponseDto.class);
        Assertions.assertNotNull(searchResponseDto);
        Assertions.assertEquals("Exception occurred", searchResponseDto.getMessage());
        Assertions.assertNull(searchResponseDto.getData());
    }
}