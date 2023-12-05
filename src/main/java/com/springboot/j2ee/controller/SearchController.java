package com.springboot.j2ee.controller;

import com.springboot.j2ee.beans.CDCBeans;
import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.*;
import com.springboot.j2ee.entity.*;
import com.springboot.j2ee.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Controller
@AllArgsConstructor
public class SearchController {
    private UserService userService;
    private PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final SearchHistoryService searchHistoryService;
    private final FriendService friendService;
    private final AnnounceService announceService;

    @Autowired
    @Qualifier("cdcBeans")
    private CDCBeans cdcBeans;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private static final String messageSocket = "/topic/general/";



    @GetMapping("search")
    public String index(@RequestParam("filter") String filter, Model model,@AuthenticationPrincipal CustomUser principal){
        String userName = principal.getUsername();

        List<Friend> list_friend_request = friendService.displayFriendRequest(principal.getUser().getId());
        List<Post> lstPost = postService.getAllPost(principal.getUser().getId());

        HashMap<Long,Long> hashLike = new HashMap<Long, Long>();
        HashMap<Long,Boolean> hashLiked = new HashMap<>();

        HashMap<Long,List<Comment>> hashComment = new HashMap<>();
        HashMap<Long,Integer> hashSlgComment = new HashMap<>();

        for(Post p:lstPost){
            long slgLike = likeService.getAllLikeByPostId(p);
            hashLike.put(p.getId(),slgLike);

            LikeDTO likeDTO = new LikeDTO();
            likeDTO.setIdUser(principal.getUser().getId());
            likeDTO.setIdPost(p.getId());
            Like like = likeService.findLike(likeDTO);
            if (like != null){
                hashLiked.put(p.getId(),true);
            }
            else{
                hashLiked.put(p.getId(),false);
            }

            List<Comment> lstComments = commentService.findCommentByPost(p.getId());
            hashComment.put(p.getId(), lstComments);
            hashSlgComment.put(p.getId(),lstComments.size());
        }

        List<Announce> lstAnnounce = announceService.getAnnounceByIdUser(principal.getUser().getId());

        model.addAttribute("lstAnnounce",lstAnnounce);
        model.addAttribute("hashSlgComment",hashSlgComment);
        model.addAttribute("hashComment",hashComment);

        model.addAttribute("hashSlgLike",hashLike);
        model.addAttribute("hashLiked",hashLiked);

        model.addAttribute("user",userService.getUserById(principal.getUser().getId()));
        model.addAttribute("lst_friend_request",list_friend_request);
        model.addAttribute("hashLike",hashLike);
        model.addAttribute("posts",postService.getAllPostFilter(principal.getUser().getId(),filter));



        List<UserDTO> users = userService.searchUser(filter,11l);
        List<Post> posts = postService.findPost(filter);

        long id = principal.getUser().getId();

        boolean isCurrentUser =false;
        if (id == principal.getUser().getId()){
            isCurrentUser=true;
        }

        if (isCurrentUser){
            User user = userService.getInfoById(principal.getUser().getId());
            model.addAttribute("user",user);
        }
        else{
            User user = userService.getInfoById(id);
            model.addAttribute("user",user);
        }

        String str_user = "";
        for (UserDTO u:users) {
            str_user+=u.getEmail();
        }
//        search history
        SearchHistoryDTO searchHistoryDTO = new SearchHistoryDTO(filter,principal.getUser().getId());
        searchHistoryService.saveSearchHistory(searchHistoryDTO);


        model.addAttribute("isCurrentUser", isCurrentUser);
        model.addAttribute("filter",filter);
        model.addAttribute("users",users);
        model.addAttribute("str_user",str_user);

        UUID uuid = UUID.randomUUID();
        var uid = principal.getUser().getId();

        cdcBeans.subscribeToWriteComment(uuid, (c) -> handleComment(c, uuid));
        cdcBeans.subscribeToWriteEmote(uuid, (c) -> handleEmote(c, uuid));
        cdcBeans.subscribeToRemoveEmote(uuid, (c) -> handleEmote(c, uuid));
        cdcBeans.subscribeToWriteAnnounce(uid, uuid, (c) -> handleAnnounce(c, uuid));
        cdcBeans.subscribeToWriteFriend(uid, uuid, (c) -> handleSocketSend(c, "FRIENDS_REQUEST",messageSocket+ uuid,
                FriendRequestDto::new));
        cdcBeans.subscribeToDeleteFriend(id, uuid, (c) -> handleSocketSend(c, "FRIENDS_REQUEST",messageSocket+ uuid,
                FriendRequestDto::new));

        model.addAttribute("principal", principal);
        model.addAttribute("uuid", uuid);

        return "search";
    }


    private void handleComment(Comment comment, UUID uuid) {
        var dto = new CommentDTO(comment);
        var response = new GenericResponse<>("COMMENT", dto);
        simpMessagingTemplate.convertAndSend(messageSocket+uuid, response);
    }
    private void handleEmote(Like like, UUID uuid) {
        var dto = new LikeDTO(like);
        var response = new GenericResponse<>("LIKE", dto);
        simpMessagingTemplate.convertAndSend(messageSocket+uuid, response);
    }
    private void handleAnnounce(Announce announce, UUID uuid) {
        var dto = new AnnounceDTO(announce);
        var response = new GenericResponse<>("NOTIFICATION", dto);
        simpMessagingTemplate.convertAndSend(messageSocket+uuid, response);
    }
    private void handleFriends(Announce announce, UUID uuid) {
        var dto = new AnnounceDTO(announce);
        var response = new GenericResponse<>("NOTIFICATION", dto);
        simpMessagingTemplate.convertAndSend(messageSocket+uuid, response);
    }

    private <T, R> void handleSocketSend(T object, String type, String location, Function<T, R> converter) {
        var dto = converter.apply(object);
        var response = new GenericResponse<>(type, dto);
        simpMessagingTemplate.convertAndSend(location, response);

    }
}
