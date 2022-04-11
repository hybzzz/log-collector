package com.demo;

import com.hybzzz.log.utils.LogSubUtils;
import com.nti56.csplice.ws.WsUtils;
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
    @GetMapping("sub/{id}")
    @SneakyThrows
    public String test(@PathVariable Long id){
        LogSubUtils.subscribeAsync("redis://139.159.194.101:21181"
                ,"csplice_log_"+id,(c,m)->{
                    System.out.println(c);
                    System.out.println(m);
                    WsUtils.sendMessageToChannelUsers(m,c);

                });
        return "sucesss";
    }

    @GetMapping("test2")
    @SneakyThrows
    public String test2(){
        return LogSubUtils.getCount()+"";
    }
}
