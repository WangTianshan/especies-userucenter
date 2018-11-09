/**
 * Created by Tianshan on 16-7-28.
 */
function editThisObject(id,type) {
    window.location.href=base+"user/"+type+"/edit/"+id;
};
function removeThisObject(id,type) {
    var r=confirm(warning_remove);
    if (r==true)
    {
        $.get("user/"+type+"/rest/remove/"+id,
            {},
            function(data,status){
                if(status){
                    if(data){
                        layer.msg(warning_remove_ok,
                            {
                                time: 500, //1.5s后自动关闭
                            },
                            function(){
                            //window.location.reload();//刷新当前页面.
                            $('[name="refresh"]').click();//刷新当前页面.
                        });
                    }
                    else{
                        layer.msg(warning_error, function(){
                        });
                    }
                }
                else{
                    layer.msg(warning_error, function(){
                    });
                }
            });
    }
    else
    {
        layer.msg(warning_cancel,
            {
                time: 500, //0.5s后自动关闭
            });
    }
};
//选择编辑
function editSelectObject(type){
    var number=0;
    var checkId="";
    $("input:checkbox[id^='sel']:checked").each(function(i){
        number=number+1;
        checkId=$(this).attr('id');
        checkId=checkId.substring(4);
    });
    if(number==0){
        alert(warning_select);
    }
    else if(number>1){
        alert(warning_select_edit);
    }
    else{
        editThisObject(checkId,type);
    }
};
//批量删除
function removeSelectObject(type){
    var number=0;
    var checkId="";
    $("input:checkbox[id^='sel']:checked").each(function(i){
        number=number+1;
    });

    if(number==0)
    {alert(warning_select);}
    else {
        var msg = warning_select_remove1+number+warning_select_remove2;
        var ids="";
        if (confirm(msg)==true){
            $("input:checkbox[id^='sel']:checked").each(function(i){
                checkId=$(this).attr('id');
                checkId=checkId.substring(4);
                if(i==0){
                    ids=checkId;
                }
                else{
                    ids=ids+"￥"+checkId;
                }
            });
            $.ajax({
                url: 'user/'+type+'/rest/removeMany/'+ids,
                cache: false,
                success: function (data) {
                    if(data>0){
                        if(data==number){
                            layer.msg(warning_select_remove_ok1+number+warning_select_remove_ok2,
                                {
                                    time: 1000, //1.5s后自动关闭
                                },
                                function(){
                                    //window.location.reload();//刷新当前页面.
                                    $('[name="refresh"]').click();//刷新当前页面.
                                });
                        }
                        else {
                            layer.msg(warning_select_remove_ok1+number+warning_select_remove_ok2+','+(number-data)+warning_select_remove_ok3,
                                {
                                    time: 2000, //1.5s后自动关闭
                                },
                                function(){
                                    //window.location.reload();//刷新当前页面.
                                    $('[name="refresh"]').click();//刷新当前页面.
                                });
                        }
                    }
                    else{
                        layer.msg(warning_error+','+warning_error2);
                    }
                },
                error: function () {
                    layer.msg(warning_error);
                }
            });
        }else{
            layer.msg(warning_cancel,
                {
                    time: 500, //0.5s后自动关闭
                });
        }
    }
};