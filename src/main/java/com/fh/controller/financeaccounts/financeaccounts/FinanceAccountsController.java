package com.fh.controller.financeaccounts.financeaccounts;

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
import com.fh.entity.Page;
import com.fh.util.AppUtil;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.Tools;
import com.fh.service.financeaccounts.financeaccounts.FinanceAccountsManager;

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
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增FinanceAccounts");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("FinanceAccounts_ID", this.get32UUID());	//主键
		financeaccountsService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除FinanceAccounts");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		financeaccountsService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改FinanceAccounts");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		financeaccountsService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表FinanceAccounts");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = financeaccountsService.list(page);	//列出FinanceAccounts列表
		mv.setViewName("financeaccounts/financeaccounts/financeaccounts_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
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
		mv.setViewName("financeaccounts/financeaccounts/financeaccounts_edit");
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
		pd = financeaccountsService.findById(pd);	//根据ID读取
		mv.setViewName("financeaccounts/financeaccounts/financeaccounts_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除FinanceAccounts");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			financeaccountsService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出FinanceAccounts到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("备注1");	//1
		titles.add("备注2");	//2
		titles.add("备注3");	//3
		titles.add("备注4");	//4
		titles.add("备注5");	//5
		titles.add("备注6");	//6
		titles.add("备注7");	//7
		titles.add("备注8");	//8
		titles.add("备注9");	//9
		titles.add("备注10");	//10
		titles.add("备注11");	//11
		titles.add("备注12");	//12
		titles.add("备注13");	//13
		titles.add("备注14");	//14
		titles.add("备注15");	//15
		titles.add("备注16");	//16
		titles.add("备注17");	//17
		titles.add("备注18");	//18
		titles.add("备注19");	//19
		titles.add("备注20");	//20
		titles.add("备注21");	//21
		titles.add("备注22");	//22
		titles.add("备注23");	//23
		titles.add("备注24");	//24
		titles.add("备注25");	//25
		titles.add("备注26");	//26
		titles.add("备注27");	//27
		titles.add("备注28");	//28
		dataMap.put("titles", titles);
		List<PageData> varOList = financeaccountsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("BILL_CODE"));	    //1
			vpd.put("var2", varOList.get(i).getString("BUSI_DATE"));	    //2
			vpd.put("var3", varOList.get(i).getString("ESTB_DEPT"));	    //3
			vpd.put("var4", varOList.get(i).getString("USER_GROP"));	    //4
			vpd.put("var5", varOList.get(i).getString("SOC_INC_BASE"));	    //5
			vpd.put("var6", varOList.get(i).getString("PER_BASIC_FUND"));	    //6
			vpd.put("var7", varOList.get(i).getString("PER_SUPP_FUND"));	    //7
			vpd.put("var8", varOList.get(i).getString("PER_TOTAL"));	    //8
			vpd.put("var9", varOList.get(i).getString("DEPT_BASIC_FUND"));	    //9
			vpd.put("var10", varOList.get(i).getString("DEPT_SUPP_FUND"));	    //10
			vpd.put("var11", varOList.get(i).getString("DEPT_TOTAL"));	    //11
			vpd.put("var12", varOList.get(i).getString("DEPT_CODE"));	    //12
			vpd.put("var13", varOList.get(i).getString("USER_CATG"));	    //13
			vpd.put("var14", varOList.get(i).getString("PMT_PLACE"));	    //14
			vpd.put("var15", varOList.get(i).getString("CUST_COL1"));	    //15
			vpd.put("var16", varOList.get(i).getString("CUST_COL2"));	    //16
			vpd.put("var17", varOList.get(i).getString("CUST_COL3"));	    //17
			vpd.put("var18", varOList.get(i).getString("CUST_COL4"));	    //18
			vpd.put("var19", varOList.get(i).getString("CUST_COL5"));	    //19
			vpd.put("var20", varOList.get(i).getString("CUST_COL6"));	    //20
			vpd.put("var21", varOList.get(i).getString("CUST_COL7"));	    //21
			vpd.put("var22", varOList.get(i).getString("CUST_COL8"));	    //22
			vpd.put("var23", varOList.get(i).getString("CUST_COL9"));	    //23
			vpd.put("var24", varOList.get(i).getString("CUST_COL10"));	    //24
			vpd.put("var25", varOList.get(i).getString("ZRZC_CODE"));	    //25
			vpd.put("var26", varOList.get(i).getString("BILL_STATE"));	    //26
			vpd.put("var27", varOList.get(i).getString("BILL_USER"));	    //27
			vpd.put("var28", varOList.get(i).getString("BILL_DATE"));	    //28
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
