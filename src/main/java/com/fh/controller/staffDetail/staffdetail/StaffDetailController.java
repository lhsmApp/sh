package com.fh.controller.staffDetail.staffdetail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
import com.fh.entity.CommonBase;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.StaffDetailModel;
import com.fh.entity.SysSealed;
import com.fh.entity.TableColumns;
import com.fh.entity.TmplConfigDetail;
import com.fh.entity.system.Department;
import com.fh.entity.system.Dictionaries;
import com.fh.entity.system.User;
import com.fh.exception.CustomException;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.Const;
import com.fh.util.Jurisdiction;
import com.fh.util.excel.LeadingInExcel;

import net.sf.json.JSONArray;

import com.fh.service.staffDetail.staffdetail.StaffDetailManager;
import com.fh.service.sysSealedInfo.syssealedinfo.impl.SysSealedInfoService;
import com.fh.service.tmplconfig.tmplconfig.impl.TmplConfigService;

/** 
 * 说明：业务封存信息
 * 创建人：FH Q313596790
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

	//底行显示的求和与平均值字段
	String SqlUserdata = "";
	//页面显示数据的年月
	String SystemDateTime = "";
	//页面显示数据的二级单位
	String DepartCode = "";
	//默认值
	Map<String, Object> DefaultValueList = new HashMap<String, Object>();
	//字典
	Map<String, Object> DicList = new HashMap<String, Object>();
	List<TmplConfigDetail> listColumns = new ArrayList<TmplConfigDetail>();
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public @ResponseBody CommonBase edit() throws Exception{
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		logBefore(logger, Jurisdiction.getUsername()+"修改JgGrid");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		
		StaffDetailModel.setModelDefault(pd, DefaultValueList);
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
		
		List<StaffDetailModel> repeatList = staffdetailService.findByPd(pd);
		if(repeatList!=null && repeatList.size()>0){
			commonBase.setCode(2);
			commonBase.setMessage("此区间内编码已存在！");
		} else {
			if(pd.getString("oper").equals("edit")){
				staffdetailService.edit(pd);
				commonBase.setCode(0);
			}
			else if(pd.getString("oper").equals("add")){
				staffdetailService.save(pd);
				commonBase.setCode(0);
			}
		}
		return commonBase;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表StaffDetail");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		PageData pd = this.getPageData();
		
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("staffDetail/staffdetail/staffdetail_list");
		//当前期间,取自tb_system_config的SystemDateTime字段
		SystemDateTime = staffdetailService.currentSection(pd);
		mv.addObject("SystemDateTime", SystemDateTime);
		//当前登录人所在二级单位
		DepartCode = Jurisdiction.getCurrentDepartmentID();//
		User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USERROL);
		String DepartName = user.getDEPARTMENT_NAME();
		mv.addObject("DepartName", DepartName);
		//封存状态,取自tb_sys_sealed_info表state字段, 数据操作需要前提为当前明细数据未封存，如果已确认封存，则明细数据不能再进行操作。
		pd.put("RPT_DEPT", DepartCode);
		pd.put("RPT_DUR", SystemDateTime);
		pd.put("BILL_TYPE", 1);// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
		String State = syssealedinfoService.getState(pd);
		mv.addObject("State", State);
		
		//用语句查询出数据库表的所有字段及其属性；拼接成jqgrid全部列
		List<TableColumns> tableColumns = staffdetailService.getTableColumns(pd);
		//默认值
		DefaultValueList = new HashMap<String, Object>();
		//字典
		DicList = new HashMap<String, Object>();
		Map<String, Map<String, Object>> listColModelAll = StaffDetailModel.jqGridColModelAll(tableColumns, DepartCode, staffdetailService, DefaultValueList);
		//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		TmplConfigDetail item = new TmplConfigDetail();
		item.setDEPT_CODE(DepartCode);
		item.setTABLE_CODE("TB_STAFF_DETAIL");
		listColumns = tmplconfigService.listNeed(item);
		//底行显示的求和与平均值字段
		SqlUserdata = "";
		//拼接真正设置的jqGrid的ColModel
		StringBuilder jqGridColModel = new StringBuilder();
		jqGridColModel.append("[");
		//添加关键字的保存列
		if(StaffDetailModel.KeyList!=null && StaffDetailModel.KeyList.size()>0){
			for(String key : StaffDetailModel.KeyList){
				jqGridColModel.append(" {name: '").append(key.toUpperCase()).append(StaffDetailModel.KeyExtra).append("', hidden: true, editable: true, editrules: {edithidden: false}}, ");
			}
		}
		//添加配置表设置列，字典（未设置就使用表默认，text或number）、隐藏、表头显示
		if(listColumns != null && listColumns.size() > 0){
			for(int i=0; i < listColumns.size(); i++){
				if(listColModelAll.containsKey(listColumns.get(i).getCOL_CODE().toUpperCase())){
					Map<String, Object> itemColModel = listColModelAll.get(listColumns.get(i).getCOL_CODE());
					jqGridColModel.append("{");
					String name = (String) itemColModel.get("name");
					String edittype = (String) itemColModel.get("edittype");
					String notedit = (String) itemColModel.get("notedit");
					if(name != null && !name.trim().equals("")){
						jqGridColModel.append(name).append(", ");
					}
					//配置表中的字典
					if(listColumns.get(i).getDICT_TRANS()!=null && !listColumns.get(i).getDICT_TRANS().trim().equals("")){
						String strDicValue = getDicValue(listColumns.get(i).getDICT_TRANS(), pd);
						String strSelectValue = ":";
						if(strDicValue!=null && !strDicValue.trim().equals("")){
							strSelectValue += ";" + strDicValue;
						}
						//选择
						jqGridColModel.append(" edittype:'select', ");
						   jqGridColModel.append(" editoptions:{value:'" + strSelectValue + "'}, ");
						//翻译
						jqGridColModel.append(" formatter: 'select', ");
					       jqGridColModel.append(" formatoptions: {value: '" + strDicValue + "'}, ");
						//查询
						jqGridColModel.append(" stype: 'select', ");
					       jqGridColModel.append(" searchoptions: {value: ':[All];" + strDicValue + "'}, ");
					} else{
						if(edittype != null && !edittype.trim().equals("")){
							jqGridColModel.append(edittype).append(", ");
						}
					}
					//配置表中的隐藏
					int intHide = Integer.parseInt(listColumns.get(i).getCOL_HIDE());
					jqGridColModel.append(" hidden: ").append(intHide == 1 ? "false" : "true").append(", ");
					if(intHide != 1){
						jqGridColModel.append(" editable:true, editrules: {edithidden: false}, ");
					}
					if(notedit != null && !notedit.trim().equals("")){
						jqGridColModel.append(notedit).append(", ");
					}
					//配置表中的表头显示
					jqGridColModel.append(" label: '").append(listColumns.get(i).getCOL_NAME()).append("' ");
					
					jqGridColModel.append("}");
					if(i < listColumns.size() -1){
						jqGridColModel.append(",");
					}
					//底行显示的求和与平均值字段
					// 1汇总 0不汇总,默认0
					if(Integer.parseInt(listColumns.get(i).getCOL_SUM()) == 1){
						if(SqlUserdata!=null && !SqlUserdata.trim().equals("")){
							SqlUserdata += ", ";
						}
						SqlUserdata += " sum(" + listColumns.get(i).getCOL_CODE() + ") " + listColumns.get(i).getCOL_CODE();
					} 
					// 0不计算 1计算 默认0
					else if(Integer.parseInt(listColumns.get(i).getCOL_AVE()) == 1){
						if(SqlUserdata!=null && !SqlUserdata.trim().equals("")){
							SqlUserdata += ", ";
						}
						SqlUserdata += " round(avg(" + listColumns.get(i).getCOL_CODE() + "), 2) " + listColumns.get(i).getCOL_CODE();
					}
				}
			}
		}
		jqGridColModel.append("]");
		mv.addObject("jqGridColModel", jqGridColModel.toString());
		return mv;
	}
	
	public String getDicValue(String dicName, PageData pd) throws Exception{
		StringBuilder ret = new StringBuilder();
		String strDicType = staffdetailService.getDicType(dicName);
		Map<String, String> dicAdd = new HashMap<String, String>();
		if(strDicType.equals("1")){
			List<Dictionaries> dicList = staffdetailService.getSysDictionaries(dicName);
			for(Dictionaries dic : dicList){
				if(ret!=null && !ret.toString().trim().equals("")){
					ret.append(";");
				}
				ret.append(dic.getBIANMA() + ":" + dic.getNAME());
				dicAdd.put(dic.getBIANMA(), dic.getNAME());
			}
		} else if(strDicType.equals("2")){
			if(dicName.toUpperCase().equals(("oa_department").toUpperCase())){
				List<Department> listPara = (List<Department>) staffdetailService.getDepartDic(pd);
				for(Department dic : listPara){
					if(ret!=null && !ret.toString().trim().equals("")){
						ret.append(";");
					}
					ret.append(dic.getDEPARTMENT_CODE() + ":" + dic.getNAME());
					dicAdd.put(dic.getDEPARTMENT_CODE(), dic.getNAME());
				}
			}
		}
		if(!DicList.containsKey(dicName)){
			DicList.put(dicName, dicAdd);
		}
		return ret.toString();
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Betting");
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String filters = pd.getString("filters");				//多条件过滤条件
		if(null != filters && !"".equals(filters)){
			pd.put("filterWhereResult", SqlTools.constructWhere(filters,null));
		}
		pd.put("Userdata", SqlUserdata);
		//页面显示数据的年月
		pd.put("SystemDateTime", SystemDateTime);
		//页面显示数据的二级单位
		pd.put("DepartCode", DepartCode);
		page.setPd(pd);
		List<PageData> varList = staffdetailService.JqPage(page);	//列出Betting列表
		int records = staffdetailService.countJqGridExtend(page);
		PageData userdata=staffdetailService.getFooterSummary(page);
		
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		result.setRowNum(page.getRowNum());
		result.setRecords(records);
		result.setPage(page.getPage());
		result.setUserdata(userdata);
		
		return result;
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
		Object DATA_ROWS = pd.get("DATA_ROWS");
		String json = DATA_ROWS.toString();  
        JSONArray array = JSONArray.fromObject(json);  
        List<StaffDetailModel> listData = (List<StaffDetailModel>) JSONArray.toCollection(array,StaffDetailModel.class);
        for(StaffDetailModel item : listData){
        	item.setBILL_CODE(" ");
        	item.setBUSI_DATE(SystemDateTime);
        	item.setDEPT_CODE(DepartCode);
        }
		if(null != listData && listData.size() > 0){
			List<StaffDetailModel> repeatList = staffdetailService.findByModel(listData);
			if(repeatList!=null && repeatList.size()>0){
				commonBase.setCode(2);
				commonBase.setMessage("此区间内编码已存在！");
			} else {
				staffdetailService.updateAll(listData);
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
		PageData pd = this.getPageData();
		Object DATA_ROWS = pd.get("DATA_ROWS");
		String json = DATA_ROWS.toString();  
        JSONArray array = JSONArray.fromObject(json);  
        List<StaffDetailModel> listData = (List<StaffDetailModel>) JSONArray.toCollection(array,StaffDetailModel.class);
        if(null != listData && listData.size() > 0){
			staffdetailService.deleteAll(listData);
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
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("staffDetail/staffdetail/uploadExcel");
		return mv;
	}

	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/readExcel")
	public @ResponseBody CommonBase readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;}//校验权限
		
		// 局部变量
		LeadingInExcel<StaffDetailModel> testExcel = null;
		List<StaffDetailModel> uploadAndRead = null;
		try {
			// 定义需要读取的数据
			String formart = "yyyy-MM-dd";
			String propertiesFileName = "config";
			String kyeName = "file_path";
			int sheetIndex = 0;
			Class<StaffDetailModel> clazz = StaffDetailModel.class;
			Map<String, String> titleAndAttribute = null;
			// 定义对应的标题名与对应属性名
			titleAndAttribute = new HashMap<String, String>();
			
			//配置表设置列
			if(listColumns != null && listColumns.size() > 0){
				for(int i=0; i < listColumns.size(); i++){
					titleAndAttribute.put(listColumns.get(i).getCOL_NAME(), listColumns.get(i).getCOL_CODE());
				}
			}

			// 调用解析工具包
			testExcel = new LeadingInExcel<StaffDetailModel>(formart);
			// 解析excel，获取客户信息集合

			uploadAndRead = testExcel.uploadAndRead(file, propertiesFileName, kyeName, sheetIndex,
					titleAndAttribute, clazz, listColumns, DicList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("读取Excel文件错误", e);
			throw new CustomException("读取Excel文件错误");
		}
		boolean judgement = false;
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
			PageData pd = this.getPageData();
			pd.put("RPT_DEPT", DEPT_CODE);
			pd.put("RPT_DUR", BUSI_DATE);
			pd.put("BILL_TYPE", 1);// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
			String State = syssealedinfoService.getState(pd);
			if(!(State!=null && State.equals("0"))){
				sbRet.add("导入期间已封存！");
			}
			if(sbRet.size()>0){
				StringBuilder sbTitle = new StringBuilder();
				for(String str : sbRet){
					sbTitle.append(str + "\n");
				}
				commonBase.setCode(2);
				commonBase.setMessage(sbTitle.toString());
			} else {
				//此处执行集合添加 
				staffdetailService.batchImport(uploadAndRead);
			}
			
			/*// 把客户信息分为没100条数据为一组迭代添加客户信息（注：将customerList集合作为参数，在Mybatis的相应映射文件中使用foreach标签进行批量添加。）
			int listSize = uploadAndRead.size();
			int toIndex = 100;
			for (int i = 0; i < listSize; i += 100) {
				if (i + 100 > listSize) {
					toIndex = listSize - i;
				}
				List<StaffDetailModel> subList = uploadAndRead.subList(i, i + toIndex);

				//此处执行集合添加 
				staffdetailService.batchImport(subList);
			}*/
		} else {
			commonBase.setCode(-1);
			commonBase.setMessage("TranslateUtil");
		}
		return commonBase;
	}
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	//public void downExcel(HttpServletResponse response)throws Exception{
	public ModelAndView downExcel(JqPage page) throws Exception{
		//页面显示数据的二级单位
		List<PageData> varOList = staffdetailService.exportModel(DepartCode);
		return export(varOList);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出StaffDetail到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		PageData pd = this.getPageData();
		//页面显示数据的年月
		pd.put("SystemDateTime", SystemDateTime);
		//页面显示数据的二级单位
		pd.put("DepartCode", DepartCode);
		page.setPd(pd);
		List<PageData> varOList = staffdetailService.exportList(page);
		return export(varOList);
	}
	
	@SuppressWarnings("unchecked")
	private ModelAndView export(List<PageData> varOList){
		ModelAndView mv = new ModelAndView();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		if(listColumns != null && listColumns.size() > 0){
			for(int i=0; i < listColumns.size(); i++){
				titles.add(listColumns.get(i).getCOL_NAME());
			}
		}
		dataMap.put("titles", titles);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			if(listColumns != null && listColumns.size() > 0){
				for(int j=1; j <= listColumns.size(); j++){
					String trans = listColumns.get(j-1).getDICT_TRANS();
					Object getCellValue = varOList.get(i).get(listColumns.get(j-1).getCOL_CODE().toUpperCase());
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
		pd.put("RPT_DEPT", DepartCode);
		pd.put("RPT_DUR", SystemDateTime);
		pd.put("BILL_TYPE", 1);// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
		String State = syssealedinfoService.getState(pd);
		if(State!=null && State.equals("1")){
			commonBase.setCode(2);
			commonBase.setMessage("当前期间已封存！");
		} else {
			User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USERROL);
			String userId = user.getUSER_ID();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date());
			
			SysSealed item = new SysSealed();
			item.setBILL_CODE(" ");
			item.setRPT_DEPT(DepartCode);
			item.setRPT_DUR(SystemDateTime);
			item.setRPT_USER(userId);
			item.setRPT_DATE(time);//YYYY-MM-DD HH:MM:SS
			item.setBILL_TYPE("1");// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
			item.setSTATE("1");// 枚举  1封存,0解封
			syssealedinfoService.report(item);
			commonBase.setCode(0);
		}
		return commonBase;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
