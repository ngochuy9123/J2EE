package com.springboot.j2ee.controller;


import com.springboot.j2ee.dto.PostDTO;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.service.EmailService;
import com.springboot.j2ee.service.PostService;
import com.springboot.j2ee.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Objects;

@Controller
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final PostService postService;

    private String email_md ;
    private Path p = Path.of("src/main/resources/static/uploads") ;
    public static String UPLOAD_DIRECTORY = "./src/main/resources/static/uploads/";
    public static String pathImg = "/uploads/";
    public UserController(UserService userService, EmailService emailService, PostService postService) {
        this.userService = userService;
        this.emailService = emailService;
        this.postService = postService;
    }

    @GetMapping("home")
    public String showHome(Principal principal, Authentication auth, Model model){
        String userName = principal.getName();
        this.email_md = userName;
        User user_t = userService.getInfo(userName);
        model.addAttribute("posts",postService.getAllPost());
        model.addAttribute("user",userService.getInfo(userName));
        return "index";
    }


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
    public String createPost(@ModelAttribute("post") PostDTO postDTO,Model model,@RequestParam("image") MultipartFile file)throws IOException {
        StringBuilder fileNames = new StringBuilder();
        UPLOAD_DIRECTORY = UPLOAD_DIRECTORY.concat(email_md);
        System.out.println(Path.of(UPLOAD_DIRECTORY));
        if (!Files.exists(Path.of(UPLOAD_DIRECTORY))) {

                Files.createDirectories(Path.of(UPLOAD_DIRECTORY));

        }
        String pathTemp = pathImg.concat(email_md);
        pathTemp = pathTemp.concat("/");
        pathTemp = pathTemp.concat(Objects.requireNonNull(file.getOriginalFilename()));

        if (!Files.exists(Path.of(UPLOAD_DIRECTORY))){

        }
        System.out.println(UPLOAD_DIRECTORY);
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());

        String t = UPLOAD_DIRECTORY.concat("/");


        postDTO.setImageUrl(pathTemp);
        userService.createPost(postDTO,this.email_md);
        UPLOAD_DIRECTORY = "./src/main/resources/static/uploads/";
        return "redirect:/home";
    }



}
