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
import com.fh.entity.JqGridModel;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.StaffDetailModel;
import com.fh.entity.TableColumns;
import com.fh.entity.TmplConfigDetail;
import com.fh.entity.system.Department;
import com.fh.entity.system.Dictionaries;
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

	//底行显示的求和与平均值字段
	String SqlUserdata = "";
	//页面显示数据的年月
	String SystemDateTime = "";
	//页面显示数据的二级单位
	String DepartCode = "";
	//默认值
	Map<String, Object> DefaultValueList = new HashMap<String, Object>();
	
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
		
		StaffDetailModel.setModelDefault(pd, DefaultValueList);

		if(pd.getString("oper").equals("edit")){
			staffdetailService.edit(pd);
			commonBase.setCode(0);
		}
		else if(pd.getString("oper").equals("add")){
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
			staffdetailService.save(pd);
			
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
		SystemDateTime = staffdetailService.currentSection(pd);
		mv.addObject("SystemDateTime", SystemDateTime);
		//当前登录人所在二级单位
		DepartCode = "001001";//Jurisdiction.getCurrentDepartmentID();
		String DepartName = "登录人单位";
		mv.addObject("DepartName", DepartName);
		//封存状态,取自tb_sys_sealed_info表state字段, 数据操作需要前提为当前明细数据未封存，如果已确认封存，则明细数据不能再进行操作。
		String State = "0";
		mv.addObject("State", State);
		
		//用语句查询出数据库表的所有字段及其属性；拼接成jqgrid全部列
		List<TableColumns> tableColumns = staffdetailService.getTableColumns(pd);
		//默认值
		DefaultValueList = new HashMap<String, Object>();
		Map<String, Map<String, Object>> listColModelAll = StaffDetailModel.jqGridColModelAll(tableColumns, DepartCode, staffdetailService, DefaultValueList);
		//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		TmplConfigDetail item = new TmplConfigDetail();
		item.setDEPT_CODE(DepartCode);
		item.setTABLE_CODE("TB_STAFF_DETAIL");
		List<TmplConfigDetail> listColumns = tmplconfigService.listNeed(item);
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
					if(name != null && name.trim() != ""){
						jqGridColModel.append(name).append(", ");
					}
					//配置表中的字典
					if(listColumns.get(i).getDICT_TRANS()!=null && !listColumns.get(i).getDICT_TRANS().trim().equals("")){
						String strDicValue = getDicValue(listColumns.get(i).getDICT_TRANS(), pd);
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
					//配置表中的隐藏
					int intHide = Integer.parseInt(listColumns.get(i).getCOL_HIDE());
					jqGridColModel.append(" hidden: ").append(intHide == 1 ? "false" : "true").append(", ");
					if(intHide != 1){
						jqGridColModel.append(" editable:true, editrules: {edithidden: false}, ");
					}
					if(notedit != null && notedit.trim() != ""){
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
	
	public String getDicValue(String dicName, PageData pd) throws Exception{
		StringBuilder ret = new StringBuilder();
		String strDicType = staffdetailService.getDicType(dicName);
		if(strDicType.equals("1")){
			List<Dictionaries> dicList = staffdetailService.getSysDictionaries(dicName);
			for(Dictionaries dic : dicList){
				if(ret!=null && !ret.toString().trim().equals("")){
					ret.append("; ");
				}
				ret.append(dic.getBIANMA() + ":" + dic.getNAME());
			}
		} else if(strDicType.equals("2")){
			if(dicName.toUpperCase().equals(("oa_department").toUpperCase())){
				/*pd.put("ColumnName", " DEPARTMENT_CODE BIANMA, NAME NAME ");
				pd.put("DicName", dicName);
				pd.put("order_by", "DEPARTMENT_CODE");
				List<Dictionaries> listPara = (List<Dictionaries>) staffdetailService.getTableDic(pd);
				for(Dictionaries dic : listPara){
					if(ret!=null && !ret.toString().trim().equals("")){
						ret.append("; ");
					}
					ret.append(dic.getBIANMA() + ":" + dic.getNAME());
				}*/
			}
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
       
		if(null != listData && listData.size() > 0){
			staffdetailService.updateAll(listData);
			commonBase.setCode(0);
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
				//staffdetailService.batchImport(subList);
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
