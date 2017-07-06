﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
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
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 ，其中包含旧版本（Ace）Jqgrid Css-->
	<%@ include file="../../system/index/topWithJqgrid.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css" />
	
	<!-- 最新版的Jqgrid Css，如果旧版本（Ace）某些方法不好用，尝试用此版本Css，替换旧版本Css -->
	<!-- <link rel="stylesheet" type="text/css" media="screen" href="static/ace/css/ui.jqgrid-bootstrap.css" /> -->
	
    <style>
		.page-header{
			padding-top: 9px;
			padding-bottom: 9px;
			margin: 0 0 8px;
		}
	</style>
</head>
<body class="no-skin">
	<div class="main-container" id="main-container">
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<!-- /section:settings.box -->
					<div class="page-header">
								    <span class="label label-xlg label-success arrowed-right">东部管道</span>
									<!-- arrowed-in-right --> 
									<span class="label label-xlg label-yellow arrowed-in arrowed-right"
									    id="subTitle" style="margin-left: 2px;">工资数据导入</span> 
                                    <span style="border-left: 1px solid #e2e2e2; margin: 0px 10px;">&nbsp;</span>
								
									<button id="btnQuery" class="btn btn-white btn-info btn-sm"
										onclick="showQueryCondi($('#jqGridBase'),null,true)">
										<i class="ace-icon fa fa-chevron-down bigger-120 blue"></i> <span>显示查询</span>
									</button>
								
						            <div class="pull-right">
									    <span class="label label-xlg label-blue arrowed-left"
									        id = "showDur" style="background:#428bca; margin-right: 2px;"></span> 
								        <span class="label label-xlg label-blue arrowed-left"
								            id = "showDept" style="background:#428bca"></span>
								    </div>
					</div><!-- /.page-header -->
			
						<div class="row">
						<div class="col-xs-12">
							<div class="widget-box" style="display: none;">
								<div class="widget-body">
									<div class="widget-main">
										<form class="form-inline">
											<span>
												<select class="chosen-select form-control" 
													name="UserCode" id="UserCode"
													data-placeholder="请选择员工编号"
													style="vertical-align: top; height:32px;width: 150px;">
													<option value="">全部</option>
													<c:forEach items="${userCodeList}" var="usercode">
														<option value="${usercode}"
															<c:if test="${pd.UserCode==usercode}">selected</c:if>>${usercode}</option>
													</c:forEach>
												</select>
											</span>
											<button type="button" class="btn btn-info btn-sm" onclick="tosearch();">
												<i class="ace-icon fa fa-search bigger-110"></i>
											</button>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-xs-12">
						    <table id="jqGridBase"></table>
						    <div id="jqGridBasePager"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	
		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>
	</div>
</body>
	
	
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	
	<!-- 最新版的Jqgrid Js，如果旧版本（Ace）某些方法不好用，尝试用此版本Js，替换旧版本JS -->
	<!-- <script src="static/ace/js/jquery.jqGrid.min.js" type="text/javascript"></script>
	<script src="static/ace/js/grid.locale-cn.js" type="text/javascript"></script> -->
	
	<!-- 旧版本（Ace）Jqgrid Js -->
	<script src="static/ace/js/jqGrid/jquery.jqGrid.src.js"></script>
	<script src="static/ace/js/jqGrid/i18n/grid.locale-cn.js"></script>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<!-- JqGrid统一样式统一操作 -->
	<script type="text/javascript" src="static/js/common/jqgrid_style.js"></script>
	<!-- 上传控件 -->
	<script src="static/ace/js/ace/elements.fileinput.js"></script>
	
	<script type="text/javascript"> 
    var gridBase_selector = "#jqGridBase";  
    var pagerBase_selector = "#jqGridBasePager";  
    
	$(document).ready(function () {
		/* $.jgrid.defaults.width = 780;*/
		//$.jgrid.defaults.styleUI = 'Bootstrap'; 
		
		$(top.hangge());//关闭加载状态
	    
		//当前期间,取自tb_system_config的SystemDateTime字段
	    var SystemDateTime = '${SystemDateTime}';
	    $("#showDur").text('当前期间：' + SystemDateTime);
		//当前登录人所在二级单位
	    var DepartName = '${DepartName}';
	    $("#showDept").text('当前单位：' + DepartName);
		//封存状态,取自tb_sys_sealed_info表state字段, 数据操作需要前提为当前明细数据未封存，如果已确认封存，则明细数据不能再进行操作。
	    // 枚举  1封存,0解封
		var State = '${State}';
		//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
	    var jqGridColModel = eval("(${jqGridColModel})");//此处记得用eval()行数将string转为array
	    
	    function getState(){
	        if($.trim(State) == "true"){
	            return true;
	        }
	        return false;
	    };
	    
		//resize to fit page size
		$(window).on('resize.jqGrid', function () {
			$(gridBase_selector).jqGrid( 'setGridWidth', $(".page-content").width());
			//$(gridBase_selector).jqGrid( 'setGridHeight', $(window).height() - 240);
			resizeGridHeight($(gridBase_selector),null,true);
	    });
		
		$(gridBase_selector).jqGrid({
			url: '<%=basePath%>socialincdetail/getPageList.do',
			datatype: "json",
			colModel: jqGridColModel,
			//caption: '当前期间：' + SystemDateTime + '， 当前单位：' + DepartName + '',
			reloadAfterSubmit: true, 
			viewrecords: true, 
			shrinkToFit: false,
			rowNum: 10,
			rowList: [10,20,30],
            multiselect: true,
            multiboxonly: true,
            sortable: true,
			altRows: true, //斑马条纹
			editurl: '<%=basePath%>socialincdetail/edit.do?',
			
			pager: pagerBase_selector,
			footerrow: true,
			userDataOnFooter: true,
			ondblClickRow: doubleClickRow,
			
			loadComplete : function() {
				var table = this;
				setTimeout(function(){
					styleCheckbox(table);
					updateActionIcons(table);
					updatePagerIcons(table);
					enableTooltips(table);
				}, 0);
			},
		});
	    
		$(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
		
			$(gridBase_selector).navGrid(pagerBase_selector, 
					{
			            //navbar options
				        edit: getState(),
			            editicon : 'ace-icon fa fa-pencil blue',
			            add: getState(),
			            addicon : 'ace-icon fa fa-plus-circle purple',
			            del: false,
			            delicon : 'ace-icon fa fa-trash-o red',
			            search: true,
			            searchicon : 'ace-icon fa fa-search orange',
			            refresh: true,
			            refreshicon : 'ace-icon fa fa-refresh green',
			            view: false,
			            viewicon : 'ace-icon fa fa-search-plus grey',
		        },
		        {
					//edit record form
				    width: 900,
					closeAfterEdit: true,
					recreateForm: true,
					beforeShowForm :beforeEditOrAddCallback,
		            afterSubmit: fn_addSubmit
		        },
		        {
					//new record form
				    width: 900,
					closeAfterAdd: true,
					recreateForm: true,
					viewPagerButtons: false,
					//width: 700,
					//reloadAfterSubmit: true,
					beforeShowForm : beforeEditOrAddCallback,
				    onclickSubmit: function(params, posdata) {
						console.log("onclickSubmit");
		            } , 
		            afterSubmit: fn_addSubmit
		        },
		        {
					//delete record form
					recreateForm: true,
					beforeShowForm : beforeDeleteCallback,
					onClick : function(e) {

					}
		        },
		        {
					//search form
					recreateForm: true,
					afterShowSearch: beforeSearchCallback,
					afterRedraw: function(){
						style_search_filters($(this));
					},
					multipleSearch: true,
					//multipleGroup:true,
					showQuery: true
		        },
		        {},{});

		if(getState()){
	        $(gridBase_selector).navButtonAdd(pagerBase_selector,
	                {
	                    buttonicon: "ace-icon fa fa-pencil-square-o purple",
	                    title: "批量编辑",
	                    caption: "",
	                    position: "last",
	                    onClickButton: batchEdit
	                });
            $(gridBase_selector).navButtonAdd(pagerBase_selector,
	                {
	                    buttonicon: "ace-icon fa fa-undo",
	                    title: "取消批量编辑",
	                    caption: "",
	                    position: "last",
	                    onClickButton: batchCancelEdit
	                });
	        		$(gridBase_selector).navButtonAdd(pagerBase_selector, {
	                     caption : "",
	                     buttonicon : "ace-icon fa fa-save green",
	                     onClickButton : batchSave,
	                     position : "last",
	                     title : "批量保存",
	                     cursor : "pointer"
	                 });
	        		$(gridBase_selector).navButtonAdd(pagerBase_selector, {
	                    caption : "",
	                    buttonicon : "ace-icon fa fa-trash-o red",
	                    onClickButton : batchDelete,
	                    position : "last",
	                    title : "批量删除",
	                    cursor : "pointer"
	                });
	        			$(gridBase_selector).navButtonAdd(pagerBase_selector, {
	        	             caption : "",
	        	             buttonicon : "ace-icon fa fa-cloud-upload",
	        	             onClickButton : importItems,
	        	             position : "last",
	        	             title : "导入",
	        	             cursor : "pointer"
	        	         });
		};
					$(gridBase_selector).navButtonAdd(pagerBase_selector, {
			             caption : "",
			             buttonicon : "ace-icon fa fa-cloud-download",
			             onClickButton : exportItems,
			             position : "last",
			             title : "导出",
			             cursor : "pointer"
			         });
					if(getState()){
    					$(gridBase_selector).navButtonAdd(pagerBase_selector, {
    			             caption : "",
    			             buttonicon : "ace-icon fa fa-check-square green",
    			             onClickButton : report,
    			             position : "last",
    			             title : "上报",
    			             cursor : "pointer"
    			         });
					};
		
			//双击编辑行
            var lastSelection;
			function doubleClickRow(rowid,iRow,iCol,e){
				if(getState()){
                if (rowid && rowid !== lastSelection) {
                    var grid = $(gridBase_selector);
                    grid.restoreRow(lastSelection);
                    grid.editRow(rowid, {
                    	keys:true, //keys:true 这里按[enter]保存  
                        restoreAfterError: false,  
                    	oneditfunc: function(rowid){  
                            console.log(rowid);  
                        },  
                        successfunc: function(response){
							if(response.responseJSON.code==0){
								grid.trigger("reloadGrid");  
								$(top.hangge());//关闭加载状态
								$("#subTitle").tips({
									side:3,
						            msg:'保存成功',
						            bg:'#009933',
						            time:3
						        });
								lastSelection = rowid;
								return [true,"",""];
							}//else{
					        //   grid.jqGrid('editRow',lastSelection);
							//	$(top.hangge());//关闭加载状态
							//	$("#subTitle").tips({
							//		side:3,
						    //        msg:'保存失败,'+response.responseJSON.message,
						    //        bg:'#cc0033',
						    //        time:3
						    //    });
							//}
                        },  
                        errorfunc: function(rowid, response){
				            grid.jqGrid('editRow',lastSelection);
							$(top.hangge());//关闭加载状态
							if(response.statusText == "success"){
								if(response.responseJSON.code != 0){
							        grid.jqGrid('editRow',lastSelection);
									$(top.hangge());//关闭加载状态
									$("#subTitle").tips({
										side:3,
								        msg:'保存失败,'+response.responseJSON.message,
								        bg:'#cc0033',
								        time:3
								    });
								}
							} else {
								$("#subTitle").tips({
									side:3,
						            msg:'保存出错:' + res.toString(),
						            bg:'#cc0033',
						            time:3
						        });
							}
                        }  
                    });
                    lastSelection = rowid;
                }
				}
			} 

			//批量编辑
			function batchEdit(e) {
				var grid = $(gridBase_selector);
		        var ids = grid.jqGrid('getDataIDs');
		        for (var i = 0; i < ids.length; i++) {
		            grid.jqGrid('editRow',ids[i]);
		        }
		    }
			
			//取消批量编辑
			function batchCancelEdit(e) {
				var grid = $(gridBase_selector);
		        var ids = grid.jqGrid('getDataIDs');
		        for (var i = 0; i < ids.length; i++) {
		            grid.jqGrid('restoreRow',ids[i]);
		        }
		    }

			/**
			 * 批量删除
			 */
		    function batchDelete(){
		    	//获得选中的行ids的方法
		    	var ids = $(gridBase_selector).getGridParam("selarrrow");  
		    	
				if(!(ids!=null && ids.length>0)){
					bootbox.dialog({
						message: "<span class='bigger-110'>您没有选择任何内容!</span>",
						buttons: 			
						{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
					});
				}else{
	                var msg = '确定要删除选中的数据吗??';
	                bootbox.confirm(msg, function(result) {
	    				if(result) {
	    					var listData =new Array();
	    					
	    					//遍历访问这个集合  
	    					$(ids).each(function (index, id){  
	    			            var rowData = $(gridBase_selector).getRowData(id);
	    			            listData.push(rowData);
	    					});
	    					
	    					top.jzts();
	    					$.ajax({
	    						type: "POST",
	    						url: '<%=basePath%>socialincdetail/deleteAll.do?',
	    				    	data: {DATA_ROWS:JSON.stringify(listData)},
	    						dataType:'json',
	    						cache: false,
	    						success: function(response){
	    							if(response.code==0){
	    								$(gridBase_selector).trigger("reloadGrid");  
	    								$(top.hangge());//关闭加载状态
	    								$("#subTitle").tips({
	    									side:3,
	    						            msg:'删除成功',
	    						            bg:'#009933',
	    						            time:3
	    						        });
	    							}else{
	    								$(top.hangge());//关闭加载状态
	    								$("#subTitle").tips({
	    									side:3,
	    						            msg:'删除失败,'+response.message,
	    						            bg:'#cc0033',
	    						            time:3
	    						        });
	    							}
	    						},
	    				    	error: function(e) {
	    							$(top.hangge());//关闭加载状态
    								$("#subTitle").tips({
    									side:3,
    						            msg:'删除出错',
    						            bg:'#cc0033',
    						            time:3
    						        });
	    				    	}
	    					});
	    				}
	                });
				}
			}

			/**
			 * 批量保存
			 */
	    function batchSave(){
	    	//获得选中行ids的方法
            var ids = $(gridBase_selector).getDataIDs();  
	    	
			if(!(ids!=null&&ids.length>0)){
				bootbox.dialog({
					message: "<span class='bigger-110'>您没有选择任何内容!</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
			}else{
                var msg = '确定要保存选中的数据吗?';
                bootbox.confirm(msg, function(result) {
    				if(result) {
    					var listData =new Array();
    					
    					//遍历访问这个集合  
    					$(ids).each(function (index, id){  
    			            $(gridBase_selector).saveRow(id, false, 'clientArray');
    			            var rowData = $(gridBase_selector).getRowData(id);
    			            listData.push(rowData);
    					});
    					
    					top.jzts();
    					$.ajax({
    						type: "POST",
    						url: '<%=basePath%>socialincdetail/updateAll.do?',
    				    	data: {DATA_ROWS:JSON.stringify(listData)},
    						dataType:'json',
    						cache: false,
    						success: function(response){
    							if(response.code==0){
    								$(gridBase_selector).trigger("reloadGrid");  
    								$(top.hangge());//关闭加载状态
    								$("#subTitle").tips({
    									side:3,
    						            msg:'保存成功',
    						            bg:'#009933',
    						            time:3
    						        });
    							}else{
        							batchEdit(null);
    								$(top.hangge());//关闭加载状态
    								$("#subTitle").tips({
    									side:3,
    						            msg:'保存失败,'+response.message,
    						            bg:'#cc0033',
    						            time:3
    						        });
    							}
    						},
    				    	error: function(e) {
    							batchEdit(e);
    							$(top.hangge());//关闭加载状态
								$("#subTitle").tips({
									side:3,
						            msg:'保存出错',
						            bg:'#cc0033',
						            time:3
						        });
    				    	}
    					});
    				}
                });
			}
	    }
        
		/**
		 * 导入
		 */
	    function importItems(){
	   	   top.jzts();
	   	   var diag = new top.Dialog();
	   	   diag.Drag=true;
	   	   diag.Title ="EXCEL 导入到数据库";
	   	   diag.URL = '<%=basePath%>socialincdetail/goUploadExcel.do';
	   	   diag.Width = 300;
	   	   diag.Height = 150;
	   	   diag.CancelEvent = function(){ //关闭事件
			  if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
				 if('${page.currentPage}' == '0'){
					 top.jzts();
					 setTimeout("self.location.reload()",100);
				 }else{
					 nextPage(${page.currentPage});
				 }
			  }
			  diag.close();
		   };
		   diag.show();
	    }

		/**
		 * 导出
		 */
	    function exportItems(){
	    	window.location.href='<%=basePath%>socialincdetail/excel.do?';
	    }

		/**
		 * 上报
		 */
		function report(){
            var msg = '确定要上报吗?';
            bootbox.confirm(msg, function(result) {
				if(result) {
					top.jzts();
					$.ajax({
						type: "POST",
						url: '<%=basePath%>socialincdetail/report.do?',
						cache: false,
						success: function(response){
							if(response.code==0){
								$(gridBase_selector).trigger("reloadGrid");  
								$(top.hangge());//关闭加载状态
								$("#subTitle").tips({
									side:3,
						            msg:'上报成功',
						            bg:'#009933',
						            time:3
						        });
							}else{
								$(top.hangge());//关闭加载状态
								$("#subTitle").tips({
									side:3,
						            msg:'上报失败,'+response.message,
						            bg:'#cc0033',
						            time:3
						        });
							}
						},
				    	error: function(e) {
							$(top.hangge());//关闭加载状态
							$("#subTitle").tips({
								side:3,
					            msg:'上报出错',
					            bg:'#cc0033',
					            time:3
					        });
				    	}
					});
				}
            });
		}
	});
	
	//检索
	function tosearch() {
		var UserCode = $("#UserCode").val();
		$(gridBase_selector).jqGrid('setGridParam',{  // 重新加载数据
			url:'<%=basePath%>socialincdetail/getPageList.do?UserCode='+UserCode,  
			datatype:'json',
		      page:1
		}).trigger("reloadGrid");
	}  

 	</script>
</html>