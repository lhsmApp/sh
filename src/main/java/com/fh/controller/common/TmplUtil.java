package com.fh.controller.common;

import java.util.ArrayList;
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
import com.fh.service.tmplconfig.tmplconfig.impl.TmplConfigService;
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
	String strKeyExtra = "__";
	// 查询表的主键字段
	List<String> KeyList = new ArrayList<String>();

	public TmplUtil(TmplConfigManager tmplconfigService,TmplConfigDictManager tmplConfigDictService,DictionariesManager dictionariesService,DepartmentManager departmentService) {
		this.tmplconfigService = tmplconfigService;
		this.tmplConfigDictService=tmplConfigDictService;
		this.dictionariesService=dictionariesService;
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
	public Map<String, Map<String, Object>> jqGridColModelAll(List<TableColumns> columns) throws Exception {
		Map<String, Map<String, Object>> list = new HashMap<String, Map<String, Object>>();
		KeyList = new ArrayList<String>();

		for (TableColumns col : columns) {
			Map<String, Object> MapAdd = new HashMap<String, Object>();

			StringBuilder model_name = new StringBuilder();
			StringBuilder model_notedit = new StringBuilder();
			model_notedit.append(" editable: false ");

			int intLength = getColumnLength(col.getColumn_type(), col.getData_type());
			if (col.getData_type() != null
					&& (col.getData_type().trim().equals("DECIMAL") || col.getData_type().trim().equals("DOUBLE")
							|| col.getData_type().trim().equals("INT") || col.getData_type().trim().equals("FLOAT"))) {
				model_name.append(" width: '120', ");
				//model_name.append(" align: 'right', searchrules: {number: true}, sorttype: 'number', formatter: 'number', ");
				model_name.append(" align: 'right', search: false, sorttype: 'number', formatter: 'number',");
			} else{
				if(intLength > 50){
					model_name.append(" width: '200', ");
				} else{
					model_name.append(" width: '130', ");
				}
			}
			model_name.append(" name: '"+ col.getColumn_name() +"'");
			MapAdd.put("name", model_name.toString());
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
		if(strDicType.equals("1")){
			List<Dictionaries> dicList = dictionariesService.getSysDictionaries(dicName);
			for(Dictionaries dic : dicList){
				if(ret!=null && !ret.toString().trim().equals("")){
					ret.append("; ");
				}
				ret.append(dic.getBIANMA() + ":" + dic.getNAME());
			}
		} else if(strDicType.equals("2")){
			if(dicName.toUpperCase().equals(("oa_department").toUpperCase())){
				PageData pd=new PageData();
				List<Department> listPara = (List<Department>) departmentService.getDepartDic(pd);
				for(Department dic : listPara){
					if(ret!=null && !ret.toString().trim().equals("")){
						ret.append("; ");
					}
					ret.append(dic.getDEPARTMENT_CODE() + ":" + dic.getNAME());
				}
			}
		}
		return ret.toString();
	}
}
