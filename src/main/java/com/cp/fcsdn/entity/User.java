package com.cp.fcsdn.entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

import javax.persistence.*;

/**
 * @author chengpeng
 * @company 趋势自家游
 * @create 2019-03-19 16:52
 * 用户实体类
 */
@Entity
@Table(name="t_user")
public class User {

    @Id
    @GeneratedValue
    private Integer id;//编号

    @NotEmpty(message="请输入用户名！")
    @Column(length=100)
    private String userName;//用户名

    @NotEmpty(message="请输入密码！")
    @Column(length=100)
    private String password;//密码

    @Email(message="邮箱地址格式有误！")
    @NotEmpty(message="请输入邮箱地址！")
    @Column(length=100)
    private String email;//验证邮箱地址

    @Column(length=100)
    private String imageName;//用户图像

    private Integer point=0;//用户积分

    private boolean isVip=false;//是否是vip

    private boolean isOff=false;//是否被封禁

    private String roleName="会员";//角色名称

    private Date registerDate;//注册日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public boolean isOff() {
        return isOff;
    }

    public void setOff(boolean off) {
        isOff = off;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
}
