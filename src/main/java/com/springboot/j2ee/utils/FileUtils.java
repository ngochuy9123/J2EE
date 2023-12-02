package com.springboot.j2ee.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class FileUtils {
    @Value("${file.assetLocation}")
    private String ASSET_PATH;

    @Value("${file.targetAssetLocation}")
    private String TARGET_PATH;


    private String relativeSaveFile(MultipartFile file, String fileName,String mainFolder, String... innerFolders) throws IOException {
        //Return the file location before appending to make it like
        ///uploads/...
        //instead of
        //./Src/resource/static/.... for web friendly way to get file
        var folderLocation = Paths.get("",innerFolders);
        var fileLocation = Paths.get(folderLocation.toString(), fileName);

        //Save location
        var finalLocation = Paths.get(mainFolder, String.valueOf(fileLocation));

        //Create parent directories if not exist
        new File(finalLocation.toString()).getParentFile().mkdirs();

        Files.write(finalLocation, file.getBytes());
        return toUnixPath(fileLocation.toString());
    }

    public String saveFile(MultipartFile file, String... innerFolders) throws IOException {
        //Put file in target so that Spring boot can serve it
        relativeSaveFile(file, file.getOriginalFilename(), TARGET_PATH, innerFolders);

        return relativeSaveFile(file, file.getOriginalFilename(), ASSET_PATH, innerFolders);
    }

    public static String getExtension(String fileName) {

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i);
        }
        return "";
    }

    public static String toUnixPath(String filePath) {

        return filePath.replace("\\", "/");
    }

    public String saveFileWithCustomName(MultipartFile file, String fileName, String... innerFolders) throws IOException {
        var ext = getExtension(file.getOriginalFilename());
        var finalName = fileName + ext;

        relativeSaveFile(file, finalName, TARGET_PATH, innerFolders);
        return relativeSaveFile(file, finalName, ASSET_PATH, innerFolders);
    }

}
