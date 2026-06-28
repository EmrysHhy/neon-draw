package com.emrys.lotterysystem.service.impl;

import com.emrys.lotterysystem.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ServiceException;
import com.emrys.lotterysystem.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author Emrys
 * content:
 */
@Service
public class PictureServiceImpl implements PictureService {
    @Value("${pic.local-path}")
    private String localPath;
    @Override
    public String savePicture(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()){
            throw new ServiceException(ServiceErrorCodeConstants.PICTURE_UPLOAD_FAILED);
        }
        //创建目录
        File dir = new File(localPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //创建索引
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = ".jpg"; // 默认后缀
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFileName = UUID.randomUUID() + suffix;//生成新的文件名
        //图片的保存
        try {
            multipartFile.transferTo(new File(localPath + "/" + newFileName));
        } catch (IOException e) {
            throw new ServiceException(ServiceErrorCodeConstants.PICTURE_UPLOAD_FAILED);
        }
        return newFileName;
    }
}
