package blossom.project.config.core.value;

import blossom.project.config.core.ConfigApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author: ZhangBlossom
 * @date: 2023/12/24 21:30
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 */
@Component
public class ConfigurablePropertyResolver {
    public static void refreshBean(Object bean, org.springframework.core.env.ConfigurablePropertyResolver propertyResolver) {

        // 定义EL表达式解释器

        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

        TemplateParserContext templateParserContext = new TemplateParserContext();


        String keyResolver, valueResolver = null;

        Object parserValue;

        // 获取真实对象属性


        Field[] declaredFields = bean.getClass().getDeclaredFields();

        boolean cglib = Arrays.stream(declaredFields).anyMatch(x -> x.getName().contains("CGLIB"));

        // 如果是cglib 代理找其父类

        if (cglib) {

            declaredFields = bean.getClass().getSuperclass().getDeclaredFields();

        }


        // 遍历Bean实例所有属性

        for (Field field : declaredFields) {

            // 判断field是否含有@Value注解

            if (field.isAnnotationPresent(Value.class)) {

                // 读取Value注解占位符

                keyResolver = field.getAnnotation(Value.class).value();

                try {

                    // 读取属性值

                    valueResolver = propertyResolver.resolveRequiredPlaceholders(keyResolver);

                    // EL表达式解析

                    // 兼容形如：@Value("#{'${codest.five.url}'.split(',')}")含有EL表达式的情况

                    Expression expression = spelExpressionParser.parseExpression(valueResolver, templateParserContext);

                    if (field.getType() == Boolean.class) {

                        parserValue = Boolean.valueOf(expression.getValue().toString());

                    } else if (field.getType() == Integer.class) {

                        parserValue = Integer.valueOf(expression.getValue().toString());

                    } else if (field.getType() == Long.class) {

                        parserValue = Long.valueOf(expression.getValue().toString());

                    } else {

                        parserValue = expression.getValue(field.getType());


                    }


                } catch (IllegalArgumentException e) {

                    continue;

                }

                // 判断配置项是否存在

                if (Objects.nonNull(valueResolver)) {

                    field.setAccessible(true);

                    try {

                        field.set(bean, parserValue);

                        continue;

                    } catch (IllegalAccessException e) {

                        e.printStackTrace();

                    }

                }

            }

        }
    }
}
