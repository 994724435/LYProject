package com.liaoyin.lyproject.socket.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName SocketServer
 * @Title 套接字服务
 * @Author Set
 * @Date 2018/8/19 10:51
 * @Description
 **/
public class SocketServer implements Runnable {

	//端口
	private int port;
	// 通道管理器
	private Selector selector;
	// 获得一个ServerSocket通道
	private ServerSocketChannel serverChannel;
	//总连接数
	private int count = 0;

	/**
	 * 创建套接字服务
	 * @param port 端口
	 * @throws IOException
	 */
	public SocketServer(int port) throws IOException {
		setting(port, null);
	}

	/**
	 * 创建套接字服务
	 * @param port 端口
	 * @param notice 数据通知函数
	 * @throws IOException
	 */
	public SocketServer(int port, DataNotice notice) throws IOException {
		setting(port, notice);
	}

	private void setting(int port, DataNotice notice) throws IOException {
		this.port = port;
		//打开通道管理
		selector = Selector.open();
		serverChannel = ServerSocketChannel.open();
		//绑定监听端口
		InetSocketAddress addr = new InetSocketAddress(port);
		serverChannel.socket().bind(addr);
		//非阻塞
		serverChannel.configureBlocking(false);
		//注册OP_ACCEPT事件  返回通道
		SelectionKey sk = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		//使用接收器进行管理
		sk.attach(new SocketAcceptor(selector, serverChannel, notice));
	}

	@Override
	public void run() {
		//线程中断时停止运行
		while (!Thread.interrupted()) {
			try {
				//连接数为0时，跳出循环
				if (0 == selector.select()){
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			//取得所有连接
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = selectedKeys.iterator();
			while (it.hasNext()) {
				//运行acceptor
				acceptor((SelectionKey) (it.next()));
				it.remove();
			}
		}
	}

	/**
	 * 运行Socket接收器
	 * @param key
	 */
	private void acceptor(SelectionKey key) {
		Runnable run = (Runnable) (key.attachment());
		if (run != null)
			//运行SocketAcceptor.run();
			run.run();
	}
}
