package com.emrys.lotterysystem.controller;

import com.emrys.lotterysystem.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.channels.MulticastChannel;

/**
 *
 * @author Emrys
 * content:
 */
@RestController
@RequestMapping("/pic")
public class PictureController {
    @Autowired
    PictureService pictureService;
    @RequestMapping("/upload")
    public String uploadPic(MultipartFile multipartFile) {
        return pictureService.savePicture(multipartFile);
    }
}
