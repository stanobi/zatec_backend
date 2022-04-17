package com.zatec.technical_challenge.controller;

import com.zatec.technical_challenge.api.ChuckApi;
import com.zatec.technical_challenge.dto.CategoryResponseDto;
import com.zatec.technical_challenge.dto.JokeResponseDto;
import com.zatec.technical_challenge.dto.JokesDto;
import com.zatec.technical_challenge.exception.ZatecException;
import com.zatec.technical_challenge.service.ChuckService;
import com.zatec.technical_challenge.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChuckController implements ChuckApi {

    private final ChuckService chuckService;

    @Override
    public ResponseEntity<CategoryResponseDto> getAllJokeCategories() {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        try {
            List<String> jokeCategories = chuckService.fetchJokeCategories();
            categoryResponseDto.setData(jokeCategories);
            categoryResponseDto.setMessage(Constant.SUCCESS_MESSAGE);
            return ResponseEntity.ok(categoryResponseDto);
        } catch (ZatecException e) {
            categoryResponseDto.setData(Collections.emptyList());
            categoryResponseDto.setMessage(e.getMessage());
            return ResponseEntity.status(e.getHttpStatus()).body(categoryResponseDto);
        }
    }

    @Override
    public ResponseEntity<JokeResponseDto> getRandomJoke(String category) {
        JokeResponseDto jokeResponseDto = new JokeResponseDto();
        try {
            JokesDto jokesDto = chuckService.fetchRandomJokeByCategory(category);
            jokeResponseDto.setData(jokesDto);
            jokeResponseDto.setMessage(Constant.SUCCESS_MESSAGE);
            return ResponseEntity.ok(jokeResponseDto);
        } catch (ZatecException e) {
            jokeResponseDto.setMessage(e.getMessage());
            return ResponseEntity.status(e.getHttpStatus()).body(jokeResponseDto);
        }
    }
}
