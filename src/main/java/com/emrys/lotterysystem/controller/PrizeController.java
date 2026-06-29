package com.emrys.lotterysystem.controller;

import com.emrys.lotterysystem.common.pojo.CommonResult;
import com.emrys.lotterysystem.controller.param.CreatePrizeParam;
import com.emrys.lotterysystem.service.PictureService;
import com.emrys.lotterysystem.service.PrizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.channels.MulticastChannel;

/**
 *
 * @author Emrys
 * content:
 */
@RestController
@Slf4j
public class PrizeController {
    @Autowired
    PictureService pictureService;
    @Autowired
    PrizeService prizeService;
    @RequestMapping("/pic/upload")
    public String uploadPic(MultipartFile multipartFile) {
        return pictureService.savePicture(multipartFile);
    }

    @RequestMapping("/prize/create")
    public CommonResult<Long> createPrize(@Validated @RequestPart CreatePrizeParam param, @RequestPart MultipartFile multipartFile) {
        log.info("create prize, param: {}, file: {}", param, multipartFile.getOriginalFilename());
        long id = prizeService.createPrize(param, multipartFile);
        return CommonResult.success(id);
    }
}
