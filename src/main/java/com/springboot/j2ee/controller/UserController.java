package com.springboot.j2ee.controller;


import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.service.EmailService;
import com.springboot.j2ee.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping({"/","/home"})
    public String showHome(){

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
