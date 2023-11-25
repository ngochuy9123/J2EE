package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.dto.CommentDetailDTO;
import com.springboot.j2ee.dto.LikeDTO;
import com.springboot.j2ee.dto.PostInfoDTO;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.*;
import com.springboot.j2ee.enums.EPostVisibility;
import com.springboot.j2ee.repository.PostRepository;
import com.springboot.j2ee.repository.UserRepository;
import com.springboot.j2ee.service.CommentService;
import com.springboot.j2ee.service.FriendService;
import com.springboot.j2ee.service.LikeService;
import com.springboot.j2ee.service.PostService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FriendService friendService;
    private final CommentService commentService;
    private final LikeService likeService;

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, FriendService friendService, CommentService commentService, LikeService likeService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.friendService = friendService;
        this.commentService = commentService;
        this.likeService = likeService;
    }


    @Override
    public Post getInfoPost(Long id) {
        return postRepository.findById(id).get();
    }

    @Override
    public List<Post> getAllPost(Long idCurrentUser) {
        List<Friend> lstFriend = friendService.displayListFriend(idCurrentUser);
        List<Long> lstIdFriend = new ArrayList<>();
        User user = userRepository.findById(idCurrentUser).get();

        for (Friend f:lstFriend) {
             if (!Objects.equals(f.getUserTo().getId(), idCurrentUser)){
                 lstIdFriend.add(f.getUserTo().getId());
             }else{
                 lstIdFriend.add(f.getUserFrom().getId());
             }

        }
        List<Post> lstPostFriend = new ArrayList<>();
        for (Long idUser:lstIdFriend
             ) {
            User user1 = userRepository.findById(idUser).get();

            List<Post> lsTemp = postRepository.findByUserAndVisibleOrderByCreatedAtDesc(user1, EPostVisibility.FRIENDS);
            lstPostFriend.addAll(lsTemp);
        }

        List<Post> lstPostPublic = postRepository.findByVisibleOrderByCreatedAtDesc(EPostVisibility.PUBLIC);
        lstPostPublic.addAll(lstPostFriend);
        lstPostPublic.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        List<Post> lstPostCurrentUser = postRepository.findByUserOrderByCreatedAtDesc(user);
        lstPostPublic.addAll(lstPostCurrentUser);

        lstPostPublic.sort(Comparator.comparing(Post::getId).reversed());

        return lstPostPublic;
    }

    @Override
    public List<Post> getPostByIdUser(Long id) {
        User user = userRepository.findById(id).get();
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public List<Post> findPost(String filter) {

        return postRepository.findPostByContent(filter);
    }

    @Override
    public PostInfoDTO getOnePost(Long id, Long idUser) {
        PostInfoDTO postInfoDTO = new PostInfoDTO();
        Post post = postRepository.findById(id).get();

//       Get Da like hay chua like
        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setIdUser(idUser);
        likeDTO.setIdPost(id);
        Like like = likeService.findLike(likeDTO);
        if (like != null){
            postInfoDTO.setLiked(true);
        }
        else{
            postInfoDTO.setLiked(false);
        }



//       List Comment
        List<Comment> lstCmt = commentService.getCommentByPost(id);

        List<CommentDetailDTO> lstCommentDetailDTO = new ArrayList<>();
        for (Comment cmt:lstCmt) {
            CommentDetailDTO commentDetailDTO =new CommentDetailDTO();
            commentDetailDTO.setContentComment(cmt.getContent());
            commentDetailDTO.setCreate_at(cmt.getCreatedAt());
            commentDetailDTO.setAvatar(cmt.getUser().getAvatar());
            commentDetailDTO.setEmail(cmt.getUser().getEmail());
            commentDetailDTO.setIdUser(cmt.getUser().getId());

            lstCommentDetailDTO.add(commentDetailDTO);
        }

        postInfoDTO.setLstComment(lstCommentDetailDTO);



        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(post.getUser().getEmail());
        userDTO.setAvatar(post.getUser().getAvatar());
        userDTO.setId(post.getUser().getId());

        postInfoDTO.setUser(userDTO);
        postInfoDTO.setContent(post.getContent());
        postInfoDTO.setId(post.getId());
        postInfoDTO.setNumLikes(likeService.getAllLikeByPostId(post));
        postInfoDTO.setCreated_at(post.getCreatedAt());

        if (post.getImageUrl() == null){
            postInfoDTO.setImage("");
        }
        else{
            postInfoDTO.setImage(post.getImageUrl());
        }


        return postInfoDTO;
    }


}
