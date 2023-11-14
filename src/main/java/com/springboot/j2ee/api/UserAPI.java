package com.springboot.j2ee.api;

import com.springboot.j2ee.config.CustomUser;
import com.springboot.j2ee.controller.UserController;
import com.springboot.j2ee.dto.UserDTO;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.service.UserService;
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



    public final static String UPLOAD_DIRECTORY = "./src/main/resources/static/uploads/";
    public final static String UPLOAD_DERECTORY_TARGET = "./target/classes/static/uploads/";
    public final static String pathImg = "/uploads/";
    private final UserService userService;

    public UserAPI(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("editAvatar")
    public String editAvatar(@RequestParam("image") MultipartFile file,@AuthenticationPrincipal CustomUser principal) throws IOException {
        if ( !file.isEmpty()){
            String pathTemp = pathImg.concat(principal.getUsername());
            pathTemp = pathTemp.concat("/");
            pathTemp = pathTemp.concat(Objects.requireNonNull(file.getOriginalFilename()));

            saveImage(file, principal.getUsername());
            saveImage(file,principal.getUsername());

            User user = userService.getInfo(principal.getUsername());
            user.setAvatar(pathTemp);
            userService.saveUser(user);


        }
        return file.getOriginalFilename();
    }

    @PostMapping("editBackground")
    public String editBackground(@RequestParam("image") MultipartFile file,@AuthenticationPrincipal CustomUser principal) throws IOException {
        if ( !file.isEmpty()){
            String pathTemp = pathImg.concat(principal.getUsername());
            pathTemp = pathTemp.concat("/");
            pathTemp = pathTemp.concat(Objects.requireNonNull(file.getOriginalFilename()));

            saveImage(file, principal.getUsername());
            saveImage(file,principal.getUsername());

            User user = userService.getInfo(principal.getUsername());
            user.setBackground(pathTemp);
            userService.saveUser(user);


        }
        return file.getOriginalFilename();
    }


    public void saveImage(MultipartFile file,String email) throws IOException {
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

}
