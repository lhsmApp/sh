package com.fh.entity;


import java.util.List;

import com.fh.util.Const;
import com.fh.util.Tools;

/**
 * 主要用于DataGrid的固定数据格式绑定（total,rows），包括分页和数据结合
* @ClassName: PageResult
* @Description: TODO(这里用一句话描述这个类的作用)
* @author jiachao
* @date 2017年6月6日
*
* @param <T>
 */
public class PageResult<T> extends CommonBase{
	private int rowNum; //每页显示记录数
	private int total;
	private int records;
	private int page;
	private List<T> rows;
	
	public PageResult(){
		try {
			this.rowNum = Integer.parseInt(Tools.readTxtFile(Const.PAGE));
		} catch (Exception e) {
			this.rowNum = 15;
		}
	}
	
	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getRecords() {
		return records;
	}
	public void setRecords(int records) {
		this.records = records;
		if(records%rowNum==0)
			total = records/rowNum;
		else
			total = records/rowNum+1;
	}
	public int getPage() {
		/*if(page<=0)
			page = 1;
		if(page>getTotal())
			page = getTotal();*/
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
