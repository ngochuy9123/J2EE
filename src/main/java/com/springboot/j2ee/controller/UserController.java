package com.springboot.j2ee.controller;


import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.LikeDTO;
import com.springboot.j2ee.dto.PostDTO;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.*;
import com.springboot.j2ee.enums.EFriendRequest;
import com.springboot.j2ee.service.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final PostService postService;
    private final FriendService friendService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final UserInfoService userInfoService;
    private final AnnounceService announceService;


    public static final String UPLOAD_DIRECTORY = "./src/main/resources/static/uploads/";
    public static final String UPLOAD_DERECTORY_TARGET = "./target/classes/static/uploads/";
    public static final String pathImg = "/uploads/";

    public UserController(UserService userService, EmailService emailService, PostService postService, FriendService friendService, LikeService likeService, CommentService commentService, UserInfoService userInfoService, AnnounceService announceService) {
        this.userService = userService;
        this.emailService = emailService;
        this.postService = postService;
        this.friendService = friendService;
        this.likeService = likeService;
        this.commentService = commentService;
        this.userInfoService = userInfoService;
        this.announceService = announceService;
    }



    @GetMapping("home")
    public String showHome(@AuthenticationPrincipal CustomUser principal, Authentication auth, Model model){
        String userName = principal.getUsername();
//        email_md = userName;
//        user_pub = userService.getInfo(userName);
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

        model.addAttribute("posts",lstPost);
        model.addAttribute("user",userService.getUserById(principal.getUser().getId()));
        model.addAttribute("lst_friend_request",list_friend_request);
        model.addAttribute("hashLike",hashLike);
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
            registrationDTO.setUsername(registrationDTO.getFirstName()+" "+registrationDTO.getLastName());
            User u = userService.save(registrationDTO);
            if (u==null){
                session.setAttribute("msgReg","DANG KI THAT BAI");
            }
            else{

                userInfoService.addInfoUser(u);

                session.setAttribute("msgReg","DANG KI THANH CONG");
                session.setAttribute("email",registrationDTO.getEmail());
            }
        }
        return "redirect:/register";
    }



    @PostMapping("confrimOTP")
    public String confirmOTP(@RequestParam String otp, @RequestParam String email, Model model,HttpSession session){
        if (userService.checkOTP(email,otp)){
            session.setAttribute("msgReg","DANG KI THANH CONG");
        }
        else{
            session.setAttribute("msgReg","Ban da nhap sai OTP hoac da qua thoi gian 5 phut");
        }


        return "redirect:/register";
    }



    @PostMapping("create_post")
    public String createPost(@AuthenticationPrincipal CustomUser principal,@ModelAttribute("post") PostDTO postDTO,Model model,@RequestParam(value = "image",required = false) MultipartFile file)throws IOException {

        if ( !file.isEmpty()){
            String pathTemp = pathImg.concat(principal.getUsername());
            pathTemp = pathTemp.concat("/");
            pathTemp = pathTemp.concat(Objects.requireNonNull(file.getOriginalFilename()));

            String fileNameAndPathTarget = saveImage(file,principal.getUsername());
            String convertedPath = convertToUnixPath(fileNameAndPathTarget);
            postDTO.setImageUrl(convertedPath);
        }

        userService.createPost(postDTO,principal.getUsername());

        return "redirect:/home";
    }




    public String convertToUnixPath(String windowsPath) {
        // Replace backslashes with forward slashes
        String unixPath = windowsPath.replace("\\", "/");

        // Remove the initial "."
        if (unixPath.startsWith("./")) {
            unixPath = unixPath.substring(2);
        }
        int startIndex = unixPath.indexOf("/uploads/");

        // If "/uploads/" is found, extract the substring after it
        if (startIndex != -1) {
            return "/uploads/"+ unixPath.substring(startIndex + "/uploads/".length());
        } else {
            return null; // "/uploads/" not found in the path
        }


    }

    public String saveImage(MultipartFile file,String email) throws IOException {
        StringBuilder fileNames = new StringBuilder();

        String uploadDirectory = UPLOAD_DIRECTORY.concat(email);
        String uploadDirectoryTarget = UPLOAD_DERECTORY_TARGET.concat(email);

        if (!Files.exists(Path.of(uploadDirectory))) {
            Files.createDirectories(Path.of(uploadDirectory));
        }
        if (!Files.exists(Path.of(uploadDirectoryTarget))) {
            Files.createDirectories(Path.of(uploadDirectoryTarget));
        }

        Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
        Path fileNameAndPathTarget = Paths.get(uploadDirectoryTarget, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());

        Files.write(fileNameAndPathTarget,file.getBytes());
        return fileNameAndPathTarget.toString();
    }

    @GetMapping("profile")
    public String showAnotherProfile(@RequestParam(name = "id", required = false, defaultValue = "-1") long id,Model model,@AuthenticationPrincipal CustomUser principal){
        if (id == -1){
            id = principal.getUser().getId();
        }
        boolean isCurrentUser =false;
        if (id == principal.getUser().getId()){
            isCurrentUser=true;
        }

        if (isCurrentUser){

            User user = userService.getInfoById(principal.getUser().getId());
            model.addAttribute("user",user);
            model.addAttribute("infoUser", userInfoService.getInfoByIdUser(user));
        }
        else{
            //            xử lý đã gửi lời mời kết bạn chưa nếu rồi thì có thể xóa lời mời
//            Xử lý có ai gửi lời mời không nếu có thì có thể hủy lời mời
//            Kiểm tra đã là bạn bè chưa
            User user = userService.getInfoById(id);
            model.addAttribute("user",user);
            model.addAttribute("infoUser", userInfoService.getInfoByIdUser(user));
        }

        if (!isCurrentUser){
            EFriendRequest eFriendRequest = friendService.checkFriendRequest(id,principal.getUser().getId());
            String friendRequest = eFriendRequest.toString();
            model.addAttribute("friend_request", friendRequest);
        }

        List<Friend> list_friend_request = friendService.displayFriendRequest(principal.getUser().getId());

        HashMap<Long,Long> hashLike = new HashMap<Long, Long>();
        HashMap<Long,Boolean> hashLiked = new HashMap<>();

        HashMap<Long,List<Comment>> hashComment = new HashMap<>();
        HashMap<Long,Integer> hashSlgComment = new HashMap<>();

        List<Post> lstPost = postService.getPostByIdUser(id);
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
        model.addAttribute("isCurrentUser", isCurrentUser);
        model.addAttribute("lsPost",lstPost);
        model.addAttribute("lst_friend_request",list_friend_request);

        return "profile";
    }


}
