package com.example.starChasers.post;

import java.sql.Date;

public class PostsVO implements java.io.Serializable {
	
	private String po_no;
	private String mem_no;
	private String po_name;
	private Date  po_crdate;
	private Date po_editdate;
	private String po_content;
	private byte[] po_photo;
	private byte[] po_film;
	private String po_status;
	private int po_viewcount;
	private int po_good;
	
	public String getPo_no() {
		return po_no;
	}
	public void setPo_no(String po_no) {
		this.po_no = po_no;
	}
	public String getMem_no() {
		return mem_no;
	}
	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}
	public String getPo_name() {
		return po_name;
	}
	public void setPo_name(String po_name) {
		this.po_name = po_name;
	}
	public Date getPo_crdate() {
		return po_crdate;
	}
	public void setPo_crdate(Date po_crdate) {
		this.po_crdate = po_crdate;
	}
	public Date getPo_editdate() {
		return po_editdate;
	}
	public void setPo_editdate(Date po_editdate) {
		this.po_editdate = po_editdate;
	}
	public String getPo_content() {
		return po_content;
	}
	public void setPo_content(String po_content) {
		this.po_content = po_content;
	}
	public byte[] getPo_photo() {
		return po_photo;
	}
	public void setPo_photo(byte[] po_photo) {
		this.po_photo = po_photo;
	}
	public byte[] getPo_film() {
		return po_film;
	}
	public void setPo_film(byte[] po_film) {
		this.po_film = po_film;
	}
	public String getPo_status() {
		return po_status;
	}
	public void setPo_status(String po_status) {
		this.po_status = po_status;
	}
	public int getPo_viewcount() {
		return po_viewcount;
	}
	public void setPo_viewcount(int po_viewcount) {
		this.po_viewcount = po_viewcount;
	}
	public int getPo_good() {
		return po_good;
	}
	public void setPo_good(int po_good) {
		this.po_good = po_good;
	}
	


}
