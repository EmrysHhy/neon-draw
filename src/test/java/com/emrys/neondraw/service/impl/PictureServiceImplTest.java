package com.emrys.neondraw.service.impl;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PictureServiceImplTest {

    @Test
    void scaleDownIfNeeded_shouldKeepSmallImage() {
        BufferedImage source = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
        BufferedImage result = PictureServiceImpl.scaleDownIfNeeded(source, 800);
        assertEquals(400, result.getWidth());
        assertEquals(300, result.getHeight());
    }

    @Test
    void scaleDownIfNeeded_shouldScaleLargeLandscapeImage() {
        BufferedImage source = new BufferedImage(1600, 900, BufferedImage.TYPE_INT_RGB);
        BufferedImage result = PictureServiceImpl.scaleDownIfNeeded(source, 800);
        assertEquals(800, result.getWidth());
        assertEquals(450, result.getHeight());
    }

    @Test
    void scaleDownIfNeeded_shouldScaleLargePortraitImage() {
        BufferedImage source = new BufferedImage(900, 1600, BufferedImage.TYPE_INT_RGB);
        BufferedImage result = PictureServiceImpl.scaleDownIfNeeded(source, 800);
        assertEquals(450, result.getWidth());
        assertEquals(800, result.getHeight());
    }

    @Test
    void scaleDownIfNeeded_shouldPreserveAspectRatioForSquareImage() {
        BufferedImage source = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_RGB);
        BufferedImage result = PictureServiceImpl.scaleDownIfNeeded(source, 800);
        assertEquals(800, result.getWidth());
        assertEquals(800, result.getHeight());
    }

    @Test
    void scaleDownIfNeeded_shouldSupportTransparentPng() {
        BufferedImage source = new BufferedImage(1200, 600, BufferedImage.TYPE_INT_ARGB);
        source.setRGB(0, 0, Color.RED.getRGB());
        BufferedImage result = PictureServiceImpl.scaleDownIfNeeded(source, 800);
        assertEquals(800, result.getWidth());
        assertEquals(400, result.getHeight());
    }
}
