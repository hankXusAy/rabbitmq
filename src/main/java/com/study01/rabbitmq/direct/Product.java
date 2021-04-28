package com.study01.rabbitmq.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study01.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
路由模式: 发送消息

一个消息可以被多个消费者接收
 */
public class Product {
    //交换机名称
    static final String DIRECT_EXCHANGE = "direct_exchange";

    //队列名称

    static final String DIRECT_QUEUE_INSERT = "direct_queue_insert";
    static final String DIRECT_QUEUE_UPDATE = "direct_queue_update";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1,创建连接
        Connection connection = ConnectionUtil.getConnection();
        //2,创建频道
        Channel channel = connection.createChannel();
        //声明交换机 参数1:交换机名称 参数2:交换机类型(fanout direct topic)
        channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);
        //4,声明队列
        /**
         * 参数1:队列名称
         * 参数2:是否定义持久化队列(消息会持久化保存到服务器上)
         * 参数3:是否独占本连接
         * 参数4:是否在不适用的时候队列自动删除
         * 参数5:其他参数
         */
        channel.queueDeclare(DIRECT_QUEUE_INSERT,true,false,false,null);
        channel.queueDeclare(DIRECT_QUEUE_UPDATE,true,false,false,null);
        //5:队列绑定到交换机
        channel.queueBind(DIRECT_QUEUE_INSERT,DIRECT_EXCHANGE,"insert");
        channel.queueBind(DIRECT_QUEUE_UPDATE,DIRECT_EXCHANGE,"update");
        //6,发送消息
        String insert = "你好,许绍帅;routing key insert";
        /**
         * 参数1:交换机名称,没有指定空字符串(表示使用默认的交换机)
         * 参数2:路由key,简单模式中可以直接使用队列名称
         * 参数3:消息其他属性
         * 参数4:消息内容
         */
        channel.basicPublish(DIRECT_EXCHANGE,"insert",null,insert.getBytes());
        System.out.println("消息已发送,发送的内容:" + insert);

        String update = "你好,许绍帅;routing key update";
        /**
         * 参数1:交换机名称,没有指定空字符串(表示使用默认的交换机)
         * 参数2:路由key,简单模式中可以直接使用队列名称
         * 参数3:消息其他属性
         * 参数4:消息内容
         */
        channel.basicPublish(DIRECT_EXCHANGE,"update",null,update.getBytes());
        System.out.println("消息已发送,发送的内容:" + update);

        //6,关闭资源
        channel.close();
        connection.close();
    }

}
