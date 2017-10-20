package com.fh.service.housefundsummy.housefundsummy;

import java.util.List;
import java.util.Map;

import com.fh.entity.JqPage;
import com.fh.entity.SysSealed;
import com.fh.util.PageData;

/** 
 * 说明： 公积金汇总接口
 * 创建人：zhangxiaoliu
 * 创建时间：2017-07-07
 * @version
 */ 
public interface HouseFundSummyManager{
	
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


	/**获取汇总数据
	 * @param
	 * @throws Exception
	 */
	public List<PageData> getHave(Map<String, String> map)throws Exception;

	/**获取记录总合计
	 * @param pd
	 * @throws Exception
	 */
	public void saveSummyModelList(List<Map<String, Object>> listMap, PageData pdBillNum, List<SysSealed> delReportList)throws Exception;
	
	
}

