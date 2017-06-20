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
						<h1>
							东部管道
							<small>
								<i class="ace-icon fa fa-angle-double-right"></i>
								成本核算
							</small>
						</h1>
					</div><!-- /.page-header -->
				
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
	$(document).ready(function () {
		/* $.jgrid.defaults.width = 780;*/
		//$.jgrid.defaults.styleUI = 'Bootstrap'; 
		
		$(top.hangge());//关闭加载状态
        var gridBase_selector = "#jqGridBase";  
        var pagerBase_selector = "#jqGridBasePager";  
        
		//resize to fit page size
		$(window).on('resize.jqGrid', function () {
			$(gridBase_selector).jqGrid( 'setGridWidth', $(".page-content").width());
			$(gridBase_selector).jqGrid( 'setGridHeight', $(window).height() - 200);
	    })
		
		$(gridBase_selector).jqGrid({
			url: '<%=basePath%>staffdetail/getPageList.do',
			datatype: "json",
			colModel: [
						{ name: 'ID', hidden: true, key: true, },
						{ label: 'Category Name', name: 'CATEGORYNAME', width: 75, 
							editable: true, edittype:'text', editoptions:{maxLength:'50'}, editrules:{required:true}
						},
						{ label: 'Product Name', name: 'PRODUCTNAME', width: 90,
							editable: true, edittype:'textarea', editoptions:{maxlength:'100'} //, rows:'2', cols:'20'
						},
						{ label: 'Country', name: 'COUNTRY', width: 100,
							//选择
							editable: true, edittype:'select', editoptions:{value:'USA:USA;UK:UK;CHI:CHINA'},
						    //翻译
						    formatter: 'select', formatoptions: {value: 'USA:USA;UK:UK;CHI:CHINA'},
							stype: 'select', searchoptions: {value: ':[All];USA:USA;UK:UK;CHI:CHINA'}
						},
						{ label: 'Price', name: 'PRICE', width: 80, sorttype: 'number', align: 'right', summaryType:'sum', summaryTpl:'<b>sum:{0}</b>',
							editable: true, edittype:'text', editoptions:{maxlength:'10', number: true},
						    searchrules: {number: true}
						},
						// sorttype is used only if the data is loaded locally or loadonce is set to true
						{ label: 'Quantity', name: 'QUANTITY', width: 80, sorttype: 'integer',
							editable: true, edittype:'text', editoptions:{maxlength:'11', integer: true},
						    searchrules: {interger: true}
						}
			],
			caption: '当前期间:' + ${SystemDateTime} + ', 当前单位:' + ${DepartName} + '',
			reloadAfterSubmit: true, 
			viewrecords: true, 
			rowNum: 10,
			rowList: [10,20,30],
            multiselect: true,
            multiboxonly: true,
            sortable: true,
			altRows: true, //斑马条纹
			editurl: '<%=basePath%>staffdetail/edit.do?',
			
			pager: pagerBase_selector,
			//footerrow: true,
			//userDataOnFooter: true,
			
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
		$(gridBase_selector).navGrid(pagerBase_selector, 
			{
	            //navbar options
	            edit: true,
	            editicon : 'ace-icon fa fa-pencil blue',
	            add: true,
	            addicon : 'ace-icon fa fa-plus-circle purple',
	            del: true,
	            delicon : 'ace-icon fa fa-trash-o red',
	            search: true,
	            searchicon : 'ace-icon fa fa-search orange',
	            refresh: false,
	            refreshicon : 'ace-icon fa fa-refresh green',
	            view: false,
	            viewicon : 'ace-icon fa fa-search-plus grey',
        },
        {
			//edit record form
			//closeAfterEdit: true,
			recreateForm: true,
			beforeShowForm :beforeEditOrAddCallback
        },
        {
			//new record form
			//width: 700,
			closeAfterAdd: true,
			recreateForm: true,
			viewPagerButtons: false,
			reloadAfterSubmit: true,
			beforeShowForm : beforeEditOrAddCallback,
		    onclickSubmit: function(params, posdata) {
				console.log("onclickSubmit");
                //console.log(posdata	);
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
        	multipleSearch:true, 
        	multipleGroup:true,
        	showQuery: true
        },
        {},{});

		$(gridBase_selector).navButtonAdd(pagerBase_selector, {
             caption : "",
             buttonicon : "ace-icon fa fa-save green",
             onClickButton : saveRows,
             position : "last",
             title : "",
             cursor : "pointer"
         });
			$(gridBase_selector).navButtonAdd(pagerBase_selector, {
	             caption : "",
	             buttonicon : "ace-icon fa fa-cloud-upload",
	             onClickButton : importItems,
	             position : "last",
	             title : "",
	             cursor : "pointer"
	         });
			$(gridBase_selector).navButtonAdd(pagerBase_selector, {
	             caption : "",
	             buttonicon : "ace-icon fa fa-cloud-download",
	             onClickButton : exportItems,
	             position : "last",
	             title : "",
	             cursor : "pointer"
	         });

	    function saveRows(){
	    	//获得选中行ids的方法
            var ids = $(gridBase_selector).jqGrid("getGridParam", "selarrrow");  
	    	
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
    						url: '<%=basePath%>jqGridExtend/updateAll.do?',
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
    								$(top.hangge());//关闭加载状态
    								$("#subTitle").tips({
    									side:3,
    						            msg:'保存失败,'+response.responseJSON.message,
    						            bg:'#cc0033',
    						            time:3
    						        });
    							}
    						},
    				    	error: function(e) {
    							$(top.hangge());//关闭加载状态
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
	   	   diag.URL = '<%=basePath%>jqGridExtend/goUploadExcel.do';
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
	    	window.location.href='<%=basePath%>jqGridExtend/excel.do?';
	    }
	    
		$(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
		
	});

 	</script>
</html>