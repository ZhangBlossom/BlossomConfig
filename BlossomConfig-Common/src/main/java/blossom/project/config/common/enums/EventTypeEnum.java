package blossom.project.config.common.enums;/**
 * @Author:Serendipity
 * @Date:
 * @Description:
 */

import lombok.Data;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/30 17:14
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * EventTypeEnum枚举类
 */
public enum EventTypeEnum {

    /**
     * 当前是一个发布事件
     */
    PUBLISH((String) "PUBLISH"),
    /**
     * 当前是删除事件
     */
    REMOVE((String) "REMOVE"),
    /**
     * 忽略事件
     */
    IGNORE((String) "IGNORE");
    private String event;

    EventTypeEnum(String event) {
        this.event = event;
    }

    public static EventTypeEnum findByEvent(String event) {
        for (EventTypeEnum reqType : EventTypeEnum.values()) {
            if (reqType.event == event) {
                return reqType;
            }
        }
        return null;
    }

    public String getValue() {
        return this.event;
    }
}

    