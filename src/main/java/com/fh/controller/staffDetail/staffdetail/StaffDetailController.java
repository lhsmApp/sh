package com.fh.controller.staffDetail.staffdetail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.fh.entity.JqGridModel;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.TableColumns;
import com.fh.entity.TmplConfigDetail;
import com.fh.exception.CustomException;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.Jurisdiction;
import com.fh.util.excel.LeadingInExcel;

import net.sf.json.JSONArray;

import com.fh.service.staffDetail.staffdetail.StaffDetailManager;
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
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public @ResponseBody CommonBase edit() throws Exception{
		CommonBase commonBase = new CommonBase();
		logBefore(logger, Jurisdiction.getUsername()+"修改JgGrid");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		if(pd.getString("oper").equals("edit")){
			staffdetailService.edit(pd);
			commonBase.setCode(0);
		}
		else if(pd.getString("oper").equals("add")){
			staffdetailService.save(pd);
			commonBase.setCode(0);
		}
		else if(pd.getString("oper").equals("del")){
			String [] ids=pd.getString("id").split(",");
			if(ids.length==1)
				staffdetailService.delete(pd);
			else
				staffdetailService.deleteAll(ids);
			commonBase.setCode(0);
		}
		
		/**此处为业务错误返回值，例如返回当前删除的信息含有业务关联字段，不能删除，自行设定setCode(返回码，客户端按码抓取并返回提示信息)和setMessage("自定义提示信息，提示给用户的")信息，并由界面进行展示。
		 * 此处不是异常返回的错误信息，异常返回错误信息统一由框架抓取异常。
		 */
		//commonBase.setCode(-1);
		//commonBase.setMessage("当前删除的信息含有业务关联字段，不能删除");
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
		String SystemDateTime = staffdetailService.currentSection(pd);
		mv.addObject("SystemDateTime", SystemDateTime);
		//当前登录人所在二级单位
		String DepartCode = "001001";//Jurisdiction.getCurrentDepartmentID();
		String DepartName = "登录人单位";
		mv.addObject("DepartName", DepartName);
		//封存状态,取自tb_sys_sealed_info表state字段, 数据操作需要前提为当前明细数据未封存，如果已确认封存，则明细数据不能再进行操作。
		String State = "0";
		mv.addObject("State", State);
		
		//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		List<TableColumns> tableColumns = staffdetailService.getTableColumns(pd);
		Map<String, Map<String, Object>> listColModelAll = jqGridColModelAll(tableColumns);
		TmplConfigDetail item = new TmplConfigDetail();
		item.setDEPT_CODE(DepartCode);
		item.setTABLE_CODE("TB_STAFF_DETAIL");
		List<TmplConfigDetail> listColumns = tmplconfigService.listNeed(item);
		//mv.addObject("listColumns", listColumns);
		SqlUserdata = "";
		StringBuilder jqGridColModel = new StringBuilder();
		jqGridColModel.append("[");
		if(listColumns != null && listColumns.size() > 0){
			for(int i=0; i < listColumns.size(); i++){
				if(listColModelAll.containsKey(listColumns.get(i).getCOL_CODE())){
					Map<String, Object> itemColModel = listColModelAll.get(listColumns.get(i).getCOL_CODE());
					jqGridColModel.append("{");
					String name = (String) itemColModel.get("name");
					String edittype = (String) itemColModel.get("edittype");
					if(name != null && name.trim() != ""){
						jqGridColModel.append(name).append(", ");
					}
					if(listColumns.get(i).getDICT_TRANS()!=null && !listColumns.get(i).getDICT_TRANS().trim().equals("")){
						String strDicValue = getDicValue(listColumns.get(i).getDICT_TRANS());
						//选择
						jqGridColModel.append(" edittype:'select', ");
						   jqGridColModel.append(" editoptions:{value:'" + strDicValue + "'}, ");
						//翻译
						jqGridColModel.append(" formatter: 'select', ");
					       jqGridColModel.append(" formatoptions: {value: '" + strDicValue + "'}, ");
						//查询
						jqGridColModel.append(" stype: 'select', ");
					       jqGridColModel.append(" searchoptions: {value: ':[All];" + strDicValue + "'}, ");
					} else{
						if(edittype != null && edittype.trim() != ""){
							jqGridColModel.append(edittype).append(", ");
						}
					}
					jqGridColModel.append(" label: '").append(listColumns.get(i).getCOL_NAME()).append("', ");
					jqGridColModel.append(" hidden: ").append(Integer.parseInt(listColumns.get(i).getCOL_HIDE()) == 1 ? "false" : "true").append(" ");
					
					jqGridColModel.append("}");
					if(i < listColumns.size() -1){
						jqGridColModel.append(",");
					}
					// 1汇总 0不汇总,默认0
					if(Integer.parseInt(listColumns.get(i).getCOL_SUM()) == 1){
						if(SqlUserdata!=null && SqlUserdata.trim()!=""){
							SqlUserdata += ", ";
						}
						SqlUserdata += " sum(" + listColumns.get(i).getCOL_CODE() + ") " + listColumns.get(i).getCOL_CODE();
					} 
					// 0不计算 1计算 默认0
					else if(Integer.parseInt(listColumns.get(i).getCOL_AVE()) == 1){
						if(SqlUserdata!=null && SqlUserdata.trim()!=""){
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
	
	public Map<String, Map<String, Object>> jqGridColModelAll(List<TableColumns> columns){
		Map<String, Map<String, Object>> list = new HashMap<String, Map<String, Object>>();

		for(TableColumns col : columns){
			Map<String, Object> MapAdd = new HashMap<String, Object>();
			
			StringBuilder model_name = new StringBuilder();
			StringBuilder model_edittype = new StringBuilder();
			if(col.getColumn_key() != null && col.getColumn_key().trim().equals("PRI")){
				model_name.append(" key: true, ");
			}
			if(col.getColumn_name().equals("BILL_CODE") || 
					col.getColumn_name().equals("BUSI_DATE") ||
					col.getColumn_name().equals("USER_NAME") ||
					col.getColumn_name().equals("DEPT_CODE")){
				model_name.append(" editable: false, ");
			} else{
				model_name.append(" editable: true, ");
			}
			int intLength = getColumnLength(col.getColumn_type(), col.getData_type());
			if(col.getData_type() != null 
					&& (col.getData_type().trim().equals("DECIMAL") || 
					    col.getData_type().trim().equals("DOUBLE") || 
						col.getData_type().trim().equals("INT") || 
					    col.getData_type().trim().equals("FLOAT"))){
				model_name.append(" width: '180', ");
				model_name.append(" align: 'right', searchrules: {number: true}, sorttype: 'number', ");
				model_edittype.append(" edittype:'text', editoptions:{maxlength:'" + intLength + "', number: true} ");
			} else{
				if(intLength > 50){
					model_name.append(" width: '220', ");
					model_edittype.append(" edittype:'textarea', ");
				} else{
					model_name.append(" width: '150', ");
					model_edittype.append(" edittype:'text', ");
				}
				model_edittype.append(" editoptions:{maxlength:'" + intLength + "'} ");
			}

			model_name.append(" name: '"+ col.getColumn_name() +"' ");
			MapAdd.put("name", model_name.toString());
			MapAdd.put("edittype", model_edittype.toString());
			list.put(col.getColumn_name(), MapAdd);
		}
		
		return list;
	}
	
	public int getColumnLength(String Column_type, String Data_type){
		int ret = 0;
		String[] listLength = Column_type.replace(Data_type, "").replace("(", "").replace(")", "").split(",");
		for(String length : listLength){
			ret += Integer.parseInt(length);
		}
		return ret;
	}
	
	public String getDicValue(String trans){
		StringBuilder ret = new StringBuilder();
		//String strDicType = staffdetailService.getDicType(trans);
		//if(strDicType.equals("1")){
			
		//} else if(strDicType.equals("2")){
			
		//}
		
		return ret.toString();
	}
	//USA:USA;UK:UK;CHI:CHINA
	
	
	
	
	
	

	String SqlUserdata = "";
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
       
		if(null != listData && listData.size() > 0){
			staffdetailService.updateAll(listData);
			commonBase.setCode(0);
		}
		return commonBase;
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出JqGridExtend到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("ID");	//
		titles.add("CategoryName");	//1
		titles.add("ProductName");	//2
		titles.add("Country");	//3
		titles.add("Price");	//4
		titles.add("Quantity");	//5
		dataMap.put("titles", titles);
		page.setPd(pd);
		List<PageData> varOList = staffdetailService.exportList(page);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("ID").toString());	    //1
			vpd.put("var2", varOList.get(i).getString("CATEGORYNAME"));	    //1
			vpd.put("var3", varOList.get(i).getString("PRODUCTNAME"));	    //2
			vpd.put("var4", varOList.get(i).getString("COUNTRY"));	    //3
			vpd.put("var5", (varOList.get(i).get("PRICE") == null? 0 : varOList.get(i).get("PRICE")).toString());	    //4
			vpd.put("var6", (varOList.get(i).get("QUANTITY") == null? 0 : varOList.get(i).get("QUANTITY")).toString());	//5
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap); 
		return mv;
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
	
	/**下载模版
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/downExcel")
	public void downExcel(HttpServletResponse response)throws Exception{
		//FileDownload.fileDownload(response, PathUtil.getClasspath() + Const.FILEPATHFILE + "JqGrids.xls", "JqGrids.xls");
	}

	/**从EXCEL导入到数据库
	 * @param file
	 * @return
	 * @throws Exception
	 */
	//@ApiOperation(value = "导入Excel", httpMethod = "POST", response = CommonBase.class, notes = "导入Excel")
	@RequestMapping(value = "/readExcel")
	public @ResponseBody CommonBase readExcel(@RequestParam(value="excel",required=false) MultipartFile file) throws Exception{
		CommonBase commonBase = new CommonBase();
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;}//校验权限
		
		// 局部变量
		LeadingInExcel<JqGridModel> testExcel = null;
		List<JqGridModel> uploadAndRead = null;
		try {
			// 定义需要读取的数据
			String formart = "yyyy-MM-dd";
			String propertiesFileName = "config";
			String kyeName = "file_path";
			int sheetIndex = 0;
			Class<JqGridModel> clazz = JqGridModel.class;
			Map<String, String> titleAndAttribute = null;
			// 定义对应的标题名与对应属性名
			titleAndAttribute = new HashMap<String, String>();
			titleAndAttribute.put("ID", "ID");
			titleAndAttribute.put("CategoryName", "CATEGORYNAME");
			titleAndAttribute.put("ProductName", "PRODUCTNAME");
			titleAndAttribute.put("Country", "COUNTRY");
			titleAndAttribute.put("Price", "PRICE");
			titleAndAttribute.put("Quantity", "QUANTITY");

			// 调用解析工具包
			testExcel = new LeadingInExcel<JqGridModel>(formart);
			// 解析excel，获取客户信息集合

			uploadAndRead = testExcel.uploadAndRead(file, propertiesFileName, kyeName, sheetIndex,
					titleAndAttribute, clazz);
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
			// 把客户信息分为没100条数据为一组迭代添加客户信息（注：将customerList集合作为参数，在Mybatis的相应映射文件中使用foreach标签进行批量添加。）
			int listSize = uploadAndRead.size();
			int toIndex = 100;
			for (int i = 0; i < listSize; i += 100) {
				if (i + 100 > listSize) {
					toIndex = listSize - i;
				}
				List<JqGridModel> subList = uploadAndRead.subList(i, i + toIndex);

				/** 此处执行集合添加 */
				staffdetailService.batchImport(subList);
			}
		} else {
			commonBase.setCode(-1);
			commonBase.setMessage("TranslateUtil");
		}
		return commonBase;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}

/*
		
		Map<String, Object> Map_BILL_CODE = new HashMap<String, Object>();
		String BILL_CODE_name = "name: 'BILL_CODE', width: '180', key: true, editable: false ";
		String BILL_CODE_edittype = "";
		Map_BILL_CODE.put("name", BILL_CODE_name);
		Map_BILL_CODE.put("edittype", BILL_CODE_edittype);
		list.put("BILL_CODE", Map_BILL_CODE);

		Map<String, Object> Map_BUSI_DATE = new HashMap<String, Object>();
		String BUSI_DATE_name = "name: 'BUSI_DATE', width: '180', key: true, editable: false ";
		String BUSI_DATE_edittype = "";
		Map_BUSI_DATE.put("name", BUSI_DATE_name);
		Map_BUSI_DATE.put("edittype", BUSI_DATE_edittype);
		list.put("BUSI_DATE", Map_BUSI_DATE);

		Map<String, Object> Map_USER_CODE = new HashMap<String, Object>();
		String USER_CODE_name = "name: 'USER_CODE', width: '180', key: true, editable: true ";
		String USER_CODE_edittype = " edittype:'text', editrules:{required:true} ";
		Map_USER_CODE.put("name", USER_CODE_name);
		Map_USER_CODE.put("edittype", USER_CODE_edittype);
		list.put("USER_CODE", Map_USER_CODE);
		
				 //选择
				 //editable: true, edittype:'select', editoptions:{value:'USA:USA;UK:UK;CHI:CHINA'},
				 //翻译
				// formatter: 'select', formatoptions: {value: 'USA:USA;UK:UK;CHI:CHINA'},
				 //stype: 'select', searchoptions: {value: ':[All];USA:USA;UK:UK;CHI:CHINA'}
		
		Map<String, Object> Map_USER_NAME = new HashMap<String, Object>();
		String USER_NAME_name = "name: 'USER_NAME', width: '180', editable: false ";
		String USER_NAME_edittype = "";
		Map_USER_NAME.put("name", USER_NAME_name);
		Map_USER_NAME.put("edittype", USER_NAME_edittype);
		list.put("USER_NAME", Map_USER_NAME); 

		Map<String, Object> Map_STAFF_IDENT = new HashMap<String, Object>();
		String STAFF_IDENT_name = "name: 'STAFF_IDENT', width: '180', editable: true ";
		String STAFF_IDENT_edittype = " edittype:'text', editoptions:{maxlength:'18'}";
		Map_STAFF_IDENT.put("name", STAFF_IDENT_name);
		Map_STAFF_IDENT.put("edittype", STAFF_IDENT_edittype);
		list.put("STAFF_IDENT", Map_STAFF_IDENT);

		Map<String, Object> Map_BANK_CARD = new HashMap<String, Object>();
		String BANK_CARD_name = "name: 'BANK_CARD', width: '180', editable: true ";
		String BANK_CARD_edittype = " edittype:'text', editoptions:{maxlength:'20'} ";
		Map_BANK_CARD.put("name", BANK_CARD_name);
		Map_BANK_CARD.put("edittype", BANK_CARD_edittype);
		list.put("BANK_CARD", Map_BANK_CARD);

		Map<String, Object> Map_USER_GROP = new HashMap<String, Object>();
		String USER_GROP_name = "name: 'USER_GROP', width: '180', editable: true ";
		String USER_GROP_edittype = " edittype:'text', editoptions:{maxlength:'30'} ";
		Map_USER_GROP.put("name", USER_GROP_name);
		Map_USER_GROP.put("edittype", USER_GROP_edittype);
		list.put("USER_GROP", Map_USER_GROP);

		Map<String, Object> Map_USER_CATG = new HashMap<String, Object>();
		String USER_CATG_name = "name: 'USER_CATG', width: '180', editable: true ";
		String USER_CATG_edittype = " edittype:'text', editoptions:{maxlength:'30'} ";
		Map_USER_CATG.put("name", USER_CATG_name);
		Map_USER_CATG.put("edittype", USER_CATG_edittype);
		list.put("USER_CATG", Map_USER_CATG);

		Map<String, Object> Map_DEPT_CODE = new HashMap<String, Object>();
		String DEPT_CODE_name = "name: 'DEPT_CODE', width: '180', editable: false ";
		String DEPT_CODE_edittype = " edittype:'text', editoptions:{maxlength:'30'} ";
		Map_DEPT_CODE.put("name", DEPT_CODE_name);
		Map_DEPT_CODE.put("edittype", DEPT_CODE_edittype);
		list.put("DEPT_CODE", Map_DEPT_CODE);

		Map<String, Object> Map_ORG_UNIT = new HashMap<String, Object>();
		String ORG_UNIT_name = "name: 'ORG_UNIT', width: '180', editable: true ";
		String ORG_UNIT_edittype = " edittype:'text', editoptions:{maxlength:'30'} ";
		Map_ORG_UNIT.put("name", ORG_UNIT_name);
		Map_ORG_UNIT.put("edittype", ORG_UNIT_edittype);
		list.put("ORG_UNIT", Map_ORG_UNIT);

		Map<String, Object> Map_SAL_RANGE = new HashMap<String, Object>();
		String SAL_RANGE_name = "name: 'SAL_RANGE', width: '180', editable: true ";
		String SAL_RANGE_edittype = " edittype:'text', editoptions:{maxlength:'30'} ";
		Map_SAL_RANGE.put("name", SAL_RANGE_name);
		Map_SAL_RANGE.put("edittype", SAL_RANGE_edittype);
		list.put("SAL_RANGE", Map_SAL_RANGE);

		Map<String, Object> Map_POST_SALY = new HashMap<String, Object>();
		String POST_SALY_name = "name: 'POST_SALY', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String POST_SALY_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_POST_SALY.put("name", POST_SALY_name);
		Map_POST_SALY.put("edittype", POST_SALY_edittype);
		list.put("POST_SALY", Map_POST_SALY);

		Map<String, Object> Map_BONUS = new HashMap<String, Object>();
		String BONUS_name = "name: 'BONUS', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String BONUS_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_BONUS.put("name", BONUS_name);
		Map_BONUS.put("edittype", BONUS_edittype);
		list.put("BONUS", Map_BONUS);

		Map<String, Object> Map_CASH_BONUS = new HashMap<String, Object>();
		String CASH_BONUS_name = "name: 'CASH_BONUS', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String CASH_BONUS_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_CASH_BONUS.put("name", CASH_BONUS_name);
		Map_CASH_BONUS.put("edittype", CASH_BONUS_edittype);
		list.put("CASH_BONUS", Map_CASH_BONUS);

		Map<String, Object> Map_WORK_OT = new HashMap<String, Object>();
		String WORK_OT_name = "name: 'WORK_OT', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String WORK_OT_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_WORK_OT.put("name", WORK_OT_name);
		Map_WORK_OT.put("edittype", WORK_OT_edittype);
		list.put("WORK_OT", Map_WORK_OT);

		Map<String, Object> Map_BACK_SALY = new HashMap<String, Object>();
		String BACK_SALY_name = "name: 'BACK_SALY', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String BACK_SALY_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_BACK_SALY.put("name", BACK_SALY_name);
		Map_BACK_SALY.put("edittype", BACK_SALY_edittype);
		list.put("BACK_SALY", Map_BACK_SALY);

		Map<String, Object> Map_RET_SALY = new HashMap<String, Object>();
		String RET_SALY_name = "name: 'RET_SALY', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String RET_SALY_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_RET_SALY.put("name", RET_SALY_name);
		Map_RET_SALY.put("edittype", RET_SALY_edittype);
		list.put("RET_SALY", Map_RET_SALY);

		Map<String, Object> Map_CHK_CASH = new HashMap<String, Object>();
		String CHK_CASH_name = "name: 'CHK_CASH', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String CHK_CASH_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_CHK_CASH.put("name", CHK_CASH_name);
		Map_CHK_CASH.put("edittype", CHK_CASH_edittype);
		list.put("CHK_CASH", Map_CHK_CASH);

		Map<String, Object> Map_INTR_SGL_AWAD = new HashMap<String, Object>();
		String INTR_SGL_AWAD_name = "name: 'INTR_SGL_AWAD', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String INTR_SGL_AWAD_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_INTR_SGL_AWAD.put("name", INTR_SGL_AWAD_name);
		Map_INTR_SGL_AWAD.put("edittype", INTR_SGL_AWAD_edittype);
		list.put("INTR_SGL_AWAD", Map_INTR_SGL_AWAD);

		Map<String, Object> Map_SENY_ALLE = new HashMap<String, Object>();
		String SENY_ALLE_name = "name: 'SENY_ALLE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String SENY_ALLE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_SENY_ALLE.put("name", SENY_ALLE_name);
		Map_SENY_ALLE.put("edittype", SENY_ALLE_edittype);
		list.put("SENY_ALLE", Map_SENY_ALLE);

		Map<String, Object> Map_POST_ALLE = new HashMap<String, Object>();
		String POST_ALLE_name = "name: 'POST_ALLE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String POST_ALLE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_POST_ALLE.put("name", POST_ALLE_name);
		Map_POST_ALLE.put("edittype", POST_ALLE_edittype);
		list.put("POST_ALLE", Map_POST_ALLE);

		Map<String, Object> Map_NS_ALLE = new HashMap<String, Object>();
		String NS_ALLE_name = "name: 'NS_ALLE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String NS_ALLE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_NS_ALLE.put("name", NS_ALLE_name);
		Map_NS_ALLE.put("edittype", NS_ALLE_edittype);
		list.put("NS_ALLE", Map_NS_ALLE);

		Map<String, Object> Map_AREA_ALLE = new HashMap<String, Object>();
		String AREA_ALLE_name = "name: 'AREA_ALLE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String AREA_ALLE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_AREA_ALLE.put("name", AREA_ALLE_name);
		Map_AREA_ALLE.put("edittype", AREA_ALLE_edittype);
		list.put("AREA_ALLE", Map_AREA_ALLE);

		Map<String, Object> Map_EXPT_ALLE = new HashMap<String, Object>();
		String EXPT_ALLE_name = "name: 'EXPT_ALLE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String EXPT_ALLE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_EXPT_ALLE.put("name", EXPT_ALLE_name);
		Map_EXPT_ALLE.put("edittype", EXPT_ALLE_edittype);
		list.put("EXPT_ALLE", Map_EXPT_ALLE);

		Map<String, Object> Map_TECH_ALLE = new HashMap<String, Object>();
		String TECH_ALLE_name = "name: 'TECH_ALLE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String TECH_ALLE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_TECH_ALLE.put("name", TECH_ALLE_name);
		Map_TECH_ALLE.put("edittype", TECH_ALLE_edittype);
		list.put("TECH_ALLE", Map_TECH_ALLE);

		Map<String, Object> Map_LIVE_EXPE = new HashMap<String, Object>();
		String LIVE_EXPE_name = "name: 'LIVE_EXPE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String LIVE_EXPE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_LIVE_EXPE.put("name", LIVE_EXPE_name);
		Map_LIVE_EXPE.put("edittype", LIVE_EXPE_edittype);
		list.put("LIVE_EXPE", Map_LIVE_EXPE);

		Map<String, Object> Map_LIVE_ALLE = new HashMap<String, Object>();
		String LIVE_ALLE_name = "name: 'LIVE_ALLE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String LIVE_ALLE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_LIVE_ALLE.put("name", LIVE_ALLE_name);
		Map_LIVE_ALLE.put("edittype", LIVE_ALLE_edittype);
		list.put("LIVE_ALLE", Map_LIVE_ALLE);

		Map<String, Object> Map_LEAVE_DM = new HashMap<String, Object>();
		String LEAVE_DM_name = "name: 'LEAVE_DM', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String LEAVE_DM_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_LEAVE_DM.put("name", LEAVE_DM_name);
		Map_LEAVE_DM.put("edittype", LEAVE_DM_edittype);
		list.put("LEAVE_DM", Map_LEAVE_DM);

		Map<String, Object> Map_HOUSE_ALLE = new HashMap<String, Object>();
		String HOUSE_ALLE_name = "name: 'HOUSE_ALLE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String HOUSE_ALLE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_HOUSE_ALLE.put("name", HOUSE_ALLE_name);
		Map_HOUSE_ALLE.put("edittype", HOUSE_ALLE_edittype);
		list.put("HOUSE_ALLE", Map_HOUSE_ALLE);

		Map<String, Object> Map_ITEM_ALLE = new HashMap<String, Object>();
		String ITEM_ALLE_name = "name: 'ITEM_ALLE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String ITEM_ALLE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_ITEM_ALLE.put("name", ITEM_ALLE_name);
		Map_ITEM_ALLE.put("edittype", ITEM_ALLE_edittype);
		list.put("ITEM_ALLE", Map_ITEM_ALLE);

		Map<String, Object> Map_MEAL_EXPE = new HashMap<String, Object>();
		String MEAL_EXPE_name = "name: 'MEAL_EXPE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String MEAL_EXPE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_MEAL_EXPE.put("name", MEAL_EXPE_name);
		Map_MEAL_EXPE.put("edittype", MEAL_EXPE_edittype);
		list.put("MEAL_EXPE", Map_MEAL_EXPE);

		Map<String, Object> Map_TRF_ALLE = new HashMap<String, Object>();
		String TRF_ALLE_name = "name: 'TRF_ALLE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String TRF_ALLE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_TRF_ALLE.put("name", TRF_ALLE_name);
		Map_TRF_ALLE.put("edittype", TRF_ALLE_edittype);
		list.put("TRF_ALLE", Map_TRF_ALLE);

		Map<String, Object> Map_TEL_EXPE = new HashMap<String, Object>();
		String TEL_EXPE_name = "name: 'TEL_EXPE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String TEL_EXPE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_TEL_EXPE.put("name", TEL_EXPE_name);
		Map_TEL_EXPE.put("edittype", TEL_EXPE_edittype);
		list.put("TEL_EXPE", Map_TEL_EXPE);

		Map<String, Object> Map_HLDY_ALLE = new HashMap<String, Object>();
		String HLDY_ALLE_name = "name: 'HLDY_ALLE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String HLDY_ALLE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_HLDY_ALLE.put("name", HLDY_ALLE_name);
		Map_HLDY_ALLE.put("edittype", HLDY_ALLE_edittype);
		list.put("HLDY_ALLE", Map_HLDY_ALLE);

		Map<String, Object> Map_KID_ALLE = new HashMap<String, Object>();
		String KID_ALLE_name = "name: 'KID_ALLE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String KID_ALLE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_KID_ALLE.put("name", KID_ALLE_name);
		Map_KID_ALLE.put("edittype", KID_ALLE_edittype);
		list.put("KID_ALLE", Map_KID_ALLE);

		Map<String, Object> Map_COOL_EXPE = new HashMap<String, Object>();
		String COOL_EXPE_name = "name: 'COOL_EXPE', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String COOL_EXPE_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_COOL_EXPE.put("name", COOL_EXPE_name);
		Map_COOL_EXPE.put("edittype", COOL_EXPE_edittype);
		list.put("COOL_EXPE", Map_COOL_EXPE);

		Map<String, Object> Map_EXT_SGL_AWAD = new HashMap<String, Object>();
		String EXT_SGL_AWAD_name = "name: 'EXT_SGL_AWAD', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String EXT_SGL_AWAD_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_EXT_SGL_AWAD.put("name", EXT_SGL_AWAD_name);
		Map_EXT_SGL_AWAD.put("edittype", EXT_SGL_AWAD_edittype);
		list.put("EXT_SGL_AWAD", Map_EXT_SGL_AWAD);

		Map<String, Object> Map_PRE_TAX_PLUS = new HashMap<String, Object>();
		String PRE_TAX_PLUS_name = "name: 'PRE_TAX_PLUS', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String PRE_TAX_PLUS_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_PRE_TAX_PLUS.put("name", PRE_TAX_PLUS_name);
		Map_PRE_TAX_PLUS.put("edittype", PRE_TAX_PLUS_edittype);
		list.put("PRE_TAX_PLUS", Map_PRE_TAX_PLUS);

		Map<String, Object> Map_GROSS_PAY = new HashMap<String, Object>();
		String GROSS_PAY_name = "name: 'GROSS_PAY', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String GROSS_PAY_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_GROSS_PAY.put("name", GROSS_PAY_name);
		Map_GROSS_PAY.put("edittype", GROSS_PAY_edittype);
		list.put("GROSS_PAY", Map_GROSS_PAY);

		Map<String, Object> Map_ENDW_INS = new HashMap<String, Object>();
		String ENDW_INS_name = "name: 'ENDW_INS', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String ENDW_INS_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_ENDW_INS.put("name", ENDW_INS_name);
		Map_ENDW_INS.put("edittype", ENDW_INS_edittype);
		list.put("ENDW_INS", Map_ENDW_INS);

		Map<String, Object> Map_UNEMPL_INS = new HashMap<String, Object>();
		String UNEMPL_INS_name = "name: 'UNEMPL_INS', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String UNEMPL_INS_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_UNEMPL_INS.put("name", UNEMPL_INS_name);
		Map_UNEMPL_INS.put("edittype", UNEMPL_INS_edittype);
		list.put("UNEMPL_INS", Map_UNEMPL_INS);

		Map<String, Object> Map_MED_INS = new HashMap<String, Object>();
		String MED_INS_name = "name: 'MED_INS', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String MED_INS_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_MED_INS.put("name", MED_INS_name);
		Map_MED_INS.put("edittype", MED_INS_edittype);
		list.put("MED_INS", Map_MED_INS);

		Map<String, Object> Map_CASD_INS = new HashMap<String, Object>();
		String CASD_INS_name = "name: 'CASD_INS', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String CASD_INS_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_CASD_INS.put("name", CASD_INS_name);
		Map_CASD_INS.put("edittype", CASD_INS_edittype);
		list.put("CASD_INS", Map_CASD_INS);

		Map<String, Object> Map_HOUSE_FUND = new HashMap<String, Object>();
		String HOUSE_FUND_name = "name: 'HOUSE_FUND', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String HOUSE_FUND_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_HOUSE_FUND.put("name", HOUSE_FUND_name);
		Map_HOUSE_FUND.put("edittype", HOUSE_FUND_edittype);
		list.put("HOUSE_FUND", Map_HOUSE_FUND);

		Map<String, Object> Map_SUP_PESN = new HashMap<String, Object>();
		String SUP_PESN_name = "name: 'SUP_PESN', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String SUP_PESN_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_SUP_PESN.put("name", SUP_PESN_name);
		Map_SUP_PESN.put("edittype", SUP_PESN_edittype);
		list.put("SUP_PESN", Map_SUP_PESN);

		Map<String, Object> Map_TAX_BASE_ADJ = new HashMap<String, Object>();
		String TAX_BASE_ADJ_name = "name: 'TAX_BASE_ADJ', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String TAX_BASE_ADJ_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_TAX_BASE_ADJ.put("name", TAX_BASE_ADJ_name);
		Map_TAX_BASE_ADJ.put("edittype", TAX_BASE_ADJ_edittype);
		list.put("TAX_BASE_ADJ", Map_TAX_BASE_ADJ);

		Map<String, Object> Map_ACCRD_TAX = new HashMap<String, Object>();
		String ACCRD_TAX_name = "name: 'ACCRD_TAX', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String ACCRD_TAX_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_ACCRD_TAX.put("name", ACCRD_TAX_name);
		Map_ACCRD_TAX.put("edittype", ACCRD_TAX_edittype);
		list.put("ACCRD_TAX", Map_ACCRD_TAX);

		Map<String, Object> Map_AFTER_TAX = new HashMap<String, Object>();
		String AFTER_TAX_name = "name: 'AFTER_TAX', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String AFTER_TAX_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_AFTER_TAX.put("name", AFTER_TAX_name);
		Map_AFTER_TAX.put("edittype", AFTER_TAX_edittype);
		list.put("AFTER_TAX", Map_AFTER_TAX);

		Map<String, Object> Map_ACT_SALY = new HashMap<String, Object>();
		String ACT_SALY_name = "name: 'ACT_SALY', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String ACT_SALY_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_ACT_SALY.put("name", ACT_SALY_name);
		Map_ACT_SALY.put("edittype", ACT_SALY_edittype);
		list.put("ACT_SALY", Map_ACT_SALY);

		Map<String, Object> Map_GUESS_DIFF = new HashMap<String, Object>();
		String GUESS_DIFF_name = "name: 'GUESS_DIFF', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String GUESS_DIFF_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_GUESS_DIFF.put("name", GUESS_DIFF_name);
		Map_GUESS_DIFF.put("edittype", GUESS_DIFF_edittype);
		list.put("GUESS_DIFF", Map_GUESS_DIFF);

		Map<String, Object> Map_CUST_COL1 = new HashMap<String, Object>();
		String CUST_COL1_name = "name: 'CUST_COL1', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String CUST_COL1_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_CUST_COL1.put("name", CUST_COL1_name);
		Map_CUST_COL1.put("edittype", CUST_COL1_edittype);
		list.put("CUST_COL1", Map_CUST_COL1);

		Map<String, Object> Map_CUST_COL2 = new HashMap<String, Object>();
		String CUST_COL2_name = "name: 'CUST_COL2', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String CUST_COL2_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_CUST_COL2.put("name", CUST_COL2_name);
		Map_CUST_COL2.put("edittype", CUST_COL2_edittype);
		list.put("CUST_COL2", Map_CUST_COL2);

		Map<String, Object> Map_CUST_COL3 = new HashMap<String, Object>();
		String CUST_COL3_name = "name: 'CUST_COL3', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String CUST_COL3_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_CUST_COL3.put("name", CUST_COL3_name);
		Map_CUST_COL3.put("edittype", CUST_COL3_edittype);
		list.put("CUST_COL3", Map_CUST_COL3);

		Map<String, Object> Map_CUST_COL4 = new HashMap<String, Object>();
		String CUST_COL4_name = "name: 'CUST_COL4', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String CUST_COL4_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_CUST_COL4.put("name", CUST_COL4_name);
		Map_CUST_COL4.put("edittype", CUST_COL4_edittype);
		list.put("CUST_COL4", Map_CUST_COL4);

		Map<String, Object> Map_CUST_COL5 = new HashMap<String, Object>();
		String CUST_COL5_name = "name: 'CUST_COL5', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String CUST_COL5_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_CUST_COL5.put("name", CUST_COL5_name);
		Map_CUST_COL5.put("edittype", CUST_COL5_edittype);
		list.put("CUST_COL5", Map_CUST_COL5);

		Map<String, Object> Map_CUST_COL6 = new HashMap<String, Object>();
		String CUST_COL6_name = "name: 'CUST_COL6', width: '180', align: 'right', searchrules: {number: true}, sorttype: 'number', editable: true ";
		String CUST_COL6_edittype = " edittype:'text', editoptions:{maxlength:'13', number: true} ";
		Map_CUST_COL6.put("name", CUST_COL6_name);
		Map_CUST_COL6.put("edittype", CUST_COL6_edittype);
		list.put("CUST_COL6", Map_CUST_COL6);

		Map<String, Object> Map_CUST_COL7 = new HashMap<String, Object>();
		String CUST_COL7_name = "name: 'CUST_COL7', width: '180', editable: true ";
		String CUST_COL7_edittype = " edittype:'text', editoptions:{maxlength:'30'} ";
		Map_CUST_COL7.put("name", CUST_COL7_name);
		Map_CUST_COL7.put("edittype", CUST_COL7_edittype);
		list.put("CUST_COL7", Map_CUST_COL7);

		Map<String, Object> Map_CUST_COL8 = new HashMap<String, Object>();
		String CUST_COL8_name = "name: 'CUST_COL8', width: '180', editable: true ";
		String CUST_COL8_edittype = " edittype:'text', editoptions:{maxlength:'30'} ";
		Map_CUST_COL8.put("name", CUST_COL8_name);
		Map_CUST_COL8.put("edittype", CUST_COL8_edittype);
		list.put("CUST_COL8", Map_CUST_COL8);

		Map<String, Object> Map_CUST_COL9 = new HashMap<String, Object>();
		String CUST_COL9_name = "name: 'CUST_COL9', width: '180', editable: true ";
		String CUST_COL9_edittype = " edittype:'text', editoptions:{maxlength:'30'} ";
		Map_CUST_COL9.put("name", CUST_COL9_name);
		Map_CUST_COL9.put("edittype", CUST_COL9_edittype);
		list.put("CUST_COL9", Map_CUST_COL9);

		Map<String, Object> Map_CUST_COL10 = new HashMap<String, Object>();
		String CUST_COL10_name = "name: 'CUST_COL10', width: '180', editable: true ";
		String CUST_COL10_edittype = " edittype:'textarea', editoptions:{maxlength:'100'} ";
		Map_CUST_COL10.put("name", CUST_COL10_name);
		Map_CUST_COL10.put("edittype", CUST_COL10_edittype);
		list.put("CUST_COL10", Map_CUST_COL10);

		Map<String, Object> Map_ESTB_DEPT = new HashMap<String, Object>();
		String ESTB_DEPT_name = "name: 'ESTB_DEPT', width: '180', editable: true ";
		String ESTB_DEPT_edittype = " edittype:'text', editoptions:{maxlength:'30'} ";
		Map_ESTB_DEPT.put("name", ESTB_DEPT_name);
		Map_ESTB_DEPT.put("edittype", ESTB_DEPT_edittype);
		list.put("ESTB_DEPT", Map_ESTB_DEPT);
		*/
