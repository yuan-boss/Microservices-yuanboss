package cn.itcast.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @module:
 * @description:
 * @author: yuan_boss
 * @create: 2022-08-05 11:19
 **/

@Component
public class FanoutConfig {
//    //声明交换机 yuan.fanout
//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new FanoutExchange("yuan.fanout");
//    }
//
//    //声明队列 fanout.queue1
//    @Bean
//    public Queue fanoutQueue1(){
//        return new Queue("fanout.queue1");
//    }
//
//    //将fanout.queue1 绑定到交换机
//
//    @Bean
//    public Binding binding1(Queue fanoutQueue1,FanoutExchange fanoutExchange){
//        return BindingBuilder
//                .bind(fanoutQueue1)
//                .to(fanoutExchange);
//    }
//
//    //声明队列 fanout.queue2
//    @Bean
//    public Queue fanoutQueue2(){
//        return new Queue("fanout.queue2");
//    }
//    //将fanout.queue2 绑定到交换机
//    @Bean
//    public Binding binding2(Queue fanoutQueue2,FanoutExchange fanoutExchange){
//        return BindingBuilder
//                .bind(fanoutQueue2)
//                .to(fanoutExchange);
//    }

    @Bean
    public Queue objectQueue(){
        return new Queue("object.queue");
    }


}
