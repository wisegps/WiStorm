package com.wicare.wistorm.ui;

public class WCity {
	
	public String name;
	public String pinyin;
	
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
