package com.zatec.technical_challenge.controller;

import com.zatec.technical_challenge.api.SwapiApi;
import com.zatec.technical_challenge.dto.PeopleDto;
import com.zatec.technical_challenge.dto.PeopleResponseDto;
import com.zatec.technical_challenge.exception.ZatecException;
import com.zatec.technical_challenge.service.SwapiService;
import com.zatec.technical_challenge.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SwapiController implements SwapiApi {

    private final SwapiService swapiService;

    @Override
    public ResponseEntity<PeopleResponseDto> getAllStarWarsPeople() {
        PeopleResponseDto peopleResponseDto = new PeopleResponseDto();

        try {
            List<PeopleDto> peopleDtoList = swapiService.fetchStarWarsPeople();
            peopleResponseDto.setMessage(Constant.SUCCESS_MESSAGE);
            peopleResponseDto.setData(peopleDtoList);
            return ResponseEntity.ok(peopleResponseDto);
        } catch (ZatecException e) {
            peopleResponseDto.setMessage(e.getMessage());
            peopleResponseDto.setData(Collections.emptyList());
            return ResponseEntity.status(e.getHttpStatus()).body(peopleResponseDto);
        }
    }
}
