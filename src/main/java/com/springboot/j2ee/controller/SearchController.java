package com.springboot.j2ee.controller;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.PostDTO;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.service.PostService;
import com.springboot.j2ee.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    private UserService userService;
    private PostService postService;
    public SearchController(UserService userService,PostService postService){
        this.userService = userService;
        this.postService = postService;
    }
    @GetMapping("search")
    public String index(@RequestParam("filter") String filter, Model model,@AuthenticationPrincipal CustomUser principal){
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
        for (UserDTO u:
             users) {
            str_user+=u.getEmail();
        }
        model.addAttribute("isCurrentUser", isCurrentUser);
        model.addAttribute("filter",filter);
        model.addAttribute("users",users);
        model.addAttribute("posts",posts);
        model.addAttribute("str_user",str_user);
        return "search";
    }
}
