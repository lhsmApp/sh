package com.fh.service.glzrzx.glzrzx;

import java.util.List;

import com.fh.entity.ZrzxModel;

/** 
 * 说明： 责任中心
 * 创建人：zhangxiaoliu
 * 创建时间：2017-07-18
 * @version
 */
public interface GlZrzxManager{

	/**获取数据
	 * 张晓柳
	 * @param pd
	 * @throws Exception
	 */
	public List<ZrzxModel> findDeptFromZrzx(String Dept)throws Exception;
	
}

