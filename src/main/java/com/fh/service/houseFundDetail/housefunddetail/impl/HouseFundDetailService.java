package com.fh.service.houseFundDetail.housefunddetail.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.JqPage;
import com.fh.util.PageData;
import com.fh.service.houseFundDetail.housefunddetail.HouseFundDetailManager;

/** 
 * 说明： 公积金明细
 * 创建人：zhangxiaoliu
 * 创建时间：2017-06-30
 * @version
 */
@Service("housefunddetailService")
public class HouseFundDetailService implements HouseFundDetailManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	
	/**获取数据
	 * 张晓柳
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> findByModel(List<PageData> listData)throws Exception{
		return (List<PageData>)dao.findForList("HouseFundDetailMapper.findByModel", listData);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> JqPage(JqPage page)throws Exception{
		return (List<PageData>)dao.findForList("HouseFundDetailMapper.datalistJqPage", page);
	}
	/**获取记录数量
	 * @param pd
	 * @throws Exception
	 */
	public int countJqGridExtend(JqPage page)throws Exception{
		return (int)dao.findForObject("HouseFundDetailMapper.countJqGridExtend", page);
	}
	/**获取记录总合计
	 * @param pd
	 * @throws Exception
	 */
	public PageData getFooterSummary(JqPage page)throws Exception{
		return (PageData)dao.findForObject("HouseFundDetailMapper.getFooterSummary", page);
	}

	/**批量删除
	 * @param 
	 * @throws Exception
	 */
	public void deleteAll(List<PageData> listData)throws Exception{
		dao.delete("HouseFundDetailMapper.deleteAll", listData);
	}
	
	/**批量修改
	 * @param pd
	 * @throws Exception
	 */
	public void deleteUpdateAll(List<PageData> listData)throws Exception{
		dao.batchDeleteUpdate("HouseFundDetailMapper.deleteAll", "HouseFundDetailMapper.save", listData);
	}
	
	/**导入
	 * @param pd
	 * @throws Exception
	 */
	public void batchImport(List<PageData> listData)throws Exception{
		dao.batchImport("HouseFundDetailMapper.importDelete", "HouseFundDetailMapper.save", listData);
	}
	
	/**导出列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> exportList(JqPage page)throws Exception{
		return (List<PageData>)dao.findForList("HouseFundDetailMapper.exportList", page);
	}
	/**导出模板
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> exportModel(String DepartCode)throws Exception{
		return (List<PageData>)dao.findForList("HouseFundDetailMapper.exportModel", DepartCode);
	}

	/**获取员工编码 相当于字典
	 * @param 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> getHaveUserCodeDic(PageData pd)throws Exception{
		return (List<String>)dao.findForList("HouseFundDetailMapper.getHaveUserCodeDic", pd);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	/**获取汇总里的明细
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> getDetailList(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("HouseFundDetailMapper.getDetailList", pd);
	}
	
	/**获取汇总数据
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> getSum(Map<String, String> map)throws Exception{
		return (List<PageData>)dao.findForList("HouseFundDetailMapper.getSum", map);
	}
}

