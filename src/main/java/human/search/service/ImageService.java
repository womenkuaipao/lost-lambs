package human.search.service;

import com.arcsoft.face.Rect;
import com.arcsoft.face.toolkit.ImageInfo;
import human.search.controller.vo.DetectResult;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface ImageService {
    /**
     * 获取图片信息
     * @param bufferedImage
     * @return
     */
    public ImageInfo getImageInfo(BufferedImage bufferedImage);

    /**
     * 获取图片检测框
     * @return
     */
    public List<Rect> getImageRects(ImageInfo imageInfo);

    /**
     * 裁剪图片
     * @param rects
     * @return
     */
    public DetectResult cutImage(List<Rect> rects, BufferedImage bufferedImage);
}
