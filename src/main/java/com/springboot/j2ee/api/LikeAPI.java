package com.springboot.j2ee.api;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.LikeDTO;
import com.springboot.j2ee.entity.Like;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikeAPI {
    private final LikeService likeService;

    public LikeAPI(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("likePost")
    public ResponseEntity<String> likePost(@RequestParam Long idPost, @AuthenticationPrincipal CustomUser principal){
        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setIdPost(idPost);
        likeDTO.setIdUser(principal.getUser().getId());
        likeService.saveLike(likeDTO);
        return new ResponseEntity<>("LIke thanh cong", HttpStatus.CREATED);
    }

    @PostMapping("dislikePost")
    public ResponseEntity<String> dislikePost(@RequestParam Long idPost, @AuthenticationPrincipal CustomUser principal){
        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setIdPost(idPost);
        likeDTO.setIdUser(principal.getUser().getId());

        Like like = likeService.findLike(likeDTO);

        likeService.disLike(like.getId());
        return new ResponseEntity<>("Bo Like thanh cong", HttpStatus.CREATED);
    }


}
