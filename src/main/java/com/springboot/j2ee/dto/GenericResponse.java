package com.springboot.j2ee.dto;

import org.springframework.http.HttpStatus;

public record GenericResponse(HttpStatus status, String data){};
