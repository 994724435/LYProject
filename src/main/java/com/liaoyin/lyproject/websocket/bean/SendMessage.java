package com.liaoyin.lyproject.websocket.bean;

/**
 * 作者：
 * 时间：2018/7/12 16:24
 * 描述：
 */
public class SendMessage {
    private Integer status;//-1连接错误 0 推送信息  1断开连接，使用示例：单点登录  2其他消息  200 连接成功
    private String title;//标题
    private String message;//消息内容
    private String attachData;//附带数据包
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getAttachData() {
        return attachData;
    }
    public void setAttachData(String attachData) {
        this.attachData = attachData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SendMessage(Integer status, String title, String message, String attachData) {
        this.status = status;
        this.title = title;
        this.message = message;
        this.attachData = attachData;
    }

    public SendMessage() {
        super();
    }
}
