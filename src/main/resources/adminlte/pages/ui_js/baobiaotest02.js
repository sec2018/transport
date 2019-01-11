/**
 * Created by 03010335 on 2018/8/22.
 */

$(function () {

    $.ajax({
        url: "/transport/api/getalllines",
        method: "GET",
        success: function (data) {
            if (data.data == null) {
                alert(data.msg);
                return;
            }else{
                alert(data.msg);
            }
        }
    })



    $.getJSON ("/transport/adminlte/pages/ui_js/data.json", function (data1)
    {
        //一个data数组
        if(data1.length>0){
            var num2 = data1[0].detail.length;
            var num = data1.length*num2;
            var numdetail = 0
            var data = new Array(num);   //先声明一维
            for(var k=0;k<data.length;k++){        //一维长度为i,i为变量，可以根据实际情况改变
                data[k]=new Array(num2);    //声明二维，每一个一维数组里面的一个元素都是一个数组
            }
        }
        for (var i=0;i<data1.length;i++){
            for (var j=0;j<data1[i].detail.length;j++){
                data[numdetail][0]="<input type=\"checkbox\" value=\"" + data1[i].detail[j].day + "\" class=\"blod\" name=\"tableline\">"+data1[i].detail[j].day;
                data[numdetail][1]=data1[i].detail[j].count;
                data[numdetail][2]=data1[i].detail[j].one;
                data[numdetail][3]=data1[i].detail[j].one1;
                data[numdetail][4]=data1[i].detail[j].one2;
                data[numdetail][5]=data1[i].detail[j].one3;
                data[numdetail][6]=data1[i].detail[j].two1;
                data[numdetail][7]=data1[i].detail[j].two2;
                data[numdetail][8]=data1[i].detail[j].two3;
                data[numdetail][9]=data1[i].detail[j].two4;
                data[numdetail][10]=data1[i].detail[j].three;
                data[numdetail][11]=data1[i].detail[j].four;
                data[numdetail][12]=data1[i].detail[j].five;
                if(numdetail%num2==0){
                    data[numdetail][13]=num2;
                }else{
                    data[numdetail][13]=0;
                }
                ++numdetail;
            }
        }

        $('#datatable').DataTable({
            'data': data,
            "jQueryUI": true,
            'paging'      : true,
            lengthMenu: [　//显示几条数据设置
                [10, 20,30, 50,-1],
                ['10 条', '20 条','30条', '50 条','全部']
            ],
            'searching'   : true,
            'ordering'    : false,
            "pageLength": 10, //每行显示记录数
            'info'        : true,
            'autoWidth'   : true,
            // "language": language,
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
            'columnDefs': [{
                targets: 0,
                createdCell: function (td, cellData, rowData, row, col) {
                    var rowspan = rowData[13];
                    if (rowspan > 1) {
                        $(td).attr('rowspan', rowspan)
                    }
                    if (rowspan == 0) {
                        $(td).remove();
                    }
                }
            }],
            dom: 'Bfrtip',
            buttons: [
                'pageLength',
                {
                    text: "导出Excel",
                    extend: 'excelHtml5',
                    filename:"断头统计报表"+"_"+new Date(),
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
                    },
                }
            ],

        });
        //最多只能选中三个
        var cb1 = $('[name="tableline"]');
        cb1.change(function () {
            if (this.checked && cb1.filter(':checked').length > 3) {
                alert("最多能选三个！");
                this.checked = false;
            }
        });
    });
})
