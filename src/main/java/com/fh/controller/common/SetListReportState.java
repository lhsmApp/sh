package com.fh.controller.common;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fh.service.sysSealedInfo.syssealedinfo.impl.SysSealedInfoService;
import com.fh.util.PageData;
import com.fh.util.enums.DurState;

/**
 * 
 * 
 * @ClassName: SqlInString
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 张晓柳
 * @date 2017年8月18日
 *
 */
public class SetListReportState {
	
	public static List<PageData> SetListReportList(List<PageData> varList, 
			SysSealedInfoService syssealedinfoService,
			Map<String, String> AdditionalColumnsMap, String strAddFeild,
			String SystemDateTime, String SelectedDepartCode, String SelectedCustCol7,
			String TypeCodeSummy) throws Exception{
		//获取封存状态，用于页面状态
		//另加的列、配置模板之外的列
	    Set<String> AdditionalColumnsSet = AdditionalColumnsMap.keySet();
	    List<String> additionalColumnsList = new ArrayList<String>();
	    additionalColumnsList.addAll(AdditionalColumnsSet);
		String strGetDeptOff = " and RPT_DUR = '" + SystemDateTime + "' ";
		strGetDeptOff += " and BILL_TYPE = '" + TypeCodeSummy + "' ";
		String strInSelectedCustCol7 = QueryFeildString.getSqlInString(SelectedCustCol7);
		if(strInSelectedCustCol7!=null && !strInSelectedCustCol7.equals("")){
			strGetDeptOff += " and BILL_OFF in (" + strInSelectedCustCol7 + ") ";
		}
		String strInSelectedDepartCode = QueryFeildString.getSqlInString(SelectedDepartCode);
		if(strInSelectedDepartCode!=null && !strInSelectedDepartCode.equals("")){
			strGetDeptOff += " and RPT_DEPT in (" + strInSelectedDepartCode + ") ";
		}
		List<PageData> listReport = syssealedinfoService.getDeptOffList(strGetDeptOff);
		if(varList != null && varList.size() > 0){
			for(PageData eachList : varList){
				eachList.put("ReportState", DurState.Release.getNameKey());
				for(PageData eachReport : listReport){
					if(eachList.get("DEPT_CODE").equals(eachReport.get("RPT_DEPT"))
							&& eachList.get("CUST_COL7").equals(eachReport.get("BILL_OFF"))
							&& eachList.get("BUSI_DATE").equals(eachReport.get("RPT_DUR"))){
						eachList.put(strAddFeild, eachReport.get(AdditionalColumnsMap.get(strAddFeild)));
					}
				}
				for(DurState dur : DurState.values()){
					if(dur.getNameKey().equals(eachList.get("ReportState"))){
						eachList.put("ReportState", dur.getNameValue());
					}
				}
			}
		}
		return varList;
	}
}
