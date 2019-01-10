package com.liaoyin.lyproject.common;

/**
 * 作者：
 * 时间：2018/9/25 9:44
 * 描述：第三方公共参数
 */
public class CommonParamter {

    //凌凯短信
    public static final String name = "CQJS003210";
    public static final String password = "xsy@123";
    public static final String KYUrl = "https://sdk2.028lk.com/sdk2/LinkWS.asmx/BatchSend2";
    public static final String KYErroCode = "–1\t账号未注册\n" +
            "–2\t其他错误\n" +
            "–3\t帐号或密码错误\n" +
            "–5\t余额不足，请充值\n" +
            "–6\t定时发送时间不是有效的时间格式\n" +
            "-7\t提交信息末尾未签名，请添加中文的企业签名【 】\n" +
            "–8\t发送内容需在1到300字之间\n" +
            "-9\t发送号码为空\n" +
            "-10\t定时时间不能小于系统当前时间" +
            "-100\tIP黑名单\n" +
            "-102\t账号黑名单\n" +
            "-103\tIP未导白";



}
