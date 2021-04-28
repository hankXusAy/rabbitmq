package com.study01.rabbitmq.work;

import com.rabbitmq.client.*;
import com.study01.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单模式:消费者接收消息
 */
public class Consumer01 {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(Product.QUEUE_NAME, true, false, false, null);
        //每次可以取多少个消息
        channel.basicQos(1);

        //前面过程和生产者一样,下面创建的是消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    //路由key
                    System.out.println("路由key" + envelope.getRoutingKey());
                    //交换机
                    System.out.println("交换机" + envelope.getExchange());
                    //消息id
                    System.out.println("消息id" + envelope.getDeliveryTag());
                    //接收到的消息
                    System.out.println("消费者1 接收到的消息" + new String(body, "UTF-8"));
                    Thread.sleep(1000);
                    //确认消息
                    /**
                     * 参数1:消息id
                     * 参数2:false表示只有当前这条被处理
                     */
                    channel.basicAck(envelope.getDeliveryTag(),false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //监听队列
        /**
         * 参数1:队列名
         * 参数2:是否自动确认;设置为true表示消息接收到之后自动向mq回复收到了,mq则会将消息从队列
         *      中删除;false则需要手动确认
         * 参数3:消费者
         */
        channel.basicConsume(Product.QUEUE_NAME, true, defaultConsumer);
    }
}
