package com.fh.controller.houseFundDetail.housefunddetail;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.fh.controller.base.BaseController;
import com.fh.controller.common.TmplUtil;
import com.fh.entity.CommonBase;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.HouseFundDetailModel;
import com.fh.entity.SysSealed;
import com.fh.entity.TmplConfigDetail;
import com.fh.entity.system.User;
import com.fh.exception.CustomException;
import com.fh.util.Const;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.date.DateFormatUtils;
import com.fh.util.date.DateUtils;
import com.fh.util.enums.BillType;
import com.fh.util.enums.DurState;
import com.fh.util.Jurisdiction;
import com.fh.util.excel.LeadingInExcel;

import net.sf.json.JSONArray;

import com.fh.service.fhoa.department.impl.DepartmentService;
import com.fh.service.houseFundDetail.housefunddetail.HouseFundDetailManager;
import com.fh.service.sysConfig.sysconfig.SysConfigManager;
import com.fh.service.sysSealedInfo.syssealedinfo.impl.SysSealedInfoService;
import com.fh.service.system.dictionaries.impl.DictionariesService;
import com.fh.service.tmplConfigDict.tmplconfigdict.impl.TmplConfigDictService;
import com.fh.service.tmplconfig.tmplconfig.impl.TmplConfigService;

/** 
 * 说明： 公积金明细
 * 创建人：zhangxiaoliu
 * 创建时间：2017-06-30
 */
@Controller
@RequestMapping(value="/housefunddetail")
public class HouseFundDetailController extends BaseController {
	
	String menuUrl = "housefunddetail/list.do"; //菜单地址(权限用)
	@Resource(name="housefunddetailService")
	private HouseFundDetailManager housefunddetailService;
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
	
	//表名
	String TableName = "tb_house_fund_detail";
	//枚举类型  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
	String TypeCode = BillType.GOLD_DETAIL.getNameKey();
	
	//页面显示数据的年月
	String SystemDateTime = "";
	//页面显示数据的二级单位
	String DepartCode = "";
	//底行显示的求和与平均值字段
	StringBuilder SqlUserdata = new StringBuilder();
	//默认值
	Map<String, Object> DefaultValueList = new HashMap<String, Object>();
	//字典
	Map<String, Object> DicList = new HashMap<String, Object>();
	//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
	List<TmplConfigDetail> ColumnsList = new ArrayList<TmplConfigDetail>();
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表HouseFundDetail");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		PageData pd = this.getPageData();
		
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("houseFundDetail/housefunddetail/housefunddetail_list");
		//当前期间,取自tb_system_config的SystemDateTime字段
		SystemDateTime = sysConfigManager.currentSection(pd);
		mv.addObject("SystemDateTime", SystemDateTime);
		//当前登录人所在二级单位
		DepartCode = Jurisdiction.getCurrentDepartmentID();//
		User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USERROL);
		String DepartName = user.getDEPARTMENT_NAME();
		mv.addObject("DepartName", DepartName);
		//封存状态,取自tb_sys_sealed_info表state字段, 数据操作需要前提为当前明细数据未封存，如果已确认封存，则明细数据不能再进行操作。
		pd.put("RPT_DEPT", DepartCode);
		pd.put("RPT_DUR", SystemDateTime);
		pd.put("BILL_TYPE", TypeCode);// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
		String State = syssealedinfoService.getState(pd);
		mv.addObject("State", State.equals(DurState.Release.getNameKey())? true:false);// 枚举  1封存,0解封
		List<String> userCodeList = housefunddetailService.getHaveUserCodeDic(pd);
		mv.addObject("userCodeList", userCodeList);
		
		TmplUtil tmpl = new TmplUtil(tmplconfigService, tmplconfigdictService, dictionariesService, departmentService);
		String jqGridColModel = tmpl.generateStructure(TableName, DepartCode, 3);
		
		SqlUserdata = tmpl.getSqlUserdata();
		//默认值
		DefaultValueList = tmpl.getDefaultValueList();
		//字典
		DicList = tmpl.getDicList();
		//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		ColumnsList = tmpl.getColumnsList();
		
		mv.addObject("jqGridColModel", jqGridColModel);
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表HouseFundDetail");
		PageData pd = new PageData();
		pd = this.getPageData();
		//
		String UserCode = pd.getString("UserCode");	
		if(null != UserCode && !"".equals(UserCode)){
			pd.put("UserCode", UserCode.trim());
		}
		//多条件过滤条件
		String filters = pd.getString("filters");
		if(null != filters && !"".equals(filters)){
			pd.put("filterWhereResult", SqlTools.constructWhere(filters,null));
		}
		//页面显示数据的年月
		pd.put("SystemDateTime", SystemDateTime);
		//页面显示数据的二级单位
		pd.put("DepartCode", DepartCode);
		page.setPd(pd);
		List<PageData> varList = housefunddetailService.JqPage(page);	//列出Betting列表
		int records = housefunddetailService.countJqGridExtend(page);
		PageData userdata = null;
		if(SqlUserdata!=null && !SqlUserdata.toString().trim().equals("")){
			//底行显示的求和与平均值字段
			pd.put("Userdata", SqlUserdata.toString());
		    userdata=housefunddetailService.getFooterSummary(page);
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
		logBefore(logger, Jurisdiction.getUsername()+"修改HouseFundDetail");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限

		PageData pd = this.getPageData();
		String checkState = CheckState(pd);
		if(checkState!=null && checkState.trim() != ""){
			commonBase.setCode(2);
			commonBase.setMessage(checkState);
		} else {
			TmplUtil.setModelDefault(pd, HouseFundDetailModel.class, DefaultValueList);
			String BILL_CODE = "BILL_CODE";
			String BUSI_DATE = "BUSI_DATE";
			String DEPT_CODE = "DEPT_CODE";
			if(pd.containsKey(BILL_CODE)){
				pd.remove(BILL_CODE);
			}
			pd.put(BILL_CODE, " ");
			if(pd.containsKey(BUSI_DATE)){
				pd.remove(BUSI_DATE);
			}
			pd.put(BUSI_DATE, SystemDateTime);
			if(pd.containsKey(DEPT_CODE)){
				pd.remove(DEPT_CODE);
			}
			pd.put(DEPT_CODE, DepartCode);
			
			List<HouseFundDetailModel> repeatList = housefunddetailService.findByPd(pd);
			if(repeatList!=null && repeatList.size()>0){
				commonBase.setCode(2);
				commonBase.setMessage("此区间内编码已存在！");
			} else {
				if(pd.getString("oper").equals("edit")){
					housefunddetailService.edit(pd);
					commonBase.setCode(0);
				}
				else if(pd.getString("oper").equals("add")){
					housefunddetailService.save(pd);
					commonBase.setCode(0);
				}
			}
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
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);

		PageData pd = this.getPageData();
		String checkState = CheckState(pd);
		if(checkState!=null && checkState.trim() != ""){
			commonBase.setCode(2);
			commonBase.setMessage(checkState);
		} else {
			Object DATA_ROWS = pd.get("DATA_ROWS");
			String json = DATA_ROWS.toString();  
	        JSONArray array = JSONArray.fromObject(json);  
	        List<HouseFundDetailModel> listData = (List<HouseFundDetailModel>) JSONArray.toCollection(array,HouseFundDetailModel.class);
	        for(HouseFundDetailModel item : listData){
	        	item.setBILL_CODE(" ");
	        	item.setBUSI_DATE(SystemDateTime);
	        	item.setDEPT_CODE(DepartCode);
	        }
			if(null != listData && listData.size() > 0){
				List<HouseFundDetailModel> repeatList = housefunddetailService.findByModel(listData);
				if(repeatList!=null && repeatList.size()>0){
					commonBase.setCode(2);
					commonBase.setMessage("此区间内编码已存在！");
				} else {
					housefunddetailService.updateAll(listData);
					commonBase.setCode(0);
				}
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

		PageData pd = this.getPageData();
		String checkState = CheckState(pd);
		if(checkState!=null && checkState.trim() != ""){
			commonBase.setCode(2);
			commonBase.setMessage(checkState);
		} else {
			Object DATA_ROWS = pd.get("DATA_ROWS");
			String json = DATA_ROWS.toString();  
	        JSONArray array = JSONArray.fromObject(json);  
	        List<HouseFundDetailModel> listData = (List<HouseFundDetailModel>) JSONArray.toCollection(array,HouseFundDetailModel.class);
	        if(null != listData && listData.size() > 0){
				housefunddetailService.deleteAll(listData);
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
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("houseFundDetail/housefunddetail/uploadExcel");
		return mv;
	}

	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/readExcel")
	//public @ResponseBody CommonBase readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
	public ModelAndView readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;}//校验权限

		PageData pd = this.getPageData();
		String checkState = CheckState(pd);
		if(checkState!=null && checkState.trim() != ""){
			commonBase.setCode(2);
			commonBase.setMessage(checkState);
		} else {
			// 局部变量
			LeadingInExcel<HouseFundDetailModel> testExcel = null;
			Map<Integer, Object> uploadAndReadMap = null;
			try {
				// 定义需要读取的数据
				String formart = "yyyy-MM-dd";
				String propertiesFileName = "config";
				String kyeName = "file_path";
				int sheetIndex = 0;
				Class<HouseFundDetailModel> clazz = HouseFundDetailModel.class;
				Map<String, String> titleAndAttribute = null;
				// 定义对应的标题名与对应属性名
				titleAndAttribute = new HashMap<String, String>();
				
				//配置表设置列
				if(ColumnsList != null && ColumnsList.size() > 0){
					for(int i=0; i < ColumnsList.size(); i++){
						titleAndAttribute.put(ColumnsList.get(i).getCOL_NAME(), ColumnsList.get(i).getCOL_CODE());
					}
				}

				// 调用解析工具包
				testExcel = new LeadingInExcel<HouseFundDetailModel>(formart);
				// 解析excel，获取客户信息集合

				uploadAndReadMap = testExcel.uploadAndRead(file, propertiesFileName, kyeName, sheetIndex,
						titleAndAttribute, clazz, ColumnsList, DicList);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("读取Excel文件错误", e);
				throw new CustomException("读取Excel文件错误");
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
				List<HouseFundDetailModel> uploadAndRead = (List<HouseFundDetailModel>) uploadAndReadMap.get(2);
				if (uploadAndRead != null && !"[]".equals(uploadAndRead.toString()) && uploadAndRead.size() >= 1) {
					judgement = true;
				}
				if (judgement) {
					List<String> sbRet = new ArrayList<String>();
					String BUSI_DATE = "";
					String DEPT_CODE = "";
					List<String> listUserCode = new ArrayList<String>();
					int listSize = uploadAndRead.size();
					for(int i=0;i<listSize;i++){
						if(!(uploadAndRead.get(i).getBUSI_DATE()!=null && !uploadAndRead.get(i).getBUSI_DATE().trim().equals(""))){
							if(!sbRet.contains("区间不能为空！")){
								sbRet.add("区间不能为空！");
							}
						}
						if(!(uploadAndRead.get(i).getDEPT_CODE()!=null && !uploadAndRead.get(i).getDEPT_CODE().trim().equals(""))){
							if(!sbRet.contains("单位不能为空！")){
								sbRet.add("单位不能为空！");
							}
						}
						if(!(uploadAndRead.get(i).getUSER_CODE()!=null && !uploadAndRead.get(i).getUSER_CODE().trim().equals(""))){
							if(!sbRet.contains("人员编码不能为空！")){
								sbRet.add("人员编码不能为空！");
							}
						}
						if(i == 0){
							BUSI_DATE = uploadAndRead.get(i).getBUSI_DATE();
							DEPT_CODE = uploadAndRead.get(i).getDEPT_CODE();
						} else {
							if(!BUSI_DATE.equals(uploadAndRead.get(i).getBUSI_DATE()) || !DEPT_CODE.equals(uploadAndRead.get(i).getDEPT_CODE())){
								if(!sbRet.contains("区间和单位必须一致！")){
									sbRet.add("区间和单位必须一致！");
								}
							}
						}
						if(listUserCode.contains(uploadAndRead.get(i).getUSER_CODE())){
							String strUserAdd = "编码" + uploadAndRead.get(i).getUSER_CODE() + "重复！";
							if(!sbRet.contains(strUserAdd)){
								sbRet.add(strUserAdd);
							}
						} else {
							listUserCode.add(uploadAndRead.get(i).getUSER_CODE());
						}
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
						housefunddetailService.batchImport(uploadAndRead);
						commonBase.setCode(0);
					}
					
					/*// 把客户信息分为没100条数据为一组迭代添加客户信息（注：将customerList集合作为参数，在Mybatis的相应映射文件中使用foreach标签进行批量添加。）
					int listSize = uploadAndRead.size();
					int toIndex = 100;
					for (int i = 0; i < listSize; i += 100) {
						if (i + 100 > listSize) {
							toIndex = listSize - i;
						}
						List<HouseFundDetailModel> subList = uploadAndRead.subList(i, i + toIndex);

						//此处执行集合添加 
						housefunddetailService.batchImport(subList);
					}*/
				} else {
					commonBase.setCode(-1);
					commonBase.setMessage("TranslateUtil");
				}
			}
		}
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("houseFundDetail/housefunddetail/uploadExcel");
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
		//页面显示数据的二级单位
		List<PageData> varOList = housefunddetailService.exportModel(DepartCode);
		return export(varOList, "HouseFundDetail"); //公积金明细
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出HouseFundDetail到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		PageData pd = this.getPageData();
		//页面显示数据的年月
		pd.put("SystemDateTime", SystemDateTime);
		//页面显示数据的二级单位
		pd.put("DepartCode", DepartCode);
		page.setPd(pd);
		List<PageData> varOList = housefunddetailService.exportList(page);
		return export(varOList, "");
	}
	
	@SuppressWarnings("unchecked")
	private ModelAndView export(List<PageData> varOList, String ExcelName){
		ModelAndView mv = new ModelAndView();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("filename", ExcelName);
		List<String> titles = new ArrayList<String>();
		if(ColumnsList != null && ColumnsList.size() > 0){
			for(int i=0; i < ColumnsList.size(); i++){
				titles.add(ColumnsList.get(i).getCOL_NAME());
			}
		}
		dataMap.put("titles", titles);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			if(ColumnsList != null && ColumnsList.size() > 0){
				for(int j=1; j <= ColumnsList.size(); j++){
					String trans = ColumnsList.get(j-1).getDICT_TRANS();
					Object getCellValue = varOList.get(i).get(ColumnsList.get(j-1).getCOL_CODE().toUpperCase());
					if(trans != null && !trans.trim().equals("")){
						String value = "";
						Map<String, String> dicAdd = (Map<String, String>) DicList.getOrDefault(trans, new HashMap<String, String>());
						value = dicAdd.getOrDefault(getCellValue, "");
						vpd.put("var" + j, value);
					} else {
						vpd.put("var" + j, getCellValue.toString());
					}
				}
			}
			varList.add(vpd);
		}
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

		PageData pd = this.getPageData();
		String checkState = CheckState(pd);
		if(checkState!=null && checkState.trim() != ""){
			commonBase.setCode(2);
			commonBase.setMessage(checkState);
		} else {
			User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USERROL);
			String userId = user.getUSER_ID();
            String time = DateUtils.getCurrentTime(DateFormatUtils.DATE_FORMAT2);
			
			SysSealed item = new SysSealed();
			item.setBILL_CODE(" ");
			item.setRPT_DEPT(DepartCode);
			item.setRPT_DUR(SystemDateTime);
			item.setRPT_USER(userId);
			item.setRPT_DATE(time);//YYYY-MM-DD HH:MM:SS
			item.setBILL_TYPE(TypeCode.toString());// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
			item.setSTATE(DurState.Sealed.getNameKey());// 枚举  1封存,0解封
			syssealedinfoService.report(item);
			commonBase.setCode(0);
		}
		return commonBase;
	}
	
	private String CheckState(PageData pd) throws Exception{
		String strRut = "当前期间已封存！";
		pd.put("RPT_DEPT", DepartCode);
		pd.put("RPT_DUR", SystemDateTime);
		pd.put("BILL_TYPE", TypeCode);// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
		String State = syssealedinfoService.getState(pd);
		if(State!=null && State.equals(DurState.Release.getNameKey())){// 枚举  1封存,0解封
			strRut = "";
		}
		return strRut;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
