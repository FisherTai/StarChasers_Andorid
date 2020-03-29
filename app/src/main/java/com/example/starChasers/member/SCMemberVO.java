package com.example.starChasers.member;

import java.sql.Date;

public class SCMemberVO implements java.io.Serializable {

	private String mem_No;
	private String mem_Name;
	private String mem_Psw;
	private String mem_Phone;
	private Integer mem_Sex;
	private String mem_Email;
	private byte[] mem_Img;
	private Integer mem_Points;
	private Date mem_Birthday;
	private Integer mem_State;

	public SCMemberVO() {

	}

	public SCMemberVO(String mem_No, String mem_Name, String mem_Psw, String mem_Phone, Integer mem_Sex,
			String mem_Email, byte[] mem_Img, Integer mem_Points, Date mem_Birthday, Integer mem_State) {
		super();
		this.mem_No = mem_No;
		this.mem_Name = mem_Name;
		this.mem_Psw = mem_Psw;
		this.mem_Phone = mem_Phone;
		this.mem_Sex = mem_Sex;
		this.mem_Email = mem_Email;
		this.mem_Img = mem_Img;
		this.mem_Points = mem_Points;
		this.mem_Birthday = mem_Birthday;
		this.mem_State = mem_State;
	}

	public String getMem_No() {
		return mem_No;
	}

	public void setMem_No(String mem_No) {
		this.mem_No = mem_No;
	}

	public String getMem_Name() {
		return mem_Name;
	}

	public void setMem_Name(String mem_Name) {
		this.mem_Name = mem_Name;
	}

	public String getMem_Psw() {
		return mem_Psw;
	}

	public void setMem_Psw(String mem_Psw) {
		this.mem_Psw = mem_Psw;
	}

	public String getMem_Phone() {
		return mem_Phone;
	}

	public void setMem_Phone(String mem_Phone) {
		this.mem_Phone = mem_Phone;
	}

	public Integer getMem_Sex() {
		return mem_Sex;
	}

	public void setMem_Sex(Integer mem_Sex) {
		this.mem_Sex = mem_Sex;
	}

	public String getMem_Email() {
		return mem_Email;
	}

	public void setMem_Email(String mem_Email) {
		this.mem_Email = mem_Email;
	}

	public byte[] getMem_Img() {
		return mem_Img;
	}

	public void setMem_Img(byte[] mem_Img) {
		this.mem_Img = mem_Img;
	}

	public Integer getMem_Points() {
		return mem_Points;
	}

	public void setMem_Points(Integer mem_Points) {
		this.mem_Points = mem_Points;
	}

	public Date getMem_Birthday() {
		return mem_Birthday;
	}

	public void setMem_Birthday(Date mem_Birthday) {
		this.mem_Birthday = mem_Birthday;
	}

	public Integer getMem_State() {
		return mem_State;
	}

	public void setMem_State(Integer mem_State) {
		this.mem_State = mem_State;
	}

}
