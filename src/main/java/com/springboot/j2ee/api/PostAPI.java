package com.springboot.j2ee.api;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.LikeDTO;
import com.springboot.j2ee.dto.PostInfoDTO;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.service.CommentService;
import com.springboot.j2ee.service.LikeService;
import com.springboot.j2ee.service.PostService;
import com.springboot.j2ee.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostAPI {
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final LikeService likeService;


    public PostAPI(PostService postService, UserService userService, CommentService commentService, LikeService likeService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.likeService = likeService;
    }


    @PostMapping("getInfoPost")
    public PostInfoDTO getInfoPost(@RequestParam Long idPost){
        PostInfoDTO postInfoDTO = postService.getOnePost(idPost);
        return postInfoDTO;
    }

    @GetMapping("helloPost")
    public PostInfoDTO helloPost(){
        Long idPost = 24L;
        PostInfoDTO postInfoDTO = postService.getOnePost(idPost);
        System.out.println(postInfoDTO.getUser());
        return postInfoDTO;
    }



}
