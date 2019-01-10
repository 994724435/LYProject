package com.liaoyin.lyproject.listener;

import com.liaoyin.lyproject.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

@Component
@Slf4j
public class PropertiesCommandLineRunner implements CommandLineRunner {

    @Value("${project.properties.files}")
    private String files;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始读取");
        try {
            if (StringUtils.isNoneBlank(files)) {
                Properties prop = new Properties();
                files = files.trim();
                String[] f = files.split(",");
                for (String fileName : f) {
                    prop.load(new InputStreamReader(PropertiesCommandLineRunner.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
                }
                PropertiesUtil.init(prop);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
