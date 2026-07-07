package com.emrys.lotterysystem.controller;

import com.emrys.lotterysystem.common.pojo.CommonResult;
import com.emrys.lotterysystem.controller.convert.ControllerConverter;
import com.emrys.lotterysystem.controller.param.CreatePrizeParam;
import com.emrys.lotterysystem.controller.param.PageParam;
import com.emrys.lotterysystem.controller.result.FindPrizeListResult;
import com.emrys.lotterysystem.service.PictureService;
import com.emrys.lotterysystem.service.PrizeService;
import com.emrys.lotterysystem.service.dto.PageListDTO;
import com.emrys.lotterysystem.service.dto.PrizeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class PrizeController {

    @Autowired
    private PictureService pictureService;
    @Autowired
    private PrizeService prizeService;

    @RequestMapping("/pic/upload")
    public String uploadPic(MultipartFile multipartFile) {
        return pictureService.savePicture(multipartFile);
    }

    @RequestMapping("/prize/create")
    public CommonResult<Long> createPrize(@RequestHeader("user_token") String token,
                                          @Validated @RequestPart CreatePrizeParam param,
                                          @RequestPart(value = "prizePic", required = false) MultipartFile prizePic) {
        log.info("create prize, param: {}, file: {}",
                param, prizePic != null ? prizePic.getOriginalFilename() : null);
        long id = prizeService.createPrize(param, prizePic, token);
        return CommonResult.success(id);
    }

    @RequestMapping("/prize/find-list")
    public CommonResult<FindPrizeListResult> findPrizeList(@RequestHeader("user_token") String token,
                                                           PageParam param) {
        log.info("find prize list, param: {}", param);
        PageListDTO<PrizeDTO> pageListDTO = prizeService.findPrizeList(param, token);
        FindPrizeListResult ret = ControllerConverter.convertToFindPrizeListResult(pageListDTO);
        return CommonResult.success(ret);
    }

    @RequestMapping("/prize/all-list")
    public CommonResult<FindPrizeListResult> findAllPrizeList(@RequestHeader("user_token") String token) {
        log.info("find all prize list");
        PageListDTO<PrizeDTO> pageListDTO = prizeService.findAllPrizeList(token);
        FindPrizeListResult ret = ControllerConverter.convertToFindPrizeListResult(pageListDTO);
        return CommonResult.success(ret);
    }
}
