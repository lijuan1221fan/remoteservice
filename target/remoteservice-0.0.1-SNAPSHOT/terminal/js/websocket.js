// 结果解析(JSON)
function resultFormat(r){
  if(r){
    var type = Object.prototype.toString.call(r);
    switch(type){
      case "[object String]":{
        try{
          var r2 =  JSON.parse(r)
          return resultFormat(r2)
        }catch(err){
          console.log(err)
          return r
        }
      }
      default:return r
    }
  }else{
    return r
  }
}

//websocket 构造函数
function CallWebSocket(url,opt){
  this.ws = null; // WebSocket实例
  this.wsUrl = null; // WebSocket地址
  this.device_states = {}; // 设备信息
  this.connInterval = null; // 设备信息轮询定时器
  var _this = this;

  // 心跳检测
  this.heartCheck = {
    timeout: 15000, // 毫秒
    // timeout: 3000, // 毫秒
    timeoutObj: null,
    serverTimeoutObj: null,
    timeoutCount:0,
    heartMessage:{
      type: 10,
      // serviceKey: "RS_ddd9392bb17b435988088211a431ad6c",
      // deviceCode: "9e7b0408b2e0a1d4",
      // pageSize: 10,
      // pageNum: 1,
      // state: 0
    },
    reset: function () {
      clearInterval(this.timeoutObj)
      clearTimeout(this.serverTimeoutObj)
      this.timeoutCount = 0
      return this
    },
    start: function (params) {
      this.heartMessage.serviceKey = params&&params.serviceKey||"";
      this.heartMessage.deviceCode = params&&params.deviceCode||"";
      // $("body").append("<h3>start"+ JSON.stringify(this.heartMessage) +"</h3>")
      var self = this;
      this.timeoutObj = setInterval(function () {
        // console.log(self.timeoutCount)
        self.timeoutCount++
        if(self.timeoutCount>2){//两次发送心跳失败关闭websocket
          _this.ws.close()
        }
        // 这里发送一个心跳，后端收到后，返回一个心跳消息，
        // onmessage拿到返回的心跳就说明连接正常
        _this.__sendHeartMessage(JSON.stringify(self.heartMessage))
      }, this.timeout)
    }
  }
  //websocket事件
  this.initEventHandle = function(){
    if(!this.ws){return}
    this.ws.onopen = function(){
      opt&&typeof opt.onopen == "function" && opt.onopen()
      // _this.heartCheck.start();//由于需要参数不在此处执行
    }
    this.ws.onmessage = function(e){
      var msg = resultFormat(e.data);
      // console.log(msg)
      //心跳信息处理
      if(msg&&msg.type === "10"){
        // $("body").append("<h3>"+'16message' +JSON.stringify(msg)+"</h3>")
        // var url = window.location.pathname
        // url = url.substring(url.lastIndexOf('/') + 1, url.length)
        // console.log(url)
        _this.heartCheck.timeoutCount = 0; //重置心跳计数
      }
      opt&&typeof opt.onmessage == "function" && opt.onmessage(msg)
    }
    this.ws.onclose = function(){
      _this.heartCheck.reset();
      opt&&typeof opt.onclose == "function" && opt.onclose();
    }
    this.ws.onerror = function(){
      opt&&typeof opt.onerror == "function" && opt.onerror()
    }
  }

  //新建 websocket
  this.createWebSocket = function(){
    try {
      if ('WebSocket' in window) {
        this.wsUrl = url;
        // this.wsUrl = 'ws://172.16.140.252:8080/remoteservice/h5SocketServer'
        // this.wsUrl = 'ws://172.16.140.150:8080/remoteservice/h5SocketServer';
        console.log(this.wsUrl)
        this.ws = new WebSocket(this.wsUrl)
        this.initEventHandle()
      } else {
        alert("浏览器不支持socket！")
      }
    } catch (e) {
      console.log(e)
      alert("socket start error!")
      // window.location.reload();
    }
  }
  //websocket信息发送方法
  this.sendMessage = function(agentData){
    var ad = agentData;
    // $("body").append("<h5 style='white-space:wrap;word-break:break-all;'>ttt_"+ws.toString()+"</h5>")
    if(!this.ws){
      setTimeout(function(){
        this.sendMessage(ad)
      },1000)
    }else{
      if(this.ws.readyState == this.ws.OPEN){
        this.ws.send(ad)
        ad = null;
      }else{
        setTimeout(function(){
          this.sendMessage(ad)
        },1000)
      }
    }
  }
  this.__sendHeartMessage=function(agentData){//用于心跳，不设置发送失败延时
    agentData = typeof agentData=="object"?JSON.stringify(agentData):agentData;
    if (this.ws&&this.ws.readyState === this.ws.OPEN) {
      this.ws.send(agentData||null);
    }
  }
  this.createWebSocket();
}