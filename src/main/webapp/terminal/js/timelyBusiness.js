//分页插件挂载(部门列表)
var timelyFun = (function () {
  var __funIndex = '3000';
  var __backTimer = null;
  var __backTimerDuration = 59;
  var __pagingPlugin; // 分页插件
  return {
    getPagingPlugin: function () {
      return __pagingPlugin
    },
    initTimer: function(){
      $("#departments-back-timer").text(__backTimerDuration)
      __backTimer = setInterval(function(){
        __backTimerDuration--;
        if(__backTimerDuration<0){
          clearInterval(__backTimer);
          __backTimer = null;
          pageJump("index")
        }else{
          $("#departments-back-timer").text(__backTimerDuration)
        }
      },1000)
    },
    stopTimer: function(){
      clearInterval(__backTimer);
      __backTimer = null;
      __backTimerDuration = 59;
    },
    /**
     * 分页插件
     */
    setPagingServicekey(key){
      __pagingPlugin.sendData.serviceKey = key;
    },
    pagingPlugin: function (dev_data) {
      // console.log("插件实例",dev_data)
      // 注册分页
      var _this = this;
      __pagingPlugin = new PagingPlugin({
        nextBtn: '#nextPage',
        lastBtn: '#lastPage',
        // url: '../api/getDeptList.do',
        url: '/remoteservice/api/getDeptList.do',
        sendData: {
          pageNum: 1,
          pageSize: 4,
          serviceKey: dev_data.serviceKey
        },
        callback: this.addDepartments
      })
      __pagingPlugin.jumpPage({
        callback: function (data) {
          localStorage.setItem('theData', JSON.stringify(data))
          _this.addDepartments(data); // 渲染页面
        }
      }); // 请求
    },
    //部门列表第一页
    resetPage(){
      var _this = this;
      __pagingPlugin.jumpPage({
        sendData:{
          pageNum:1,
        },
        callback: function (data) {
          // console.log(data)
          localStorage.setItem('theData', JSON.stringify(data))
          _this.addDepartments(data); // 渲染页面
        }
      });
    },
    /**
     * 渲染页面 添加部门
     * @param data
     */
    addDepartments: function (data) {
      // console.log("????",Math.ceil(data.resultList.total/4))
      // $("body").append("<h6>"+JSON.stringify(data)+"</h6>")
      var _this = this
      // __pagingPlugin.totalPage = data.resultList.pages
      __pagingPlugin.totalPage = Math.ceil(data.resultList.total/4)
      if (__pagingPlugin.pageNum === __pagingPlugin.totalPage) {
        $('#nextPage').parent().hide()
        $('#lastPage').parent().show()
      } else if (__pagingPlugin.pageNum === 1) {
        $('#lastPage').parent().hide()
        $('#nextPage').parent().show()
      } else {
        $('#lastPage').parent().show()
        $('#nextPage').parent().show()
      }
      var str = data.resultList.list && timelyFun.spliceDepartments(data.resultList.list.slice(0,4))
      $('#department_shell').html('').append(str)
      if (window.sessionStorage) {
        __funIndex = sessionStorage.getItem('funIndex', $(this).index()) ? sessionStorage.getItem('funIndex', $(this).index()) : '0'
      } else {
        return
      }
      if (__pagingPlugin.totalPage > 1) { // 总页数
        $('.department-page-btns').show()
      } else {
        $('.department-page-btns').hide()
      }
    },
    init: function () {
      // var funIndex = '3000'
      if (window.sessionStorage) {
        __funIndex = sessionStorage.getItem('funIndex', $(this).index()) ? sessionStorage.getItem('funIndex', $(this).index()) : '0'
      } else {
        return
      }

      $('.go-home').hide(); // 显示回首页按钮
      /**
       * 部门点击事件挂载
       */
      $(document).on("click",".items-list .items-wrap",function(){
        var timer
        if(requesting){return}
        $(this).css('-webkit-tap-highlight-color', 'rgba(255,0,0,0)')
        $(this).css('outline', 'none')
        localStorage.firstType = $(this).data('id') + ',' + $(this).data('name')
        localStorage.lastClick = $(this).data('id') + ',' + $(this).data('name')
        
        var _deptid= $(this).data('id')
        // console.log("_deptid",_deptid)
        requesting = true;
        timer = setTimeout(function(){
          requesting = false;
          layer.close(request_loading);
          timer&&clearTimeout(timer)
          timer=null;
        },2000)
        request_loading = lodingDom();
        pickFun.updateBusinessList({
          parentId:currentBackId,
          deptId:_deptid,
        })
        pageJump("picker")
        event.stopPropagation()
      })
    },
    /**
     * 根据数据拼接部门
     * @param dataList
     */
    spliceDepartments: function (dataList) {
      // console.log("部门拼接",dataList)
      if (dataList instanceof Array) {
        var aColor = ['#3B84F7', '#50D165', '#F36861', '#0FC9A8']
        var _html = ''
        for (var i = 0; i < dataList.length; i++) {
          var singleD = dataList[i]
          var sColor = aColor[i % aColor.length]

          _html += ['<div style="float:left;" class="items-wrap" data-id="' + singleD.id + '">',
            '                        <div class="list-box blue"  style="background: ' + sColor + ' url(img/bg.png) no-repeat 0 bottom/100% auto;">',
            '                            <h3>' + singleD.deptName + '</h3>',
            '                        </div>',
            '                        <div class="list-txt">',
            '                            当前排队<span>' + singleD.waitCount + '</span>人',
            '                        </div>',
            '                    </div>'
          ].join('')
        }
        return _html
      }
    }
  }
})()
/********************* socket初始化开始**********************/
socket_loading = lodingDom();
// websocket消息发送
function sendMessage(agentData){
  agentData = typeof agentData=="object"?JSON.stringify(agentData):agentData;
  if(!ws||!ws.ws){
    setTimeout(function(){
      sendMessage(agentData)
    },1000)
  }else{
    if(ws.ws.readyState == ws.ws.OPEN){
      ws.ws.send(agentData)
    }else{
      setTimeout(function(){
        sendMessage(agentData)
      },1000)
    }
  }
}
//初始化socket
function startSocket(){
  if(!deviceData.ip){return}
  ws = new CallWebSocket('ws://'+deviceData.ip+'/remoteservice/h5SocketServer',{
    onopen:function(){
      console.log("onopen",deviceData)
      socket_loading&&setTimeout(function(){//防止秒开无法关闭
        layer.close(socket_loading);
        socket_loading = null;
      },300);
      ws.ws.send(JSON.stringify({
        serviceKey: deviceData.serviceKey,
        deviceCode: deviceData.deviceCode,
        type: TYPE.DEVICESTATE,
      }))
      departMentParams.serviceKey = deviceData.serviceKey;
      departMentParams.deviceCode = deviceData.deviceCode;
      ws.ws.send(JSON.stringify(departMentParams))
      ws.heartCheck.start(deviceData)//启动心跳
    },
    onmessage:function(msg){
      has_started = false;
      // if(msg.type!=10){
      //   console.log("onmessage",msg)
      // }
      if(msg){
        switch(msg.type){
          case "6":{//一键检测
            // todo("要安卓设备状态")
            androidApi.loadDeviceState()
          }break;
          case "10":break;//心跳
          case "16":{//部门列表
            console.log(msg)
            if(msg&&msg.resultList&&Array.isArray(msg.resultList.list)){
              timelyFun.addDepartments(msg)
            }
          }break;
          case "17":{//更新排队号
            if(pagePos == "csIndex"){
              $(".items-wrap").each(function(){
                if($(this).data("id")== msg.deptId){
                  $(this).children(".list-txt").children("span").text(msg.waitNumber)
                }
              })
            }
          }break;
          case "18":{
            androidApi.actionIntentSettingAct('设备不存在');
            ws.onclose();
          }break
          default:{
            console.log("message_notype_",msg)
            bmData = msg;
            // 语音播报
            // if(Array.isArray(bmData)&&bmData.length){
            //   for(var i=0;i<bmData.length;i++){
            //     if(bmData[i].serviceKey==deviceData.serviceKey){
            //       // proceNumber 实际播号
            //       todo("播号",bmData[i].nextNumber)
            //       bmData[i].nextNumber&&androidApi.actionPlayProcessNum(bmData[i].nextNumber);//测试播号
            //       break;
            //     }
            //   }
            // }
            updateWaitList();
            setCenterInterval();
          }
        }
      }
    },
    onclose:function(){
      console.log("socket close")
      setTimeout(function(){//防socket报错闪烁
        startSocket()
      },2000)
    },
    onerror:function(){console.log("socket error")}
  })
}

/***************socket初始化结束***********************/

// 服务中心播号信息更新(数据更新)
function updateWaitList(index){
  if(!Array.isArray(bmData)||!bmData.length){return}
  index = index?index:0;
  $('#bmHeadName').text(bmData[index].serviceName)
  $('#bm').text(bmData[0].deptName + '等待人数：' + (bmData[index].waitCount||bmData[index].waitNumber||0))
  $('#bmDoneNumber').text(bmData[index].proceNumber || '空闲')
  $('#bmDoneName').text(bmData[index].processServiceName || '')
  $('#bmWaitName').text(bmData[index].nextNumber || '空闲')
  $('#bmWaitNum').text(bmData[index].nextServiceName || '')
}
//播报服务中心更新轮询
function setCenterInterval(){
  if(serviceCenterInterval){return;}
  var i = 0;
  serviceCenterInterval = setInterval(function () {
    // console.log("更新服务中心")
    if (i >= bmData.length - 1) {
      i = -1
    }
    i++
    updateWaitList(i)
  }, 4000)
}

/***
 * 页面事件部分
 */
//播号页跳转事件
function initClickFuns(){
  //pad模式直接跳转
  $("#drp").click(function(){
    timelyFun.resetPage();
    pageJump("csIndex")
    // console.log(pagePos)
  })
  //跳回播号
  $(".home-icon").click(function(){
    pageJump("index")
    // console.log(pagePos)
  })
}
//页面跳转时间
function pageJump(pos){
  //时钟清理
  if(pageJumping){return}
  pageJumping = true;
  switch(pagePos){
    case 'csIndex':{
      if(pos!="csIndex"){
        timelyFun.stopTimer()
      }
    }break;
    case "picker":{
      if(pos!="picker"){
        pickFun.stopTimer()
      }
    }break;
    default:;
  }
  //时钟设置
  switch(pos){
    case "csIndex":{
      timelyFun.initTimer()
    }break;
    case "picker":{
      pickFun.initTimer()
    }break;
    default:;
  }
  //页面跳转
  $("#"+pagePos).slideUp(animation_duration)
  $("#"+pos).slideDown(animation_duration)
  pagePos = pos;
  setTimeout(function(){
    pageJumping = false
  },animation_duration)
}
// 取号完成重置参数以及状态
function goHome(){
  interactive.clearTimerBackTimer();
  setTimeout(function(){ // 防止页面提前渲染
    $('.page2-content').show();
    $('.content-result').hide();
    $("#header_back").show();
    currentBackId = 0;
    $('.timer-back').show();
    $('.go-home').hide(); // 显示回首页按钮
    $('.content-result').hide();
    $('#countdown').html(10);
  },animation_duration)
  timelyFun.stopTimer();
  currentBackId = 0;
  cardInfo = null;
  pageJump("index")
}

// 页面加载完成
$(function () {
  /*****  webScoket 连接方法开始 */
  initClickFuns(); //事件挂载
  timelyFun.init(); //分页插件初始化
})
