package com.liaoyin.lyproject.dao;

import com.liaoyin.lyproject.entity.TSystemUserLockVote;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface TSystemUserLockVoteMapper extends Mapper<TSystemUserLockVote> {

    /**
     * 作者：
     * 时间： 2018/10/9 11:34
     * 描述： 根据被锁定用户查询是否存在上次流程
     **/
    TSystemUserLockVote selectUserLockVoteLockUserAndStatus(Integer lockUserId);

    /**
     * 作者：
     * 时间： 2018/10/9 11:41
     * 描述： 查询正在锁定的流程
     **/
    TSystemUserLockVote selectUserLockVoteLockStatus();

    /**
     * 作者：
     * 时间： 2018/10/10 14:43
     * 描述： 查询记录
     **/
    List<Map<String,Object>> selectSystemUserLockVote(String nowUserId);
}