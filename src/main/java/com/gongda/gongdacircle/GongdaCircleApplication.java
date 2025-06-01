package com.gongda.gongdacircle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 工大圈应用启动类
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.gongda.gongdacircle.mapper")
public class GongdaCircleApplication {

    public static void main(String[] args) {
        SpringApplication.run(GongdaCircleApplication.class, args);
        System.out.println("==========================================");
        System.out.println("(♥◠‿◠)ﾉﾞ  工大圈启动成功   ლ(´ڡ`ლ)ﾞ  ");
        System.out.println("==========================================");
    }
} 