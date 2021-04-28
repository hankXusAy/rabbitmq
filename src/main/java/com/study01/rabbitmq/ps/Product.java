package com.study01.rabbitmq.ps;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.study01.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/*
发布/订阅模式: 发送消息

一个消息可以被多个消费者接收
 */
public class Product {
    //交换机名称
    static final String FANOUT_EXCHANGE = "fanout_exchange";

    //队列名称

    static final String QUEUE_NAME1 = "fanout_queue1";
    static final String QUEUE_NAME2 = "fanout_queue2";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1,创建连接
        Connection connection = ConnectionUtil.getConnection();
        //2,创建频道
        Channel channel = connection.createChannel();
        //声明交换机 参数1:交换机名称 参数2:交换机类型(fanout direct topic)
        channel.exchangeDeclare(FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);
        //4,声明队列
        /**
         * 参数1:队列名称
         * 参数2:是否定义持久化队列(消息会持久化保存到服务器上)
         * 参数3:是否独占本连接
         * 参数4:是否在不适用的时候队列自动删除
         * 参数5:其他参数
         */

        channel.queueDeclare(QUEUE_NAME1,true,false,false,null);
        channel.queueDeclare(QUEUE_NAME2,true,false,false,null);
        //5:队列绑定到交换机
        channel.queueBind(QUEUE_NAME1,FANOUT_EXCHANGE,"");
        channel.queueBind(QUEUE_NAME2,FANOUT_EXCHANGE,"");
        //6,发送消息
        for (int i = 0; i < 10; i++) {
            String msg = "你好,许绍帅" + i;
            /**
             * 参数1:交换机名称,没有指定空字符串(表示使用默认的交换机)
             * 参数2:路由key,简单模式中可以直接使用队列名称
             * 参数3:消息其他属性
             * 参数4:消息内容
             */
            channel.basicPublish(FANOUT_EXCHANGE,"",null,msg.getBytes());
            System.out.println("消息已发送,发送的内容:" + msg);
        }
        //6,关闭资源
        channel.close();
        connection.close();
    }

}
