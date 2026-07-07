package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.CreatePrizeParam;
import com.emrys.lotterysystem.controller.param.PageParam;
import com.emrys.lotterysystem.service.dto.PageListDTO;
import com.emrys.lotterysystem.service.dto.PrizeDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PrizeService {

    Long createPrize(CreatePrizeParam param, MultipartFile multipartFile, String adminToken);

    PageListDTO<PrizeDTO> findPrizeList(PageParam param, String adminToken);

    PageListDTO<PrizeDTO> findAllPrizeList(String adminToken);
}
