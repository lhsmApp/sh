package com.fh.controller.common;

import java.util.List;
import java.util.Map;

import com.fh.entity.CommonBase;
import com.fh.entity.TableColumns;
import com.fh.service.importdetail.importdetail.impl.ImportDetailService;
import com.fh.service.sysSealedInfo.syssealedinfo.impl.SysSealedInfoService;
import com.fh.util.PageData;
import com.fh.util.enums.BillState;

/**
 * 单号
* @ClassName: FilterBillCode
* @Description: TODO(这里用一句话描述这个类的作用)
* @author 张晓柳
* @date 2017年8月21日
*
 */
public class FilterBillCode {
	
	
	
	//复制插入数据：在接口已上报过（接口有记录），汇总已上报过（汇总有记录）时执行复制插入，并删掉汇总上报记录
	public static CommonBase copyInsert(SysSealedInfoService syssealedinfoService, ImportDetailService importdetailService, 
			String DepartCode, String SystemDateTime, String CUST_COL7,
			String TypeCodeListen, String TypeCodeSummy, String TableNameSummy, String TableNameDetail,
			String emplGroupType,
			Map<String, TableColumns> map_HaveColumnsList) throws Exception{
		//, Boolean report
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		
		String strHelpful = "";
		PageData pdGetState = new PageData();
		pdGetState.put("RPT_DEPT", DepartCode);
		pdGetState.put("RPT_DUR", SystemDateTime);
		pdGetState.put("BILL_TYPE", TypeCodeListen);
		pdGetState.put("BILL_OFF", CUST_COL7);
		String stateListen = syssealedinfoService.getState(pdGetState);
		if(stateListen != null && !stateListen.equals("")){
			//重新汇总接口上报记录删掉，明细有改动汇总上报记录删掉
			//接口已上报过（接口有记录），汇总没上报过（汇总没记录），取未在汇总表中出现单号的记录
			pdGetState.put("BILL_TYPE", TypeCodeSummy);// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
			String stateSummy = syssealedinfoService.getState(pdGetState);
			if(stateSummy != null && !stateSummy.equals("")){
				//汇总已上报过（汇总有记录），取汇总单据状态不为0的
				strHelpful += FilterBillCode.getBillCodeNotInSumInvalid(TableNameSummy);
				
				PageData pdGetList = new PageData();
				pdGetList.put("TableName", TableNameDetail);
				pdGetList.put("SystemDateTime", SystemDateTime);
				pdGetList.put("DepartCode", DepartCode);
				pdGetList.put("BILL_OFF", CUST_COL7);
				pdGetList.put("USER_GROP", emplGroupType);
				pdGetList.put("QueryFeild", strHelpful);
				List<PageData> getList = importdetailService.getCopyInsertList(pdGetList);
				if(!(getList != null && getList.size() > 0)){
					commonBase.setCode(2);
					commonBase.setMessage("没有可操作的数据！");
				} else {
					for(PageData each : getList){
						each.put("TableNameDetail", TableNameDetail);
						each.put("BILL_CODE", " ");
						TmplUtil.setModelDefault(each, map_HaveColumnsList);

						each.put("TypeCodeSummy", TypeCodeSummy);
					}
					importdetailService.insertCopy(getList);
				}
			}
		}
		commonBase.setCode(0);
		return commonBase;
	}
	
	
	//导入界面的显示数据
	public static String getCanOperateCondition(SysSealedInfoService syssealedinfoService, 
			String DepartCode, String SystemDateTime, String CUST_COL7,
			String TypeCodeListen, String TypeCodeSummy, String TableNameSummy) throws Exception{
		String strHelpful = "";
		PageData pd = new PageData();
		pd.put("RPT_DEPT", DepartCode);
		pd.put("RPT_DUR", SystemDateTime);
		pd.put("BILL_TYPE", TypeCodeListen);
		pd.put("BILL_OFF", CUST_COL7);
		String stateListen = syssealedinfoService.getState(pd);
		if(!(stateListen != null && !stateListen.equals(""))){
			//接口没上报过（接口没记录），取汇总单据状态不为0的
			strHelpful += FilterBillCode.getBillCodeNotInSumInvalid(TableNameSummy);
		} else {
			//重新汇总接口上报记录删掉，明细有改动汇总上报记录删掉
			//接口已上报过（接口有记录），汇总没上报过（汇总没记录），取未在汇总表中出现单号的记录
			pd.put("BILL_TYPE", TypeCodeSummy);
			String stateSummy = syssealedinfoService.getState(pd);
			if(!(stateSummy != null && !stateSummy.equals(""))){
				//汇总没上报过（汇总没记录），取汇总单据中不存在的单号
				strHelpful += FilterBillCode.getBillCodeNotInSummy(TableNameSummy);
			} else {
				//汇总已上报过（汇总有记录），取汇总单据状态不为0的
				strHelpful += FilterBillCode.getBillCodeNotInSumInvalid(TableNameSummy);
			}
		}
		return strHelpful;
	}
	
	
	
    //汇总单据状态不为0，就是没汇总或汇总但没作废
	public static String getBillCodeNotInSumInvalid(String tableNameSummy){
		String strReturn = " and BILL_CODE not in (select BILL_CODE from " + tableNameSummy + " where BILL_STATE = " + BillState.Invalid.getNameKey() + ") ";
		return strReturn;
	}

    //没汇总过
	public static String getBillCodeNotInSummy(String tableNameSummy){
		String strReturn = " and BILL_CODE not in (select BILL_CODE from " + tableNameSummy + ") ";
		return strReturn;
	}

    //
	public static String getReportListenNotSummy(String tableNameSummy, String TypeCodeSummy, String TypeCodeListen){
		String strReturn = " and BILL_CODE not in (select BILL_CODE from " + tableNameSummy;
		strReturn += "                             where (BUSI_DATE, DEPT_CODE) not in (select RPT_DUR, RPT_DEPT from tb_sys_sealed_info where BILL_TYPE = '" + TypeCodeSummy + "') ";
		strReturn += "                             and (BUSI_DATE, DEPT_CODE) in (select RPT_DUR, RPT_DEPT from tb_sys_sealed_info where BILL_TYPE = '" + TypeCodeListen + "') ";
		strReturn += "                             ) ";
		return strReturn;
	}
}
	