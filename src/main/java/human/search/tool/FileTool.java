package human.search.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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
}
