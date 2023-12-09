package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.dto.CommentDetailDTO;
import com.springboot.j2ee.dto.LikeDTO;
import com.springboot.j2ee.dto.PostInfoDTO;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.*;
import com.springboot.j2ee.enums.EFriendRequest;
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
//        lấy danh sách ban be
        List<Friend> lstFriend = friendService.displayListFriend(idCurrentUser);

//        Lay ra danh sach id cua ban be
        List<Long> lstIdFriend = new ArrayList<>();
        for (Friend f : lstFriend) {
            if (!Objects.equals(f.getUserTo().getId(), idCurrentUser)) {
                lstIdFriend.add(f.getUserTo().getId());
            } else {
                lstIdFriend.add(f.getUserFrom().getId());
            }
        }
//      Danh sach bai post duoc hien thi
        List<Post> postList = new ArrayList<>();
//        Them nhung bai post la ban be
        for (Long idUser : lstIdFriend) {
            User user1 = userRepository.findById(idUser).get();
            List<Post> lsTemp = postRepository.getListUserPostByVisible(user1.getId(), 1, "");
            postList.addAll(lsTemp);
        }
//      Them nhugn bai post ở chế độ công khai
        postList.addAll(postRepository.getAllPublicPostOtherUser(idCurrentUser, ""));

//        Lay nguoi dung hien tai đang đăng nhập
        User user = userRepository.findById(idCurrentUser).get();
        postList.addAll(postRepository.getAllPostByUserId(user.getId()));

//        Sap xep theo thoi gian tao roi toi Id
        postList.sort(Comparator.comparing(Post::getId).reversed());
        postList.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        return postList;
    }

    @Override
    public List<Post> getAllPostByIdUserAndVisible(Long idUser,EPostVisibility ePostVisibility) {
        User user = userRepository.findById(idUser).get();
        return postRepository.findByUserAndVisibleOrderByCreatedAtDesc(user,ePostVisibility);
    }

    @Override
    public List<Post> getAllPostHome(Long id) {
        return null;
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
        if (like != null) {
            postInfoDTO.setLiked(true);
        } else {
            postInfoDTO.setLiked(false);
        }


//       List Comment
        List<Comment> lstCmt = commentService.getCommentByPost(id);

        List<CommentDetailDTO> lstCommentDetailDTO = new ArrayList<>();
        for (Comment cmt : lstCmt) {
            CommentDetailDTO commentDetailDTO = new CommentDetailDTO();
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
        userDTO.setUsername(post.getUser().getUsername());

        postInfoDTO.setUser(userDTO);
        postInfoDTO.setContent(post.getContent());
        postInfoDTO.setId(post.getId());
        postInfoDTO.setNumLikes(likeService.getAllLikeByPostId(post));
        postInfoDTO.setCreated_at(post.getCreatedAt());
        postInfoDTO.createAtFormat = post.getCreatedFormat();

        if (post.getImageUrl() == null) {
            postInfoDTO.setImage("");
        } else {
            postInfoDTO.setImage(post.getImageUrl());
        }


        return postInfoDTO;
    }

    @Override
    public List<Post> getAllPostFilter(Long idCurrentUser, String filter) {
        //        lấy danh sách ban be
        List<Friend> lstFriend = friendService.displayListFriend(idCurrentUser);

//        Lay ra danh sach id cua ban be
        List<Long> lstIdFriend = new ArrayList<>();
        for (Friend f : lstFriend) {
            if (!Objects.equals(f.getUserTo().getId(), idCurrentUser)) {
                lstIdFriend.add(f.getUserTo().getId());
            } else {
                lstIdFriend.add(f.getUserFrom().getId());
            }
        }
//      Danh sach bai post duoc hien thi
        List<Post> postList = new ArrayList<>();
//        Them nhung bai post la ban be
        for (Long idUser : lstIdFriend) {
            User user1 = userRepository.findById(idUser).get();
            List<Post> lsTemp = postRepository.getListUserPostByVisible(user1.getId(), 1, filter);
            postList.addAll(lsTemp);
        }
//      Them nhugn bai post ở chế độ công khai
        postList.addAll(postRepository.getAllPublicPostOtherUser(idCurrentUser, filter));


//        Sap xep theo thoi gian tao roi toi Id
        postList.sort(Comparator.comparing(Post::getId).reversed());
        postList.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        return postList;
    }

    @Override
    public List<Post> getAllPostForProfile(Long idUser, Long idCurrentUser) {

        if (!Objects.equals(idCurrentUser, idUser)){
//            Xac dinh xem co phai la ban be hay khong

            EFriendRequest  eFriendRequest = friendService.checkFriendRequest(idUser,idCurrentUser);
//            Neu la ban be thi lay tat cac bai post o che do friend va public
            if (eFriendRequest == EFriendRequest.FRIEND){
                List<Post> lstFriendPost = getAllPostByIdUserAndVisible(idUser,EPostVisibility.FRIENDS);
                List<Post> lstPublicPost = getAllPostByIdUserAndVisible(idUser,EPostVisibility.PUBLIC);
                lstFriendPost.addAll(lstPublicPost);
//                Sap xep bai post
                lstFriendPost.sort(Comparator.comparing(Post::getId).reversed());
                lstFriendPost.sort(Comparator.comparing(Post::getCreatedAt).reversed());
                return lstFriendPost;
            }
//            Neu khong thi chi lay bai post o che do public
            else{
                return getAllPostByIdUserAndVisible(idUser,EPostVisibility.PUBLIC);
            }

        }
        else{
            return getPostByIdUser(idUser);
        }

    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }


}
