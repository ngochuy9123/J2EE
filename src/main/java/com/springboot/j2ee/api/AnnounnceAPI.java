package com.springboot.j2ee.api;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.AnnounceDTO;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.enums.EAnnounceStatus;
import com.springboot.j2ee.enums.EAnnounceType;
import com.springboot.j2ee.service.AnnounceService;
import com.springboot.j2ee.service.PostService;
import com.springboot.j2ee.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
public class AnnounnceAPI {
    private final AnnounceService announceService;
    private final PostService postService;




    private final UserService userService;


    public AnnounnceAPI(AnnounceService announceService, PostService postService, UserService userService) {
        this.announceService = announceService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/changeStatusAnnounce")
    @ResponseBody
    public void changeStatusAnnounce(@RequestParam(name = "userId") long userId) {
        announceService.getAnnounceByIdUser(userId);
        announceService.changeToStatusSeen(userId);
    }


    @GetMapping("/addAnnounceComment")
    @ResponseBody
    public void addAnnounceComment(@AuthenticationPrincipal CustomUser principal,@RequestParam String postId){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Post post = postService.getInfoPost(Long.parseLong(postId));

        AnnounceDTO announceDTO = new AnnounceDTO();
        announceDTO.setCreat_at(timestamp);
        announceDTO.setEAnnounceType(EAnnounceType.COMMENT);
        announceDTO.setEAnnounceStatus(EAnnounceStatus.NONE);
        announceDTO.setIdPost(Long.parseLong(postId));
        announceDTO.setIdUserFrom(principal.getUser().getId());
        announceDTO.setIdUserTo(post.getUser().getId());
        if (announceDTO.getIdUserTo() != announceDTO.getIdUserFrom()){
            announceService.addAnnounce(announceDTO);
        }
    }

    @GetMapping("/removeAnnounceLike")
    @ResponseBody
    public void removeAnnounceLike(@AuthenticationPrincipal CustomUser principal,@RequestParam Long idPost){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Post post = postService.getInfoPost(idPost);
        AnnounceDTO announceDTO = new AnnounceDTO();
        announceDTO.setCreat_at(timestamp);
        announceDTO.setEAnnounceType(EAnnounceType.LIKE);
        announceDTO.setEAnnounceStatus(EAnnounceStatus.NONE);
        announceDTO.setIdPost(idPost);
        announceDTO.setIdUserFrom(principal.getUser().getId());
        announceDTO.setIdUserTo(post.getUser().getId());
        if (announceDTO.getIdUserTo() != announceDTO.getIdUserFrom()) {
            announceService.removeAnnounce(announceDTO);
        }
    }

    @GetMapping("/addAnnounceLike")
    @ResponseBody
    public void addAnnounceLike(@AuthenticationPrincipal CustomUser principal,@RequestParam Long idPost){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Post post = postService.getInfoPost(idPost);
        AnnounceDTO announceDTO = new AnnounceDTO();
        announceDTO.setCreat_at(timestamp);
        announceDTO.setEAnnounceType(EAnnounceType.LIKE);
        announceDTO.setEAnnounceStatus(EAnnounceStatus.NONE);
        announceDTO.setIdPost(idPost);
        announceDTO.setIdUserFrom(principal.getUser().getId());
        announceDTO.setIdUserTo(post.getUser().getId());
        if (announceDTO.getIdUserTo() != announceDTO.getIdUserFrom()){
            announceService.addAnnounce(announceDTO);
        }
    }


}
