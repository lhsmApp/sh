package com.fh.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fh.entity.system.User;
import com.fh.service.staffDetail.staffdetail.StaffDetailManager;
import com.fh.util.PageData;

public class StaffDetailModel {
	
	private String BILL_CODE__ = " "; 
	private String BUSI_DATE__ = " "; 
	private String USER_CODE__ = " "; 
	private String BILL_CODE = " "; 
	private String BUSI_DATE = " "; 
	private String USER_CODE = " "; 
	private String USER_NAME = " "; 
	private String STAFF_IDENT = " ";
	private String BANK_CARD = " "; 
	private String USER_GROP = " ";
	private String USER_CATG = " "; 
	private String DEPT_CODE = " "; 
	private String ORG_UNIT = " "; 
	private String SAL_RANGE = " "; 
	private Double POST_SALY = (double) 0;
	private Double BONUS = (double) 0; 
	private Double CASH_BONUS = (double) 0; 
	private Double WORK_OT = (double) 0; 
	private Double BACK_SALY = (double) 0; 
	private Double RET_SALY = (double) 0; 
	private Double CHK_CASH = (double) 0; 
	private Double INTR_SGL_AWAD = (double) 0; 
	private Double SENY_ALLE = (double) 0; 
	private Double POST_ALLE = (double) 0; 
	private Double NS_ALLE = (double) 0; 
	private Double AREA_ALLE = (double) 0; 
	private Double EXPT_ALLE = (double) 0; 
	private Double TECH_ALLE = (double) 0; 
	private Double LIVE_EXPE = (double) 0; 
	private Double LIVE_ALLE = (double) 0; 
	private Double LEAVE_DM = (double) 0; 
	private Double HOUSE_ALLE = (double) 0; 
	private Double ITEM_ALLE = (double) 0; 
	private Double MEAL_EXPE = (double) 0;
	private Double TRF_ALLE = (double) 0; 
	private Double TEL_EXPE = (double) 0; 
	private Double HLDY_ALLE = (double) 0; 
	private Double KID_ALLE = (double) 0; 
	private Double COOL_EXPE = (double) 0; 
	private Double EXT_SGL_AWAD = (double) 0; 
	private Double PRE_TAX_PLUS = (double) 0; 
	private Double GROSS_PAY = (double) 0; 
	private Double ENDW_INS = (double) 0; 
	private Double UNEMPL_INS = (double) 0; 
	private Double MED_INS = (double) 0; 
	private Double CASD_INS = (double) 0; 
	private Double HOUSE_FUND = (double) 0; 
	private Double SUP_PESN = (double) 0; 
	private Double TAX_BASE_ADJ = (double) 0;
	private Double ACCRD_TAX = (double) 0; 
	private Double AFTER_TAX = (double) 0; 
	private Double ACT_SALY = (double) 0; 
	private Double GUESS_DIFF = (double) 0; 
	private Double CUST_COL1 = (double) 0; 
	private Double CUST_COL2 = (double) 0; 
	private Double CUST_COL3 = (double) 0; 
	private Double CUST_COL4 = (double) 0; 
	private Double CUST_COL5 = (double) 0; 
	private Double CUST_COL6 = (double) 0; 
	private String CUST_COL7 = " "; 
	private String CUST_COL8 = " "; 
	private String CUST_COL9 = " "; 
	private String CUST_COL10 = " ";
	private String ESTB_DEPT = " ";
	public String getBILL_CODE__() {
		return BILL_CODE__;
	}
	public void setBILL_CODE__(String bILL_CODE__) {
		BILL_CODE__ = bILL_CODE__;
	}
	public String getBUSI_DATE__() {
		return BUSI_DATE__;
	}
	public void setBUSI_DATE__(String bUSI_DATE__) {
		BUSI_DATE__ = bUSI_DATE__;
	}
	public String getUSER_CODE__() {
		return USER_CODE__;
	}
	public void setUSER_CODE__(String uSER_CODE__) {
		USER_CODE__ = uSER_CODE__;
	}
	public String getBILL_CODE() {
		return BILL_CODE;
	}
	public void setBILL_CODE(String bILL_CODE) {
		BILL_CODE = bILL_CODE;
	}
	public String getBUSI_DATE() {
		return BUSI_DATE;
	}
	public void setBUSI_DATE(String bUSI_DATE) {
		BUSI_DATE = bUSI_DATE;
	}
	public String getUSER_CODE() {
		return USER_CODE;
	}
	public void setUSER_CODE(String uSER_CODE) {
		USER_CODE = uSER_CODE;
	}
	public String getUSER_NAME() {
		return USER_NAME;
	}
	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}
	public String getSTAFF_IDENT() {
		return STAFF_IDENT;
	}
	public void setSTAFF_IDENT(String sTAFF_IDENT) {
		STAFF_IDENT = sTAFF_IDENT;
	}
	public String getBANK_CARD() {
		return BANK_CARD;
	}
	public void setBANK_CARD(String bANK_CARD) {
		BANK_CARD = bANK_CARD;
	}
	public String getUSER_GROP() {
		return USER_GROP;
	}
	public void setUSER_GROP(String uSER_GROP) {
		USER_GROP = uSER_GROP;
	}
	public String getUSER_CATG() {
		return USER_CATG;
	}
	public void setUSER_CATG(String uSER_CATG) {
		USER_CATG = uSER_CATG;
	}
	public String getDEPT_CODE() {
		return DEPT_CODE;
	}
	public void setDEPT_CODE(String dEPT_CODE) {
		DEPT_CODE = dEPT_CODE;
	}
	public String getORG_UNIT() {
		return ORG_UNIT;
	}
	public void setORG_UNIT(String oRG_UNIT) {
		ORG_UNIT = oRG_UNIT;
	}
	public String getSAL_RANGE() {
		return SAL_RANGE;
	}
	public void setSAL_RANGE(String sAL_RANGE) {
		SAL_RANGE = sAL_RANGE;
	}
	public Double getPOST_SALY() {
		return POST_SALY;
	}
	public void setPOST_SALY(Double pOST_SALY) {
		POST_SALY = pOST_SALY;
	}
	public Double getBONUS() {
		return BONUS;
	}
	public void setBONUS(Double bONUS) {
		BONUS = bONUS;
	}
	public Double getCASH_BONUS() {
		return CASH_BONUS;
	}
	public void setCASH_BONUS(Double cASH_BONUS) {
		CASH_BONUS = cASH_BONUS;
	}
	public Double getWORK_OT() {
		return WORK_OT;
	}
	public void setWORK_OT(Double wORK_OT) {
		WORK_OT = wORK_OT;
	}
	public Double getBACK_SALY() {
		return BACK_SALY;
	}
	public void setBACK_SALY(Double bACK_SALY) {
		BACK_SALY = bACK_SALY;
	}
	public Double getRET_SALY() {
		return RET_SALY;
	}
	public void setRET_SALY(Double rET_SALY) {
		RET_SALY = rET_SALY;
	}
	public Double getCHK_CASH() {
		return CHK_CASH;
	}
	public void setCHK_CASH(Double cHK_CASH) {
		CHK_CASH = cHK_CASH;
	}
	public Double getINTR_SGL_AWAD() {
		return INTR_SGL_AWAD;
	}
	public void setINTR_SGL_AWAD(Double iNTR_SGL_AWAD) {
		INTR_SGL_AWAD = iNTR_SGL_AWAD;
	}
	public Double getSENY_ALLE() {
		return SENY_ALLE;
	}
	public void setSENY_ALLE(Double sENY_ALLE) {
		SENY_ALLE = sENY_ALLE;
	}
	public Double getPOST_ALLE() {
		return POST_ALLE;
	}
	public void setPOST_ALLE(Double pOST_ALLE) {
		POST_ALLE = pOST_ALLE;
	}
	public Double getNS_ALLE() {
		return NS_ALLE;
	}
	public void setNS_ALLE(Double nS_ALLE) {
		NS_ALLE = nS_ALLE;
	}
	public Double getAREA_ALLE() {
		return AREA_ALLE;
	}
	public void setAREA_ALLE(Double aREA_ALLE) {
		AREA_ALLE = aREA_ALLE;
	}
	public Double getEXPT_ALLE() {
		return EXPT_ALLE;
	}
	public void setEXPT_ALLE(Double eXPT_ALLE) {
		EXPT_ALLE = eXPT_ALLE;
	}
	public Double getTECH_ALLE() {
		return TECH_ALLE;
	}
	public void setTECH_ALLE(Double tECH_ALLE) {
		TECH_ALLE = tECH_ALLE;
	}
	public Double getLIVE_EXPE() {
		return LIVE_EXPE;
	}
	public void setLIVE_EXPE(Double lIVE_EXPE) {
		LIVE_EXPE = lIVE_EXPE;
	}
	public Double getLIVE_ALLE() {
		return LIVE_ALLE;
	}
	public void setLIVE_ALLE(Double lIVE_ALLE) {
		LIVE_ALLE = lIVE_ALLE;
	}
	public Double getLEAVE_DM() {
		return LEAVE_DM;
	}
	public void setLEAVE_DM(Double lEAVE_DM) {
		LEAVE_DM = lEAVE_DM;
	}
	public Double getHOUSE_ALLE() {
		return HOUSE_ALLE;
	}
	public void setHOUSE_ALLE(Double hOUSE_ALLE) {
		HOUSE_ALLE = hOUSE_ALLE;
	}
	public Double getITEM_ALLE() {
		return ITEM_ALLE;
	}
	public void setITEM_ALLE(Double iTEM_ALLE) {
		ITEM_ALLE = iTEM_ALLE;
	}
	public Double getMEAL_EXPE() {
		return MEAL_EXPE;
	}
	public void setMEAL_EXPE(Double mEAL_EXPE) {
		MEAL_EXPE = mEAL_EXPE;
	}
	public Double getTRF_ALLE() {
		return TRF_ALLE;
	}
	public void setTRF_ALLE(Double tRF_ALLE) {
		TRF_ALLE = tRF_ALLE;
	}
	public Double getTEL_EXPE() {
		return TEL_EXPE;
	}
	public void setTEL_EXPE(Double tEL_EXPE) {
		TEL_EXPE = tEL_EXPE;
	}
	public Double getHLDY_ALLE() {
		return HLDY_ALLE;
	}
	public void setHLDY_ALLE(Double hLDY_ALLE) {
		HLDY_ALLE = hLDY_ALLE;
	}
	public Double getKID_ALLE() {
		return KID_ALLE;
	}
	public void setKID_ALLE(Double kID_ALLE) {
		KID_ALLE = kID_ALLE;
	}
	public Double getCOOL_EXPE() {
		return COOL_EXPE;
	}
	public void setCOOL_EXPE(Double cOOL_EXPE) {
		COOL_EXPE = cOOL_EXPE;
	}
	public Double getEXT_SGL_AWAD() {
		return EXT_SGL_AWAD;
	}
	public void setEXT_SGL_AWAD(Double eXT_SGL_AWAD) {
		EXT_SGL_AWAD = eXT_SGL_AWAD;
	}
	public Double getPRE_TAX_PLUS() {
		return PRE_TAX_PLUS;
	}
	public void setPRE_TAX_PLUS(Double pRE_TAX_PLUS) {
		PRE_TAX_PLUS = pRE_TAX_PLUS;
	}
	public Double getGROSS_PAY() {
		return GROSS_PAY;
	}
	public void setGROSS_PAY(Double gROSS_PAY) {
		GROSS_PAY = gROSS_PAY;
	}
	public Double getENDW_INS() {
		return ENDW_INS;
	}
	public void setENDW_INS(Double eNDW_INS) {
		ENDW_INS = eNDW_INS;
	}
	public Double getUNEMPL_INS() {
		return UNEMPL_INS;
	}
	public void setUNEMPL_INS(Double uNEMPL_INS) {
		UNEMPL_INS = uNEMPL_INS;
	}
	public Double getMED_INS() {
		return MED_INS;
	}
	public void setMED_INS(Double mED_INS) {
		MED_INS = mED_INS;
	}
	public Double getCASD_INS() {
		return CASD_INS;
	}
	public void setCASD_INS(Double cASD_INS) {
		CASD_INS = cASD_INS;
	}
	public Double getHOUSE_FUND() {
		return HOUSE_FUND;
	}
	public void setHOUSE_FUND(Double hOUSE_FUND) {
		HOUSE_FUND = hOUSE_FUND;
	}
	public Double getSUP_PESN() {
		return SUP_PESN;
	}
	public void setSUP_PESN(Double sUP_PESN) {
		SUP_PESN = sUP_PESN;
	}
	public Double getTAX_BASE_ADJ() {
		return TAX_BASE_ADJ;
	}
	public void setTAX_BASE_ADJ(Double tAX_BASE_ADJ) {
		TAX_BASE_ADJ = tAX_BASE_ADJ;
	}
	public Double getACCRD_TAX() {
		return ACCRD_TAX;
	}
	public void setACCRD_TAX(Double aCCRD_TAX) {
		ACCRD_TAX = aCCRD_TAX;
	}
	public Double getAFTER_TAX() {
		return AFTER_TAX;
	}
	public void setAFTER_TAX(Double aFTER_TAX) {
		AFTER_TAX = aFTER_TAX;
	}
	public Double getACT_SALY() {
		return ACT_SALY;
	}
	public void setACT_SALY(Double aCT_SALY) {
		ACT_SALY = aCT_SALY;
	}
	public Double getGUESS_DIFF() {
		return GUESS_DIFF;
	}
	public void setGUESS_DIFF(Double gUESS_DIFF) {
		GUESS_DIFF = gUESS_DIFF;
	}
	public Double getCUST_COL1() {
		return CUST_COL1;
	}
	public void setCUST_COL1(Double cUST_COL1) {
		CUST_COL1 = cUST_COL1;
	}
	public Double getCUST_COL2() {
		return CUST_COL2;
	}
	public void setCUST_COL2(Double cUST_COL2) {
		CUST_COL2 = cUST_COL2;
	}
	public Double getCUST_COL3() {
		return CUST_COL3;
	}
	public void setCUST_COL3(Double cUST_COL3) {
		CUST_COL3 = cUST_COL3;
	}
	public Double getCUST_COL4() {
		return CUST_COL4;
	}
	public void setCUST_COL4(Double cUST_COL4) {
		CUST_COL4 = cUST_COL4;
	}
	public Double getCUST_COL5() {
		return CUST_COL5;
	}
	public void setCUST_COL5(Double cUST_COL5) {
		CUST_COL5 = cUST_COL5;
	}
	public Double getCUST_COL6() {
		return CUST_COL6;
	}
	public void setCUST_COL6(Double cUST_COL6) {
		CUST_COL6 = cUST_COL6;
	}
	public String getCUST_COL7() {
		return CUST_COL7;
	}
	public void setCUST_COL7(String cUST_COL7) {
		CUST_COL7 = cUST_COL7;
	}
	public String getCUST_COL8() {
		return CUST_COL8;
	}
	public void setCUST_COL8(String cUST_COL8) {
		CUST_COL8 = cUST_COL8;
	}
	public String getCUST_COL9() {
		return CUST_COL9;
	}
	public void setCUST_COL9(String cUST_COL9) {
		CUST_COL9 = cUST_COL9;
	}
	public String getCUST_COL10() {
		return CUST_COL10;
	}
	public void setCUST_COL10(String cUST_COL10) {
		CUST_COL10 = cUST_COL10;
	}
	public String getESTB_DEPT() {
		return ESTB_DEPT;
	}
	public void setESTB_DEPT(String eSTB_DEPT) {
		ESTB_DEPT = eSTB_DEPT;
	}

	//查询表的主键字段后缀，区别于主键字段，用于修改或删除
	public static String KeyExtra = "__";
	//查询表的主键字段
	public static List<String> KeyList = Arrays.asList("BILL_CODE","BUSI_DATE","USER_CODE");
	//查询表的所有字段
	public static List<String> FieldList = Arrays.asList(
			"BILL_CODE", 
			"BUSI_DATE", 
			"USER_CODE", 
			"USER_NAME", 
			"STAFF_IDENT",
			"BANK_CARD", 
			"USER_GROP",
			"USER_CATG", 
			"DEPT_CODE", 
			"ORG_UNIT", 
			"SAL_RANGE", 
			"POST_SALY",
			"BONUS", 
			"CASH_BONUS", 
			"WORK_OT", 
			"BACK_SALY", 
			"RET_SALY", 
			"CHK_CASH", 
			"INTR_SGL_AWAD", 
			"SENY_ALLE", 
			"POST_ALLE", 
			"NS_ALLE", 
			"AREA_ALLE", 
			"EXPT_ALLE", 
			"TECH_ALLE", 
			"LIVE_EXPE", 
			"LIVE_ALLE", 
			"LEAVE_DM", 
			"HOUSE_ALLE", 
			"ITEM_ALLE", 
			"MEAL_EXPE",
			"TRF_ALLE", 
			"TEL_EXPE", 
			"HLDY_ALLE", 
			"KID_ALLE", 
			"COOL_EXPE", 
			"EXT_SGL_AWAD", 
			"PRE_TAX_PLUS", 
			"GROSS_PAY", 
			"ENDW_INS", 
			"UNEMPL_INS", 
			"MED_INS", 
			"CASD_INS", 
			"HOUSE_FUND", 
			"SUP_PESN", 
			"TAX_BASE_ADJ",
			"ACCRD_TAX", 
			"AFTER_TAX", 
			"ACT_SALY", 
			"GUESS_DIFF", 
			"CUST_COL1", 
			"CUST_COL2", 
			"CUST_COL3", 
			"CUST_COL4", 
			"CUST_COL5", 
			"CUST_COL6", 
			"CUST_COL7", 
			"CUST_COL8", 
			"CUST_COL9", 
			"CUST_COL10",
			"ESTB_DEPT");

	//用语句查询出数据库表的所有字段及其属性；拼接成jqgrid全部列
	public static Map<String, Map<String, Object>> jqGridColModelAll(List<TableColumns> columns, String DepartCode, StaffDetailManager staffdetailService, Map<String, Object> DefaultValueList) 
			throws Exception{
		Map<String, Map<String, Object>> list = new HashMap<String, Map<String, Object>>();

		for(TableColumns col : columns){
			DefaultValueList.put(col.getColumn_name(), col.getColumn_default());
			
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
				model_name.append(" align: 'right', search: false, sorttype: 'number', ");
				model_edittype.append(" edittype:'text', formatter: 'number', editoptions:{maxlength:'" + intLength + "', number: true} ");
			} else{
				//if(col.getColumn_name().equals("USER_NAME")){
				//	String strUserValue = getUserDic(DepartCode, staffdetailService);
				//	//选择
				//	model_edittype.append(" edittype:'select', ");
				//	model_edittype.append(" editoptions:{value:'" + strUserValue + "'}, ");
				//	//翻译
				//	model_edittype.append(" formatter: 'select', ");
				//	model_edittype.append(" formatoptions: {value: '" + strUserValue + "'}, ");
				//	//查询
				//	model_edittype.append(" stype: 'select', ");
				//	model_edittype.append(" searchoptions: {value: ':[All];" + strUserValue + "'} ");
				//} else {
					if(intLength > 50){
						model_name.append(" width: '200', ");
						model_edittype.append(" edittype:'textarea', ");
					} else{
						model_name.append(" width: '130', ");
						model_edittype.append(" edittype:'text', ");
					}
					model_edittype.append(" editoptions:{maxlength:'" + intLength + "'} ");
				//}
			}

			if(col.getColumn_name().equals("USER_CODE")){
				model_name.append(" editrules:{required:true}, ");
			}
			//if(col.getColumn_name().equals("USER_NAME")){
			//	model_name.append(" name: 'USER_CODE' ");
			//} else {
				model_name.append(" name: '"+ col.getColumn_name() +"' ");
			//}
			MapAdd.put("name", model_name.toString());
			MapAdd.put("edittype", model_edittype.toString());
			MapAdd.put("notedit", model_notedit.toString());
			list.put(col.getColumn_name(), MapAdd);
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
		list.put("ESTB_DEPT", Map_ESTB_DEPT); */
		
		return list;
	}
	
	public static int getColumnLength(String Column_type, String Data_type){
		int ret = 0;
		String[] listLength = Column_type.replace(Data_type, "").replace("(", "").replace(")", "").split(",");
		for(String length : listLength){
			ret += Integer.parseInt(length);
		}
		return ret;
	}
	
	/*public static String getUserDic(String department, StaffDetailManager staffdetailService) throws Exception{
		StringBuilder ret = new StringBuilder();
		List<User> dicList = staffdetailService.getUsersInDepart(department);
		for(User dic : dicList){
			if(ret!=null && !ret.toString().trim().equals("")){
				ret.append("; ");
			}
			ret.append(dic.getUSER_ID() + ":" + dic.getUSERNAME());
		}
		return ret.toString();
	}*/
	
	public static void setModelDefault(PageData pd, Map<String, Object> DefaultValueList) throws ClassNotFoundException{
		/*Class<?> clazz = Class.forName("java/com/fh/entity/StaffDetailModel");//根据类名获得其对应的Class对象 写上你想要的类名就是了 注意是全名 如果有包的话要加上 比如java.Lang.String
	    Field[] fields = clazz.getDeclaredFields();//根据Class对象获得属性 私有的也可以获得
	    for(Field f : fields) {
	    	String ss = f.getType().getName();
	       System.out.println(f.getType().getName());//打印每个属性的类型名字
	    } */
		for(String field : FieldList){
			if(!(pd.containsKey(field.toUpperCase()) 
					&& pd.get(field.toUpperCase()) != null
					&& !pd.getString(field.toUpperCase()).trim().equals(""))){
				Object defaultValue = DefaultValueList.get(field.toUpperCase());
		        pd.put(field.toUpperCase(), defaultValue);
			}
		}
	}
	
}