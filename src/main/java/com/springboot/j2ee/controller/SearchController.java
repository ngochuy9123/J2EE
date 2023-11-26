package com.springboot.j2ee.controller;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.LikeDTO;
import com.springboot.j2ee.dto.SearchHistoryDTO;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.*;
import com.springboot.j2ee.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Controller
public class SearchController {
    private UserService userService;
    private PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final SearchHistoryService searchHistoryService;
    private final FriendService friendService;
    private final AnnounceService announceService;

    public SearchController(UserService userService, PostService postService, SearchHistoryService searchHistoryService,
    CommentService commentService, LikeService likeService, FriendService friendService,
    AnnounceService announceService)
    {
        this.userService = userService;
        this.postService = postService;
        this.searchHistoryService = searchHistoryService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.friendService = friendService;
        this.announceService = announceService;
    }
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
        return "search";
    }
}
