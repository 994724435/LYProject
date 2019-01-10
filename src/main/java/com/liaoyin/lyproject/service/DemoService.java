package com.liaoyin.lyproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liaoyin.lyproject.base.api.JsonRestResponse;
import com.liaoyin.lyproject.base.service.BaseService;
import com.liaoyin.lyproject.dao.DemoMapper;
import com.liaoyin.lyproject.entity.Demo;
import com.liaoyin.lyproject.exception.BusinessException;
import com.liaoyin.lyproject.util.RestUtil;
import com.liaoyin.lyproject.util.StringUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @项目名：公司内部模板项目
 * @作者：
 * @描述：Demo业务类
 * @日期：Created in 2018/6/8 14:55
 */
@Service
public class DemoService extends BaseService<DemoMapper, Demo> {


   /**
    * @方法名：selectDemoByPage
    * @描述： 分页查询
    * @作者：
    * @日期： Created in 2018/6/8 16:53
    */
    public PageInfo<Demo>  selectDemoByPage(Demo demo){
        PageHelper.startPage(demo.getNum(),demo.getSize(), demo.getOrderby()==null?null:null);
        List<Demo> list = this.mapper.selectDemoByPage(demo);
        PageInfo<Demo> p = new PageInfo<>(list);
        return p;
    }

    /**
     * @方法名：insertDemo
     * @描述： 新增
     * @作者：
     * @日期： Created in 2018/6/11 10:27
     */
    @Transactional
    public void insertDemo(Demo demo)
    {
        if(demo==null){
            throw new BusinessException("common.entityIsNull");
        }
        this.mapper.insert(demo);
//        Demo d1 = new Demo();
//        d1.setDel("0");
//        d1.setName("aa");
//        Demo d2 = new Demo();
//        d2.setDel("0");
//        d2.setName("bb");
//        List<Demo> list = new ArrayList<>();
//        list.add(d1);
//        list.add(d2);
//        this.mapper.insertList(list);
    }
    /**
     * @方法名：updateDemo
     * @描述： 修改
     * @作者：
     * @日期： Created in 2018/6/11 10:27
     */
    public void updateDemo(Demo demo)
    {
        if(demo==null || demo.getId()==null){
            throw new BusinessException("common.idIsNull");
        }
        this.mapper.updateByPrimaryKeySelective(demo);
    }
    /**
     * @方法名：deleteDemo
     * @描述： 根据主键逻辑删除
     * @作者：
     * @日期： Created in 2018/6/11 10:27
     */
    public void deleteDemo(String id)
    {
        if(StringUtil.isEmpty(id)){
            throw new BusinessException("common.idIsNull");
        }
        Demo demo = new Demo();
        demo.setId(id);
        demo.setDel("1");
        this.mapper.updateByPrimaryKeySelective(demo);
    }
    /**
     * @方法名：deleteDemo
     * @描述： 根据主键物理删除
     * @作者：
     * @日期： Created in 2018/6/11 10:27
     */
    public void deleteDemoPhysical(Demo demo)
    {
        if(demo==null || demo.getId()==null){
            throw new BusinessException("common.idIsNull");
        }
        this.mapper.delete(demo);
    }

}
