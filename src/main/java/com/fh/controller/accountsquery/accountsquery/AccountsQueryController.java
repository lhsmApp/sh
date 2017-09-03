package com.fh.controller.accountsquery.accountsquery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fh.controller.base.BaseController;
import com.fh.controller.common.AcconutsShowList;
import com.fh.controller.common.DictsUtil;
import com.fh.controller.common.FilterBillCode;
import com.fh.controller.common.QueryFeildString;
import com.fh.controller.common.TmplUtil;
import com.fh.entity.CommonBase;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.TableColumns;
import com.fh.entity.TmplConfigDetail;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.enums.BillType;
import com.fh.util.enums.DurState;
import com.fh.util.enums.SysConfigKeyCode;

import net.sf.json.JSONArray;

import com.fh.util.Jurisdiction;
import com.fh.service.accountsquery.accountsquery.AccountsQueryManager;
import com.fh.service.fhoa.department.impl.DepartmentService;
import com.fh.service.sysConfig.sysconfig.SysConfigManager;
import com.fh.service.system.dictionaries.impl.DictionariesService;
import com.fh.service.system.user.UserManager;
import com.fh.service.tmplConfigDict.tmplconfigdict.impl.TmplConfigDictService;
import com.fh.service.tmplconfig.tmplconfig.impl.TmplConfigService;

/** 
 * 说明： 对账查询
 * 创建人：张晓柳
 * 创建时间：2017-08-07
 * @version
 */
@Controller
@RequestMapping(value="/accountsquery")
public class AccountsQueryController extends BaseController {
	
	String menuUrl = "accountsquery/list.do"; //菜单地址(权限用)
	@Resource(name="accountsqueryService")
	private AccountsQueryManager accountsqueryService;
	@Resource(name="tmplconfigService")
	private TmplConfigService tmplconfigService;
	@Resource(name="tmplconfigdictService")
	private TmplConfigDictService tmplconfigdictService;
	@Resource(name="dictionariesService")
	private DictionariesService dictionariesService;
	@Resource(name="departmentService")
	private DepartmentService departmentService;
	@Resource(name = "userService")
	private UserManager userService;
	@Resource(name="sysconfigService")
	private SysConfigManager sysConfigManager;

	//默认的which值
	String DefaultWhile = "1";
	//显示结构的单位
    String ShowDepartCode = "01001";

	String HouseFundSummy = "tb_house_fund_summy";
	String SocialIncSummy = "tb_social_inc_summy";
	String StaffSummy = "TB_STAFF_summy";

	//枚举类型  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
	String TypeCodeGoldDetail = BillType.GOLD_DETAIL.getNameKey();
	String TypeCodeGoldSummy = BillType.GOLD_SUMMARY.getNameKey();
	String TypeCodeGoldListen = BillType.GOLD_LISTEN.getNameKey();
	String TypeCodeSocialDetail = BillType.SECURITY_DETAIL.getNameKey();
	String TypeCodeSocialSummy = BillType.SECURITY_SUMMARY.getNameKey();
	String TypeCodeSocialListen = BillType.SECURITY_LISTEN.getNameKey();
	String TypeCodeStaffDetail = BillType.SALLARY_DETAIL.getNameKey();
	String TypeCodeStaffSummy = BillType.SALLARY_SUMMARY.getNameKey();
	String TypeCodeStaffListen = BillType.SALLARY_LISTEN.getNameKey();

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表AccountsQuery");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)

		PageData pd = this.getPageData();
		String which = getWhileValue(pd.getString("TABLE_CODE"));
		String summyTableName = getSummyTableCode(which);
        //分组字段
		String groupbyFeild = getGroupbyFeild(which);
		List<String> listGroupbyFeild = getFeildStringToList(groupbyFeild);
		
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("accountsquery/accountsquery/accountsquery_list");
		//while
		pd.put("which", which);
		mv.addObject("pd", pd);
		//当前期间,取自tb_system_config的SystemDateTime字段
		String SystemDateTime = sysConfigManager.currentSection(pd);
		mv.addObject("SystemDateTime", SystemDateTime);
		
		//DEPT_CODE
		mv.addObject("zTreeNodes", DictsUtil.getDepartmentSelectTreeSource(departmentService));
		
		TmplUtil tmpl = new TmplUtil(tmplconfigService, tmplconfigdictService, dictionariesService, departmentService,userService, listGroupbyFeild);
		String jqGridColModel = tmpl.generateStructureAccount(summyTableName, ShowDepartCode);
		mv.addObject("jqGridColModel", jqGridColModel);

		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表FinanceAccounts");
		
		PageData pd = this.getPageData();
		String which = getWhileValue(pd.getString("TABLE_CODE"));
		String summyTableName = getSummyTableCode(which);
		String auditeTableName = getAuditeTableCode(which);
		String detailTableName = getDetailTableCode(which);
		String sallaryType = getSallaryType(which);
		String groupbyFeild = getGroupbyFeild(which);
		List<String> listGroupbyFeild = getFeildStringToList(groupbyFeild);

		//多条件过滤条件
		String filters = pd.getString("filters");
		if(null != filters && !"".equals(filters)){
			pd.put("filterWhereResult", SqlTools.constructWhere(filters,null));
		}
		//显示查询
		String QueryFeild = QueryFeildString.getQueryFeild(pd, QueryFeildList);
		if(QueryFeild!=null && !QueryFeild.equals("")){
			pd.put("QueryFeild", QueryFeild);
		}
		if(sallaryType!=null && !sallaryType.trim().equals("")){
			//工资分的类型
			pd.put("SallaryType", sallaryType);
		}
		//汇总字段
		pd.put("GroupbyFeild", groupbyFeild);
		
		//界面对比的表结构
		List<TableColumns> tableSumColumns = tmplconfigService.getTableColumns(summyTableName);
		
		//获取明细汇总信息
		List<TableColumns> tableDetailColumns = tmplconfigService.getTableColumns(detailTableName);
		String detailSelectFeild = TmplUtil.getSumFeildSelect(listGroupbyFeild, tableDetailColumns);
		pd.put("SelectFeild", detailSelectFeild);
		//表名
		pd.put("TableName", detailTableName);
		//上报
		String detailReport = " and (BUSI_DATE, DEPT_CODE) in (select RPT_DUR, RPT_DEPT from tb_sys_sealed_info where STATE = '" + DurState.Sealed.getNameKey() + "' and BILL_TYPE = '" + getDetailTypeCode(which) + "' ) ";
		detailReport += FilterBillCode.getBillCodeNotInSumInvalid(HouseFundSummy);
		detailReport += FilterBillCode.getBillCodeNotInSumInvalid(SocialIncSummy);
		detailReport += FilterBillCode.getBillCodeNotInSumInvalid(StaffSummy);
		
		detailReport += FilterBillCode.getReportListenNotSummy(HouseFundSummy, TypeCodeGoldSummy, TypeCodeGoldListen);
		detailReport += FilterBillCode.getReportListenNotSummy(SocialIncSummy, TypeCodeSocialSummy, TypeCodeSocialListen);
		detailReport += FilterBillCode.getReportListenNotSummy(StaffSummy, TypeCodeStaffSummy, TypeCodeStaffListen);
		
		pd.put("CheckReport", detailReport);
		page.setPd(pd);
		List<PageData> detailSummayList = accountsqueryService.JqPage(page);

		//获取对账汇总信息
		List<TableColumns> tableAuditeColumns = tmplconfigService.getTableColumns(auditeTableName);
		String auditeSelectFeild = TmplUtil.getSumFeildSelect(listGroupbyFeild, tableAuditeColumns);
		pd.put("SelectFeild", auditeSelectFeild);
		//表名
		pd.put("TableName", auditeTableName);
		//上报
		//String auditeReport = " and (BUSI_DATE, DEPT_CODE) in (select RPT_DUR, RPT_DEPT from tb_sys_sealed_info where STATE = '" + DurState.Sealed.getNameKey() + "' and BILL_TYPE = '" + getAuditeTypeCode(which) + "' ) ";
		//pd.put("CheckReport", auditeReport);
		pd.put("CheckReport", "");
		page.setPd(pd);
		List<PageData> auditeSummayList = accountsqueryService.JqPage(page);
		
		List<PageData> varList = AcconutsShowList.getShowListAll(detailSummayList, auditeSummayList, tableSumColumns, listGroupbyFeild, TmplUtil.keyExtra);
		int records = varList.size();
		List<PageData> showList = AcconutsShowList.getShowListPage(varList, page.getPage(), page.getRowNum());
		
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(showList);
		result.setRowNum(page.getRowNum());
		result.setRecords(records);
		result.setPage(page.getPage());
		
		return result;
	}
	
	//界面查询字段
    List<String> QueryFeildList = Arrays.asList("BUSI_DATE", "DEPT_CODE");

	/**明细显示结构
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getDetailColModel")
	public @ResponseBody CommonBase getDetailColModel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"getDetailColModel");
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		// 字典
		excel_dicList = new LinkedHashMap<String, Object>();
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		excel_SetColumnsList = new LinkedHashMap<String, TmplConfigDetail>();
		//导出数据
		excelListData = new ArrayList<PageData>();
		
		PageData pd = this.getPageData();
		String which = getWhileValue(pd.getString("TABLE_CODE"));
		String TabType = getWhileValue(pd.getString("TabType"));
		String tableNameDetail = getDetailTableCode(which, TabType, true);
		String DEPT_CODE = (String) pd.get("DATA_DEPT_CODE");
		List<String> resetList = Arrays.asList("USER_CODE");
		TmplUtil tmpl = new TmplUtil(tmplconfigService, tmplconfigdictService, dictionariesService, departmentService,userService,resetList);
		String detailColModel = tmpl.generateStructureAccount(tableNameDetail, DEPT_CODE);

		// 字典
		excel_dicList = tmpl.getDicList();
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		excel_SetColumnsList = tmpl.getSetColumnsList();
		
		commonBase.setCode(0);
		commonBase.setMessage(detailColModel);
		
		return commonBase;
	}

	/**明细数据
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getDetailList")
	public @ResponseBody PageResult<PageData> getDetailList(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"getDetailList");
		//导出数据
		excelListData = new ArrayList<PageData>();
		
		PageData pd = this.getPageData();
		String which = getWhileValue(pd.getString("TABLE_CODE"));
		String TabType = getWhileValue(pd.getString("TabType"));
		Object DATA_ROWS = pd.get("DATA_ROWS");
		String json = DATA_ROWS.toString();  
        JSONArray array = JSONArray.fromObject(json); 
		List<PageData> listData = (List<PageData>) JSONArray.toCollection(array,PageData.class);
		
		String whereSql = "";
		String whereSqlFirst = "";
		String whereSqlSecond = "";
		//多条件过滤条件
		String filters = pd.getString("filters");
		if(null != filters && !"".equals(filters.trim())){
			whereSql += SqlTools.constructWhere(filters,null);
		}
		//工资分的类型
		String sallaryType = getSallaryType(which);
		if(sallaryType!=null && !sallaryType.trim().equals("")){
			whereSql += " and " + sallaryType;
		}
        //分组字段
		String groupbyFeild = getGroupbyFeild(which);
		List<String> listGroupbyFeild = getFeildStringToList(groupbyFeild);
		if(listGroupbyFeild!=null){
			for(String feild : listGroupbyFeild){
				if(feild != null && !feild.trim().equals("")){
					whereSql += " and " + feild + " = '" + listData.get(0).get(feild + TmplUtil.keyExtra) + "' ";
				}
			}
		}
		whereSqlFirst = whereSql;
		whereSqlFirst += getDetailHelpful(TabType, true);
		whereSqlSecond = whereSql;
		whereSqlSecond += getDetailHelpful(TabType, false);
		
		String firstTableName = getDetailTableCode(which, TabType, true);
		pd.put("TableName", firstTableName);
		pd.put("whereSql", whereSqlFirst);
		page.setPd(pd);
		List<PageData> listFirst = accountsqueryService.dataListDetail(page);
		
		String secondTableName = getDetailTableCode(which, TabType, false);
		pd.put("TableName", secondTableName);
		pd.put("whereSql", whereSqlSecond);
		page.setPd(pd);
		List<PageData> listSecond = accountsqueryService.dataListDetail(page);

		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		//TmplConfigDetail item = new TmplConfigDetail();
		//item.setDEPT_CODE(departCode);
		//item.setTABLE_CODE(falseTableName);
		//List<TmplConfigDetail> m_columnsList = tmplconfigService.listNeed(item);
		//界面对比的表结构
		List<TableColumns> tableSumColumns = tmplconfigService.getTableColumns(firstTableName);

		List<String> listMatchFeild = Arrays.asList("USER_CODE");
		List<PageData> varList = AcconutsShowList.getShowListAll(listFirst, listSecond, tableSumColumns, listMatchFeild, "");
		//导出数据
		excelListData = varList;
		
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		return result;
	}


	// 字典
	private Map<String, Object> excel_dicList = new LinkedHashMap<String, Object>();
	// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
	private Map<String, TmplConfigDetail> excel_SetColumnsList = new LinkedHashMap<String, TmplConfigDetail>();
	//导出数据
	List<PageData> excelListData = new ArrayList<PageData>();
	
	/**导出数据
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/excel")
	public ModelAndView excel() throws Exception{
		ModelAndView mv = new ModelAndView();
		Map<String,Object> dataMap = new LinkedHashMap<String,Object>();
		dataMap.put("filename", "");
		List<String> titles = new ArrayList<String>();
		List<PageData> varList = new ArrayList<PageData>();
		if(excel_SetColumnsList != null && excel_SetColumnsList.size() > 0){
		    for (TmplConfigDetail col : excel_SetColumnsList.values()) {
				if(col.getCOL_HIDE().equals("1")){
					titles.add(col.getCOL_NAME());
				}
			}
			if(excelListData!=null && excelListData.size()>0){
				for(int i=0;i<excelListData.size();i++){
					PageData vpd = new PageData();
					int j = 1;
					for (TmplConfigDetail col : excel_SetColumnsList.values()) {
						if(col.getCOL_HIDE().equals("1")){
						    String trans = col.getDICT_TRANS();
						    Object getCellValue = excelListData.get(i).get(col.getCOL_CODE().toUpperCase());
						    if(trans != null && !trans.trim().equals("")){
							    String value = "";
							    Map<String, String> dicAdd = (Map<String, String>) excel_dicList.getOrDefault(trans, new LinkedHashMap<String, String>());
							    value = dicAdd.getOrDefault(getCellValue, "");
							    vpd.put("var" + j, value);
						    } else {
							    vpd.put("var" + j, getCellValue.toString());
						    }
						    j++;
						}
					}
					varList.add(vpd);
				}
			}
		}
		dataMap.put("titles", titles);
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap); 
		return mv;
	}

	private String getWhileValue(String value){
        String which = DefaultWhile;
		if(value != null && !value.trim().equals("")){
			which = value;
		}
		return which;
	}
	/**
	 * 根据前端业务表索引获取表名称
	 * 
	 * @param which
	 * @return
	 */
	private String getSummyTableCode(String which) {
		String tableCode = "";
		if (which != null && which.equals("1")) {
			tableCode = StaffSummy;
		} else if (which != null && which.equals("2")) {
			tableCode = StaffSummy;
		} else if (which != null && which.equals("3")) {
			tableCode = StaffSummy;
		} else if (which != null && which.equals("4")) {
			tableCode = SocialIncSummy;
		} else if (which != null && which.equals("5")) {
			tableCode = HouseFundSummy;
		}
		return tableCode;
	}
	
	private String getDetailTypeCode(String which) {
		String typeCode = "";
		if (which != null && which.equals("1")) {
			typeCode = BillType.SALLARY_DETAIL.getNameKey();
		} else if (which != null && which.equals("2")) {
			typeCode = BillType.SALLARY_DETAIL.getNameKey();
		} else if (which != null && which.equals("3")) {
			typeCode = BillType.SALLARY_DETAIL.getNameKey();
		} else if (which != null && which.equals("4")) {
			typeCode = BillType.SECURITY_DETAIL.getNameKey();
		} else if (which != null && which.equals("5")) {
			typeCode = BillType.GOLD_DETAIL.getNameKey();
		}
		return typeCode;
	}
	private String getAuditeTableCode(String which) {
		String tableCode = "";
		if (which != null && which.equals("1")) {
			tableCode = "tb_staff_audit";
		} else if (which != null && which.equals("2")) {
			tableCode = "tb_staff_audit";
		} else if (which != null && which.equals("3")) {
			tableCode = "tb_staff_audit";
		} else if (which != null && which.equals("4")) {
			tableCode = "tb_social_inc_audit";
		} else if (which != null && which.equals("5")) {
			tableCode = "tb_house_fund_audit";
		}
		return tableCode;
	}
	private String getDetailTableCode(String which) {
		String tableCode = "";
		if (which != null && which.equals("1")) {
			tableCode = "tb_staff_detail";
		} else if (which != null && which.equals("2")) {
			tableCode = "tb_staff_detail";
		} else if (which != null && which.equals("3")) {
			tableCode = "tb_staff_detail";
		} else if (which != null && which.equals("4")) {
			tableCode = "tb_social_inc_detail";
		} else if (which != null && which.equals("5")) {
			tableCode = "tb_house_fund_detail";
		}
		return tableCode;
	}
	private String getDetailTableCode(String which, String tabType, Boolean bolFirst) {
		String tableCode = "";
		if ("1".equals(which) || "2".equals(which) || "3".equals(which)) {
			if("1".equals(tabType)){
				tableCode = bolFirst ? "tb_staff_detail" : "tb_staff_audit";
			} else if("2".equals(tabType)){
				tableCode = bolFirst ? "tb_staff_audit" : "tb_staff_detail";
			}
		} else if ("4".equals(which)) {
			if("1".equals(tabType)){
				tableCode = bolFirst ? "tb_social_inc_detail" : "tb_social_inc_audit";
			} else if("2".equals(tabType)){
				tableCode = bolFirst ? "tb_social_inc_audit" : "tb_social_inc_detail";
			}
		} else if ("5".equals(which)) {
			if("1".equals(tabType)){
				tableCode = bolFirst ? "tb_house_fund_detail" : "tb_house_fund_audit";
			} else if("2".equals(tabType)){
				tableCode = bolFirst ? "tb_house_fund_audit" : "tb_house_fund_detail";
			}
		}
		return tableCode;
	}
	private String getSallaryType(String which) throws Exception {
		String strKeyCode = "";
		if (which != null && which.equals("1")) {
			strKeyCode = SysConfigKeyCode.ChkMktGRPCOND;
		} else if (which != null && which.equals("2")) {
			strKeyCode = SysConfigKeyCode.ChkRunGRPCOND;
		} else if (which != null && which.equals("3")) {
			strKeyCode = SysConfigKeyCode.ChkEmployGRPCOND;
		}
		String sallaryType = "";
		if(strKeyCode != null && !strKeyCode.trim().equals("")){
			PageData pd = new PageData();
			pd.put("KEY_CODE", strKeyCode);
			sallaryType = sysConfigManager.getSysConfigByKey(pd);
		}
		return sallaryType;
	}
	private String getGroupbyFeild(String which) throws Exception {
		String strKeyCode = "";
		if (which != null && which.equals("1")) {
			strKeyCode = SysConfigKeyCode.ChkStaffGRP;
		} else if (which != null && which.equals("2")) {
			strKeyCode = SysConfigKeyCode.ChkStaffGRP;
		} else if (which != null && which.equals("3")) {
			strKeyCode = SysConfigKeyCode.ChkStaffGRP;
		} else if (which != null && which.equals("4")) {
			strKeyCode = SysConfigKeyCode.ChkSocialGRP;
		} else if (which != null && which.equals("5")) {
			strKeyCode = SysConfigKeyCode.ChkHouseGRP;
		}
		String groupbyFeild = "";
		if(strKeyCode != null && !strKeyCode.trim().equals("")){
			PageData pd = new PageData();
			pd.put("KEY_CODE", strKeyCode);
			groupbyFeild = sysConfigManager.getSysConfigByKey(pd);
		}
		List<String> list = getFeildStringToList(groupbyFeild);
		if(!(list!=null&&list.contains("BUSI_DATE"))){
			if(groupbyFeild!=null && !groupbyFeild.trim().equals("")){
				groupbyFeild += ",";
			}
			groupbyFeild += "BUSI_DATE";
		}
		if(!(list!=null&&list.contains("DEPT_CODE"))){
			if(groupbyFeild!=null && !groupbyFeild.trim().equals("")){
				groupbyFeild += ",";
			}
			groupbyFeild += "DEPT_CODE";
		}
		return groupbyFeild;
	}
	private List<String> getFeildStringToList(String strFeild){
		List<String> list = new ArrayList<String>();
		if(strFeild != null && !strFeild.trim().equals("")){
			list = Arrays.asList(strFeild.replace(" ", "").toUpperCase().split(","));
		}
		return list;
	}private String getDetailHelpful(String tabType, Boolean bolFirst) {
		String detailReport = "";
		if("1".equals(tabType)){
			if(bolFirst){
				detailReport += FilterBillCode.getBillCodeNotInSumInvalid(HouseFundSummy);
				detailReport += FilterBillCode.getBillCodeNotInSumInvalid(SocialIncSummy);
				detailReport += FilterBillCode.getBillCodeNotInSumInvalid(StaffSummy);
				
				detailReport += FilterBillCode.getReportListenNotSummy(HouseFundSummy, TypeCodeGoldSummy, TypeCodeGoldListen);
				detailReport += FilterBillCode.getReportListenNotSummy(SocialIncSummy, TypeCodeSocialSummy, TypeCodeSocialListen);
				detailReport += FilterBillCode.getReportListenNotSummy(StaffSummy, TypeCodeStaffSummy, TypeCodeStaffListen);
			}
		} else if("2".equals(tabType)){
			if(!bolFirst){
				detailReport += FilterBillCode.getBillCodeNotInSumInvalid(HouseFundSummy);
				detailReport += FilterBillCode.getBillCodeNotInSumInvalid(SocialIncSummy);
				detailReport += FilterBillCode.getBillCodeNotInSumInvalid(StaffSummy);
				
				detailReport += FilterBillCode.getReportListenNotSummy(HouseFundSummy, TypeCodeGoldSummy, TypeCodeGoldListen);
				detailReport += FilterBillCode.getReportListenNotSummy(SocialIncSummy, TypeCodeSocialSummy, TypeCodeSocialListen);
				detailReport += FilterBillCode.getReportListenNotSummy(StaffSummy, TypeCodeStaffSummy, TypeCodeStaffListen);
			}
		}
		return detailReport;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}