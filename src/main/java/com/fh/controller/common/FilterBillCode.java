package com.fh.controller.common;

import com.fh.util.enums.BillState;

/**
 * 单号
* @ClassName: FilterBillCode
* @Description: TODO(这里用一句话描述这个类的作用)
* @author 张晓柳
* @date 2017年8月21日
*
 */
public class FilterBillCode {

	public static String getHelpfulBillCode(String tableNameSummy){
		String strReturn = " and BILL_CODE not in (select BILL_CODE from " + tableNameSummy + " where BILL_STATE = " + BillState.Invalid.getNameKey() + ") ";
		return strReturn;
	}
}
	