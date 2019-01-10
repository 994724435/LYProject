package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "m_job")
public class MJob {
    /**
     * 主键id
     */
    @Id
    private Integer id;

    @Column(name = "mateId")
    private Integer mateid;

    /**
     * 工作时间
     */
    @Column(name = "overDate")
    private Date overdate;

    /**
     * 0-未入账  1-已入账
     **/
    private Integer status;


    /**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return mateId
     */
    public Integer getMateid() {
        return mateid;
    }

    /**
     * @param mateid
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}