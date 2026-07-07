package com.emrys.neondraw.common.convert;

import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractJacksonHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

/**
 * 支持 multipart/form-data 中 @RequestPart JSON 字段的反序列化。
 * <p>
 * Spring Boot 4 使用 Jackson 3，需继承 {@link AbstractJacksonHttpMessageConverter} 并注入 {@link JsonMapper}。
 *
 * @author yibo
 */
@Component
public class MultipartJackson2HttpMessageConverter extends AbstractJacksonHttpMessageConverter<JsonMapper> {

    public MultipartJackson2HttpMessageConverter(JsonMapper jsonMapper) {
        super(jsonMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    public boolean canWrite(ResolvableType type, Class<?> valueClass, @Nullable MediaType mediaType) {
        // 转换器不用于写入（即不用于响应的序列化）
        return false;
    }
}
