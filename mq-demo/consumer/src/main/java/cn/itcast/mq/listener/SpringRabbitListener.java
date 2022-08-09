package cn.itcast.mq.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @module:
 * @description:
 * @author: yuan_boss
 * @create: 2022-08-05 10:27
 **/

@Component
public class SpringRabbitListener {

/*    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String msg){

        System.out.println("消费者接收到 simple.queue的消息：【"+msg+"】");
    }*/

//    @RabbitListener(queues = "fanout.queue1")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "fanout.queue1"),
            exchange = @Exchange(name = "yuan.fanout")
    ))
    public void listenFanoutQueue1(String msg){

        System.out.println("消费者1接收到消息：【"+msg+"】");
    }
//    @RabbitListener(queues = "fanout.queue2")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "fanout.queue2"),
            exchange = @Exchange(name = "yuan.fanout")
    ))
    public void listenFanoutQueue2(String msg){

        System.out.println("消费者2接收到消息：【"+msg+"】");
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "yuan.direct"),
            key = {"red","blue"}
    ))
    public void listenDirectQueue1(String msg){

        System.out.println("消费者1接收到消息：【"+msg+"】");
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "yuan.direct"),
            key = {"red","yellow"}
    ))
    public void listenDirectQueue2(String msg){

        System.out.println("消费者2接收到消息：【"+msg+"】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(name = "yuan.topic",type = ExchangeTypes.TOPIC),
            key = "#.news"
    ))
    public void listenTopicQueue1(String msg){

        System.out.println("消费者1接收到消息：【"+msg+"】");
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue2"),
            exchange = @Exchange(name = "yuan.topic",type = ExchangeTypes.TOPIC),
            key = "china.#"
    ))
    public void listenTopicQueue2(String msg){

        System.out.println("消费者2接收到消息：【"+msg+"】");
    }

    @RabbitListener(queues = "object.queue")
    public void listenObjectQueue(Map<String,Object> msg){
        System.out.println(msg);
    }

}
