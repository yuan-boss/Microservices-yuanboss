package cn.itcast.mq.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @module:
 * @description:
 * @author: yuan_boss
 * @create: 2022-08-05 10:00
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue(){
        String queueName = "simple.queue";
        String message = "hello,spring amqp";
        rabbitTemplate.convertAndSend(queueName,message);
    }
    @Test
    public void testSendMessage2FanoutQueue(){
        String exChange = "yuan.fanout";
        String message = "hello,fanout";
        //发送消息，参数分别是：交换机名称，RoutingKey（暂时为空），消息
        rabbitTemplate.convertAndSend(exChange,"",message);
    }
    @Test
    public void testSendMessage2DirectQueue(){
        String exChange = "yuan.direct";
        String message = "hello,yellow";
        //发送消息，参数分别是：交换机名称，RoutingKey（暂时为空），消息
        rabbitTemplate.convertAndSend(exChange,"yellow",message);
    }

    @Test
    public void testSendMessage2TopicQueue(){
        String exChange = "yuan.topic";
        String message = "hello,中国的长城";
        //发送消息，参数分别是：交换机名称，RoutingKey，消息
        rabbitTemplate.convertAndSend(exChange,"china.local",message);
    }

    @Test
    public void testObjectQueue(){
        Map<String,Object> msg = new HashMap<>();
        msg.put("name","渊哥");
        msg.put("age",22);
        rabbitTemplate.convertAndSend("object.queue",msg);
    }
}
