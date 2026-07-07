package com.emrys.neondraw.service.impl;

import com.emrys.neondraw.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.neondraw.common.exception.ServiceException;
import com.emrys.neondraw.common.utils.JWTUtil;
import com.emrys.neondraw.controller.param.CreatePrizeParam;
import com.emrys.neondraw.controller.param.PageParam;
import com.emrys.neondraw.dao.dataobject.PrizeDO;
import com.emrys.neondraw.dao.mapper.ActivityPrizeMapper;
import com.emrys.neondraw.dao.mapper.PrizeMapper;
import com.emrys.neondraw.service.PictureService;
import com.emrys.neondraw.service.PrizeService;
import com.emrys.neondraw.service.convert.ServiceConverter;
import com.emrys.neondraw.service.dto.PageListDTO;
import com.emrys.neondraw.service.dto.PrizeDTO;
import com.emrys.neondraw.service.enums.ActivityPrizeStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PrizeServiceImpl implements PrizeService {

    @Autowired
    private PictureService pictureService;
    @Autowired
    private PrizeMapper prizeMapper;
    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Override
    public Long createPrize(CreatePrizeParam param, MultipartFile multipartFile, String adminToken) {
        Long adminId = JWTUtil.getAdminId(adminToken);
        String imageUrl = null;
        if (multipartFile != null && !multipartFile.isEmpty()) {
            imageUrl = pictureService.savePicture(multipartFile);
        }
        PrizeDO prizeDO = new PrizeDO();
        prizeDO.setAdminId(adminId);
        prizeDO.setName(param.getPrizeName());
        prizeDO.setDescription(param.getDescription());
        prizeDO.setPrice(param.getPrice());
        prizeDO.setImageUrl(imageUrl);
        prizeMapper.insert(prizeDO);
        return prizeDO.getId();
    }

    @Override
    public PageListDTO<PrizeDTO> findPrizeList(PageParam param, String adminToken) {
        Long adminId = JWTUtil.getAdminId(adminToken);
        int totalCount = prizeMapper.countPrizeByAdminId(adminId);
        List<PrizeDO> prizeDOList = prizeMapper.selectPrizeListByAdminId(
                adminId, param.getOffset(), param.getPageSize());
        List<PrizeDTO> prizeDTOS = ServiceConverter.convertToPrizeDTOList(prizeDOList);
        return new PageListDTO<>(totalCount, prizeDTOS);
    }

    @Override
    public PageListDTO<PrizeDTO> findAllPrizeList(String adminToken) {
        Long adminId = JWTUtil.getAdminId(adminToken);
        List<PrizeDO> prizeDOList = prizeMapper.selectAllPrizeListByAdminId(adminId);
        List<PrizeDTO> prizeDTOS = ServiceConverter.convertToPrizeDTOList(prizeDOList);
        return new PageListDTO<>(prizeDOList.size(), prizeDTOS);
    }

    @Override
    public void deletePrize(Long prizeId, String adminToken) {
        if (prizeId == null) {
            throw new ServiceException(ServiceErrorCodeConstants.PRIZE_NOT_FOUND);
        }
        Long adminId = JWTUtil.getAdminId(adminToken);
        PrizeDO prizeDO = prizeMapper.selectByIdAndAdminId(prizeId, adminId);
        if (prizeDO == null) {
            throw new ServiceException(ServiceErrorCodeConstants.PRIZE_NOT_FOUND);
        }
        if (activityPrizeMapper.countByPrizeIdAndStatus(
                prizeId, ActivityPrizeStatusEnum.NOT_DRAW.name()) > 0) {
            throw new ServiceException(ServiceErrorCodeConstants.PRIZE_IN_USE);
        }
        int ret = prizeMapper.deleteByIdAndAdminId(prizeId, adminId);
        if (ret <= 0) {
            throw new ServiceException(ServiceErrorCodeConstants.PRIZE_DELETE_FAILED);
        }
    }
}
