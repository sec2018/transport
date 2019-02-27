/**
 * Created by 03010335 on 2018/8/22.
 */
var billviewdata;
var billdata;
$(function () {
    var senddata = {};
    senddata.startitem = 1;
    senddata.pagesize = 10;
    senddata.isfinishflag = 1;
    $.ajax({
        url: "/transport/api/getusertabbill",
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
                billviewdata = $.extend(true,[],data.data.billviewdata);
                billdata = $.extend(true,[],data.data.billdata);
                for(var i = 0;i<data.data.billviewdata.length;i++){
                    data.data.billviewdata[i].statustime = timetrans(data.data.billviewdata[i].statustime).replace('T'," ");
                }
                var dt = $('#datatable').DataTable({
                    data: data.data.billviewdata,
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
                        {
                            "class":          "details-control",
                            "orderable":      false,
                            "data":           null,
                            "defaultContent": "",
                            "width": "1px"
                        },
                        { "data": "bill_code","width":"140px"},
                        { "data": "line" },
                        { "data": "shop_name" },
                        { "data": "rec_name" },
                        { "data": "company_name" },
                        { "data": "status"},
                        { "data": "statustime","width":"140px" }
                    ],
                    buttons: [
                        'pageLength',
                        {
                            text: "导出Excel",
                            extend: 'excelHtml5',
                            filename:"货运托单报表"+"_"+new Date(),
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
        return '运单编号: '+billdata[index].bill_code+'   &nbsp;&nbsp;&nbsp;&nbsp;物流公司名称：'+billdata[index].company_name+ '   &nbsp;&nbsp;&nbsp;&nbsp;商品名称：'+billdata[index].goodsname+
            '   &nbsp;&nbsp;&nbsp;&nbsp;商品数量: '+billdata[index].goodsnum+ '   &nbsp;&nbsp;&nbsp;&nbsp;快递费用: '+billdata[index].delivery_fee+ '<br>' +'收件人姓名: '+billdata[index].rec_name+ '   &nbsp;&nbsp;&nbsp;&nbsp;收件人地址: '+billdata[index].rec_procity+billdata[index].rec_detailarea+  '   &nbsp;&nbsp;&nbsp;&nbsp;收件人电话: '+billdata[index].rec_tel+ '<br>' +
            '寄件人姓名: '+billdata[index].sender_name+ '   &nbsp;&nbsp;&nbsp;&nbsp;寄件人地址: '+billdata[index].sender_procity+billdata[index].sender_detailarea+ '   &nbsp;&nbsp;&nbsp;&nbsp;寄件人电话: '+billdata[index].sender_tel+ '<br>';
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
    
    function deleteRow(obj) {
        alert(obj.id);
    }
})
