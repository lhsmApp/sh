package com.fh.service.staffsummy.staffsummy;

import java.util.List;

import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.util.PageData;

/** 
 * 说明： 工资汇总接口
 * 创建人：zhangxiaoliu
 * 创建时间：2017-07-07
 * @version
 */
public interface StaffSummyManager{
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> JqPage(JqPage page)throws Exception;
	
	/**获取记录数量
	 * @param pd
	 * @throws Exception
	 */
	public int countJqGridExtend(JqPage page)throws Exception;
	
	/**获取记录总合计
	 * @param pd
	 * @throws Exception
	 */
	public PageData getFooterSummary(JqPage page)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
}

