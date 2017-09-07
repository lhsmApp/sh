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
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 ，其中包含旧版本（Ace）Jqgrid Css-->
	<%@ include file="../../system/index/topWithJqgrid.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css" />
	
	<!-- 最新版的Jqgrid Css，如果旧版本（Ace）某些方法不好用，尝试用此版本Css，替换旧版本Css -->
	<!-- <link rel="stylesheet" type="text/css" media="screen" href="static/ace/css/ui.jqgrid-bootstrap.css" /> -->
	
	<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
	<!-- 树形下拉框start -->
	<script type="text/javascript" src="plugins/selectZtree/selectTree.js"></script>
	<script type="text/javascript" src="plugins/selectZtree/framework.js"></script>
	<link rel="stylesheet" type="text/css"
		href="plugins/selectZtree/import_fh.css" />
	<script type="text/javascript" src="plugins/selectZtree/ztree/ztree.js"></script>
	<link type="text/css" rel="stylesheet"
		href="plugins/selectZtree/ztree/ztree.css"></link>
	<!-- 树形下拉框end -->
    <!-- 标准页面统一样式 -->
    <link rel="stylesheet" href="static/css/normal.css" />
	
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
								    <span class="label label-xlg label-success arrowed-right">人工成本</span>
									<!-- arrowed-in-right --> 
									<span class="label label-xlg label-yellow arrowed-in arrowed-right"
									    id="subTitle" style="margin-left: 2px;">工资数据导入</span> 
                                    <span style="border-left: 1px solid #e2e2e2; margin: 0px 10px;">&nbsp;</span>
								
									<button id="btnQuery" class="btn btn-white btn-info btn-sm"
										onclick="showQueryCondi($('#jqGridBase'),null,true)">
										<i class="ace-icon fa fa-chevron-down bigger-120 blue"></i> <span>隐藏查询</span>
									</button>
								
						            <div class="pull-right">
									    <!-- <span class="label label-xlg label-blue arrowed-left"
									        id = "showDur" style="background:#428bca; margin-right: 2px;"></span>  -->
								        <!-- <span class="label label-xlg label-blue arrowed-left"
								            id = "showDept" style="background:#428bca"></span> -->
									    
							            <div class="btn-toolbar inline middle no-margin">
								            <div data-toggle="buttons" class="btn-group no-margin">
									            <label class="btn btn-sm btn-primary active"> <span
									    	        class="bigger-110">合同化</span> <input type="radio" value="1" />
									            </label> 
									            <label class="btn btn-sm btn-primary"> <span
									            	class="bigger-110">市场化</span> <input type="radio" value="2" />
									            </label> 
									            <label class="btn btn-sm btn-primary"> <span
									            	class="bigger-110">系统内劳务</span> <input type="radio" value="3" />
									            </label>
									            <label class="btn btn-sm btn-primary"> <span
									    	        class="bigger-110">运行人员</span> <input type="radio" value="4" />
									            </label>
									            <label class="btn btn-sm btn-primary"> <span
										            class="bigger-110">劳务派遣</span> <input type="radio" value="5" />
									            </label>
								            </div>
							            </div>      
								    </div>
					</div><!-- /.page-header -->
			
						<div class="row">
						<div class="col-xs-12">
							<div class="widget-box" style="display: block;">
								<div class="widget-body">
									<div class="widget-main">
										<form class="form-inline">
											<span class="pull-left" style="margin-right: 5px;">
												<select class="chosen-select form-control"
													name="SelectedCustCol7" id="SelectedCustCol7"
													data-placeholder="请选择帐套"
													style="vertical-align: top; height:32px;width: 150px;">
													<option value="">请选择帐套</option>
													<c:forEach items="${FMISACC}" var="each">
														<option value="${each.DICT_CODE}">${each.NAME}</option>
														    <!-- <c:if test="${pd.SelectedCustCol7==each.DICT_CODE}">selected</c:if> -->
													</c:forEach>
												</select>
											</span>
											<span class="pull-left" id="spanSelectTree" style="margin-right: 5px;" <c:if test="${pd.departTreeSource=='0'}">hidden</c:if>>
												<div class="selectTree" id="selectTree" multiMode="false"
												    allSelectable="false" noGroup="false"></div>
											    <input type="text" id="SelectedDepartCode" hidden></input>
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
    
	var which;
	// 枚举  1封存,0解封
	var State;
	//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
    var jqGridColModel;

    function setStateTrue(){
    	State = "true";
    }
    function setStateFalse(){
    	State = "false";
    }
    function getState(){
        if($.trim(State) == "true"){
            return true; 
        }
        return false;
    };
    
    function setNavButtonState(){
        if($.trim(State) == "true"){
        	console.log("按钮可用");
        	//Enable 按钮可用
            $("#edit").removeClass('ui-state-disabled');
            $("#add").removeClass('ui-state-disabled');
            $("#del").removeClass('ui-state-disabled');
            $("#batchDelete").removeClass('ui-state-disabled');
            $("#batchEdit").removeClass('ui-state-disabled');
            $("#batchCancelEdit").removeClass('ui-state-disabled');
            $("#batchSave").removeClass('ui-state-disabled');
            $("#importItems").removeClass('ui-state-disabled');
            $("#report").removeClass('ui-state-disabled');
            
            //$("#edit").show();
            //$("#add").show();
            //$("#del").show();
            //$("#batchDelete").show();
            //$("#batchEdit").show();
            //$("#batchCancelEdit").show();
            //$("#batchSave").show();
            //$("#importItems").show();
            //$("#report").show();
            
            $("#edit.ui-state-disabled .ui-icon").removeAttr("style");
            $("#add.ui-state-disabled .ui-icon").removeAttr("style");
            $("#del.ui-state-disabled .ui-icon").removeAttr("style");
            $("#batchDelete.ui-state-disabled .ui-icon").removeAttr("style");
            $("#batchEdit.ui-state-disabled .ui-icon").removeAttr("style");
            $("#batchCancelEdit.ui-state-disabled .ui-icon").removeAttr("style");
            $("#batchSave.ui-state-disabled .ui-icon").removeAttr("style");
            $("#importItems.ui-state-disabled .ui-icon").removeAttr("style");
            $("#report.ui-state-disabled .ui-icon").removeAttr("style");
        } else {
        	console.log("按钮灰掉不可用");
        	 //Disable 按钮灰掉不可用
            $("#edit").addClass('ui-state-disabled');
            $("#add").addClass('ui-state-disabled');
            $("#del").addClass('ui-state-disabled');
            $("#batchDelete").addClass('ui-state-disabled');
            $("#batchEdit").addClass('ui-state-disabled');
            $("#batchCancelEdit").addClass('ui-state-disabled');
            $("#batchSave").addClass('ui-state-disabled');
            $("#importItems").addClass('ui-state-disabled');
            $("#report").addClass('ui-state-disabled');
            
            //$("#edit").hide();
            //$("#add").hide();
            //$("#del").hide();
            //$("#batchDelete").hide();
            //$("#batchEdit").hide();
            //$("#batchCancelEdit").hide();
            //$("#batchSave").hide();
            //$("#importItems").hide();
            //$("#report").hide();
            
            $("#edit.ui-state-disabled .ui-icon").attr("style",'color:#B0B0B0 !important');
            $("#add.ui-state-disabled .ui-icon").attr("style",'color:#B0B0B0 !important');
            $("#del.ui-state-disabled .ui-icon").attr("style",'color:#B0B0B0 !important');
            $("#batchDelete.ui-state-disabled .ui-icon").attr("style",'color:#B0B0B0 !important');
            $("#batchEdit.ui-state-disabled .ui-icon").attr("style",'color:#B0B0B0 !important');
            $("#batchCancelEdit.ui-state-disabled .ui-icon").attr("style",'color:#B0B0B0 !important');
            $("#batchSave.ui-state-disabled .ui-icon").attr("style",'color:#B0B0B0 !important');
            $("#importItems.ui-state-disabled .ui-icon").attr("style",'color:#B0B0B0 !important');
            $("#report.ui-state-disabled .ui-icon").attr("style",'color:#B0B0B0 !important');
        }
    };
    
    function SetStructure(){
		//resize to fit page size
		$(window).on('resize.jqGrid', function () {
			$(gridBase_selector).jqGrid( 'setGridWidth', $(".page-content").width());
			resizeGridHeight($(gridBase_selector),null,true);
	    });
		
		$(gridBase_selector).jqGrid({
			url: '<%=basePath%>staffdetail/getPageList.do?SelectedTableNo='+which
                +'&SelectedDepartCode='+$("#SelectedDepartCode").val()
                +'&SelectedCustCol7='+$("#SelectedCustCol7").val(),
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
			editurl: '<%=basePath%>staffdetail/edit.do?SelectedTableNo='+which
                +'&SelectedDepartCode='+$("#SelectedDepartCode").val()
                +'&SelectedCustCol7='+$("#SelectedCustCol7").val(),
			
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
			        edit: true,
		            editicon : 'ace-icon fa fa-pencil blue',
		            add: true,
		            addicon : 'ace-icon fa fa-plus-circle purple',
		            del: false,
		            delicon : 'ace-icon fa fa-trash-o red',
		            search: true,
		            searchicon : 'ace-icon fa fa-search orange',
		            refresh: true,
		            refreshicon : 'ace-icon fa fa-refresh green',
		            view: false,
		            viewicon : 'ace-icon fa fa-search-plus grey',
		            //delfunc : batchDelete(),
	        },
	        {
				//edit record form
				id: "edit",
				width: 900,
				closeAfterEdit: true,
				recreateForm: true,
				beforeShowForm :beforeEditOrAddCallback,
	            afterSubmit: fn_addSubmit_extend
	        },
	        {
				//new record form
				id: "add",
				width: 900,
				closeAfterAdd: true,
				recreateForm: true,
				viewPagerButtons: false,
				//reloadAfterSubmit: true,
				beforeShowForm : beforeEditOrAddCallback,
			    onclickSubmit: function(params, posdata) {
					console.log("onclickSubmit");
	            } , 
	            afterSubmit: fn_addSubmit_extend
	        },
	        {
				//delete record form
				id: "del",
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
				showQuery: false
	        },
	        {},{});
		
		$(gridBase_selector).navSeparatorAdd(pagerBase_selector, {
			sepclass : "ui-separator",
			sepcontent: ""
		});
        $(gridBase_selector).navButtonAdd(pagerBase_selector, {
			id : "batchEdit",
            title: "批量编辑",
            caption: "",
        	buttonicon: "ace-icon fa fa-pencil-square-o purple",
            position: "last",
            onClickButton: batchEdit,
            cursor : "pointer"
        });
        $(gridBase_selector).navButtonAdd(pagerBase_selector, {
        	id : "batchCancelEdit",
        	buttonicon: "ace-icon fa fa-undo",
                    title: "取消批量编辑",
                    caption: "",
                    position: "last",
                    onClickButton: batchCancelEdit,
                    cursor : "pointer"
                });
        		$(gridBase_selector).navButtonAdd(pagerBase_selector, {
        			id : "batchSave",
        			caption : "",
                     buttonicon : "ace-icon fa fa-save green",
                     onClickButton : batchSave,
                     position : "last",
                     title : "批量保存",
                     cursor : "pointer"
                 });
        	    $(gridBase_selector).navButtonAdd(pagerBase_selector, {
        			id : "batchDelete",
                    title : "批量删除",
                    caption: "",
                    buttonicon : "ace-icon fa fa-trash-o red",
                    onClickButton : batchDelete,
                    position : "3",
                    cursor : "pointer"
                }); 
        		$(gridBase_selector).navSeparatorAdd(pagerBase_selector, {
        			sepclass : "ui-separator",
        			sepcontent: ""
        		});
        			$(gridBase_selector).navButtonAdd(pagerBase_selector, {
        				id : "importItems",
        				caption : "",
        	             buttonicon : "ace-icon fa fa-cloud-upload",
        	             onClickButton : importItems,
        	             position : "last",
        	             title : "导入",
        	             cursor : "pointer"
        	         });
				$(gridBase_selector).navButtonAdd(pagerBase_selector, {
		             caption : "",
		             buttonicon : "ace-icon fa fa-cloud-download",
		             onClickButton : exportItems,
		             position : "last",
		             title : "导出",
		             cursor : "pointer"
		         });
					$(gridBase_selector).navButtonAdd(pagerBase_selector, {
        				id : "report",
			             caption : "",
			             buttonicon : "ace-icon fa fa-check-square-o green",
			             onClickButton : report,
			             position : "last",
			             title : "上报",
			             cursor : "pointer"
			         });
					setNavButtonState();
					getCheckState();
    }
    
	$(document).ready(function () {
		$(top.hangge());//关闭加载状态
	    
		//当前期间,取自tb_system_config的SystemDateTime字段
	    //var SystemDateTime = '${SystemDateTime}';
		//当前登录人所在二级单位
	    //var DepartName = '${DepartName}';
	    //$("#showDur").text('当前期间：' + SystemDateTime + ' 当前单位：' + DepartName);
	    //$("#showDept").text('当前单位：' + DepartName);
		//封存状态,取自tb_sys_sealed_info表state字段, 数据操作需要前提为当前明细数据未封存，如果已确认封存，则明细数据不能再进行操作。
	    // 枚举  1封存,0解封
		State = '${State}';
		//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
	    jqGridColModel = eval("(${jqGridColModel})");//此处记得用eval()行数将string转为array
		
		//初始化当前选择凭证类型
		if('${pd.which}'!=""){
			$('[data-toggle="buttons"] .btn').each(function(index, data){
				var target = $(this).find('input[type=radio]');
				$(this).removeClass('active');
				var whichCur = parseInt(target.val());
				console.log(which);
				if(whichCur=='${pd.which}'){
					$(this).addClass('active');
					which=whichCur;
				}
			});
		} 
		//凭证类型变化
		$('[data-toggle="buttons"] .btn').on('click', function(e){
			var target = $(this).find('input[type=radio]');
			which = parseInt(target.val());
			//if(which!='${pd.which}'){
				window.location.href="<%=basePath%>staffdetail/list.do?SelectedTableNo="+which;
                //+'&SelectedDepartCode='+$("#SelectedDepartCode").val() + '&SelectedCustCol7='+$("#SelectedCustCol7").val()
			//}
		});
		
		SetStructure();
	});

	//双击编辑行
    var lastSelection;
	function doubleClickRow(rowid,iRow,iCol,e){
		if(getState()){
			$(gridBase_selector).restoreRow(lastSelection);
			$(gridBase_selector).editRow(rowid, {
            	keys:true, //keys:true 这里按[enter]保存  
                restoreAfterError: false,  
            	oneditfunc: function(rowid){  
                    console.log(rowid);  
                },  
                successfunc: function(response){
			        var responseJSON = JSON.parse(response.responseText);
			    	if (responseJSON.code == 0) {
						$(gridBase_selector).trigger("reloadGrid");  
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
			        //   $(gridBase_selector).jqGrid('editRow',lastSelection);
					//	$(top.hangge());//关闭加载状态
					//	$("#subTitle").tips({
					//		side:3,
				    //        msg:'保存失败:'+response.responseJSON.message,
				    //        bg:'#cc0033',
				    //        time:3
				    //    });
					//}
                },  
                errorfunc: function(rowid, response){
                    var responseJSON = JSON.parse(response.responseText);
                	$(gridBase_selector).jqGrid('editRow',lastSelection);
					$(top.hangge());//关闭加载状态
					if(response.statusText == "success"){
						if(responseJSON.code != 0){
							$(gridBase_selector).jqGrid('editRow',lastSelection);
							$(top.hangge());//关闭加载状态
							$("#subTitle").tips({
								side:3,
						        msg:'保存失败:'+responseJSON.message,
						        bg:'#cc0033',
						        time:3
						    });
						}
					} else {
						$("#subTitle").tips({
							side:3,
				            msg:'保存出错:' + responseJSON.message,
				            bg:'#cc0033',
				            time:3
				        });
					}
                }  
            });
            lastSelection = rowid;
        }
	} 

	//批量编辑
	function batchEdit(e) {
        var ids = $(gridBase_selector).jqGrid('getDataIDs');
        for (var i = 0; i < ids.length; i++) {
        	$(gridBase_selector).jqGrid('editRow',ids[i]);
        }
    }
	
	//取消批量编辑
	function batchCancelEdit(e) {
        var ids = $(gridBase_selector).jqGrid('getDataIDs');
        for (var i = 0; i < ids.length; i++) {
        	$(gridBase_selector).jqGrid('restoreRow',ids[i]);
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
            var msg = '确定要删除选中的数据吗?';
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
						url: '<%=basePath%>staffdetail/deleteAll.do?SelectedTableNo='+which
		                    +'&SelectedDepartCode='+$("#SelectedDepartCode").val()
		                    +'&SelectedCustCol7='+$("#SelectedCustCol7").val(),
				    	data: {DataRows:JSON.stringify(listData)},
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
				    	error: function(response) {
							$(top.hangge());//关闭加载状态
							$("#subTitle").tips({
								side:3,
					            msg:'删除出错:'+response.responseJSON.message,
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
            	buttons: { "button":{ "label":"确定", "className":"btn-sm btn-success"}}
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
					    url: '<%=basePath%>staffdetail/updateAll.do?SelectedTableNo='+which
	                        +'&SelectedDepartCode='+$("#SelectedDepartCode").val()
	                        +'&SelectedCustCol7='+$("#SelectedCustCol7").val(),
			    	    data: {DataRows:JSON.stringify(listData)},
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
			    	    error: function(response) {
						    batchEdit(null);
						    $(top.hangge());//关闭加载状态
						    $("#subTitle").tips({
							    side:3,
				                msg:'保存出错:'+response.responseJSON.message,
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
	    diag.URL = '<%=basePath%>staffdetail/goUploadExcel.do?SelectedTableNo='+which
           +'&SelectedDepartCode='+$("#SelectedDepartCode").val()
           +'&SelectedCustCol7='+$("#SelectedCustCol7").val();
	    diag.Width = 300;
	    diag.Height = 150;
	    diag.CancelEvent = function(){ //关闭事件
	        top.jzts();
	        $(gridBase_selector).trigger("reloadGrid");  
	        $(top.hangge());//关闭加载状态
	        diag.close();
       };
       diag.show();
    }

    /**
     * 导出
     */
    function exportItems(){
	    window.location.href='<%=basePath%>staffdetail/excel.do?SelectedTableNo='+which
            +'&SelectedDepartCode='+$("#SelectedDepartCode").val()
            +'&SelectedCustCol7='+$("#SelectedCustCol7").val();
    }

    /**
     * 上报
     */
    function report(){
        //获得选中的行ids的方法
	    var ids = $(gridBase_selector).getDataIDs();  
	
	    if(!(ids!=null && ids.length>0)){
	        bootbox.dialog({
	            message: "<span class='bigger-110'>界面没有任何内容!</span>",
	            buttons: 			
	                { "button":{ "label":"确定", "className":"btn-sm btn-success"}}
	        });
	    }else{
	         var msg = '确定要上报吗?';
	         bootbox.confirm(msg, function(result) {
	        	if(result) {
	        	    top.jzts();
	        	    $.ajax({
	        	        type: "POST",
	        	        url: '<%=basePath%>staffdetail/report.do?SelectedTableNo='+which
	        	            +'&SelectedDepartCode='+$("#SelectedDepartCode").val()
                            +'&SelectedCustCol7='+$("#SelectedCustCol7").val(),
                        cache: false,
				        success: function(response){
					        if(response.code==0){
						        setStateFalse();  
						        setNavButtonState();
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
		    	        error: function(response) {
					        $(top.hangge());//关闭加载状态
					        $("#subTitle").tips({
					        	side:3,
			                    msg:'上报出错:'+response.responseJSON.message,
			                    bg:'#cc0033',
			                    time:3
			                });
		    	        }
			        });
		        }
            });
        }
    }
    
    /*
     * 判断state设置按钮状态
     */
    function getCheckState(){
		top.jzts();
		$.ajax({
			type: "POST",
			url: '<%=basePath%>staffdetail/getState.do?SelectedTableNo='+which
                +'&SelectedDepartCode='+$("#SelectedDepartCode").val()
                +'&SelectedCustCol7='+$("#SelectedCustCol7").val(),
			cache: false,
			success: function(response){
				console.log(response.code);
				console.log("message:" + response.message);
				if(response.code==0){
				    // 枚举  1封存,0解封
					State = response.message;

					console.log("State:" + State);
					console.log("true" == State);
					setNavButtonState();
					$(top.hangge());//关闭加载状态
				}else{
					setStateFalse();  
					setNavButtonState();
					$(top.hangge());//关闭加载状态
					$("#subTitle").tips({
						side:3,
			            msg:'获取封存状态失败,'+response.message,
			            bg:'#cc0033',
			            time:3
			        });
				}
			},
	    	error: function(response) {
				setStateFalse();  
				setNavButtonState();
				$(top.hangge());//关闭加载状态
				$("#subTitle").tips({
					side:3,
		            msg:'获取封存状态出错！',
		            bg:'#cc0033',
		            time:3
		        });
	    	}
		});
    };

    /**
     * 增加成功
     * 
     * @param response
     * @param postdata
     * @returns
     */
    function fn_addSubmit_extend(response, postdata) {
        var responseJSON = JSON.parse(response.responseText);
    	if (responseJSON.code == 0) {
    		// console.log("Add Success");
    		$("#subTitle").tips({
    			side : 3,
    			msg : '保存成功',
    			bg : '#009933',
    			time : 3
    		});
    		return [ true ];
    	} else {
    		// console.log("Add Failed"+response.responseJSON.message);
    		$("#subTitle").tips({
    			side : 3,
    			msg : '保存失败,' + responseJSON.message,
    			bg : '#cc0033',
    			time : 3
    		});
    		return [ false, responseJSON.message ];
    	}
    }
	
	//检索
	function tosearch() {
		setStateTrue();
		setNavButtonState();
		$(gridBase_selector).jqGrid('GridUnload'); 
		SetStructure();
		//$(gridBase_selector).jqGrid('setGridParam',{  // 重新加载数据
		//	url:'<%=basePath%>staffdetail/getPageList.do?SelectedTableNo='+which
        //    +'&SelectedDepartCode='+$("#SelectedDepartCode").val()
        //    +'&SelectedCustCol7='+$("#SelectedCustCol7").val(),  
		//	datatype:'json',
		//      page:1
		//}).trigger("reloadGrid");
		//getCheckState();
	}  
	
	//加载单位树
	function initComplete(){
		//下拉树
		var nodes = ${zTreeNodes};
		//if(nodes.length <= 1){
        //    $("#spanSelectTree").hide();
		//	return;
		//} else {
        //    $("#spanSelectTree").show();
		//}
		var defaultNodes = {"treeNodes":nodes};
		//绑定change事件
		$("#selectTree").bind("change",function(){
			$("#SelectedDepartCode").val("");
			if($(this).attr("relValue")){
				$("#SelectedDepartCode").val($(this).attr("relValue"));
		    }
		});
		//赋给data属性
		$("#selectTree").data("data",defaultNodes);  
		$("#selectTree").render();
		$("#selectTree2_input").val("请选择");
	}

 	</script>
</html>