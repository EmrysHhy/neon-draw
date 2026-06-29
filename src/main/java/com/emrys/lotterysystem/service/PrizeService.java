package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.CreatePrizeParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Emrys
 * content:
 */
public interface PrizeService {
    Long createPrize(CreatePrizeParam param, MultipartFile multipartFile);
}
