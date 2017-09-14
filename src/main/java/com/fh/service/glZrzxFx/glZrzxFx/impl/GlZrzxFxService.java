package com.fh.service.glZrzxFx.glZrzxFx.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
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
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("GlZrzxFxMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("GlZrzxFxMapper.delete", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("GlZrzxFxMapper.datalistPage", page);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("GlZrzxFxMapper.findById", pd);
	}
	
}

