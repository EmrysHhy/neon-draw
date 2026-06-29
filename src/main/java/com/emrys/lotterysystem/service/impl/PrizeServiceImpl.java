package com.emrys.lotterysystem.service.impl;

import com.emrys.lotterysystem.controller.param.CreatePrizeParam;
import com.emrys.lotterysystem.dao.dataobject.PrizeDO;
import com.emrys.lotterysystem.dao.mapper.PrizeMapper;
import com.emrys.lotterysystem.service.PictureService;
import com.emrys.lotterysystem.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Emrys
 * content:
 */
public class PrizeServiceImpl implements PrizeService {
    @Autowired
    PictureService pictureService;
    @Autowired
    PrizeMapper prizeMapper;
    @Override
    public Long createPrize(CreatePrizeParam param, MultipartFile multipartFile) {

        //上传图片
        String fileName = pictureService.savePicture(multipartFile);
        //保存数据
        PrizeDO prizeDO = new PrizeDO();
        prizeDO.setName(param.getPrizeName());
        prizeDO.setDescription(param.getDescription());
        prizeDO.setPrice(param.getPrice());
        prizeDO.setImageUrl(fileName);
        prizeMapper.insert(prizeDO);//返回prizeDO的id
        return prizeDO.getId();
    }
}
