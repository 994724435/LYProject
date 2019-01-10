package com.liaoyin.lyproject.socket.main;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @ClassName Session
 * @Title
 * @Author Set
 * @Date 2018/8/19 13:29
 * @Description
 **/
public class SocketSession {

    private SocketChannel channel;

    SocketSession(SocketChannel channel){
        this.channel = channel;
    }

    /**
     * 读数据
     * @param buffer
     * @return
     * @throws IOException
     */
    int read(ByteBuffer buffer) throws IOException {
        if(channel != null){
            return channel.read(buffer);
        }
        return -1;
    }

    /**
     * 获取地址字符串
     * @return
     */
    public String getLocalSocketAddress(){
        String result = "";
        if(channel != null){
            try {
                result = channel.getLocalAddress().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 关闭会话连接
     * @throws IOException
     */
    public void close() throws IOException {
        if(channel != null){
            channel.close();
        }
    }

    /**
     * 向会话发送数据
     * @param buffer
     * @return 成功/失败
     * @throws IOException
     */
    public boolean send(ByteBuffer buffer){
        try {
            synchronized (channel){
                if(channel != null && channel.isOpen() && channel.isConnected()){
                    channel.write(buffer);
                    return true;
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return false;
    }
}
