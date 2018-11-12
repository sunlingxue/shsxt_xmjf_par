$(function () {
    loadItemData();

    $(".tab").click(function () {
        $(this).addClass("list_active");
        $(".tab").not(this).removeClass("list_active");

        var itemCycle = $(this).index();
        var itemType = $("#itemType").val();
        var isHistory = 0;
        if (itemCycle == 4){
            isHistory = 1;
        }

        loadItemData(itemCycle,itemType,isHistory);
    })
})


/**
 * 加载数据
 */
function loadItemData(itemCycle,itemType,isHistory,pageNum) {
    
    var params = {};

    params.isHistory=0;// 默认查询可投项目
    
    if (!isEmpty(itemCycle)){
        params.itemCycle=itemCycle;
    }
    if (!isEmpty(itemType)){
        params.itemType=itemType;
    }
    if (!isEmpty(isHistory)){
        params.isHistory=isHistory;
    }
    if (!isEmpty(pageNum)){
        params.pageNum=pageNum;
    }


    $.ajax({
        url: ctx + "/item/list",
        type: "post",
        data: params,
        dataType: "json",
        success: function (data) {
            var paginator = data.paginator;
            var list = data.list;

            if (paginator.total > 0) {

                initTrHtml(list);

                initPageHtml(paginator);

                //渲染进度条
                initItemScale();

                //倒计时
                initSyTime();

            } else {
                $("#pcItemList").html("");
            }

        }
    })
}

/**
 * 拼接dom
 * @param list
 */
function initTrHtml(list) {
    var trs = "";
    for (var i = 0; i < list.length; i++) {
        var temp = list[i];

        //利率添加
        trs += "<tr>";
        trs += "<td><strong>" + temp.item_rate + "</strong>";
        if (temp.item_add_rate > 0) {
            trs += "<span>+" + temp.item_add_rate + "</span>";
        }

        trs += "%</td>";


        //拼接项目期限
        trs += "<td>" + temp.item_cycle + "</td>";

        //拼接项目名称
        trs += "<td>" + temp.item_name;
        if (temp.item_isnew == 1) {
            trs = trs + "<strong class='colorful' new>NEW</strong>";
        }
        if (temp.item_isnew == 0 && temp.move_vip == 1) {
            trs = trs + "<strong class='colorful' app>APP</strong>";
        }
        if (temp.item_isnew == 0 && temp.move_vip == 0 && temp.item_isrecommend == 1) {
            trs = trs + "<strong class='colorful' hot>HOT</strong>";
        }
        if (temp.item_isnew == 0 && temp.move_vip == 0 && temp.item_isrecommend == 0 && !(isEmpty(temp.password))) {
            trs = trs + "<strong class='colorful' psw>LOCK</strong>";
        }
        trs += "</td>";

        //信用等级
        trs += "<td class='trust_range'>";

        if (temp.total > 90) {
            trs = trs + "A+";
        }

        if (temp.total > 85 && temp.total <= 90) {
            trs = trs + "A";
        }
        if (temp.total > 75 && temp.total <= 85) {
            trs = trs + "A-";
        }
        if (temp.total > 65 && temp.total <= 75) {
            trs = trs + "B";
        }

        trs += "</td>";

        // 第三方担保机构
        trs = trs + "<td class='company'> <img src='/img/logo.png'></td>";

        //投资进度
        if (temp.item_status !=1){
            trs = trs + "<td class = 'data-val'  data-val='"+temp.item_scale+"'></td>";
        }else{
            // 显示项目倒计时操作
            /**
             *
             * @type {string}
             */
            trs += "<td ><strong class='countdown time' data-item='"+temp.id+"'  data-time="+temp.syTime+">" +
                "<time class='hour'></time>&nbsp;:&nbsp;" +
                "<time class='min'></time>&nbsp;:&nbsp;" +
                "<time class='sec'></time>" +
                "</strong></td>";
        }



        // 操作项
        trs = trs + "<td>";
        if (temp.item_status == 1) {
            trs = trs + "<p><a><input class='countdownButton' valid type='button' value='即将开标'>" +
                "</a></p>";
        }

        if (temp.item_status == 10 || temp.item_status == 13 || temp.item_status == 18) {
            trs = trs + "<p class='left_money'>可投金额" + temp.syAccount + "元</p>" +
                "<p><a ><input valid type='button' value='立即投资'></a></p>";
        }
        if (temp.item_status == 20) {
            trs = trs + "<p><a ><input not_valid type='button' value='已抢完'></a></p>";
        }


        if (temp.item_status == 30 || temp.item_status == 31) {
            trs = trs + "<p><a ><input not_valid type='button' value='还款中'></a></p>";
        }

        if (temp.item_status == 32) {
            trs = trs + "<p style='position: relative'> <a  class='yihuankuan'>已还款</a> <div class='not_valid_pay'></div> </p>";
        }

        if (temp.item_status == 23) {
            trs = trs + "<p><a ><input not_valid type='button' value='已满标'></a></p>";
        }
        trs = trs + "</td>";
        trs += "</tr>";

    }
    $("#pcItemList").html(trs);
}


/**
 * 下拉框改变查询数据
 * @param itemType
 */
function initItemData(itemType) {

    var itemCycle;

    $(".tab").each(function () {
        if ($(this).hasClass("list_active")) {
            itemCycle = $(this).index();
        }
    });

    var isHistory = 0;
    if (itemCycle==4){
        isHistory = 1;
    }

    loadItemData(itemCycle,itemType,isHistory)

}

/**
 * 拼接页码
 * @param paginator
 */
function initPageHtml(paginator) {

    var pageArrary = paginator.navigatepageNums;

    var lis = "";

    for (var i = 0;i<pageArrary.length;i++){
        var p = pageArrary[i];

        var href = "javascript:toPageItemData("+p+")";

        if (p==paginator.pageNum){
            lis += "<li class='active'><a href='"+href+"' title= 第"+p+"页>"+p+"</a></li>"
        } else{
            lis += "<li ><a href='"+href+"' title= 第"+p+"页>"+p+"</a></li>"
        }

    }

    $("#pages").html(lis);
}

/**
 * 分页查询
 * @param pageNum
 */
function toPageItemData(pageNum) {
    var itemCycle;

    $(".tab").each(function () {
        if ($(this).hasClass("list_active")) {
            itemCycle = $(this).index();
        }
    });

    var isHistory = 0;
    if (itemCycle==4){
        isHistory = 1;
    }

    var itemType=$("#itemType").val();

    loadItemData(itemCycle,itemType,isHistory,pageNum);
}


/**
 * 初始化圆形进度条
 */
function initItemScale() {
    $('.data-val').radialIndicator({
        barColor:"orange",
        barWidth: 5,
        roundCorner : true,
        percentage: true,
        radius:30
    });

    $(".data-val").each(function(){
        var radialObj = $(this).data('radialIndicator');
        var val=$(this).attr("data-val");
        radialObj.animate(val);
    });
}

/**
 * 添加倒计时
 */
function initSyTime() {
    $(".countdown").each(function () {
        var syTime = $(this).attr("data-time");
        var itemId = $(this).attr("data-item");
        timer(syTime,$(this),itemId);
    })
}

function timer(intDiff,obj,itemId){
    if( obj.timers){
        clearInterval( obj.timers);
    }
    obj.timers=setInterval(function(){
        var day=0,
            hour=0,
            minute=0,
            second=0;//时间默认值
        if(intDiff > 0){
            day = Math.floor(intDiff / (60 * 60 * 24));
            hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
            minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
            second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
        }

        if (minute <= 9) minute = '0' + minute;
        if (second <= 9) second = '0' + second;
        obj.find('.hour').html(hour);
        obj.find('.min').html(minute);
        obj.find('.sec').html(second);
        intDiff--;
        if(intDiff==-1){
            $.ajax({
                url : ctx+'/item/updateBasItemStatusToOpen',
                dataType : 'json',
                type : 'post',
                data:{
                    itemId:itemId
                },
                success : function(data) {
                    if(data.code==200){
                        window.location.reload();
                    }
                },
                error : function(textStatus, errorThrown) {

                }
            });
        }
    }, 1000);
}