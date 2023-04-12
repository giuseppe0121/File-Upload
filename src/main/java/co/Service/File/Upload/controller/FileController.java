package co.Service.File.Upload.controller;

import co.Service.File.Upload.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public List<String> upload(@RequestParam MultipartFile[] files){
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String singleUpload = fileService.upload(file);
                fileNames.add(singleUpload);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return fileNames;
    }

    @GetMapping("/download")
    public byte[] download(@RequestParam String fileName, HttpServletResponse response){
        try {
            String extension = FilenameUtils.getExtension(fileName);
            switch (extension){
                case "gif":
                    response.setContentType(MediaType.IMAGE_GIF_VALUE);
                    break;
                case "jpg":
                case "jpeg":
                    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                    break;
                case "png":
                    response.setContentType(MediaType.IMAGE_PNG_VALUE);
                    break;
            }
            response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");

            return fileService.download(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
