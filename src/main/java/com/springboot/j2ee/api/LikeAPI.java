package com.springboot.j2ee.api;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.AnnounceDTO;
import com.springboot.j2ee.dto.LikeDTO;
import com.springboot.j2ee.entity.Like;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.enums.EAnnounceStatus;
import com.springboot.j2ee.enums.EAnnounceType;
import com.springboot.j2ee.service.AnnounceService;
import com.springboot.j2ee.service.LikeService;
import com.springboot.j2ee.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@AllArgsConstructor
public class LikeAPI {
    @Autowired
    private final LikeService likeService;

    @Autowired
    private final PostService postService;
    @Autowired
    private final AnnounceService announceService;


    @PostMapping("likePost")
    public ResponseEntity<String> likePost(@RequestParam Long idPost, @AuthenticationPrincipal CustomUser principal){
        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setIdPost(idPost);
        likeDTO.setIdUser(principal.getUser().getId());
        likeService.saveLike(likeDTO);
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


        return new ResponseEntity<>("LIke thanh cong", HttpStatus.CREATED);
    }

    @PostMapping("dislikePost")
    public ResponseEntity<String> dislikePost(@RequestParam Long idPost, @AuthenticationPrincipal CustomUser principal){
        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setIdPost(idPost);
        likeDTO.setIdUser(principal.getUser().getId());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Like like = likeService.findLike(likeDTO);
        likeDTO.setId(like.getId());

        Post post = postService.getInfoPost(idPost);
        AnnounceDTO announceDTO = new AnnounceDTO();
        announceDTO.setCreat_at(timestamp);
        announceDTO.setEAnnounceType(EAnnounceType.LIKE);
        announceDTO.setEAnnounceStatus(EAnnounceStatus.NONE);
        announceDTO.setIdPost(idPost);
        announceDTO.setIdUserFrom(principal.getUser().getId());
        announceDTO.setIdUserTo(post.getUser().getId());

//        announceService.removeAnnounce(announceDTO);
        likeService.disLike(like);
        return new ResponseEntity<>("Bo Like thanh cong", HttpStatus.CREATED);
    }


    @PostMapping("countLikeIdPost")
    public ResponseEntity<String> countLikeIdPost(@RequestParam Long post_id, @AuthenticationPrincipal CustomUser principal){
        Post post = postService.getInfoPost(post_id);
        long likes = likeService.getAllLikeByPostId(post);
        return ResponseEntity.ok(String.valueOf(likes));
    }

    @PostMapping("postLiked")
    public ResponseEntity<Boolean> findPostLiked(@RequestParam Long post_id, @AuthenticationPrincipal CustomUser principal){
        Post post = postService.getInfoPost(post_id);

        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setIdUser(principal.getUser().getId());
        likeDTO.setIdPost(post.getId());

        Like like = likeService.findLike(likeDTO);

        if (like != null){
            return ResponseEntity.ok(true);
        }
        else{
            return ResponseEntity.ok(false);
        }

    }




}
