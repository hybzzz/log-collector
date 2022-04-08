package demo;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.csplice.log.collector.slf4j.CspliceMarkerFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@RestController("demo")
@Slf4j
public class DemoApplication2 {
        public static void main(String[] args) {
            SpringApplication.run(DemoApplication2.class, args);
        }
        @GetMapping("test/{id}")
        @SneakyThrows
        public String test(@PathVariable Long id){
            Marker marker = CspliceMarkerFactory.getMarker(id + "");
            log.info(marker,"微服务2开始执行");
            Thread.sleep(1000);
            log.info(marker,"微服务2执行:{}",1);
            Thread.sleep(1000);

            log.info(marker,"微服务2执行:{}",2);
            Thread.sleep(1000);

            log.info(marker,"微服务2执行:{}",3);
            Thread.sleep(1000);

            log.info(marker,"微服务2执行:{}",4);
            Thread.sleep(1000);

            log.info(marker,"微服务2执行:{}",5);
            Thread.sleep(1000);

            log.info(marker,"微服务2执行:{}",6);
            Thread.sleep(1000);

            log.info(marker,"微服务2执行结束");
            return "success";
        }
}
