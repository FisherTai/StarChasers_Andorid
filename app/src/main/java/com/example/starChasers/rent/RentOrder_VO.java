package com.example.starChasers.rent;

import java.sql.Date;

public class RentOrder_VO implements java.io.Serializable {
	
	private String rd_no;
	private String erg_no;
	private String mem_no;
	private Date rd_startdate;
	private Date rd_enddate;
	private Integer rd_day;
	private Date rd_finaldate;
	private Integer rd_deposit;
	private Integer rd_fine;
	private Integer rd_cps;
	private Integer rd_pay;
	private Integer rd_money;
	private String rd_state;
	private String rd_locate;

	public RentOrder_VO() {
	}

	
	public String getRd_no() {
		return rd_no;
	}

	public void setRd_no(String rd_no) {
		this.rd_no = rd_no;
	}

	public String getErg_no() {
		return erg_no;
	}

	public void setErg_no(String erg_no) {
		this.erg_no = erg_no;
	}

	public String getMem_no() {
		return mem_no;
	}

	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}

	public Date getRd_startdate() {
		return rd_startdate;
	}

	public void setRd_startdate(Date rd_startdate) {
		this.rd_startdate = rd_startdate;
	}

	public Date getRd_enddate() {
		return rd_enddate;
	}

	public void setRd_enddate(Date rd_enddate) {
		this.rd_enddate = rd_enddate;
	}

	public Integer getRd_day() {
		return rd_day;
	}

	public void setRd_day(Integer rd_day) {
		this.rd_day = rd_day;
	}

	public Date getRd_finaldate() {
		return rd_finaldate;
	}

	public void setRd_finaldate(Date rd_finaldate) {
		this.rd_finaldate = rd_finaldate;
	}

	public Integer getRd_deposit() {
		return rd_deposit;
	}

	public void setRd_deposit(Integer rd_deposit) {
		this.rd_deposit = rd_deposit;
	}

	public Integer getRd_fine() {
		return rd_fine;
	}

	public void setRd_fine(Integer rd_fine) {
		this.rd_fine = rd_fine;
	}

	public Integer getRd_cps() {
		return rd_cps;
	}

	public void setRd_cps(Integer rd_cps) {
		this.rd_cps = rd_cps;
	}

	public Integer getRd_pay() {
		return rd_pay;
	}

	public void setRd_pay(Integer rd_pay) {
		this.rd_pay = rd_pay;
	}

	public Integer getRd_money() {
		return rd_money;
	}

	public void setRd_money(Integer rd_money) {
		this.rd_money = rd_money;
	}

	public String getRd_state() {
		return rd_state;
	}

	public void setRd_state(String rd_state) {
		this.rd_state = rd_state;
	}

	public String getRd_locate() {
		return rd_locate;
	}

	public void setRd_locate(String rd_locate) {
		this.rd_locate = rd_locate;
	}
}
