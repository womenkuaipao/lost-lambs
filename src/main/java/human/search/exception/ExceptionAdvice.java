package human.search.exception;

import human.search.controller.ImageController;
import human.search.controller.vo.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    public static final Logger logger= LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(BusinessException.class)
    public BaseResult<String> businessExceptionHandle(BusinessException businessException){
        return BaseResult.fail(-1,businessException.getMsg(),null);
    }

    @ExceptionHandler(Exception.class)
    public BaseResult<String> businessExceptionHandle(Exception exception){
        logger.error("系统内部异常",exception);
        return BaseResult.fail(-2,"系统内部异常",null);
    }
}
