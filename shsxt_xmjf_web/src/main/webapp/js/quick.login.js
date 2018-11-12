$(function () {
    $(".validImg").click(function () {
        $(this).attr("src", ctx + "/image?time=" + new Date())
    })

    $("#clickMes02").click(function () {
        var phone = $("#phone").val();
        var imageCode = $("#code").val();

        if (isEmpty(phone)) {
            layer.tips("请输入手机号", "#phone");
            return;
        }
        if (isEmpty(imageCode)) {
            layer.tips("请输入验证码", "#code");
            return;
        }


        $.ajax({
            url: ctx + "/sendSms",
            type: "post",
            data: {
                phone: phone,
                imageCode: imageCode,
                type: 2,
            },
            dataType: "json",
            success: function (data) {

                console.log(data);
                if (data.code == 200) {
                    //短信发送成功
                    var time = 6;

                    var a = setInterval(function () {
                        time--;
                        if (time == 0) {
                            clearInterval(a);
                            $("#clickMes02").attr("disabled", "");
                            $("#clickMes02").val("获取验证码");
                            $("#clickMes02").css("background", "#fcb22f");
                        } else {
                            $("#clickMes02").attr("disabled", "disabled");
                            $("#clickMes02").val("(" + time + "秒)");
                            $("#clickMes02").css("background", "#ababab");
                        }
                    }, 1000);
                } else {
                    $(".validImg").attr("src", ctx + "/image?time=" + new Date());
                    layer.tips(data.msg, "#clickMes02")
                }
            }


        })
    })


    $("#login").click(function () {
        var phone = $("#phone").val();
        var code = $("#verification").val();

        if (isEmpty(phone)) {
            layer.tips("请输入手机号", "#phone");
            return;
        }
        if (isEmpty(code)) {
            layer.tips("请输入短信验证码", "#verification");
            return;
        }

        $.ajax({
            type: "post",
            url: ctx + "user/quickLogin",
            data: {
                phone: phone,
                code: code,
            },
            dataType: "json",
            success: function (data) {
                if (data.code == 200) {
                    window.location.href = ctx + "/index";
                } else {
                    layer.tips(data.msg, "#login");
                }
            }
        })
    })

})