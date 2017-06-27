package com.fh.service.sysSealedInfo.syssealedinfo;

import java.util.List;

import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.util.PageData;

/** 
 * 说明： 业务封存信息接口
 * 创建人：FH Q313596790
 * 创建时间：2017-06-16
 * @version
 */
public interface SysSealedInfoManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(JqPage page)throws Exception;
	
	/**获取记录数量
	 * @param pd
	 * @throws Exception
	 */
	public int count(PageData pd)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**
	 * 单位列表
	 * @return
	 * @throws Exception
	 */
	public List<String> deptList() throws Exception;
	
	/**
	 * 获取状态 
	 * @return
	 * @throws Exception
	 */
	public String getState(PageData pd) throws Exception;
	
}

