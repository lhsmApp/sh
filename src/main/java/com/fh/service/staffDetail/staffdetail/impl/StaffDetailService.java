package com.fh.service.staffDetail.staffdetail.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.JqGridModel;
import com.fh.entity.JqPage;
import com.fh.entity.StaffDetailModel;
import com.fh.entity.TableColumns;
import com.fh.entity.system.Dictionaries;
import com.fh.entity.system.User;
import com.fh.util.PageData;
import com.fh.service.staffDetail.staffdetail.StaffDetailManager;

/** 
 * 说明： 工资明细
 * 创建人：zhangxiaoliu
 * 创建时间：2017-06-19
 * @version
 */
@Service("staffdetailService")
public class StaffDetailService implements StaffDetailManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**获取系统期间
	 * @param pd
	 * @throws Exception
	 */
	public String currentSection(PageData pd)throws Exception{
		return (String) dao.findForObject("StaffDetailMapper.currentSection", pd);
	}
	
	/**获取某表的所有列
	 * @param 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TableColumns> getTableColumns(PageData pd)throws Exception{
		return (List<TableColumns>)dao.findForList("StaffDetailMapper.getTableColumns", pd);
	}
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("StaffDetailMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("StaffDetailMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("StaffDetailMapper.edit", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("StaffDetailMapper.findById", pd);
	}
	
	/**导出列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> exportList(JqPage page)throws Exception{
		return (List<PageData>)dao.findForList("StaffDetailMapper.exportList", page);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> JqPage(JqPage page)throws Exception{
		return (List<PageData>)dao.findForList("StaffDetailMapper.datalistJqPage", page);
	}
	
	/**获取记录数量
	 * @param pd
	 * @throws Exception
	 */
	public int countJqGridExtend(JqPage page)throws Exception{
		return (int)dao.findForObject("StaffDetailMapper.countJqGridExtend", page);
	}
	
	/**获取记录总合计
	 * @param pd
	 * @throws Exception
	 */
	public PageData getFooterSummary(JqPage page)throws Exception{
		return (PageData)dao.findForObject("StaffDetailMapper.getFooterSummary", page);
	}
	
	/**批量删除
	 * @param 
	 * @throws Exception
	 */
	public void deleteAll(List<StaffDetailModel> listData)throws Exception{
		dao.delete("StaffDetailMapper.deleteAll", listData);
	}
	
	/**批量修改
	 * @param pd
	 * @throws Exception
	 */
	public void updateAll(List<StaffDetailModel> listData)throws Exception{
		dao.update("StaffDetailMapper.updateAll", listData);
	}
	
	/**导入
	 * @param pd
	 * @throws Exception
	 */
	public void batchImport(List<StaffDetailModel> listData)throws Exception{
		dao.update("StaffDetailMapper.batchImport", listData);
	}
	
	/**获取字典翻译类型
	 * @param 
	 * @throws Exception
	 */
	public String getDicType(String dicName)throws Exception{
		return (String)dao.findForObject("StaffDetailMapper.getDicType", dicName);
	}
	
	/**获取SysDictionaries字典
	 * @param 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Dictionaries> getSysDictionaries(String dicName)throws Exception{
		return (List<Dictionaries>)dao.findForList("StaffDetailMapper.getSysDictionaries", dicName);
	}
	/**获取表字典
	 * @param 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Dictionaries> getTableDic(PageData pd)throws Exception{
		return (List<Dictionaries>)dao.findForList("StaffDetailMapper.getTableDic", pd);
	}
	
	/**获取单位人员
	 * @param 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUsersInDepart(String department)throws Exception{
		return (List<User>)dao.findForList("StaffDetailMapper.getUsersInDepart", department);
	}
	
}

