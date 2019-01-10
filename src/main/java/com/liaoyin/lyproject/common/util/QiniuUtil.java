package com.liaoyin.lyproject.common.util;

import java.io.IOException;
import java.util.UUID;

import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.util.RestUtil;
import org.springframework.web.multipart.MultipartFile;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class QiniuUtil {
	public static final String ACCESS_KEY = "aTzHI3UGTwGRfea6vcSSNtTXlZs0y8_K2XEIzcRa"; // 这两个登录七牛
	// 账号里面可以找到
	public static final String SECRET_KEY = "wyPrwVsm5fVZ_-bP0-EKv1bi-zKEi3gsefo-2Wh1";
	// 要上传的空间
	public static final String bucketname = "cs-365"; // 对应要上传到七牛上 你的那个路径（自己建文件夹 注意设置公开）
	// 路径
	public static final String url = "pif8irfd1.bkt.clouddn.com";
	// 密钥配置
	Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
	// 创建上传对象
	UploadManager uploadManager = new UploadManager();

	// 简单上传，使用默认策略，只需要设置上传的空间名就可以了
	public String getUpToken() {
		return auth.uploadToken(bucketname);
	}

	public JsonRestResponse upload(MultipartFile file) throws IOException {
		try {
			String key = UUID.randomUUID().toString();
			// 获取文件名称
			String fileName = file.getOriginalFilename();
			// 文件名称后的类型
			String extensionName = fileName
					.substring(fileName.lastIndexOf("."));
			key += extensionName;
			// 调用put方法上传
			Response res = uploadManager
					.put(file.getBytes(), key, getUpToken());
			// 打印返回的信息
			System.out.println(res.bodyString());
			return RestUtil.createResponse(key);
		} catch (QiniuException e) {
			Response r = e.response;
			// 请求失败时打印的异常的信息
			System.out.println(r.bodyString());
			return RestUtil.createResponseErro(r.bodyString());
		}
	}
	
	public String download(String url){
		return auth.privateDownloadUrl(url);
	}

}
