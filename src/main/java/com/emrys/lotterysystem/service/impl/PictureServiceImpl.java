package com.emrys.lotterysystem.service.impl;

import com.emrys.lotterysystem.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ServiceException;
import com.emrys.lotterysystem.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Emrys
 * content:
 */
@Service
public class PictureServiceImpl implements PictureService {
    @Value("${pic.local-path}")
    private String localPath;

    /** 图片最长边上限，超过则等比例缩小 */
    @Value("${pic.max-edge:800}")
    private int maxEdge;

    @Override
    public String savePicture(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new ServiceException(ServiceErrorCodeConstants.PICTURE_UPLOAD_FAILED);
        }
        File dir = new File(localPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = ".jpg";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }
        String newFileName = java.util.UUID.randomUUID() + suffix;
        File dest = new File(localPath + "/" + newFileName);

        try {
            BufferedImage source = ImageIO.read(multipartFile.getInputStream());
            if (source == null) {
                throw new ServiceException(ServiceErrorCodeConstants.PICTURE_UPLOAD_FAILED);
            }
            BufferedImage image = scaleDownIfNeeded(source, maxEdge);
            String formatName = resolveFormatName(suffix);
            if ("jpeg".equals(formatName) && image.getTransparency() != BufferedImage.OPAQUE) {
                image = toOpaqueImage(image);
            }
            if (!ImageIO.write(image, formatName, dest)) {
                throw new ServiceException(ServiceErrorCodeConstants.PICTURE_UPLOAD_FAILED);
            }
        } catch (IOException e) {
            throw new ServiceException(ServiceErrorCodeConstants.PICTURE_UPLOAD_FAILED);
        }
        return newFileName;
    }

    /**
     * 超过 maxEdge 时等比例缩小，保持宽高比
     */
    static BufferedImage scaleDownIfNeeded(BufferedImage source, int maxEdge) {
        int width = source.getWidth();
        int height = source.getHeight();
        if (width <= maxEdge && height <= maxEdge) {
            return source;
        }
        double scale = Math.min((double) maxEdge / width, (double) maxEdge / height);
        int newWidth = Math.max(1, (int) Math.round(width * scale));
        int newHeight = Math.max(1, (int) Math.round(height * scale));

        int imageType = source.getTransparency() == BufferedImage.OPAQUE
                ? BufferedImage.TYPE_INT_RGB
                : BufferedImage.TYPE_INT_ARGB;
        BufferedImage target = new BufferedImage(newWidth, newHeight, imageType);
        Graphics2D graphics = target.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(source, 0, 0, newWidth, newHeight, null);
        graphics.dispose();
        return target;
    }

    private static BufferedImage toOpaqueImage(BufferedImage source) {
        BufferedImage rgb = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = rgb.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, source.getWidth(), source.getHeight());
        graphics.drawImage(source, 0, 0, null);
        graphics.dispose();
        return rgb;
    }

    private static String resolveFormatName(String suffix) {
        return switch (suffix) {
            case ".png" -> "png";
            case ".gif" -> "gif";
            case ".bmp" -> "bmp";
            case ".jpeg", ".jpg" -> "jpeg";
            default -> "jpeg";
        };
    }
}
