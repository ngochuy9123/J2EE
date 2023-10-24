package com.springboot.j2ee.api;

import org.springframework.web.bind.annotation.*;

@RestController
public class PostAPI {
    @GetMapping(value = "/createComment")
    @ResponseBody
    public String createComment(@RequestParam String postId, @RequestParam String content){
        System.out.println("Received ID: " + postId + ", Content: " + content);

        return "Received ID: " + postId + ", Content: " + content;
    }
}
