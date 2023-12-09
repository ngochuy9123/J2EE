package com.springboot.j2ee.controller;


import com.springboot.j2ee.beans.CDCBeans;
import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.dto.*;
import com.springboot.j2ee.entity.*;
import com.springboot.j2ee.enums.EFriendRequest;
import com.springboot.j2ee.service.*;
import com.springboot.j2ee.utils.FileUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
import java.util.UUID;
import java.util.function.Function;


@Controller
@AllArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final PostService postService;

    @Autowired
    private final FriendService friendService;

    @Autowired
    private final LikeService likeService;

    @Autowired
    private final CommentService commentService;

    @Autowired
    private final UserInfoService userInfoService;

    @Autowired
    private final AnnounceService announceService;


    @Autowired
    private FileUtils fileUtils;

    @Autowired
    @Qualifier("cdcBeans")
    private CDCBeans cdcBeans;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private static final String messageSocket = "/topic/general/";


    @GetMapping("home")
    public String showHome(@AuthenticationPrincipal CustomUser principal, Authentication auth, Model model){
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
        model.addAttribute("currentUser", principal.getUser());
        model.addAttribute("lst_friend_request",list_friend_request);
        model.addAttribute("hashLike",hashLike);
        model.addAttribute("posts",postService.getAllPost(principal.getUser().getId()));


        //CDC / Socket
//        var userName = principal.getUsername();
        UUID uuid = UUID.randomUUID();
        var id = principal.getUser().getId();

        cdcBeans.subscribeToWriteComment(uuid, (c) -> handleComment(c, uuid));
        cdcBeans.subscribeToWriteEmote(uuid, (c) -> handleEmote(c, uuid));
        cdcBeans.subscribeToRemoveEmote(uuid, (c) -> handleEmote(c, uuid));
        cdcBeans.subscribeToWriteAnnounce(id, uuid, (c) -> handleAnnounce(c, uuid));
        cdcBeans.subscribeToWriteFriend(id, uuid, (c) -> handleSocketSend(c, "FRIENDS_REQUEST",messageSocket+ uuid,
                FriendRequestDto::new));
        cdcBeans.subscribeToDeleteFriend(id, uuid, (c) -> handleSocketSend(c, "FRIENDS_REQUEST",messageSocket+ uuid,
                FriendRequestDto::new));
        model.addAttribute("principal", principal);
        model.addAttribute("uuid", uuid);


        return "index";
    }

//    @GetMapping("profile")
//    public String showProfile(Model model){
//        model.addAttribute("user",user_pub);
//        return "profile";
//    }

    @GetMapping("signin")
    public String showSignInForm(HttpSession session){
        session.removeAttribute("msgReg");
        session.removeAttribute("email");
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
            session.setAttribute("msgReg","Email đã tồn tại");
        }
        else{

            registrationDTO.setAvatar("https://cdn.alongwalk.info/vn/wp-content/uploads/2022/10/14054104/image-100-y-tuong-avatar-cute-doc-dao-an-tuong-nhat-cho-ban-166567566414594.jpg");
            registrationDTO.setBackground("https://c4.wallpaperflare.com/wallpaper/321/512/923/tom-and-jerry-heroes-cartoons-desktop-hd-wallpaper-for-mobile-phones-tablet-and-pc-1920%C3%971200-wallpaper-thumb.jpg");
            registrationDTO.setUsername(registrationDTO.getFirstName()+" "+registrationDTO.getLastName());
            User u = userService.save(registrationDTO);
            if (u==null){
                session.setAttribute("msgReg","Đăng ký thất bại");
            }
            else{

                userInfoService.addInfoUser(u);

                session.setAttribute("msgReg","Đăng ký thành công");
                session.setAttribute("email",registrationDTO.getEmail());
                return "redirect:/verifyotp";
            }
        }
        return "redirect:/register";
    }



    @GetMapping("verifyotp")
    public String verifyOTP(){
        return "VerifyOtp";
    }


    @PostMapping("confrimOTP")
    public String confirmOTP(@RequestParam String otp, @RequestParam String email, Model model,HttpSession session){
        if (userService.checkOTP(email,otp)){
            session.setAttribute("msgReg","Đăng ký thành công");
        }
        else{
            session.setAttribute("msgReg","OTP không chính xác hoặc đã quá 5 phút");
        }


        return "redirect:/register";
    }

    @GetMapping("forgotPass")
    public String showForgotPass(){
        return "forgotpassword";
    }

    @PostMapping("forgotPass")
    public String forgotPass(@RequestParam String email,Model model,HttpSession session){
        boolean f = userService.checkEmail(email);
        if (f){
            return "redirect:/VerifyOtp";
        }
        else{
            System.out.println("Email không tồn tại trong hệ thống");
        }
        return null;
    }


    @PostMapping("create_post")
    public String createPost(@AuthenticationPrincipal CustomUser principal,@ModelAttribute("post") PostDTO postDTO,Model model,@RequestParam(value = "image",required = false) MultipartFile file)throws IOException {

        if ( !file.isEmpty()){

            String path = fileUtils.saveFile(file, "uploads", "users", principal.getUser().getEmail(), "posts");
            postDTO.setImageUrl(path);
        }

        userService.createPost(postDTO,principal.getUsername());

        return "redirect:/home";
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

        List<Post> lstPost = postService.getAllPostForProfile(id,principal.getUser().getId());
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

            List<Comment> lstComments = commentService.getCommentByPost(p.getId());
            hashComment.put(p.getId(), lstComments);
            hashSlgComment.put(p.getId(),lstComments.size());
        }

        List<Announce> lstAnnounce = announceService.getAnnounceByIdUser(principal.getUser().getId());

        model.addAttribute("lstAnnounce",lstAnnounce);

        model.addAttribute("hashSlgComment",hashSlgComment);
        model.addAttribute("hashComment",hashComment);
        model.addAttribute("currentUser",userService.getUserById(principal.getUser().getId()));
        model.addAttribute("hashSlgLike",hashLike);
        model.addAttribute("hashLiked",hashLiked);
        model.addAttribute("isCurrentUser", isCurrentUser);
        model.addAttribute("lsPost",lstPost);
        model.addAttribute("lst_friend_request",list_friend_request);


        var userName = principal.getUsername();
        UUID uuid = UUID.randomUUID();
        var uid = principal.getUser().getId();

        cdcBeans.subscribeToWriteComment(uuid, (c) -> handleComment(c, uuid));
        cdcBeans.subscribeToWriteAnnounce(uid, uuid, (c) -> handleAnnounce(c, uuid));
        cdcBeans.subscribeToWriteFriend(uid, uuid, (c) -> handleSocketSend(c, "FRIENDS_REQUEST",messageSocket+ uuid,
                FriendRequestDto::new));
        cdcBeans.subscribeToDeleteFriend(id, uuid, (c) -> handleSocketSend(c, "FRIENDS_REQUEST",messageSocket+ uuid,
                FriendRequestDto::new));

        model.addAttribute("principal", principal);
        model.addAttribute("uuid", uuid);


        return "profile";
    }


    private <T, R> void handleSocketSend(T object, String type, String location, Function<T, R> converter) {
        var dto = converter.apply(object);
        var response = new GenericResponse<>(type, dto);
        simpMessagingTemplate.convertAndSend(location, response);

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



}
