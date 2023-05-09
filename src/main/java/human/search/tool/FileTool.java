package human.search.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;

public class FileTool {
    public static final Logger logger= LoggerFactory.getLogger(FileTool.class);

    public static String getClassPath(){
        return FileTool.class.getClassLoader().getResource("").getPath();
    }

    public static void checkPath(String path){
        File file=new File(path);
        if(!file.exists()){
            file.mkdir();
        }
    }

    public static String createImageName(){
        return System.currentTimeMillis()+"_" + UUID.randomUUID().toString().replace("-","")+".jpg";
    }
    /**
     * 本地存储图片url
     * @return
     */
    public static String createLocalUrl(String imageName){
        String imageTmpPath=ApplicationTool.getParam("image.tmp.path");
        return imageTmpPath+File.separator+imageName;
    }

    public static String createNetUrl(String serverAddress,String imageName){
        return serverAddress+"/image/getImage?i="+imageName;
    }
}
