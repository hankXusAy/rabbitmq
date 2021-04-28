package com.study01.rabbitmq.ps;

import com.rabbitmq.client.*;
import com.study01.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模式:消费者接收消息
 */
public class Consumer2 {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(Product.FANOUT_EXCHANGE,BuiltinExchangeType.FANOUT);
        //声明队列
        channel.queueDeclare(Product.QUEUE_NAME2,true,false,false,null);
        //绑定队列到交换机
        channel.queueBind(Product.QUEUE_NAME2,Product.FANOUT_EXCHANGE,"");
        //前面过程和生产者一样,下面创建的是消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //路由key
                System.out.println("路由key" + envelope.getRoutingKey());
                //交换机
                System.out.println("交换机" + envelope.getExchange());
                //消息id
                System.out.println("消息id" + envelope.getDeliveryTag());
                //接收到的消息
                System.out.println("消费者 2 --- 接收到的消息" + new String(body,"UTF-8"));
            }
        };
        //监听队列
        /**
         * 参数1:队列名
         * 参数2:是否自动确认;设置为true表示消息接收到之后自动向mq回复收到了,mq则会将消息从队列
         *      中删除;false则需要手动确认
         * 参数3:消费者
         */
        channel.basicConsume(Product.QUEUE_NAME2,true,defaultConsumer);
    }
}
