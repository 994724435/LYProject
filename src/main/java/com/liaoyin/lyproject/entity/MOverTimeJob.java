package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "m_overtime_job")
public class MOverTimeJob {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 匹配单id
     */
    @Column(name = "mateId")
    private Integer mateid;

    /**
     * 工作时间
     */
    @Column(name = "overDate")
    private Date overdate;

    /**
     * 0-打款超时  1-确认收款超时
     */
    private Integer mold;

    /**
     * 0-未触发  1-触发   2-已停止
     */
    private Integer status;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取匹配单id
     *
     * @return mateId - 匹配单id
     */
    public Integer getMateid() {
        return mateid;
    }

    /**
     * 设置匹配单id
     *
     * @param mateid 匹配单id
     */
    public void setMateid(Integer mateid) {
        this.mateid = mateid;
    }

    /**
     * 获取工作时间
     *
     * @return overDate - 工作时间
     */
    public Date getOverdate() {
        return overdate;
    }

    /**
     * 设置工作时间
     *
     * @param overdate 工作时间
     */
    public void setOverdate(Date overdate) {
        this.overdate = overdate;
    }

    /**
     * 获取0-打款超时  1-确认收款超时
     *
     * @return mold - 0-打款超时  1-确认收款超时
     */
    public Integer getMold() {
        return mold;
    }

    /**
     * 设置0-打款超时  1-确认收款超时
     *
     * @param mold 0-打款超时  1-确认收款超时
     */
    public void setMold(Integer mold) {
        this.mold = mold;
    }

    /**
     * 获取0-未触发  1-触发
     *
     * @return status - 0-未触发  1-触发
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0-未触发  1-触发
     *
     * @param status 0-未触发  1-触发
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}