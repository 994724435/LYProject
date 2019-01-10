package com.liaoyin.lyproject.websocket;

import com.liaoyin.lyproject.common.Common;
import com.liaoyin.lyproject.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;

import java.util.*;

/**
 * 
 * 创 建 人 ：  创建日期：2018年2月28日 描 述：基类
 */
@Slf4j
public class WsPool {

	private static final Map<WebSocket, String> wsUserMap = new HashMap<>();

	/**
	 * 
	 * 创 建 人 ：
	 * 创建日期：2018年2月28日
	 * 描       述：通过websocket连接获取其对应的用户
	 * @param conn
	 * @return
	 */
	public static String getUserByWs(WebSocket conn) {
		return wsUserMap.get(conn);
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2018年2月28日 描
	 * 述：根据userName获取WebSocket,这是一个list,此处取第一个,在失去连接时已将连接移除
	 * 
	 * @param token 唯一标识
	 *
	 * @return
	 */
	public static WebSocket getWsByUser(String token) {
		Set<WebSocket> keySet = wsUserMap.keySet();
		synchronized (keySet) {
			for (WebSocket conn : keySet) {
				String cuser = wsUserMap.get(conn);
				if (cuser.equals(token)) {
					return conn;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2018年2月28日 描 述：添加连接
	 * 
	 * @param token
	 * @param conn
	 */
	public static void addUser(String token, WebSocket conn) {
		wsUserMap.put(conn, token); // 添加连接
		log.info("当前连接数："+wsUserMap.size());
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2018年2月28日 描
	 * 述：获取所有连接池中的用户，因为set是不允许重复的，所以可以得到无重复的user数组
	 * 
	 * @return
	 */
	public static Collection<String> getOnlineUser() {
		List<String> setUsers = new ArrayList<String>();
		Collection<String> setUser = wsUserMap.values();
		for (String u : setUser) {
			setUsers.add(u);
		}
		return setUsers;
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2018年2月28日 描 述：移除连接池中的连接
	 * 
	 * @param conn
	 * @return
	 */
	public static boolean removeUser(WebSocket conn) {
		if (wsUserMap.containsKey(conn)) {
			wsUserMap.remove(conn); // 移除连接
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2018年2月28日 描 述：向指定的用户发送数据
	 * 
	 * @param token 唯一标识
	 * @param message
	 * @param status 0发送给app  1发送给pc  2发送全部
	 * @return true发送成功  false发送失败
	 */
	public static boolean sendMessageToUser(String token, String message,
			Integer status) {
		//只要不出现异常，都将返回true;
		boolean boo = false;
		if (Common.isNull(status)) {
			log.info("客户端接收类型必传");
			return boo;
		}
		try {
			switch (status){
				case 0 ://发给app
					WebSocket conn_app = getWsByUser(token + PropertiesUtil.get("socket.app"));// 当前消息只发送给app用户
					if (null != conn_app && null != wsUserMap.get(conn_app)) {
						conn_app.send(message);// 执行发送
					}
					break;
				case 1://发给pc
					WebSocket conn_pc = getWsByUser(token + PropertiesUtil.get("socket.pc"));
					if (null != conn_pc && null != wsUserMap.get(conn_pc)) {
						conn_pc.send(message);// 执行发送
					}
					break;
				case 2://app、pc均发送
					WebSocket all_conn_app = getWsByUser(token + PropertiesUtil.get("socket.app"));
					WebSocket all_conn_pc = getWsByUser(token + PropertiesUtil.get("socket.pc"));
					if (!Common.isNull(all_conn_app)) {
						all_conn_app.send(message);
					}
					if (!Common.isNull(all_conn_pc)) {
						all_conn_pc.send(message);
					}
				break;
			}
			boo = true;
		}catch (Exception e){
			boo = false;
		}
		return boo;
	}

	/**
	 * 
	 * 创 建 人 ：  创建日期：2018年2月28日 描 述：向所有的用户发送消息
	 * 
	 * @param message
	 * @param status 0发送给app  1发送给pc  2发送全部
	 */
	public static void sendMessageToAll(String message, Integer status) {
		if (Common.isNull(status)) {
			return;
		}
		Set<WebSocket> keySet = wsUserMap.keySet();
		synchronized (keySet) {
			for (WebSocket conn : keySet) {
				String user = wsUserMap.get(conn);
				switch (status) {
				case 0://发送给app
					if (user != null && !user.endsWith(PropertiesUtil.get("socket.pc"))) {//发送给app
						conn.send(message);
					}
					break;
				case 1://发送给pc
					if (user != null && !user.endsWith(PropertiesUtil.get("socket.app"))) {//发送给app
						conn.send(message);
					}
					break;
				case 2://发送全部
					conn.send(message);
					break;
				}

			}
		}
	}

}
