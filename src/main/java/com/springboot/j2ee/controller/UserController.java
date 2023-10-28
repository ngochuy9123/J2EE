package com.springboot.j2ee.controller;


import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.PostDTO;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.Friend;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.service.EmailService;
import com.springboot.j2ee.service.FriendService;
import com.springboot.j2ee.service.PostService;
import com.springboot.j2ee.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final PostService postService;
    private final FriendService friendService;

    public static String email_md ;
    public static User user_pub;

    public static final String UPLOAD_DIRECTORY = "./src/main/resources/static/uploads/";
    public static final String UPLOAD_DERECTORY_TARGET = "./target/classes/static/uploads/";
    public static final String pathImg = "/uploads/";
    public UserController(UserService userService, EmailService emailService, PostService postService, FriendService friendService) {
        this.userService = userService;
        this.emailService = emailService;
        this.postService = postService;
        this.friendService = friendService;
    }



    @GetMapping("home")
    public String showHome(@AuthenticationPrincipal CustomUser principal, Authentication auth, Model model){
        String userName = principal.getUsername();
        email_md = userName;
        user_pub = userService.getInfo(userName);
        List<Friend> list_friend_request = friendService.displayFriendRequest(principal.getUser().getId());

        model.addAttribute("posts",postService.getAllPost());
        model.addAttribute("user",user_pub);
        model.addAttribute("lst_friend_request",list_friend_request);
        return "index";
    }

//    @GetMapping("profile")
//    public String showProfile(Model model){
//        model.addAttribute("user",user_pub);
//        return "profile";
//    }

    @GetMapping("signin")
    public String showSignInForm(){
        return "login";
    }
    @GetMapping("register")
    public String showSignUpForm(){
        return "register";
    }

    @PostMapping("register")
    public String registerUserAccount(@Valid @ModelAttribute("user") UserDTO registrationDTO, HttpSession session, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "register";
        }

        boolean f = userService.checkEmail(registrationDTO.getEmail());
        if (f){
            session.setAttribute("msgReg","Email Da Ton Tai");
        }
        else{

            registrationDTO.setAvatar("https://cdn.alongwalk.info/vn/wp-content/uploads/2022/10/14054104/image-100-y-tuong-avatar-cute-doc-dao-an-tuong-nhat-cho-ban-166567566414594.jpg");
            registrationDTO.setBackground("https://c4.wallpaperflare.com/wallpaper/321/512/923/tom-and-jerry-heroes-cartoons-desktop-hd-wallpaper-for-mobile-phones-tablet-and-pc-1920%C3%971200-wallpaper-thumb.jpg");
            User u = userService.save(registrationDTO);
            if (u==null){
                session.setAttribute("msgReg","DANG KI THAT BAI");
            }
            else{
                emailService.sendSimpleEmail(registrationDTO.getEmail());
                session.setAttribute("msgReg","DANG KI THANH CONG");
            }
        }
        return "redirect:/register";
    }

    @PostMapping("create_post")
    public String createPost(@ModelAttribute("post") PostDTO postDTO,Model model,@RequestParam(value = "image",required = false) MultipartFile file)throws IOException {

        if ( !file.isEmpty()){
            String pathTemp = pathImg.concat(email_md);
            pathTemp = pathTemp.concat("/");
            pathTemp = pathTemp.concat(Objects.requireNonNull(file.getOriginalFilename()));

            saveImage(file);
            saveImage(file);

            postDTO.setImageUrl(pathTemp);
        }

        userService.createPost(postDTO,this.email_md);

        return "redirect:/home";
    }

    public void saveImage(MultipartFile file) throws IOException {
        StringBuilder fileNames = new StringBuilder();

        String uploadDirectory = UPLOAD_DIRECTORY.concat(email_md);
        String uploadDirectoryTarget = UPLOAD_DERECTORY_TARGET.concat(email_md);

        if (!Files.exists(Path.of(uploadDirectory))) {
            Files.createDirectories(Path.of(uploadDirectory));
        }
        if (!Files.exists(Path.of(uploadDirectoryTarget))) {
            Files.createDirectories(Path.of(uploadDirectoryTarget));
        }

        Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
        Path fileNameAndPathTarget = Paths.get(uploadDirectory, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        Files.write(fileNameAndPathTarget,file.getBytes());
    }

    @GetMapping("profile")
    public String showAnotherProfile(@RequestParam(name = "id", required = false, defaultValue = "-1") long id,Model model){
        if (id == -1){
            id = user_pub.getId();
        }


        boolean isCurrentUser =false;
        if (id == user_pub.getId()){
            isCurrentUser=true;
        }

        if (isCurrentUser){
            User user = userService.getInfoById(user_pub.getId());
            model.addAttribute("user",user);
        }
        else{
            User user = userService.getInfoById(id);
            model.addAttribute("user",user);
        }
        model.addAttribute("isCurrentUser", isCurrentUser);
        return "profile";
    }






}
