package com;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;

import java.io.RandomAccessFile;
import java.util.Properties;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws NacosException {
        Properties properties = new Properties();
        String serverAddr = "10.36.1.54:8848";
        String namespace = "a27eb4c0-4f9d-4203-9330-ce55817be0a2";
//        namespace = "a06f7ada-47b8-477c-b7e5-cf3a14c1a0e8";
        String dataId = "application-dev.yml";
        String group = "DEFAULT_GROUP";
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.setProperty(PropertyKeyConst.NAMESPACE, namespace);
        properties.setProperty(PropertyKeyConst.USERNAME, "pay");
        properties.setProperty(PropertyKeyConst.PASSWORD, "pay");
        ConfigService configService = NacosFactory.createConfigService(properties);
        System.out.println(configService.getServerStatus());
        System.out.println(configService.getConfig(dataId, group, 5000));
        String content = "# 信贷核心配置\n" +
                "credit-core:\n" +
                "  tenantConfigs:\n" +
                "    # 租户ID\n" +
                "    - id: 4\n" +
                "      # 商户各产品回调通知地址\n" +
                "      callbackUrls:\n" +
                "        # 贝拉小镇\n" +
                "        BL_WFQ: http://10.36.0.185:18191/callback/creditCore\n" +
                "      # 自营租户\n" +
                "      selfTenant: true";
        configService.publishConfig(dataId, group, content, ConfigType.YAML.getType());
        configService.removeConfig(dataId, group);

    }
}
