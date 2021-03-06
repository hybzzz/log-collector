package com.demo;

import com.hybzzz.log.utils.LogSubUtils;
import com.hybzzz.websocket.WsUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@RestController("demo")
@ComponentScan({"com.hybzzz.*","com.demo"})
@Slf4j
public class DemoApplication3 {
    public static void main(String[] args) {
            SpringApplication.run(DemoApplication3.class, args);
        }
    @GetMapping("sub/{channel}")
    @SneakyThrows
    public String test(@PathVariable String channel){
        LogSubUtils.subscribe("redis://localhost:6379",
                (c,m)->{
                    System.out.println(c);
                    System.out.println(m);
                    WsUtils.sendMessageToChannelUsers(m,c);

                },
                channel);
        return "sucesss";
    }

}
