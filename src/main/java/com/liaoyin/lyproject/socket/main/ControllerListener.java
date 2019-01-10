package com.liaoyin.lyproject.socket.main;/**
 * Created by Set on 2018/8/19.
 */

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName ControllerListener
 * @Title 管理器监听
 * @Author Set
 * @Date 2018/8/19 11:20
 * @Description
 **/
public interface ControllerListener {

    void execute(SocketController controller, SelectionKey selectionKey, SocketSession session,
                 ThreadPoolExecutor pool) throws IOException;

    void notice(DataNotice listener);
}
