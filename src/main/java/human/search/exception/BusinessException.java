package human.search.exception;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException{
    private String msg;
    private Throwable cause;
    public BusinessException(String msg){
        this.msg=msg;
    }
    public BusinessException(String msg,Throwable cause){
        this.msg=msg;
        this.cause=cause;
    }
}
