package com.liaoyin.lyproject.socket.main;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SocketController
 * @Title 套接字管理器
 * @Author Set
 * @Date 2018/8/19 11:09
 * @Description
 **/
public class SocketController implements Runnable {

    private final SelectionKey selectionKey;

    private final SocketSession session;

    //默认线程池大小
    private static final int THREAD_COUNTING = 3000;
    //默认最大线程数
    private static final int THREAD_COUNTING_MAX = 3000;
    //默认最大空闲时间(秒)
    private static final int THREAD_MAX_FREE_TIME = 10;

    //使用线程池管理所有连接
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(
            THREAD_COUNTING, THREAD_COUNTING_MAX, THREAD_MAX_FREE_TIME, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    private ControllerListener listener;
    //消息回调通知
    private DataNotice notice;

    public SocketController(SelectionKey selectionKey, SocketSession session, DataNotice notice) {
        this.selectionKey = selectionKey;
        this.session = session;
        this.notice = notice;
        //数据处理
        listener = new DataReceiveController();
        //最大线程数
//        pool.setMaximumPoolSize(64);
    }

    public void run() {
        try {
            if(listener != null){
                listener.notice(notice);
                listener.execute(this, selectionKey, session, pool);
            }
        } catch (IOException e) {
            close();
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            selectionKey.cancel();
            session.close();
            System.out.println("========================= 设备退出 =========================");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
