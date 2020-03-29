package com.example.starChasers.tourgroup;

import java.sql.Date;

public class TgVO implements java.io.Serializable {
	private String tg_no;
	private String mem_no;
	private String tg_name;
	private Date tg_activitydate;
	private Date tg_creatdate;
	private Date tg_signupend;
	private String tg_content;
	private Integer tg_numlimit;
	private Integer tg_num;
	private String tg_condition;
	private Integer tg_state;
//	private Integer tg_day;
	private String tg_address;
	private byte[] tg_img;
	private Date tg_enddate;

	public Date getTg_enddate() {
		return tg_enddate;
	}

	public void setTg_enddate(Date tg_enddate) {
		this.tg_enddate = tg_enddate;
	}


	public String getTg_no() {
		return tg_no;
	}

	public void setTg_no(String tg_no) {
		this.tg_no = tg_no;
	}

	public String getMem_no() {
		return mem_no;
	}

	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}

	public String getTg_name() {
		return tg_name;
	}

	public void setTg_name(String tg_name) {
		this.tg_name = tg_name;
	}

	public Date getTg_activitydate() {
		return tg_activitydate;
	}

	public void setTg_activitydate(Date tg_activitydate) {
		this.tg_activitydate = tg_activitydate;
	}

	public Date getTg_creatdate() {
		return tg_creatdate;
	}

	public void setTg_creatdate(Date tg_creatdate) {
		this.tg_creatdate = tg_creatdate;
	}

	public Date getTg_signupend() {
		return tg_signupend;
	}

	public void setTg_signupend(Date tg_signupend) {
		this.tg_signupend = tg_signupend;
	}

	public String getTg_content() {
		return tg_content;
	}

	public void setTg_content(String tg_content) {
		this.tg_content = tg_content;
	}

	public Integer getTg_numlimit() {
		return tg_numlimit;
	}

	public void setTg_numlimit(Integer tg_numlimit) {
		this.tg_numlimit = tg_numlimit;
	}

	public Integer getTg_num() {
		return tg_num;
	}

	public void setTg_num(Integer tg_num) {
		this.tg_num = tg_num;
	}

	public String getTg_condition() {
		return tg_condition;
	}

	public void setTg_condition(String tg_condition) {
		this.tg_condition = tg_condition;
	}

	public Integer getTg_state() {
		return tg_state;
	}

	public void setTg_state(Integer tg_state) {
		this.tg_state = tg_state;
	}

//	public Integer getTg_day() {
//		return tg_day;
//	}
//
//	public void setTg_day(Integer tg_day) {
//		this.tg_day = tg_day;
//	}

	public String getTg_address() {
		return tg_address;
	}

	public void setTg_address(String tg_address) {
		this.tg_address = tg_address;
	}

	public byte[] getTg_img() {
		return tg_img;
	}

	public void setTg_img(byte[] tg_img) {
		this.tg_img = tg_img;
	}

}
