package com.springboot.j2ee.api;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.AnnounceDTO;
import com.springboot.j2ee.dto.CommentDTO;
import com.springboot.j2ee.entity.Comment;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.enums.EAnnounceStatus;
import com.springboot.j2ee.enums.EAnnounceType;
import com.springboot.j2ee.service.AnnounceService;
import com.springboot.j2ee.service.CommentService;
import com.springboot.j2ee.service.PostService;
import com.springboot.j2ee.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
public class CommentAPI {

    private final CommentService commentService;

    private final UserService userService;
    private final PostService postService;
    private final AnnounceService announceService;


    public CommentAPI(CommentService commentService, UserService userService, PostService postService, AnnounceService announceService) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
        this.announceService = announceService;
    }

    @GetMapping("/createComment")
    public ResponseEntity<String> createComment(@RequestParam String postId, @RequestParam String content,@AuthenticationPrincipal CustomUser principal){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(content);
        Post post = postService.getInfoPost(Long.parseLong(postId));

        Comment cmt = new Comment();
        cmt.setContent(content);
        cmt.setPost(post);
        cmt.setUser(userService.getInfo(principal.getUsername()) );
        cmt.setCreatedAt(timestamp);
        Comment comment = commentService.save(cmt);
//        return new ResponseEntity<>(comment, HttpStatus.CREATED);

//        Tao thong bao comment

//        AnnounceDTO announceDTO = new AnnounceDTO();
//        announceDTO.setCreat_at(timestamp);
//        announceDTO.setEAnnounceType(EAnnounceType.COMMENT);
//        announceDTO.setEAnnounceStatus(EAnnounceStatus.NONE);
//        announceDTO.setIdPost(Long.parseLong(postId));
//        announceDTO.setIdUserFrom(principal.getUser().getId());
//        announceDTO.setIdUserTo(post.getUser().getId());
//        if (announceDTO.getIdUserTo() != announceDTO.getIdUserFrom()){
//            announceService.addAnnounce(announceDTO);
//        }

        return new ResponseEntity<>("Them Thanh Cong",HttpStatus.CREATED);
    }

    @PostMapping("/searchComment")
    @ResponseBody
    public List<CommentDTO> searchComment(@RequestParam(name = "post_id") String post_id, @AuthenticationPrincipal CustomUser principal){
        List<Comment> comments = postService.getInfoPost(Long.parseLong(post_id)).getComments();
        List<CommentDTO> ds = new ArrayList<>();
        comments.forEach(item->ds.add(new CommentDTO(item)));
        ds.sort(Comparator.comparing(CommentDTO::getCreatedAt).reversed());
        return ds;
    }

    @PostMapping("getComment")
    @ResponseBody
    public List<CommentDTO> test(@RequestParam(name = "idPost") Long postid){

        List<Comment> lstCmt = commentService.getCommentByPost(postid);
        List<CommentDTO> ds = new ArrayList<>();
        for (Comment cmt:lstCmt) {
             CommentDTO commentDTO = new CommentDTO();
            commentDTO.setUser_id(cmt.getId());
            commentDTO.setUser_avatar(cmt.getUser().getAvatar());
            commentDTO.setUser_name(cmt.getUser().getUsername());
            commentDTO.setPost_id(postid);
            commentDTO.setCreateAt(cmt.getCreatedAt());
            commentDTO.setContent(cmt.getContent());

            ds.add(commentDTO);
        }
        return ds;
    }




}
