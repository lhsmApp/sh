package com.fh.service.glzrzx.glzrzx.impl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.ZrzxModel;
import com.fh.service.glzrzx.glzrzx.GlZrzxManager;

/** 
 * 说明： 责任中心
 * 创建人：zhangxiaoliu
 * 创建时间：2017-07-18
 * @version
 */
@Service("glzrzxService")
public class GlZrzxService implements GlZrzxManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**获取数据
	 * 张晓柳
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ZrzxModel> findDeptFromZrzx(String Dept)throws Exception{
		return (List<ZrzxModel>)dao.findForList("GlZrzxMapper.findDeptFromZrzx", Dept);
	}
}

