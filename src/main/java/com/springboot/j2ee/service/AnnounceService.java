package com.springboot.j2ee.service;

import com.springboot.j2ee.dto.AnnounceDTO;
import com.springboot.j2ee.entity.Announce;
import com.springboot.j2ee.entity.User;

import java.util.List;

public interface AnnounceService {
    void changeToStatusSeen(long idUserTo);
    Announce addAnnounce(AnnounceDTO announceDTO);
    void removeAnnounce(AnnounceDTO announceDTO);
    List<Announce> getAnnounceByIdUser(long idUserTo);

    Announce getAnnounceById(Long id);


}
