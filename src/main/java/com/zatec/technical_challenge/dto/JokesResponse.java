package com.zatec.technical_challenge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JokesResponse {

    private Integer total;
    private List<JokesDto> result;

}
