package human.search.service.impl;

import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.Rect;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import human.search.controller.vo.DetectResult;
import human.search.exception.BusinessException;
import human.search.service.ImageService;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import human.search.tool.ApplicationTool;
import human.search.tool.FileTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {
    public static final Logger logger= LoggerFactory.getLogger(ImageServiceImpl.class);

    @Value("${image.tmp.path}")
    private String imageTmpPath;

    @Autowired
    private FaceEngine faceEngine;

    @Override
    public ImageInfo getImageInfo(BufferedImage bufferedImage) {
        return ImageFactory.bufferedImage2ImageInfo(bufferedImage);
    }

    ImageServiceImpl(){
        //加载opencv库，使用opencv处理图片速度非常快
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    @Override
    public List<Rect> getImageRects(ImageInfo imageInfo) {
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
        List<Rect> rects = faceInfoList.stream().map(f -> f.getRect()).collect(Collectors.toList());
        return rects;
    }

    @Override
    public DetectResult cutImage(List<Rect> rects, BufferedImage bufferedImage) {
        DetectResult detectResult=new DetectResult();
        List<String> imageUrls=new ArrayList<>();
        if(CollectionUtils.isEmpty(rects)){
            throw new BusinessException("未检测出人脸");
        }
        byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        Mat bigPicMat=new Mat(bufferedImage.getHeight(),bufferedImage.getWidth(), CvType.CV_8UC3);
        bigPicMat.put(0,0,data);
        FileTool.checkPath(imageTmpPath);
        String serverAddress = ApplicationTool.getContextInfo();
        //存储大图
        String bkgName = FileTool.createImageName();
        String localBkgUrl = FileTool.createLocalUrl(bkgName);
        String netBkgUrl = FileTool.createNetUrl(serverAddress, bkgName);
        Imgcodecs.imwrite(localBkgUrl,bigPicMat);
        detectResult.setBkgUrl(netBkgUrl);
        //存储小图
        for(int i=0;i<rects.size();i++){
            Mat cutMat=new Mat(bigPicMat,transRect(rects.get(i)));
            String imageName= FileTool.createImageName();
            String cutImageName=FileTool.createLocalUrl(imageName);
            String netUrl=FileTool.createNetUrl(serverAddress,imageName);
            Imgcodecs.imwrite(cutImageName,cutMat);
            imageUrls.add(netUrl);
        }
        detectResult.setFaceUrls(imageUrls);
        return detectResult;
    }

    private org.opencv.core.Rect transRect(Rect rect){
        int x=rect.getLeft();
        int y=rect.getTop();
        int width=rect.getRight()-x;
        int height=rect.getBottom()-y;
        org.opencv.core.Rect oRect=new org.opencv.core.Rect(x,y,width,height);
        return oRect;
    }
}
