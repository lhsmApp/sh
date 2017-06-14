package com.fh.controller.common;

import java.util.List;

import javax.annotation.Resource;

import com.fh.entity.system.Dictionaries;
import com.fh.service.system.dictionaries.DictionariesManager;
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
	private DictionariesManager dictionariesManager;
	
	public DictsUtil(DictionariesManager dictionariesManager){
		this.dictionariesManager=dictionariesManager;
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
		PageData pdDictResult = dictionariesManager.findByBianma(pdDict);
		if (pdDictResult != null && !StringUtil.isEmpty(StringUtil.toString(pdDictResult.get("DICTIONARIES_ID"), ""))) {
			areaList = dictionariesManager.listAllDict(StringUtil.toString(pdDictResult.get("DICTIONARIES_ID"), ""));
		}
		return areaList;
	}
}
