package com.liaoyin.lyproject.websocket;


import com.liaoyin.lyproject.common.Config;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocketImpl;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 
 * 创 建 人 ：
 * 创建日期：2018年2月28日
 * 描    述：开启websocket线程
 */
@Component
@Slf4j
public class StartFilter implements ApplicationRunner {
	/**
	 *
	 * 创 建 人 ：
	 * 创建日期：2018年2月28日
	 * 描       述： 启动即时socket服务
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("websocket已启动...");
		String fozu =
				"                   _ooOoo_" + "\n" +
						"                  o8888888o" + "\n" +
						"                  88\" ● \"88" + "\n" +
						"                  (| -_- |)  .o〇我保佑此程序永无bug！！！" + "\n" +
						"                  O\\  =  /O" + "\n" +
						"               ____/`---'\\____" + "\n" +
						"             .'  \\\\|     |//  `." + "\n" +
						"            /  \\\\|||  :  |||//  \\" + "\n" +
						"           /  _||||| -:- |||||-  \\" + "\n" +
						"           |   | \\\\\\  -  /// |   |" + "\n" +
						"           | \\_|  ''\\---/''  |   |" + "\n" +
						"           \\  .-\\__  `-`  ___/-. /" + "\n" +
						"         ___`. .'  /--.--\\  `. . __" + "\n" +
						"      .\"\" '<  `.___\\_<|>_/___.'  >'\"\"." + "\n" +
						"     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |" + "\n" +
						"     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /" + "\n" +
						"======`-.____`-.___\\_____/___.-`____.-'======" + "\n" +
						"                   `=---='" + "\n" +
						"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + "\n";
		System.out.println(fozu);
		WebSocketImpl.DEBUG = false;
		WsServer s;
		s = new WsServer(Config.port_websocket);
		s.start();
	}
}
