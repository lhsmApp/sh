package com.fh.controller.common;


import java.util.List;

import com.fh.util.PageData;

/**
 * 
 * 
 * @ClassName: SqlInString
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 张晓柳
 * @date 2017年8月18日
 *
 */
public class QueryFeildString {
	
	public static String getQueryFeild(PageData pd, List<String> feildList) throws Exception{
		//"BUSI_DATE", "DEPT_CODE", "USER_CATG", "USER_GROP", "CUST_COL7"
		String BUSI_DATE = "";
		String DEPT_CODE = "";
		String USER_CATG = "";
		String USER_GROP = "";
		String CUST_COL7 = "";
		if(feildList!=null && feildList.size()>0){
			if(feildList.contains("BUSI_DATE")){
				BUSI_DATE = pd.getString("BUSI_DATE");
			}
			if(feildList.contains("DEPT_CODE")){
				DEPT_CODE = pd.getString("DEPT_CODE");
			}
			if(feildList.contains("USER_CATG")){
				USER_CATG = pd.getString("USER_CATG");
			}
			if(feildList.contains("USER_GROP")){
				USER_GROP = pd.getString("USER_GROP");
			}
			if(feildList.contains("CUST_COL7")){
				CUST_COL7 = pd.getString("CUST_COL7");
			}
		}
		String QueryFeild = "";
		if(BUSI_DATE!=null && !BUSI_DATE.trim().equals("")){
			QueryFeild += " and BUSI_DATE like '%" + BUSI_DATE.trim() + "%' ";
		}
		if(DEPT_CODE!=null && !DEPT_CODE.trim().equals("")){
			String strIn = getSqlInString(DEPT_CODE);
			if(strIn!=null && !strIn.equals("")){
				QueryFeild += " and DEPT_CODE in (" + strIn + ") ";
			}
		}
		if(USER_CATG!=null && !USER_CATG.trim().equals("")){
			String strIn = getSqlInString(USER_CATG);
			if(strIn!=null && !strIn.equals("")){
				QueryFeild += " and USER_CATG in (" + strIn + ") ";
			}
		}
		if(USER_GROP!=null && !USER_GROP.trim().equals("")){
			String strIn = getSqlInString(USER_GROP);
			if(strIn!=null && !strIn.equals("")){
				QueryFeild += " and USER_GROP in (" + strIn + ") ";
			}
		}
		if(CUST_COL7!=null && !CUST_COL7.trim().equals("")){
			String strIn = getSqlInString(CUST_COL7);
			if(strIn!=null && !strIn.equals("")){
				QueryFeild += " and CUST_COL7 in (" + strIn + ") ";
			}
		}
		return QueryFeild;
	}
	
	/**
	 * 
	 * 
	 * @param 
	 * @return
	 * @throws Exception
	 */
	private static String getSqlInString(String strList) throws Exception {
		String strIn = "";
		if(strList!=null && !strList.trim().equals("")){
			String[] list = strList.replace(" ", "").split(",");
			for(String str : list){
				if(strIn!=null && !strIn.trim().equals("")){
					strIn += ",";
				}
				strIn += "'" + str +"'";
			}
		}
		return strIn;
	}
	
	public static String getFieldSelectKey(List<String> keyListBase, String keyExtra) throws Exception{
		String strReturn = "";
		if(keyListBase!=null && keyListBase.size()>0){
			for(String each : keyListBase){
				strReturn += ", " + each + " " + each + keyExtra;
			}
		}
		return strReturn.trim();
	}
	
	public static String tranferSumFieldToString(List<String> SumField){
		StringBuilder ret = new StringBuilder();
		for(String field : SumField){
			if(!ret.toString().trim().equals("")){
				ret.append(",");
			}
			ret.append(field);
		}
		return ret.toString();
	}
}
