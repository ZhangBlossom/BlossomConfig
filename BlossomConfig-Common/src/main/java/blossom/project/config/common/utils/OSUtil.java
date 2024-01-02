package blossom.project.config.common.utils;

import java.io.File;

/**
 * @author: 张锦标
 * @date: 2024/1/2 15:31
 * OSUtil类
 */
public class OSUtil {

    private static final String CONFIG_DIRECTORY_NAME = "/blossomConfigCache";

    /**
     * 获取配置文件的存储路径
     * @return 配置文件的路径
     */
    public static String getConfigFilePath() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            // Windows系统
            return "C:\\blossomConfig" + CONFIG_DIRECTORY_NAME;
        } else {
            // Linux或其他类Unix系统
            return "/etc" + CONFIG_DIRECTORY_NAME;
        }
    }

    /**
     * 创建配置文件存储目录
     */
    public static void createConfigDirectoryIfNeeded() {
        String directoryPath = getConfigFilePath();
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                // 处理无法创建目录的情况
            }
        }
    }
}
