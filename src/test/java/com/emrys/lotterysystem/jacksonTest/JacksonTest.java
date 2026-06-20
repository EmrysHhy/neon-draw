package com.emrys.lotterysystem.jacksonTest;

import com.emrys.lotterysystem.common.pojo.CommonResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@SpringBootTest
public class JacksonTest {
    @Test
    public void testJackson() throws JsonProcessingException {
        CommonResult<String> result =CommonResult.error("系统发生错误");
        ObjectMapper objectMapper = new ObjectMapper();
        //将object转换成string
        String str = objectMapper.writeValueAsString(result);//异常
        System.out.println(str);

        //反序列化
        CommonResult<String> result1 = objectMapper.readValue(str, CommonResult.class);
        System.out.println(result1.getCode() + result1.getMessage());
    }
    //List 的序列化和反序列化过程2

    public List<CommonResult<String>> results2 = Arrays.asList(
            CommonResult.error("系统发生错误"),
            CommonResult.success("成功")
    );

     @Test
    public void testJackson2() throws JsonProcessingException {
         ObjectMapper objectMapper = new ObjectMapper();
         String str = objectMapper.writeValueAsString(results2); //一样的过程
         System.out.println(str);

         List<CommonResult<String>> resultList = objectMapper.readValue(str, objectMapper.getTypeFactory().constructCollectionType(List.class, CommonResult.class));
         for (CommonResult<String> result : resultList) {
             System.out.println(result.getCode() + result.getMessage());
         }
     }
}
