package com.emrys.lotterysystem.controller;

import com.emrys.lotterysystem.controller.param.CreatePrizeParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * PrizeController 集成测试
 * @author Emrys
 */
@SpringBootTest
@AutoConfigureMockMvc
class PrizeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPrize() throws Exception {
        // 1. 构造奖品 JSON
        CreatePrizeParam param = new CreatePrizeParam();
        param.setPrizeName("测试奖品");
        param.setDescription("测试奖品描述");
        param.setPrice(BigDecimal.valueOf(100));

        MockMultipartFile paramPart = new MockMultipartFile(
                "param",              // @RequestPart 的参数名
                "",                   // 原始文件名（空）
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(param)
        );

        // 2. 读取真实图片文件
        Path imagePath = Paths.get("upload/9c52d474-bd1a-408d-b34f-38d96dee0a21.jpg");
        byte[] imageBytes = Files.readAllBytes(imagePath);
        MockMultipartFile imagePart = new MockMultipartFile(
                "multipartFile",      // @RequestPart 的参数名
                "test-prize.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                imageBytes
        );

        // 3. 发 multipart POST 请求
        mockMvc.perform(multipart("/prize/create")
                        .file(paramPart)
                        .file(imagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isNumber());
    }
}
