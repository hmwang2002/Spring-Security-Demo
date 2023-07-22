package com.whm.spring_security_demo.utils;

import com.alibaba.fastjson.JSON;
import com.whm.spring_security_demo.domain.vo.ResponseResult;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author whm
 * @date 2023/7/22 17:56
 */
public class ResponseUtils {
    public static void setSuccessResponse(HttpServletResponse response, int code, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        response.getWriter().write(JSON.toJSONString(ResponseResult.success(code, message)));
    }

    public static void setSFailedResponse(HttpServletResponse response, int code, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(code);
        response.getWriter().write(JSON.toJSONString(ResponseResult.failed(code, message)));
    }
}
