//package com.liaoyin.lyproject.socket.service;
//
//
//import com.liaoyin.lyproject.common.Config;
//import com.liaoyin.lyproject.socket.main.SocketServer;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
///**
// * 作者：
// * 时间：2018/7/25 11:45
// * 描述：启动socket通信服务
// */
//@Component
//@Slf4j
//public class SocketMain implements ApplicationRunner {
//
//    /**
//     * 作者：
//     * 时间： 2018/7/25 11:45
//     * 描述： 启动socket通信
//     **/
//    @Override
//    public void run(ApplicationArguments args) {
//
//        //单独开启一个新的线程，socket中的while循环会造成线程阻塞
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    ServiceNotice notice = new ServiceNotice();
//                    new SocketServer(Config.port_socket, notice).run();
//                    log.info("socket已启动...");
//                } catch (Exception e) {
//                    log.info("socket启动发送异常...");
//                    log.error(e.getMessage());
//                }
//            }
//        }).start();
//    }
//}