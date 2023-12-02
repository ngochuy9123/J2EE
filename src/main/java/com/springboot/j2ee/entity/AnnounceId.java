package com.springboot.j2ee.entity;

import com.springboot.j2ee.enums.EAnnounceType;
import lombok.Data;

import java.io.Serializable;
public class AnnounceId implements Serializable {
    long id;
    EAnnounceType eAnnounceType;
}
