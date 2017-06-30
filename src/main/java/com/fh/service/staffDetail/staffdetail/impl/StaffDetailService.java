package com.fh.service.staffDetail.staffdetail.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.JqGridModel;
import com.fh.entity.JqPage;
import com.fh.entity.StaffDetailModel;
import com.fh.entity.TableColumns;
import com.fh.entity.system.Department;
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
	
	/**获取某表的所有列
	 * 张晓柳
	 * @param 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TableColumns> getTableColumns(PageData pd)throws Exception{
		return (List<TableColumns>)dao.findForList("StaffDetailMapper.getTableColumns", pd);
	}
	
	/**获取数据
	 * 张晓柳
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<StaffDetailModel> findByPd(PageData pd)throws Exception{
		return (List<StaffDetailModel>)dao.findForList("StaffDetailMapper.findByPd", pd);
	}
	
	/**获取数据
	 * 张晓柳
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<StaffDetailModel> findByModel(List<StaffDetailModel> listData)throws Exception{
		return (List<StaffDetailModel>)dao.findForList("StaffDetailMapper.findByModel", listData);
	}
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("StaffDetailMapper.save", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("StaffDetailMapper.edit", pd);
	}
	
	/**导出列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> exportList(JqPage page)throws Exception{
		return (List<PageData>)dao.findForList("StaffDetailMapper.exportList", page);
	}
	/**导出模板
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> exportModel(String DepartCode)throws Exception{
		return (List<PageData>)dao.findForList("StaffDetailMapper.exportModel", DepartCode);
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
		dao.batchDelete("StaffDetailMapper.deleteAll", listData);
	}
	
	/**批量修改
	 * @param pd
	 * @throws Exception
	 */
	public void updateAll(List<StaffDetailModel> listData)throws Exception{
		dao.batchUpdate("StaffDetailMapper.updateAll", listData);
	}
	
	/**导入
	 * @param pd
	 * @throws Exception
	 */
	public void batchImport(List<StaffDetailModel> listData)throws Exception{
		dao.batchImport("StaffDetailMapper.importDelete", "StaffDetailMapper.importInsert", listData);
	}

	/**获取员工编码
	 * @param 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> getHaveUserCodeDic(PageData pd)throws Exception{
		return (List<String>)dao.findForList("StaffDetailMapper.getHaveUserCodeDic", pd);
	}
	
}

