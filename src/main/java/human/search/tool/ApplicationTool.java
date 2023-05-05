package human.search.tool;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ApplicationTool implements ApplicationContextAware, EnvironmentAware {
    private static ApplicationContext applicationContext=null;
    private static Environment environment=null;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationTool.applicationContext=applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        ApplicationTool.environment=environment;
    }
    /**
     * 获取服务器信息
     * @return
     */
    public static String getContextInfo(){
        try {
            InetAddress address=InetAddress.getLocalHost();
            String localAddr=address.getHostAddress();
            ServletRequestAttributes requestAttributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request=requestAttributes.getRequest();
//          String localAddr=request.getLocalAddr();
            int serverPort=request.getServerPort();
            String contextPath = environment.getProperty("server.servlet.context-path");
            return "http://"+localAddr+":"+serverPort+contextPath;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }



}
