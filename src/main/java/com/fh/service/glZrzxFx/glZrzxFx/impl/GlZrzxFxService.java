package com.fh.service.glZrzxFx.glZrzxFx.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.glZrzxFx.glZrzxFx.GlZrzxFxManager;

/** 
 * 说明： 
 * 创建人：zhangxiaoliu
 * 创建时间：2017-09-14
 * @version
 */
@Service("glZrzxFxService")
public class GlZrzxFxService implements GlZrzxFxManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> JqPage(JqPage page)throws Exception{
		return (List<PageData>)dao.findForList("GlZrzxFxMapper.datalistPage", page);
	}
	/**获取记录数量
	 * @param pd
	 * @throws Exception
	 */
	public int countJqGridExtend(JqPage page)throws Exception{
		return (int)dao.findForObject("GlZrzxFxMapper.countJqGridExtend", page);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("GlZrzxFxMapper.findById", pd);
	}
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(List<PageData> listData)throws Exception{
		dao.batchDeleteOneUpdate("GlZrzxFxMapper.delete", "GlZrzxFxMapper.save", listData);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll()throws Exception{
		return (List<PageData>)dao.findForList("GlZrzxFxMapper.listAll", null);
	}
	
}

