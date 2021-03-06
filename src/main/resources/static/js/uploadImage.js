$(function() {
    //上传文件按钮
    document.getElementById('file').addEventListener('change',function(e){
        var files = this.files;
        var reader = new FileReader();
        reader.readAsDataURL(files[0]);
        reader.onload = function(e){
            var html="<img src='"+ e.target.result + "' class='img-thumbnail center-block' id='img-view-area'/>";
            var mb = (e.total/1024)/1024;
            if(mb>= 50){
                alert('文件大小大于50M');
                return;
            }
            document.getElementById('img-view').innerHTML = html;
            $('#fileType').val(0);
            $('#cropperBtn').enable();
        }
    });
    $("#imageView").click(function() {
        //模拟点击File
        $("#file").click();
    });
    $("#uploadBtn").click(function() {
        if(verificationGroups()){
            // 进度条归零
            $("#progressBar").width("0%");
            // 上传按钮禁用
            $("#uploadBtn").attr("disabled", true);
            // 进度条显示
            $("#progressBar").parent().show();
            $("#progressBar").parent().addClass("active");
            upload("带进度条的文件上传");
        }
    });
    function refreshBtn(){
        setTimeout(function() {
            $("#uploadBtn").text("开始鉴定");
            $("#uploadBtn").removeAttr("disabled");
        }, 1500);
    }
    function upload(name) {
        var formData = new FormData();
        try {
            //判断类型
            if($('#fileType').val()==1){
                formData.append('file', CropingImage().toDataURL());
            }
            else{
                formData.append('file', $('#file')[0].files[0]);
            }
        }
        catch(err){
            formData.append('file', $('#file')[0].files[0]);
        }

        formData.append('name', name);
        formData.append('groups', $('#groups').val());
        formData.append('_csrf', $('input[name="_csrf"]').val());
        function onprogress(evt) {
            // 写要实现的内容
            var progressBar = $("#progressBar");
            if (evt.lengthComputable) {
                var completePercent = Math.round(evt.loaded / evt.total * 100);
                progressBar.width(completePercent + "%");
                $("#progressBar").text(completePercent + "%");
            }
        }
        var xhr_provider = function() {
            var xhr = jQuery.ajaxSettings.xhr();
            if (onprogress && xhr.upload) {
                xhr.upload.addEventListener('progress', onprogress, false);
            }
            return xhr;
        };
        layer.load(2);
        $.ajax({
            url : 'user/upload/rest/img',
            type : 'POST',
            cache : false,
            data : formData,
            processData : false,
            contentType : false,
            xhr : xhr_provider,
            success : function(result) {
                layer.closeAll('loading');
                if (result.code == "2") {
                    $("#uploadBtn").text(result.message);
                    layer.msg(result.message,
                        {
                            time: 1500, //1s后自动关闭
                        }
                    );
                    $('#resultsTable').bootstrapTable('load', result.results);

                    setTimeout(function() {
                        $("#uploadBtn").text("鉴定完成");
                    }, 10000);
                } else if(result.code=="-4"){
                    $("#uploadBtn").text(result.message);
                    layer.msg(result.message,
                        {
                            time: 1000, //1s后自动关闭
                        });
                } else {
                    $("#uploadBtn").text(result.message);
                    layer.msg(result.message,
                        {
                            time: 1000, //1s后自动关闭
                        });
                }
                // 进度条隐藏
                $("#progressBar").parent().hide();
                refreshBtn();
            },
            error : function(data) {
                layer.closeAll('loading');
                console.info(data);
                alert("error：" + data);
                refreshBtn();
            }
        })
    }
});

function verificationGroups(){
    var canAutoidentify=false;
    if($('#file').val()==null || $('#file').val() == ""){
        layer.msg(no_image, function(){
        });
    }
    else if($('#groups').val()==null){
        layer.msg(warning_nogroups, function(){
        });
    }
    else {
        //判断类群是否开放识别
        $.ajax({
            url: 'user/groups/rest/canAutoidentify',
            type: 'GET',
            cache: false,
            async:false,
            data: {groupId: $('#groups').val()},
            success: function (result) {
                if (result == true) {
                    canAutoidentify=true;
                } else {
                    layer.msg(autoidentify_unopen, function(){
                    });
                }
            },
            error: function (data) {
                layer.msg(autoidentify_unopen, function(){
                });
            }
        });
    }
    return canAutoidentify;
}