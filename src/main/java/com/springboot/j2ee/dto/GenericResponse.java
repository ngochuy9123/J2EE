package com.springboot.j2ee.dto;

import org.springframework.http.HttpStatus;

public record GenericResponse<T>(String type, T data){};
