package demo;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.csplice.log.collector.slf4j.CspliceMarkerFactory;
import com.nti56.csplice.common.util.IdGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 类说明: <br/>
 *
 * @author huangyubin <br/>
 * @version 1.0
 * @date 2022/4/7 14:00<br/>
 * @since JDK 1.8
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@RestController("demo")
@Slf4j
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @GetMapping("test")
    @SneakyThrows
    public Long test(){
        long id = IdGenerator.getId();

        new Thread(()->{
            Marker marker = CspliceMarkerFactory.getMarker(id + "");
            try {
                log.info(marker,"开始执行");
                Thread.sleep(30000);
                log.info(marker,"微服务1执行:{}",1);
                Thread.sleep(1000);
                log.info(marker,"微服务1执行:{}",2);
                Thread.sleep(1000);
                log.info(marker,"微服务1执行:{}",3);
                Thread.sleep(1000);
                log.info(marker,"微服务1执行:{}",4);
                Thread.sleep(1000);
                log.info(marker,"微服务1执行:{}",5);
                Thread.sleep(1000);
                log.info(marker,"微服务1执行:{}",6);
                Thread.sleep(1000);
                log.info(marker,"微服务1执行:{}",7);
                Thread.sleep(1000);
                new RestTemplate().getForEntity("http://localhost:8088/test/"+id,String.class);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                log.info(marker,"执行结束");
            }

        }).start();
        return id ;


    }
}
