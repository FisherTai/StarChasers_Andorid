package com.example.starChasers.rent;

public class Rg_CateGorie_VO implements java.io.Serializable {
	private String rgcate_no;
	private String rgcate_name;
	private Integer rg_price;
	private String rg_info;
	private byte[] rg_img;
	private Integer rg_stock;
	private String rg_state;

	public Rg_CateGorie_VO() {
	}

	public String getRgcate_no() {
		return rgcate_no;
	}

	public void setRgcate_no(String rgcate_no) {
		this.rgcate_no = rgcate_no;
	}

	public String getRgcate_name() {
		return rgcate_name;
	}

	public void setRgcate_name(String rgcate_name) {
		this.rgcate_name = rgcate_name;
	}

	public Integer getRg_price() {
		return rg_price;
	}

	public void setRg_price(Integer rg_price) {
		this.rg_price = rg_price;
	}

	public String getRg_info() {
		return rg_info;
	}

	public void setRg_info(String rg_info) {
		this.rg_info = rg_info;
	}

	public byte[] getRg_img() {
		return rg_img;
	}

	public void setRg_img(byte[] rg_img) {
		this.rg_img = rg_img;
	}

	public Integer getRg_stock() {
		return rg_stock;
	}

	public void setRg_stock(Integer rg_stock) {
		this.rg_stock = rg_stock;
	}

	public String getRg_state() {
		return rg_state;
	}

	public void setRg_state(String rg_state) {
		this.rg_state = rg_state;
	}

}
