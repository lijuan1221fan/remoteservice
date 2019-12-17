//页面适配
getRem(1280, 100);

function getRem(pwidth, prem) {
    var winWidth = document.documentElement.offsetWidth || document.body.offsetWidth;
    var oHtml = document.getElementsByTagName('html')[0];
    oHtml.style.fontSize = prem * winWidth / pwidth + 'px';
}
window.addEventListener('resize', function () {
    getRem(1280, 100);
})

/**
 * 全局变量
 */
var zlayerIndex = null;
var currentBackId = 0;
var animation_duration = 400; //整体切换动画时厂
var ws; //websocket实例对象
var has_started = false; //是否初次加载页面
var deviceData = null; //设备数据
var cardInfo = null; //身份信息
var departmentList = null; //部门列表
var pagePos = "index"; //页面位置 index:播号页 csIndex:部门列表 picker:挑选业务 finishCount:叫号结束
var serviceKeyThis = ""; //本机服务中心serviceKey
var deviceCodeThis = ""; //本机设备code
var socket_loading = null; //socket启动loading
var request_loading = null; 
var serviceCenterInterval = null; //播号服务中心轮询时钟
var bmData = []; //取号信息
var dmData = [];
var TYPE = {//socket的type信息
  DEVICESTATE:15, //设备状态
  DEPARTMENTS:16 //部门列表
}
var departMentParams = {//部门列表默认分页信息
  pageSize: 10,
  pageNum: 1,
  state: 0,
  type: TYPE.DEPARTMENTS,
};
var requesting = false; // 请求锁定
var pageJumping = false; //翻页动画操作&&时钟布置锁
var numPicking = false; // 取号锁定
var hasPicked = false; // 是否取号成功


function todo(mark,data){//写入页面
    if(isPC){
      console.log(mark,'***',data)
      return;
    }
    data = typeof data == "object"?JSON.stringify(data):data;
    console.log(mark+"___",data)
    $("#test-html-box").append(mark+"___"+JSON.stringify(data))
  }
  
/**
 * 封装的ajax函数，可以防止二次点击功能
 * @param data  要传过来的数据 json
 * @param callback 成功的函数 fn
 * @param async  异步还是同步  默认异步
 */

function ajaxdata(option, data, callback) {
    var _lodingIndex = null;

    /*if (!ajaxdata.obj) {
        ajaxdata.obj = {};
    }*/

    /*zhanghao 暂时关闭*/
    layer.close(_lodingIndex);

    if (option.isLoding != -1) {
        setTimeout(function () {
            _lodingIndex = lodingDom();
        }, 1);
    }

    //定义发送头
    if (!option.contentType) {
      option.contentType = 'application/json';
      // option.contentType = 'application/x-www-form-urlencoded;charset=UTF-8';
    } else {
      option.contentType = 'application/json';
        if (option.contentType.toLowerCase() == 'json') {
            option.contentType = 'application/json';
        }
    }

    // layerLoad = layer.load(2);

    /*var _str = JSON.stringify(data);

    if (ajaxdata.obj[_str]) {
        _lodingIndex = lodingDom()
        return;
    } else {
        ajaxdata.obj[_str] = _str;
    }*/

    $.ajax({
        type: option.type ? option.type : 'POST',
        url: option.url,
        data: JSON.stringify(data),
        async: option.async ? false : true, //异步为true，同步false
        contentType: "application/json",
        dataType: "json",
        timeout: option.timeout ? option.timeout : 50000, //超时时间设置，单位毫秒
        success: function (data) {
            // layer.close(layerLoad);
            setTimeout(function () {
                layer.close(_lodingIndex);
            }, 1);
            callback(data.result, data);
        },
        error: function (xhr, type, exception) {
            setTimeout(function () {
                layer.close(_lodingIndex);
            }, 1);
            callback(false, {msg: "请求超时"});
        }
    });
};

/********公用loding层**********/
function lodingDom() {
    return layer.load(1, {
        shade: [0.3, '#000'], //0.1透明度的白色背景
        time: 9999999999999
    });
}
function nowIsNotOk() {
    layer.load(1, {
        content: '此功能暂未开放',
        shade: [0.3, '#000'], //0.1透明度的白色背景
        skin: 'msg',
        time: 2
    });
    return false;
}

function msgLayer(str) {
    // var str = str ? str : '通信出错';
    layer.load(1, {
        // content: str,
        shade: [0.3, '#000'], //0.1透明度的白色背景
        skin: 'msg',
        time: 2
    });
    return false;
}

//公共方法
var commonFun = (function(window, document,undefined){
    var setTime = 150,
        isTimer = false,//是否关闭定时器
        timerDom,//定时器的dom
        timerMark; //正在执行的定时器
    return{
        /**
         * 页面左上方的回首页方法
         */
        clickGoBackHomePage:function(){

        },
        //修改时间
        setTimeFun:function(time){
            if(typeof time === "number"){
                setTime = time;
            }
        },
        //停止定时器
        stopTime:function(){
            if(timerMark){
                clearTimeout(timerMark);
                timerMark = null;
            }
            isTimer = true;
            // timerDom.parentNode.removeChild(timerDom);
        },
        //    倒计时150秒
        timerBack:function(dom,time){
            // alert("首頁開始計時150秒");
            // alert($(dom).hasClass("home-timer-back"));
            var _this =this;
            isTimer = false;
            timerDom = dom;
            if(time && typeof time === "number"){
                setTime = time;
            }
            fun();
            function fun(){
                if(!isTimer){
                    if(!setTime){
                        pageJump("index")
                        // _this.goBackHomePage();
                        // window.location.href = 'index.html';
                        // commonFun.fireAndroidInfo({command:'CMD_GET_SOME_MSG',params:{funType:1}},true);
                        return
                    }else{
                        setTime--;
                        dom.innerHTML = setTime;
                        timerMark = setTimeout(fun,1000);
                    }
                }
            }
        },
        /*
         * 获取小时
         * @author wangtuizhi
         */
        getHours: function getHours() {
            var hours = new Date().getHours() + "";
            return hours.length == 1 ? "0" + hours : hours;
        },
        /**
         * 获取分钟
         * @author wangtuizhi
         */
        getMinutes: function() {
            var minutes = new Date().getMinutes() + "";
            return minutes.length == 1 ? "0" + minutes : minutes;
        },
        /**
         * 获取秒
         * @author wangtuizhi
         */
        getSeconds: function() {
            var seconds = new Date().getSeconds() + "";
            return seconds.length == 1 ? "0" + seconds : seconds;
        },
        /***
         * 创建随机字符
         * @param {Object} len 字符长度
         * 默认32位字符
         * @author wangruizhi
         */
        randomString: function(len) {
            len = len || 32;
            /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/

            var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
            var maxPos = $chars.length;
            var pwd = '';
            for (i = 0; i < len; i++) {
                pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
            }
            return pwd;
        },
    }
})(window,document);



/**
 * 所有与安卓交互的方法
 * @author zhanghao
 *
 */

var interactive = (function(){
    var __timerBackTimer = 0;
    return{
        selectId:0,//点击取号的dom
        deptid:0, //部门id
        parentId:0, //类别id
        //定时返回
        /**
         * 取号成功十秒返回首页
         * @author zhengtongwen
         */
        timerBack:function(){
            var _this = this,
                _num = 10;
            __timerBackTimer = setInterval(function () {
                if (_num <= 1) {
                    // window.location.href = "index.html"
                    goHome();
                    clearInterval(__timerBackTimer);
                } else {
                    $('#countdown').html(--_num);
                }
            }, 1000);
        },
        clearTimerBackTimer:function(){
            if(__timerBackTimer){
                clearInterval(__timerBackTimer);
                __timerBackTimer = 0;
            }
        },
        //点击取号处理
        clickhandle:function(that,printingData,flag){//jdom、key、是否打印
            var _id = that ? that.data('id') - 0 : "20061"; //业务id
            var _this = this;
            var new_id = _id.toString().substr(0, 1) + '000';
            //请求获取取号
            var urlData = {
                // 老的叫号地址
                    // url: window.location.protocol + '//' + window.location.host + '/remoteservice/api/takeBusinessNumber.do',
                    url: window.location.protocol + '//' + window.location.host + '/remoteservice/api/getBusinessNumber.do',
                },
                sendData = {
                    serviceKey: printingData ? printingData.serviceKey : "",
                    // type: new_id,
                    // businessType: _id,
                    // idCard:printingData ? printingData.identityNum : "",//身份证号
                    // deviceType:printingData ? printingData.deviceType : "",//设备类型
                    // androidBusinessType: printingData.businessType,
                    typeId:_id
                };
                console.log(sendData)
            ajaxdata(urlData,sendData, function (result, data) {
                if (result != 404) {
                    if (result) {
                        $('.page2-content').hide();
                        $('.content-result').show();
                        $("#header_back").hide();
                        var newBusinessName = data.businessName ? data.businessName.replace(/\#/g,"") : "";
                        $('#business_type').html(newBusinessName);
                        //如果三级取号成功点击返回直接到首页
                        // $('.content-apply').hide();


                        $('#oder_num').html(data.number);
                        // $('#header_back').click(function () {
                        //         window.location.href = "index.html"
                        //         // commonFun.fireAndroidInfo({command:'CMD_GET_SOME_MSG',params:{funType:1}},true);//向安卓发送信息 检查是否为一体机 然后是否自己回首页
                        //     }
                        // );
                        commonFun.stopTime();//停止当前页面的定时器并删除
                        //如果有打印数据 并且得到了取号后的数据 那么向安卓发送打印请求
                        //取号成功后十秒内没有操作自动返回首页
                        _this.timerBack();
                        numPicking = false;
                        if(data && flag){
                            var wait_num = isNaN(data.beforeWaitNumber+1)?data.beforeWaitNumber:(data.beforeWaitNumber+1)
                            var send_data = {
                                command:"CMD_STSRT_PRINTING",//打印
                                params:{
                                    businessName:newBusinessName,//所要办理的业务名称
                                    number:data.number,//取号的排号名
                                    waitNumber:data.waitNumber,//前方等待办理业务人数
                                    // beforeWaitNumber: data.beforeWaitNumber,//前方等待办理业务人数
                                    beforeWaitNumber: wait_num,//前方等待办理业务人数
                                    serviceName:data.serviceName,//服务中心的名称
                                    dateTime:data.dateTime,//取号时的时间
                                }
                            };
                            // todo("打纸",send_data)
                            androidApi.printPaper(send_data)
                        }
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        },

    }
})();

/**
 * 分页插件
 * @param obj.nextBtn 下一步按钮
 * @param obj.lastBtn 上一步按钮
 * @constructor
 */
var PagingPlugin = function(obj){
    this.nextBtn = obj.nextBtn;
    this.lastBtn = obj.lastBtn;
    this.reqUrl = obj.url || '';
    this.pageNum = 1; //页数
    this.pageSize = 10; //每页请求数量
    this.totalPage = 9999999999;
    this.callback = obj.callback;

    if(obj.sendData){
        //保存用户期望的请求数据
        this.sendData = obj.sendData;
        for(key in obj.sendData){
            this[key] = obj.sendData[key];
        }
    }
    // console.log("before add event")
    this.addEvent();
};
PagingPlugin.prototype = {
    addEvent:function(){
        var _this = this;
        $(document).on("click",this.nextBtn,function(){
            if(typeof _this.callback === 'function'){
                _this.jumpPage({
                    type:'next',
                    callback:function(data){
                        console.log("clicknext",data)
                        _this.callback(data);
                      localStorage.setItem("theData", JSON.stringify(data));
                    }
                });
            }else{
                _this.jumpPage({
                    type:'next'
                });
            }
        });
        $(document).on("click",this.lastBtn,function(){
            if(typeof _this.callback === 'function'){
                _this.jumpPage({
                    type:'last',
                    callback:function(data){
                        _this.callback(data);
                      localStorage.setItem("theData", JSON.stringify(data));
                    }
                });
            }else{
                _this.jumpPage({
                    type:'last',
                });
            }
        });

    },
    /**
     * 跳转页面
     * @param obj.type  {string} 类型 （'last'  'next'）
     * @param obj.pageNum {number}  跳转页数
     */
    jumpPage:function(obj){
        var _this = this;
        // console.log("jumppage___",obj)
        if(obj.type && obj.type.toUpperCase() === 'LAST'){
            if(this.pageNum > 1){
                this.pageNum -= 1;
            }
        }else if(obj.type && obj.type.toUpperCase() === 'NEXT'){
            if(this.pageNum<this.totalPage){
                this.pageNum += 1;
            }
        }


        // //获取最新的请求数据
        // for (key in this.sendData){
        //     this.sendData[key] = this[key];
        // }
        if(obj.sendData){
            // this.pageNum = obj.pageNum;
            for (val in obj.sendData){
                this[val] = obj.sendData[val];
            }
            // for (val in obj.sendData){
            //     this.sendData[val] = obj.sendData[val];
            // }
        }
        for (key in this.sendData){
            this.sendData[key] = this[key];
        }
        var urlData={
                url:this.reqUrl,
                isLoding:-1
            };
        ajaxdata(urlData,this.sendData,function(result,data){
            if(result){
                // _this.totalPage = data.resultList.total; //获取总页数
                if(typeof obj.callback === 'function'){
                    data.sendData = _this.sendData;
                    obj.callback(data)
                }
            }else{
                requesting = false
                layer.close(request_loading);
            }
        })
    }
};

// jumpPage();
//退出提示层
function logoOutMsg(title, fnSuccess, fnEnd) {
    var _title = title ? title : '您确定要退出吗？';
    var _str = '<div class="zlayer">\
                <div class="zlayer-title">\
                    <span class="zlayer-title-txt">温馨提示</span>\
                </div>\
                <div class="zlayer-content loginout-txt-box">\
                    <p class="loginout-txt-title">' + _title + '</p>\
                    <p class="loginout-txt-time"><span id="loginout_time" class="">5</span>秒后返回首页!</p>\
                </div>\
            </div>';
    zlayerIndex = layer.open({
        type: 1,
        shade: [0.7],
        title: false,
        area: [],
        resize: false,
        scrollbar: false,
        shadeClose: false, //点击遮罩关闭
        content: _str,
        success: function () {
            $('.zlayer .icon-close,.zlayer-btn-cancel').click(function () {
                layer.close(zlayerIndex);
            });
            $('.zlayer-btn-srue').click(function () {
                if (fnSuccess) {
                    fnSuccess();
                }
            });
            var oLoginTime = $('#loginout_time'),
                nEndTime = 5;
            setInterval(function () {
                if (nEndTime >= 1) {
                    oLoginTime.html(--nEndTime);
                } else {
                    window.location.href = zBaseUrl + "user/index.do";
                }
            }, 1000);

        }, end: function () {
            if (fnEnd) {
                fnEnd();
            }
        }
    })
}