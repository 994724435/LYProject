package com.liaoyin.lyproject.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "s_user")
public class SUser {
    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 账号
     */
    @Column(name = "userAccount")
    private String useraccount;

    /**
     * 密码
     */
    private String password;

    /**
     * 二级密码
     */
    @Column(name = "twoPassword")
    private String twopassword;

    /**
     * 昵称
     */
    @Column(name = "userNickName")
    private String usernickname;

    /**
     * 真实姓名
     **/
    @Column(name = "realName")
    private String realName;

    /**
     * 头像
     */
    @Column(name = "userHeadImg")
    private String userheadimg;

    /**
     * 最后登录时间
     */
    @Column(name = "lastLoginTime")
    private Date lastlogintime;

    /**
     * 令牌
     */
    private String token;

    /**
     * 长连接的唯一标识
     */
    @Column(name = "socket_uuid")
    private String socketUuid;

    /**
     * 是否禁用 0：否 1：是
     */
    @Column(name = "isDelete")
    private Integer isdelete;

    /**
     * 0：未激活  1：激活  2：冻结  3：封号  4：黑名单
     */
    private Integer status;

    /**
     * 账号主从：0-主账号   1-子账号
     */
    private Integer arrangement;

    /**
     * 推荐人id
     **/
    @Column(name = "refereeId")
    private Integer refereeId;

    /**
     * 是否允许撞单 0-允许 1-不允许
     **/
    @Column(name = "mold")
    private Integer mold;

    /**
     * 注册时间
     */
    @Column(name = "createDate")
    private Date createdate;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取账号
     *
     * @return userAccount - 账号
     */
    public String getUseraccount() {
        return useraccount;
    }

    /**
     * 设置账号
     *
     * @param useraccount 账号
     */
    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取二级密码
     *
     * @return twoPassword - 二级密码
     */
    public String getTwopassword() {
        return twopassword;
    }

    /**
     * 设置二级密码
     *
     * @param twopassword 二级密码
     */
    public void setTwopassword(String twopassword) {
        this.twopassword = twopassword;
    }

    /**
     * 获取昵称
     *
     * @return userNickName - 昵称
     */
    public String getUsernickname() {
        return usernickname;
    }

    /**
     * 设置昵称
     *
     * @param usernickname 昵称
     */
    public void setUsernickname(String usernickname) {
        this.usernickname = usernickname;
    }

    /**
     * 获取头像
     *
     * @return userHeadImg - 头像
     */
    public String getUserheadimg() {
        return userheadimg;
    }

    /**
     * 设置头像
     *
     * @param userheadimg 头像
     */
    public void setUserheadimg(String userheadimg) {
        this.userheadimg = userheadimg;
    }

    /**
     * 获取最后登录时间
     *
     * @return lastLoginTime - 最后登录时间
     */
    public Date getLastlogintime() {
        return lastlogintime;
    }

    /**
     * 设置最后登录时间
     *
     * @param lastlogintime 最后登录时间
     */
    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    /**
     * 获取令牌
     *
     * @return token - 令牌
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置令牌
     *
     * @param token 令牌
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取长连接的唯一标识
     *
     * @return socket_uuid - 长连接的唯一标识
     */
    public String getSocketUuid() {
        return socketUuid;
    }

    /**
     * 设置长连接的唯一标识
     *
     * @param socketUuid 长连接的唯一标识
     */
    public void setSocketUuid(String socketUuid) {
        this.socketUuid = socketUuid;
    }

    /**
     * 获取是否禁用 0：否 1：是
     *
     * @return isDelete - 是否禁用 0：否 1：是
     */
    public Integer getIsdelete() {
        return isdelete;
    }

    /**
     * 设置是否禁用 0：否 1：是
     *
     * @param isdelete 是否禁用 0：否 1：是
     */
    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    /**
     * 获取0：未激活  1：激活  2：冻结  3：封号  4：黑名单
     *
     * @return status - 0：未激活  1：激活  2：冻结  3：封号  4：黑名单
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0：未激活  1：激活  2：冻结  3：封号  4：黑名单
     *
     * @param status 0：未激活  1：激活  2：冻结  3：封号  4：黑名单
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取账号主从：0-主账号   1-子账号
     *
     * @return arrangement - 账号主从：0-主账号   1-子账号
     */
    public Integer getArrangement() {
        return arrangement;
    }

    /**
     * 设置账号主从：0-主账号   1-子账号
     *
     * @param arrangement 账号主从：0-主账号   1-子账号
     */
    public void setArrangement(Integer arrangement) {
        this.arrangement = arrangement;
    }

    /**
     * 获取注册时间
     *
     * @return createDate - 注册时间
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * 设置注册时间
     *
     * @param createdate 注册时间
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Integer getRefereeId() {
        return refereeId;
    }

    public void setRefereeId(Integer refereeId) {
        this.refereeId = refereeId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getMold() {
        return mold;
    }

    public void setMold(Integer mold) {
        this.mold = mold;
    }
}