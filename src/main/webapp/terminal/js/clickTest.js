/**
 * 点击测试
 */
function ClickTest(){
  this.click_count=0;
  this.request_count=0;
  this.el = null;
  this.el_i = null;
  this.el_b = null;
  this.el_str = '<h4 id="click-test-btn"> \
              点击：<i></i><br> \
              请求：<b></b> \
            </h4>';
  //计数器更新
  this.updateCount = function(){
    this.el_i.text(this.click_count)
    this.el_b.text(this.request_count)
  };
  this.resetCount = function(){
    this.request_count = 0;
    this.click_count = 0;
    this.updateCount()
  }.bind(this)
  this.init = function(){
    $("body").append(this.el_str);
    this.el = $("#click-test-btn");
    this.el_i = $("#click-test-btn i");
    this.el_b = $("#click-test-btn b");
    //样式：
    this.el.css({
      width:"1rem",
      padding:"0.1rem",
      backgroundColor:"rgba(50,50,50,.5)",
      borderRadius:"50%",
      position:"fixed",
      top:"0.1rem",
      right:"0.5rem",
      color:"#fff",
      fontSize:"0.1rem",
      textAlign:"center"
    })
    this.el_i.css({
      color:"yellowgreen",
      fontSize:"0.12rem"
    })
    this.el_b.css({
      color:"yellowgreen",
      fontSize:"0.12rem"
    })
    this.updateCount();
    this.el.click(this.resetCount)
  }
  this.clickCount = function(){
    this.click_count++;
    this.updateCount()
  }
  this.requestCount = function(){
    this.request_count++;
    this.updateCount()
  }

  this.init();
}

var clickTest;
$(function(){
  clickTest = new ClickTest();
})