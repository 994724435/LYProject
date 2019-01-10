package com.liaoyin.lyproject.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：Redis工具类
 * @日期：Created in 2018/6/8 15:08
 */
@Component
public class RedisUtil {


	private static RedisTemplate redisTemplate;

	@Autowired
	public void setRedisTemplate(RedisTemplate redisTemplate) {
		RedisUtil.redisTemplate=redisTemplate;
	}

	/**
	 * @方法名：del
	 * @描述： 删除缓存<br>
	 * 	       根据key精确匹配删除
	 * @作者：
	 * @日期： Created in 2018/6/8 15:09
	 */
	public static void del(String... key) {
		if (key != null && key.length > 0) {
			if (key.length == 1) {
				redisTemplate.delete(key[0]);
			} else {
				redisTemplate.delete(CollectionUtils.arrayToList(key));
			}
		}
	}

	/**
	 * @方法名：batchDel
	 * @描述： 批量删除<br>
	 * 	       （该操作会执行模糊查询，请尽量不要使用，以免影响性能或误删）
	 * @作者：
	 * @日期： Created in 2018/6/8 15:10
	 */
	public static void batchDel(String... pattern) {
		for (String kp : pattern) {
			redisTemplate.delete(redisTemplate.keys(kp + "*"));
		}
	}

	/**
	 * @方法名：get
	 * @描述： 获取缓存<br>
	 * 	 	    注：该方法暂不支持Character数据类型
	 * @作者：
	 * @日期： Created in 2018/6/8 15:10
	 */
	public static <T> T get(String key, Class<T> clazz) {
		return (T) redisTemplate.boundValueOps(key).get();
	}

	/**
	 * 作者：
	 * 时间： 2018/7/31 17:17
	 * 描述： redis根据key读取值，返回Object
	 **/
	public static Object get(String key) {
		return redisTemplate.boundValueOps(key).get();
	}

	/**
	 * 作者：
	 * 时间： 2018/7/31 17:26
	 * 描述： redis根据key读取值，返回String
	 **/
	public static String getString(String key) {
		return (String)redisTemplate.boundValueOps(key).get();
	}

	/**
	 * 作者：
	 * 时间： 2018/7/31 17:26
	 * 描述： redis根据key读取值，返回int
	 **/
	public static int getInt(String key) {
		return (int)redisTemplate.boundValueOps(key).get();
	}

	/**
	 * 作者：
	 * 时间： 2018/7/31 17:26
	 * 描述： redis根据key读取值，返回double
	 **/
	public static double getDouble(String key) {
		return (double)redisTemplate.boundValueOps(key).get();
	}

	/**
	 * 作者：
	 * 时间： 2018/7/31 17:26
	 * 描述： redis根据key读取值，返回long
	 **/
	public static double getLong(String key) {
		return (double)redisTemplate.boundValueOps(key).get();
	}

	/**
	 * 作者：
	 * 时间： 2018/7/31 17:26
	 * 描述： redis根据key读取值，返回BigDecimal
	 **/
	public static BigDecimal getBigDecimal(String key) {
		return (BigDecimal)redisTemplate.boundValueOps(key).get();
	}

	/**
	 * @方法名：set
	 * @描述： 将value对象写入缓存
	 * @作者：
	 * @param expireTime    失效时间(秒)
	 * @日期： Created in 2018/6/8 15:11
	 */
	public static void set(String key, Object value, Long expireTime) {
		redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
	}

	/**
	 * @方法名：set
	 * @描述： 将value对象写入缓存-不设置失效时间
	 * @作者：
	 * @日期： Created in 2018/6/8 15:11
	 */
	public static void set(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 指定缓存的失效时间
	 *
	 * @param key
	 *            缓存KEY
	 * @param expireTime
	 *            失效时间(秒)
	 */
	/**
	 * @方法名：expire
	 * @描述： 指定缓存的失效时间
	 * @作者：
	 * @param key   缓存KEY
	 * @param expireTime  失效时间(秒)
	 * @日期： Created in 2018/6/8 15:12
	 */
	public static void expire(String key, Long expireTime) {
		if (expireTime > 0) {
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
		}
	}

	/**
	 * @方法名：keys
	 * @描述： 模糊查询keys
	 * @作者：
	 * @日期： Created in 2018/6/8 15:12
	 */
	public static Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	/**
	 * @方法名：flushAll
	 * @描述： 清空缓存
	 * @作者：
	 * @日期： Created in 2018/6/8 15:13
	 */
	public static void flushAll(){
		redisTemplate.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return null;
			}
		});
	}
}
