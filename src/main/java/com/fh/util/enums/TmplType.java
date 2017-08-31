package com.fh.util.enums;

public enum TmplType {
	TB_STAFF_DETAIL_CONTRACT("S001","合同化工资明细导入表"),
	TB_STAFF_DETAIL_MARKET("S002","市场化工资明细导入表"),
	TB_STAFF_DETAIL_SYS_LABOR("S003","系统内劳务工资明细导入表"),
	TB_STAFF_DETAIL_OPER_LABOR("S004","运行人员工资明细导入表"),
	TB_STAFF_DETAIL_LABOR("S005","劳务派遣工资明细导入表"),
	TB_STAFF_SUMMY_CONTRACT("S006","合同化工资汇总表"),
	TB_STAFF_SUMMY_MARKET("S007","市场化工资汇总表"),
	TB_STAFF_SUMMY_SYS_LABOR("S008","系统内劳务工资汇总表"),
	TB_STAFF_SUMMY_OPER_LABOR("S009","运行人员工资汇总表"),
	
	TB_STAFF_SUMMY_LABOR("S010","劳务派遣工资汇总表"),
	TB_STAFF_AUDIT_CONTRACT("S011","合同化工资核算表"),
	TB_STAFF_AUDIT_MARKET("S012","市场化工资核算表"),
	TB_STAFF_AUDIT_SYS_LABOR("S013","系统内劳务工资核算表"),
	TB_STAFF_AUDIT_OPER_LABOR("S014","运行人员工资核算表"),
	TB_STAFF_AUDIT_LABOR("S015","劳务派遣工资核算表"),
	TB_STAFF_TRANSFER_CONTRACT("S016","合同化工资传输表"),
	TB_STAFF_TRANSFER_MARKET("S017","市场化工资传输表"),
	TB_STAFF_TRANSFER_SYS_LABOR("S018","系统内劳务工资传输表"),
	
	TB_STAFF_TRANSFER_OPER_LABOR("S019","运行人员工资传输表"),
	TB_STAFF_TRANSFER_LABOR("S020","劳务派遣工资传输表"),
	TB_SOCIAL_INC_DETAIL("SI001","社保明细导入表"),
	TB_SOCIAL_INC_SUMMY("SI002","社保汇总表"),
	TB_SOCIAL_INC_AUDIT("SI003","社保核算表"),
	TB_SOCIAL_INC_TRANSFER("SI004","社保传输表"),
	TB_HOUSE_FUND_DETAIL("HF001","公积金明细导入表"),
	TB_HOUSE_FUND_SUMMY("HF002","公积金汇总表"),
	TB_HOUSE_FUND_AUDIT("HF003","公积金核算表"),
	TB_HOUSE_FUND_TRANSFER("HF004","公积金传输表");
	private String nameKey;

    private String nameValue;
    
    
    private TmplType(String nameKey, String nameValue) {
    	this.nameKey = nameKey;
        this.setNameValue(nameValue);
	}

	

	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}



	public String getNameValue() {
		return nameValue;
	}



	public void setNameValue(String nameValue) {
		this.nameValue = nameValue;
	}
	
	/** 
     * 根据key获取value 
     *  
     * @param key 
     *            : 键值key 
     * @return String 
     */  
    public static String getValueByKey(String key) {  
    	BillType[] enums = BillType.values();  
        for (int i = 0; i < enums.length; i++) {  
            if (enums[i].getNameKey().equals(key)) {  
                return enums[i].getNameValue();  
            }  
        }  
        return "";  
    }  
	
}
