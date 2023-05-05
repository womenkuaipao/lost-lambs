package human.search.controller.vo;

import lombok.Data;

@Data
public class BaseResult<T> {
    private int status;
    private String msg;
    private T data;
    BaseResult(int status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }
    public static <T> BaseResult<T> success(T data){
        return new BaseResult(0,null,data);
    }

    public static <T> BaseResult<T> fail(int status,String msg,T data){
        return new BaseResult<>(status,msg,data);
    }
}
