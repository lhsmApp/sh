package com.fh.entity;

import com.fh.util.Const;
import com.fh.util.PageData;
import com.fh.util.Tools;

/**
 * JqGrid分页
* @ClassName: JqPage
* @Description: TODO(这里用一句话描述这个类的作用)
* @author jiachao
* @date 2017年6月6日
*
 */
public class JqPage {
	
	private int rowNum; //每页显示记录数
	private String sord;		//排序方式
	private int page;	//当前页
	private int currentResult;	//当前记录起始索引
	private boolean _search;//是否查询
	private String sidx;//排序字段
	
	private boolean entityOrField;	//true:需要分页的地方，传入的参数就是Page实体；false:需要分页的地方，传入的参数所代表的实体拥有Page属性
	private PageData pd = new PageData();
	
	public JqPage(){
		try {
			if(rowNum==0)
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

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public boolean is_search() {
		return _search;
	}

	public void set_search(boolean _search) {
		this._search = _search;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
	
	public int getCurrentResult() {
		currentResult = (page-1)*rowNum;
		if(currentResult<0)
			currentResult = 0;
		return currentResult;
	}
	
	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}

	public boolean isEntityOrField() {
		return entityOrField;
	}
	
	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
	}
	
	public PageData getPd() {
		return pd;
	}

	public void setPd(PageData pd) {
		this.pd = pd;
	}
	
}
