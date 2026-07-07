package com.emrys.neondraw.service.impl;

import com.emrys.neondraw.service.dto.ActivityDetailDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Emrys
 * content:
 */
@SpringBootTest
class ActivityServiceImplTest {
    @Autowired
    ActivityServiceImpl activityService;
    @Autowired
    ObjectMapper objectMapper;
    @Test
    void getActivityDetailDTO() {
        ActivityDetailDTO activityDetailDTO = activityService.getActivityDetailDTO(31L);
        try {
            System.out.println(objectMapper.writeValueAsString(activityDetailDTO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}