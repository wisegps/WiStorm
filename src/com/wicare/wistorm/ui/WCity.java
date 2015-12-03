package com.wicare.wistorm.ui;

/**
 * @author Wu
 * 城市选择的一个实体类。用来设置 城市名字和拼音
 */
public class WCity {
	
	public String name;//城市的名字
	public String pinyin;//城市的拼音
	
	public WCity() {
		super();
	}

	public WCity(String name, String pinyin) {
		super();
		this.name = name;
		this.pinyin = pinyin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

}
