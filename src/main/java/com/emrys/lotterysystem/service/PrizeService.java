package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.CreatePrizeParam;
import com.emrys.lotterysystem.controller.param.PageParam;
import com.emrys.lotterysystem.service.dto.PageListDTO;
import com.emrys.lotterysystem.service.dto.PrizeDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 奖品业务服务接口。
 * <p>
 * 负责奖品的创建（含图片上传）、分页查询及全量列表查询，供创建活动时选择奖品使用。
 *
 * @author Emrys
 */
public interface PrizeService {
    /**
     * 创建奖品，同时上传并关联奖品图片。
     */
    Long createPrize(CreatePrizeParam param, MultipartFile multipartFile);

    /**
     * 分页查询奖品列表。
     */
    PageListDTO<PrizeDTO> findPrizeList(PageParam param);

    /**
     * 查询全部奖品列表（不分页）。
     */
    PageListDTO<PrizeDTO> findAllPrizeList();
}
