package com.liaoyin.lyproject.websocket;

import java.io.Serializable;
import java.net.InetSocketAddress;

import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.common.Config;
import com.liaoyin.lyproject.util.PropertiesUtil;
import com.liaoyin.lyproject.websocket.bean.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 创 建 人 ：  创建日期：2018年2月28日 描 述：socket服务
 */
@Slf4j
public class WsServer extends WebSocketServer implements Serializable{

	private static final long serialVersionUID = 1L;

	public WsServer(int port) {
		super(new InetSocketAddress(port));
	}

	public WsServer(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// ws连接的时候触发的代码，onOpen中我们不做任何操作
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		log.info("客户端断开了一个连接");
		// 断开连接时候触发代码
		userLeave(conn);
	}

	@Override
	public void onMessage(WebSocket conn, String user) {
		if (Common.isNull(user)){
			//发送错误信息
			conn.send(JSON.toJSONString(new SendMessage(-1,Config.sysTitle,PropertiesUtil.get("common.tokenInvalid"),"")));
			return;
		}
		// 用户连接
		if (null != user && user.startsWith("online")) {
			String token = user.substring(6, user.length());// 去掉前缀鉴证
			//判定当前连接用户是否是合法用户

			//判断当前用户标识是否在连接中存在 当存在时发送消息通知并断开连接
			WebSocket socket = WsPool.getWsByUser(token);
			if (!Common.isNull(socket)) {
				int status=0;//0发送给app  1发送给pc
				if (token.endsWith(PropertiesUtil.get("socket.pc"))){
					status=1;
				}
				//当前连接已存在，发送提示信息
				WsPool.sendMessageToUser(token, JSON
						.toJSONString(new SendMessage(1,Config.sysTitle,PropertiesUtil.get("common.otherDevice"), "")), status);
				//将原连接剔除
				WsPool.removeUser(socket);
			}
			// 用户加入连接
			userJoin(conn, token);
			// 将连接成功信息返回给客户端
			conn.send(JSON.toJSONString(new SendMessage(200,Config.sysTitle,PropertiesUtil.get("common.socketSuccess"),"")));
			log.info("用户" + "'" + token + "'" + "已加入");
			// 加入之后的处理逻辑请在以下执行(例：将未链接时的未读消息返回)-------------------------------

		} else if (null != user && user.startsWith("offline")) {
			userLeave(conn);
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		// 错误时候触发的代码
		log.info("出现一个错误");
		ex.printStackTrace();
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2018年2月28日 描 述：去除掉失效的websocket链接
	 * 
	 * @param conn
	 */
	private void userLeave(WebSocket conn) {
		WsPool.removeUser(conn);
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2018年2月28日 描 述：将websocket加入用户池
	 * 
	 * @param conn
	 * @param userName
	 */
	private void userJoin(WebSocket conn, String userName) {
		WsPool.addUser(userName, conn);
	}

}
