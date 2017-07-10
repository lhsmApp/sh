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

import org.aspectj.weaver.ast.Var;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.controller.common.DictsUtil;
import com.fh.entity.CommonBase;
import com.fh.entity.JqGridModel;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.service.fhoa.department.DepartmentManager;
import com.fh.service.tmplConfigDict.tmplconfigdict.TmplConfigDictManager;
import com.fh.service.tmplconfig.tmplconfig.TmplConfigManager;
import com.fh.util.AppUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.Tools;

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
	
	@Resource(name="tmplconfigdictService")
	private TmplConfigDictManager tmplconfigdictService;
	
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		
		List<PageData>	listBase = tmplconfigService.listBase(page);	//列出TmplConfigBase列表
		mv.addObject("zTreeNodes", DictsUtil.getDepartmentSelectTreeSource(departmentService));
		
		String dicTypeValus=DictsUtil.getDicTypeValue(tmplconfigdictService);
		String dictString=" : ;"+dicTypeValus;
				
		mv.addObject("dictString", dictString);
		mv.setViewName("tmplconfig/tmplconfig/tmplconfig_list");
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
		PageData tpd = tmplconfigService.findTableCodeByTableNo(pd);
		pd.put("TABLE_CODE", tpd.getString("TABLE_CODE"));
		page.setPd(pd);
		List<PageData> varList = tmplconfigService.listAll(page);	
		PageResult<PageData> result = new PageResult<PageData>();
		if (varList.size()!=0) {
			result.setRows(varList);
		} else {
			List<PageData> temporaryList = tmplconfigService.temporaryList(page);	
			result.setRows(temporaryList);
		}
		
		return result;
		
	}
	
	/**批量修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/updateAll")
	/*@RequestBody RequestBase<JqGridModel> jqGridModel*/
	public @ResponseBody CommonBase updateAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除JgGrid");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		PageData pd = this.getPageData();
		String strDataRows = pd.getString("DATA_ROWS");
        JSONArray array = JSONArray.fromObject(strDataRows);  
        List<PageData> listData = (List<PageData>) JSONArray.toCollection(array,PageData.class);// 过时方法
        
		if(null != listData && listData.size() > 0){
			//根据这两个条件删除表
			pd.put("DEPT_CODE", listData.get(0).get("DEPT_CODE"));
			pd.put("TABLE_CODE", listData.get(0).get("TABLE_CODE"));
			tmplconfigService.deleteTable(pd);
			
			tmplconfigService.updateAll(listData);
			commonBase.setCode(0);
		}
		return commonBase;
	}
	
	/**
	 * 复制
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/copyAll")
	public @ResponseBody CommonBase copyAll() throws Exception {
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		PageData pd = this.getPageData();
		PageData tpd = tmplconfigService.findTableCodeByTableNo(pd);
		pd.put("TABLE_CODE", tpd.getString("TABLE_CODE"));
		
		String strCode = pd.getString("deptIds");
		JSONArray array = JSONArray.fromObject(strCode);  
        List<String> listCode = (List<String>) JSONArray.toCollection(array,String.class);// 过时方法
        for (String string : listCode) {
        	pd.put("DEPT_CODE", string);
        	tmplconfigService.deleteTable(pd);
		}
        if(null != listCode && listCode.size() > 0){
			tmplconfigService.copyAll(listCode);
			commonBase.setCode(0);
		}
		return commonBase;
	}
}
