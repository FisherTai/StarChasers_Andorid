package com.example.starChasers.manager;

import java.sql.Date;

public class WMVO implements java.io.Serializable{
	private String wmno;
	private String wmpsw;
	private String wmname;
	private Date wmobd;
	private Integer wmst;
	private String wmemail;
	private	byte[] wmimg;
	
	
	//建構子不用?
	public WMVO() {
		
	}
	public String getWmno() {
		return wmno;
	}
	public void setWmno(String wmno) {
		this.wmno = wmno;
	}
	public String getWmpsw() {
		return wmpsw;
	}
	public void setWmpsw(String wmpsw) {
		this.wmpsw = wmpsw;
	}
	public String getWmname() {
		return wmname;
	}
	public void setWmname(String wmname) {
		this.wmname = wmname;
	}
	public Date getWmobd() {
		return wmobd;
	}
	public void setWmobd(Date wmobd) {
		this.wmobd = wmobd;
	}
	public Integer getWmst() {
		return wmst;
	}
	public void setWmst(Integer wmst) {
		this.wmst = wmst;
	}
	public String getWmemail() {
		return wmemail;
	}
	public void setWmemail(String wmemail) {
		this.wmemail = wmemail;
	}
	public byte[] getWmimg() {
		return wmimg;
	}
	public void setWmimg(byte[] wmimg) {
		this.wmimg = wmimg;
	}
	
}
