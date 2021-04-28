package com.study01.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtil {
    public static Connection getConnection() throws IOException, TimeoutException {
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
        return connection;
    }
}
