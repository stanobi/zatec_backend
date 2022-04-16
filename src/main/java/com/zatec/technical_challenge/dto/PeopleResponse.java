package com.zatec.technical_challenge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PeopleResponse {

    private Integer count;
    private String next;
    private String previous;
    private List<PeopleDto> results;

}
