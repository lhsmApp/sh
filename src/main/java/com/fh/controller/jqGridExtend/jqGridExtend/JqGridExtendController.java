package com.fh.controller.jqGridExtend.jqGridExtend;

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
import com.fh.entity.CommonBase;
import com.fh.entity.JqGridModel;
import com.fh.entity.JqPage;
import com.fh.entity.PageResult;
import com.fh.service.jqGridExtend.jqGridExtend.JqGridExtendManager;
import com.fh.util.AppUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * JqGridExtend测试练习
* @ClassName: JqGridExtendController
* @Description: TODO(这里用一句话描述这个类的作用)
* @author zhangxiaoliu
* @date 2017年6月8日
*
 */
@Controller
@RequestMapping(value="/jqGridExtend")
public class JqGridExtendController extends BaseController {
	
	String menuUrl = "jqGridExtend/list.do"; //菜单地址(权限用)
	@Resource(name="jqGridExtendService")
	private JqGridExtendManager jqGridExtendService;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表JqGridExtend");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		mv.setViewName("jqGridExtend/jqGridExtend/jqGridExtend_list");
		
		/**此处放当前页面初始化时用到的一些数据，例如搜索的下拉列表数据，所需的字典数据、权限数据等等。
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		 * 
		 * 
		 */
		return mv;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public @ResponseBody CommonBase edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改JqGridExtend");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		PageData pd = new PageData();
		pd = this.getPageData();
		jqGridExtendService.edit(pd);
		commonBase.setCode(0);
		return commonBase;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Betting");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData> varList = jqGridExtendService.list(page);	//列出Betting列表
		int records = jqGridExtendService.countJqGridExtend(page);
		PageData userdata=jqGridExtendService.getFooterSummary(page);
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		result.setRecords(records);
		result.setPage(page.getPage());
		result.setUserdata(userdata);
		
		return result;
	}
	/**明细
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getDetailList")
	public @ResponseBody PageResult<PageData> getDetailList() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"明细Betting");
		PageData pd = new PageData();
		pd = this.getPageData();
		
		List<PageData> varList = jqGridExtendService.getDetailList(pd);	//列出Betting列表
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		
		return result;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	public @ResponseBody CommonBase deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除JqGridExtend");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		PageData pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			jqGridExtendService.deleteAll(ArrayDATA_IDS);
			commonBase.setCode(0);
		}
		pdList.add(pd);
		return commonBase;
	}
	
	 /**批量修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/updateAll")
	public @ResponseBody CommonBase updateAll() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		PageData pd = this.getPageData();
		Object DATA_ROWS = pd.get("DATA_ROWS");
		String json = DATA_ROWS.toString();  
        JSONArray array = JSONArray.fromObject(json);  
        List<JqGridModel> listData = (List<JqGridModel>) JSONArray.toCollection(array,JqGridModel.class);
        
        /* List<JqGridModel>  dtoList=new ArrayList<JqGridModel>();  

        for (int i = 0; i < array.size(); i++) {  
            JSONObject jsonObject = array.getJSONObject(i);  
            JqGridModel item = (JqGridModel) JSONObject.toBean(jsonObject, JqGridModel.class);
            if(item != null){
                dtoList.add(item);  
            }
        }  */

		if(null != listData && listData.size() > 0){
			jqGridExtendService.updateAll(listData);
			commonBase.setCode(0);
		}
		return commonBase;
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出JqGridExtend到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("CategoryName");	//1
		titles.add("ProductName");	//2
		titles.add("Country");	//3
		titles.add("Price");	//4
		titles.add("Quantity");	//5
		dataMap.put("titles", titles);
		List<PageData> varOList = jqGridExtendService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CATEGORYNAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("PRODUCTNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("COUNTRY"));	    //3
			vpd.put("var4", varOList.get(i).getString("PRICE"));	    //4
			vpd.put("var5", varOList.get(i).get("QUANTITY").toString());	//5
			varList.add(vpd);
		}
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
