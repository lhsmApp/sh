<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css" />
</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<!-- /section:basics/sidebar -->
	<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">
					
					<form action="staffsummy/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="STAFFSUMMY_ID" id="STAFFSUMMY_ID" value="${pd.STAFFSUMMY_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注1:</td>
								<td><input type="text" name="BILL_CODE" id="BILL_CODE" value="${pd.BILL_CODE}" maxlength="20" placeholder="这里输入备注1" title="备注1" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注2:</td>
								<td><input type="text" name="BUSI_DATE" id="BUSI_DATE" value="${pd.BUSI_DATE}" maxlength="8" placeholder="这里输入备注2" title="备注2" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注3:</td>
								<td><input type="text" name="ESTB_DEPT" id="ESTB_DEPT" value="${pd.ESTB_DEPT}" maxlength="30" placeholder="这里输入备注3" title="备注3" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注4:</td>
								<td><input type="text" name="USER_GROP" id="USER_GROP" value="${pd.USER_GROP}" maxlength="30" placeholder="这里输入备注4" title="备注4" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注5:</td>
								<td><input type="text" name="USER_CATG" id="USER_CATG" value="${pd.USER_CATG}" maxlength="30" placeholder="这里输入备注5" title="备注5" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注6:</td>
								<td><input type="text" name="DEPT_CODE" id="DEPT_CODE" value="${pd.DEPT_CODE}" maxlength="30" placeholder="这里输入备注6" title="备注6" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注7:</td>
								<td><input type="text" name="ORG_UNIT" id="ORG_UNIT" value="${pd.ORG_UNIT}" maxlength="30" placeholder="这里输入备注7" title="备注7" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注8:</td>
								<td><input type="text" name="SAL_RANGE" id="SAL_RANGE" value="${pd.SAL_RANGE}" maxlength="30" placeholder="这里输入备注8" title="备注8" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注9:</td>
								<td><input type="text" name="POST_SALY" id="POST_SALY" value="${pd.POST_SALY}" maxlength="12" placeholder="这里输入备注9" title="备注9" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注10:</td>
								<td><input type="text" name="BONUS" id="BONUS" value="${pd.BONUS}" maxlength="12" placeholder="这里输入备注10" title="备注10" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注11:</td>
								<td><input type="text" name="CASH_BONUS" id="CASH_BONUS" value="${pd.CASH_BONUS}" maxlength="12" placeholder="这里输入备注11" title="备注11" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注12:</td>
								<td><input type="text" name="WORK_OT" id="WORK_OT" value="${pd.WORK_OT}" maxlength="12" placeholder="这里输入备注12" title="备注12" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注13:</td>
								<td><input type="text" name="BACK_SALY" id="BACK_SALY" value="${pd.BACK_SALY}" maxlength="12" placeholder="这里输入备注13" title="备注13" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注14:</td>
								<td><input type="text" name="RET_SALY" id="RET_SALY" value="${pd.RET_SALY}" maxlength="12" placeholder="这里输入备注14" title="备注14" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注15:</td>
								<td><input type="text" name="CHK_CASH" id="CHK_CASH" value="${pd.CHK_CASH}" maxlength="12" placeholder="这里输入备注15" title="备注15" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注16:</td>
								<td><input type="text" name="INTR_SGL_AWAD" id="INTR_SGL_AWAD" value="${pd.INTR_SGL_AWAD}" maxlength="12" placeholder="这里输入备注16" title="备注16" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注17:</td>
								<td><input type="text" name="SENY_ALLE" id="SENY_ALLE" value="${pd.SENY_ALLE}" maxlength="12" placeholder="这里输入备注17" title="备注17" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注18:</td>
								<td><input type="text" name="POST_ALLE" id="POST_ALLE" value="${pd.POST_ALLE}" maxlength="12" placeholder="这里输入备注18" title="备注18" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注19:</td>
								<td><input type="text" name="NS_ALLE" id="NS_ALLE" value="${pd.NS_ALLE}" maxlength="12" placeholder="这里输入备注19" title="备注19" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注20:</td>
								<td><input type="text" name="AREA_ALLE" id="AREA_ALLE" value="${pd.AREA_ALLE}" maxlength="12" placeholder="这里输入备注20" title="备注20" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注21:</td>
								<td><input type="text" name="EXPT_ALLE" id="EXPT_ALLE" value="${pd.EXPT_ALLE}" maxlength="12" placeholder="这里输入备注21" title="备注21" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注22:</td>
								<td><input type="text" name="TECH_ALLE" id="TECH_ALLE" value="${pd.TECH_ALLE}" maxlength="12" placeholder="这里输入备注22" title="备注22" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注23:</td>
								<td><input type="text" name="LIVE_EXPE" id="LIVE_EXPE" value="${pd.LIVE_EXPE}" maxlength="12" placeholder="这里输入备注23" title="备注23" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注24:</td>
								<td><input type="text" name="LIVE_ALLE" id="LIVE_ALLE" value="${pd.LIVE_ALLE}" maxlength="12" placeholder="这里输入备注24" title="备注24" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注25:</td>
								<td><input type="text" name="LEAVE_DM" id="LEAVE_DM" value="${pd.LEAVE_DM}" maxlength="12" placeholder="这里输入备注25" title="备注25" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注26:</td>
								<td><input type="text" name="HOUSE_ALLE" id="HOUSE_ALLE" value="${pd.HOUSE_ALLE}" maxlength="12" placeholder="这里输入备注26" title="备注26" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注27:</td>
								<td><input type="text" name="ITEM_ALLE" id="ITEM_ALLE" value="${pd.ITEM_ALLE}" maxlength="12" placeholder="这里输入备注27" title="备注27" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注28:</td>
								<td><input type="text" name="MEAL_EXPE" id="MEAL_EXPE" value="${pd.MEAL_EXPE}" maxlength="12" placeholder="这里输入备注28" title="备注28" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注29:</td>
								<td><input type="text" name="TRF_ALLE" id="TRF_ALLE" value="${pd.TRF_ALLE}" maxlength="12" placeholder="这里输入备注29" title="备注29" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注30:</td>
								<td><input type="text" name="TEL_EXPE" id="TEL_EXPE" value="${pd.TEL_EXPE}" maxlength="12" placeholder="这里输入备注30" title="备注30" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注31:</td>
								<td><input type="text" name="HLDY_ALLE" id="HLDY_ALLE" value="${pd.HLDY_ALLE}" maxlength="12" placeholder="这里输入备注31" title="备注31" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注32:</td>
								<td><input type="text" name="KID_ALLE" id="KID_ALLE" value="${pd.KID_ALLE}" maxlength="12" placeholder="这里输入备注32" title="备注32" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注33:</td>
								<td><input type="text" name="COOL_EXPE" id="COOL_EXPE" value="${pd.COOL_EXPE}" maxlength="12" placeholder="这里输入备注33" title="备注33" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注34:</td>
								<td><input type="text" name="EXT_SGL_AWAD" id="EXT_SGL_AWAD" value="${pd.EXT_SGL_AWAD}" maxlength="12" placeholder="这里输入备注34" title="备注34" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注35:</td>
								<td><input type="text" name="PRE_TAX_PLUS" id="PRE_TAX_PLUS" value="${pd.PRE_TAX_PLUS}" maxlength="12" placeholder="这里输入备注35" title="备注35" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注36:</td>
								<td><input type="text" name="GROSS_PAY" id="GROSS_PAY" value="${pd.GROSS_PAY}" maxlength="12" placeholder="这里输入备注36" title="备注36" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注37:</td>
								<td><input type="text" name="ENDW_INS" id="ENDW_INS" value="${pd.ENDW_INS}" maxlength="12" placeholder="这里输入备注37" title="备注37" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注38:</td>
								<td><input type="text" name="UNEMPL_INS" id="UNEMPL_INS" value="${pd.UNEMPL_INS}" maxlength="12" placeholder="这里输入备注38" title="备注38" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注39:</td>
								<td><input type="text" name="MED_INS" id="MED_INS" value="${pd.MED_INS}" maxlength="12" placeholder="这里输入备注39" title="备注39" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注40:</td>
								<td><input type="text" name="CASD_INS" id="CASD_INS" value="${pd.CASD_INS}" maxlength="12" placeholder="这里输入备注40" title="备注40" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注41:</td>
								<td><input type="text" name="HOUSE_FUND" id="HOUSE_FUND" value="${pd.HOUSE_FUND}" maxlength="12" placeholder="这里输入备注41" title="备注41" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注42:</td>
								<td><input type="text" name="SUP_PESN" id="SUP_PESN" value="${pd.SUP_PESN}" maxlength="12" placeholder="这里输入备注42" title="备注42" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注43:</td>
								<td><input type="text" name="TAX_BASE_ADJ" id="TAX_BASE_ADJ" value="${pd.TAX_BASE_ADJ}" maxlength="12" placeholder="这里输入备注43" title="备注43" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注44:</td>
								<td><input type="text" name="ACCRD_TAX" id="ACCRD_TAX" value="${pd.ACCRD_TAX}" maxlength="12" placeholder="这里输入备注44" title="备注44" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注45:</td>
								<td><input type="text" name="AFTER_TAX" id="AFTER_TAX" value="${pd.AFTER_TAX}" maxlength="12" placeholder="这里输入备注45" title="备注45" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注46:</td>
								<td><input type="text" name="ACT_SALY" id="ACT_SALY" value="${pd.ACT_SALY}" maxlength="12" placeholder="这里输入备注46" title="备注46" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注47:</td>
								<td><input type="text" name="GUESS_DIFF" id="GUESS_DIFF" value="${pd.GUESS_DIFF}" maxlength="12" placeholder="这里输入备注47" title="备注47" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注48:</td>
								<td><input type="text" name="CUST_COL1" id="CUST_COL1" value="${pd.CUST_COL1}" maxlength="12" placeholder="这里输入备注48" title="备注48" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注49:</td>
								<td><input type="text" name="CUST_COL2" id="CUST_COL2" value="${pd.CUST_COL2}" maxlength="12" placeholder="这里输入备注49" title="备注49" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注50:</td>
								<td><input type="text" name="CUST_COL3" id="CUST_COL3" value="${pd.CUST_COL3}" maxlength="12" placeholder="这里输入备注50" title="备注50" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注51:</td>
								<td><input type="text" name="CUST_COL4" id="CUST_COL4" value="${pd.CUST_COL4}" maxlength="12" placeholder="这里输入备注51" title="备注51" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注52:</td>
								<td><input type="text" name="CUST_COL5" id="CUST_COL5" value="${pd.CUST_COL5}" maxlength="12" placeholder="这里输入备注52" title="备注52" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注53:</td>
								<td><input type="text" name="CUST_COL6" id="CUST_COL6" value="${pd.CUST_COL6}" maxlength="12" placeholder="这里输入备注53" title="备注53" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注54:</td>
								<td><input type="text" name="CUST_COL7" id="CUST_COL7" value="${pd.CUST_COL7}" maxlength="30" placeholder="这里输入备注54" title="备注54" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注55:</td>
								<td><input type="text" name="CUST_COL8" id="CUST_COL8" value="${pd.CUST_COL8}" maxlength="30" placeholder="这里输入备注55" title="备注55" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注56:</td>
								<td><input type="text" name="CUST_COL9" id="CUST_COL9" value="${pd.CUST_COL9}" maxlength="30" placeholder="这里输入备注56" title="备注56" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注57:</td>
								<td><input type="text" name="CUST_COL10" id="CUST_COL10" value="${pd.CUST_COL10}" maxlength="100" placeholder="这里输入备注57" title="备注57" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注58:</td>
								<td><input type="text" name="ZRZC_CODE" id="ZRZC_CODE" value="${pd.ZRZC_CODE}" maxlength="30" placeholder="这里输入备注58" title="备注58" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注59:</td>
								<td><input type="text" name="BILL_STATE" id="BILL_STATE" value="${pd.BILL_STATE}" maxlength="1" placeholder="这里输入备注59" title="备注59" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注60:</td>
								<td><input type="text" name="BILL_USER" id="BILL_USER" value="${pd.BILL_USER}" maxlength="8" placeholder="这里输入备注60" title="备注60" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注61:</td>
								<td><input type="text" name="BILL_DATE" id="BILL_DATE" value="${pd.BILL_DATE}" maxlength="20" placeholder="这里输入备注61" title="备注61" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="text-align: center;" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</td>
							</tr>
						</table>
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
					</form>
					</div>
					<!-- /.col -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.page-content -->
		</div>
	</div>
	<!-- /.main-content -->
</div>
<!-- /.main-container -->


	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		//保存
		function save(){
			if($("#BILL_CODE").val()==""){
				$("#BILL_CODE").tips({
					side:3,
		            msg:'请输入备注1',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BILL_CODE").focus();
			return false;
			}
			if($("#BUSI_DATE").val()==""){
				$("#BUSI_DATE").tips({
					side:3,
		            msg:'请输入备注2',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BUSI_DATE").focus();
			return false;
			}
			if($("#ESTB_DEPT").val()==""){
				$("#ESTB_DEPT").tips({
					side:3,
		            msg:'请输入备注3',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ESTB_DEPT").focus();
			return false;
			}
			if($("#USER_GROP").val()==""){
				$("#USER_GROP").tips({
					side:3,
		            msg:'请输入备注4',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USER_GROP").focus();
			return false;
			}
			if($("#USER_CATG").val()==""){
				$("#USER_CATG").tips({
					side:3,
		            msg:'请输入备注5',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USER_CATG").focus();
			return false;
			}
			if($("#DEPT_CODE").val()==""){
				$("#DEPT_CODE").tips({
					side:3,
		            msg:'请输入备注6',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DEPT_CODE").focus();
			return false;
			}
			if($("#ORG_UNIT").val()==""){
				$("#ORG_UNIT").tips({
					side:3,
		            msg:'请输入备注7',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ORG_UNIT").focus();
			return false;
			}
			if($("#SAL_RANGE").val()==""){
				$("#SAL_RANGE").tips({
					side:3,
		            msg:'请输入备注8',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SAL_RANGE").focus();
			return false;
			}
			if($("#POST_SALY").val()==""){
				$("#POST_SALY").tips({
					side:3,
		            msg:'请输入备注9',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#POST_SALY").focus();
			return false;
			}
			if($("#BONUS").val()==""){
				$("#BONUS").tips({
					side:3,
		            msg:'请输入备注10',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BONUS").focus();
			return false;
			}
			if($("#CASH_BONUS").val()==""){
				$("#CASH_BONUS").tips({
					side:3,
		            msg:'请输入备注11',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CASH_BONUS").focus();
			return false;
			}
			if($("#WORK_OT").val()==""){
				$("#WORK_OT").tips({
					side:3,
		            msg:'请输入备注12',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#WORK_OT").focus();
			return false;
			}
			if($("#BACK_SALY").val()==""){
				$("#BACK_SALY").tips({
					side:3,
		            msg:'请输入备注13',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BACK_SALY").focus();
			return false;
			}
			if($("#RET_SALY").val()==""){
				$("#RET_SALY").tips({
					side:3,
		            msg:'请输入备注14',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#RET_SALY").focus();
			return false;
			}
			if($("#CHK_CASH").val()==""){
				$("#CHK_CASH").tips({
					side:3,
		            msg:'请输入备注15',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CHK_CASH").focus();
			return false;
			}
			if($("#INTR_SGL_AWAD").val()==""){
				$("#INTR_SGL_AWAD").tips({
					side:3,
		            msg:'请输入备注16',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#INTR_SGL_AWAD").focus();
			return false;
			}
			if($("#SENY_ALLE").val()==""){
				$("#SENY_ALLE").tips({
					side:3,
		            msg:'请输入备注17',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SENY_ALLE").focus();
			return false;
			}
			if($("#POST_ALLE").val()==""){
				$("#POST_ALLE").tips({
					side:3,
		            msg:'请输入备注18',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#POST_ALLE").focus();
			return false;
			}
			if($("#NS_ALLE").val()==""){
				$("#NS_ALLE").tips({
					side:3,
		            msg:'请输入备注19',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#NS_ALLE").focus();
			return false;
			}
			if($("#AREA_ALLE").val()==""){
				$("#AREA_ALLE").tips({
					side:3,
		            msg:'请输入备注20',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#AREA_ALLE").focus();
			return false;
			}
			if($("#EXPT_ALLE").val()==""){
				$("#EXPT_ALLE").tips({
					side:3,
		            msg:'请输入备注21',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#EXPT_ALLE").focus();
			return false;
			}
			if($("#TECH_ALLE").val()==""){
				$("#TECH_ALLE").tips({
					side:3,
		            msg:'请输入备注22',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TECH_ALLE").focus();
			return false;
			}
			if($("#LIVE_EXPE").val()==""){
				$("#LIVE_EXPE").tips({
					side:3,
		            msg:'请输入备注23',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LIVE_EXPE").focus();
			return false;
			}
			if($("#LIVE_ALLE").val()==""){
				$("#LIVE_ALLE").tips({
					side:3,
		            msg:'请输入备注24',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LIVE_ALLE").focus();
			return false;
			}
			if($("#LEAVE_DM").val()==""){
				$("#LEAVE_DM").tips({
					side:3,
		            msg:'请输入备注25',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LEAVE_DM").focus();
			return false;
			}
			if($("#HOUSE_ALLE").val()==""){
				$("#HOUSE_ALLE").tips({
					side:3,
		            msg:'请输入备注26',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#HOUSE_ALLE").focus();
			return false;
			}
			if($("#ITEM_ALLE").val()==""){
				$("#ITEM_ALLE").tips({
					side:3,
		            msg:'请输入备注27',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ITEM_ALLE").focus();
			return false;
			}
			if($("#MEAL_EXPE").val()==""){
				$("#MEAL_EXPE").tips({
					side:3,
		            msg:'请输入备注28',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEAL_EXPE").focus();
			return false;
			}
			if($("#TRF_ALLE").val()==""){
				$("#TRF_ALLE").tips({
					side:3,
		            msg:'请输入备注29',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TRF_ALLE").focus();
			return false;
			}
			if($("#TEL_EXPE").val()==""){
				$("#TEL_EXPE").tips({
					side:3,
		            msg:'请输入备注30',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TEL_EXPE").focus();
			return false;
			}
			if($("#HLDY_ALLE").val()==""){
				$("#HLDY_ALLE").tips({
					side:3,
		            msg:'请输入备注31',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#HLDY_ALLE").focus();
			return false;
			}
			if($("#KID_ALLE").val()==""){
				$("#KID_ALLE").tips({
					side:3,
		            msg:'请输入备注32',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#KID_ALLE").focus();
			return false;
			}
			if($("#COOL_EXPE").val()==""){
				$("#COOL_EXPE").tips({
					side:3,
		            msg:'请输入备注33',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COOL_EXPE").focus();
			return false;
			}
			if($("#EXT_SGL_AWAD").val()==""){
				$("#EXT_SGL_AWAD").tips({
					side:3,
		            msg:'请输入备注34',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#EXT_SGL_AWAD").focus();
			return false;
			}
			if($("#PRE_TAX_PLUS").val()==""){
				$("#PRE_TAX_PLUS").tips({
					side:3,
		            msg:'请输入备注35',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PRE_TAX_PLUS").focus();
			return false;
			}
			if($("#GROSS_PAY").val()==""){
				$("#GROSS_PAY").tips({
					side:3,
		            msg:'请输入备注36',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GROSS_PAY").focus();
			return false;
			}
			if($("#ENDW_INS").val()==""){
				$("#ENDW_INS").tips({
					side:3,
		            msg:'请输入备注37',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ENDW_INS").focus();
			return false;
			}
			if($("#UNEMPL_INS").val()==""){
				$("#UNEMPL_INS").tips({
					side:3,
		            msg:'请输入备注38',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#UNEMPL_INS").focus();
			return false;
			}
			if($("#MED_INS").val()==""){
				$("#MED_INS").tips({
					side:3,
		            msg:'请输入备注39',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MED_INS").focus();
			return false;
			}
			if($("#CASD_INS").val()==""){
				$("#CASD_INS").tips({
					side:3,
		            msg:'请输入备注40',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CASD_INS").focus();
			return false;
			}
			if($("#HOUSE_FUND").val()==""){
				$("#HOUSE_FUND").tips({
					side:3,
		            msg:'请输入备注41',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#HOUSE_FUND").focus();
			return false;
			}
			if($("#SUP_PESN").val()==""){
				$("#SUP_PESN").tips({
					side:3,
		            msg:'请输入备注42',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUP_PESN").focus();
			return false;
			}
			if($("#TAX_BASE_ADJ").val()==""){
				$("#TAX_BASE_ADJ").tips({
					side:3,
		            msg:'请输入备注43',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TAX_BASE_ADJ").focus();
			return false;
			}
			if($("#ACCRD_TAX").val()==""){
				$("#ACCRD_TAX").tips({
					side:3,
		            msg:'请输入备注44',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ACCRD_TAX").focus();
			return false;
			}
			if($("#AFTER_TAX").val()==""){
				$("#AFTER_TAX").tips({
					side:3,
		            msg:'请输入备注45',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#AFTER_TAX").focus();
			return false;
			}
			if($("#ACT_SALY").val()==""){
				$("#ACT_SALY").tips({
					side:3,
		            msg:'请输入备注46',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ACT_SALY").focus();
			return false;
			}
			if($("#GUESS_DIFF").val()==""){
				$("#GUESS_DIFF").tips({
					side:3,
		            msg:'请输入备注47',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GUESS_DIFF").focus();
			return false;
			}
			if($("#CUST_COL1").val()==""){
				$("#CUST_COL1").tips({
					side:3,
		            msg:'请输入备注48',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL1").focus();
			return false;
			}
			if($("#CUST_COL2").val()==""){
				$("#CUST_COL2").tips({
					side:3,
		            msg:'请输入备注49',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL2").focus();
			return false;
			}
			if($("#CUST_COL3").val()==""){
				$("#CUST_COL3").tips({
					side:3,
		            msg:'请输入备注50',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL3").focus();
			return false;
			}
			if($("#CUST_COL4").val()==""){
				$("#CUST_COL4").tips({
					side:3,
		            msg:'请输入备注51',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL4").focus();
			return false;
			}
			if($("#CUST_COL5").val()==""){
				$("#CUST_COL5").tips({
					side:3,
		            msg:'请输入备注52',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL5").focus();
			return false;
			}
			if($("#CUST_COL6").val()==""){
				$("#CUST_COL6").tips({
					side:3,
		            msg:'请输入备注53',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL6").focus();
			return false;
			}
			if($("#CUST_COL7").val()==""){
				$("#CUST_COL7").tips({
					side:3,
		            msg:'请输入备注54',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL7").focus();
			return false;
			}
			if($("#CUST_COL8").val()==""){
				$("#CUST_COL8").tips({
					side:3,
		            msg:'请输入备注55',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL8").focus();
			return false;
			}
			if($("#CUST_COL9").val()==""){
				$("#CUST_COL9").tips({
					side:3,
		            msg:'请输入备注56',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL9").focus();
			return false;
			}
			if($("#CUST_COL10").val()==""){
				$("#CUST_COL10").tips({
					side:3,
		            msg:'请输入备注57',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL10").focus();
			return false;
			}
			if($("#ZRZC_CODE").val()==""){
				$("#ZRZC_CODE").tips({
					side:3,
		            msg:'请输入备注58',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ZRZC_CODE").focus();
			return false;
			}
			if($("#BILL_STATE").val()==""){
				$("#BILL_STATE").tips({
					side:3,
		            msg:'请输入备注59',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BILL_STATE").focus();
			return false;
			}
			if($("#BILL_USER").val()==""){
				$("#BILL_USER").tips({
					side:3,
		            msg:'请输入备注60',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BILL_USER").focus();
			return false;
			}
			if($("#BILL_DATE").val()==""){
				$("#BILL_DATE").tips({
					side:3,
		            msg:'请输入备注61',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BILL_DATE").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		</script>
</body>
</html>