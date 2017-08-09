package com.fh.controller.detailimportquery.detailimportquery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.fh.controller.common.DictsUtil;
import com.fh.controller.common.TmplUtil;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.TableColumns;
import com.fh.entity.TmplConfigDetail;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.enums.SysConfigKeyCode;
import com.fh.util.Jurisdiction;
import com.fh.service.detailimportquery.detailimportquery.DetailImportQueryManager;
import com.fh.service.fhoa.department.impl.DepartmentService;
import com.fh.service.sysConfig.sysconfig.SysConfigManager;
import com.fh.service.system.dictionaries.impl.DictionariesService;
import com.fh.service.system.user.UserManager;
import com.fh.service.tmplConfigDict.tmplconfigdict.impl.TmplConfigDictService;
import com.fh.service.tmplconfig.tmplconfig.impl.TmplConfigService;

/** 
 * 说明： 明细导入查询
 * 创建人：张晓柳
 * 创建时间：2017-08-07
 * @version
 */
@Controller
@RequestMapping(value="/detailimportquery")
public class DetailImportQueryController extends BaseController {
	
	String menuUrl = "detailimportquery/list.do"; //菜单地址(权限用)
	@Resource(name="detailimportqueryService")
	private DetailImportQueryManager detailimportqueryService;
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

	//页面显示数据的年月
	//String SystemDateTime = "";
    
	//底行显示的求和与平均值字段
	StringBuilder SqlUserdata = new StringBuilder();
	//字典
	Map<String, Object> DicList = new LinkedHashMap<String, Object>();
	//表结构  
	Map<String, TableColumns> map_HaveColumnsList = new LinkedHashMap<String, TableColumns>();
	// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
	Map<String, TmplConfigDetail> map_SetColumnsList = new LinkedHashMap<String, TmplConfigDetail>();

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表DetailImportQuery");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)

		PageData pd = this.getPageData();
		String which = getWhileValue(pd.getString("TABLE_CODE"));
		String tableName = getTableCode(which);
		
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("detailimportquery/detailimportquery/detailimportquery_list");
		//当前期间,取自tb_system_config的SystemDateTime字段
		String SystemDateTime = sysConfigManager.currentSection(pd);
		mv.addObject("SystemDateTime", SystemDateTime);
		//while
		pd.put("which", which);
		mv.addObject("pd", pd);
		
		mv.addObject("zTreeNodes", DictsUtil.getDepartmentSelectTreeSource(departmentService));

		TmplUtil tmpl = new TmplUtil(tmplconfigService, tmplconfigdictService, dictionariesService, departmentService,userService);
		String jqGridColModel = tmpl.generateStructureNoEdit(tableName, ShowDepartCode);
		mv.addObject("jqGridColModel", jqGridColModel);

		SqlUserdata = tmpl.getSqlUserdata();
		//字典
		DicList = tmpl.getDicList();
		//表结构  
		map_HaveColumnsList = tmpl.getHaveColumnsList();
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		map_SetColumnsList = tmpl.getSetColumnsList();
		
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
		String tableName = getTableCode(which);
		String sallaryType = getSallaryType(which);

		//表名
		pd.put("TableName", tableName);
		//多条件过滤条件
		String filters = pd.getString("filters");
		if(null != filters && !"".equals(filters)){
			pd.put("filterWhereResult", SqlTools.constructWhere(filters,null));
		}
		if(sallaryType!=null && !sallaryType.trim().equals("")){
			//工资分的类型
			pd.put("SallaryType", sallaryType);
		}
		//QueryFeild
		page.setPd(pd);
		List<PageData> varList = detailimportqueryService.JqPage(page);	//列出Betting列表
		int records = detailimportqueryService.countJqGridExtend(page);
		PageData userdata = null;
		if(SqlUserdata!=null && !SqlUserdata.toString().trim().equals("")){
			//底行显示的求和与平均值字段
			pd.put("Userdata", SqlUserdata.toString());
		    userdata = detailimportqueryService.getFooterSummary(page);
		}
		
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		result.setRowNum(page.getRowNum());
		result.setRecords(records);
		result.setPage(page.getPage());
		result.setUserdata(userdata);
		
		return result;
	}

	private String getWhileValue(String value){
        String which = DefaultWhile;
		if(value != null && !value.trim().equals("")){
			which = value;
		}
		return which;
	}
	private String getTableCode(String which) {
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
		} else if (which != null && which.equals("5")) {
			tableCode = "tb_staff_detail";
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
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出HouseFundDetail到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		PageData pd = this.getPageData();
		String which = getWhileValue(pd.getString("TABLE_CODE"));
		String tableName = getTableCode(which);
		String sallaryType = getSallaryType(which);

		//表名
		pd.put("TableName", tableName);
		//多条件过滤条件
		String filters = pd.getString("filters");
		if(null != filters && !"".equals(filters)){
			pd.put("filterWhereResult", SqlTools.constructWhere(filters,null));
		}
		if(sallaryType!=null && !sallaryType.trim().equals("")){
			//工资分的类型
			pd.put("SallaryType", sallaryType);
		}
		//QueryFeild
		page.setPd(pd);
		List<PageData> varOList = detailimportqueryService.datalistExport(page);
		
		ModelAndView mv = new ModelAndView();
		Map<String,Object> dataMap = new LinkedHashMap<String,Object>();
		dataMap.put("filename", "");
		List<String> titles = new ArrayList<String>();
		List<PageData> varList = new ArrayList<PageData>();
		if(map_SetColumnsList != null && map_SetColumnsList.size() > 0){
		    for (TmplConfigDetail col : map_SetColumnsList.values()) {
				if(col.getCOL_HIDE().equals("1")){
					titles.add(col.getCOL_NAME());
				}
			}
			if(varOList!=null && varOList.size()>0){
				for(int i=0;i<varOList.size();i++){
					PageData vpd = new PageData();
					int j = 1;
					for (TmplConfigDetail col : map_SetColumnsList.values()) {
						if(col.getCOL_HIDE().equals("1")){
						String trans = col.getDICT_TRANS();
						Object getCellValue = varOList.get(i).get(col.getCOL_CODE().toUpperCase());
						if(trans != null && !trans.trim().equals("")){
							String value = "";
							Map<String, String> dicAdd = (Map<String, String>) DicList.getOrDefault(trans, new LinkedHashMap<String, String>());
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
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}