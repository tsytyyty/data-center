package com.data.center.pojo.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result {

    //返回值
    private int code;

    //提示信息
    private String message;

    //其他（List、对象等）
    private Object object;

    public static Result success(Object object) {
        return new Result().setCode(0).setMessage("success").setObject(object);
    }

    public static Result success(String msg, Object object) {
        return new Result().setCode(0).setMessage(msg).setObject(object);
    }

    public static Result error(String message) {
        return new Result().setCode(500).setMessage(message);
    }

    public static Result error(String message, Object object) {
        return new Result().setCode(500).setMessage(message).setObject(object);
    }

}
