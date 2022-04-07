package com.demo;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nti56.csplice.log.utils.LogSubUtils;
import com.nti56.csplice.ws.WsUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@Controller("demo")
@ComponentScan("com.nti56.*")
@Slf4j
public class DemoApplication3 {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setConnectTimeout(60000);
        httpComponentsClientHttpRequestFactory.setReadTimeout(60000);
        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(60000);
        RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
        MappingJackson2HttpMessageConverter jsonHttpMessageConverter =
                new MappingJackson2HttpMessageConverter();
        jsonHttpMessageConverter.getObjectMapper()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jsonHttpMessageConverter.setSupportedMediaTypes(new ArrayList<MediaType>(){{add(MediaType.APPLICATION_JSON_UTF8);}});
        restTemplate.getMessageConverters().add(jsonHttpMessageConverter);
        return restTemplate;
    }
        public static void main(String[] args) {
            SpringApplication.run(DemoApplication3.class, args);
        }
        @GetMapping("sub/{id}")
        @SneakyThrows
        public void test(@PathVariable Long id){
            LogSubUtils.subscribe("redis://139.159.194.101:21181"
                    ,"csplice_log_"+id,(c,m)->{
                        System.out.println(c);
                        System.out.println(m);
                        WsUtils.sendMessageToChannelUsers(m,c);

                    });
        }
}
