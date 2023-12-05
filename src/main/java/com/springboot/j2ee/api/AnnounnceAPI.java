package com.springboot.j2ee.api;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.AnnounceDTO;
import com.springboot.j2ee.dto.GenericResponse;
import com.springboot.j2ee.entity.Announce;
import com.springboot.j2ee.service.AnnounceService;
import com.springboot.j2ee.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AnnounnceAPI {
    private final AnnounceService announceService;





    private final UserService userService;


    public AnnounnceAPI(AnnounceService announceService, UserService userService) {
        this.announceService = announceService;
        this.userService = userService;
    }

    @PostMapping("/changeStatusAnnounce")
    @ResponseBody
    public void changeStatusAnnounce(@RequestParam(name = "userId") long userId) {
        announceService.getAnnounceByIdUser(userId);
        announceService.changeToStatusSeen(userId);
    }

    @GetMapping("/api/notification")
    public ResponseEntity<List<AnnounceDTO>> getAnnouncement(@AuthenticationPrincipal CustomUser user) {
        List<Announce> announces = announceService.getAnnounceByIdUser(user.getUser().getId());
        var announcesDtos = announces.stream().map(AnnounceDTO::new).toList();
        return new ResponseEntity<>(announcesDtos, HttpStatus.OK);

    }

}
