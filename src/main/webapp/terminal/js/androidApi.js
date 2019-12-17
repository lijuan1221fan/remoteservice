/**
 * 获取身份证：fun actionLoadInfo()
 * 关闭读卡：fun actionCloseRead()
 * 跳转到设置页面：fun actionIntentSettingAct(msg: String)
 * 更改服务中心名字 fun actionChangeServiceName(name: String)
 * 保存H5版本号码：fun actionSaveHTMLVersion(code: String)
 * 保存websoket:fun actionSaveSoketMsg(msg: String)
 * 语音播报：fun actionPlayProcessNum(processNum: String)
 * 显示对话框：fun actionShowLoading()
 * 关闭对话框：fun actionCloseLoading()
 * 获取读卡模块状态： fun loadCarderState()
 * 获取安卓配置信息: fun loadUserInfo()
 * 打纸：fun printPaper(String msg)
 * 获取设备信息：fun loadDeviceState()
 */
var isPC = false;//本地测试

var androidApi = function(){
  return {
    //获取身份证
    getCardInfo:function(){
      window.ycsb && window.ycsb.actionLoadInfo()
    },
    //打纸
    printPaper:function(msg){
      if(typeof msg == "object"){
        msg = JSON.stringify(msg)
      }
      window.ycsb && window.ycsb.printPaper(msg)
    },
    goSetPage:function(msg){
      window.ycsb && window.ycsb.actionIntentSettingAct(msg)
    },
    changeServiceName:function(name){
      window.ycsb && window.ycsb.actionChangeServiceName(name)
    },
    saveHversion:function(code){
      window.ycsb && window.ycsb.actionSaveHTMLVersion(code)
    },
    actionSaveSoketMsg:function(msg){
      window.ycsb && window.ycsb.actionSaveSoketMsg(code)
    },
    //语音播报
    actionPlayProcessNum:function(processNum){
      window.ycsb && window.ycsb.actionPlayProcessNum(processNum)
    },
    getCardState:function(){
      window.ycsb && window.ycsb.loadCarderState(processNum)
    },
    //获取安卓数据
    getAndroidOriginData:function(){
      if(isPC){
        // getAndroidSettingData({
        //   businessType:"01",
        //   deviceCode:"e8441c9b21baf7af",
        //   deviceType:"01",
        //   ip:"172.26.140.150.9090",
        //   serviceKey:"RS_4c575969018a4e1abc00738243fc7873",
        //   serviceName:"江干区",
        //   showType:"01"
        // })
        getAndroidSettingData({
          businessType:"01",
          deviceCode:"e8441c9b21baf799",
          deviceType:"01",
          ip:"172.16.140.150:9090",
          serviceKey:"RS_5a9532974004419a90f16393d1f18f64",
          serviceName:"请问",
          showType:"01"
        })
      }else{
        window.ycsb && window.ycsb.loadUserInfo()
      } 
    },
    //
    loadDeviceState:function(){
      if(isPC){
        loadCarderState({
          params:{
            cardReader:0,
            deviceCode:"e8441c9b21baf799",
            deviceMode:"PC",
            funType:7,
            printMode:0
          }
        })
        return;
      }
      window.ycsb && window.ycsb.loadDeviceState()
    },
    actionIntentSettingAct:function(strinig){
      window.ycsb && window.ycsb.actionIntentSettingAct(strinig)
    },
    //以下api提供安卓调用
    /**
    * 收到安卓数据时执行的操作
    * @param {
    * serviceName 服务中心名称
    * serviceKey 服务中心key
    * deviceCode 设备编码
    * ip 服务器IP
    * deviceType 设备类型 pad:"01"/一体机:"02"
    * businessType 是否为综办(弃用)
    * showType 不知
    * } data 
    */
    getAndroidSettingData:function(data){
      // if(isPC){
      //   todo("PC调用",data)
      // }else{
      //   todo("安卓调用",data)
      // }
      if(data){
        deviceData = resultFormat(data)
        startSocket()
        //显示区分pad和一体机
        switch(deviceData.deviceType){
          case "01":{
            setTimeout(function(){
              $('#hasId').hide()
              $('#drp').show()
            })
          }break;
          case "02":{
            setTimeout(function(){
              $('#drp').hide()
              $('#hasId').show()
            })
          }break;
          default:;
        }
        //插件实例化
        pickFun.pagingPlugin(9,deviceData);
        timelyFun.pagingPlugin(deviceData);
        pickFun.initEvent();
      }
      console.log("获取设备信息"+JSON.stringify(data))
    },
    /**
     * 安卓发送身份证时调用
     * @param {
     * params:身份证信息
     *  {
     *   identityNum:身份证号
     *  }
     * } data 
     */
    androidGiveIdCard:function(data){
      //注：console.log不能用","，否者安卓调用的时候直接报错
      console.log("deviceType:"+deviceData.deviceType+"__拿到身份证:"+JSON.stringify(data))
      if(!socket_loading && pagePos=="index"){
        cardInfo = resultFormat(data).params;
        // if(deviceData.deviceType=="01"){
          timelyFun.resetPage();
          pageJump("csIndex")
          console.log("翻页")
        // }else{console.log("没有翻页")}
      }else{
        console.log("没有操作")
      }
    },
    /**
     * 一键检测
     * @param {*} data 
     */
    loadCarderState:function(data){
      // todo("一键检测",data)
      var _deviceState = resultFormat(data).params;
      sendMessage({
        type:6,
        serviceKey:deviceData.serviceKey,
        cardReader: _deviceState.cardReader,
        deviceCode:_deviceState.deviceCode,
        printMode:_deviceState.printMode,
      })
      console.log("一键检测"+JSON.stringify(data))
    }
  }
}()
/**
 * 注册在window的方法(提供安卓调用)
 */
!function(){
  //安卓数据
  window['getAndroidSettingData'] = androidApi.getAndroidSettingData;
  //读取身份证
  window['androidGiveIdCard'] = androidApi.androidGiveIdCard;
  //一键检测
  window['loadCarderState'] = androidApi.loadCarderState;
}()

$(function(){
  // todo("getAndroidOriginData","start")
  androidApi.getAndroidOriginData();
  // todo("getAndroidOriginData","end")
})
