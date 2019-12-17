/**
 *
 * @type {{}}
 */
var pickFun =  (function(){
    var __pagingPlugin;
    var __pickerTimer;
    return {
        getPagingPlugin:function(){
            return __pagingPlugin;
        },
        pagingPlugin:function(size,getData){//初始化插件
          getData = getData?getData:deviceData;//获取安卓的所有数据
        //   console.log("pagingPlugin",getData)
            //注册分页
            var _this = this;
            if(size && size === 9){ //一体机
                this.createStyle('css/imacStyle.css');
            }else if(size && size === 10){ //pad
                this.createStyle('css/pcStyle.css');
            }
            __pagingPlugin = new PagingPlugin({
                nextBtn:'#nextPageBusiness',
                lastBtn:'#lastPageBusiness',
                url:'/remoteservice/api/getBusinessClasses.do',
                sendData: {
                  pageNum: 1,
                  pageSize: size,
                  deptId: '',
                  parentId: currentBackId,
                  serviceKey: getData.serviceKey
                },
                // sendData:{
                //     pageNum:1,
                //     pageSize:size,
                //     deptId:window.localStorage.lastClick.split(',')[0],
                //     parentId:currentBackId
                // },
                callback:this.businessCallback
            });
            // __pagingPlugin.jumpPage({
            //     callback:function(data){
            //         // _this.businessCallback(data);//渲染页面
            //         _this.firstPlugin(data);
            //     }
            // });//请求
        },
        createStyle:function(url){
            var newLink = document.createElement('link');
            newLink.rel = "stylesheet";
            // <link rel="stylesheet" href="css/imacStyle.css?"/>
            newLink.href = url || 'css/imacStyle.css';
            $("head").append(newLink);
        },
        firstPlugin:function(data){
            console.log("firstPlugin",data)
            this.businessCallback(data);
        },
        updateBusinessList:function(params){
            var _this = this;
            __pagingPlugin.jumpPage({
                sendData:params,
                callback:function(data){
                    _this.businessCallback(data);//渲染页面
                    // _this.firstPlugin(data);
                    requesting = false;
                    layer.close(request_loading);
                }
            });//请求
        },
        businessCallback:function(data){
            var oBox = $('#item_box'),
                oBack = $('#header_back');
            // currentBackId = obj.parentId;
            __pagingPlugin.totalPage = data.resultList.pages;

            if(__pagingPlugin.pageNum === __pagingPlugin.totalPage){
                $('#nextPageBusiness').parent().hide();
                $('#lastPageBusiness').parent().show();

            }else if(__pagingPlugin.pageNum === 1){
                $('#lastPageBusiness').parent().hide();
                $('#nextPageBusiness').parent().show();
            } else {
              $('#lastPageBusiness').parent().show();
              $('#nextPageBusiness').parent().show();
            }
            __pagingPlugin.totalPage = data.resultList.pages;
            if(data.resultList.pages > 1){ //总页数
                $('.business-page-btns').show();
            }else{
                $('.business-page-btns').hide();
            }
            oBox.html(createItem(data.resultList.list)); //渲染列表
            oBack.data('id', data.sendData.parentId).data('deptId', data.sendData.deptId);
            currentBackId = data.sendData.parentId;
        },
        initTimer:function(){//执行倒计时
            commonFun.timerBack(document.getElementsByClassName("timer-back")[0],150);
        },
        stopTimer:function(){//关闭倒计时
            commonFun.stopTime()
        },
        initEvent:function(){
            // pickFun.pagingPlugin();
            var pagingPlugin = pickFun.getPagingPlugin();
            // console.log("pagingPlugin",pagingPlugin)
            // commonFun.fireAndroidInfo({command:'CMD_GET_SOME_MSG',params:{funType:5}},true);


            var oBox = $('#item_box');

            /**
             * 不同按钮的点击方法
             */
            oBox.on('click', '.item-list', function (event) {
                var _this = $(this);
                var _isleaf = _this.data('isleaf');
                var _deptid = _this.data('deptid');
                var _id = _this.data('id');
                //点击后先请求设备类型
                _this.css("-webkit-tap-highlight-color", "rgba(255,0,0,0)");
                _this.css("outline", "none");
                localStorage.lastClick = _this.data('id') + ',' + _this.data('name');

                if(_isleaf == 0){ //还有下一级
                    commonFun.setTimeFun(150);//重置倒计时 因为又下钻了一个页面
                    pagingPlugin.jumpPage({
                        sendData:{
                            deptId:_deptid,
                            parentId:_id,
                        },
                        callback:function(data){
                            // alert(JSON.stringify(data))
                            pickFun.businessCallback(data);
                        }
                    })
                }else{
                    console.log(_this)
                    if(numPicking){return}
                    numPicking = true;
                    interactive.clickhandle(_this,{
                        serviceKey: deviceData.serviceKey,
                    },deviceData.deviceType == "01") //取号操作
                    $('.timer-back').hide();
                    $('.go-home').show(); // 显示回首页按钮
                }
                event.stopPropagation();//停止冒泡
            });

            //添加回退事件
            $('#header_back').click(function () {
                var _this = $(this),
                    _deptId = _this.data('deptId') - 0,
                    _parentId = _this.data('id') - 0;
                // _id = window.localStorage.lastClick.split(',')[0];
                $(this).css("-webkit-tap-highlight-color", "rgba(255,0,0,0)");
                $(this).css("outline", "none");
        
                var flag = false;
                $('#item_box .item-list').each(function(){
                    var _isLeaf = $(this).data('isleaf')*1;
                    if(_isLeaf === 0){ //有下一页
                        flag = true;
                    }
                });
                if(currentBackId === 0){ //当前在类别页  返回到部门页
                    pageJump("csIndex")
                }else{  //在详情页  返回到类别页
                    currentBackId = 0;
                    pagingPlugin.jumpPage({
                        sendData:{
                            deptId:_deptId,
                            parentId:currentBackId,
                        },
                        callback:function(data){
                            pickFun.businessCallback(data);
                        }
                    })
                }
        
            });
        }
    }
})();

$(function () {
    //初始化
    // pickFun.pagingPlugin(9)
    // pickFun.initEvent();


    // // pickFun.pagingPlugin();
    // var pagingPlugin = pickFun.getPagingPlugin();
    // commonFun.fireAndroidInfo({command:'CMD_GET_SOME_MSG',params:{funType:5}},true);
    //
    // $('#header_back').click(function () {
    //
    //     var _this = $(this),
    //         _deptId = _this.data('deptId') - 0,
    //         _parentId = _this.data('id') - 0;
    //         // _id = window.localStorage.lastClick.split(',')[0];
    //     $(this).css("-webkit-tap-highlight-color", "rgba(255,0,0,0)");
    //     $(this).css("outline", "none");
    //
    //     var flag = false;
    //     $('#item_box .item-list').each(function(){
    //         var _isLeaf = $(this).data('isleaf')*1;
    //         if(_isLeaf === 0){ //有下一页
    //             flag = true;
    //         }
    //     });
    //     if(currentBackId === 0){ //当前在类别页  返回到部门页
    //         window.location.href = './timelyBusiness.html';
    //     }else{  //在详情页  返回到类别页
    //         currentBackId = 0;
    //         pagingPlugin.jumpPage({
    //             sendData:{
    //                 deptId:_deptId,
    //                 parentId:currentBackId,
    //             },
    //             callback:function(data){
    //                 pickFun.businessCallback(data);
    //             }
    //         })
    //     }
    //
    // });
    // //执行倒计时
    // commonFun.timerBack(document.getElementsByClassName("timer-back")[0],150);
    //
    // var oBox = $('#item_box');
    //
    // /**
    //  * 不同按钮的点击方法
    //  */
    // oBox.on('click', '.item-list', function (event) {
    //     var _this = $(this);
    //     var _isleaf = _this.data('isleaf');
    //     var _deptid = _this.data('deptid');
    //     var _id = _this.data('id');
    //     //点击后先请求设备类型
    //     _this.css("-webkit-tap-highlight-color", "rgba(255,0,0,0)");
    //     _this.css("outline", "none");
    //     localStorage.lastClick = _this.data('id') + ',' + _this.data('name');
    //
    //     if(_isleaf == 0){ //还有下一级
    //         commonFun.setTimeFun(150);//重置倒计时 因为又下钻了一个页面
    //         pagingPlugin.jumpPage({
    //             sendData:{
    //                 deptId:_deptid,
    //                 parentId:_id,
    //             },
    //             callback:function(data){
    //                 pickFun.businessCallback(data);
    //             }
    //         })
    //     }else{
    //         interactive.selectId = _this;//存起jq的dom对象
    //         //向安卓发送数据
    //         commonFun.fireAndroidInfo({command:'CMD_GET_SOME_MSG',params:{funType:3}},true);
    //     }
    //     event.stopPropagation();//停止冒泡
    // })
});


//渲染dom
function createItem(_data) {
    var _str = '',
        aColor = ['#F36861', '#4F8BBF', '#F98700', '#32B16C'];
    if (_data.length) {
        var sColor = '',
            sClass = '';
        for (var i = 0; i < _data.length; i++) {
            var singleData = _data[i];
            sColor = aColor[i % aColor.length];
            sClass = singleData.isLeaf === 0 ? 'has-link' : '';

            var deptName = singleData.describes;

            deptName = deptName.split('#');


            if(singleData.type !== "2006"){
                // _str += '<li class="item-list ' + sClass + '" data-isLeaf="'+singleData.isLeaf+'" data-deptId="'+singleData.deptId+'" data-id="' + singleData.id + '" data-name="' + singleData.describe + '" style="background: ' + sColor + '">\
                //        <h3 class="item-name">' + fnSplit(singleData.describes) + '</h3>\
                //     </li>';
                _str += '<li class="item-list ' + sClass + '" data-isLeaf="'+singleData.isLeaf+'" data-deptId="'+singleData.deptId+'" data-id="' + singleData.id + '" data-name="' + singleData.describe + '" style="background: ' + sColor + '">';

                for (var k = 0,len1= deptName.length;k<len1;k++){

                    _str += '<h3 class="item-name">'+ deptName[k] +'</h3>';
                }

                 _str +=   '</li>';
            }

        }
    }

    return _str;

    function fnSplit(sSplit) {
        var aArr = sSplit ? sSplit.split('#') : "",
            _str = '';
        for (var i = 0, _len = aArr.length; i < _len; i++) {
            if (i == _len - 1) {
                _str += aArr[i];
            } else {
                _str += aArr[i] + '<br>';
            }
        }
        return _str; 吗
    }
}

//获取办理的业务类型
function getClassName(type, id) {
    var arr = [];
    type = parseInt(type);
    id = parseInt(id);
    if (type == 1000) {
        arr.push('公安类');
        switch (id) {
            case 10011:
                arr.push('婚姻状况变更');
                break;
            case 10012:
                arr.push('文化程度变更');
                break;
            case 10013:
                arr.push('服务处所变更');
                break;
            case 1002:
                arr.push('居住证办理服务指南');
                break;
            case 1003:
                arr.push('居住房租出租登记办理服务');
                break;
            case 1004:
                arr.push('流动人口居住登记服务');
                break;
            default:
                arr.push('');
                break;
        }
    }
    if (type == 2000) {
        arr.push('社保类');
        switch (id) {
            case 20011:
                arr.push('基本养老保险关系转移');
                break;
            case 20012:
                arr.push('基本医疗保险关系转移');
                break;
            case 20013:
                arr.push('退休后医保一次性移交');
                break;
            case 20014:
                arr.push('被征地人员社会保障转职工养老保险');
                break;
            case 20021:
                arr.push('城乡居民基本养老保险补缴');
                break;
            case 20022:
                arr.push('城乡居民基本养老保险关系转移');
                break;
            case 20031:
                arr.push('省外异地就医住院直接结算备案');
                break;
            default:
                arr.push('');
                break;
        }
    }
    return arr;
}

