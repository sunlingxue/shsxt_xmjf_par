$(function () {
   $(".validImg").click(function () {
       $(this).attr("src",ctx+"/image?time="+new Date());
   })


    $("#clickMes").click(function () {
        var phone = $("#phone").val();
        var imageCode = $("#code").val();

        if (isEmpty(phone)){
            layer.tips("请输入手机号码","#phone");
            return;
        }

        if (isEmpty(imageCode)){
            layer.tips("请输入图片验证码","#code");
            return;
        }



        $.ajax({
            url:ctx+"/sendSms",
            type:"post",
            data:{
                phone:phone,
                imageCode:imageCode,
                type:1,
            },
            dataType:"json",
            success:function (data) {
                if (data.code==200){
                    //倒计时处理
                    var time=10;

                    var a = setInterval(function () {
                        time--;
                        if (time==0){
                            clearInterval(a);
                            $("#clickMes").attr("disabled","");
                            $("#clickMes").css("background","#fcb22f");
                            $("#clickMes").val("获取验证码");
                        } else{
                            $("#clickMes").attr("disabled","disabled");
                            $("#clickMes").css("background","#ababab");
                            $("#clickMes").val("("+time+"秒)");
                        }

                    },1000);

                }else{
                    $(".validImg").attr("src",ctx+"/image?time="+new Date());
                    layer.tips(data.msg,"#clickMes");
                }
            }
        })

    });


    $("#register").click(function () {
        var phone = $("#phone").val();
        var code = $("#verification").val();
        var password = $("#password").val();

        if (isEmpty(phone)){
            layer.tips("请输入手机号码","#phone");
            return;
        }
        if (isEmpty(code)){
            layer.tips("请输入手机验证码","#verification");
            return;
        }
        if (isEmpty(password)){
            layer.tips("请输入密码","#password");
            return;
        }


        $.ajax({
            url:ctx+"/user/register",
            type:"post",
            data:{
                phone:phone,
                code:code,
                password:password,
            },
            dataType:"json",
            success:function (data) {
                if (data.code == 200 ){
                    window.location.href=ctx+"/login";
                }else{
                    layer.tips(data.msg,"#register");
                }
            }
        })

    })



});
