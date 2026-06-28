package com.emrys.lotterysystem.service;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Emrys
 * content:
 */
public interface PictureService {
    /**
     * 保存图片
     * @param multipartFile 上传文件的工具类
     * @return 图片的索引(保证这个索引是唯一的)
     */
    String savePicture(MultipartFile multipartFile);
}
