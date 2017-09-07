<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">

<!-- jsp文件头和头部 -->
<%@ include file="../system/index/top.jsp"%>

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
							<form name="Form" id="Form" method="post" enctype="multipart/form-data">
								<div id="zhongxin">
								<table style="width:95%;" >
									<tr>
										<td style="padding-top: 20px;"><input type="file" id="excel" name="excel" style="width:50px;" onchange="fileType(this)" /></td>
									</tr>
									<tr>
										<td style="text-align: center;padding-top: 10px;">
											<a class="btn btn-mini btn-primary" onclick="save();">导入</a>
											<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
											<a class="btn btn-mini btn-success" onclick="downModel('<%=basePath%>')">下载模版</a>
										</td>
									</tr>
								</table>
								</div>
								<div id="zhongxin2" class="center" style="display:none"><br/><img src="static/images/jzx.gif" /><br/><h4 class="lighter block green"></h4></div>
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

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../system/index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 上传控件 -->
	<script src="static/ace/js/ace/elements.fileinput.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script type="text/javascript">
	    var local = '${local}';
	    var which = '${which}';
	    var SelectedDepartCode = '${SelectedDepartCode}';
	    var SelectedCustCol7 = '${SelectedCustCol7}';
	    var tipfiles = "请选择xls格式的文件";
	    
		$(document).ready(function () {
			$(top.hangge());
			
			document.getElementById("Form").action = local + "/readExcel.do?TABLE_CODE="+which+"&SelectedTableNo="+which
                +'&SelectedDepartCode='+SelectedDepartCode+'&SelectedCustCol7='+SelectedCustCol7;
			
		    var commonBaseCode = '${commonBaseCode}';
		    var commonMessage = '${commonMessage}';
		    if(commonBaseCode != null && $.trim(commonBaseCode) != ""){
		        if($.trim(commonBaseCode) == 0){
		            $("#excel").tips({
		                side:3,
		                msg:'导入成功',
		                bg:'#AE81FF',
		                time:3
		            });
		        } else {
			        if($.trim(commonBaseCode) != -1){
					    $("#excel").tips({
					    	side:3,
			                msg:commonMessage,
			                bg:'#AE81FF',
			                time:3
			            });
			        }
			    }
		    };
		})
		
		$(function() {
			//上传
			$('#excel').ace_file_input({
				no_file:'请选择EXCEL ...',
				btn_choose:'选择',
				btn_change:'更改',
				droppable:false,
				onchange:null,
				thumbnail:false, //| true | large
				whitelist:'xls|xls',
				blacklist:'gif|png|jpg|jpeg'
				//onchange:''
			});
		});
		
		//下载模板
		function downModel(basePath){
			var url = basePath + local + '/downExcel.do?TABLE_CODE='+which+"&SelectedTableNo="+which
                +'&SelectedDepartCode='+SelectedDepartCode+'&SelectedCustCol7='+SelectedCustCol7;
			window.location.href = url;
		}
		//保存
		function save(){
			if($("#excel").val() == "" || document.getElementById("excel").files[0] == tipfiles){
				$("#excel").tips({
					side:3,
		            msg:'请选择文件',
		            bg:'#AE81FF',
		            time:3
		        });
				return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		function fileType(obj){
			var fileType=obj.value.substr(obj.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
		    if(fileType != '.xls' && fileType != '.xlsx' ){
		    	$("#excel").tips({
					side:3,
		            msg:tipfiles,
		            bg:'#AE81FF',
		            time:3
		        });
		    	$("#excel").val('');
		    	document.getElementById("excel").files[0] = tipfiles;
		    }
		}
	</script>


</body>
</html>