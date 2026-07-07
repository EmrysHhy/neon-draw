package com.emrys.neondraw.service;

import com.emrys.neondraw.controller.param.CreatePrizeParam;
import com.emrys.neondraw.controller.param.PageParam;
import com.emrys.neondraw.service.dto.PageListDTO;
import com.emrys.neondraw.service.dto.PrizeDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PrizeService {

    Long createPrize(CreatePrizeParam param, MultipartFile multipartFile, String adminToken);

    PageListDTO<PrizeDTO> findPrizeList(PageParam param, String adminToken);

    PageListDTO<PrizeDTO> findAllPrizeList(String adminToken);

    void deletePrize(Long prizeId, String adminToken);
}
