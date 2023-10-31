package com.springboot.j2ee.api;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.controller.UserController;
import com.springboot.j2ee.entity.Comment;
import com.springboot.j2ee.repository.PostRepository;
import com.springboot.j2ee.repository.UserRepository;
import com.springboot.j2ee.service.CommentService;
import com.springboot.j2ee.service.PostService;
import com.springboot.j2ee.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentAPI {

    private final CommentService commentService;

    private final UserService userService;
    private final PostService postService;

    public CommentAPI(CommentService commentService, UserService userService, PostService postService) {
        this.commentService = commentService;

        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/createComment")
    public ResponseEntity<String> createComment(@RequestParam String postId, @RequestParam String content,@AuthenticationPrincipal CustomUser principal){
        System.out.println(content);
        Comment cmt = new Comment();
        cmt.setContent(content);
        cmt.setPost(postService.getInfoPost(Long.parseLong(postId)));
        cmt.setUser(userService.getInfo(principal.getUsername()) );
        Comment comment = commentService.save(cmt);
//        return new ResponseEntity<>(comment, HttpStatus.CREATED);
        return new ResponseEntity<>("Them Thanh Cong",HttpStatus.CREATED);
    }


}
