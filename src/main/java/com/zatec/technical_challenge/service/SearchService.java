package com.zatec.technical_challenge.service;

import com.zatec.technical_challenge.dto.JokesDto;
import com.zatec.technical_challenge.dto.PeopleDto;
import com.zatec.technical_challenge.dto.SearchResponseDataDto;
import com.zatec.technical_challenge.exception.ZatecException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final ChuckService chuckService;
    private final SwapiService swapiService;

    public SearchResponseDataDto search(String query) throws ZatecException {
        SearchResponseDataDto searchResponseDataDto = new SearchResponseDataDto();
        List<JokesDto> jokesDtoList = isValidJokeQuery(query) ? chuckService.searchJokes(query) : Collections.emptyList();
        searchResponseDataDto.setJokes(jokesDtoList);
        List<PeopleDto> peopleDtoList = swapiService.searchStarWarsPeople(query);
        searchResponseDataDto.setPeople(peopleDtoList);
        return searchResponseDataDto;
    }

    public boolean isValidJokeQuery(String query) {
        return StringUtils.hasText(query) && query.length() > 2 && query.length() < 21;
    }

}
