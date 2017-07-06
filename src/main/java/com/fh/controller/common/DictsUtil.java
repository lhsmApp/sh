package com.fh.controller.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fh.entity.system.Department;
import com.fh.entity.system.Dictionaries;
import com.fh.service.fhoa.department.DepartmentManager;
import com.fh.service.system.dictionaries.DictionariesManager;
import com.fh.service.tmplConfigDict.tmplconfigdict.TmplConfigDictManager;
import com.fh.util.PageData;
import com.fh.util.StringUtil;

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
	private DictionariesManager dictionariesService;
	private DepartmentManager departmentService;
	
	public DictsUtil(DictionariesManager dictionariesService,DepartmentManager departmentService){
		this.dictionariesService=dictionariesService;
		this.departmentService=departmentService;
	}
	
	/**
	 * 根据字典父编码获取所有字典详情信息
	 * 
	 * @param parentBianma
	 *            字典父编码
	 * @return
	 * @throws Exception 
	 */
	public List<Dictionaries> getDictsByParentBianma(String parentBianma) throws Exception {
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
	 * 根据字典名称获取字典信息，获取组织结构信息，生成Jqgrid editOptions和SearchOptions所需的Select格式。
	 * @param dicName 字典名称
	 * @param dicType 字典类型，1取自系统字典表 2取自组织机构表
	 * @return
	 * @throws Exception
	 */
	public String getDicValue(String dicName,String dicType) throws Exception{
		StringBuilder ret = new StringBuilder();
		Map<String, String> dicAdd = new HashMap<String, String>();
		if(dicType.equals("1")){
			List<Dictionaries> dicList = dictionariesService.getSysDictionaries(dicName);
			for(Dictionaries dic : dicList){
				if(ret!=null && !ret.toString().trim().equals("")){
					ret.append(";");
				}
				ret.append(dic.getBIANMA() + ":" + dic.getNAME());
				dicAdd.put(dic.getBIANMA(), dic.getNAME());
			}
		} else if(dicType.equals("2")){
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
		return ret.toString();
	}
}
