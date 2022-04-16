package com.zatec.technical_challenge.controller;

import com.zatec.technical_challenge.api.SearchApi;
import com.zatec.technical_challenge.dto.SearchResponseDataDto;
import com.zatec.technical_challenge.dto.SearchResponseDto;
import com.zatec.technical_challenge.exception.ZatecException;
import com.zatec.technical_challenge.service.SearchService;
import com.zatec.technical_challenge.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController implements SearchApi {

    private final SearchService searchService;

    @Override
    public ResponseEntity<SearchResponseDto> getSearchResult(String query) {
        SearchResponseDto searchResponseDto = new SearchResponseDto();
        try {
            SearchResponseDataDto searchResponseDataDto = searchService.search(query);
            searchResponseDto.setMessage(Constant.SUCCESS_MESSAGE);
            searchResponseDto.setData(searchResponseDataDto);
            return ResponseEntity.ok(searchResponseDto);
        } catch (ZatecException e) {
            searchResponseDto.setMessage(e.getMessage());
            return ResponseEntity.status(e.getHttpStatus()).body(searchResponseDto);
        }
    }
}
