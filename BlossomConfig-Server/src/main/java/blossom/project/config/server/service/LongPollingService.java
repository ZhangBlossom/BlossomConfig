package blossom.project.config.server.service;

import blossom.project.config.common.entity.Result;
import blossom.project.config.common.enums.EventTypeEnum;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static blossom.project.config.common.constants.BlossomConstants.SEPARATOR;

@Service
public class LongPollingService {

    private final Map<String, AsyncContext> contextsMap = new ConcurrentHashMap();

    /**
     * 添加订阅者
     *
     * @param namespace
     * @param group
     * @param configId
     * @param request
     * @param response
     */
    public void subscribeConfig(String namespace, String group, String configId,
                                HttpServletRequest request, HttpServletResponse response) {
        String key = namespace + SEPARATOR + group + SEPARATOR + configId;
        AsyncContext context = request.startAsync();
        // Set timeout, e.g., 10 seconds
        context.setTimeout(60 * 1000);
        //TODO 读多写少的场景 考虑使用一个读写锁来解决并发问题
        //if (true) {
        //    Config config = configService.getConfig(configId, namespace, group, type);
        //    if (Objects.isNull(config)) {
        //        contextsMap.put(key, context);
        //    } else {
        //        //如果有配置文件就直接返回 否则让他进行监听等待
        //        try {
        //            context.getResponse().getWriter().write(config.getContent());
        //        } catch (IOException e) {
        //            throw new RuntimeException(e);
        //        }
        //        context.complete();
        //    }
        //}
        contextsMap.put(key,context);

    }

    /**
     * 通知监听当前配置文件的请求，并进行响应
     *
     * @param namespace
     * @param group
     * @param configId
     * @param content
     */
    public void notifySubscriber(EventTypeEnum eventTypeEnum , String namespace,
                                 String group, String configId, String content) {
        String key = namespace + SEPARATOR + group + SEPARATOR + configId;
        AsyncContext context = contextsMap.get(key);

        switch (eventTypeEnum){
            case PUBLISH -> {
                try {
                    Result<String> result = Result.ok(content, null, EventTypeEnum.PUBLISH.getValue());
                    String jsonString = JSON.toJSONString(result);
                    context.getResponse().getWriter().write(jsonString);
                    context.complete();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    contextsMap.remove(key);
                }
            }
            case REMOVE -> {
                try {
                    //告诉客户端是哪一个配置文件被移除了
                    Result<String> result = Result.ok(key, null, EventTypeEnum.REMOVE.getValue());
                    String jsonString = JSON.toJSONString(result);
                    context.getResponse().getWriter().write(jsonString);
                    context.complete();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    contextsMap.remove(key);
                }
            }
            //测试用
            case IGNORE -> {
                try {
                    context.getResponse().getWriter().write("ignore-event:"+key+",this is test respose");
                    context.complete();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    contextsMap.remove(key);
                }
            }
        }

    }

}
