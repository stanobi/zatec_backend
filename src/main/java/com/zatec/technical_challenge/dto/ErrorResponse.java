package com.zatec.technical_challenge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private String timestamp;
    private String status;
    private String error;
    private String message;
    private String path;

}
