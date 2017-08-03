/**
 * Created by Administrator on 2017/6/15.
 */
// 分页
$(document).ready(function(){
    $('#dataTables-example').DataTable({
        searching: false,// 搜索
        ordering: false,// 排序
        lengthChange: false,
        "scrollX": true,
        "scrollY": true,
        "scrollCollapse": true,
        language: {
            zeroRecords: "没有匹配结果",
            info : "显示  _START_ ~ _END_ 条，共有数据   _TOTAL_ 条",
            infoEmpty: "显示第 0 ~ 0 项结果，共 0 项",
            paginate: {
                first:    '第一页',
                previous: '上一页',
                next:     '下一页',
                last:     '最后一页'
            },
        },

    });
});