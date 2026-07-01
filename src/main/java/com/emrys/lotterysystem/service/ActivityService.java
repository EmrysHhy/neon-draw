package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.CreateActivityParam;
import com.emrys.lotterysystem.controller.param.PageParam;
import com.emrys.lotterysystem.service.dto.ActivityDTO;
import com.emrys.lotterysystem.service.dto.CreateActivityDTO;
import com.emrys.lotterysystem.service.dto.PageListDTO;

/**
 *
 * @author Emrys
 * content:
 */
public interface ActivityService {
    CreateActivityDTO createActivity(CreateActivityParam param);

    PageListDTO<ActivityDTO> findActivityList(PageParam param);
}
