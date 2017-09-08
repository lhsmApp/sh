package com.fh.controller.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fh.entity.TableColumns;
import com.fh.entity.TmplConfigDetail;
import com.fh.entity.system.Department;
import com.fh.entity.system.Dictionaries;
import com.fh.service.fhoa.department.DepartmentManager;
import com.fh.service.system.dictionaries.DictionariesManager;
import com.fh.service.system.user.UserManager;
import com.fh.service.tmplConfigDict.tmplconfigdict.TmplConfigDictManager;
import com.fh.service.tmplconfig.tmplconfig.TmplConfigManager;
import com.fh.util.Const;
import com.fh.util.PageData;
import com.fh.util.Tools;
import com.fh.util.enums.BillState;

/**
 * 模板通用类
 * 
 * @ClassName: TmplUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhangxiaoliu
 * @date 2017年6月23日
 *
 */
public class TmplUtil {

	private static TmplConfigManager tmplconfigService;
	private TmplConfigDictManager tmplConfigDictService;
	private DictionariesManager dictionariesService;
	private DepartmentManager departmentService;
	private UserManager userService;

	// 查询表的主键字段后缀，区别于主键字段，用于修改或删除
	public static String keyExtra = "__";
	// 查询表的主键字段
	private List<String> keyList = Arrays.asList("BILL_CODE", "BUSI_DATE", "USER_CODE");
	// 底行显示的求和与平均值字段
	StringBuilder m_sqlUserdata = new StringBuilder();
	public StringBuilder getSqlUserdata() {
		return m_sqlUserdata;
	}
	//界面分组字段
	private List<String> jqGridGroupColumn = new ArrayList<String>();
	//分组字段是否显示在表中
	List<String> m_jqGridGroupColumnShow = new ArrayList<String>();
	public List<String> getJqGridGroupColumnShow() {
		return m_jqGridGroupColumnShow;
	}

	// 字典
	private Map<String, Object> m_dicList = new LinkedHashMap<String, Object>();
	public Map<String, Object> getDicList() {
		return m_dicList;
	}
	
	//表结构  
	private Map<String, TableColumns> map_HaveColumnsList = new LinkedHashMap<String, TableColumns>();
	public Map<String, TableColumns> getHaveColumnsList() {
		return map_HaveColumnsList;
	}

	// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
	private Map<String, TmplConfigDetail> map_SetColumnsList = new LinkedHashMap<String, TmplConfigDetail>();
	public Map<String, TmplConfigDetail> getSetColumnsList() {
		return map_SetColumnsList;
	}
	

	public TmplUtil(TmplConfigManager tmplconfigService, TmplConfigDictManager tmplConfigDictService,
			DictionariesManager dictionariesService, DepartmentManager departmentService,UserManager userService) {
		TmplUtil.tmplconfigService = tmplconfigService;
		this.tmplConfigDictService = tmplConfigDictService;
		this.dictionariesService = dictionariesService;
		this.departmentService = departmentService;
		this.userService=userService;
	}

	public TmplUtil(TmplConfigManager tmplconfigService, TmplConfigDictManager tmplConfigDictService,
			DictionariesManager dictionariesService, DepartmentManager departmentService,UserManager userService,
			List<String> keyList, List<String> jqGridGroupColumn) {
		TmplUtil.tmplconfigService = tmplconfigService;
		this.tmplConfigDictService = tmplConfigDictService;
		this.dictionariesService = dictionariesService;
		this.departmentService = departmentService;
		this.userService=userService;
		this.keyList = keyList;
		this.jqGridGroupColumn = jqGridGroupColumn;
		InitJqGridGroupColumnShow();
	}

	//分组字段是否显示在表中
	private void InitJqGridGroupColumnShow(){
		m_jqGridGroupColumnShow = new ArrayList<String>();
		if(jqGridGroupColumn!=null){
			int size = jqGridGroupColumn.size();
			for(int i = 0; i < size; i++){
				m_jqGridGroupColumnShow.add(String.valueOf(true));
			}
		}
	}
	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String generateStructure(String tableNo, String departCode, int columnCount, List<String> MustNotEditFeildList) throws Exception {
		//分组字段是否显示在表中
		InitJqGridGroupColumnShow();
		// 字典
		m_dicList = new LinkedHashMap<String, Object>();
		//表结构
		map_HaveColumnsList = new LinkedHashMap<String, TableColumns>();
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		map_SetColumnsList = new LinkedHashMap<String, TmplConfigDetail>();
		// 底行显示的求和与平均值字段
		m_sqlUserdata = new StringBuilder();
		// 拼接真正设置的jqGrid的ColModel
		StringBuilder jqGridColModelAll = new StringBuilder();
		
		PageData pd=new PageData();
		pd.put("TABLE_NO", tableNo);
		PageData pdResult=tmplconfigService.findTableCodeByTableNo(pd);
		String tableCodeTmpl=pdResult.getString("TABLE_CODE");
		String tableCodeOri=DictsUtil.getActualTable(tableCodeTmpl);//数据库真实业务数据表
		// 用语句查询出数据库表的所有字段及其属性；拼接成jqgrid全部列
		List<TableColumns> tableColumns = tmplconfigService.getTableColumns(tableCodeOri);
		Map<String, Map<String, Object>> listColModelAll = jqGridColModelAll(tableColumns);
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		List<TmplConfigDetail> m_columnsList = getShowColumnList(tableCodeTmpl, departCode);
		int row = 1;
		int col = 1;
		
		StringBuilder jqGridColModelCustom = new StringBuilder();
		// 添加配置表设置列，字典（未设置就使用表默认，text或number）、隐藏、表头显示
		if (m_columnsList != null && m_columnsList.size() > 0) {
			for (int i = 0; i < m_columnsList.size(); i++) {
				String getCOL_CODE = m_columnsList.get(i).getCOL_CODE();
				map_SetColumnsList.put(getCOL_CODE, m_columnsList.get(i));
				if (listColModelAll.containsKey(getCOL_CODE.toUpperCase())) {
					Map<String, Object> itemColModel = listColModelAll.get(getCOL_CODE);
					String name = (String) itemColModel.get("name");
					String edittype = (String) itemColModel.get("edittype");
					//String notedit = (String) itemColModel.get("notedit");
					//Boolean editable = (Boolean) itemColModel.get("editable");
					if (name != null && !name.trim().equals("")) {
						if (jqGridColModelCustom!=null && !jqGridColModelCustom.toString().trim().equals("")) {
							jqGridColModelCustom.append(", ");
						}
						jqGridColModelCustom.append("{");
						jqGridColModelCustom.append(name).append(", ");
					} else {
						continue;
					}
					// 配置表中的字典
					if (m_columnsList.get(i).getDICT_TRANS() != null
							&& !m_columnsList.get(i).getDICT_TRANS().trim().equals("")) {
						String strDicValue = getDicValue(m_columnsList.get(i).getDICT_TRANS());
						String strSelectValue = ":";
						if (strDicValue != null && !strDicValue.trim().equals("")) {
							strSelectValue += ";" + strDicValue;
						}
						// 选择
						jqGridColModelCustom.append(" edittype:'select', ");
						jqGridColModelCustom.append(" editoptions:{value:'" + strSelectValue + "'}, ");
						// 翻译
						jqGridColModelCustom.append(" formatter: 'select', ");
						jqGridColModelCustom.append(" formatoptions: {value: '" + strDicValue + "'}, ");
						// 查询
						jqGridColModelCustom.append(" stype: 'select', ");
						jqGridColModelCustom.append(" searchoptions: {value: ':[All];" + strDicValue + "'}, ");
					} else {
						if (edittype != null && !edittype.trim().equals("")) {
							jqGridColModelCustom.append(edittype).append(", ");
						}
					}
					// 设置必定不用编辑的列
					// 配置表中的隐藏
					int intHide = Integer.parseInt(m_columnsList.get(i).getCOL_HIDE());
					jqGridColModelCustom.append(" hidden: ").append(intHide == 1 ? "false" : "true").append(", ");
					// intHide != 1 隐藏
					if (intHide != 1) {
						jqGridColModelCustom.append(" editable:true, editrules: {edithidden: false}, ");
						if(jqGridGroupColumn!=null){
							int groupColumnSize = jqGridGroupColumn.size();
							for(int intGroupColumn = 0; intGroupColumn < groupColumnSize; intGroupColumn++){
								if(jqGridGroupColumn.get(intGroupColumn).toUpperCase().equals(m_columnsList.get(i).getCOL_CODE().toUpperCase())){
									m_jqGridGroupColumnShow.set(intGroupColumn, String.valueOf(false));
								}
							}
						}
					} else {
						if (MustNotEditFeildList.contains(getCOL_CODE)) {
							jqGridColModelCustom.append(" editable: false, editrules: {edithidden: false}, ");
						} else {
							jqGridColModelCustom.append(" editable: true, ");
							jqGridColModelCustom.append(" formoptions:{ rowpos:" + row + ", colpos:" + col + " }, ");
							col++;
							if (col > columnCount) {
								row++;
								col = 1;
							}
						}
					}
					
					
					//if (notedit != null && !notedit.trim().equals("")) {
					//	jqGridColModelCustom.append(notedit).append(", ");
					//}
					//if(editable){
					//	jqGridColModelCustom.append(" formoptions:{ rowpos:" + row + ", colpos:" + col + " }, ");
					//	col++;
					//	if (col > columnCount) {
					//		row++;
					//		col = 1;
					//	}
					//} else {
						
					//}
					/*
					 * if(i < m_columnsList.size() -1){
					 * jqGridColModel.append(","); }
					 */
					// 底行显示的求和与平均值字段
					// 1汇总 0不汇总,默认0
					if (Integer.parseInt(m_columnsList.get(i).getCOL_SUM()) == 1) {
						if (m_sqlUserdata != null && !m_sqlUserdata.toString().trim().equals("")) {
							m_sqlUserdata.append(", ");
						}
						m_sqlUserdata.append(" sum(" + getCOL_CODE + ") "
								+ getCOL_CODE);
						jqGridColModelCustom.append(" summaryType:'sum', summaryTpl:'<b>sum:{0}</b>', ");
					}
					// 0不计算 1计算 默认0
					else if (Integer.parseInt(m_columnsList.get(i).getCOL_AVE()) == 1) {
						if (m_sqlUserdata != null && !m_sqlUserdata.toString().trim().equals("")) {
							m_sqlUserdata.append(", ");
						}
						m_sqlUserdata.append(" round(avg(" + getCOL_CODE + "), 2) "
								+ m_columnsList.get(i).getCOL_CODE());
						jqGridColModelCustom.append(" summaryType:'avg', summaryTpl:'<b>avg:{0}</b>', ");
					}
					// 配置表中的表头显示
					jqGridColModelCustom.append(" label: '").append(m_columnsList.get(i).getCOL_NAME()).append("' ");

					jqGridColModelCustom.append("}");
				}
			}
		}
		StringBuilder jqGridColModelKey = new StringBuilder();
		row++;
		col = 1;
		// 添加关键字的保存列
		if (keyList != null && keyList.size() > 0) {
			for (int i = 0; i < keyList.size(); i++) {
				String key = keyList.get(i);
				if (jqGridColModelKey!=null && !jqGridColModelKey.toString().trim().equals("")) {
					jqGridColModelKey.append(", ");
				}
				jqGridColModelKey.append(" {name: '").append(key.toUpperCase()).append(keyExtra)
						.append("', hidden: true, editable: true, editrules: {edithidden: false}, ");
				jqGridColModelKey.append(" formoptions:{ rowpos:" + row + ", colpos:" + col + " }} ");
				col++;
				if (col > columnCount) {
					row++;
					col = 1;
				}
			}
		}
		jqGridColModelAll.append("[");
		jqGridColModelAll.append(jqGridColModelCustom);
		if (jqGridColModelCustom!=null && !jqGridColModelCustom.toString().trim().equals("")) {
			if (jqGridColModelKey!=null && !jqGridColModelKey.toString().trim().equals("")) {
				jqGridColModelAll.append(", ");
				jqGridColModelAll.append(jqGridColModelKey);
			}
		}
		jqGridColModelAll.append("]");
		return jqGridColModelAll.toString();
	}

	/**
	 * 用语句查询出数据库表的所有字段及其属性；拼接成jqgrid全部列
	 * 
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, Object>> jqGridColModelAll(List<TableColumns> columns) throws Exception {
		Map<String, Map<String, Object>> list = new LinkedHashMap<String, Map<String, Object>>();

		for (TableColumns col : columns) {
			//表结构
			map_HaveColumnsList.put(col.getColumn_name(), col);

			Map<String, Object> MapAdd = new LinkedHashMap<String, Object>();

			StringBuilder model_name = new StringBuilder();
			StringBuilder model_edittype = new StringBuilder();
			//StringBuilder model_notedit = new StringBuilder();
			//Boolean editable = null;

			// 设置必定不用编辑的列
			//if (MustNotEditFeildList.contains(col.getColumn_name())) {
			//	model_notedit.append(" editable: true, editrules: {edithidden: false} ");
			//	editable = false;
			//} else {
			//	model_notedit.append(" editable: true ");
			//	editable = true;
			//}
			int intLength = getColumnLength(col.getColumn_type(), col.getData_type());
			if (col.getData_type() != null && IsNumFeild(col.getData_type())) {
				model_name.append(" width: '150', ");
				model_name.append(" align: 'right', search: false, sorttype: 'number', editrules: {number: true}, ");
				model_edittype.append(" edittype:'text', formatter: 'number', editoptions:{maxlength:'" + intLength
						+ "', number: true} ");
			} else {
				if (intLength > 50) {
					model_name.append(" width: '200', ");
					model_edittype.append(" edittype:'textarea', ");
				} else {
					model_name.append(" width: '130', ");
					model_edittype.append(" edittype:'text', ");
				}
				model_edittype.append(" editoptions:{maxlength:'" + intLength + "'} ");
			}

			if (col.getColumn_name().equals("USER_CODE")) {
				model_name.append(" editrules:{required:true}, ");
			}
			model_name.append(" name: '" + col.getColumn_name() + "' ");

			MapAdd.put("name", model_name.toString());
			MapAdd.put("edittype", model_edittype.toString());
			//MapAdd.put("notedit", model_notedit.toString());
			//MapAdd.put("editable", editable);
			list.put(col.getColumn_name(), MapAdd);
		}

		return list;
	}
	

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String generateStructureNoEdit(String tableNo, String departCode) throws Exception {
		//分组字段是否显示在表中
		InitJqGridGroupColumnShow();
		// 底行显示的求和与平均值字段
		m_sqlUserdata = new StringBuilder();
		//表结构
		map_HaveColumnsList = new LinkedHashMap<String, TableColumns>();
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		map_SetColumnsList = new LinkedHashMap<String, TmplConfigDetail>();
		
		PageData pd=new PageData();
		pd.put("TABLE_NO", tableNo);
		PageData pdResult=tmplconfigService.findTableCodeByTableNo(pd);
		String tableCodeTmpl=pdResult.getString("TABLE_CODE");
		String tableCodeOri=DictsUtil.getActualTable(tableCodeTmpl);
		// 用语句查询出数据库表的所有字段及其属性；拼接成jqgrid全部列
		List<TableColumns> tableColumns = tmplconfigService.getTableColumns(tableCodeOri);
		Map<String, Map<String, Object>> listColModelAll = jqGridColModelAllNoEdit(tableColumns);
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		List<TmplConfigDetail> m_columnsList = getShowColumnList(tableCodeTmpl, departCode);
		// 拼接真正设置的jqGrid的ColModel
		StringBuilder jqGridColModel = new StringBuilder();

		jqGridColModel.append("[");
		// 添加关键字的保存列
		if (keyList != null && keyList.size() > 0) {
			for (int i = 0; i < keyList.size(); i++) {
				String key = keyList.get(i);
				if (i != 0) {
					jqGridColModel.append(", ");
				}
				jqGridColModel.append(" {name: '").append(key.toUpperCase()).append(keyExtra).append("', hidden: true, editable: false} ");
			}
		}
		// 添加配置表设置列，字典（未设置就使用表默认，text或number）、隐藏、表头显示
		if (m_columnsList != null && m_columnsList.size() > 0) {
			for (int i = 0; i < m_columnsList.size(); i++) {
				map_SetColumnsList.put(m_columnsList.get(i).getCOL_CODE(), m_columnsList.get(i));
				if (listColModelAll.containsKey(m_columnsList.get(i).getCOL_CODE().toUpperCase())) {
					Map<String, Object> itemColModel = listColModelAll.get(m_columnsList.get(i).getCOL_CODE());
					jqGridColModel.append(", {");
					String name = (String) itemColModel.get("name");
					if (name != null && !name.trim().equals("")) {
						jqGridColModel.append(name).append(", ").append(" editable: false, ");
					} else {
						continue;
					}
					// 配置表中的字典
					if (m_columnsList.get(i).getDICT_TRANS() != null
							&& !m_columnsList.get(i).getDICT_TRANS().trim().equals("")) {
						String strDicValue = getDicValue(m_columnsList.get(i).getDICT_TRANS());

						String strSelectValue = ":";
						if (strDicValue != null && !strDicValue.trim().equals("")) {
							strSelectValue += ";" + strDicValue;
						}
						// 选择
						jqGridColModel.append(" edittype:'select', ");
						jqGridColModel.append(" editoptions:{value:'" + strSelectValue + "'}, ");
						// 翻译
						jqGridColModel.append(" formatter: 'select', ");
						jqGridColModel.append(" formatoptions: {value: '" + strDicValue + "'}, ");
						// 查询
						jqGridColModel.append(" stype: 'select', ");
						jqGridColModel.append(" searchoptions: {value: ':[All];" + strDicValue + "'}, ");
					}
					// 配置表中的隐藏
					int intHide = Integer.parseInt(m_columnsList.get(i).getCOL_HIDE());
					jqGridColModel.append(" hidden: ").append(intHide == 1 ? "false" : "true").append(", ");
					// intHide != 1 隐藏
					if(intHide != 1){
						if(jqGridGroupColumn!=null){
							int groupColumnSize = jqGridGroupColumn.size();
							for(int intGroupColumn = 0; intGroupColumn < groupColumnSize; intGroupColumn++){
								if(jqGridGroupColumn.get(intGroupColumn).toUpperCase().equals(m_columnsList.get(i).getCOL_CODE().toUpperCase())){
									m_jqGridGroupColumnShow.set(intGroupColumn, String.valueOf(false));
								}
							}
						}
					}
					// 底行显示的求和与平均值字段
					// 1汇总 0不汇总,默认0
					if (Integer.parseInt(m_columnsList.get(i).getCOL_SUM()) == 1) {
						if (m_sqlUserdata != null && !m_sqlUserdata.toString().trim().equals("")) {
							m_sqlUserdata.append(", ");
						}
						m_sqlUserdata.append(" sum(" + m_columnsList.get(i).getCOL_CODE() + ") "
								+ m_columnsList.get(i).getCOL_CODE());
						jqGridColModel.append(" summaryType:'sum', summaryTpl:'<b>sum:{0}</b>', ");
					}
					// 0不计算 1计算 默认0
					else if (Integer.parseInt(m_columnsList.get(i).getCOL_AVE()) == 1) {
						if (m_sqlUserdata != null && !m_sqlUserdata.toString().trim().equals("")) {
							m_sqlUserdata.append(", ");
						}
						m_sqlUserdata.append(" round(avg(" + m_columnsList.get(i).getCOL_CODE() + "), 2) "
								+ m_columnsList.get(i).getCOL_CODE());
						jqGridColModel.append(" summaryType:'avg', summaryTpl:'<b>avg:{0}</b>', ");
					}
					// 配置表中的表头显示
					jqGridColModel.append(" label: '").append(m_columnsList.get(i).getCOL_NAME()).append("' ");

					jqGridColModel.append("}");
				}
			}
		}
		jqGridColModel.append("]");
		return jqGridColModel.toString();
	}

	/**
	 * 用语句查询出数据库表的所有字段及其属性；拼接成jqgrid全部列
	 * 
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, Object>> jqGridColModelAllNoEdit(List<TableColumns> columns) throws Exception {
		Map<String, Map<String, Object>> list = new LinkedHashMap<String, Map<String, Object>>();

		for (TableColumns col : columns) {
			//表结构
			map_HaveColumnsList.put(col.getColumn_name(), col);
			
			
			Map<String, Object> MapAdd = new LinkedHashMap<String, Object>();

			StringBuilder model_name = new StringBuilder();

			int intLength = getColumnLength(col.getColumn_type(), col.getData_type());
			if (col.getData_type() != null && IsNumFeild(col.getData_type())) {
				model_name.append(" width: '150', ");
				model_name.append(" align: 'right', search: false, sorttype: 'number', formatter: 'number',summaryTpl: 'sum: {0}', summaryType: 'sum', ");
			} else {
				if (intLength > 50) {
					model_name.append(" width: '200', ");
				} else {
					model_name.append(" width: '130', ");
				}
			}
			model_name.append(" name: '" + col.getColumn_name() + "' ");
			MapAdd.put("name", model_name.toString());
			list.put(col.getColumn_name(), MapAdd);
		}
		return list;
	}
	
	private int getColumnLength(String Column_type, String Data_type) {
		int ret = 0;
		String[] listLength = Column_type.replace(Data_type, "").replace("(", "").replace(")", "").split(",");
		for (String length : listLength) {
			ret += Integer.parseInt(length);
		}
		return ret;
	}

	private String getDicValue(String dicName) throws Exception {
		StringBuilder ret = new StringBuilder();
		String strDicType = tmplConfigDictService.getDicType(dicName);
		Map<String, String> dicAdd = new LinkedHashMap<String, String>();
		if (strDicType.equals("1")) {
			List<Dictionaries> dicList = dictionariesService.getSysDictionaries(dicName);
			for (Dictionaries dic : dicList) {
				if (ret != null && !ret.toString().trim().equals("")) {
					ret.append(";");
				}
				ret.append(dic.getDICT_CODE() + ":" + dic.getNAME());
				dicAdd.put(dic.getDICT_CODE(), dic.getNAME());
			}
		} else if (strDicType.equals("2")) {
			if (dicName.toUpperCase().equals(("oa_department").toUpperCase())) {
				PageData pd = new PageData();
				List<Department> listPara = (List<Department>) departmentService.getDepartDic(pd);
				for (Department dic : listPara) {
					if (ret != null && !ret.toString().trim().equals("")) {
						ret.append(";");
					}
					ret.append(dic.getDEPARTMENT_CODE() + ":" + dic.getNAME());
					dicAdd.put(dic.getDEPARTMENT_CODE(), dic.getNAME());
				}
			}else if (dicName.toUpperCase().equals(("sys_user").toUpperCase())) {
				PageData pd = new PageData();
				List<PageData> listUser = (List<PageData>) userService.getUserValue(pd);
				for (PageData dic : listUser) {
					if (ret != null && !ret.toString().trim().equals("")) {
						ret.append(";");
					}
					ret.append(dic.get("USER_ID").toString() + ":" + dic.getString("NAME"));
					dicAdd.put(dic.get("USER_ID").toString(), dic.getString("NAME"));
				}
			}
		}else if (strDicType.equals("3")) {//枚举
			if (dicName.toUpperCase().equals(("BILL_STATE").toUpperCase())) {
				for(BillState billState:BillState.values()){
					ret.append(billState.getNameKey() + ":" + billState.getNameValue());
					ret.append(';');
					dicAdd.put(billState.getNameKey(), billState.getNameValue());
				}
				if (!ret.toString().trim().equals("")) {
					ret.deleteCharAt(ret.length()-1);
				}
			}
		}
		if (!m_dicList.containsKey(dicName)) {
			m_dicList.put(dicName, dicAdd);
		}
		return ret.toString();
	}

	public static void setModelDefault(PageData pd, Map<String, TableColumns> haveColumnsList)
			throws ClassNotFoundException {
		String InsertField = "";
		String InsertVale = "";
	    for (TableColumns col : haveColumnsList.values()) {
			Object value = pd.get(col.getColumn_name().toUpperCase());
			if(value != null && value.toString() != null && !value.toString().trim().equals("")){
				if(InsertField!=null && !InsertField.trim().equals("")){
					InsertField += ",";
					InsertVale += ",";
				}
				InsertField += col.getColumn_name();
				InsertVale += "'" + value.toString() + "'";
			}
		}
		pd.put("InsertField", InsertField);
		pd.put("InsertVale", InsertVale);
	}

	public static Boolean IsNumFeild(String Data_type){
		Boolean bol = false;
		if(Data_type.trim().equals("DECIMAL") || Data_type.trim().equals("DOUBLE")
		    || Data_type.trim().equals("INT") || Data_type.trim().equals("FLOAT")){
				bol = true;
		}
		return bol;
	}

	public static String getSumFeildSelect(List<String> GroupbyFeild, List<TableColumns> tableDetailColumns){
		String SelectFeild = "";
		if(GroupbyFeild != null){
			for(String feild : GroupbyFeild){
				if(SelectFeild!=null && !SelectFeild.trim().equals("")){
					SelectFeild += ", ";
				}
				SelectFeild += feild + ", " + feild + " " + feild + keyExtra;
			}
		}
		if(tableDetailColumns != null && tableDetailColumns.size() > 0){
			for(TableColumns col : tableDetailColumns){
				if(TmplUtil.IsNumFeild(col.getData_type())){
					String getCOL_CODE = col.getColumn_name();
					if(GroupbyFeild == null || (GroupbyFeild != null && !GroupbyFeild.contains(getCOL_CODE))){
						if(SelectFeild!=null && !SelectFeild.trim().equals("")){
							SelectFeild += ", ";
						}
						SelectFeild += " sum(" + getCOL_CODE +") " + getCOL_CODE;
					}
				}
			}
		}
		return SelectFeild;
	}
	
	


	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String generateStructureAccount(String tableNo, String departCode) throws Exception {
		//分组字段是否显示在表中
		InitJqGridGroupColumnShow();
		// 底行显示的求和与平均值字段
		m_sqlUserdata = new StringBuilder();
		// 字典
		m_dicList = new LinkedHashMap<String, Object>();
		//表结构
		map_HaveColumnsList = new LinkedHashMap<String, TableColumns>();
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		map_SetColumnsList = new LinkedHashMap<String, TmplConfigDetail>();
		
		PageData pd=new PageData();
		pd.put("TABLE_NO", tableNo);
		PageData pdResult=tmplconfigService.findTableCodeByTableNo(pd);
		String tableCodeTmpl=pdResult.getString("TABLE_CODE");
		String tableCodeOri=DictsUtil.getActualTable(tableCodeTmpl);//数据库真实业务数据表
		
		// 用语句查询出数据库表的所有字段及其属性；拼接成jqgrid全部列
		List<TableColumns> tableColumns = tmplconfigService.getTableColumns(tableCodeOri);
		Map<String, Map<String, Object>> listColModelAll = jqGridColModelAccount(tableColumns);
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		List<TmplConfigDetail> m_columnsList = getShowColumnList(tableCodeTmpl, departCode);
		// 拼接真正设置的jqGrid的ColModel
		StringBuilder jqGridColModel = new StringBuilder();

		jqGridColModel.append("[");
		// 添加关键字的保存列
		if (keyList != null && keyList.size() > 0) {
			for (int i = 0; i < keyList.size(); i++) {
				String key = keyList.get(i);
				if (i != 0) {
					jqGridColModel.append(", ");
				}
				jqGridColModel.append(" {name: '").append(key.toUpperCase()).append(keyExtra).append("', hidden: true, editable: false} ");
			}
		}
		// 添加配置表设置列，字典（未设置就使用表默认，text或number）、隐藏、表头显示
		if (m_columnsList != null && m_columnsList.size() > 0) {
			for (int i = 0; i < m_columnsList.size(); i++) {
				map_SetColumnsList.put(m_columnsList.get(i).getCOL_CODE(), m_columnsList.get(i));
				if (listColModelAll.containsKey(m_columnsList.get(i).getCOL_CODE().toUpperCase())) {
					Map<String, Object> itemColModel = listColModelAll.get(m_columnsList.get(i).getCOL_CODE());
					jqGridColModel.append(", {");
					String name = (String) itemColModel.get("name");
					if (name != null && !name.trim().equals("")) {
						jqGridColModel.append(name).append(", ");
					} else {
						continue;
					}
					// 配置表中的字典
					if (m_columnsList.get(i).getDICT_TRANS() != null
							&& !m_columnsList.get(i).getDICT_TRANS().trim().equals("")) {
						String strDicValue = getDicValue(m_columnsList.get(i).getDICT_TRANS());

						String strSelectValue = ":";
						if (strDicValue != null && !strDicValue.trim().equals("")) {
							strSelectValue += ";" + strDicValue;
						}
						// 选择
						jqGridColModel.append(" edittype:'select', ");
						jqGridColModel.append(" editoptions:{value:'" + strSelectValue + "'}, ");
						// 翻译
						jqGridColModel.append(" formatter: 'select', ");
						jqGridColModel.append(" formatoptions: {value: '" + strDicValue + "'}, ");
						// 查询
						jqGridColModel.append(" stype: 'select', ");
						jqGridColModel.append(" searchoptions: {value: ':[All];" + strDicValue + "'}, ");
					}
					// 配置表中的隐藏
					int intHide = Integer.parseInt(m_columnsList.get(i).getCOL_HIDE());
					jqGridColModel.append(" hidden: ").append(intHide == 1 ? "false" : "true").append(", ");
					// intHide != 1 隐藏
					if(intHide != 1){
						if(jqGridGroupColumn!=null){
							int groupColumnSize = jqGridGroupColumn.size();
							for(int intGroupColumn = 0; intGroupColumn < groupColumnSize; intGroupColumn++){
								if(jqGridGroupColumn.get(intGroupColumn).toUpperCase().equals(m_columnsList.get(i).getCOL_CODE().toUpperCase())){
									m_jqGridGroupColumnShow.set(intGroupColumn, String.valueOf(false));
								}
							}
						}
					}
					
					// 配置表中的表头显示
					jqGridColModel.append(" label: '").append(m_columnsList.get(i).getCOL_NAME()).append("' ");

					jqGridColModel.append("}");
				}
			}
		}
		jqGridColModel.append("]");
		return jqGridColModel.toString();
	}

	/**
	 * 用语句查询出数据库表的所有字段及其属性；拼接成jqgrid全部列
	 * 
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, Object>> jqGridColModelAccount(List<TableColumns> columns) throws Exception {
		Map<String, Map<String, Object>> list = new LinkedHashMap<String, Map<String, Object>>();

		for (TableColumns col : columns) {
			//表结构
			map_HaveColumnsList.put(col.getColumn_name(), col);
			
			Map<String, Object> MapAdd = new LinkedHashMap<String, Object>();

			StringBuilder model_name = new StringBuilder();
			model_name.append(" editable: false, ");

			int intLength = getColumnLength(col.getColumn_type(), col.getData_type());
			if (col.getData_type() != null && IsNumFeild(col.getData_type())) {
				model_name.append(" width: '150', ");
				model_name.append(" align: 'right', search: false, sorttype: 'number', ");
			} else {
				if (intLength > 50) {
					model_name.append(" width: '200', ");
				} else {
					model_name.append(" width: '130', ");
				}
			}
			model_name.append(" name: '" + col.getColumn_name() + "' ");
			MapAdd.put("name", model_name.toString());
			list.put(col.getColumn_name(), MapAdd);
		}
		return list;
	}
	
	/**
	 * 获取显示结构，未设置获取上级单位
	 * 
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public static List<TmplConfigDetail> getShowColumnList(String tableCode, String departCode) throws Exception{
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		TmplConfigDetail item = new TmplConfigDetail();
		item.setDEPT_CODE(departCode);
		item.setTABLE_CODE(tableCode);
		List<TmplConfigDetail> m_columnsList = tmplconfigService.listNeed(item);
		if(m_columnsList.size()==0){
			String rootDeptCode=Tools.readTxtFile(Const.ROOT_DEPT_CODE);
			item.setDEPT_CODE(rootDeptCode);
			item.setTABLE_CODE(tableCode);
			m_columnsList = tmplconfigService.listNeed(item);
		}
		return m_columnsList;
	}
}
