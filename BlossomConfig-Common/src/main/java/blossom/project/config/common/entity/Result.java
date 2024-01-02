package blossom.project.config.common.entity;

   


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * 响应信息主体
 *
 * @author: 张锦标
 * @date: 2023/2/23 18:36
 * Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    public static final Integer FAIL = 500;

    private int code;

    private String msg;

    private T data;

    /**
     * 扩展字段 根据不同的情况会有不同的值
     */
    private String ext;

    public static <T> Result<T> ok()
    {
        return restResult(null, SUCCESS, null);
    }

    public static <T> Result<T> ok(T data)
    {
        return restResult(data, SUCCESS, null);
    }

    public static <T> Result<T> ok(T data, String msg)
    {
        return restResult(data, SUCCESS, msg);
    }
    public static <T> Result<T> ok(T data, String msg,String ext)
    {
        return restResult(data, SUCCESS, msg,ext);
    }

    public static <T> Result<T> fail(String ext)
    {
        return restResult(null, FAIL, null,ext);
    }

    public static <T> Result<T> fail(String msg,String ext)
    {
        return restResult(null, FAIL, msg,ext);
    }

    public static <T> Result<T> fail(T data,String ext)
    {
        return restResult(data, FAIL, null,ext);
    }

    public static <T> Result<T> fail(T data, String msg,String ext)
    {
        return restResult(data, FAIL, msg,ext);
    }

    public static <T> Result<T> fail(T data,int code, String msg,String ext)
    {
        return restResult(data, code, msg,ext);
    }

    private static <T> Result<T> restResult(T data, int code, String msg)
    {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
    private static <T> Result<T> restResult(T data, int code, String msg,String ext)
    {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setExt(ext);
        return apiResult;
    }

    public static <T> Boolean isError(Result<T> ret)
    {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(Result<T> ret)
    {
        return Result.SUCCESS == ret.getCode();
    }
}
