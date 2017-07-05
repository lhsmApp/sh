package com.fh.controller.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fh.entity.TableColumns;
import com.fh.entity.TmplConfigDetail;
import com.fh.entity.system.Department;
import com.fh.entity.system.Dictionaries;
import com.fh.service.fhoa.department.DepartmentManager;
import com.fh.service.system.dictionaries.DictionariesManager;
import com.fh.service.tmplConfigDict.tmplconfigdict.TmplConfigDictManager;
import com.fh.service.tmplconfig.tmplconfig.TmplConfigManager;
import com.fh.util.PageData;

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

	private TmplConfigManager tmplconfigService;
	private TmplConfigDictManager tmplConfigDictService;
	private DictionariesManager dictionariesService;
	private DepartmentManager departmentService;

	// 查询表的主键字段后缀，区别于主键字段，用于修改或删除
	private String keyExtra = "__";
	// 查询表的主键字段
	private List<String> keyList = Arrays.asList("BILL_CODE","BUSI_DATE","USER_CODE");
	//底行显示的求和与平均值字段
	StringBuilder m_sqlUserdata = new StringBuilder();
	public StringBuilder getSqlUserdata() {
		return m_sqlUserdata;
	}
	//默认值
	private Map<String, Object> m_defaultValueList = new HashMap<String, Object>();
	public Map<String, Object> getDefaultValueList() {
		return m_defaultValueList;
	}
	//字典
	private Map<String, Object> m_dicList = new HashMap<String, Object>();
	public Map<String, Object> getDicList() {
		return m_dicList;
	}
	//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
	private List<TmplConfigDetail> m_columnsList = new ArrayList<TmplConfigDetail>();
	public List<TmplConfigDetail> getColumnsList() {
		return m_columnsList;
	}

	public TmplUtil(TmplConfigManager tmplconfigService,TmplConfigDictManager tmplConfigDictService,DictionariesManager dictionariesService,DepartmentManager departmentService) {
		this.tmplconfigService = tmplconfigService;
		this.tmplConfigDictService=tmplConfigDictService;
		this.dictionariesService=dictionariesService;
		this.departmentService=departmentService;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String generateStructureNoEdit(String tableCode, String departCode) throws Exception {
		// 用语句查询出数据库表的所有字段及其属性；拼接成jqgrid全部列
		List<TableColumns> tableColumns = tmplconfigService.getTableColumns(tableCode);
		Map<String, Map<String, Object>> listColModelAll = jqGridColModelAll(tableColumns);
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		TmplConfigDetail item = new TmplConfigDetail();
		item.setDEPT_CODE(departCode);
		item.setTABLE_CODE(tableCode);
		List<TmplConfigDetail> listColumns = tmplconfigService.listNeed(item);
		// 底行显示的求和与平均值字段
		String SqlUserdata = "";
		// 拼接真正设置的jqGrid的ColModel
		StringBuilder jqGridColModel = new StringBuilder();
		jqGridColModel.append("[");
		// 添加配置表设置列，字典（未设置就使用表默认，text或number）、隐藏、表头显示
		if (listColumns != null && listColumns.size() > 0) {
			for (int i = 0; i < listColumns.size(); i++) {
				if (listColModelAll.containsKey(listColumns.get(i).getCOL_CODE())) {
					Map<String, Object> itemColModel = listColModelAll.get(listColumns.get(i).getCOL_CODE());
					jqGridColModel.append("{");
					String name = (String) itemColModel.get("name");
					String notedit = (String) itemColModel.get("notedit");
					if (name != null && name.trim() != "") {
						jqGridColModel.append(name).append(", ");
					}
					//配置表中的字典
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
					}
					// 配置表中的隐藏
					int intHide = Integer.parseInt(listColumns.get(i).getCOL_HIDE());
					jqGridColModel.append(" hidden: ").append(intHide == 1 ? "false" : "true").append(", ");
					if (notedit != null && notedit.trim() != "") {
						jqGridColModel.append(notedit).append(", ");
					}
					// 配置表中的表头显示
					jqGridColModel.append(" label: '").append(listColumns.get(i).getCOL_NAME()).append("' ");

					jqGridColModel.append("}");
					if (i < listColumns.size() - 1) {
						jqGridColModel.append(",");
					}
					// 底行显示的求和与平均值字段
					// 1汇总 0不汇总,默认0
					if (Integer.parseInt(listColumns.get(i).getCOL_SUM()) == 1) {
						if (SqlUserdata != null && SqlUserdata.trim() != "") {
							SqlUserdata += ", ";
						}
						SqlUserdata += " sum(" + listColumns.get(i).getCOL_CODE() + ") "
								+ listColumns.get(i).getCOL_CODE();
					}
					// 0不计算 1计算 默认0
					else if (Integer.parseInt(listColumns.get(i).getCOL_AVE()) == 1) {
						if (SqlUserdata != null && SqlUserdata.trim() != "") {
							SqlUserdata += ", ";
						}
						SqlUserdata += " round(avg(" + listColumns.get(i).getCOL_CODE() + "), 2) "
								+ listColumns.get(i).getCOL_CODE();
					}
				}
			}
		}
		jqGridColModel.append("]");

		return jqGridColModel.toString();
	}

	/**
	 * 用语句查询出数据库表的所有字段及其属性；拼接成jqgrid全部列
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, Object>> jqGridColModelAll(List<TableColumns> columns) 
			throws Exception {
		Map<String, Map<String, Object>> list = new HashMap<String, Map<String, Object>>();

		for(TableColumns col : columns){
			m_defaultValueList.put(col.getColumn_name(), col.getColumn_default());
			
			Map<String, Object> MapAdd = new HashMap<String, Object>();
			
			StringBuilder model_name = new StringBuilder();
			StringBuilder model_edittype = new StringBuilder();
			StringBuilder model_notedit = new StringBuilder();

			//设置必定不用编辑的列
			if(col.getColumn_name().equals("BILL_CODE") || 
					col.getColumn_name().equals("BUSI_DATE") ||
					col.getColumn_name().equals("DEPT_CODE")){
				model_notedit.append(" editable: false ");
			} else{
				model_notedit.append(" editable: true ");
			}
			int intLength = getColumnLength(col.getColumn_type(), col.getData_type());
			if(col.getData_type() != null 
					&& (col.getData_type().trim().equals("DECIMAL") || 
					    col.getData_type().trim().equals("DOUBLE") || 
						col.getData_type().trim().equals("INT") || 
					    col.getData_type().trim().equals("FLOAT"))){
				model_name.append(" width: '150', ");
				model_name.append(" align: 'right', search: false, sorttype: 'number', editrules: {number: true}, ");
				model_edittype.append(" edittype:'text', formatter: 'number', editoptions:{maxlength:'" + intLength + "', number: true} ");
			} else{
					if(intLength > 50){
						model_name.append(" width: '200', ");
						model_edittype.append(" edittype:'textarea', ");
					} else{
						model_name.append(" width: '130', ");
						model_edittype.append(" edittype:'text', ");
					}
					model_edittype.append(" editoptions:{maxlength:'" + intLength + "'} ");
			}

			if(col.getColumn_name().equals("USER_CODE")){
				model_name.append(" editrules:{required:true}, ");
			}
			model_name.append(" name: '"+ col.getColumn_name() +"' ");
			
			MapAdd.put("name", model_name.toString());
			MapAdd.put("edittype", model_edittype.toString());
			MapAdd.put("notedit", model_notedit.toString());
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
	
	private String getDicValue(String dicName) throws Exception{
		StringBuilder ret = new StringBuilder();
		String strDicType = tmplConfigDictService.getDicType(dicName);
		Map<String, String> dicAdd = new HashMap<String, String>();
		if(strDicType.equals("1")){
			List<Dictionaries> dicList = dictionariesService.getSysDictionaries(dicName);
			for(Dictionaries dic : dicList){
				if(ret!=null && !ret.toString().trim().equals("")){
					ret.append(";");
				}
				ret.append(dic.getBIANMA() + ":" + dic.getNAME());
				dicAdd.put(dic.getBIANMA(), dic.getNAME());
			}
		} else if(strDicType.equals("2")){
			if(dicName.toUpperCase().equals(("oa_department").toUpperCase())){
				PageData pd=new PageData();
				List<Department> listPara = (List<Department>) departmentService.getDepartDic(pd);
				for(Department dic : listPara){
					if(ret!=null && !ret.toString().trim().equals("")){
						ret.append(";");
					}
					ret.append(dic.getDEPARTMENT_CODE() + ":" + dic.getNAME());
					dicAdd.put(dic.getDEPARTMENT_CODE(), dic.getNAME());
				}
			}
		}
		if(!m_dicList.containsKey(dicName)){
			m_dicList.put(dicName, dicAdd);
		}
		return ret.toString();
	}
	
	public static void setModelDefault(PageData pd, Class<?> clazz, Map<String, Object> DefaultValueList) 
			throws ClassNotFoundException{
		Field[] fields = clazz.getDeclaredFields();//根据Class对象获得属性 私有的也可以获得
		for(Field field : fields){
			String fieldName = field.getName().toUpperCase();
			if(!(pd.containsKey(fieldName) 
					&& pd.get(fieldName) != null
					&& !pd.getString(fieldName).trim().equals(""))){
				Object defaultValue = DefaultValueList.get(fieldName);
		        pd.put(fieldName, defaultValue);
			}
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String generateStructure(String tableCode, String departCode, int columnCount) throws Exception {
		//默认值
		m_defaultValueList = new HashMap<String, Object>();
		//字典
		m_dicList = new HashMap<String, Object>();
		//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		m_columnsList = new ArrayList<TmplConfigDetail>();
		//底行显示的求和与平均值字段
		m_sqlUserdata = new StringBuilder();
		//拼接真正设置的jqGrid的ColModel
		StringBuilder jqGridColModel = new StringBuilder();

		//用语句查询出数据库表的所有字段及其属性；拼接成jqgrid全部列
		List<TableColumns> tableColumns = tmplconfigService.getTableColumns(tableCode);
		Map<String, Map<String, Object>> listColModelAll = jqGridColModelAll(tableColumns);
		//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		TmplConfigDetail item = new TmplConfigDetail();
		item.setDEPT_CODE(departCode);
		item.setTABLE_CODE(tableCode);
		m_columnsList = tmplconfigService.listNeed(item);
		
		jqGridColModel.append("[");
		//添加关键字的保存列
		if(keyList!=null && keyList.size()>0){
			for(int i=0; i<keyList.size(); i++){
				String key = keyList.get(i);
				if(i != 0){
					jqGridColModel.append(", ");
				}
				jqGridColModel.append(" {name: '").append(key.toUpperCase()).append(keyExtra).append("', hidden: true, editable: true, editrules: {edithidden: false}} ");
			}
		}
		//添加配置表设置列，字典（未设置就使用表默认，text或number）、隐藏、表头显示
		if(m_columnsList != null && m_columnsList.size() > 0){
			int row = 4;
			int col = 1;
			for(int i=0; i < m_columnsList.size(); i++){
				if(listColModelAll.containsKey(m_columnsList.get(i).getCOL_CODE().toUpperCase())){
					Map<String, Object> itemColModel = listColModelAll.get(m_columnsList.get(i).getCOL_CODE());
					jqGridColModel.append(", {");
					String name = (String) itemColModel.get("name");
					String edittype = (String) itemColModel.get("edittype");
					String notedit = (String) itemColModel.get("notedit");
					if(name != null && !name.trim().equals("")){
						jqGridColModel.append(name).append(", ");
					} else {
						continue;
					}
					//配置表中的字典
					if(m_columnsList.get(i).getDICT_TRANS()!=null && !m_columnsList.get(i).getDICT_TRANS().trim().equals("")){
						String strDicValue = getDicValue(m_columnsList.get(i).getDICT_TRANS());
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
					int intHide = Integer.parseInt(m_columnsList.get(i).getCOL_HIDE());
					jqGridColModel.append(" hidden: ").append(intHide == 1 ? "false" : "true").append(", ");
					if(intHide != 1){
						jqGridColModel.append(" editable:true, editrules: {edithidden: false}, ");
					} else {
						if(notedit!=null && notedit.trim().endsWith("true")){
							jqGridColModel.append(" formoptions:{ rowpos:" + row + ", colpos:" + col + " }, ");
							col++;
							if(col > columnCount){
								row++;
								col = 1;
							} 
						}
					}
					if(notedit != null && !notedit.trim().equals("")){
						jqGridColModel.append(notedit).append(", ");
					}
					//配置表中的表头显示
					jqGridColModel.append(" label: '").append(m_columnsList.get(i).getCOL_NAME()).append("' ");
					
					jqGridColModel.append("}");
					/* if(i < m_columnsList.size() -1){
						jqGridColModel.append(",");
					} */
					//底行显示的求和与平均值字段
					// 1汇总 0不汇总,默认0
					if(Integer.parseInt(m_columnsList.get(i).getCOL_SUM()) == 1){
						if(m_sqlUserdata!=null && !m_sqlUserdata.toString().trim().equals("")){
							m_sqlUserdata.append(", ");
						}
						m_sqlUserdata.append(" sum(" + m_columnsList.get(i).getCOL_CODE() + ") " + m_columnsList.get(i).getCOL_CODE());
					} 
					// 0不计算 1计算 默认0
					else if(Integer.parseInt(m_columnsList.get(i).getCOL_AVE()) == 1){
						if(m_sqlUserdata!=null && !m_sqlUserdata.toString().trim().equals("")){
							m_sqlUserdata.append(", ");
						}
						m_sqlUserdata.append(" round(avg(" + m_columnsList.get(i).getCOL_CODE() + "), 2) " + m_columnsList.get(i).getCOL_CODE());
					}
				}
			}
		}
		jqGridColModel.append("]");
		return jqGridColModel.toString();
	}
}
