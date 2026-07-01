package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.CreatePrizeParam;
import com.emrys.lotterysystem.controller.param.PageParam;
import com.emrys.lotterysystem.service.dto.PageListDTO;
import com.emrys.lotterysystem.service.dto.PrizeDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Emrys
 * content:
 */
public interface PrizeService {
    Long createPrize(CreatePrizeParam param, MultipartFile multipartFile);

    /**
     * 分页查询
     * @param param
     * @return
     */
    PageListDTO<PrizeDTO> findPrizeList(PageParam param);

    PageListDTO<PrizeDTO> findAllPrizeList();
}
