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
import com.fh.controller.common.DictsUtil;
import com.fh.controller.common.GenerateTransferData;
import com.fh.controller.common.TmplUtil;
import com.fh.entity.CommonBase;
import com.fh.entity.JqGridModel;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.SysSealed;
import com.fh.entity.TableColumns;
import com.fh.entity.system.User;
import com.fh.service.fhoa.department.DepartmentManager;
import com.fh.service.sysSealedInfo.syssealedinfo.SysSealedInfoManager;
import com.fh.service.system.dictionaries.DictionariesManager;
import com.fh.service.tmplConfigDict.tmplconfigdict.TmplConfigDictManager;
import com.fh.service.tmplconfig.tmplconfig.impl.TmplConfigService;
import com.fh.service.voucher.voucher.VoucherManager;
import com.fh.util.AppUtil;
import com.fh.util.Const;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.date.DateUtils;
import com.fh.util.enums.BillType;
import com.fh.util.enums.TransferOperType;

import net.sf.json.JSONArray;

/**
 * 凭证数据传输
 * 
 * @ClassName: VoucherController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author jiachao
 * @date 2017年6月29日
 *
 */
@Controller
@RequestMapping(value = "/voucher")
public class VoucherController extends BaseController {

	String menuUrl = "voucher/list.do"; // 菜单地址(权限用)
	@Resource(name = "voucherService")
	private VoucherManager voucherService;

	@Resource(name = "departmentService")
	private DepartmentManager departmentService;

	@Resource(name = "tmplconfigService")
	private TmplConfigService tmplconfigService;

	@Resource(name = "tmplconfigdictService")
	private TmplConfigDictManager tmplConfigDictService;

	@Resource(name = "dictionariesService")
	private DictionariesManager dictionariesService;

	@Resource(name = "syssealedinfoService")
	private SysSealedInfoManager syssealedinfoService;
	
	//底行显示的求和与平均值字段
	private	StringBuilder SqlUserdata = new StringBuilder();

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Page page) throws Exception {
		// if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		// //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("voucher/voucher/voucher_list");
		PageData pd = this.getPageData();
		String which = pd.getString("TABLE_CODE");
		String tableCode = "";
		String tableCodeSub = "";
		if (which != null && which.equals("1")) {
			tableCode = "TB_STAFF_SUMMY";
			tableCodeSub = "TB_STAFF_DETAIL";
		} else if (which != null && which.equals("2")) {
			tableCode = "TB_SOCIAL_INC_SUMMY";
			tableCodeSub = "TB_SOCIAL_INC_DETAIL";
		} else if (which != null && which.equals("3")) {
			tableCode = "TB_HOUSE_FUND_SUMMY";
			tableCodeSub = "TB_HOUSE_FUND_DETAIL";
		} else {
			tableCode = "TB_STAFF_SUMMY";
			tableCodeSub = "TB_STAFF_DETAIL";
		}
		// 此处放当前页面初始化时用到的一些数据，例如搜索的下拉列表数据，所需的字典数据、权限数据等等。
		// mv.addObject("pd", pd);
		// *********************加载单位树*******************************
		List<PageData> zdepartmentPdList = new ArrayList<PageData>();
		JSONArray arr = JSONArray.fromObject(departmentService.listAllDepartmentToSelect("0", zdepartmentPdList));
		mv.addObject("zTreeNodes", (null == arr ? "" : arr.toString()));
		// ***********************************************************

		pd.put("which", which);
		mv.addObject("pd", pd);

		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		// 生成主表结构
		TmplUtil tmplUtil = new TmplUtil(tmplconfigService, tmplConfigDictService, dictionariesService,
				departmentService);
		String jqGridColModel = tmplUtil.generateStructureNoEdit(tableCode, Jurisdiction.getCurrentDepartmentID());
		mv.addObject("jqGridColModel", jqGridColModel);

		// 生成子表结构
		String jqGridColModelSub = tmplUtil.generateStructureNoEdit(tableCodeSub,
				Jurisdiction.getCurrentDepartmentID());
		mv.addObject("jqGridColModelSub", jqGridColModelSub);
		
		//底行显示的求和与平均值字段
		SqlUserdata = tmplUtil.getSqlUserdata();
		return mv;
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(JqPage page) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "获取待传输列表Voucher");
		PageData pd = new PageData();
		pd = this.getPageData();
		String which = pd.getString("TABLE_CODE");
	    String voucherType=pd.getString("VOUCHER_TYPE");
		String tableCode = getTableCode(which);
		pd.put("TABLE_CODE", tableCode);
		String sealType= getSealType(which,voucherType);
		pd.put("BILL_TYPE", sealType);//封存类型
		String sealType1="";//汇总封存类型对应的传输接口类型
		if(sealType.equals(BillType.SALLARY_SUMMARY.getNameKey())){
			sealType1=BillType.SALLARY_LISTEN.getNameKey();
		}
		else if(sealType.equals(BillType.GOLD_SUMMARY.getNameKey())){
			sealType1=BillType.GOLD_LISTEN.getNameKey();
		}
		else if(sealType.equals(BillType.SECURITY_SUMMARY.getNameKey())){
			sealType1=BillType.SECURITY_LISTEN.getNameKey();
		}
		pd.put("BILL_TYPE1", sealType1);//封存类型
		
		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		String filters = pd.getString("filters"); // 多条件过滤条件
		if (null != filters && !"".equals(filters)) {
			pd.put("filterWhereResult", SqlTools.constructWhere(filters, null));
		}
		
		page.setPd(pd);
		List<PageData> varList = voucherService.list(page); // 列出Betting列表
		int records = voucherService.countJqGrid(pd);
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		result.setRowNum(page.getRowNum());
		result.setRecords(records);
		result.setPage(page.getPage());
		PageData userdata = null;
		if(SqlUserdata!=null && !SqlUserdata.toString().trim().equals("")){
			//底行显示的求和与平均值字段
			pd.put("Userdata", SqlUserdata.toString());
			userdata = voucherService.getFooterSummary(page);
			result.setUserdata(userdata);
		}
		// PageData userData=new PageData();
		// userData.put("PRICE", 2622.99);
		// result.setUserdata(userData);

		return result;
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/getDetailList")
	public @ResponseBody PageResult<PageData> getDetailList() throws Exception {
		PageData pd = this.getPageData();
		String which = pd.getString("TABLE_CODE");
		String tableCode = getTableCode(which);
		pd.put("TABLE_CODE", tableCode);
		List<PageData> varList = voucherService.listDetail(pd); // 列出Betting列表
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		return result;
	}

	/**
	 * 批量删除
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "批量删除Voucher");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
			return null;
		} // 校验权限
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if (null != DATA_IDS && !"".equals(DATA_IDS)) {
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			voucherService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		} else {
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}

	/**
	 * 批量修改
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/voucherTransfer")
	public @ResponseBody CommonBase voucherTransfer() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "凭证传输");
		// if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;}
		// //校验权限
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		PageData pd = this.getPageData();
		String strDataRows = pd.getString("DATA_ROWS");
		JSONArray array = JSONArray.fromObject(strDataRows);
		List<PageData> listTransferData = (List<PageData>) JSONArray.toCollection(array, PageData.class);// 过时方法

		try{
			if (null != listTransferData && listTransferData.size() > 0) {
				/********************** 生成传输数据 ************************/
				String which = pd.getString("TABLE_CODE");
				String tableCode = getTableCode(which);
				// String voucherType=pd.getString("VOUCHER_TYPE");
	
				// 用语句查询出数据库表的所有字段及其属性；拼接成jqgrid全部列
				List<TableColumns> tableColumns = tmplconfigService.getTableColumns(tableCode);
				GenerateTransferData generateTransferData = new GenerateTransferData();
				String transferData = generateTransferData.generateVoucherData(tableColumns, listTransferData, "001001",
						TransferOperType.INSERT, tableCode);
				// 执行上传FIMS
	
				// 执行上传成功后对数据进行封存
				List<SysSealed> listSysSealed=new ArrayList<SysSealed>();
				User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USERROL);
				String userId = user.getUSER_ID();
				for (PageData pageData : listTransferData) {
					SysSealed item = new SysSealed();
					item.setBILL_CODE(pageData.getString("BILL_CODE"));
					item.setRPT_DEPT(pageData.getString("DEPT_CODE"));
					item.setRPT_DUR(pageData.getString("BUSI_DATE"));
					item.setRPT_USER(userId);
					item.setRPT_DATE(DateUtils.getCurrentTime());// YYYY-MM-DD HH:MM:SS
					String sealType= getSealType(which,"2");//封存类型
					item.setBILL_TYPE(sealType);
					item.setSTATE("1");// 枚举 1封存,0解封
					listSysSealed.add(item);
				}
				syssealedinfoService.insertBatch(listSysSealed);
				/******************************************************/
				commonBase.setCode(0);
			}
		}
		catch(Exception ex){
			commonBase.setMessage(ex.toString());
		}
		return commonBase;
	}

	/**
	 * 根据前端业务表索引获取表名称
	 * 
	 * @param which
	 * @return
	 */
	private String getTableCode(String which) {
		String tableCode = "";
		if (which != null && which.equals("1")) {
			tableCode = "TB_STAFF_SUMMY";
		} else if (which != null && which.equals("2")) {
			tableCode = "TB_SOCIAL_INC_SUMMY";
		} else if (which != null && which.equals("3")) {
			tableCode = "TB_HOUSE_FUND_SUMMY";
		} else {
			tableCode = "TB_STAFF_SUMMY";
		}
		return tableCode;
	}
	
	/**
	 * 根据前端业务表索引,及凭证功能类型获取封存类型
	 * 
	 * @param which
	 * @return
	 */
	private String getSealType(String which,String voucherType) {
		String sealType = "";
		if(voucherType.equals("1")){
			if (which != null && which.equals("1")) {
				sealType = BillType.SALLARY_SUMMARY.getNameKey();
			} else if (which != null && which.equals("2")) {
				sealType = BillType.SECURITY_SUMMARY.getNameKey();
			} else if (which != null && which.equals("3")) {
				sealType = BillType.GOLD_SUMMARY.getNameKey();
			} else {
				sealType = BillType.SALLARY_SUMMARY.getNameKey();
			}
		}else if(voucherType.equals("2")){
			if (which != null && which.equals("1")) {
				sealType = BillType.SALLARY_LISTEN.getNameKey();
			} else if (which != null && which.equals("2")) {
				sealType = BillType.SECURITY_LISTEN.getNameKey();
			} else if (which != null && which.equals("3")) {
				sealType = BillType.GOLD_LISTEN.getNameKey();
			} else {
				sealType = BillType.SALLARY_LISTEN.getNameKey();
			}
		}
		return sealType;
	}
	
	/**汇总数据未上报单位
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/transferValidate")
	public ModelAndView transferValidate()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		String which = pd.getString("TABLE_CODE");
		pd.put("TABLE_CODE", which);
		//String empty=" : ;";
		String strDict=DictsUtil.getDepartmentValue(departmentService);
		pd.put("strDict", strDict);
		mv.setViewName("voucher/voucher/voucher_transvali");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTransferValidate")
	public @ResponseBody PageResult<PageData> getTransferValidate() throws Exception {
		PageData pd = this.getPageData();
		String which = pd.getString("TABLE_CODE");
		String tableCode = getTableCode(which);
		pd.put("TABLE_CODE", tableCode);
		String sealType= getSealType(which,"1");
		pd.put("BILL_TYPE", sealType);//封存类型
		String filters = pd.getString("filters"); // 多条件过滤条件
		if (null != filters && !"".equals(filters)) {
			pd.put("filterWhereResult", SqlTools.constructWhere(filters, null));
		}
		List<PageData> varList = voucherService.getTransferValidate(pd);
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		return result;
	}

	/**
	 * 导出到excel
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel")
	public ModelAndView exportExcel() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "导出Voucher到excel");
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
			return null;
		}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("备注1"); // 1
		titles.add("备注2"); // 2
		titles.add("备注3"); // 3
		titles.add("备注4"); // 4
		titles.add("备注5"); // 5
		titles.add("备注6"); // 6
		titles.add("备注7"); // 7
		titles.add("备注8"); // 8
		titles.add("备注9"); // 9
		titles.add("备注10"); // 10
		titles.add("备注11"); // 11
		titles.add("备注12"); // 12
		titles.add("备注13"); // 13
		titles.add("备注14"); // 14
		titles.add("备注15"); // 15
		titles.add("备注16"); // 16
		titles.add("备注17"); // 17
		titles.add("备注18"); // 18
		titles.add("备注19"); // 19
		titles.add("备注20"); // 20
		titles.add("备注21"); // 21
		titles.add("备注22"); // 22
		titles.add("备注23"); // 23
		titles.add("备注24"); // 24
		titles.add("备注25"); // 25
		titles.add("备注26"); // 26
		titles.add("备注27"); // 27
		titles.add("备注28"); // 28
		dataMap.put("titles", titles);
		List<PageData> varOList = voucherService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < varOList.size(); i++) {
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("BILL_CODE")); // 1
			vpd.put("var2", varOList.get(i).getString("BUSI_DATE")); // 2
			vpd.put("var3", varOList.get(i).getString("ESTB_DEPT")); // 3
			vpd.put("var4", varOList.get(i).getString("USER_GROP")); // 4
			vpd.put("var5", varOList.get(i).getString("SOC_INC_BASE")); // 5
			vpd.put("var6", varOList.get(i).getString("PER_BASIC_FUND")); // 6
			vpd.put("var7", varOList.get(i).getString("PER_SUPP_FUND")); // 7
			vpd.put("var8", varOList.get(i).getString("PER_TOTAL")); // 8
			vpd.put("var9", varOList.get(i).getString("DEPT_BASIC_FUND")); // 9
			vpd.put("var10", varOList.get(i).getString("DEPT_SUPP_FUND")); // 10
			vpd.put("var11", varOList.get(i).getString("DEPT_TOTAL")); // 11
			vpd.put("var12", varOList.get(i).getString("DEPT_CODE")); // 12
			vpd.put("var13", varOList.get(i).getString("USER_CATG")); // 13
			vpd.put("var14", varOList.get(i).getString("PMT_PLACE")); // 14
			vpd.put("var15", varOList.get(i).getString("CUST_COL1")); // 15
			vpd.put("var16", varOList.get(i).getString("CUST_COL2")); // 16
			vpd.put("var17", varOList.get(i).getString("CUST_COL3")); // 17
			vpd.put("var18", varOList.get(i).getString("CUST_COL4")); // 18
			vpd.put("var19", varOList.get(i).getString("CUST_COL5")); // 19
			vpd.put("var20", varOList.get(i).getString("CUST_COL6")); // 20
			vpd.put("var21", varOList.get(i).getString("CUST_COL7")); // 21
			vpd.put("var22", varOList.get(i).getString("CUST_COL8")); // 22
			vpd.put("var23", varOList.get(i).getString("CUST_COL9")); // 23
			vpd.put("var24", varOList.get(i).getString("CUST_COL10")); // 24
			vpd.put("var25", varOList.get(i).getString("ZRZC_CODE")); // 25
			vpd.put("var26", varOList.get(i).getString("BILL_STATE")); // 26
			vpd.put("var27", varOList.get(i).getString("BILL_USER")); // 27
			vpd.put("var28", varOList.get(i).getString("BILL_DATE")); // 28
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
	}
}
