package com.fh.controller.staffsummy.staffsummy;

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
import com.fh.service.staffsummy.staffsummy.StaffSummyManager;

/** 
 * 说明：工资汇总
 * 创建人：lhsmplus
 * 创建时间：2017-07-07
 */
@Controller
@RequestMapping(value="/staffsummy")
public class StaffSummyController extends BaseController {
	
	String menuUrl = "staffsummy/list.do"; //菜单地址(权限用)
	@Resource(name="staffsummyService")
	private StaffSummyManager staffsummyService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增StaffSummy");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("STAFFSUMMY_ID", this.get32UUID());	//主键
		staffsummyService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除StaffSummy");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		staffsummyService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改StaffSummy");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		staffsummyService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表StaffSummy");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = staffsummyService.list(page);	//列出StaffSummy列表
		mv.setViewName("staffsummy/staffsummy/staffsummy_list");
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
		mv.setViewName("staffsummy/staffsummy/staffsummy_edit");
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
		pd = staffsummyService.findById(pd);	//根据ID读取
		mv.setViewName("staffsummy/staffsummy/staffsummy_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除StaffSummy");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			staffsummyService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出StaffSummy到excel");
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
		titles.add("备注29");	//29
		titles.add("备注30");	//30
		titles.add("备注31");	//31
		titles.add("备注32");	//32
		titles.add("备注33");	//33
		titles.add("备注34");	//34
		titles.add("备注35");	//35
		titles.add("备注36");	//36
		titles.add("备注37");	//37
		titles.add("备注38");	//38
		titles.add("备注39");	//39
		titles.add("备注40");	//40
		titles.add("备注41");	//41
		titles.add("备注42");	//42
		titles.add("备注43");	//43
		titles.add("备注44");	//44
		titles.add("备注45");	//45
		titles.add("备注46");	//46
		titles.add("备注47");	//47
		titles.add("备注48");	//48
		titles.add("备注49");	//49
		titles.add("备注50");	//50
		titles.add("备注51");	//51
		titles.add("备注52");	//52
		titles.add("备注53");	//53
		titles.add("备注54");	//54
		titles.add("备注55");	//55
		titles.add("备注56");	//56
		titles.add("备注57");	//57
		titles.add("备注58");	//58
		titles.add("备注59");	//59
		titles.add("备注60");	//60
		titles.add("备注61");	//61
		dataMap.put("titles", titles);
		List<PageData> varOList = staffsummyService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("BILL_CODE"));	    //1
			vpd.put("var2", varOList.get(i).getString("BUSI_DATE"));	    //2
			vpd.put("var3", varOList.get(i).getString("ESTB_DEPT"));	    //3
			vpd.put("var4", varOList.get(i).getString("USER_GROP"));	    //4
			vpd.put("var5", varOList.get(i).getString("USER_CATG"));	    //5
			vpd.put("var6", varOList.get(i).getString("DEPT_CODE"));	    //6
			vpd.put("var7", varOList.get(i).getString("ORG_UNIT"));	    //7
			vpd.put("var8", varOList.get(i).getString("SAL_RANGE"));	    //8
			vpd.put("var9", varOList.get(i).getString("POST_SALY"));	    //9
			vpd.put("var10", varOList.get(i).getString("BONUS"));	    //10
			vpd.put("var11", varOList.get(i).getString("CASH_BONUS"));	    //11
			vpd.put("var12", varOList.get(i).getString("WORK_OT"));	    //12
			vpd.put("var13", varOList.get(i).getString("BACK_SALY"));	    //13
			vpd.put("var14", varOList.get(i).getString("RET_SALY"));	    //14
			vpd.put("var15", varOList.get(i).getString("CHK_CASH"));	    //15
			vpd.put("var16", varOList.get(i).getString("INTR_SGL_AWAD"));	    //16
			vpd.put("var17", varOList.get(i).getString("SENY_ALLE"));	    //17
			vpd.put("var18", varOList.get(i).getString("POST_ALLE"));	    //18
			vpd.put("var19", varOList.get(i).getString("NS_ALLE"));	    //19
			vpd.put("var20", varOList.get(i).getString("AREA_ALLE"));	    //20
			vpd.put("var21", varOList.get(i).getString("EXPT_ALLE"));	    //21
			vpd.put("var22", varOList.get(i).getString("TECH_ALLE"));	    //22
			vpd.put("var23", varOList.get(i).getString("LIVE_EXPE"));	    //23
			vpd.put("var24", varOList.get(i).getString("LIVE_ALLE"));	    //24
			vpd.put("var25", varOList.get(i).getString("LEAVE_DM"));	    //25
			vpd.put("var26", varOList.get(i).getString("HOUSE_ALLE"));	    //26
			vpd.put("var27", varOList.get(i).getString("ITEM_ALLE"));	    //27
			vpd.put("var28", varOList.get(i).getString("MEAL_EXPE"));	    //28
			vpd.put("var29", varOList.get(i).getString("TRF_ALLE"));	    //29
			vpd.put("var30", varOList.get(i).getString("TEL_EXPE"));	    //30
			vpd.put("var31", varOList.get(i).getString("HLDY_ALLE"));	    //31
			vpd.put("var32", varOList.get(i).getString("KID_ALLE"));	    //32
			vpd.put("var33", varOList.get(i).getString("COOL_EXPE"));	    //33
			vpd.put("var34", varOList.get(i).getString("EXT_SGL_AWAD"));	    //34
			vpd.put("var35", varOList.get(i).getString("PRE_TAX_PLUS"));	    //35
			vpd.put("var36", varOList.get(i).getString("GROSS_PAY"));	    //36
			vpd.put("var37", varOList.get(i).getString("ENDW_INS"));	    //37
			vpd.put("var38", varOList.get(i).getString("UNEMPL_INS"));	    //38
			vpd.put("var39", varOList.get(i).getString("MED_INS"));	    //39
			vpd.put("var40", varOList.get(i).getString("CASD_INS"));	    //40
			vpd.put("var41", varOList.get(i).getString("HOUSE_FUND"));	    //41
			vpd.put("var42", varOList.get(i).getString("SUP_PESN"));	    //42
			vpd.put("var43", varOList.get(i).getString("TAX_BASE_ADJ"));	    //43
			vpd.put("var44", varOList.get(i).getString("ACCRD_TAX"));	    //44
			vpd.put("var45", varOList.get(i).getString("AFTER_TAX"));	    //45
			vpd.put("var46", varOList.get(i).getString("ACT_SALY"));	    //46
			vpd.put("var47", varOList.get(i).getString("GUESS_DIFF"));	    //47
			vpd.put("var48", varOList.get(i).getString("CUST_COL1"));	    //48
			vpd.put("var49", varOList.get(i).getString("CUST_COL2"));	    //49
			vpd.put("var50", varOList.get(i).getString("CUST_COL3"));	    //50
			vpd.put("var51", varOList.get(i).getString("CUST_COL4"));	    //51
			vpd.put("var52", varOList.get(i).getString("CUST_COL5"));	    //52
			vpd.put("var53", varOList.get(i).getString("CUST_COL6"));	    //53
			vpd.put("var54", varOList.get(i).getString("CUST_COL7"));	    //54
			vpd.put("var55", varOList.get(i).getString("CUST_COL8"));	    //55
			vpd.put("var56", varOList.get(i).getString("CUST_COL9"));	    //56
			vpd.put("var57", varOList.get(i).getString("CUST_COL10"));	    //57
			vpd.put("var58", varOList.get(i).getString("ZRZC_CODE"));	    //58
			vpd.put("var59", varOList.get(i).getString("BILL_STATE"));	    //59
			vpd.put("var60", varOList.get(i).getString("BILL_USER"));	    //60
			vpd.put("var61", varOList.get(i).getString("BILL_DATE"));	    //61
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
