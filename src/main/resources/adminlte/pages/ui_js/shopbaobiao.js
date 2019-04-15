/**
 * Created by WangZJ on 2019/1/26.
 */
/**
 * Created by 03010335 on 2018/8/22.
 */
var viewdata;
var checkshopid;
$(function () {
    var senddata = {};
    senddata.startitem = 1;
    senddata.pagesize = 10;
    $.ajax({
        url: "/transport/api/getallshops",
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
                    data.data[i].action = "<a href='javascript:void(0);'onclick='checkThisRow("+ data.data[i].shop_id + ",\""+ data.data[i].shopcheckstatus + "\")'  class='down btn btn-default btn-xs'><i class='fa fa-arrow-down'></i> 审核</a>&nbsp;&nbsp;<a href='javascript:void(0);'onclick='ShowImage(\""+ data.data[i].shop_url + "\")'  class='down btn btn-default btn-xs'><i class='fa fa-arrow-down'></i> 营业执照</a>";
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
                        { "data": "shop_id","width":"40px" },
                        { "data": "shop_name" },
                        { "data": "nickname", "width":"70px" },
                        { "data": "shop_tel" },
                        { "data": "shop_procity","width":"140px" },
                        { "data": "shop_detailarea","width":"230px" },
                        { "data": "shopcheckstatus"},
                        { "data": "action" ,"width":"120px" }
                    ],
                    buttons: [
                        'pageLength',
                        {
                            text: "导出Excel",
                            extend: 'excelHtml5',
                            filename:"商户信息报表"+"_"+new Date(),
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
            }
        }
    });

    $("#btnsave").click(function () {
        var selectvalue = $(".checkradio input:radio:checked").val()=="1"?true:false;
        var senddata = {};
        senddata.shopid = checkshopid;
        senddata.ispass = selectvalue;
        $.ajax({
            url: "/transport/api/admincheckshop",
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
    checkshopid = id;
}

function ShowImage(shop_url) {
    var shopsenddata = {};
    shopsenddata.imagename = shop_url;
    shopsenddata.roleid = "2";
    var newwin = window.open();
    $.ajax({
        url: "/transport/api/adminshowimage",
        method: "GET",
        data: shopsenddata,
        // beforeSend: function(request) {
        //     request.setRequestHeader("roleid", "2");
        //     request.setRequestHeader("token", window.localStorage.getItem("transport_token"));
        // },
        success: function (res) {
            var myimg = newwin.document.createElement("img");
            myimg.setAttribute('style','width:600px;display:block;margin:20px auto;')
            myimg.src = "data:image/png;base64,"+res.data;
            newwin.document.body.appendChild(myimg);
        }
    });
}