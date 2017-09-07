package com.fh.controller.staffDetail.staffdetail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.fh.controller.base.BaseController;
import com.fh.controller.common.DictsUtil;
import com.fh.controller.common.FilterBillCode;
import com.fh.controller.common.QueryFeildString;
import com.fh.controller.common.TmplUtil;
import com.fh.entity.CommonBase;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.SysSealed;
import com.fh.entity.TableColumns;
import com.fh.entity.TmplConfigDetail;
import com.fh.entity.system.User;
import com.fh.exception.CustomException;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.date.DateFormatUtils;
import com.fh.util.date.DateUtils;
import com.fh.util.enums.DurState;
import com.fh.util.enums.TmplType;
import com.fh.util.Const;
import com.fh.util.Jurisdiction;
import com.fh.util.excel.LeadingInExcelToPageData;

import net.sf.json.JSONArray;

import com.fh.service.fhoa.department.impl.DepartmentService;
import com.fh.service.importdetail.importdetail.impl.ImportDetailService;
import com.fh.service.staffDetail.staffdetail.StaffDetailManager;
import com.fh.service.sysConfig.sysconfig.SysConfigManager;
import com.fh.service.sysSealedInfo.syssealedinfo.impl.SysSealedInfoService;
import com.fh.service.system.dictionaries.impl.DictionariesService;
import com.fh.service.system.user.UserManager;
import com.fh.service.tmplConfigDict.tmplconfigdict.impl.TmplConfigDictService;
import com.fh.service.tmplconfig.tmplconfig.impl.TmplConfigService;

/** 
 * 说明：工资明细
 * 创建人：zhangxiaoliu
 * 创建时间：2017-06-16
 */
@Controller
@RequestMapping(value="/staffdetail")
public class StaffDetailController extends BaseController {
	
	String menuUrl = "staffdetail/list.do"; //菜单地址(权限用)
	@Resource(name="staffdetailService")
	private StaffDetailManager staffdetailService;
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
	@Resource(name = "importdetailService")
	private ImportDetailService importdetailService;

	//表名
	String TableNameDetail = "TB_STAFF_DETAIL";
	String TableNameSummy = "TB_STAFF_summy";

	//默认的which值
	String DefaultWhile = TmplType.TB_STAFF_DETAIL_CONTRACT.getNameKey();
	//枚举类型 TmplType
	String TypeCodeDetail = "";
	String TypeCodeSummy = "";
	String TypeCodeListen = "";

	//页面显示数据的年月
	String SystemDateTime = "";
	//页面显示数据的二级单位
	String UserDepartCode = "";
	//登录人的二级单位是最末层
	private int departSelf = 0;
	
	//底行显示的求和与平均值字段
	StringBuilder SqlUserdata = new StringBuilder();
	//字典
	Map<String, Object> DicList = new LinkedHashMap<String, Object>();
	//表结构  
	Map<String, TableColumns> map_HaveColumnsList = new LinkedHashMap<String, TableColumns>();
	// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
	Map<String, TmplConfigDetail> map_SetColumnsList = new LinkedHashMap<String, TmplConfigDetail>();

	//界面查询字段
    List<String> QueryFeildList = Arrays.asList("DEPT_CODE", "CUST_COL7", "USER_GROP");
    //设置必定不用编辑的列
    List<String> MustNotEditList = Arrays.asList("BILL_CODE", "BUSI_DATE", "DEPT_CODE", "CUST_COL7", "USER_GROP");
	// 查询表的主键字段，作为标准列，jqgrid添加带__列，mybaits获取带__列
    List<String> keyListAdd = Arrays.asList("STAFF_IDENT", "USER_CODE");
	List<String> keyListBase = getKeyListBase();
	private List<String> getKeyListBase(){
		List<String> list = new ArrayList<String>();
		for(String strFeild : MustNotEditList){
			if (!list.contains(strFeild)) {
			    list.add(strFeild);
			}
		}
		for(String strFeild : keyListAdd){
			if (!list.contains(strFeild)) {
				list.add(strFeild);
			}
		}
		return list;
	}
	
	String getPageListSelectedCustCol7 = "";
	String getPageListSelectedDepartCode = "";
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表StaffDetail");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)

		getPageListSelectedCustCol7 = "";
		getPageListSelectedDepartCode = "";
		
		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		//当前期间,取自tb_system_config的SystemDateTime字段
		SystemDateTime = sysConfigManager.currentSection(getPd);
		//当前登录人所在二级单位
		UserDepartCode = Jurisdiction.getCurrentDepartmentID();//
		//User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USERROL);
		//String DepartName = user.getDEPARTMENT_NAME();
		
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("staffDetail/staffdetail/staffdetail_list");
		//while
		getPd.put("which", SelectedTableNo);
		//mv.addObject("SystemDateTime", SystemDateTime);
		//mv.addObject("DepartName", DepartName);
		
		// 枚举  1封存,0解封
		String State = DurState.Sealed.getNameKey();
		mv.addObject("State", String.valueOf(State.equals(DurState.Release.getNameKey())? true:false));

		//CUST_COL7 FMISACC 帐套字典
		mv.addObject("FMISACC", DictsUtil.getDictsByParentCode(dictionariesService, "FMISACC"));
		// *********************加载单位树  DEPT_CODE*******************************
		String DepartmentSelectTreeSource=DictsUtil.getDepartmentSelectTreeSource(departmentService);
		if(DepartmentSelectTreeSource.equals("0"))
		{
			this.departSelf = 1;
			getPd.put("departTreeSource", DepartmentSelectTreeSource);
		} else {
			departSelf = 0;
			getPd.put("departTreeSource", 1);
		}
		mv.addObject("zTreeNodes", DepartmentSelectTreeSource);
		// ***********************************************************
		
		TmplUtil tmpl = new TmplUtil(tmplconfigService, tmplconfigdictService, dictionariesService, 
				departmentService,userService,keyListBase);//
		//tmpl.setMustNotEditFeildList(MustNotEditList);
		String jqGridColModel = tmpl.generateStructure(SelectedTableNo, UserDepartCode, 3, MustNotEditList);
		
		SqlUserdata = tmpl.getSqlUserdata();
		//字典
		DicList = tmpl.getDicList();
		//表结构  
		map_HaveColumnsList = tmpl.getHaveColumnsList();
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		map_SetColumnsList = tmpl.getSetColumnsList();

		mv.addObject("pd", getPd);
		mv.addObject("jqGridColModel", jqGridColModel);
		return mv;
	}

	/**状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getState")
	public @ResponseBody CommonBase getState() throws Exception{
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		String returnState = DurState.Sealed.getNameKey();
		
		PageData getPd = this.getPageData();
		//员工组 必须执行，用来设置汇总和传输上报类型
		getWhileValue(getPd.getString("SelectedTableNo"));
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		if(departSelf == 1){
			SelectedDepartCode = UserDepartCode;
		}
		
		if(SelectedCustCol7 != null && !SelectedCustCol7.trim().equals("") && SelectedDepartCode != null && !SelectedDepartCode.trim().equals("")){
			PageData statePd = new PageData();
			//封存状态,取自tb_sys_sealed_info表state字段, 数据操作需要前提为当前明细数据未封存，如果已确认封存，则明细数据不能再进行操作。
			statePd.put("BILL_OFF", SelectedCustCol7);
			statePd.put("RPT_DEPT", SelectedDepartCode);
			statePd.put("RPT_DUR", SystemDateTime);
			statePd.put("BILL_TYPE", TypeCodeDetail);
			String getState = syssealedinfoService.getState(statePd);
			if(!DurState.Sealed.getNameKey().equals(getState)){
				returnState = DurState.Release.getNameKey();
			}
		}
		commonBase.setMessage(String.valueOf(returnState.equals(DurState.Release.getNameKey())? true:false));
		commonBase.setCode(0);
		
		return commonBase;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表StaffDetail");

		getPageListSelectedCustCol7 = "";
		getPageListSelectedDepartCode = "";
		
		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		if(departSelf == 1){
			SelectedDepartCode = UserDepartCode;
		}
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");
		
		getPageListSelectedCustCol7 = SelectedCustCol7;
		getPageListSelectedDepartCode = SelectedDepartCode;

		PageData getQueryFeildPd = new PageData();
		getQueryFeildPd.put("USER_GROP", emplGroupType);
		getQueryFeildPd.put("DEPT_CODE", SelectedDepartCode);
		getQueryFeildPd.put("CUST_COL7", SelectedCustCol7);
		String QueryFeild = QueryFeildString.getQueryFeild(getQueryFeildPd, QueryFeildList);
		if(!(SelectedDepartCode != null && !SelectedDepartCode.trim().equals(""))){
			QueryFeild += " and 1 != 1 ";
		}
		if(!(SelectedCustCol7 != null && !SelectedCustCol7.trim().equals(""))){
			QueryFeild += " and 1 != 1 ";
		}
		if(!(emplGroupType!=null && !emplGroupType.trim().equals(""))){
			QueryFeild += " and 1 != 1 ";
		}
		//根据凭证上报情况判断当前显示信息
		String strHelpful = FilterBillCode.getCanOperateCondition(syssealedinfoService, 
				SelectedDepartCode, SystemDateTime, SelectedCustCol7, 
				TypeCodeListen, TypeCodeSummy, TableNameSummy);
		if(!(strHelpful != null && !strHelpful.trim().equals(""))){
			strHelpful += " and 1 != 1 ";
		}
		QueryFeild += strHelpful;
		getPd.put("QueryFeild", QueryFeild);
		//多条件过滤条件
		String filters = getPd.getString("filters");
		if(null != filters && !"".equals(filters)){
			getPd.put("filterWhereResult", SqlTools.constructWhere(filters,null));
		}
		//页面显示数据的年月
		getPd.put("SystemDateTime", SystemDateTime);
		//页面显示数据的二级单位
		//getPd.put("DepartCode", SelectedDepartCode);
		String strFieldSelectKey = QueryFeildString.getFieldSelectKey(keyListBase, TmplUtil.keyExtra);
		if(null != strFieldSelectKey && !"".equals(strFieldSelectKey.trim())){
			getPd.put("FieldSelectKey", strFieldSelectKey);
		}
		page.setPd(getPd);
		List<PageData> varList = staffdetailService.JqPage(page);	//列出Betting列表
		int records = staffdetailService.countJqGridExtend(page);
		PageData userdata = null;
		if(SqlUserdata!=null && !SqlUserdata.toString().trim().equals("")){
			//底行显示的求和与平均值字段
			getPd.put("Userdata", SqlUserdata.toString());
			userdata = staffdetailService.getFooterSummary(page);
		}
		
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		result.setRowNum(page.getRowNum());
		result.setRecords(records);
		result.setPage(page.getPage());
		result.setUserdata(userdata);
		
		return result;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public @ResponseBody CommonBase edit() throws Exception{
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		logBefore(logger, Jurisdiction.getUsername()+"修改StaffDetail");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限

		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		if(departSelf == 1){
			SelectedDepartCode = UserDepartCode;
		}
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");
		//操作
		String oper = getPd.getString("oper");

		//判断选择为必须选择的
		String strGetCheckMustSelected = CheckMustSelectedAndSame(emplGroupType, SelectedCustCol7, SelectedDepartCode, true);
		if(strGetCheckMustSelected!=null && !strGetCheckMustSelected.trim().equals("")){
			commonBase.setCode(2);
			commonBase.setMessage(strGetCheckMustSelected);
			return commonBase;
		}
		
		String checkState = CheckState(SelectedCustCol7, SelectedDepartCode);
		if(checkState!=null && !checkState.trim().equals("")){
			commonBase.setCode(2);
			commonBase.setMessage(checkState);
			return commonBase;
		}
		//必定不用编辑的列  MustNotEditList Arrays.asList("BILL_CODE", "BUSI_DATE", "DEPT_CODE", "CUST_COL7", "USER_GROP");
		getPd.put("BILL_CODE", " ");
		if(oper.equals("add")){
			getPd.put("BUSI_DATE", SystemDateTime);
			getPd.put("DEPT_CODE", SelectedDepartCode);
			getPd.put("CUST_COL7", SelectedCustCol7);
			getPd.put("USER_GROP", emplGroupType);
		} else {
			for(String strFeild : MustNotEditList){
				getPd.put(strFeild, getPd.get(strFeild + TmplUtil.keyExtra));
			}
		}
		TmplUtil.setModelDefault(getPd, map_HaveColumnsList);
		
		FilterBillCode.copyInsert(syssealedinfoService, importdetailService, 
				SelectedDepartCode, SystemDateTime, SelectedCustCol7, 
				TypeCodeListen, TypeCodeSummy, TableNameSummy, TableNameDetail, 
				emplGroupType,
				map_HaveColumnsList);
		String strHelpful = FilterBillCode.getCanOperateCondition(syssealedinfoService, 
				SelectedDepartCode, SystemDateTime, SelectedCustCol7, 
				TypeCodeListen, TypeCodeSummy, TableNameSummy);
		if(!(strHelpful != null && !strHelpful.trim().equals(""))){
			commonBase.setCode(2);
			commonBase.setMessage("获取可操作的数据的条件失败！");
			return commonBase;
		}
		getPd.put("CanOperate", strHelpful);
		
		List<PageData> listData = new ArrayList<PageData>();
		listData.add(getPd);
		List<String> repeatList = staffdetailService.findByUserCodeModel(listData);
		if(repeatList!=null && repeatList.size()>0){
			commonBase.setCode(2);
			commonBase.setMessage("此区间内编码已存在！");
			return commonBase;
		} 
		List<String> StaffIdentList = staffdetailService.findStaffIdentByModel(listData);
		if(StaffIdentList!=null && StaffIdentList.size()>0){
			commonBase.setCode(2);
			commonBase.setMessage("此区间内身份证号已存在！");
			return commonBase;
		} 
        staffdetailService.deleteUpdateAll(listData);
		commonBase.setCode(0);
		
		return commonBase;
	}
	
	 /**批量修改
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/updateAll")
	public @ResponseBody CommonBase updateAll() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		
		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		if(departSelf == 1){
			SelectedDepartCode = UserDepartCode;
		}
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");

		//判断选择为必须选择的
		String strGetCheckMustSelected = CheckMustSelectedAndSame(emplGroupType, SelectedCustCol7, SelectedDepartCode, true);
		if(strGetCheckMustSelected!=null && !strGetCheckMustSelected.trim().equals("")){
			commonBase.setCode(2);
			commonBase.setMessage(strGetCheckMustSelected);
			return commonBase;
		}
		
		String checkState = CheckState(SelectedCustCol7, SelectedDepartCode);
		if(checkState!=null && !checkState.trim().equals("")){
			commonBase.setCode(2);
			commonBase.setMessage(checkState);
		} else {
			FilterBillCode.copyInsert(syssealedinfoService, importdetailService, 
					SelectedDepartCode, SystemDateTime, SelectedCustCol7, 
					TypeCodeListen, TypeCodeSummy, TableNameSummy, TableNameDetail, 
					emplGroupType,
					map_HaveColumnsList);
			String strHelpful = FilterBillCode.getCanOperateCondition(syssealedinfoService, 
					SelectedDepartCode, SystemDateTime, SelectedCustCol7, 
					TypeCodeListen, TypeCodeSummy, TableNameSummy);
			if(!(strHelpful != null && !strHelpful.trim().equals(""))){
				commonBase.setCode(2);
				commonBase.setMessage("获取可操作的数据的条件失败！");
				return commonBase;
			}
			
			Object DATA_ROWS = getPd.get("DataRows");
			String json = DATA_ROWS.toString();  
	        JSONArray array = JSONArray.fromObject(json);  
	        List<PageData> listData = (List<PageData>) JSONArray.toCollection(array,PageData.class);
	        List<String> listUserCodeAdd = new ArrayList<String>();
	        List<String> listStaffIdentAdd = new ArrayList<String>();
	        for(PageData item : listData){
	        	String strUserCode = item.getString("USER_CODE__");
	        	String strStaffIdent = item.getString("STAFF_IDENT__");
	        	if(listUserCodeAdd.contains(strUserCode)){
					commonBase.setCode(2);
					commonBase.setMessage("此区间内编码重复:" + strUserCode);
					return commonBase;
	        	}
	        	listUserCodeAdd.add(strUserCode);
	        	if(listStaffIdentAdd.contains(strStaffIdent)){
					commonBase.setCode(2);
					commonBase.setMessage("此区间内身份证号重复:" + strStaffIdent);
					return commonBase;
	        	}
	        	listStaffIdentAdd.add(strStaffIdent);
	        	item.put("BILL_CODE", " ");
	        	item.put("CanOperate", strHelpful);
				TmplUtil.setModelDefault(item, map_HaveColumnsList);
	        }
			if(null != listData && listData.size() > 0){
				List<String> repeatList = staffdetailService.findByUserCodeModel(listData);
				if(repeatList!=null && repeatList.size()>0){
					commonBase.setCode(2);
					commonBase.setMessage("此区间内编码已存在！");
					return commonBase;
				}

				List<String> StaffIdentList = staffdetailService.findStaffIdentByModel(listData);
				if(StaffIdentList!=null && StaffIdentList.size()>0){
					commonBase.setCode(2);
					commonBase.setMessage("此区间内身份证号已存在！");
					return commonBase;
				} 
				staffdetailService.deleteUpdateAll(listData);
				commonBase.setCode(0);
			}
		}
		return commonBase;
	}
	
	 
	/**批量删除
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/deleteAll")
	public @ResponseBody CommonBase deleteAll() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "delete")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		
		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		if(departSelf == 1){
			SelectedDepartCode = UserDepartCode;
		}
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");

		//判断选择为必须选择的
		String strGetCheckMustSelected = CheckMustSelectedAndSame(emplGroupType, SelectedCustCol7, SelectedDepartCode, true);
		if(strGetCheckMustSelected!=null && !strGetCheckMustSelected.trim().equals("")){
			commonBase.setCode(2);
			commonBase.setMessage(strGetCheckMustSelected);
			return commonBase;
		}
		
		String checkState = CheckState(SelectedCustCol7, SelectedDepartCode);
		if(checkState!=null && !checkState.trim().equals("")){
			commonBase.setCode(2);
			commonBase.setMessage(checkState);
		} else {
			FilterBillCode.copyInsert(syssealedinfoService, importdetailService, 
					SelectedDepartCode, SystemDateTime, SelectedCustCol7, 
					TypeCodeListen, TypeCodeSummy, TableNameSummy, TableNameDetail, 
					emplGroupType,
					map_HaveColumnsList);
			String strHelpful = FilterBillCode.getCanOperateCondition(syssealedinfoService, 
					SelectedDepartCode, SystemDateTime, SelectedCustCol7, 
				    TypeCodeListen, TypeCodeSummy, TableNameSummy);
			if(!(strHelpful != null && !strHelpful.trim().equals(""))){
				commonBase.setCode(2);
				commonBase.setMessage("获取可操作的数据的条件失败！");
				return commonBase;
			}
			
			Object DATA_ROWS = getPd.get("DataRows");
			String json = DATA_ROWS.toString();  
	        JSONArray array = JSONArray.fromObject(json);  
	        List<PageData> listData = (List<PageData>) JSONArray.toCollection(array,PageData.class);
	        if(null != listData && listData.size() > 0){
	        	for(PageData item : listData){
	        	    item.put("CanOperate", strHelpful);
	            }
				staffdetailService.deleteAll(listData);
				commonBase.setCode(0);
			}
		}
		return commonBase;
	}
	
	
	/**打开上传EXCEL页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goUploadExcel")
	public ModelAndView goUploadExcel()throws Exception{
		CommonBase commonBase = new CommonBase();
	    commonBase.setCode(-1);
	    
		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		if(departSelf == 1){
			SelectedDepartCode = UserDepartCode;
		}
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");

		//判断选择为必须选择的
		String strGetCheckMustSelected = CheckMustSelectedAndSame(emplGroupType, SelectedCustCol7, SelectedDepartCode, true);
		if(strGetCheckMustSelected!=null && !strGetCheckMustSelected.trim().equals("")){
			commonBase.setCode(2);
			commonBase.setMessage(strGetCheckMustSelected);
		}
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("common/uploadExcel");
		mv.addObject("local", "staffdetail");
		mv.addObject("which", SelectedTableNo);
		mv.addObject("SelectedDepartCode", SelectedDepartCode);
		mv.addObject("SelectedCustCol7", SelectedCustCol7);
		mv.addObject("commonBaseCode", commonBase.getCode());
		mv.addObject("commonMessage", commonBase.getMessage());
		return mv;
	}

	
	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/readExcel")
	//public @ResponseBody CommonBase readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
	public ModelAndView readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;}//校验权限

		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		if(departSelf == 1){
			SelectedDepartCode = UserDepartCode;
		}
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");
		//判断选择为必须选择的
		String strGetCheckMustSelected = CheckMustSelectedAndSame(emplGroupType, SelectedCustCol7, SelectedDepartCode, true);
		if(strGetCheckMustSelected!=null && !strGetCheckMustSelected.trim().equals("")){
			commonBase.setCode(2);
			commonBase.setMessage(strGetCheckMustSelected);
		} else {
			String checkState = CheckState(SelectedCustCol7, SelectedDepartCode);
			if(checkState!=null && !checkState.trim().equals("")){
				commonBase.setCode(2);
				commonBase.setMessage(checkState);
			} else {
				if(!(SystemDateTime!=null && !SystemDateTime.trim().equals("")
						&& SelectedDepartCode!=null && !SelectedDepartCode.trim().equals(""))){
					commonBase.setCode(2);
					commonBase.setMessage("当前区间和当前单位不能为空！");
				} else {
					FilterBillCode.copyInsert(syssealedinfoService, importdetailService, 
							SelectedDepartCode, SystemDateTime, SelectedCustCol7, 
							TypeCodeListen, TypeCodeSummy, TableNameSummy, TableNameDetail, 
							emplGroupType,
							map_HaveColumnsList);
					String strHelpful = FilterBillCode.getCanOperateCondition(syssealedinfoService, 
							SelectedDepartCode, SystemDateTime, SelectedCustCol7, 
							TypeCodeListen, TypeCodeSummy, TableNameSummy);
					if(!(strHelpful != null && !strHelpful.trim().equals(""))){
						commonBase.setCode(2);
						commonBase.setMessage("获取可操作的数据的条件失败！");
					} else {
						// 局部变量
						LeadingInExcelToPageData<PageData> testExcel = null;
						Map<Integer, Object> uploadAndReadMap = null;
						try {
							// 定义需要读取的数据
							String formart = "yyyy-MM-dd";
							String propertiesFileName = "config";
							String kyeName = "file_path";
							int sheetIndex = 0;
							Map<String, String> titleAndAttribute = null;
							// 定义对应的标题名与对应属性名
							titleAndAttribute = new LinkedHashMap<String, String>();
							
							//配置表设置列
							if(map_SetColumnsList != null && map_SetColumnsList.size() > 0){
								for (TmplConfigDetail col : map_SetColumnsList.values()) {
									titleAndAttribute.put(col.getCOL_NAME(), col.getCOL_CODE());
								}
							}

							// 调用解析工具包
							testExcel = new LeadingInExcelToPageData<PageData>(formart);
							// 解析excel，获取客户信息集合

							uploadAndReadMap = testExcel.uploadAndRead(file, propertiesFileName, kyeName, sheetIndex,
									titleAndAttribute, map_HaveColumnsList, map_SetColumnsList, DicList);
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("读取Excel文件错误", e);
							throw new CustomException("读取Excel文件错误",false);
						}
						boolean judgement = false;
						if(uploadAndReadMap.get(1).equals(false)){
							Map<String, Object> returnError =  (Map<String, Object>) uploadAndReadMap.get(2);
							String message = "字典无此翻译： "; // \n
							for (String k : returnError.keySet())  
						    {
								message += k + " : " + returnError.get(k);
						    }
							commonBase.setCode(2);
							commonBase.setMessage(message);
						} else {
							List<PageData> uploadAndRead = (List<PageData>) uploadAndReadMap.get(2);
							if (uploadAndRead != null && !"[]".equals(uploadAndRead.toString()) && uploadAndRead.size() >= 1) {
								judgement = true;
							}
							if (judgement) {
								List<String> sbRet = new ArrayList<String>();
								int listSize = uploadAndRead.size();
								if(listSize > 0){
									//获取数据库中不是本部门、员工组和账套中的UserCode、StaffIdent
									PageData pdHaveFeild = new PageData();
									pdHaveFeild.put("SystemDateTime", SystemDateTime);
									pdHaveFeild.put("SelectedDepartCode", SelectedDepartCode);
									pdHaveFeild.put("SelectedCustCol7", SelectedCustCol7);
									pdHaveFeild.put("emplGroupType", emplGroupType);
									pdHaveFeild.put("CanOperate", strHelpful);
									List<String> listUserCode = staffdetailService.exportHaveUserCode(pdHaveFeild);
							        List<String> listStaffIdent = staffdetailService.exportHaveStaffIdent(pdHaveFeild);
									
									for(int i=0;i<listSize;i++){
										uploadAndRead.get(i).put("CanOperate", strHelpful);
										uploadAndRead.get(i).put("BILL_CODE", " ");
										String getCUST_COL7 = (String) uploadAndRead.get(i).get("CUST_COL7");
										String getUSER_GROP = (String) uploadAndRead.get(i).get("USER_GROP");
										if(!(getCUST_COL7!=null && !getCUST_COL7.trim().equals(""))){
											uploadAndRead.get(i).put("CUST_COL7", SelectedCustCol7);
											getCUST_COL7 = SelectedCustCol7;
										}
										if(!SelectedCustCol7.equals(getCUST_COL7)){
											if(!sbRet.contains("导入账套和当前账套必须一致！")){
												sbRet.add("导入账套和当前账套必须一致！");
											}
										}
										if(!(getUSER_GROP!=null && !getUSER_GROP.trim().equals(""))){
											uploadAndRead.get(i).put("USER_GROP", emplGroupType);
											getUSER_GROP = emplGroupType;
										}
										if(!emplGroupType.equals(getUSER_GROP)){
											if(!sbRet.contains("导入员工组和当前员工组必须一致！")){
												sbRet.add("导入员工组和当前员工组必须一致！");
											}
										}
										String getBUSI_DATE = (String) uploadAndRead.get(i).get("BUSI_DATE");
										String getDEPT_CODE = (String) uploadAndRead.get(i).get("DEPT_CODE");
										String getUSER_CODE = (String) uploadAndRead.get(i).get("USER_CODE");
										String getSTAFF_IDENT = (String) uploadAndRead.get(i).get("STAFF_IDENT");
										if(!(getBUSI_DATE!=null && !getBUSI_DATE.trim().equals(""))){
											uploadAndRead.get(i).put("BUSI_DATE", SystemDateTime);
											getBUSI_DATE = SystemDateTime;
										}
										if(!SystemDateTime.equals(getBUSI_DATE)){
											if(!sbRet.contains("导入区间和当前区间必须一致！")){
												sbRet.add("导入区间和当前区间必须一致！");
											}
										}
										if(!(getDEPT_CODE!=null && !getDEPT_CODE.trim().equals(""))){
											uploadAndRead.get(i).put("DEPT_CODE", SelectedDepartCode);
											getDEPT_CODE = SelectedDepartCode;
										}
										if(!SelectedDepartCode.equals(getDEPT_CODE)){
											if(!sbRet.contains("导入单位和当前单位必须一致！")){
												sbRet.add("导入单位和当前单位必须一致！");
											}
										}
										if(!(getUSER_CODE!=null && !getUSER_CODE.trim().equals(""))){
											if(!sbRet.contains("人员编码不能为空！")){
												sbRet.add("人员编码不能为空！");
											}
										}
										if(listUserCode.contains(getUSER_CODE.trim())){
											String strUserAdd = "人员编码" + getUSER_CODE + "重复！";
											if(!sbRet.contains(strUserAdd)){
												sbRet.add(strUserAdd);
											}
										} else {
											listUserCode.add(getUSER_CODE.trim());
										}
										if(!(getSTAFF_IDENT!=null && !getSTAFF_IDENT.trim().equals(""))){
											if(!sbRet.contains("身份证号不能为空！")){
												sbRet.add("身份证号不能为空！");
											}
										}
										if(listStaffIdent.contains(getSTAFF_IDENT.trim())){
											String strUserAdd = "身份证号" + getSTAFF_IDENT + "重复！";
											if(!sbRet.contains(strUserAdd)){
												sbRet.add(strUserAdd);
											}
										} else {
											listStaffIdent.add(getSTAFF_IDENT.trim());
										}
										String getESTB_DEPT = (String) uploadAndRead.get(i).get("ESTB_DEPT");
										if(!(getESTB_DEPT!=null && !getESTB_DEPT.trim().equals(""))){
											uploadAndRead.get(i).put("ESTB_DEPT", SelectedDepartCode);
										}
										TmplUtil.setModelDefault(uploadAndRead.get(i), map_HaveColumnsList);
									}
									if(sbRet.size()>0){
										StringBuilder sbTitle = new StringBuilder();
										for(String str : sbRet){
											sbTitle.append(str + "  "); // \n
										}
										commonBase.setCode(2);
										commonBase.setMessage(sbTitle.toString());
									} else {
										//此处执行集合添加 
										staffdetailService.batchImport(uploadAndRead);
										commonBase.setCode(0);
									}
								}
							} else {
								commonBase.setCode(-1);
								commonBase.setMessage("TranslateUtil");
							}
						}
					}
				}
			}
		}
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("common/uploadExcel");
		mv.addObject("local", "staffdetail");
		mv.addObject("which", SelectedTableNo);
		mv.addObject("SelectedDepartCode", SelectedDepartCode);
		mv.addObject("SelectedCustCol7", SelectedCustCol7);
		mv.addObject("commonBaseCode", commonBase.getCode());
		mv.addObject("commonMessage", commonBase.getMessage());
		return mv;
	}
	
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	//public void downExcel(HttpServletResponse response)throws Exception{
	public ModelAndView downExcel(JqPage page) throws Exception{
		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		if(departSelf == 1){
			SelectedDepartCode = UserDepartCode;
		}
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");

		PageData transferPd = this.getPageData();
		//页面显示数据的二级单位
		transferPd.put("SelectedDepartCode", SelectedDepartCode);
		//账套
		transferPd.put("SelectedCustCol7", SelectedCustCol7);
		//员工组
		transferPd.put("emplGroupType", emplGroupType);
		
		//页面显示数据的二级单位
		List<PageData> varOList = staffdetailService.exportModel(transferPd);
		return export(varOList, "StaffDetail"); //工资明细
	}
	
	
	/**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出StaffDetail到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		if(departSelf == 1){
			SelectedDepartCode = UserDepartCode;
		}
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");
		
		//页面显示数据的年月
		getPd.put("SystemDateTime", SystemDateTime);
		//页面显示数据的二级单位
		getPd.put("SelectedDepartCode", SelectedDepartCode);
		//账套
		getPd.put("SelectedCustCol7", SelectedCustCol7);
		//员工组
		getPd.put("emplGroupType", emplGroupType);

		String strHelpful = FilterBillCode.getCanOperateCondition(syssealedinfoService, 
				SelectedDepartCode, SystemDateTime, SelectedCustCol7, 
				TypeCodeListen, TypeCodeSummy, TableNameSummy);
		if(!(strHelpful != null && !strHelpful.trim().equals(""))){
			ObjectExcelView erv = new ObjectExcelView();
			Map<String,Object> dataMap = new LinkedHashMap<String,Object>();
			ModelAndView mv = new ModelAndView(erv,dataMap); 
			return mv;
		}
		getPd.put("CanOperate", strHelpful);
		
		page.setPd(getPd);
		List<PageData> varOList = staffdetailService.exportList(page);
		return export(varOList, "");
	}
	
	@SuppressWarnings("unchecked")
	private ModelAndView export(List<PageData> varOList, String ExcelName){
		ModelAndView mv = new ModelAndView();
		Map<String,Object> dataMap = new LinkedHashMap<String,Object>();
		dataMap.put("filename", ExcelName);
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
	
	 /**上报
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/report")
	public @ResponseBody CommonBase report() throws Exception{
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "report")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);

		PageData getPd = this.getPageData();
		//员工组 必须执行，用来设置汇总和传输上报类型
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		if(departSelf == 1){
			SelectedDepartCode = UserDepartCode;
		}
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");

		//判断选择为必须选择的
		String strGetCheckMustSelected = CheckMustSelectedAndSame(emplGroupType, SelectedCustCol7, SelectedDepartCode, true);
		if(strGetCheckMustSelected!=null && !strGetCheckMustSelected.trim().equals("")){
			commonBase.setCode(2);
			commonBase.setMessage(strGetCheckMustSelected);
			return commonBase;
		}
		
		String checkState = CheckState(SelectedCustCol7, SelectedDepartCode);
		if(checkState!=null && !checkState.trim().equals("")){
			commonBase.setCode(2);
			commonBase.setMessage(checkState);
		} else {
			FilterBillCode.copyInsert(syssealedinfoService, importdetailService, 
					SelectedDepartCode, SystemDateTime, SelectedCustCol7, 
					TypeCodeListen, TypeCodeSummy, TableNameSummy, TableNameDetail, 
					emplGroupType,
					map_HaveColumnsList);
			
			User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USERROL);
			String userId = user.getUSER_ID();
            String time = DateUtils.getCurrentTime(DateFormatUtils.DATE_FORMAT2);
			
			SysSealed item = new SysSealed();
			item.setBILL_CODE(" ");
			item.setRPT_DEPT(SelectedDepartCode);
			item.setRPT_DUR(SystemDateTime);
			item.setBILL_OFF(SelectedCustCol7);
			item.setRPT_USER(userId);
			item.setRPT_DATE(time);//YYYY-MM-DD HH:MM:SS
			item.setBILL_TYPE(TypeCodeDetail);// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
			item.setSTATE(DurState.Sealed.getNameKey());// 枚举  1封存,0解封
            List<SysSealed> listReport = new ArrayList<SysSealed>();
            listReport.add(item);
			syssealedinfoService.report(listReport);
			commonBase.setCode(0);
		}
		return commonBase;
	}
	
	private String CheckMustSelectedAndSame(String emplGroupType, String CUST_COL7, String DEPT_CODE, Boolean isCheckSame) throws Exception{
		String strRut = "";
		if(!(CUST_COL7 != null && !CUST_COL7.trim().equals(""))){
			strRut += "查询条件中的账套必须选择！";
		}
		if(!(DEPT_CODE != null && !DEPT_CODE.trim().equals(""))){
			strRut += "查询条件中的责任中心不能为空！";
		}
		if(isCheckSame){
			if(CUST_COL7 != null && !CUST_COL7.equals(getPageListSelectedCustCol7)){
				strRut += "查询条件中所选账套与页面显示数据账套不一致，请单击查询再进行操作！";
			}
			if(DEPT_CODE != null && !DEPT_CODE.equals(getPageListSelectedDepartCode)){
				strRut += "查询条件中所选责任中心与页面显示数据责任中心不一致，请单击查询再进行操作！";
			}
		}
		if(!(emplGroupType!=null && !emplGroupType.trim().equals(""))){
			strRut += "工资对应的员工组编码为空！";
		}
		return strRut;
	}

	private String CheckState(String CUST_COL7, String DEPT_CODE) throws Exception{
		String strRut = "选项卡对应的封存类型为空！";
		if(TypeCodeDetail != null && !TypeCodeDetail.trim().equals("")){
			strRut = "当前期间已封存！";
			if(CUST_COL7 != null && !CUST_COL7.trim().equals("") && DEPT_CODE != null && !DEPT_CODE.trim().equals("")){
				//封存状态,取自tb_sys_sealed_info表state字段, 数据操作需要前提为当前明细数据未封存，如果已确认封存，则明细数据不能再进行操作。
				PageData statePd = new PageData();
				statePd.put("BILL_OFF", CUST_COL7);
				statePd.put("RPT_DEPT", DEPT_CODE);
				statePd.put("RPT_DUR", SystemDateTime);
				statePd.put("BILL_TYPE", TypeCodeDetail);
				String State = syssealedinfoService.getState(statePd);
				if(!DurState.Sealed.getNameKey().equals(State)){// 枚举  1封存,0解封
					strRut = "";
				}
			}
		}
		return strRut;
	}

	private String getWhileValue(String value){
        String which = DefaultWhile;
		if(value != null && !value.trim().equals("")){
			which = value;
		}
		//枚举类型 TmplType
		TypeCodeDetail = "";
		TypeCodeSummy = "";
		TypeCodeListen = "";
		if(which.equals(TmplType.TB_STAFF_DETAIL_CONTRACT.getNameKey())){
			//合同化
			TypeCodeDetail = TmplType.TB_STAFF_DETAIL_CONTRACT.getNameKey();
			TypeCodeSummy = TmplType.TB_STAFF_SUMMY_CONTRACT.getNameKey();
			TypeCodeListen = TmplType.TB_STAFF_TRANSFER_CONTRACT.getNameKey();
		}
		if(which.equals(TmplType.TB_STAFF_DETAIL_MARKET.getNameKey())){
			//市场化
			TypeCodeDetail = TmplType.TB_STAFF_DETAIL_MARKET.getNameKey();
			TypeCodeSummy = TmplType.TB_STAFF_SUMMY_MARKET.getNameKey();
			TypeCodeListen = TmplType.TB_STAFF_TRANSFER_MARKET.getNameKey();
		}
		if(which.equals(TmplType.TB_STAFF_DETAIL_SYS_LABOR.getNameKey())){
			//系统内劳务
			TypeCodeDetail = TmplType.TB_STAFF_DETAIL_SYS_LABOR.getNameKey();
			TypeCodeSummy = TmplType.TB_STAFF_SUMMY_SYS_LABOR.getNameKey();
			TypeCodeListen = TmplType.TB_STAFF_TRANSFER_SYS_LABOR.getNameKey();
		}
		if(which.equals(TmplType.TB_STAFF_DETAIL_OPER_LABOR.getNameKey())){
			//运行人员
			TypeCodeDetail = TmplType.TB_STAFF_DETAIL_OPER_LABOR.getNameKey();
			TypeCodeSummy = TmplType.TB_STAFF_SUMMY_OPER_LABOR.getNameKey();
			TypeCodeListen = TmplType.TB_STAFF_TRANSFER_OPER_LABOR.getNameKey();
		}
		if(which.equals(TmplType.TB_STAFF_DETAIL_LABOR.getNameKey())){
			//劳务派遣工资
			TypeCodeDetail = TmplType.TB_STAFF_DETAIL_LABOR.getNameKey();
			TypeCodeSummy = TmplType.TB_STAFF_SUMMY_LABOR.getNameKey();
			TypeCodeListen = TmplType.TB_STAFF_TRANSFER_LABOR.getNameKey();
		}
		return which;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
