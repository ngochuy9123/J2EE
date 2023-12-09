package com.springboot.j2ee.api;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.PostInfoDTO;
import com.springboot.j2ee.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostAPI {
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final AnnounceService announceService;


    public PostAPI(PostService postService, UserService userService, CommentService commentService, LikeService likeService, AnnounceService announceService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.announceService = announceService;
    }


    @PostMapping("getInfoPost")
    public PostInfoDTO getInfoPost(@RequestParam Long idPost,@AuthenticationPrincipal CustomUser principal){
        PostInfoDTO postInfoDTO = postService.getOnePost(idPost,principal.getUser().getId());
        return postInfoDTO;
    }

    @PostMapping("deletePost")
    public void deletePost(@RequestParam Long idPost,@AuthenticationPrincipal CustomUser principal){
        PostInfoDTO postInfoDTO = postService.getOnePost(idPost,principal.getUser().getId());
        commentService.deleteCommentByIdPost(idPost);
        likeService.deleteLikeByPost(idPost);
        announceService.deleteAnnounceByIdPost(idPost);
        deleteP(idPost);
    }

    public void deleteP(long id){
        postService.deletePostById(id);
    }


}
