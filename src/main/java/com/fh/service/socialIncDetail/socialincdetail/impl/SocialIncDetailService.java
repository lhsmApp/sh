package com.fh.service.socialIncDetail.socialincdetail.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.JqPage;
import com.fh.entity.SocialIncDetailModel;
import com.fh.entity.TableColumns;
import com.fh.util.PageData;
import com.fh.service.socialIncDetail.socialincdetail.SocialIncDetailManager;

/** 
 * 说明： 社保明细
 * 创建人：zhangxiaoliu
 * 创建时间：2017-06-30
 * @version
 */
@Service("socialincdetailService")
public class SocialIncDetailService implements SocialIncDetailManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**获取数据
	 * 张晓柳
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SocialIncDetailModel> findByPd(PageData pd)throws Exception{
		return (List<SocialIncDetailModel>)dao.findForList("SocialIncDetailMapper.findByPd", pd);
	}
	
	/**获取数据
	 * 张晓柳
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SocialIncDetailModel> findByModel(List<SocialIncDetailModel> listData)throws Exception{
		return (List<SocialIncDetailModel>)dao.findForList("SocialIncDetailMapper.findByModel", listData);
	}
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("SocialIncDetailMapper.save", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("SocialIncDetailMapper.edit", pd);
	}
	
	/**导出列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> exportList(JqPage page)throws Exception{
		return (List<PageData>)dao.findForList("SocialIncDetailMapper.exportList", page);
	}
	/**导出模板
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> exportModel(String DepartCode)throws Exception{
		return (List<PageData>)dao.findForList("SocialIncDetailMapper.exportModel", DepartCode);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> JqPage(JqPage page)throws Exception{
		return (List<PageData>)dao.findForList("SocialIncDetailMapper.datalistJqPage", page);
	}
	
	/**获取记录数量
	 * @param pd
	 * @throws Exception
	 */
	public int countJqGridExtend(JqPage page)throws Exception{
		return (int)dao.findForObject("SocialIncDetailMapper.countJqGridExtend", page);
	}
	
	/**获取记录总合计
	 * @param pd
	 * @throws Exception
	 */
	public PageData getFooterSummary(JqPage page)throws Exception{
		return (PageData)dao.findForObject("SocialIncDetailMapper.getFooterSummary", page);
	}
	
	/**批量删除
	 * @param 
	 * @throws Exception
	 */
	public void deleteAll(List<SocialIncDetailModel> listData)throws Exception{
		dao.batchDelete("SocialIncDetailMapper.deleteAll", listData);
	}
	
	/**批量修改
	 * @param pd
	 * @throws Exception
	 */
	public void updateAll(List<SocialIncDetailModel> listData)throws Exception{
		dao.batchUpdate("SocialIncDetailMapper.updateAll", listData);
	}
	
	/**导入
	 * @param pd
	 * @throws Exception
	 */
	public void batchImport(List<SocialIncDetailModel> listData)throws Exception{
		dao.batchImport("SocialIncDetailMapper.importDelete", "SocialIncDetailMapper.importInsert", listData);
	}

	/**获取员工编码
	 * @param 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> getHaveUserCodeDic(PageData pd)throws Exception{
		return (List<String>)dao.findForList("SocialIncDetailMapper.getHaveUserCodeDic", pd);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	/**获取汇总里的明细
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> getDetailList(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("SocialIncDetailMapper.getDetailList", pd);
	}
	
	/**获取汇总数据
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> getSum(Map<String, String> map)throws Exception{
		return (List<PageData>)dao.findForList("SocialIncDetailMapper.getSum", map);
	}
}

