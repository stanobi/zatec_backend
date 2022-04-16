package com.zatec.technical_challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zatec.technical_challenge.dto.PeopleDto;
import com.zatec.technical_challenge.dto.PeopleResponseDto;
import com.zatec.technical_challenge.exception.ZatecException;
import com.zatec.technical_challenge.service.SwapiService;
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
@WebMvcTest(SwapiController.class)
class SwapiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SwapiService swapiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_when_fetchStarWarsPeople_ReturnsAllPeople() throws Exception {
        PeopleDto peopleDto = new PeopleDto();
        peopleDto.setName("sampleName");
        Mockito.when(swapiService.fetchStarWarsPeople()).thenReturn(Collections.singletonList(peopleDto));

        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.get("/swapi/people"));
        Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

        PeopleResponseDto peopleResponseDto = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), PeopleResponseDto.class);
        Assertions.assertNotNull(peopleResponseDto);
        Assertions.assertEquals(Constant.SUCCESS_MESSAGE, peopleResponseDto.getMessage());
        Assertions.assertFalse(peopleResponseDto.getData().isEmpty());
    }

    @Test
    void test_when_fetchStarWarsPeople_throwsException() throws Exception {

        Mockito.when(swapiService.fetchStarWarsPeople()).thenThrow(new ZatecException(HttpStatus.EXPECTATION_FAILED, "Exception occurred"));

        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.get("/swapi/people"));
        Assertions.assertEquals(HttpStatus.EXPECTATION_FAILED.value(), mockHttpServletResponse.getStatus());

        PeopleResponseDto peopleResponseDto = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), PeopleResponseDto.class);
        Assertions.assertNotNull(peopleResponseDto);
        Assertions.assertEquals("Exception occurred", peopleResponseDto.getMessage());
        Assertions.assertTrue(peopleResponseDto.getData().isEmpty());
    }
}