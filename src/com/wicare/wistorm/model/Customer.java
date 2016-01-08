/**
 * Customer.java
 * c
 * TODO
 * 2015-12-28
 */
package com.wicare.wistorm.model;

import java.io.Serializable;

/**
 * Customer 客户
 * 
 * @author c TODO 2015-12-28
 */
public class Customer implements Serializable {

	private int custId;
	private String loginId; // 第三方登录返回的标识ID
	private String custName; // 用户昵称
	private int custType; // 用户类型 0: 无车 1: 车主 2：服务商
	private int serviceType; // 服务商类型（0 销售，1 售后，2 保险，3 理赔，4 代办，5 维修，6 保养）
	private String carBrand; // 车辆品牌
	private String carSeries; // 车型
	private String mobile; // 登陆手机
	private String email; // 邮箱地址
	private String password; // 登陆密码
	private int parentCustId; // 父用户ID
	private String province; // 省份
	private String city; // 城市
	private double[] loc; // 经纬度
	private String logo; // 车主头像
	private String[] photo; // 店铺照片
	private String remark; // 用户简介
	private int sex; // 性别
	private String birth; // 生日
	private String contacts; // 联系人
	private String address; // 联系地址
	private String tel; // 联系电话
	private String idCardType; // 驾照类型
	private String annualInspectDate; // 驾照年审
	private String changeDate; // 换证日期
	private int balance; // 账户余额，仅用于返还现金，暂时不支持充值
	private String createTime; // 创建时间
	private String updateTime; // 更新时间

	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public int getCustType() {
		return custType;
	}

	public void setCustType(int custType) {
		this.custType = custType;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getCarSeries() {
		return carSeries;
	}

	public void setCarSeries(String carSeries) {
		this.carSeries = carSeries;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getParentCustId() {
		return parentCustId;
	}

	public void setParentCustId(int parentCustId) {
		this.parentCustId = parentCustId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double[] getLoc() {
		return loc;
	}

	public void setLoc(double[] loc) {
		this.loc = loc;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String[] getPhoto() {
		return photo;
	}

	public void setPhoto(String[] photo) {
		this.photo = photo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getIdCardType() {
		return idCardType;
	}

	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}

	public String getAnnualInspectDate() {
		return annualInspectDate;
	}

	public void setAnnualInspectDate(String annualInspectDate) {
		this.annualInspectDate = annualInspectDate;
	}

	public String getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
