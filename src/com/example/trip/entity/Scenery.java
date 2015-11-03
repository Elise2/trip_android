package com.example.trip.entity;

import java.io.Serializable;

import android.R.integer;

import com.baidu.location.i;

public class Scenery implements Serializable {
	private String title;
	private int cityId;
	private String img;
	private String content;
	private int typeId;
	private int talkNum;
	private String sceneryType;
	private String recommendTitle;
	private String recommendContent;
	private String username;
	// 增加userImg
	private String userImg;
	private String hotelPrice;
	
	private String obstract;
	private String scenery_title;
	private String lng;
	private String lat;
	private int isTop;
	private String strategy_descript;//景点攻略下的描述
	private String scenery_cost;//门票
	
	public String getStrategy_descript() {
		return strategy_descript;
	}

	public void setStrategy_descript(String strategy_descript) {
		this.strategy_descript = strategy_descript;
	}

	public String getScenery_cost() {
		return scenery_cost;
	}

	public void setScenery_cost(String scenery_cost) {
		this.scenery_cost = scenery_cost;
	}

	public String getTitle_comment() {
		return title_comment;
	}

	public void setTitle_comment(String title_comment) {
		this.title_comment = title_comment;
	}

	public String getContact_tel() {
		return contact_tel;
	}

	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}

	private String open_time;//开放时间
	private String title_comment;//典型评论
	private String contact_tel;//联系电话
	

	private int scenery_id;

	public int getScenery_id() {
		return scenery_id;
	}

	public void setScenery_id(int scenery_id) {
		this.scenery_id = scenery_id;
	}

	public int getIsTop() {
		return isTop;
	}

	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}


	public String getScenery_title() {
		return scenery_title;
	}

	public void setScenery_title(String scenery_title) {
		this.scenery_title = scenery_title;
	}

	public String getOpen_time() {
		return open_time;
	}

	public void setOpen_time(String open_time) {
		this.open_time = open_time;
	}

	public String getObstract() {
		return obstract;
	}

	public void setObstract(String obstract) {
		this.obstract = obstract;
	}

	public String getHotelPrice() {
		return hotelPrice;
	}

	public void setHotelPrice(String hotelPrice) {
		this.hotelPrice = hotelPrice;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getTalkNum() {
		return talkNum;
	}

	public void setTalkNum(int talkNum) {
		this.talkNum = talkNum;
	}

	public String getSceneryType() {
		return sceneryType;
	}

	public void setSceneryType(String sceneryType) {
		this.sceneryType = sceneryType;
	}

	public String getRecommendTitle() {
		return recommendTitle;
	}

	public void setRecommendTitle(String recommendTitle) {
		this.recommendTitle = recommendTitle;
	}

	public String getRecommendContent() {
		return recommendContent;
	}

	public void setRecommendContent(String recommendContent) {
		this.recommendContent = recommendContent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

}
