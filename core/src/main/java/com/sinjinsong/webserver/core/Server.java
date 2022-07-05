package com.sinjinsong.webserver.core;

import com.sinjinsong.webserver.core.servlet.base.RequestDispatcher;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by SinjinSong on 2017/7/20.
 */
@Slf4j
public class Server {
    private static final int DEFAULT_PORT = 8080;
    private ServerSocket server;// 创建一个ServerSocket套接字
    private Acceptor acceptor;// 创建一个请求接受者，并创建一个线程
    private RequestDispatcher requestDispatcher;// 拿到线程之后具体步骤

    public Server() {
        this(DEFAULT_PORT);
    }

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            acceptor = new Acceptor();
            acceptor.start();
            requestDispatcher = new RequestDispatcher();
            log.info("服务器启动");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("初始化服务器失败");
            close();
        }
    }
    // 关闭接受者与之后的服务
    public void close() {
        acceptor.shutdown();
        requestDispatcher.shutdown();
    }
    // 创建一个接受者
    private class Acceptor extends Thread {
        @Override
        public void interrupt() {
            try {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                super.interrupt();
            }
        }
        // 终止线程
        public void shutdown() {
            Thread.currentThread().interrupt();
        }
        // 开启线程
        @Override
        public void run() {
            log.info("开始监听");
            while (!Thread.currentThread().isInterrupted()) {
                Socket client;
                try {
                    // TCP的短连接，请求处理完即关闭
                    client = server.accept();
                    log.info("client:{}", client);
                    // 传入server
                    requestDispatcher.doDispatch(client);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
