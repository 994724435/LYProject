package com.liaoyin.lyproject.entity;

import javax.persistence.*;

@Table(name = "s_dict")
public class SDict {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 编码
     */
    private String code;

    /**
     * 类型汇总
     */
    @Column(name = "dictType")
    private String dicttype;

    /**
     * 分类型
     */
    @Column(name = "displayValue")
    private String displayvalue;

    /**
     * 真实值
     */
    @Column(name = "realValue")
    private Double realvalue;

    /**
     * 父id
     */
    @Column(name = "parentId")
    private Integer parentid;

    /**
     * 获取id主键
     *
     * @return id - id主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id主键
     *
     * @param id id主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取编码
     *
     * @return code - 编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置编码
     *
     * @param code 编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取类型汇总
     *
     * @return dictType - 类型汇总
     */
    public String getDicttype() {
        return dicttype;
    }

    /**
     * 设置类型汇总
     *
     * @param dicttype 类型汇总
     */
    public void setDicttype(String dicttype) {
        this.dicttype = dicttype;
    }

    /**
     * 获取分类型
     *
     * @return displayValue - 分类型
     */
    public String getDisplayvalue() {
        return displayvalue;
    }

    /**
     * 设置分类型
     *
     * @param displayvalue 分类型
     */
    public void setDisplayvalue(String displayvalue) {
        this.displayvalue = displayvalue;
    }

    /**
     * 获取真实值
     *
     * @return realValue - 真实值
     */
    public Double getRealvalue() {
        return realvalue;
    }

    /**
     * 设置真实值
     *
     * @param realvalue 真实值
     */
    public void setRealvalue(Double realvalue) {
        this.realvalue = realvalue;
    }

    /**
     * 获取父id
     *
     * @return parentId - 父id
     */
    public Integer getParentid() {
        return parentid;
    }

    /**
     * 设置父id
     *
     * @param parentid 父id
     */
    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }
}