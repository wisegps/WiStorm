package com.wicare.wistorm.model;

/**
 * @author Wu
 * 
 * 汽车品牌数据
 */
public class CarBrandData {

	private String brand; //汽车品牌
	private String letter;//汽车品牌的字母
	private String id;//id号
	private String logoUrl;//图片的url
	
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
}
