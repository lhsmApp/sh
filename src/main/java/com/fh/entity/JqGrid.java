package com.fh.entity;

import java.io.Serializable;

/**
 * JqGrid类
* @ClassName: JqGrid
* @Description: TODO(这里用一句话描述这个类的作用)
* @author zhangxiaoliu
* @date 2017年6月12日
*
 */
public class JqGrid implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private int ID;
	private String CATEGORYNAME;
	private String PRODUCTNAME;
	private String COUNTRY;
	private double PRICE;
	private int QUANTITY;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getCATEGORYNAME() {
		return CATEGORYNAME;
	}
	public void setCATEGORYNAME(String cATEGORYNAME) {
		CATEGORYNAME = cATEGORYNAME;
	}
	public String getPRODUCTNAME() {
		return PRODUCTNAME;
	}
	public void setPRODUCTNAME(String pRODUCTNAME) {
		PRODUCTNAME = pRODUCTNAME;
	}
	public String getCOUNTRY() {
		return COUNTRY;
	}
	public void setCOUNTRY(String cOUNTRY) {
		COUNTRY = cOUNTRY;
	}
	public double getPRICE() {
		return PRICE;
	}
	public void setPRICE(double pRICE) {
		PRICE = pRICE;
	}
	public int getQUANTITY() {
		return QUANTITY;
	}
	public void setQUANTITY(int qUANTITY) {
		QUANTITY = qUANTITY;
	}
	
	
}
	