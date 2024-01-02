package blossom.project.config.common.utils;

import blossom.project.config.common.entity.Result;
import com.alibaba.fastjson.JSON;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/30 19:47
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * HttpUtils类
 */

public class HttpUtils {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static String sendGetRequest(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        Result result = JSON.parseObject(response.body(), Result.class);
        return (String) result.getData();
    }

    public static boolean sendPostRequest(String url, String content) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(BodyPublishers.ofString(content))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    public static boolean sendDeleteRequest(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        return response.statusCode() == 200;
    }
}
