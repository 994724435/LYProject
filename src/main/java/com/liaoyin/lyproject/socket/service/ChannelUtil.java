package com.liaoyin.lyproject.socket.service;

import com.liaoyin.lyproject.socket.main.SocketSession;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：
 * 时间：2018/8/24 15:57
 * 描述：
 */
@Slf4j
public class ChannelUtil {

    private static Map<String,SocketSession> channel = new HashMap<>();

    /**
     * 作者：
     * 时间： 2018/8/24 16:01
     * 描述： 添加通道
     **/
    public static void addChannel(SocketSession session){
        log.info(session.getLocalSocketAddress()+"已加入");
        channel.put(session.getLocalSocketAddress(),session);
    }

    /**
     * 作者：
     * 时间： 2018/8/24 16:05
     * 描述： 获取指定设备
     **/
    public static SocketSession getChiannel(String deviceId){
        return channel.get(deviceId);
    }


}
