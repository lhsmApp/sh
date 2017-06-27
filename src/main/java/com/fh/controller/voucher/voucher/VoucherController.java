package com.fh.controller.voucher.voucher;

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
import com.fh.controller.common.TmplUtil;
import com.fh.entity.CommonBase;
import com.fh.entity.JqGridModel;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.service.fhoa.department.DepartmentManager;
import com.fh.service.system.dictionaries.DictionariesManager;
import com.fh.service.tmplConfigDict.tmplconfigdict.TmplConfigDictManager;
import com.fh.service.tmplconfig.tmplconfig.impl.TmplConfigService;
import com.fh.service.voucher.voucher.VoucherManager;
import com.fh.util.AppUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;

import net.sf.json.JSONArray;

/** 
 * 说明：凭证数据传输
 * 创建人：FH Q313596790
 * 创建时间：2017-06-23
 */
@Controller
@RequestMapping(value="/voucher")
public class VoucherController extends BaseController {
	
	String menuUrl = "voucher/list.do"; //菜单地址(权限用)
	@Resource(name="voucherService")
	private VoucherManager voucherService;
	
	@Resource(name="departmentService")
	private DepartmentManager departmentService;
	
	@Resource(name="tmplconfigService")
	private TmplConfigService tmplconfigService;
	
	@Resource(name="tmplconfigdictService")
	private TmplConfigDictManager tmplConfigDictService;
	
	@Resource(name="dictionariesService")
	private DictionariesManager dictionariesService;
	
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("voucher/voucher/voucher_list");
		PageData pd = this.getPageData();
		String which=pd.getString("TABLE_CODE");
		String tableCode="";
		String tableCodeSub="";
		if(which!=null&&which.equals("1")){
			tableCode="TB_STAFF_SUMMY";
			tableCodeSub="TB_STAFF_DETAIL";
		}
		else if(which!=null&&which.equals("2")){
			tableCode="TB_SOCIAL_INC_SUMMY";
			tableCodeSub="TB_SOCIAL_INC_DETAIL";
		}
		else if(which!=null&&which.equals("3")){
			tableCode="TB_HOUSE_FUND_SUMMY";
			tableCodeSub="TB_HOUSE_FUND_DETAIL";
		}
		else{
			tableCode="TB_HOUSE_FUND_SUMMY";
			tableCodeSub="TB_HOUSE_FUND_DETAIL";
		}
		//此处放当前页面初始化时用到的一些数据，例如搜索的下拉列表数据，所需的字典数据、权限数据等等。
		//mv.addObject("pd", pd);
		//*********************加载单位树*******************************
		List<PageData> zdepartmentPdList = new ArrayList<PageData>();
		JSONArray arr = JSONArray.fromObject(departmentService.listAllDepartmentToSelect("0",zdepartmentPdList));
		mv.addObject("zTreeNodes", (null == arr ?"":arr.toString()));
		//***********************************************************
		
		pd.put("which", which);
		mv.addObject("pd", pd);
		
		//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		//生成主表结构
		TmplUtil tmplUtil=new TmplUtil(tmplconfigService,tmplConfigDictService,dictionariesService,departmentService);
		String jqGridColModel=tmplUtil.generateStructureNoEdit(tableCode,Jurisdiction.getCurrentDepartmentID());
		mv.addObject("jqGridColModel", jqGridColModel);
		
		//生成子表结构
		String jqGridColModelSub=tmplUtil.generateStructureNoEdit(tableCodeSub,Jurisdiction.getCurrentDepartmentID());
		mv.addObject("jqGridColModelSub", jqGridColModelSub);
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"获取待传输列表Voucher");
		PageData pd = new PageData();
		pd = this.getPageData();
		String which=pd.getString("TABLE_CODE");
		//String voucherType=pd.getString("VOUCHER_TYPE");
		String tableCode="";
		if(which!=null&&which.equals("1")){
			tableCode="TB_STAFF_SUMMY";
		}
		else if(which!=null&&which.equals("2")){
			tableCode="TB_SOCIAL_INC_SUMMY";
		}
		else if(which!=null&&which.equals("3")){
			tableCode="TB_HOUSE_FUND_SUMMY";
		}
		else{
			tableCode="TB_HOUSE_FUND_SUMMY";
		}
		pd.put("TABLE_CODE", tableCode);
		
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String filters = pd.getString("filters");				//多条件过滤条件
		if(null != filters && !"".equals(filters)){
			pd.put("filterWhereResult", SqlTools.constructWhere(filters,null));
		}
		
		page.setPd(pd);
		List<PageData> varList = voucherService.list(page);	//列出Betting列表
		int records = voucherService.countJqGrid(pd);
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		result.setRowNum(page.getRowNum());
		result.setRecords(records);
		result.setPage(page.getPage());
		//PageData userData=new PageData();
		//userData.put("PRICE", 2622.99);
		//result.setUserdata(userData);
		
		return result;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getDetailList")
	public @ResponseBody PageResult<PageData> getDetailList() throws Exception{
		PageData pd = this.getPageData();
		List<PageData> varList = voucherService.listDetail(pd);	//列出Betting列表
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		return result;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Voucher");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			voucherService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
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
        List<JqGridModel> listData = (List<JqGridModel>) JSONArray.toCollection(array,JqGridModel.class);// 过时方法
        /*List<JqGridModel>  dtoList=new ArrayList<JqGridModel>();  
        for (int i = 0; i < array.size(); i++) {  
            JSONObject jsonObject = array.getJSONObject(i);  
            JqGridModel item = (JqGridModel) JSONObject.toBean(jsonObject, JqGridModel.class);
            if(item != null){
                dtoList.add(item);  
            }
        }  */
		if(null != listData && listData.size() > 0){
			//voucherService.updateAll(listData);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Voucher到excel");
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
		List<PageData> varOList = voucherService.listAll(pd);
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
