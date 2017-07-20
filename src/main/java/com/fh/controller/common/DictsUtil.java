package com.fh.controller.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fh.entity.system.Department;
import com.fh.entity.system.Dictionaries;
import com.fh.service.fhoa.department.DepartmentManager;
import com.fh.service.system.dictionaries.DictionariesManager;
import com.fh.service.system.user.UserManager;
import com.fh.service.tmplConfigDict.tmplconfigdict.TmplConfigDictManager;
import com.fh.util.PageData;
import com.fh.util.StringUtil;

import net.sf.json.JSONArray;

/**
 * 字典信息通用类
 * 
 * @ClassName: DictsUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author jiachao
 * @date 2017年5月10日
 *
 */
public class DictsUtil {
	/**
	 * 根据字典父编码获取所有字典详情信息
	 * 
	 * @param parentBianma
	 *            字典父编码
	 * @return
	 * @throws Exception
	 */
	public static List<Dictionaries> getDictsByParentBianma(DictionariesManager dictionariesService,String parentBianma) throws Exception {
		List<Dictionaries> areaList = null;
		PageData pdDict = new PageData();
		pdDict.put("BIANMA", parentBianma);
		PageData pdDictResult = dictionariesService.findByBianma(pdDict);
		if (pdDictResult != null && !StringUtil.isEmpty(StringUtil.toString(pdDictResult.get("DICTIONARIES_ID"), ""))) {
			areaList = dictionariesService.listAllDict(StringUtil.toString(pdDictResult.get("DICTIONARIES_ID"), ""));
		}
		return areaList;
	}

	/**
	 * 根据字典名称获取字典信息，生成Jqgrid editOptions和SearchOptions所需的Select格式。
	 * 
	 * @param dicName
	 *            字典名称
	 * @return
	 * @throws Exception
	 */
	public static String getDicValue(DictionariesManager dictionariesService,String dicName) throws Exception {
		StringBuilder ret = new StringBuilder();
		Map<String, String> dicAdd = new HashMap<String, String>();
		List<Dictionaries> dicList = dictionariesService.getSysDictionaries(dicName);
		for (Dictionaries dic : dicList) {
			if (ret != null && !ret.toString().trim().equals("")) {
				ret.append(";");
			}
			ret.append(dic.getDICT_CODE() + ":" + dic.getNAME());
			dicAdd.put(dic.getDICT_CODE(), dic.getNAME());
		}
		return ret.toString();
	}

	/**
	 * 获取组织结构信息，生成Jqgrid editOptions和SearchOptions所需的Select格式。
	 * @param departmentService
	 * @return
	 * @throws Exception
	 */
	public static String getDepartmentValue(DepartmentManager departmentService) throws Exception {
		StringBuilder ret = new StringBuilder();
		PageData pd = new PageData();
		List<Department> listPara = (List<Department>) departmentService.getDepartDic(pd);
		for (Department dic : listPara) {
			if (ret != null && !ret.toString().trim().equals("")) {
				ret.append(";");
			}
			ret.append(dic.getDEPARTMENT_CODE() + ":" + dic.getNAME());
		}
		return ret.toString();
	}
	
	/**
	 * 获取组织结构信息，生成Jqgrid editOptions和SearchOptions所需的Select格式。
	 * @param departmentService
	 * @return
	 * @throws Exception
	 */
	public static String getSysUserValue(UserManager userService) throws Exception {
		StringBuilder ret = new StringBuilder();
		PageData pd = new PageData();
		List<PageData> listPara = (List<PageData>) userService.getUserValue(pd);
		for (PageData dic : listPara) {
			if (ret != null && !ret.toString().trim().equals("")) {
				ret.append(";");
			}
			ret.append(dic.getString("USER_ID") + ":" + dic.getString("NAME"));
		}
		return ret.toString();
	}
	
	/**
	 * 获取自定类型信息，生成Jqgrid editOptions和SearchOptions所需的Select格式。
	 * @param departmentService
	 * @return
	 * @throws Exception
	 */
	public static String getDicTypeValue(TmplConfigDictManager tmplconfigdictService) throws Exception {
		StringBuilder ret = new StringBuilder();
		PageData pd = new PageData();
		List<PageData> listPara = (List<PageData>) tmplconfigdictService.listAll(pd);
		for (PageData dic : listPara) {
			if (ret != null && !ret.toString().trim().equals("")) {
				ret.append(";");
			}
			ret.append(dic.getString("DICT_CODE") + ":" + dic.getString("DICT_NAME"));
		}
		return ret.toString();
	}
	
	/**
	 * 获取组织机构树数据源
	 * @param departmentService
	 * @return
	 * @throws Exception
	 */
	public static String getDepartmentSelectTreeSource(DepartmentManager departmentService) throws Exception{
		List<PageData> zdepartmentPdList = new ArrayList<PageData>();
		JSONArray arr = JSONArray.fromObject(departmentService.listAllDepartmentToSelect("0", zdepartmentPdList));
		return (null == arr ? "" : arr.toString());
	}
	
	/**
	 * 获取组织机构树数据源
	 * @param departmentService
	 * @return
	 * @throws Exception
	 */
	public static List<PageData> getDepartmentSelectTreeSourceList(DepartmentManager departmentService) throws Exception{
		List<PageData> zdepartmentPdList = new ArrayList<PageData>();
		return departmentService.listAllDepartmentToSelect("0", zdepartmentPdList);
	}
}
