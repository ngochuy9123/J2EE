package com.springboot.j2ee.controller;


import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.service.EmailService;
import com.springboot.j2ee.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("home")
    public String showHome(Principal principal, Authentication auth, Model model){
        String userName = principal.getName();
        System.out.println("Current Logged in User is: " + userName);
        model.addAttribute("user",userService.getInfo(userName));
        return "index";
    }

    @GetMapping("chat")
    public String chat(Principal principal, Model model){
        String userName = principal.getName();
        System.out.println("Current Logged in User is: " + userName);
        model.addAttribute("user",userService.getInfo(userName));
        return "chat";
    }

    @PostMapping("createPost")
    public String createPost(Principal principal, Authentication auth,Model model){
        String userName = principal.getName();
        User user = userService.getInfo(userName);
        return "redirect:/chat";
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



}
