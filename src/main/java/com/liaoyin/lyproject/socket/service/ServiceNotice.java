package com.liaoyin.lyproject.socket.service;


import com.liaoyin.lyproject.socket.main.DataNotice;
import com.liaoyin.lyproject.socket.main.SocketSession;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @ClassName ServiceNotice
 * @Title
 * @Author Set
 * @Date 2018/8/19 13:21
 * @Description
 **/
public class ServiceNotice implements DataNotice {
    @Override
    public void receive(ByteBuffer buffer, SocketSession session) {
        analysisCmd(buffer, session);
    }

    //前导码
    private static final byte[] QDM = {(byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE};
    //起始符
    private static final byte START = (byte) 0xA8;
    //结束符
    private static final byte END = (byte) 0x42;

    /**
     * 前导码校验
     *
     * @param cmd
     * @return
     */
    private boolean qdmCheck(byte[] cmd) {
        if (cmd == null) {
            return false;
        }
        if (cmd.length < 5) {
            return false;
        }
        for (int i = 0; i < QDM.length; i++) {
            if (QDM[i] != cmd[i]) {
                return false;
            }
        }
        cmd = BasicData.bytesSplit(cmd, 5, 0);
        return true;
    }

    /**
     * 解析命令
     */
    private void analysisCmd(ByteBuffer buffer, SocketSession session) {
        byte[] cmds = buffer.array();
        String cmdText = BasicData.bytesToHex(cmds);
        buffer = ByteBuffer.wrap(cmds);
        byte[] cmd = new byte[5];
        buffer.get(cmd);
        //前导码校验
        if (!qdmCheck(cmd)) {
            System.out.println("错误的前导码 CMD:" + cmdText);
            return;
        }
        //获取起始符
        byte start = buffer.get();
        //起始符校验
        if (start != START) {
            System.out.println("错误的起始符:" + start + ", CMD:" + cmdText);
            return;
        }
        //报文总长
        int len = buffer.getShort();
        //总字节
        cmds = BasicData.bytesSplit(cmds, 5, len + 5);
        cmdText = BasicData.bytesToHex(cmds);
        //协议版本号
//		byte version =
        buffer.get();
        //区域代码
        int area = buffer.getShort();
        //传输方向
        byte dire = buffer.get();
        //请求响应标识
        byte reqresp = buffer.get();
        //RTU地址长度
        byte rtulen = buffer.get();
        //RTU地址
        byte[] rtu = new byte[rtulen];
        buffer.get(rtu);
        //数据域长度
        int datalen = buffer.getShort();
        //数据域
        byte[] data = new byte[datalen];

        buffer.get(data);
        //校验码
        int check = buffer.getShort();
        //结束符
        byte end = buffer.get();
        if (end != END) {
            System.out.println("结束码错误 CMD：" + cmdText);
            return;
        }
        //
        cmd = BasicData.bytesSplit(cmds, 0, cmds.length - 3);
        short mycheck = (short) CRC16.calcCrc16(cmd);
//		int mycheck1 = CRC16.calcCrc16(cmd);
//		System.out.println(mycheck1);
        if (mycheck != check) {
            System.out.println("校验错误：" + cmdText);
            return;
        }
        buffer = ByteBuffer.wrap(data);
        //功能码
        byte[] fcode = new byte[2];
        buffer.get(fcode);
        String code = BasicData.bytesToHex(fcode);

        String rtutext = BasicData.bytesToHex(rtu);

        if ("0000".equals(code)) {
//			if(socketPool.containsKey(rtutext)){
//				SocketChannel key = socketPool.get(rtutext);
//				if(key != channel){
//					try {
//						key.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					socketPool.put(rtutext, channel);
//				}
//			}else{
//				socketPool.put(rtutext, channel);
//			}
//			System.out.println("CMD:"+cmdText);
//			SocketService.getInstance().heart(rtutext, area, dire, reqresp, buffer);
//            System.out.println("收到心跳："+rtutext);
            ByteBuffer sendData = ByteBuffer.wrap("张三".getBytes());
            session.send(sendData);
        }
    }
}
