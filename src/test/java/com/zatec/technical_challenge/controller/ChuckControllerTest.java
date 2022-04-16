package com.zatec.technical_challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zatec.technical_challenge.dto.CategoryResponseDto;
import com.zatec.technical_challenge.exception.ZatecException;
import com.zatec.technical_challenge.service.ChuckService;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChuckController.class)
class ChuckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChuckService chuckService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_when_fetchJokeCategories_ReturnsAllJokeCategories() throws Exception {
        Mockito.when(chuckService.fetchJokeCategories()).thenReturn(Arrays.asList("animal", "test"));

        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.get("/chuck/categories"));
        Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

        CategoryResponseDto categoryResponseDto = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), CategoryResponseDto.class);
        Assertions.assertNotNull(categoryResponseDto);
        Assertions.assertEquals(Constant.SUCCESS_MESSAGE, categoryResponseDto.getMessage());
        Assertions.assertFalse(categoryResponseDto.getData().isEmpty());
    }

    @Test
    void test_when_fetchJokeCategories_throwsException() throws Exception {
        Mockito.when(chuckService.fetchJokeCategories()).thenThrow(new ZatecException(HttpStatus.EXPECTATION_FAILED, "Exception occurred"));

        MockHttpServletResponse mockHttpServletResponse = getMockMvc(mockMvc, MockMvcRequestBuilders.get("/chuck/categories"));
        Assertions.assertEquals(HttpStatus.EXPECTATION_FAILED.value(), mockHttpServletResponse.getStatus());

        CategoryResponseDto categoryResponseDto = new ObjectMapper().readValue(mockHttpServletResponse.getContentAsString(), CategoryResponseDto.class);
        Assertions.assertNotNull(categoryResponseDto);
        Assertions.assertEquals("Exception occurred", categoryResponseDto.getMessage());
        Assertions.assertTrue(categoryResponseDto.getData().isEmpty());
    }

    public static MockHttpServletResponse getMockMvc(MockMvc mockMvc, MockHttpServletRequestBuilder mockHttpServletRequestBuilder)
            throws Exception {
        return mockMvc.perform(mockHttpServletRequestBuilder
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();
    }
}