package com.fh.service.staffDetail.staffdetail;

import java.util.List;

import com.fh.entity.JqGridModel;
import com.fh.entity.JqPage;
import com.fh.entity.StaffDetailModel;
import com.fh.entity.TableColumns;
import com.fh.entity.system.Department;
import com.fh.entity.system.Dictionaries;
import com.fh.entity.system.User;
import com.fh.util.PageData;

/** 
 * 说明： 工资明细
 * 创建人：zhangxiaoliu
 * 创建时间：2017-06-19
 * @version
 */
public interface StaffDetailManager{
	
	/**获取某表的所有列
	 * 张晓柳
	 * @param 
	 * @throws Exception
	 */
	public List<TableColumns> getTableColumns(PageData pd)throws Exception;
	
	/**获取数据
	 * 张晓柳
	 * @param pd
	 * @throws Exception
	 */
	public List<StaffDetailModel> findByPd(PageData pd)throws Exception;
	
	/**获取数据
	 * 张晓柳
	 * @param pd
	 * @throws Exception
	 */
	public List<StaffDetailModel> findByModel(List<StaffDetailModel> listData)throws Exception;

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**导出列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> exportList(JqPage page)throws Exception;
	/**导出模板
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> exportModel(String DepartCode)throws Exception;
	
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
	
	/**批量删除
	 * @param 
	 * @throws Exception
	 */
	public void deleteAll(List<StaffDetailModel> listData)throws Exception;
	
	/**批量修改
	 * @param pd
	 * @throws Exception
	 */
	public void updateAll(List<StaffDetailModel> listData)throws Exception;
	
	/**导入
	 * @param pd
	 * @throws Exception
	 */
	public void batchImport(List<StaffDetailModel> listData)throws Exception;

	/**获取员工编码
	 * 张晓柳
	 * @param 
	 * @throws Exception
	 */
	public List<String> getHaveUserCodeDic(PageData pd)throws Exception;
}

