package human.search.controller;

import com.arcsoft.face.Rect;
import com.arcsoft.face.toolkit.ImageInfo;
import human.search.controller.vo.BaseResult;
import human.search.controller.vo.DetectResult;
import human.search.exception.BusinessException;
import human.search.service.ImageService;
import human.search.tool.FileTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
    public BaseResult<DetectResult> detectImage(@RequestParam(name="file")MultipartFile image){
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
        DetectResult detectResult = imageService.cutImage(imageRects, bufferedImage);
        return BaseResult.success(detectResult);
    }

    @GetMapping("/getImage")
    public void getImageStream(HttpServletRequest request,HttpServletResponse response){
        String imageName = request.getParameter("i");
        String localUrl = FileTool.createLocalUrl(imageName);
        response.setContentType("image/jpeg");
        try {
            OutputStream outputStream = response.getOutputStream();
            byte[] bytes = FileUtils.readFileToByteArray(new File(localUrl));
            outputStream.write(bytes);
        }catch (Exception e){
            throw new BusinessException("获取图片失败",e);
        }
    }
}
