package com.zatec.technical_challenge.service;

import com.zatec.technical_challenge.dto.PeopleDto;
import com.zatec.technical_challenge.dto.PeopleResponse;
import com.zatec.technical_challenge.exception.ZatecException;
import com.zatec.technical_challenge.rest.SwapiFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SwapiService {

    private final SwapiFeignClient swapiFeignClient;

    public List<PeopleDto> fetchStarWarsPeople() throws ZatecException {

        boolean hasNext = true;
        Integer pageNumber = null;
        List<PeopleDto> peopleDtoList = new ArrayList<>();

        while (hasNext) {
            try {
                PeopleResponse peopleResponse = swapiFeignClient.fetchPeople(pageNumber);
                peopleDtoList.addAll(peopleResponse.getResults());
                pageNumber = getPage(peopleResponse.getNext());
                hasNext = Objects.nonNull(pageNumber);
            } catch (Exception e) {
                log.error("Unable to fetch Starwars People", e);
                throw new ZatecException(HttpStatus.EXPECTATION_FAILED, "Unable to fetch all star wars people");
            }
        }

        return peopleDtoList;
    }

    public List<PeopleDto> searchStarWarsPeople(String search) throws ZatecException {

        boolean hasNext = true;
        Integer pageNumber = null;
        List<PeopleDto> peopleDtoList = new ArrayList<>();

        while (hasNext) {
            try {
                PeopleResponse peopleResponse = swapiFeignClient.searchPeople(search, pageNumber);
                peopleDtoList.addAll(peopleResponse.getResults());
                pageNumber = getPage(peopleResponse.getNext());
                hasNext = Objects.nonNull(pageNumber);
            } catch (Exception e) {
                log.error("Unable to search Starwars People", e);
                throw new ZatecException(HttpStatus.EXPECTATION_FAILED, "Unable to search star wars people");
            }
        }

        return peopleDtoList;
    }

    private Integer getPage(String nextUrl) {
        if (StringUtils.hasText(nextUrl)) {
            String[] split = nextUrl.split("page=");
            return split.length > 1 ? Integer.parseInt(split[1]) : null;
        }
        return null;
    }

}
