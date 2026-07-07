package com.emrys.neondraw.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传服务接口。
 * <p>
 * 负责接收前端上传的图片文件，保存到本地磁盘并返回唯一文件名（供奖品等模块引用）。
 *
 * @author Emrys
 */
public interface PictureService {
    /**
     * 保存图片
     * @param multipartFile 上传文件的工具类
     * @return 图片的索引(保证这个索引是唯一的)
     */
    String savePicture(MultipartFile multipartFile);
}
