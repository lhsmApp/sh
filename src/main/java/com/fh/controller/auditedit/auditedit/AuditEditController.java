package com.fh.controller.auditedit.auditedit;

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
import com.fh.controller.common.QueryFeildString;
import com.fh.controller.common.TmplUtil;
import com.fh.entity.CommonBase;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.TableColumns;
import com.fh.entity.TmplConfigDetail;
import com.fh.exception.CustomException;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.enums.TmplType;
import com.fh.util.excel.LeadingInExcelToPageData;

import net.sf.json.JSONArray;

import com.fh.util.Jurisdiction;
import com.fh.service.auditedit.auditedit.AuditEditManager;
import com.fh.service.fhoa.department.impl.DepartmentService;
import com.fh.service.sysConfig.sysconfig.SysConfigManager;
import com.fh.service.sysSealedInfo.syssealedinfo.impl.SysSealedInfoService;
import com.fh.service.system.dictionaries.impl.DictionariesService;
import com.fh.service.system.user.UserManager;
import com.fh.service.tmplConfigDict.tmplconfigdict.impl.TmplConfigDictService;
import com.fh.service.tmplconfig.tmplconfig.impl.TmplConfigService;

/** 
 * 说明： 对账数据编辑
 * 创建人：zhangxiaoliu
 * 创建时间：2017-07-26
 * @version
 */
@Controller
@RequestMapping(value="/auditedit")
public class AuditEditController extends BaseController {
	
	String menuUrl = "auditedit/list.do"; //菜单地址(权限用)
	@Resource(name="auditeditService")
	private AuditEditManager auditeditService;
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
	
	//默认的which值
	String DefaultWhile = TmplType.TB_STAFF_AUDIT_CONTRACT.getNameKey();

	//页面显示数据的年月
	String SystemDateTime = "";
	//页面显示数据的二级单位
	String DepartCode = "01001";
	//底行显示的求和与平均值字段
	StringBuilder SqlUserdata = new StringBuilder();
	//字典
	Map<String, Object> DicList = new LinkedHashMap<String, Object>();
	//表结构  
	Map<String, TableColumns> map_HaveColumnsList = new LinkedHashMap<String, TableColumns>();
	// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
	Map<String, TmplConfigDetail> map_SetColumnsList = new LinkedHashMap<String, TmplConfigDetail>();

	// 设置必定不用编辑的列
	List<String> MustNotEditList = Arrays.asList("BILL_CODE", "BUSI_DATE", "CUST_COL7", "USER_GROP");
	//界面查询字段
    List<String> QueryFeildList = Arrays.asList("DEPT_CODE", "CUST_COL7", "USER_GROP");
	// 查询表的主键字段，作为标准列，jqgrid添加带__列，mybaits获取带__列
	List<String> keyListBase = Arrays.asList("BUSI_DATE", "USER_CODE", "CUST_COL7", "USER_GROP");
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表AuditEdit");
		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		//当前期间,取自tb_system_config的SystemDateTime字段
		SystemDateTime = sysConfigManager.currentSection(getPd);

		ModelAndView mv = this.getModelAndView();
		mv.setViewName("auditedit/auditedit/auditedit_list");
		//while
		getPd.put("which", SelectedTableNo);
		mv.addObject("pd", getPd);
		
		//DEPT_CODE
		mv.addObject("zTreeNodes", DictsUtil.getDepartmentSelectTreeSource(departmentService));
		//CUST_COL7 FMISACC 帐套字典
		mv.addObject("FMISACC", DictsUtil.getDictsByParentCode(dictionariesService, "FMISACC"));
		
		setMustNotEditList(SelectedTableNo);
		TmplUtil tmpl = new TmplUtil(tmplconfigService, tmplconfigdictService, dictionariesService, departmentService,userService, keyListBase);
		tmpl.setMustNotEditList(MustNotEditList);
		String jqGridColModel = tmpl.generateStructure(SelectedTableNo, DepartCode, 3);
		
		SqlUserdata = tmpl.getSqlUserdata();
		//字典
		DicList = tmpl.getDicList();
		//表结构  
		map_HaveColumnsList = tmpl.getHaveColumnsList();
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		map_SetColumnsList = tmpl.getSetColumnsList();
		
		mv.addObject("jqGridColModel", jqGridColModel);
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表AuditEdit");
		
		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");
		
		String tableName = getTableCode(SelectedTableNo);
		
		PageData getQueryFeildPd = new PageData();
		//工资分的类型, 只有工资返回值
		getQueryFeildPd.put("USER_GROP", emplGroupType);
		getQueryFeildPd.put("DEPT_CODE", SelectedDepartCode);
		getQueryFeildPd.put("CUST_COL7", SelectedCustCol7);
		String QueryFeild = QueryFeildString.getQueryFeild(getQueryFeildPd, QueryFeildList);
		//工资无账套无数据
		if(CheckStaffOrNot(SelectedTableNo)){
			if(!(SelectedCustCol7!=null && !SelectedCustCol7.trim().equals(""))){
				QueryFeild += " and 1 != 1 ";
			}
		}
		if(QueryFeild!=null && !QueryFeild.equals("")){
			getPd.put("QueryFeild", QueryFeild);
		}
		//多条件过滤条件
		String filters = getPd.getString("filters");
		if(null != filters && !"".equals(filters)){
			getPd.put("filterWhereResult", SqlTools.constructWhere(filters,null));
		}
		//页面显示数据的年月
		getPd.put("SystemDateTime", SystemDateTime);
		//表名
		getPd.put("TableName", tableName);
		String strFieldSelectKey = QueryFeildString.getFieldSelectKey(keyListBase, TmplUtil.keyExtra);
		if(null != strFieldSelectKey && !"".equals(strFieldSelectKey.trim())){
			getPd.put("FieldSelectKey", strFieldSelectKey);
		}
		page.setPd(getPd);
		List<PageData> varList = auditeditService.JqPage(page);	//列出Betting列表
		int records = auditeditService.countJqGridExtend(page);
		PageData userdata = null;
		if(SqlUserdata!=null && !SqlUserdata.toString().trim().equals("")){
			//底行显示的求和与平均值字段
			getPd.put("Userdata", SqlUserdata.toString());
			userdata = auditeditService.getFooterSummary(page);
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
		logBefore(logger, Jurisdiction.getUsername()+"修改AuditEdit");

		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");
		
		String tableName = getTableCode(SelectedTableNo);
		//操作
		String oper = getPd.getString("oper");

		getPd.put("StaffOrNot", "");
		getPd.put("BUSI_DATE", SystemDateTime);
		//工资无账套无数据
		if(CheckStaffOrNot(SelectedTableNo)){
			getPd.put("StaffOrNot", "true");
			if(!(SelectedCustCol7!=null && !SelectedCustCol7.trim().equals(""))){
				commonBase.setCode(2);
				commonBase.setMessage("工资必须选择账套！");
		        return commonBase;
			}
			if(oper.equals("add")){
				getPd.put("CUST_COL7", SelectedCustCol7);
				getPd.put("USER_GROP", emplGroupType);
			}
		}
		TmplUtil.setModelDefault(getPd, map_HaveColumnsList);
		//表名
		getPd.put("TableName", tableName);
		
		List<PageData> listData = new ArrayList<PageData>();
		listData.add(getPd);
		PageData pdFindByModel = new PageData();
		pdFindByModel.put("TableName", tableName);
		pdFindByModel.put("ListData", listData);
		List<PageData> repeatList = auditeditService.findByModel(pdFindByModel);
		if(repeatList!=null && repeatList.size()>0){
			commonBase.setCode(2);
			commonBase.setMessage("此区间内编码已存在！");
		} else {
            auditeditService.deleteUpdateAll(listData);
			commonBase.setCode(0);
		}
		return commonBase;
	}
	
	 /**批量修改
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/updateAll")
	public @ResponseBody CommonBase updateAll() throws Exception{
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);

		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");
		
		Boolean isStaffOrNot = CheckStaffOrNot(SelectedTableNo);
		
		String tableName = getTableCode(SelectedTableNo);
		
		Object DATA_ROWS = getPd.get("DATA_ROWS");
		String json = DATA_ROWS.toString();  
        JSONArray array = JSONArray.fromObject(json);  
        List<PageData> listData = (List<PageData>) JSONArray.toCollection(array,PageData.class);
        List<String> listUserCodeAdd = new ArrayList<String>();
        for(PageData item : listData){
        	String strUserCode = item.getString("USER_CODE__");
        	if(listUserCodeAdd.contains(strUserCode)){
				commonBase.setCode(2);
				commonBase.setMessage("此区间内编码重复:" + strUserCode);
				return commonBase;
        	}
			item.put("StaffOrNot", "");
    		if(isStaffOrNot){
    			item.put("StaffOrNot", "true");
    		}
        	item.put("BUSI_DATE", SystemDateTime);
			TmplUtil.setModelDefault(item, map_HaveColumnsList);
			//表名
			item.put("TableName", tableName);
        }
		if(null != listData && listData.size() > 0){
			PageData pdFindByModel = new PageData();
			pdFindByModel.put("TableName", tableName);
			pdFindByModel.put("ListData", listData);
			List<PageData> repeatList = auditeditService.findByModel(pdFindByModel);
			if(repeatList!=null && repeatList.size()>0){
				commonBase.setCode(2);
				commonBase.setMessage("此区间内编码已存在！");
			} else {
				auditeditService.deleteUpdateAll(listData);
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
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "delete")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);

		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");
		
		Boolean isStaffOrNot = CheckStaffOrNot(SelectedTableNo);
		
		String tableName = getTableCode(SelectedTableNo);

		Object DATA_ROWS = getPd.get("DATA_ROWS");
		String json = DATA_ROWS.toString();  
        JSONArray array = JSONArray.fromObject(json);  
        List<PageData> listData = (List<PageData>) JSONArray.toCollection(array,PageData.class);
        for(PageData item : listData){
			item.put("StaffOrNot", "");
    		if(isStaffOrNot){
    			item.put("StaffOrNot", "true");
    		}
			//表名
			item.put("TableName", tableName);
        }
        if(null != listData && listData.size() > 0){
			PageData pdDeleteAll = new PageData();
			pdDeleteAll.put("TableName", tableName);
			pdDeleteAll.put("ListData", listData);
			auditeditService.deleteAll(pdDeleteAll);
			commonBase.setCode(0);
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
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");

		//工资账套为必须选择的
		if(CheckStaffOrNot(SelectedTableNo)){
			if(!(SelectedCustCol7!=null && !SelectedCustCol7.trim().equals(""))){
				commonBase.setCode(2);
				commonBase.setMessage("工资必须选择账套！");
			}
		}
		
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("common/uploadExcel");
		mv.addObject("local", "auditedit");
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
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");
		
		String tableName = getTableCode(SelectedTableNo);

		//工资账套为必须选择的
		if(CheckStaffOrNot(SelectedTableNo) && !(SelectedCustCol7!=null && !SelectedCustCol7.trim().equals(""))){
			commonBase.setCode(2);
			commonBase.setMessage("工资必须选择账套！");
		} else {
			if(!(SystemDateTime!=null && !SystemDateTime.trim().equals(""))){
				commonBase.setCode(2);
				commonBase.setMessage("当前区间不能为空！");
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
						List<String> listUserCode = new ArrayList<String>();
						//工资 获取数据库中不是本部门、员工组和账套中的UserCode
						if(CheckStaffOrNot(SelectedTableNo)){
							//获取数据库中不是本部门、员工组和账套中的UserCode
							PageData pdHaveFeild = new PageData();
							pdHaveFeild.put("SystemDateTime", SystemDateTime);
							pdHaveFeild.put("SelectedDepartCode", SelectedDepartCode);
							pdHaveFeild.put("SelectedCustCol7", SelectedCustCol7);
							pdHaveFeild.put("emplGroupType", emplGroupType);
							listUserCode = auditeditService.exportHaveUserCode(pdHaveFeild);
						}
						int listSize = uploadAndRead.size();
						for(int i=0;i<listSize;i++){
							uploadAndRead.get(i).put("StaffOrNot", "");
							//工资 
							if(CheckStaffOrNot(SelectedTableNo)){
								//工资 设置删除本账套和员工组数据
								uploadAndRead.get(i).put("StaffOrNot", true);
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
							}
							String getBUSI_DATE = (String) uploadAndRead.get(i).get("BUSI_DATE");
							String getUSER_CODE = (String) uploadAndRead.get(i).get("USER_CODE");
							if(!(getBUSI_DATE!=null && !getBUSI_DATE.trim().equals(""))){
								uploadAndRead.get(i).put("BUSI_DATE", SystemDateTime);
								getBUSI_DATE = SystemDateTime;
							}
							if(!SystemDateTime.equals(getBUSI_DATE)){
								if(!sbRet.contains("导入区间和当前区间必须一致！")){
									sbRet.add("导入区间和当前区间必须一致！");
								}
							}
							if(!(getUSER_CODE!=null && !getUSER_CODE.trim().equals(""))){
								if(!sbRet.contains("人员编码不能为空！")){
									sbRet.add("人员编码不能为空！");
								}
							}
							if(listUserCode.contains(getUSER_CODE.trim())){
								String strUserAdd = "编码" + getUSER_CODE + "重复！";
								if(!sbRet.contains(strUserAdd)){
									sbRet.add(strUserAdd);
								}
							} else {
								listUserCode.add(getUSER_CODE.trim());
							}
							String getESTB_DEPT = (String) uploadAndRead.get(i).get("ESTB_DEPT");
							if(!(getESTB_DEPT!=null && !getESTB_DEPT.trim().equals(""))){
								uploadAndRead.get(i).put("ESTB_DEPT", Jurisdiction.getCurrentDepartmentID());
							}
							TmplUtil.setModelDefault(uploadAndRead.get(i), map_HaveColumnsList);
							//表名
							uploadAndRead.get(i).put("TableName", tableName);
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
							auditeditService.batchImport(uploadAndRead);
							commonBase.setCode(0);
						}
					} else {
						commonBase.setCode(-1);
						commonBase.setMessage("TranslateUtil");
					}
				}
			}
		}
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("common/uploadExcel");
		mv.addObject("local", "auditedit");
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
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");
		
		String QueryFeild = "";
		if(SelectedDepartCode!=null && !SelectedDepartCode.trim().equals("")){
			QueryFeild += " and DEPT_CODE = '" + SelectedDepartCode + "' ";
		}

		String tableName = getTableCode(SelectedTableNo);

		//表名
		getPd.put("TableName", tableName);
		if(CheckStaffOrNot(SelectedTableNo)){
			if(emplGroupType!=null && !emplGroupType.trim().equals("")
					&& SelectedCustCol7!=null && !SelectedCustCol7.trim().equals("")){
				QueryFeild += " and USER_GROP = '" + emplGroupType + "' ";
				QueryFeild += " and CUST_COL7 = '" + SelectedCustCol7 + "' ";
			} else {
				QueryFeild += " and 1 != 1 ";
			}
		}
		getPd.put("QueryFeild", QueryFeild);
		page.setPd(getPd);
		
		List<PageData> varOList = auditeditService.exportModel(page);
		return export(varOList, "AuditEdit"); //工资明细
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出AuditEdit到excel");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		PageData getPd = this.getPageData();
		//员工组
		String SelectedTableNo = getWhileValue(getPd.getString("SelectedTableNo"));
		String emplGroupType = DictsUtil.getEmplGroupType(SelectedTableNo);
		//单位
		String SelectedDepartCode = getPd.getString("SelectedDepartCode");
		//账套
		String SelectedCustCol7 = getPd.getString("SelectedCustCol7");
		
		String QueryFeild = "";
		if(SelectedDepartCode!=null && !SelectedDepartCode.trim().equals("")){
			QueryFeild += " and DEPT_CODE = '" + SelectedDepartCode + "' ";
		}

		String tableName = getTableCode(SelectedTableNo);
		
		//页面显示数据的年月
		getPd.put("SystemDateTime", SystemDateTime);
		//表名
		getPd.put("TableName", tableName);
		if(CheckStaffOrNot(SelectedTableNo)){
			if(emplGroupType!=null && !emplGroupType.trim().equals("")
					&& SelectedCustCol7!=null && !SelectedCustCol7.trim().equals("")){
				QueryFeild += " and USER_GROP = '" + emplGroupType + "' ";
				QueryFeild += " and CUST_COL7 = '" + SelectedCustCol7 + "' ";
			} else {
				QueryFeild += " and 1 != 1 ";
			}
		}
		getPd.put("QueryFeild", QueryFeild);
		page.setPd(getPd);
		List<PageData> varOList = auditeditService.exportList(page);
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
	
	private String getWhileValue(String value){
        String which = DefaultWhile;
		if(value != null && !value.trim().equals("")){
			which = value;
		}
		return which;
	}

	/**
	 * 根据前端业务表索引获取表名称
	 * 
	 * @param which
	 * @return
	 */
	private void setMustNotEditList(String which) {
		// 设置必定不用编辑的列
	    MustNotEditList = new ArrayList<String>();
		if (which != null){
			if (which.equals(TmplType.TB_STAFF_AUDIT_CONTRACT.getNameKey())
                    ||which.equals(TmplType.TB_STAFF_AUDIT_MARKET.getNameKey())
					||which.equals(TmplType.TB_STAFF_AUDIT_SYS_LABOR.getNameKey())
					||which.equals(TmplType.TB_STAFF_AUDIT_OPER_LABOR.getNameKey())
					||which.equals(TmplType.TB_STAFF_AUDIT_LABOR.getNameKey())) {
				MustNotEditList = Arrays.asList("BILL_CODE", "BUSI_DATE", "CUST_COL7", "USER_GROP");
			} else if (which.equals(TmplType.TB_SOCIAL_INC_AUDIT.getNameKey())
					||which.equals(TmplType.TB_HOUSE_FUND_AUDIT.getNameKey())) {
				MustNotEditList = Arrays.asList("BILL_CODE", "BUSI_DATE");
			}
		}
	}
	private String getTableCode(String which) {
		String tableCode = "";
		if (which != null){
			if (which.equals(TmplType.TB_STAFF_AUDIT_CONTRACT.getNameKey())
                    ||which.equals(TmplType.TB_STAFF_AUDIT_MARKET.getNameKey())
					||which.equals(TmplType.TB_STAFF_AUDIT_SYS_LABOR.getNameKey())
					||which.equals(TmplType.TB_STAFF_AUDIT_OPER_LABOR.getNameKey())
					||which.equals(TmplType.TB_STAFF_AUDIT_LABOR.getNameKey())) {
				tableCode = "tb_staff_audit";
			} else if (which.equals(TmplType.TB_SOCIAL_INC_AUDIT.getNameKey())) {
				tableCode = "tb_social_inc_audit";
			} else if (which.equals(TmplType.TB_HOUSE_FUND_AUDIT.getNameKey())) {
				tableCode = "tb_house_fund_audit";
			}
		}
		return tableCode;
	}
	private Boolean CheckStaffOrNot(String which) {
		if (which != null){
			if (which.equals(TmplType.TB_STAFF_AUDIT_CONTRACT.getNameKey())
                    ||which.equals(TmplType.TB_STAFF_AUDIT_MARKET.getNameKey())
					||which.equals(TmplType.TB_STAFF_AUDIT_SYS_LABOR.getNameKey())
					||which.equals(TmplType.TB_STAFF_AUDIT_OPER_LABOR.getNameKey())
					||which.equals(TmplType.TB_STAFF_AUDIT_LABOR.getNameKey())) {
				return true;
			}
		}
		return false;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
