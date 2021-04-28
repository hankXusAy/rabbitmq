package com.study01.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
简单模式: 发送消息
 */
public class Product {
    static final String QUEUE_NAME = "simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1,创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //主机:默认localhsot
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        //虚拟主机
        connectionFactory.setVirtualHost("/itcast");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        //2,创建连接
        Connection connection  = connectionFactory.newConnection();
        //3,创建频道
        Channel channel = connection.createChannel();
        //4,声明队列
        /**
         * 参数1:队列名称
         * 参数2:是否定义持久化队列(消息会持久化保存到服务器上)
         * 参数3:是否独占本连接
         * 参数4:是否在不适用的时候队列自动删除
         * 参数5:其他参数
         */
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        //5,发送消息
        String msg = "你好,许绍帅";
        /**
         * 参数1:交换机名称,没有指定空字符串(表示使用默认的交换机)
         * 参数2:路由key,简单模式中可以直接使用队列名称
         * 参数3:消息其他属性
         * 参数4:消息内容
         */
        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
        System.out.println("消息已发送,发送的内容:" + msg);
        //6,关闭资源
        channel.close();
        connection.close();
    }

}
