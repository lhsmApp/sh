package com.fh.controller.financeaccounts.financeaccounts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fh.controller.base.BaseController;
import com.fh.controller.common.TmplUtil;
import com.fh.entity.CommonBase;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.TableColumns;
import com.fh.entity.TmplConfigDetail;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.Jurisdiction;
import com.fh.util.enums.SysConfigKeyCode;

import net.sf.json.JSONArray;

import com.fh.service.fhoa.department.impl.DepartmentService;
import com.fh.service.financeaccounts.financeaccounts.FinanceAccountsManager;
import com.fh.service.sysConfig.sysconfig.SysConfigManager;
import com.fh.service.sysSealedInfo.syssealedinfo.impl.SysSealedInfoService;
import com.fh.service.system.dictionaries.impl.DictionariesService;
import com.fh.service.system.user.UserManager;
import com.fh.service.tmplConfigDict.tmplconfigdict.impl.TmplConfigDictService;
import com.fh.service.tmplconfig.tmplconfig.impl.TmplConfigService;

/** 
 * 说明：财务对账
 * 创建人：张晓柳
 * 创建时间：2017-07-28
 */
@Controller
@RequestMapping(value="/financeaccounts")
public class FinanceAccountsController extends BaseController {
	
	String menuUrl = "financeaccounts/list.do"; //菜单地址(权限用)
	@Resource(name="financeaccountsService")
	private FinanceAccountsManager financeaccountsService;
	@Resource(name="tmplconfigService")
	private TmplConfigService tmplconfigService;
	@Resource(name="syssealedinfoService")
	private SysSealedInfoService syssealedinfoService;
	@Resource(name="sysconfigService")
	private SysConfigManager sysConfigManager;
	@Resource(name="tmplconfigdictService")
	private TmplConfigDictService tmplconfigdictService;
	@Resource(name="dictionariesService")
	private DictionariesService dictionariesService;
	@Resource(name="departmentService")
	private DepartmentService departmentService;
	@Resource(name = "userService")
	private UserManager userService;
	
	//默认的which值
	String DefaultWhile = "1";
	//显示结构的单位
    String ShowDepartCode = "01001";

	//页面显示数据的年月
	String SystemDateTime = "";
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表FinanceAccounts");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)

		PageData pd = this.getPageData();
		String which = getWhileValue(pd.getString("TABLE_CODE"));
		String summyTableName = getSummyTableCode(which);
		
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("financeaccounts/financeaccounts/financeaccounts_list");
		//while
		pd.put("which", which);
		mv.addObject("pd", pd);
		//当前期间,取自tb_system_config的SystemDateTime字段
		SystemDateTime = sysConfigManager.currentSection(pd);
		
		TmplUtil tmpl = new TmplUtil(tmplconfigService, tmplconfigdictService, dictionariesService, departmentService,userService);
		String jqGridColModel = tmpl.generateStructureNoEdit(summyTableName, ShowDepartCode);
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
		//页面显示数据的年月
		pd.put("SystemDateTime", SystemDateTime);
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
		String detailSelectFeild = TmplUtil.getSumFeildSelect(groupbyFeild, tableDetailColumns, listGroupbyFeild);
		pd.put("SelectFeild", detailSelectFeild);
		//表名
		pd.put("TableName", detailTableName);
		page.setPd(pd);
		List<PageData> detailSummayList = financeaccountsService.JqPage(page);

		//获取对账汇总信息
		List<TableColumns> tableAuditeColumns = tmplconfigService.getTableColumns(auditeTableName);
		String auditeSelectFeild = TmplUtil.getSumFeildSelect(groupbyFeild, tableAuditeColumns, listGroupbyFeild);
		pd.put("SelectFeild", auditeSelectFeild);
		//表名
		pd.put("TableName", auditeTableName);
		page.setPd(pd);
		List<PageData> auditeSummayList = financeaccountsService.JqPage(page);
		
		List<PageData> varList = getShowList(detailSummayList, auditeSummayList, tableSumColumns);
		int records = varList.size();
		
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		result.setRowNum(page.getRowNum());
		result.setRecords(records);
		result.setPage(page.getPage());
		
		return result;
	}

	/**明细显示结构
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getDetailColModel")
	public @ResponseBody CommonBase getDetailColModel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"getDetailColModel");
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		
		PageData pd = this.getPageData();
		String which = getWhileValue(pd.getString("TABLE_CODE"));
		String TabType = getWhileValue(pd.getString("TabType"));
		String tableNameDetail = getDetailTableCode(which, TabType, true);
		String DEPT_CODE = (String) pd.get("DATA_DEPT_CODE");
		TmplUtil tmpl = new TmplUtil(tmplconfigService, tmplconfigdictService, dictionariesService, departmentService,userService);
		String detailColModel = tmpl.generateStructureNoEdit(tableNameDetail, DEPT_CODE);
		
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
	public @ResponseBody PageResult<PageData> getDetailList() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"getDetailList");
		PageData pd = this.getPageData();
		String which = getWhileValue(pd.getString("TABLE_CODE"));
		String TabType = getWhileValue(pd.getString("TabType"));
		Object DATA_ROWS = pd.get("DATA_ROWS");
		String json = DATA_ROWS.toString();  
        JSONArray array = JSONArray.fromObject(json); 
		List<PageData> listData = (List<PageData>) JSONArray.toCollection(array,PageData.class);
		
		String whereSql = "";
		whereSql += " where BUSI_DATE = '" + SystemDateTime + "' ";
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
					whereSql += " and " + feild + " = '" + listData.get(0).get(feild) + "' ";
				}
			}
		}
		pd.put("whereSql", whereSql);
		
		String falseTableName = getDetailTableCode(which, TabType, true);
		pd.put("TableName", falseTableName);
		List<PageData> listFirst = financeaccountsService.dataListDetail(pd);
		
		String secondTableName = getDetailTableCode(which, TabType, false);
		pd.put("TableName", secondTableName);
		List<PageData> listSecond = financeaccountsService.dataListDetail(pd);

		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		//TmplConfigDetail item = new TmplConfigDetail();
		//item.setDEPT_CODE(departCode);
		//item.setTABLE_CODE(falseTableName);
		//List<TmplConfigDetail> m_columnsList = tmplconfigService.listNeed(item);
		//界面对比的表结构
		List<TableColumns> tableSumColumns = tmplconfigService.getTableColumns(falseTableName);

		List<PageData> varList = getShowList(listFirst, listSecond, tableSumColumns);
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		return result;
	}
	
	private List<PageData> getShowList(List<PageData> listFirst, List<PageData> listSecond, List<TableColumns> m_columnsList){
		List<PageData> varList = new ArrayList<PageData>();
		if(listFirst!=null && listFirst.size() > 0){
			if(!(listSecond!=null && listSecond.size()>0)){
				varList = listFirst;
			} else {
				varList = listFirst;
				for(PageData fir : listFirst){
					for(PageData sec : listSecond){
						
						
						
						
						
						
						
					}
				}
			}
		}
		return varList;
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
			tableCode = "tb_staff_summy";
		} else if (which != null && which.equals("2")) {
			tableCode = "tb_staff_summy";
		} else if (which != null && which.equals("3")) {
			tableCode = "tb_staff_summy";
		} else if (which != null && which.equals("4")) {
			tableCode = "tb_social_inc_summy";
		} else if (which != null && which.equals("5")) {
			tableCode = "tb_house_fund_summy";
		}
		return tableCode;
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
		return groupbyFeild;
	}
	private List<String> getFeildStringToList(String strFeild){
		List<String> list = new ArrayList<String>();
		if(strFeild != null && !strFeild.trim().equals("")){
			list = Arrays.asList(strFeild.replace(" ", "").toUpperCase().split(","));
		}
		return list;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
