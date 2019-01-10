package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.MMate;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface MMateMapper extends Mapper<MMate> {

    /**
     * 作者：
     * 时间： 2018/10/4 16:35
     * 描述： 查询匹配单记录
     **/
    List<MMate> selectMateToUser(@Param("planToonUserId") Integer planToonUserId, @Param("cashapplyUserId") Integer cashapplyUserId,
                                 @Param("status") Integer status,@Param("planToonId") Integer planToonId,
                                 @Param("cashapplyId") Integer cashapplyId,@Param("type")Integer type);

    /**
     * 作者：
     * 时间： 2018/10/15 14:58
     * 描述： 匹配单记录的总价值
     **/
    BigDecimal selectMateToUserSumPrice(@Param("planToonUserId") Integer planToonUserId, @Param("cashapplyUserId") Integer cashapplyUserId,
                                        @Param("status") Integer status,@Param("planToonId") Integer planToonId,
                                        @Param("cashapplyId") Integer cashapplyId,@Param("type")Integer type);

    /**
     * 作者：
     * 时间： 2018/10/4 16:49
     * 描述： 记录详细信息
     **/
    Map<String,Object> selectMateRecordDetail(Integer mateId);

    /**
     * 作者：
     * 时间： 2018/10/10 18:27
     * 描述： 查询匹配记录
     **/
    List<Map<String,Object>> selectMateRecordToAll(@Param("key") String key, @Param("startDate") Date startDate,
                                                   @Param("endDate") Date endDate, @Param("status") Integer status,
                                                   @Param("mold")Integer mold);

    /**
     * 作者：
     * 时间： 2018/10/18 17:15
     * 描述： 根据用户id拉取打了款的记录
     **/
    List<MMate> selectMateOut(Integer userId);

    /**
     * 作者：
     * 时间： 2018/10/18 17:18
     * 描述： 首页统计
     **/
    BigDecimal selectHomeZJS(@Param("startDate") Date startDate,
                             @Param("endDate") Date endDate);
    Map<String,Object> selectHomeCSZJS(@Param("startDate") Date startDate,
                               @Param("endDate") Date endDate);
    BigDecimal selectHomeZCRS(@Param("startDate") Date startDate,
                              @Param("endDate") Date endDate);
    BigDecimal selectHomeCCZJES();
    BigDecimal selectHomePDZJES();

}