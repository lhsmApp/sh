package com.fh.controller.staffsummy.staffsummy;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
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
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.StaffDetailModel;
import com.fh.entity.StaffSummyModel;
import com.fh.entity.SysSealed;
import com.fh.entity.TableColumns;
import com.fh.entity.system.User;
import com.fh.util.Const;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.collectionSql.GroupUtils;
import com.fh.util.collectionSql.GroupUtils.GroupBy;
import com.fh.util.Jurisdiction;
import com.fh.util.date.DateFormatUtils;
import com.fh.util.date.DateUtils;
import com.fh.util.enums.BillType;
import com.fh.util.enums.DurState;
import com.fh.util.enums.TransferOperType;

import net.sf.json.JSONArray;

import com.fh.service.fhoa.department.impl.DepartmentService;
import com.fh.service.staffDetail.staffdetail.StaffDetailManager;
import com.fh.service.staffsummy.staffsummy.StaffSummyManager;
import com.fh.service.sysConfig.sysconfig.SysConfigManager;
import com.fh.service.sysSealedInfo.syssealedinfo.impl.SysSealedInfoService;
import com.fh.service.system.dictionaries.impl.DictionariesService;
import com.fh.service.tmplConfigDict.tmplconfigdict.impl.TmplConfigDictService;
import com.fh.service.tmplconfig.tmplconfig.impl.TmplConfigService;

/** 
 * 说明：工资汇总
 * 创建人：zhangxiaoliu
 * 创建时间：2017-07-07
 */
@Controller
@RequestMapping(value="/staffsummy")
public class StaffSummyController extends BaseController {
	
	String menuUrl = "staffsummy/list.do"; //菜单地址(权限用)
	@Resource(name="staffsummyService")
	private StaffSummyManager staffsummyService;
	@Resource(name="staffdetailService")
	private StaffDetailManager staffdetailService;
	@Resource(name="tmplconfigService")
	private TmplConfigService tmplconfigService;
	@Resource(name="syssealedinfoService")
	private SysSealedInfoService syssealedinfoService;
	@Resource(name="tmplconfigdictService")
	private TmplConfigDictService tmplconfigdictService;
	@Resource(name="dictionariesService")
	private DictionariesService dictionariesService;
	@Resource(name="departmentService")
	private DepartmentService departmentService;
	@Resource(name="sysconfigService")
	private SysConfigManager sysConfigManager;

	//表名
	String TableNameBase = "tb_staff_summy";
	String TableNameDetail = "tb_staff_detail";
	//枚举类型  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
	String TypeCode = BillType.SALLARY_SUMMARY.getNameKey();
	//显示结构的单位
    String ShowDepartCode = "001001";
	// 查询表的主键字段
	private List<String> keyListBase = Arrays.asList("BILL_CODE", "DEPT_CODE", "BUSI_DATE", "USER_CATG", "USER_GROP");
    
    //汇总字段
    List<String> SumField = Arrays.asList("BUSI_DATE", "DEPT_CODE", "USER_CATG", "USER_GROP");
    String SumFieldToString = tranferSumFieldToString();
	
	//页面显示数据的年月
	String SystemDateTime = "";
	//底行显示的求和与平均值字段
	StringBuilder SqlUserdata = new StringBuilder();
	//默认值
	Map<String, Object> DefaultValueList = new HashMap<String, Object>();
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表StaffSummy");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		PageData pd = this.getPageData();
		
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("staffsummy/staffsummy/staffsummy_list");
		//当前期间,取自tb_system_config的SystemDateTime字段
		SystemDateTime = sysConfigManager.currentSection(pd);
		mv.addObject("SystemDateTime", SystemDateTime);
		
		mv.addObject("zTreeNodes", DictsUtil.getDepartmentSelectTreeSource(departmentService));
		
		TmplUtil tmpl = new TmplUtil(tmplconfigService, tmplconfigdictService, dictionariesService, departmentService, keyListBase);
		String jqGridColModel = tmpl.generateStructure(TableNameBase, ShowDepartCode, 1);

		//底行显示的求和与平均值字段
		SqlUserdata = tmpl.getSqlUserdata();
		//默认值
		DefaultValueList = tmpl.getDefaultValueList();
		
		mv.addObject("jqGridColModel", jqGridColModel);
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表StaffSummy");
		PageData pd = new PageData();
		pd = this.getPageData();
		//多条件过滤条件
		String filters = pd.getString("filters");
		if(null != filters && !"".equals(filters)){
			pd.put("filterWhereResult", SqlTools.constructWhere(filters,null));
		}
		//
		String DepartCode = pd.getString("DepartCode");	
		if(null != DepartCode && !"".equals(DepartCode)){
			pd.put("DepartCode", DepartCode.trim());
		}
		//页面显示数据的年月
		pd.put("SystemDateTime", SystemDateTime);
		//类型
		pd.put("TypeCode", TypeCode);
		pd.put("DurState", DurState.Sealed.getNameKey());
		page.setPd(pd);
		List<PageData> varList = staffsummyService.JqPage(page);	//列出Betting列表
		int records = staffsummyService.countJqGridExtend(page);
		PageData userdata = null;
		if(SqlUserdata!=null && !SqlUserdata.toString().trim().equals("")){
			//底行显示的求和与平均值字段
			pd.put("Userdata", SqlUserdata.toString());
			userdata = staffsummyService.getFooterSummary(page);
		}
		
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		result.setRowNum(page.getRowNum());
		result.setRecords(records);
		result.setPage(page.getPage());
		result.setUserdata(userdata);
		
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
		String DEPT_CODE = (String) pd.get("DATA_DEPT_CODE");
		TmplUtil tmpl = new TmplUtil(tmplconfigService, tmplconfigdictService, dictionariesService, departmentService);
		String detailColModel = tmpl.generateStructure(TableNameDetail, DEPT_CODE, 1);
		
		commonBase.setCode(0);
		commonBase.setMessage(detailColModel);
		
		return commonBase;
	}

	/**明细数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getDetailList")
	public @ResponseBody PageResult<PageData> getDetailList() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"getDetailList");
		PageData pd = this.getPageData();
		
		List<PageData> varList = staffdetailService.getDetailList(pd);	//列出Betting列表
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		
		return result;
	}
	
	 /**上报
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/report")
	public @ResponseBody CommonBase report() throws Exception{
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "report")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);

		User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USERROL);
		String userId = user.getUSER_ID();
        String time = DateUtils.getCurrentTime(DateFormatUtils.DATE_FORMAT2);

		PageData pd = this.getPageData();
		Object DATA_ROWS_REPORT = pd.get("DATA_ROWS_REPORT");
		String json = DATA_ROWS_REPORT.toString();  
        JSONArray array = JSONArray.fromObject(json);  
        

		List<PageData> listTransferData = (List<PageData>) JSONArray.toCollection(array, PageData.class);// 过时方法
		if (null != listTransferData && listTransferData.size() > 0) {
			List<SysSealed> listSysSealed = new ArrayList<SysSealed>();
			// 将获取的字典数据进行分组
			Map<String, List<PageData>> mapListTransferData = GroupUtils.group(listTransferData,
					new GroupBy<String>() {
						@Override
						public String groupby(Object obj) {
							PageData d = (PageData) obj;
							return d.getString("DEPT_CODE__"); // 分组依据为DEPT_CODE
						}
					});
			for (Map.Entry<String, List<PageData>> entry : mapListTransferData.entrySet()) {
				SysSealed item = new SysSealed();
				// item.setBILL_CODE(pageData.getString("BILL_CODE"));
				item.setBILL_CODE(" ");
				item.setRPT_DEPT(entry.getKey());
				List<PageData> listItem = entry.getValue();
				PageData pgItem = listItem.get(0);
				item.setRPT_DUR(pgItem.getString("BUSI_DATE__"));
				item.setRPT_USER(userId);
				item.setRPT_DATE(time);// YYYY-MM-DD HH:MM:SS
				item.setBILL_TYPE(TypeCode.toString());
				item.setSTATE(DurState.Sealed.getNameKey());// 枚举  1封存,0解封
				listSysSealed.add(item);
			}
			syssealedinfoService.insertBatch(listSysSealed);
			commonBase.setCode(0);
		}
        
        
        
        
        /*List<StaffSummyModel> listData = (List<StaffSummyModel>) JSONArray.toCollection(array,StaffSummyModel.class);
        List<SysSealed> listTransfer = new ArrayList<SysSealed>();
        if(null != listData && listData.size() > 0){
        	for(StaffSummyModel summy : listData){
    			SysSealed item = new SysSealed();
    			//item.setBILL_CODE(summy.getBILL_CODE());
    			item.setBILL_CODE(" ");
    			item.setRPT_DEPT(summy.getDEPT_CODE());
    			item.setRPT_DUR(SystemDateTime);
    			item.setRPT_USER(userId);
    			item.setRPT_DATE(time);//YYYY-MM-DD HH:MM:SS
    			item.setBILL_TYPE(TypeCode.toString());// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
    			item.setSTATE(DurState.Sealed.getNameKey());// 枚举  1封存,0解封
    			listTransfer.add(item);
    			
    			String checkState = CheckState(item);
    			if(checkState!=null && checkState.trim() != ""){
    				commonBase.setCode(2);
    				commonBase.setMessage(checkState);
    				break;
    			}
        	}
		}
        if(commonBase.getCode() == -1){
        	syssealedinfoService.insertBatch(listTransfer);
    		commonBase.setCode(0);
        } */
		return commonBase;
	}
	
	 /**汇总
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/summaryModelList")
	public @ResponseBody CommonBase summaryModelList() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "delete")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		
		PageData pd = this.getPageData();
		Object DATA_ROWS_SUM = pd.get("DATA_ROWS_SUM");
		String json = DATA_ROWS_SUM.toString();  
        JSONArray array = JSONArray.fromObject(json);  
        List<PageData> listData = (List<PageData>) JSONArray.toCollection(array,PageData.class);
		if (null != listData && listData.size() > 0) {
			// 将获取的字典数据进行分组
			Map<String, List<PageData>> mapListTransferData = GroupUtils.group(listData,
					new GroupBy<String>() {
						@Override
						public String groupby(Object obj) {
							PageData d = (PageData) obj;
							return d.getString("DEPT_CODE"); // 分组依据为DEPT_CODE
						}
					});
			for (Map.Entry<String, List<PageData>> entry : mapListTransferData.entrySet()) {
				List<PageData> listItem = entry.getValue();
				PageData pgItem = listItem.get(0);
				
				SysSealed item = new SysSealed();
				// item.setBILL_CODE(pageData.getString("BILL_CODE"));
				item.setRPT_DEPT(entry.getKey());
				item.setRPT_DUR(pgItem.getString("BUSI_DATE"));
				item.setBILL_TYPE(TypeCode.toString());
				
    			String checkState = CheckState(item);
    			if(checkState!=null && checkState.trim() != ""){
    				commonBase.setCode(2);
    				commonBase.setMessage(checkState);
    				break;
    			}
    			Map<String, String> map = new HashMap<String, String>();
    			map.put("BUSI_DATE", pgItem.getString("BUSI_DATE"));
    			map.put("DEPT_CODE", entry.getKey());
    			map.put("GroupbyFeild", SumFieldToString);
    			List<PageData> getSaveDate = staffdetailService.getSum(map);
    			
    			
    			

    			/***************获取最大单号及更新最大单号********************
    			/*PageData pdBillNum=new PageData();
    			pdBillNum.put("BILL_CODE", BillNumType.SHBX);
    			pdBillNum.put("BILL_DATE", DateUtil.getMonth());
    			PageData pdBillNumResult=sysbillnumService.findById(pdBillNum);
    			int billNum=ConvertUtils.strToInt(pdBillNumResult.getString("BILL_NUMBER"),0);
    			pdBillNum.put("BILL_NUMBER", billNum++);
    			sysbillnumService.edit(pdBillNum);*/
    			/***************************************************/
    			//TmplUtil.setModelDefault(pd, StaffDetailModel.class, DefaultValueList);
    			//pd.put("deleteFilter", "");
			}
		}
        
        /* if(null != listData && listData.size() > 0){
        	for(StaffSummyModel summy : listData){
    			SysSealed item = new SysSealed();
    			//item.setBILL_CODE(summy.getBILL_CODE());
    			item.setRPT_DEPT(summy.getDEPT_CODE());
    			item.setRPT_DUR(SystemDateTime);
    			item.setBILL_TYPE(TypeCode.toString());// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
    			
    			String checkState = CheckState(item);
    			if(checkState!=null && checkState.trim() != ""){
    				commonBase.setCode(2);
    				commonBase.setMessage(checkState);
    				break;
    			}
        	}
		} */
        if(commonBase.getCode() == -1){
			staffsummyService.summaryModelList(listData);
			commonBase.setCode(0);
        }
		return commonBase;
	}
	
	 /**汇总
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/summaryDepartString")
	public @ResponseBody CommonBase summaryDepartString() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "delete")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		
		PageData pd = this.getPageData();
		Object DATA_DEPART = pd.get("DATA_DEPART");
		String[] listDepart = DATA_DEPART.toString().split(",");  
        if(null != listDepart && listDepart.length > 0){
        	for(String depart : listDepart){
    			SysSealed item = new SysSealed();
    			//item.setBILL_CODE(summy.getBILL_CODE());
    			item.setRPT_DEPT(depart);
    			item.setRPT_DUR(SystemDateTime);
    			item.setBILL_TYPE(TypeCode.toString());// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
    			
    			String checkState = CheckState(item);
    			if(checkState!=null && checkState.trim() != ""){
    				commonBase.setCode(2);
    				commonBase.setMessage(checkState);
    				break;
    			}
        	}
		}
        if(commonBase.getCode() == -1){
			//staffsummyService.summaryDepartString(listDepart, SystemDateTime, SumField);
			commonBase.setCode(0);
        }
		return commonBase;
	}
	
	private String CheckState(SysSealed item) throws Exception{
		String strRut = "编号：" + item.getBILL_CODE() + "期间已封存！";
		String State = syssealedinfoService.getStateFromModel(item);
		if(!DurState.Sealed.getNameKey().equals(State)){// 枚举  1封存,0解封
			strRut = "";
		}
		return strRut;
	}
	
	private String tranferSumFieldToString(){
		StringBuilder SumFieldToString = new StringBuilder();
		for(String field : SumField){
			if(SumFieldToString.toString().trim() != ""){
				SumFieldToString.append(",");
			}
			SumFieldToString.append(field);
		}
		return SumFieldToString.toString();
	}
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
