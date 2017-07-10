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
					
					<form action="socialincsummy/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="SOCIALINCSUMMY_ID" id="SOCIALINCSUMMY_ID" value="${pd.SOCIALINCSUMMY_ID}"/>
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
								<td><input type="text" name="SOC_INC_BASE" id="SOC_INC_BASE" value="${pd.SOC_INC_BASE}" maxlength="12" placeholder="这里输入备注5" title="备注5" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注6:</td>
								<td><input type="text" name="PER_ENDW_INS" id="PER_ENDW_INS" value="${pd.PER_ENDW_INS}" maxlength="12" placeholder="这里输入备注6" title="备注6" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注7:</td>
								<td><input type="text" name="PER_MED_INS" id="PER_MED_INS" value="${pd.PER_MED_INS}" maxlength="12" placeholder="这里输入备注7" title="备注7" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注8:</td>
								<td><input type="text" name="PER_UNEMPL_INS" id="PER_UNEMPL_INS" value="${pd.PER_UNEMPL_INS}" maxlength="12" placeholder="这里输入备注8" title="备注8" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注9:</td>
								<td><input type="text" name="PER_TOTAL" id="PER_TOTAL" value="${pd.PER_TOTAL}" maxlength="12" placeholder="这里输入备注9" title="备注9" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注10:</td>
								<td><input type="text" name="DEPT_ENDW_INS" id="DEPT_ENDW_INS" value="${pd.DEPT_ENDW_INS}" maxlength="12" placeholder="这里输入备注10" title="备注10" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注11:</td>
								<td><input type="text" name="DEPT_MED_INS" id="DEPT_MED_INS" value="${pd.DEPT_MED_INS}" maxlength="12" placeholder="这里输入备注11" title="备注11" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注12:</td>
								<td><input type="text" name="DEPT_UNEMPL_INS" id="DEPT_UNEMPL_INS" value="${pd.DEPT_UNEMPL_INS}" maxlength="12" placeholder="这里输入备注12" title="备注12" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注13:</td>
								<td><input type="text" name="EMPT_INJ_INS" id="EMPT_INJ_INS" value="${pd.EMPT_INJ_INS}" maxlength="12" placeholder="这里输入备注13" title="备注13" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注14:</td>
								<td><input type="text" name="MATY_INS" id="MATY_INS" value="${pd.MATY_INS}" maxlength="12" placeholder="这里输入备注14" title="备注14" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注15:</td>
								<td><input type="text" name="DEPT_TOTAL" id="DEPT_TOTAL" value="${pd.DEPT_TOTAL}" maxlength="12" placeholder="这里输入备注15" title="备注15" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注16:</td>
								<td><input type="text" name="DEPT_CODE" id="DEPT_CODE" value="${pd.DEPT_CODE}" maxlength="30" placeholder="这里输入备注16" title="备注16" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注17:</td>
								<td><input type="text" name="USER_CATG" id="USER_CATG" value="${pd.USER_CATG}" maxlength="30" placeholder="这里输入备注17" title="备注17" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注18:</td>
								<td><input type="text" name="PMT_PLACE" id="PMT_PLACE" value="${pd.PMT_PLACE}" maxlength="20" placeholder="这里输入备注18" title="备注18" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注19:</td>
								<td><input type="text" name="CUST_COL1" id="CUST_COL1" value="${pd.CUST_COL1}" maxlength="12" placeholder="这里输入备注19" title="备注19" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注20:</td>
								<td><input type="text" name="CUST_COL2" id="CUST_COL2" value="${pd.CUST_COL2}" maxlength="12" placeholder="这里输入备注20" title="备注20" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注21:</td>
								<td><input type="text" name="CUST_COL3" id="CUST_COL3" value="${pd.CUST_COL3}" maxlength="12" placeholder="这里输入备注21" title="备注21" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注22:</td>
								<td><input type="text" name="CUST_COL4" id="CUST_COL4" value="${pd.CUST_COL4}" maxlength="12" placeholder="这里输入备注22" title="备注22" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注23:</td>
								<td><input type="text" name="CUST_COL5" id="CUST_COL5" value="${pd.CUST_COL5}" maxlength="12" placeholder="这里输入备注23" title="备注23" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注24:</td>
								<td><input type="text" name="CUST_COL6" id="CUST_COL6" value="${pd.CUST_COL6}" maxlength="12" placeholder="这里输入备注24" title="备注24" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注25:</td>
								<td><input type="text" name="CUST_COL7" id="CUST_COL7" value="${pd.CUST_COL7}" maxlength="30" placeholder="这里输入备注25" title="备注25" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注26:</td>
								<td><input type="text" name="CUST_COL8" id="CUST_COL8" value="${pd.CUST_COL8}" maxlength="30" placeholder="这里输入备注26" title="备注26" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注27:</td>
								<td><input type="text" name="CUST_COL9" id="CUST_COL9" value="${pd.CUST_COL9}" maxlength="30" placeholder="这里输入备注27" title="备注27" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注28:</td>
								<td><input type="text" name="CUST_COL10" id="CUST_COL10" value="${pd.CUST_COL10}" maxlength="100" placeholder="这里输入备注28" title="备注28" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注29:</td>
								<td><input type="text" name="ZRZC_CODE" id="ZRZC_CODE" value="${pd.ZRZC_CODE}" maxlength="30" placeholder="这里输入备注29" title="备注29" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注30:</td>
								<td><input type="text" name="BILL_STATE" id="BILL_STATE" value="${pd.BILL_STATE}" maxlength="1" placeholder="这里输入备注30" title="备注30" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注31:</td>
								<td><input type="text" name="BILL_USER" id="BILL_USER" value="${pd.BILL_USER}" maxlength="8" placeholder="这里输入备注31" title="备注31" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注32:</td>
								<td><input type="text" name="BILL_DATE" id="BILL_DATE" value="${pd.BILL_DATE}" maxlength="20" placeholder="这里输入备注32" title="备注32" style="width:98%;"/></td>
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
			if($("#SOC_INC_BASE").val()==""){
				$("#SOC_INC_BASE").tips({
					side:3,
		            msg:'请输入备注5',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SOC_INC_BASE").focus();
			return false;
			}
			if($("#PER_ENDW_INS").val()==""){
				$("#PER_ENDW_INS").tips({
					side:3,
		            msg:'请输入备注6',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PER_ENDW_INS").focus();
			return false;
			}
			if($("#PER_MED_INS").val()==""){
				$("#PER_MED_INS").tips({
					side:3,
		            msg:'请输入备注7',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PER_MED_INS").focus();
			return false;
			}
			if($("#PER_UNEMPL_INS").val()==""){
				$("#PER_UNEMPL_INS").tips({
					side:3,
		            msg:'请输入备注8',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PER_UNEMPL_INS").focus();
			return false;
			}
			if($("#PER_TOTAL").val()==""){
				$("#PER_TOTAL").tips({
					side:3,
		            msg:'请输入备注9',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PER_TOTAL").focus();
			return false;
			}
			if($("#DEPT_ENDW_INS").val()==""){
				$("#DEPT_ENDW_INS").tips({
					side:3,
		            msg:'请输入备注10',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DEPT_ENDW_INS").focus();
			return false;
			}
			if($("#DEPT_MED_INS").val()==""){
				$("#DEPT_MED_INS").tips({
					side:3,
		            msg:'请输入备注11',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DEPT_MED_INS").focus();
			return false;
			}
			if($("#DEPT_UNEMPL_INS").val()==""){
				$("#DEPT_UNEMPL_INS").tips({
					side:3,
		            msg:'请输入备注12',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DEPT_UNEMPL_INS").focus();
			return false;
			}
			if($("#EMPT_INJ_INS").val()==""){
				$("#EMPT_INJ_INS").tips({
					side:3,
		            msg:'请输入备注13',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#EMPT_INJ_INS").focus();
			return false;
			}
			if($("#MATY_INS").val()==""){
				$("#MATY_INS").tips({
					side:3,
		            msg:'请输入备注14',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MATY_INS").focus();
			return false;
			}
			if($("#DEPT_TOTAL").val()==""){
				$("#DEPT_TOTAL").tips({
					side:3,
		            msg:'请输入备注15',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DEPT_TOTAL").focus();
			return false;
			}
			if($("#DEPT_CODE").val()==""){
				$("#DEPT_CODE").tips({
					side:3,
		            msg:'请输入备注16',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DEPT_CODE").focus();
			return false;
			}
			if($("#USER_CATG").val()==""){
				$("#USER_CATG").tips({
					side:3,
		            msg:'请输入备注17',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USER_CATG").focus();
			return false;
			}
			if($("#PMT_PLACE").val()==""){
				$("#PMT_PLACE").tips({
					side:3,
		            msg:'请输入备注18',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PMT_PLACE").focus();
			return false;
			}
			if($("#CUST_COL1").val()==""){
				$("#CUST_COL1").tips({
					side:3,
		            msg:'请输入备注19',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL1").focus();
			return false;
			}
			if($("#CUST_COL2").val()==""){
				$("#CUST_COL2").tips({
					side:3,
		            msg:'请输入备注20',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL2").focus();
			return false;
			}
			if($("#CUST_COL3").val()==""){
				$("#CUST_COL3").tips({
					side:3,
		            msg:'请输入备注21',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL3").focus();
			return false;
			}
			if($("#CUST_COL4").val()==""){
				$("#CUST_COL4").tips({
					side:3,
		            msg:'请输入备注22',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL4").focus();
			return false;
			}
			if($("#CUST_COL5").val()==""){
				$("#CUST_COL5").tips({
					side:3,
		            msg:'请输入备注23',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL5").focus();
			return false;
			}
			if($("#CUST_COL6").val()==""){
				$("#CUST_COL6").tips({
					side:3,
		            msg:'请输入备注24',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL6").focus();
			return false;
			}
			if($("#CUST_COL7").val()==""){
				$("#CUST_COL7").tips({
					side:3,
		            msg:'请输入备注25',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL7").focus();
			return false;
			}
			if($("#CUST_COL8").val()==""){
				$("#CUST_COL8").tips({
					side:3,
		            msg:'请输入备注26',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL8").focus();
			return false;
			}
			if($("#CUST_COL9").val()==""){
				$("#CUST_COL9").tips({
					side:3,
		            msg:'请输入备注27',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL9").focus();
			return false;
			}
			if($("#CUST_COL10").val()==""){
				$("#CUST_COL10").tips({
					side:3,
		            msg:'请输入备注28',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CUST_COL10").focus();
			return false;
			}
			if($("#ZRZC_CODE").val()==""){
				$("#ZRZC_CODE").tips({
					side:3,
		            msg:'请输入备注29',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ZRZC_CODE").focus();
			return false;
			}
			if($("#BILL_STATE").val()==""){
				$("#BILL_STATE").tips({
					side:3,
		            msg:'请输入备注30',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BILL_STATE").focus();
			return false;
			}
			if($("#BILL_USER").val()==""){
				$("#BILL_USER").tips({
					side:3,
		            msg:'请输入备注31',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BILL_USER").focus();
			return false;
			}
			if($("#BILL_DATE").val()==""){
				$("#BILL_DATE").tips({
					side:3,
		            msg:'请输入备注32',
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