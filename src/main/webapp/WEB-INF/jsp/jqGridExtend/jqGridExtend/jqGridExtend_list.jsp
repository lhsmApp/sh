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
						    <table id="jqGridDetail"></table>
						</div>
					</div>
				</div>
			</div>
		</div>
	
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>
	</div>
	
	
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
		
	<script type="text/javascript"> 
	$(document).ready(function () {
		/* $.jgrid.defaults.width = 780;*/
		//$.jgrid.defaults.styleUI = 'Bootstrap'; 
		
		$(top.hangge());//关闭加载状态
        var gridBase_selector = "#jqGridBase";  
        var pagerBase_selector = "#jqGridBasePager";  
        var gridDetail_selector = "#jqGridDetail";  
		
		//resize to fit page size
		$(window).on('resize.jqGrid', function () {
			$(gridBase_selector).jqGrid( 'setGridWidth', $(".page-content").width());
			$(gridDetail_selector).jqGrid( 'setGridWidth', $(".page-content").width());
			//console.log("ccc"+$("iframe").height());
			//$(gridBase_selector).jqGrid( 'setGridHeight', $(window).height() - 138);
	    })
		
		$(gridBase_selector).jqGrid({
			url: '<%=basePath%>jqGridExtend/getPageList.do',
			datatype: "json",
			colModel: [
						{name:'',index:'', width:80, fixed:true, sortable:false, resize:false,
							formatter:'actions', 
							formatoptions:{ 
								keys:true,
							},
							frozen: true
						},
						{ name: 'ID', hidden: true, key: true, frozen: true},
						{ label: 'Category Name', name: 'CATEGORYNAME', width: 75, frozen: true,
							editable: true, edittype:'text', editoptions:{maxLength:'50'}, editrules:{required:true}
						},
						{ label: 'Product Name', name: 'PRODUCTNAME', width: 90,
							editable: true, edittype:'textarea', editoptions:{maxlength:'100'} //, rows:'2', cols:'20'
						},
						{ label: 'Country', name: 'COUNTRY', width: 100,
							editable: true, edittype:'select', editoptions:{value:'USA:USA;UK:UK;CHI:CHINA'} 
						},
						{ label: 'Price', name: 'PRICE', width: 80, sorttype: 'integer', align: 'right', summaryType:'sum', summaryTpl:'<b>{0}</b>',
							editable: true, edittype:'text', editoptions:{maxlength:'10', number: true} 
						},
						// sorttype is used only if the data is loaded locally or loadonce is set to true
						{ label: 'Quantity', name: 'QUANTITY', width: 80, sorttype: 'number',
							editable: true, edittype:'text', editoptions:{maxlength:'11', integer: true} 
						}
			],
			caption: "jqGrid with inline editing",
			viewrecords: true, 
			rowNum: 30,
			height: 340, 
            multiselect: true,
            sortable: true,
            sortname: 'CATEGORYNAME',
			sortorder: 'asc',
			editurl: '<%=basePath%>jqGridExtend/edit.do?',
			
			pager: pagerBase_selector,
			footerrow: true,
			userDataOnFooter: true,
			grouping: true,
			groupingView: {
				groupField: ['CATEGORYNAME'],
				groupOrder: ['asc'],
				groupColumnShow: [true],
				groupText: ['<b>{0}</b>'],
				groupSummary: [true],
				groupSummaryPos: ['header'],
				groupCollapse: false,
			},
			
			subGrid: true,
			subGridOptions: {
				plusicon: "glyphicon-hand-right",
				minusicon: "glyphicon-hand-down"
            },
            subGridRowExpanded: showChildGrid,
			
			//onSelectRow : function(id){
			//	alert(id);
			//	$(gridDetail_selector).jqGrid('setGridParam',{url:'<%=basePath%>jqGridExtend/getDetailList.do?parentId='+id+''}).trigger("reloadGrid");                 
			//	//$(gridDetail_selector).jqGrid('setGridParam',{editurl:"servlet/SampleReceiveDetailUpdate?sampleReceiveNo="+id});
			//}, 
			
			loadComplete : function() {
				var table = this;
				setTimeout(function(){
					styleCheckbox(table);
					updateActionIcons(table);
					updatePagerIcons(table);
					enableTooltips(table);
				}, 0);
			},
		}).navGrid(pagerBase_selector, 
			{//navButtons
	            //navbar options
	            edit: false,
	            editicon : 'ace-icon fa fa-pencil blue',
	            add: true,
	            addicon : 'ace-icon fa fa-plus-circle purple',
	            del: false,
	            delicon : 'ace-icon fa fa-trash-o red',
	            search: true,
	            searchicon : 'ace-icon fa fa-search orange',
	            refresh: false,
	            refreshicon : 'ace-icon fa fa-refresh green',
	            view: false,
	            viewicon : 'ace-icon fa fa-search-plus grey',
        });

        // the event handler on expanding parent row receives two parameters
        // the ID of the grid tow  and the primary key of the row
        function showChildGrid(parentRowID, parentRowKey) {
        	alert(parentRowID+"  "+parentRowKey);
            var childGridID = parentRowID + "_table";
            var childGridPagerID = parentRowID + "_pager";

            // send the parent row primary key to the server so that we know which grid to show
            var childGridURL = '<%=basePath%>jqGridExtend/getDetailList.do?parentId='+parentRowKey+'';
            //childGridURL = childGridURL + "&parentRowID=" + encodeURIComponent(parentRowKey)

            // add a table and pager HTML elements to the parent grid row - we will render the child grid here
            $('#' + parentRowID).append('<table id=' + childGridID + '></table><div id=' + childGridPagerID + ' class=scroll></div>');

            $("#" + childGridID).jqGrid({
                url: childGridURL,
                datatype: "json",
                colModel: [
				           {lable: 'ParentID', name: 'ParentID', width: 80},
				           {lable: 'ID', name: 'ID', width: 80},
				           {lable: 'Name', name: 'Name'},
				           {lable: 'Qty', name: 'Qty', width: 100},
				           {lable: 'SaleDate', name: 'SaleDate', width: 100}
                ],
                page: 1,
                //viewrecords: true,
                //pager: "#" + childGridPagerID,
    			
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
        }

		/* 
		$(gridBase_selector).setGroupHeaders({
			useColSpanStyle: false, //定义没有分组的列表头上是否增加一个空白的行， false添加空白行，否则此列的表头作为一个整体
			groupHeaders: [
			               {startColumnName: 'CATEGORYNAME', numberOfColumns: 2, titleText: '<b>name</b>'},
			               {startColumnName: 'PRICE', numberOfColumns: 2, titleText: 'num'}
			               ]
		    //startColumnName	string	colModel中的name值，页头分组开始的列
		    //numberOfColumns	integer	页头分组的列数。包含startColumnName配置的列。如果遇到隐藏的列，不包含此列列头，但是还是计算到总数里面
		    //titleText	string	页头分组显示的内容，可以包含html标签
		}); */
		$(gridBase_selector).jqGrid('setFrozenColumns');
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
            buttonicon : "ace-icon fa fa-trash-o red",
            onClickButton : deleteRows,
            position : "last",
            title : "",
            cursor : "pointer"
        });
		 
		$(gridDetail_selector).jqGrid({
            datatype: 'json',
			colModel: [
			           {lable: 'ParentID', name: 'ParentID', width: 80},
			           {lable: 'ID', name: 'ID', width: 80},
			           {lable: 'Name', name: 'Name'},
			           {lable: 'Qty', name: 'Qty', width: 100},
			           {lable: 'SaleDate', name: 'SaleDate', width: 100}
		        ],
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
    			    	var listJson = "";
    			    	listJson+='[';
    					//遍历访问这个集合  
    					$(ids).each(function (index, id){  
    			            $(gridBase_selector).saveRow(id, false, 'clientArray');
    			            var rowData = $(gridBase_selector).getRowData(id);
    			            
    			            listJson+='{'
    			            	+'\"ID\":' + rowData.ID +','
    			            	+'\"CATEGORYNAME\":\"' + rowData.CATEGORYNAME +'\",'
    			            	+'\"PRODUCTNAME\":\"' + rowData.PRODUCTNAME +'\",'
    			            	+'\"COUNTRY\":\"' + rowData.COUNTRY +'\",'
    			            	+'\"PRICE\":' + rowData.PRICE +','
    			            	+'\"QUANTITY\":' + rowData.QUANTITY +''
    			            	+'},';
    					});
    			    	listJson+=']';
    					
    					top.jzts();
    					$.ajax({
    						type: "POST",
    						url: '<%=basePath%>jqGridExtend/updateAll.do?',
    				    	data: {DATA_ROWS:listJson},
    						dataType:'json',
    						cache: false,
    						success: function(data){
    							refreshJqGrid();
    						},
    				    	error: function(e) {
    							$(top.hangge());//关闭加载状态
    				    	}
    					});
    				}
                });
			}
	    }

	    function deleteRows(){
	    	//获得选中的行ids的方法
	    	var ids = $(gridBase_selector).getGridParam("selarrrow");  

			var str = '';
			//遍历访问这个集合  
			$(ids).each(function (index, id){  
			  	if(str.trim()=='') str += id;
			  	else str += ',' + id;
			});
			if(str==''){
				bootbox.dialog({
					message: "<span class='bigger-110'>您没有选择任何内容!</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
			}else{
                var msg = '确定要删除选中的数据吗?';
                bootbox.confirm(msg, function(result) {
    				if(result) {
    					top.jzts();
    					$.ajax({
    						type: "POST",
    						url: '<%=basePath%>jqGridExtend/deleteAll.do?',
    				    	data: {DATA_IDS:str},
    						dataType:'json',
    						cache: false,
    						success: function(data){
    							refreshJqGrid();
    						},
    				    	error: function(e) {
    							$(top.hangge());//关闭加载状态
    				    	}
    					});
    				}
                });
			}
		}

		function refreshJqGrid(){
			$(gridBase_selector).trigger("reloadGrid");  
			$(top.hangge());//关闭加载状态
		}
	    
		$(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
		
		//it causes some flicker when reloading or navigating grid
		//it may be possible to have some custom formatter to do this as the grid is being created to prevent this
		//or go back to default browser checkbox styles for the grid
		function styleCheckbox(table) {
		/**
			$(table).find('input:checkbox').addClass('ace')
			.wrap('<label />')
			.after('<span class="lbl align-top" />')
	
	
			$('.ui-jqgrid-labels th[id*="_cb"]:first-child')
			.find('input.cbox[type=checkbox]').addClass('ace')
			.wrap('<label />').after('<span class="lbl align-top" />');
		*/
		}
		
	
		//unlike navButtons icons, action icons in rows seem to be hard-coded
		//you can change them like this in here if you want
		function updateActionIcons(table) {
			/**
			var replacement = 
			{
				'ui-ace-icon fa fa-pencil' : 'ace-icon fa fa-pencil blue',
				'ui-ace-icon fa fa-trash-o' : 'ace-icon fa fa-trash-o red',
				'ui-icon-disk' : 'ace-icon fa fa-check green',
				'ui-icon-cancel' : 'ace-icon fa fa-times red'
			};
			$(table).find('.ui-pg-div span.ui-icon').each(function(){
				var icon = $(this);
				var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
				if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
			})
			*/
		}
		
		//replace icons with FontAwesome icons like above
		function updatePagerIcons(table) {
			var replacement = 
			{
				'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
				'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
				'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
				'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
			};
			$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
				var icon = $(this);
				var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
				
				if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
			})
		}
	
		function enableTooltips(table) {
			$('.navtable .ui-pg-button').tooltip({container:'body'});
			$(table).find('.ui-pg-div').tooltip({container:'body'});
		}
	
	});

 	</script>
</body>
</html>