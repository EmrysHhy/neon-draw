package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.CreateActivityParam;
import com.emrys.lotterysystem.service.dto.CreateActivityDTO;

/**
 *
 * @author Emrys
 * content:
 */
public interface ActivityService {
    CreateActivityDTO createActivity(CreateActivityParam param);

}
