package com.whm.spring_security_demo.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whm
 * @date 2023/7/22 17:15
 */
@Data
@NoArgsConstructor
public class ResponseResult<T> {
    /**
     * 返回的状态码
     */
    private Integer code;
    /**
     * 返回的信息提示
     */
    private String message;
    /**
     * 返回的数据
     */
    private T data;

    protected static <T> ResponseResult<T> build(T data) {
        ResponseResult<T> result = new ResponseResult<>();
        if (data != null)
            result.setData(data);
        return result;
    }

    public static <T> ResponseResult<T> build(T body, Integer code, String message) {
        ResponseResult<T> result = build(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }


    public static<T> ResponseResult<T> success(){
        return ResponseResult.success(null);
    }

    public static<T> ResponseResult<T> success(int code, String message){
        return ResponseResult.build(null, code, message);
    }

    public static<T> ResponseResult<T> success(T data){
        return build(data, 200, "操作成功");
    }

    public static<T> ResponseResult<T> failed(){
        return ResponseResult.failed(null);
    }

    public static<T> ResponseResult<T> failed(int code, String message){
        return ResponseResult.build(null, code, message);
    }

    public static<T> ResponseResult<T> failed(T data){
        return build(data, 999, "操作失败");
    }
}
