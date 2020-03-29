package com.example.starChasers.tourgroup;

import java.sql.Date;


public class TsuVO implements java.io.Serializable{
	private String tg_no	;
	private String mem_no	;
	private Date tg_sn_date;
	private String tg_sn_state;
	
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
	public Date getTg_sn_date() {
		return tg_sn_date;
	}
	public void setTg_sn_date(Date tg_sn_date) {
		this.tg_sn_date = tg_sn_date;
	}
	public String getTg_sn_state() {
		return tg_sn_state;
	}
	public void setTg_sn_state(String tg_sn_state) {
		this.tg_sn_state = tg_sn_state;
	}


}
