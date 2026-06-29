package com.emrys.lotterysystem.service.impl;

import com.emrys.lotterysystem.service.convert.ServiceConverter;
import com.emrys.lotterysystem.controller.param.CreatePrizeParam;
import com.emrys.lotterysystem.controller.param.PageParam;
import com.emrys.lotterysystem.dao.dataobject.PrizeDO;
import com.emrys.lotterysystem.dao.mapper.PrizeMapper;
import com.emrys.lotterysystem.service.PictureService;
import com.emrys.lotterysystem.service.PrizeService;
import com.emrys.lotterysystem.service.dto.PageListDTO;
import com.emrys.lotterysystem.service.dto.PrizeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Service
public class PrizeServiceImpl implements PrizeService {
    @Autowired
    PictureService pictureService;
    @Autowired
    PrizeMapper prizeMapper;

    /**
     * 上传图片
     * @param param
     * @param multipartFile
     * @return
     */
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

    /**
     * 分页查询奖品
     * @param param
     * @return
     */
    @Override
    public PageListDTO<PrizeDTO> findPrizeList(PageParam param) {
        //1.获得奖品总数
        int totalCount = prizeMapper.countPrize();
        //2.获得当前页面的奖品
        List<PrizeDO> prizeDOList = prizeMapper.selectPrizeList(param.getOffset(),param.getPageSize());
        //3.将PrizeDO转换为PrizeDTO
        List<PrizeDTO> prizeDTOS = ServiceConverter.convertToPrizeDTOList(prizeDOList);
        return new PageListDTO<>(totalCount,prizeDTOS);
    }
}
