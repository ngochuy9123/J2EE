package com.springboot.j2ee.api;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.controller.UserController;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.service.UserService;
import com.springboot.j2ee.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@RestController
public class UserAPI {



    private final UserService userService;

    @Autowired
    FileUtils fileUtils;

    public UserAPI(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("editAvatar")
    public String editAvatar(@RequestParam("image") MultipartFile file,@AuthenticationPrincipal CustomUser principal) throws IOException {
        if (!file.isEmpty()){
            var path = fileUtils.saveFile(file, "uploads", "users", principal.getUsername(), "avatar");
            User user = userService.getInfo(principal.getUsername());
            user.setAvatar(path);
            userService.saveUser(user);

            return path;
        }
        return null;
    }

    @PostMapping("editBackground")
    public String editBackground(@RequestParam("image") MultipartFile file,@AuthenticationPrincipal CustomUser principal) throws IOException {
        if ( !file.isEmpty()){
            var path = fileUtils.saveFile(file, "uploads", "users", principal.getUsername(), "background");

            User user = userService.getInfo(principal.getUsername());
            user.setBackground(path);
            userService.saveUser(user);
            return path;
        }
        return null;
    }

    @PostMapping("searchUser")
    @ResponseBody
    public List<UserDTO> searchUser(@RequestParam(name = "contentSearch") String content,@AuthenticationPrincipal CustomUser principal){
        String email = "%".concat(content).concat("%");

        List<UserDTO> listUser = userService.searchUser(email, principal.getUser().getId());
        return listUser;
    }


    @GetMapping ("searchUser")
    @ResponseBody
    public List<UserDTO> searchUsert(@AuthenticationPrincipal CustomUser principal){
        String email = "huy";

        List<UserDTO> listUser = userService.searchUser(email, principal.getUser().getId());
        return listUser;
    }

    @GetMapping("editAvatar")
    public String edit(){
        return "Hello hihi";
    }


    @GetMapping("/api/user")
    public ResponseEntity<List<UserDTO>> getUser(@DestinationVariable String email, @DestinationVariable int limit) {
       var users = userService.getUserByEmailLimitBy(email, limit);
       var userDTOs = users.stream().map(UserDTO::new).toList();
       return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

}
