package com.unicuaca.asst.unicauca_asst.common.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private HttpStatus status;
    private String message;
    private T data;
    private Integer code;

}
