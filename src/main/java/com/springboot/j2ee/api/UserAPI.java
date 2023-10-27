package com.springboot.j2ee.api;

import com.springboot.j2ee.controller.UserController;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public String editAvatar(@RequestParam("image") MultipartFile file) throws IOException {
        if ( !file.isEmpty()){
            String pathTemp = pathImg.concat(UserController.email_md);
            pathTemp = pathTemp.concat("/");
            pathTemp = pathTemp.concat(Objects.requireNonNull(file.getOriginalFilename()));

            saveImage(file);
            saveImage(file);

            User user = userService.getInfo(UserController.email_md);
            user.setAvatar(pathTemp);
            userService.saveUser(user);


        }
        return file.getOriginalFilename();
    }

    @PostMapping("editBackground")
    public String editBackground(@RequestParam("image") MultipartFile file) throws IOException {
        if ( !file.isEmpty()){
            String pathTemp = pathImg.concat(UserController.email_md);
            pathTemp = pathTemp.concat("/");
            pathTemp = pathTemp.concat(Objects.requireNonNull(file.getOriginalFilename()));

            saveImage(file);
            saveImage(file);

            User user = userService.getInfo(UserController.email_md);
            user.setAvatar(pathTemp);
            userService.saveUser(user);


        }
        return file.getOriginalFilename();
    }


    public void saveImage(MultipartFile file) throws IOException {
        StringBuilder fileNames = new StringBuilder();

        String uploadDirectory = UPLOAD_DIRECTORY.concat(UserController.email_md);
        String uploadDirectoryTarget = UPLOAD_DERECTORY_TARGET.concat(UserController.email_md);

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

    @GetMapping("editAvatar")
    public String edit(){
        return "Hello hihi";
    }
}
