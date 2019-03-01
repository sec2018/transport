/**
 * Created by 03010335 on 2018/8/22.
 */
var viewdata;
var checkcompanyid;
$(function () {
    var senddata = {};
    senddata.startitem = 1;
    senddata.pagesize = 10;
    $.ajax({
        url: "/transport/api/getallcompany",
        method: "GET",
        data: senddata,
        beforeSend: function(request) {
            request.setRequestHeader("roleid", "0");
            request.setRequestHeader("token", window.localStorage.getItem("transport_token"));
        },
        success: function (data) {
            if (data.data == null) {
                alert(data.msg);
                return;
            }else{
                for(var i = 0;i<data.data.length;i++){
                    data.data[i].action = "<a href='javascript:void(0);'onclick='checkThisRow("+ data.data[i].company_id + ",\""+ data.data[i].companycheckstatus + "\")'  class='down btn btn-default btn-xs'><i class='fa fa-arrow-down'></i> 审核</a>";
                }
                viewdata = $.extend(true,[],data.data);
                var dt = $('#datatable').DataTable({
                    data: data.data,
                    "jQueryUI": true,
                    'paging'      : true,
                    lengthMenu: [　//显示几条数据设置
                        [10, 20,30, 50,-1],
                        ['10 条', '20 条','30条', '50 条','全部']
                    ],
                    'searching'   : true,
                    'ordering'    : true,
                    "pageLength": 10, //每行显示记录数
                    'info'        : true,
                    'autoWidth'   : true,
                    "oLanguage": {
                        buttons: {
                            pageLength: {
                                _: "每页%d条记录",
                                '-1': "显示所有记录"
                            }
                        },
                        "sLengthMenu": "每页显示 _MENU_ 条记录",
                        "sZeroRecords": "暂无数据",
                        "sInfo": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录",
                        "sInfoEmtpy": "暂无数据",
                        "sInfoFiltered": "数据表中共为 _MAX_ 条记录)",
                        "sProcessing": "正在加载中",
                        "sSearch": "搜索",
                        "sUrl": "",
                        "oPaginate": {
                            "sFirst": "第一页",
                            "sPrevious": " 上一页 ",
                            "sNext": " 下一页 ",
                            "sLast": " 最后一页 "
                        }
                    },
                    "scrollY": "450px",
                    dom: 'Bfrtip',
                    "processing": true,
                    "columns": [
                        { "data": "company_id"},
                        { "data": "company_name" },
                        { "data": "nickname" },
                        { "data": "company_tel" },
                        { "data": "line" },
                        { "data": "company_procity","width":"140px"},
                        { "data": "company_tel"},
                        { "data": "companycheckstatus"},
                        { "data": "action" }
                    ],
                    buttons: [
                        'pageLength',
                        {
                            text: "导出Excel",
                            extend: 'excelHtml5',
                            filename:"物流公司报表"+"_"+new Date(),
                            exportOptions: {
                                format: {
                                }
                            },
                            customize: function (win) {
                                console.log(win);
                                var sheet = win.xl.worksheets['sheet1.xml'];
                                $('cols col', sheet).attr({'width':30});
                            }
                        },
                        {
                            extend: 'print',
                            text:"打印报表",
                            messageTop: function () {
                            }
                        }
                    ]
                });

                // Array to track the ids of the details displayed rows
                var detailRows = [];

                $('#tbody').on( 'click', 'tr td.details-control', function () {
                    var tr = $(this).closest('tr');
                    var row = dt.row( tr );
                    var idx = $.inArray( tr.attr('id'), detailRows );

                    if ( row.child.isShown() ) {
                        tr.removeClass( 'details' );
                        row.child.hide();

                        // Remove from the 'open' array
                        detailRows.splice( idx, 1 );
                    }
                    else {
                        tr.addClass( 'details' );
                        row.child( format( row.index() ) ).show();

                        // Add to the 'open' array
                        if ( idx === -1 ) {
                            detailRows.push( tr.attr('id') );
                        }
                    }
                } );

                // On each draw, loop over the `detailRows` array and show any child rows
                dt.on( 'draw', function () {
                    $.each( detailRows, function ( i, id ) {
                        $('#'+id+' td.details-control').trigger( 'click' );
                    } );
                });
            }
        }
    })


    function format ( index ) {
        return '物流公司名称: '+viewdata[index].company_name+'   &nbsp;&nbsp;&nbsp;&nbsp;物流公司省市：'+viewdata[index].company_procity+ '   &nbsp;&nbsp;&nbsp;&nbsp;详细地址：'+viewdata[index].company_detailarea+ '<br>' +
            '物流公司注册电话: '+viewdata[index].company_tel+ '   &nbsp;&nbsp;&nbsp;&nbsp;服务电话: '+viewdata[index].service_tel +'   &nbsp;&nbsp;&nbsp;&nbsp;投诉电话: '+viewdata[index].complain_tel+ '   &nbsp;&nbsp;&nbsp;&nbsp;默认线路: '+viewdata[index].line+  '<br>' +
            '到站地址: '+viewdata[index].arrive_detail_addr+ '   &nbsp;&nbsp;&nbsp;&nbsp;到站电话: '+viewdata[index].arrive_tel;
    }

    function timetrans(date){
//    var date = new Date(date*1000);//如果date为13位不需要乘1000
        var date = new Date(date);
        var Y = date.getFullYear() + '-';
        var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        var D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + 'T';
        var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
        var m = (date.getMinutes() <10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
        var s = (date.getSeconds() <10 ? '0' + date.getSeconds() : date.getSeconds());
        return Y+M+D+h+m+s;
    }

    $("#btnsave").click(function () {
        var selectvalue = $(".checkradio input:radio:checked").val()=="1"?true:false;
        var senddata = {};
        senddata.companyid = checkcompanyid;
        senddata.ispass = selectvalue;
        $.ajax({
            url: "/transport/api/admincheckcompany",
            method: "POST",
            data: senddata,
            beforeSend: function(request) {
                request.setRequestHeader("roleid", "0");
                request.setRequestHeader("token", window.localStorage.getItem("transport_token"));
            },
            success: function (data) {
                $('#myModal').modal('hide');
                if (data.data == null) {
                    alert(data.msg);
                    window.location.reload();
                    return;
                }
            }
        });
    });
})



function checkThisRow(id,checkstatus) {
    if(checkstatus == "通过"){
        $("#myModal #optionsRadios1").attr("checked", true);
    }else{
        $("#myModal #optionsRadios2").attr("checked", true);
    }
    $('#myModal').modal('toggle');
    checkcompanyid = id;
}