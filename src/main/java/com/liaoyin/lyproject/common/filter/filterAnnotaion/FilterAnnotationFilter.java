package com.liaoyin.lyproject.common.filter.filterAnnotaion;

import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.util.RedisUtil;
import com.liaoyin.lyproject.ztest.util.ClasspathPackageScanner;
import com.liaoyin.lyproject.ztest.util.PackageScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：
 * 时间：2018/8/10 11:29
 * 描述：指定接口安全过滤
 */
@Component
@Slf4j
public class FilterAnnotationFilter implements ApplicationRunner {

    private static final Map<String,Boolean> filter = new HashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //扫描包下的类
        PackageScanner scan = new ClasspathPackageScanner(Config.controllerUrl);
        List<String> classArray = scan.getFullyQualifiedClassNameList();
        for (String cls : classArray) {
            Class targetClass = Class.forName(cls);
            //根据名称取到对应类的Class并得到类下所有的方法
            Method[] as = targetClass.getMethods();
            for (int i = 0; i < as.length; i++) {
                for (int j = 0; j < as[i].getDeclaredAnnotations().length; j++) {
                    //根据方法得到对应的注解
                    Annotation annotation = as[i].getDeclaredAnnotations()[j];
                    //自定义注解名称
                    String customName = FilterAnnotation.class.getName();
                    //接口全路径
                    StringBuilder interfaceUrl = new StringBuilder();
                    //当前接口包含了指定注解
                    if (Common.isEqual(annotation.toString(), "@".concat(customName).concat("()"))) {
                        //路径位置
                        RequestMapping mp = (RequestMapping) targetClass.getAnnotation(RequestMapping.class);
                        interfaceUrl.append(mp.value()[0]);
                        PostMapping postMapping = as[i].getAnnotation(PostMapping.class);
                        if (!Common.isNull(postMapping)) {
                            String ul = postMapping.value()[0];
                            if (!ul.startsWith("/")) interfaceUrl.append("/").append(ul);
                            else interfaceUrl.append(ul);
                        }
                        System.out.println("当前接口：" + interfaceUrl.toString() + "拥有自定义注解");
                        //将接口放入Redis
                        set(interfaceUrl.toString().concat(Config.FiterAnnotaion),true);
                    }
                }
            }
        }
    }

    /**
     * 作者：
     * 时间： 2018/10/31 15:11
     * 描述： 注入
     **/
    public static void set(String key,Boolean boo){
        filter.put(key,boo);
    }

    /**
     * 作者：
     * 时间： 2018/10/31 15:12
     * 描述： 验证是否存在
     **/
    public static boolean get(String key){
        return filter.containsKey(key);
    }
}
