package blossom.project.config.server.controller;

import blossom.project.config.common.constants.BlossomConstants;
import blossom.project.config.common.entity.Result;
import blossom.project.config.common.enums.EventTypeEnum;
import blossom.project.config.server.entity.Config;
import blossom.project.config.server.service.ConfigService;
import blossom.project.config.server.service.LongPollingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static blossom.project.config.common.constants.BlossomConstants.SEPARATOR;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/27 23:45
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * ConfigController类
 * 当前Controller需要提供基础的配置中心的CRUD的接口
 * 同时，当前Controller的作用是用来测试各种类型的长轮询方案
 * 1：使用 HttpServletRequest 的 startAsync 方法开始异步处理。
 * 这个方法返回一个 AsyncContext 对象，通过它可以控制请求的异步处理流程。
 * 2：使用DefferedResult，这个东西基于MVC，并且类似于Promise的用法。
 * 可以设定onComplete和onResult方法。结果一旦被设定，就会真的返回数据。
 */
@RestController
@RequestMapping(BlossomConstants.CONFIG_CONTROLLER_PATH)
@RequiredArgsConstructor
public class ConfigController {

    @Autowired
    private ConfigService service;
    @Autowired
    private LongPollingService longPollingService;

    private final Map<String, DeferredResult<String>> requestsMap = new ConcurrentHashMap<>();


    /**
     * 获取配置信息
     * @param configId
     * @param namespace
     * @param group
     * @param type
     * @return
     */
    @GetMapping("")
    public Result getConfig(
            @RequestParam(value = "configId") String configId,
            @RequestParam(value = "namespace") String namespace,
            @RequestParam(value = "group") String group,
            @RequestParam(value = "type",required = false) String type) {
        //将客户端请求放到内存进行监听
        //一旦配置发生变更就马上返回变更数据
        return Result.ok(service.getConfig(configId, namespace,group,type));
    }

    /**
     * 发布配置
     * @param config
     * @return
     */
    @PostMapping("")
    public Result publishConfig(@RequestBody Config config) {
        service.publishConfig(config);
        return Result.ok();
    }

    @DeleteMapping("")
    public Result removeConfig(
            @RequestParam(value = "namespace") String namespace,
            @RequestParam(value = "group") String group,
            @RequestParam(value = "configId") String configId) {
        service.removeConfig(configId, namespace, group);
        return Result.ok();
    }

    // 测试长轮询解决方案 后续将这些方案整合到正式的请求
    @GetMapping("/subscribe")
    public void subscribe(String namespace, String group, String configId,
                               HttpServletRequest request, HttpServletResponse response) {
        longPollingService.subscribeConfig(namespace, group, configId, request, response);
    }

    /**
     * 当前方法只是测试方法，因为配置变更的时候就应该通知对应的监听当前配置的连接了
     * @param namespace
     * @param group
     * @param configId
     * @param content
     * @return
     */
    @Deprecated
    @PostMapping("/publish")
    public String publishAsync(String namespace, String group, String configId, String content) {
        //1:修改配置文件信息
        //2:找到对应的key的订阅者，并对其进行处理
        longPollingService.notifySubscriber(EventTypeEnum.PUBLISH,namespace, group, configId, content);
        return "Message sent to all subscribers.";
    }


    @GetMapping("/subscribe-deferred")
    public DeferredResult<String> subscribeDeferred(String namespace, String group, String configId,
                                                    HttpServletRequest request, HttpServletResponse response) {
        String key = namespace + SEPARATOR + group + SEPARATOR + configId;
        DeferredResult<String> result = new DeferredResult<>(10000L, "Timeout");
        requestsMap.put(key, result);
        result.onCompletion(() -> requestsMap.remove(result));
        return result;
    }

    @PostMapping("/publish-deferred")
    public String publishDeferred(String namespace, String group, String configId, String content) {
        String key = namespace + SEPARATOR + group + SEPARATOR + configId;
        DeferredResult<String> result = requestsMap.get(key);
        result.setResult(content);
        return "Message sent to  subscribers.";
    }

}
