package com.fh.controller.tmplconfig.tmplconfig;

import java.io.Console;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.service.fhoa.department.DepartmentManager;
import com.fh.service.tmplconfig.tmplconfig.TmplConfigManager;
import com.fh.util.AppUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.Tools;
import com.sun.tools.corba.se.idl.constExpr.And;

import net.sf.json.JSONArray;

/**
 * 数据模板详情
* @ClassName: TmplConfigController
* @Description: TODO(这里用一句话描述这个类的作用)
* @author jiachao
* @date 2017年6月19日
*
 */
@Controller
@RequestMapping(value="/tmplconfig")
public class TmplConfigController extends BaseController {
	
	String menuUrl = "tmplconfig/list.do"; //菜单地址(权限用)
	@Resource(name="tmplconfigService")
	private TmplConfigManager tmplconfigService;
	@Resource(name="departmentService")
	private DepartmentManager departmentService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增TmplConfig");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("TMPLCONFIG_ID", this.get32UUID());	//主键
		tmplconfigService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除TmplConfig");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		tmplconfigService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改TmplConfig");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		tmplconfigService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData>	varList = tmplconfigService.list(page);	//列出TmplConfigBase列表
		List<PageData>	listBase = tmplconfigService.listBase(page);	//列出TmplConfigBase列表
		String TABLE_CODE = pd.getString("TABLE_CODE");				//关键词检索条件
		if(null != TABLE_CODE && !"".equals(TABLE_CODE)){
			pd.put("TABLE_CODE", TABLE_CODE.trim());
		} 
		//列表页面树形下拉框用(保持下拉树里面的数据不变)
				String ZDEPARTMENT_ID = pd.getString("ZDEPARTMENT_ID");
				ZDEPARTMENT_ID = Tools.notEmpty(ZDEPARTMENT_ID)?ZDEPARTMENT_ID:Jurisdiction.getDEPARTMENT_ID();
				pd.put("ZDEPARTMENT_ID", ZDEPARTMENT_ID);
				List<PageData> zdepartmentPdList = new ArrayList<PageData>();
				JSONArray arr = JSONArray.fromObject(departmentService.listAllDepartmentToSelect(ZDEPARTMENT_ID,zdepartmentPdList));
				mv.addObject("zTreeNodes", arr.toString());
				PageData dpd = departmentService.findById(pd);
				if(null != dpd){
					ZDEPARTMENT_ID = dpd.getString("NAME");
				}
				mv.addObject("depname", ZDEPARTMENT_ID);
		
				
		
				
		mv.setViewName("tmplconfig/tmplconfig/tmplconfig_list");
		mv.addObject("varList", varList);
		mv.addObject("listBase", listBase);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(Page page) throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		//PageData dpd = departmentService.findById(pd);
		//pd.put("DEPT_BIANMA", dpd.getString("BIANMA"));
		PageData tpd = tmplconfigService.findTableCodeByTableNo(pd);
		pd.put("TABLE_CODE", tpd.getString("TABLE_CODE"));
		page.setPd(pd);
		List<PageData> varList = tmplconfigService.listAll(page);	//列出Betting列表
		PageResult<PageData> result = new PageResult<PageData>();
		if (varList.size()!=0) {
			result.setRows(varList);
		} else {
			List<PageData> temporaryList = tmplconfigService.temporaryList(page);	//列出Betting列表
			result.setRows(temporaryList);
		}
		
		return result;
		
	}
	
	/**
	 * 获取字典集合
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getDictList")
	public List<PageData> getDictList() throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> dictList = tmplconfigService.dictList(pd);
		return dictList;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("tmplconfig/tmplconfig/tmplconfig_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = tmplconfigService.findById(pd);	//根据ID读取
		mv.setViewName("tmplconfig/tmplconfig/tmplconfig_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除TmplConfig");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			tmplconfigService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出TmplConfig到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("单位编码");	//1
		titles.add("表名");	//2
		titles.add("列编码");	//3
		titles.add("列名称");	//4
		titles.add("显示序号");	//5
		titles.add("字典翻译");	//6
		titles.add("列隐藏");	//7
		titles.add("列汇总");	//8
		titles.add("列平均值");	//9
		dataMap.put("titles", titles);
////		List<PageData> varOList = tmplconfigService.listAll(pd);
//		List<PageData> varList = new ArrayList<PageData>();
//		for(int i=0;i<varOList.size();i++){
//			PageData vpd = new PageData();
//			vpd.put("var1", varOList.get(i).getString("DEPT_CODE"));	    //1
//			vpd.put("var2", varOList.get(i).getString("TABLE_CODE"));	    //2
//			vpd.put("var3", varOList.get(i).getString("COL_CODE"));	    //3
//			vpd.put("var4", varOList.get(i).getString("COL_NAME"));	    //4
//			vpd.put("var5", varOList.get(i).get("DISP_ORDER").toString());	//5
//			vpd.put("var6", varOList.get(i).getString("DICT_TRANS"));	    //6
//			vpd.put("var7", varOList.get(i).getString("COL_HIDE"));	    //7
//			vpd.put("var8", varOList.get(i).getString("COL_SUM"));	    //8
//			vpd.put("var9", varOList.get(i).getString("COL_AVE"));	    //9
//			varList.add(vpd);
//		}
//		dataMap.put("varList", varList);
//		ObjectExcelView erv = new ObjectExcelView();
//		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
