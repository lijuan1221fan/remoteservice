lastPage$(function () {




    $('.items-list').click(function (event) {
        $(this).css("-webkit-tap-highlight-color", "rgba(255,0,0,0)");
        $(this).css("outline", "none");
        if ($(this).hasClass('has-go')) {
            console.log($(this).index());
            if(window.sessionStorage){
                sessionStorage.setItem("funIndex", $(this).index());
            }
            window.location.href = 'timelyBusiness.html';
        } else {
            // nowIsNotOk();
            layer.msg('此功能暂未开放');
        }
        event.stopPropagation();
    });
    $(".go-home").hide();//显示回首页按钮
    // commonFun.fireAndroidInfo({command:'CMD_GET_SOME_MSG',params:{funType:4}},true);

    // commonFun.timerBack($(".home-timer-back")[0],150);
});


