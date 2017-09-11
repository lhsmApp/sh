package com.fh.util.enums;

/**
 * 配置表
* @ClassName: SysConfig
* @Description: TODO(这里用一句话描述这个类的作用)
* @author 张晓柳
* @date 2017年6月22日
*
 */
public class SysConfigKeyCode {
	//定义系统区间
	public static final String SystemDataTime = "SystemDataTime";
	//定义工资数据的显示条件
	//public static final String ChkMktGRPCOND = "ChkMktGRPCOND";
	//public static final String ChkRunGRPCOND = "ChkRunGRPCOND";
	//public static final String ChkEmployGRPCOND = "ChkEmployGRPCOND";
	
	//定义汇总字段
	//工资核算分组字段，所属单位、员工组分组
	public static final String ChkStaffGRP = "ChkStaffGRP";
	//社保核算分组字典，所属单位
	public static final String ChkSocialGRP = "ChkSocialGRP";
	//公积金核算分组字典，所属单位
	public static final String ChkHouseGRP = "ChkHouseGRP";
	

	//定义汇总字段
	//合同化工资汇总
	public static final String ContractGRPCond = "ContractGRPCond";
	//市场化工资汇总
	public static final String MarketGRPCond = "MarketGRPCond";
	//运行人员工资汇总
	public static final String OperLaborGRPCond = "OperLaborGRPCond";
	//系统内工资汇总
	public static final String SysLaborGRPCond = "SysLaborGRPCond";
	//劳务派遣工资汇总
	public static final String LaborGRPCond = "LaborGRPCond";
	//社保汇总
	public static final String SocialIncGRPCond = "SocialIncGRPCond";
	//公积金汇总
	public static final String HouseFundGRPCond = "HouseFundGRPCond";
	
}
