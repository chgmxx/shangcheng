webpackJsonp([7],{274:function(t,n,s){s(645);var i=s(0)(s(432),s(776),null,null);t.exports=i.exports},340:function(t,n,s){"use strict";Object.defineProperty(n,"__esModule",{value:!0});var i=s(3),a=s(274),e=s.n(a);new i.default({render:function(t){return t(e.a)}}).$mount("#app")},432:function(t,n,s){"use strict";Object.defineProperty(n,"__esModule",{value:!0});s(2);n.default={components:{},data:function(){return{dataCount:{yesterday_orders_num:"",path:null,domain:""}}},methods:{link:function(t,n){console.log(n,t,"changeMenus(url)"),parent.window.postMessage("changeMenus('"+n+"','"+t+"')","*")},ajax:function(){var t=this;this.ajaxRequest({url:DFshop.activeAPI.mallCount_post,success:function(n){t.dataCount=n.data,t.domain=n.data.domain,t.yesterday_orders_num="¥ "+n.yesterday_orders_num}})}},mounted:function(){var t=this;this.ajax();var n=window.location.href;t.path=n.split("views")[0]}}},645:function(t,n){},776:function(t,n){t.exports={render:function(){var t=this,n=t.$createElement,s=t._self._c||n;return s("div",{staticClass:"survey-wrapper"},[s("div",{staticClass:"survey-item statistics-main"},[s("p",{staticClass:"item-title"},[t._v("统计概况")]),t._v(" "),s("div",{staticClass:"item-content"},[s("div",{staticClass:"col1"},[s("p",{staticStyle:{color:"#fa4c54"},domProps:{textContent:t._s(t.dataCount.unfilled_orders_num)}}),t._v(" "),s("div",[t._v("待发货订单")])]),t._v(" "),s("div",{staticClass:"col1"},[s("p",{domProps:{textContent:t._s(t.dataCount.bad_orders_num)}}),t._v(" "),s("div",[t._v("维权订单")])]),t._v(" "),s("div",{staticClass:"col1"},[s("p",{domProps:{textContent:t._s(t.dataCount.yesterday_orders_num)}}),t._v(" "),s("div",[t._v("昨日订单")])])])]),t._v(" "),s("div",{staticClass:"survey-item function-main"},[s("p",{staticClass:"item-title"},[t._v("常用功能")]),t._v(" "),s("div",{staticClass:"item-content"},[s("div",{staticClass:"col1"},[s("a",{on:{click:function(n){t.link("?url=releaseGoods/add",t.path+"views/goods/index.html#/mygoods")}}},[s("i",{staticClass:"iconfont icon-jia"}),t._v("发布商品\n        ")])]),t._v(" "),s("div",{staticClass:"col1"},[s("a",{on:{click:function(n){t.link("",t.path+"views/order/index.html#/allOrder")}}},[s("i",{staticClass:"iconfont icon-dingdan1"}),t._v("所有订单\n        ")])]),t._v(" "),s("div",{staticClass:"col1"},[s("a",{attrs:{target:"_blank",href:t.domain+"wxapplet/indexstart.do"}},[s("i",{staticClass:"iconfont icon-xiaochengxu1"}),t._v("小程序\n          ")])])]),t._v(" "),s("div",{staticClass:"item-content"},[s("div",{staticClass:"col1"},[s("a",{on:{click:function(n){t.link("?url=page",t.path+"views/shop/index.html#/shop")}}},[s("i",{staticClass:"iconfont icon-yemian"}),t._v("页面管理\n        ")])]),t._v(" "),s("div",{staticClass:"col1"},[s("a",{on:{click:function(n){t.link("?url=setup",t.path+"views/setUp/index.html#/danbao")}}},[s("i",{staticClass:"iconfont icon-shezhi1"}),t._v("通用设置\n        ")])]),t._v(" "),s("div",{staticClass:"col1"},[s("a",{on:{click:function(n){t.link("",t.path+"views/trade/index.html#/")}}},[s("i",{staticClass:"iconfont icon-zhankai"}),t._v("交易记录\n        ")])])])])])},staticRenderFns:[]}}},[340]);