package com.fh.service.staffsummy.staffsummy.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.staffsummy.staffsummy.StaffSummyManager;

/** 
 * 说明： 工资汇总
 * 创建人：zhangxiaoliu
 * 创建时间：2017-07-07
 * @version
 */
@Service("staffsummyService")
public class StaffSummyService implements StaffSummyManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> JqPage(JqPage page)throws Exception{
		return (List<PageData>)dao.findForList("StaffSummyMapper.datalistJqPage", page);
	}
	
	/**获取记录数量
	 * @param pd
	 * @throws Exception
	 */
	public int countJqGridExtend(JqPage page)throws Exception{
		return (int)dao.findForObject("StaffSummyMapper.countJqGridExtend", page);
	}
	
	/**获取记录总合计
	 * @param pd
	 * @throws Exception
	 */
	public PageData getFooterSummary(JqPage page)throws Exception{
		return (PageData)dao.findForObject("StaffSummyMapper.getFooterSummary", page);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("StaffSummyMapper.findById", pd);
	}
	
}

