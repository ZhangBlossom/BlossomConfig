package blossom.project.config.common.utils;

import java.io.*;

import static blossom.project.config.common.constants.BlossomConstants.SEPARATOR;

/**
 * @author: ZhangBlossom
 * @date: 2024/1/2 15:48
 * @contact: QQ:4602197553
 * @contact: WX:qczjhczs0114
 * @blog: https://blog.csdn.net/Zhangsama1
 * @github: https://github.com/ZhangBlossom
 * @description:
 */
public class FileUtil {
    /**
     * 当前方法用于将配置信息写入到磁盘文件
     *
     * @param key
     * @param content
     * @throws IOException
     */
    public static void writeConfigToFile(String key, String content) throws IOException {
        String configFilePath = OSUtil.getConfigFilePath() + File.separator + key.replace(SEPARATOR, "_") + ".txt";
        File configFile = new File(configFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
            writer.write(content);
        } catch (IOException e) {
            throw new IOException("Failed to write config to file: " + configFilePath, e);
        }
    }

    /**
     * 当前方法用于读取配置文件
     *
     * @param key
     * @return
     * @throws IOException
     */
    public static String readConfigFromFile(String key) throws IOException {
        String configFilePath = OSUtil.getConfigFilePath() + File.separator + key.replace(SEPARATOR, "_") + ".txt";
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            throw new FileNotFoundException("Config file not found: " + configFilePath);
        }
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new IOException("Failed to read config from file: " + configFilePath, e);
        }
        return contentBuilder.toString();
    }

}
