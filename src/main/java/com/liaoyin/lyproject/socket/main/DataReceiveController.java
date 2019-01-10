package com.liaoyin.lyproject.socket.main;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName SocketDataController
 * @Title 数据接收处理
 * @Author Set
 * @Date 2018/8/19 11:26
 * @Description
 **/
public class DataReceiveController implements ControllerListener {

    private SelectionKey selectionKey;

    private DataNotice notice;

    private SocketSession session;

    public DataReceiveController() {
    }

    @Override
    public void execute(SocketController controller, SelectionKey selectionKey, SocketSession session, ThreadPoolExecutor pool) throws IOException {
        this.selectionKey = selectionKey;
        this.session = session;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //读取字节
        int len = session.read(buffer);
        //读取为-1时，视为客户端已断开连接，并结束套接字
        if(len == -1) {
            System.out.println("[Warning!] A client has been closed.");
            controller.close();
            return;
        }
        if(len == 0){
            return;
        }
        //清理事件
//        controller.clearListener();
        //接收到数据后，进行消息回调通知
        long dd = pool.getTaskCount();
        //System.out.println("任务数："+pool.getActiveCount());
        pool.execute(new CallbackThread(buffer));
    }

    @Override
    public void notice(DataNotice notice) {
        this.notice = notice;
    }

    /*
     * 消息回调通知
     */
    synchronized void callback(ByteBuffer buffer) {
//        System.out.println("当前线程数："+threadCount());
        //消息回调不为空时，调用回调函数，否则打印消息
        if(notice != null){
            notice.receive(buffer, session);
        } else {
            System.out.println(bytesToHex(buffer.array()));
        }
        //注册OP_WRITE事件
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        //使阻塞的Selector立即返回
        selectionKey.selector().wakeup();
    }
    /*
	 * 数据回调处理
	 */
    class CallbackThread implements Runnable {

        ByteBuffer buf;

        public CallbackThread(ByteBuffer buf) {
            this.buf=buf;
        }

        @Override
        public void run() {
            callback(buf);
        }
    }

    /**
     * 将数组转为16进制字符串
     * @param cmd
     */
    private String bytesToHex(byte[] cmd){
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : cmd){
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = Integer.toHexString(0xFF & b);
            // 每个字节8为，转为16进制标志，2个16进制位
            if (tmp.length() == 1)  {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase();
    }

    private int threadCount(){
        //获取线程数
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        while(threadGroup.getParent() != null){
            threadGroup = threadGroup.getParent();
        }
        int totalThread = threadGroup.activeCount();
        return totalThread;
    }
}
