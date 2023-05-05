package human.search.controller;

import com.arcsoft.face.Rect;
import com.arcsoft.face.toolkit.ImageInfo;
import human.search.controller.vo.BaseResult;
import human.search.exception.BusinessException;
import human.search.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Api(tags="图片相关")
@RestController
@RequestMapping("/image")
public class ImageController {
    public static final Logger logger= LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;

    @ApiOperation(value="检测并且切割图片",httpMethod = "POST")
    @PostMapping("/detectImage")
    public BaseResult<List<String>> detectImage(@RequestParam(name="image")MultipartFile image){
        String fileName=image.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        if(index==-1){
            throw new BusinessException("文件格式错误");
        }
        ImageInfo imageInfo=null;
        BufferedImage bufferedImage = null;
        try {
            bufferedImage=ImageIO.read(image.getInputStream());
            imageInfo = imageService.getImageInfo(bufferedImage);
        } catch (IOException e) {
            throw new BusinessException("获取上传图片失败",e);
        }
        List<Rect> imageRects = imageService.getImageRects(imageInfo);
        List<String> imageUrls = imageService.cutImage(imageRects, bufferedImage);
        return BaseResult.success(imageUrls);
    }
}
