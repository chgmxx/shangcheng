webpackJsonp([9],{"23Jn":function(t,e,a){"use strict";function o(t){a("NQLv")}var r={props:["statu","errorMsg"],data:function(){return{msg:"您还没有相关的订单"}},mounted:function(){null!=this.errorMsg&&(this.msg=this.errorMsg)},methods:{lockMall:function(){var t=this.$route.params.busId;this.$parent.getMemberCenter(t,1)}}},i=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"shop-main-no"},[a("div",{staticClass:"shop-no-content"},[1==t.statu?a("div",{staticClass:"no-order"},[t._m(0),t._v(" "),a("p",{staticClass:"fs52"},[t._v(t._s(t.msg))]),t._v(" "),a("a",{staticClass:"fs36",on:{click:t.lockMall}},[t._v(" 可以去逛逛哦~")])]):t._e(),t._v(" "),2==t.statu?a("div",{staticClass:"no-news"},[t._m(1),t._v(" "),a("a",{staticClass:"fs36"},[t._v(" 暂无相关信息")])]):t._e(),t._v(" "),3==t.statu?a("div",{staticClass:"no-shopcart"},[t._m(2),t._v(" "),a("p",{staticClass:"fs36 shopGray shop-textc"},[t._v("您的购物车还没有任何商品")]),t._v(" "),a("span",{staticClass:"no-button shopborder fs36",on:{click:t.lockMall}},[t._v("去逛逛")])]):t._e(),t._v(" "),4==t.statu?a("div",{staticClass:"no-shopcart"},[t._m(3),t._v(" "),a("a",{staticClass:"fs42",staticStyle:{color:"#333"}},[t._v(" "+t._s(t.errorMsg||"暂无相关信息"))])]):t._e()])])},s=[function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"shop-no-icon"},[a("i",{staticClass:"iconfont icon-order"})])},function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"shop-no-icon"},[a("i",[t._v("!")])])},function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"shop-no-icon"},[a("i",[t._v("!")])])},function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"shop-no-icon2"},[a("i",{staticClass:"icon-zanwudizhi iconfont"})])}],n={render:i,staticRenderFns:s},d=n,l=a("8AGX"),c=o,u=l(r,d,!1,c,"data-v-940b8da6",null);e.a=u.exports},"4jiG":function(t,e,a){"use strict";a("VCXJ").default.mixin({methods:{showConfirmDialogs:function(t){var e=this,a={btnNum:"2",dialogMsg:"您确定已经到货？",btnOne:"确定",btnTow:"关闭",dialogTitle:"确认收货提示",callback:{btnOne:function(){e.confirmReceipts(t)}}};e.$parent.$refs.dialog.showDialog(a)},confirmReceipts:function(t){var e=this;e.ajaxRequest({url:h5App.activeAPI.confirm_receipt_post,data:{busId:e.busId,url:location.href,browerType:e.$store.state.browerType,orderId:t},success:function(t){null!=e.orderList?(e.orderList=null,e.getOrderList({curPage:1})):null!=e.order&&null!=e.orderDetailList&&e.loadOrderDetail()}})},showDeleteOrderDialog:function(t){var e=this;e.$parent.$refs.dialog.showDialog({btnNum:"2",dialogMsg:Language.delete_order_tip_msg,btnOne:Language.confirm_msg,btnTow:Language.close_msg,dialogTitle:Language.delete_order_title_tip_msg,callback:{btnOne:function(){e.deloteOrders(t)}}})},deloteOrders:function(t){var e=this,a={browerType:e.$store.state.browerType,busId:e.busId,url:location.href,orderId:this.orderId};e.ajaxRequest({url:h5App.activeAPI.delete_order_post,data:a,success:function(t){history.go(-1)}})},showCloseReturnDialog:function(t){var e=this,a={btnNum:"2",dialogMsg:Language.return_order_tip_msg,btnOne:Language.confirm_msg,btnTow:Language.close_msg,dialogTitle:Language.close_return_title_msg,callback:{btnOne:function(){e.closeReturn(t)}}};e.$parent.$refs.dialog.showDialog(a)},closeReturn:function(t){var e=this,a={browerType:e.$store.state.browerType,busId:e.busId,url:location.href,returnId:t};e.ajaxRequest({url:h5App.activeAPI.close_return_post,data:a,success:function(t){null!=e.orderList?(e.orderList=null,e.getOrderList({curPage:1})):null!=e.order&&null!=e.orderDetailList&&e.loadOrderDetail()}})}}})},"9k5c":function(t,e,a){"use strict";var o=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("section",{staticClass:"more-main",class:{"margin-bottom-clear":!t.$store.state.isShowFooter}},[a("p",{directives:[{name:"show",rawName:"v-show",value:2==t.isMore,expression:"isMore == 2"}]},[t._v("加载中，请稍等……")]),t._v(" "),a("p",{directives:[{name:"show",rawName:"v-show",value:3==t.isMore,expression:"isMore == 3"}]},[t._v("抱歉,没有更多了")])])},r=[],i={render:o,staticRenderFns:r};e.a=i},"9wKw":function(t,e,a){var o=a("JrIC");"string"==typeof o&&(o=[[t.i,o,""]]),o.locals&&(t.exports=o.locals);a("8bSs")("247f03f1",o,!0)},IALW:function(t,e,a){var o=a("SPAC");"string"==typeof o&&(o=[[t.i,o,""]]),o.locals&&(t.exports=o.locals);a("8bSs")("9ffbd314",o,!0)},JrIC:function(t,e,a){e=t.exports=a("BkJT")(!1),e.push([t.i,".ik-box[data-v-717623a4]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-717623a4]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}.text-overflow[data-v-717623a4]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.imgbox[data-v-717623a4],.user-head-portrait[data-v-717623a4]{width:100%;height:100%;position:relative}.user-head-portrait[data-v-717623a4]{background-size:100%;background-repeat:no-repeat;background-position:50%;z-index:1}.default-img1[data-v-717623a4],.default-img2[data-v-717623a4]{width:100%;height:100%;position:absolute;top:0;left:0;z-index:0;line-height:1;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:center;-o-box-pack:center;box-pack:center;-webkit-box-align:center;-o-box-align:center;box-align:center}.default-img1 .iconfont[data-v-717623a4],.default-img2 .iconfont[data-v-717623a4]{font-size:1.33333333rem;color:#d6d6d6}.default-img2[data-v-717623a4]{background:#eee}.default-img2 .iconfont[data-v-717623a4]{font-size:1rem;color:#cecece}",""])},MH3W:function(t,e,a){e=t.exports=a("BkJT")(!1),e.push([t.i,'.ik-box[data-v-72f2a3ba]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-72f2a3ba]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}.text-overflow[data-v-72f2a3ba]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.order-content[data-v-72f2a3ba]{width:100%;background:#fff}.order-content .logistics-title[data-v-72f2a3ba]{padding:.33333333rem .23333333rem;line-height:1}.order-content .logistics-title .logistics-img[data-v-72f2a3ba]{width:.4rem;height:.4rem;border-radius:100%;margin-right:.13333333rem;border:1px solid #ededed}.order-content .logistics-list[data-v-72f2a3ba]{width:100%;padding-left:.54666667rem;padding-right:.2rem;line-height:1;overflow:hidden}.order-content .logistics-list .logistics-item[data-v-72f2a3ba]{padding:.26666667rem 0 .26666667rem .54666667rem;position:relative;height:1.56666667rem}.order-content .logistics-list .logistics-item .logistics-txt[data-v-72f2a3ba]{line-height:.35rem;margin-bottom:.1rem}.order-content .logistics-list .logistics-item em[data-v-72f2a3ba]{position:absolute;width:.16666667rem;height:.16666667rem;background:#ddd;top:.33333333rem;left:-.09333333rem;border-radius:100%}.order-content .logistics-list .logistics-item[data-v-72f2a3ba]:after{content:"";position:relative;display:inline-block;width:.02rem;height:200%;background:#ddd;left:-.57333333rem;top:-.8rem}.order-content .logistics-list>div[data-v-72f2a3ba]:first-child{color:#0bb453}.order-content .logistics-list>div:first-child em[data-v-72f2a3ba]{background:#0bb453;-webkit-box-shadow:0 0 0 2px rgba(11,180,83,.3);box-shadow:0 0 0 2px rgba(11,180,83,.3)}.order-main[data-v-72f2a3ba]{padding:.98666667rem 0}.padding-bottom-clear[data-v-72f2a3ba]{padding-bottom:0!important}.order-main2[data-v-72f2a3ba]{padding-top:0}.deltails-main[data-v-72f2a3ba],.order-main[data-v-72f2a3ba]{position:relative}.deltails-main .order-box[data-v-72f2a3ba],.order-main .order-box[data-v-72f2a3ba]{width:100%}.deltails-main .order-item[data-v-72f2a3ba],.order-main .order-item[data-v-72f2a3ba]{width:100%;margin-bottom:.13333333rem;background:#fff}.deltails-main .order-item-title[data-v-72f2a3ba],.order-main .order-item-title[data-v-72f2a3ba]{padding:0 .26666667rem;background:#fafafa;height:.9rem;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-align:center;-o-box-align:center;box-align:center}.deltails-main .order-item-title .order-title-img[data-v-72f2a3ba],.order-main .order-item-title .order-title-img[data-v-72f2a3ba]{width:.53333333rem;height:.53333333rem;border-radius:100%;border:1px solid #efefef;background-size:cover;overflow:hidden}.deltails-main .order-item-title span[data-v-72f2a3ba],.order-main .order-item-title span[data-v-72f2a3ba]{margin-left:.13333333rem;font-weight:700}.deltails-main .order-shop[data-v-72f2a3ba],.order-main .order-shop[data-v-72f2a3ba]{width:100%;height:.77333333rem;padding-left:.26666667rem;padding-right:.2rem;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-align:center;-o-box-align:center;box-align:center;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify;font-size:0}.deltails-main .order-shop-name span[data-v-72f2a3ba],.order-main .order-shop-name span[data-v-72f2a3ba]{margin:.2rem}.deltails-main .order-item-content[data-v-72f2a3ba],.order-main .order-item-content[data-v-72f2a3ba]{line-height:1;font-size:0}.deltails-main .order-item-content[data-v-72f2a3ba],.deltails-main .order-number-time[data-v-72f2a3ba],.order-main .order-item-content[data-v-72f2a3ba],.order-main .order-number-time[data-v-72f2a3ba]{display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify;width:100%;padding:.16rem .2rem .16rem .26666667rem}.deltails-main .order-item-content .order-item-img[data-v-72f2a3ba],.deltails-main .order-number-time .order-item-img[data-v-72f2a3ba],.order-main .order-item-content .order-item-img[data-v-72f2a3ba],.order-main .order-number-time .order-item-img[data-v-72f2a3ba]{width:1.76666667rem;height:1.76666667rem;background-size:cover}.deltails-main .order-item-content .order-item-txt[data-v-72f2a3ba],.deltails-main .order-number-time .order-item-txt[data-v-72f2a3ba],.order-main .order-item-content .order-item-txt[data-v-72f2a3ba],.order-main .order-number-time .order-item-txt[data-v-72f2a3ba]{width:73%}.deltails-main .order-item-content .order-item-txt p[data-v-72f2a3ba],.deltails-main .order-number-time .order-item-txt p[data-v-72f2a3ba],.order-main .order-item-content .order-item-txt p[data-v-72f2a3ba],.order-main .order-number-time .order-item-txt p[data-v-72f2a3ba]{margin-bottom:.13333333rem}.deltails-main .order-item-button[data-v-72f2a3ba],.deltails-main .order-item-total[data-v-72f2a3ba],.deltails-main .order-number-time[data-v-72f2a3ba],.order-main .order-item-button[data-v-72f2a3ba],.order-main .order-item-total[data-v-72f2a3ba],.order-main .order-number-time[data-v-72f2a3ba]{width:100%;padding:.25333333rem .2rem .25333333rem .26666667rem;text-align:right}.deltails-main .order-item-button .order-button[data-v-72f2a3ba],.deltails-main .order-item-total .order-button[data-v-72f2a3ba],.deltails-main .order-number-time .order-button[data-v-72f2a3ba],.order-main .order-item-button .order-button[data-v-72f2a3ba],.order-main .order-item-total .order-button[data-v-72f2a3ba],.order-main .order-number-time .order-button[data-v-72f2a3ba]{color:#fff;width:1.69333333rem;height:.58666667rem;line-height:.58666667rem;display:inline-block;border-radius:5px;text-align:center;margin-left:.13333333rem}.more-main[data-v-72f2a3ba]{padding-bottom:0}',""])},NQLv:function(t,e,a){var o=a("ar7S");"string"==typeof o&&(o=[[t.i,o,""]]),o.locals&&(t.exports=o.locals);a("8bSs")("bd79f474",o,!0)},R7Cr:function(t,e,a){var o=a("MH3W");"string"==typeof o&&(o=[[t.i,o,""]]),o.locals&&(t.exports=o.locals);a("8bSs")("755a317a",o,!0)},SPAC:function(t,e,a){e=t.exports=a("BkJT")(!1),e.push([t.i,".more-main{width:100%;font-size:.3rem;color:#737373;text-align:center;padding:.25rem 0;padding-bottom:60px}.more-main p{margin-bottom:.05rem;text-align:center}.margin-bottom-clear{margin-bottom:0!important}",""])},U2lE:function(t,e,a){var o=a("izCW");"string"==typeof o&&(o=[[t.i,o,""]]),o.locals&&(t.exports=o.locals);a("8bSs")("4bf8c310",o,!0)},YuAX:function(t,e){t.exports={props:["isMore"],data:function(){return{}}}},ar7S:function(t,e,a){e=t.exports=a("BkJT")(!1),e.push([t.i,".ik-box[data-v-940b8da6]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-940b8da6]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}.text-overflow[data-v-940b8da6]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.shop-main-no[data-v-940b8da6]{padding:2.65333333rem 0 1.66666667rem}.shop-main-no .shop-no-content[data-v-940b8da6]{width:100%;text-align:center;line-height:1;font-size:0}.shop-main-no .shop-no-content .shop-no-icon[data-v-940b8da6]{width:1.43333333rem;height:1.43333333rem;border-radius:100%;background:#d1d2d4;margin:0 auto;margin-bottom:.66666667rem;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-align:center;-o-box-align:center;box-align:center;-webkit-box-pack:center;-o-box-pack:center;box-pack:center}.shop-main-no .shop-no-content .shop-no-icon i[data-v-940b8da6]{color:#fff;font-size:1rem}.shop-main-no .shop-no-content .shop-no-icon2[data-v-940b8da6]{width:1.43333333rem;height:1.43333333rem;margin:0 auto;margin-bottom:.66666667rem;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-align:center;-o-box-align:center;box-align:center;-webkit-box-pack:center;-o-box-pack:center;box-pack:center}.shop-main-no .shop-no-content .shop-no-icon2 i[data-v-940b8da6]{color:#d1d2d4!important;font-size:1.18666667rem}.shop-main-no .shop-no-content p[data-v-940b8da6]{margin-bottom:.33333333rem}.shop-main-no .shop-no-content a[data-v-940b8da6]{color:#999}.shop-main-no .shop-no-content .no-button[data-v-940b8da6]{display:inline-block;padding:.13333333rem .2rem;border-radius:6px}.shop-main-no .shop-no-content .no-shopcart[data-v-940b8da6]{width:100%}.shop-main-no .shop-no-content .no-shopcart .shop-no-icon[data-v-940b8da6]{background:0;border:1px solid #7a7e83}.shop-main-no .shop-no-content .no-shopcart i[data-v-940b8da6]{color:#7a7e83;font-weight:100}",""])},fOec:function(t,e,a){"use strict";function o(t){a("U2lE")}var r={props:["headers","status","selectColor","selectbg"],data:function(){return{selectNav:"",selectFontColor:"shop-font"}},mounted:function(){this.selectNav=this.$route.params.type||0,this.selectFontColor=this.selectColor||"shop-font"},watch:{$route:function(){this.selectNav=this.$route.params.type||0}},methods:{selects:function(t){if(-1!=t){this.selectNav=t;var e=this.$route.params.busId,a=this.$route.params.shopId;return"order"===this.status?void this.$router.push("/order/list/"+e+"/"+t):"cart"===this.status?void this.$router.push("/cart/"+a+"/"+e+"/"+t):void this.onValue(t)}},onValue:function(t){this.$emit("change",t)}}},i=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"shop-header"},[a("div",{staticClass:"header-nav fs42"},t._l(t.headers,function(e){return a("div",{key:e.id,staticClass:"header-itme",class:[e.id==t.selectNav?t.selectFontColor:""],on:{click:function(a){t.selects(e.id)}}},[a("p",{domProps:{textContent:t._s(e.name)}}),t._v(" "),a("em",{class:[t.selectbg?t.selectbg:"shop-bg"]})])}))])},s=[],n={render:i,staticRenderFns:s},d=n,l=a("8AGX"),c=o,u=l(r,d,!1,c,"data-v-91ea153c",null);e.a=u.exports},g9Zl:function(t,e,a){"use strict";function o(t){a("R7Cr")}Object.defineProperty(e,"__esModule",{value:!0});var r=a("23Jn"),i=a("fOec"),s=a("a58N"),n=a("kr9m"),d=a("uotU"),l=(a("4jiG"),{name:"myorder",data:function(){return{homeNav:Language.order_nav_msg,isNavshow:"my",isShowNav:!1,statu:1,bondStatu:2,isShow:!1,isShowNullContent:!1,background:null,busId:this.$route.params.busId||sessionStorage.getItem("busId"),type:this.$route.params.type,curPage:0,pageCount:1,orderId:0,orderList:[],isMore:2,imgUrl:"",errorMsg:"",clickOrderId:""}},mounted:function(){var t=this;if(this.getOrderList({curPage:1}),$(window).bind("scroll",function(){$(window).scrollTop()>0&&$(window).scrollTop()>=$(document).height()-$(window).height()-1e3&&t.loadMore()}),t.setTitle(),null!=this.homeNav&&this.homeNav.length>0)for(var e=0;e<this.homeNav.length;e++){var a=this.homeNav[e];if(a.id==this.type){this.isShowNav=!0;break}}},watch:{$route:function(t,e){this.type=this.$route.params.type,this.getOrderList({curPage:1,type:this.type}),this.setTitle()}},components:{headerNav:i.a,contentNo:r.a,shopDialog:s.a,defaultImg:n.a,more:d.default},beforeDestroy:function(){this.isMore=-1},methods:{loadMore:function(){var t=this.pageCount;if(this.curPage>=t)return void(this.isMore=3);2!=this.isMore&&(this.curPage++,this.isMore=2,this.getOrderList({curPage:this.curPage}),this.setTitle())},getOrderList:function(t){var e=this,a={busId:e.busId,url:location.href,browerType:e.$store.state.browerType,type:t.type>=0?t.type:e.type,curPage:t.curPage>0?t.curPage:1};e.ajaxRequest({status:!1,url:h5App.activeAPI.order_list_post,data:a,success:function(t){if(0!=t.code)return e.errorMsg=t.msg,void(e.isShowNullContent=!0);var a=t.data;e.orderData=a;var o=a.orderResultList;e.curPage=a.curPage,e.pageCount=a.pageCount,e.imgUrl=t.imgUrl,o.forEach(function(t,a){t.orderMoney=e.commonFn.moneySplit(t.orderMoney),t.detailResultList.forEach(function(t,a){t.productPrice=e.commonFn.moneySplit(t.productPrice)})}),1===e.curPage?e.orderList=o:e.orderList=e.orderList.concat(o)||[],e.isShowNullContent=!1,e.isMore=1,e.curPage>=e.pageCount&&(e.isMore=3)}})},order_ulShow:function(){$(".orderTotal-ul").toggleClass("shop-hide"),$(".icon-up").toggleClass("shop-hide"),$(".icon-jiantou").toggleClass("shop-hide")},sure_dialog:function(t){this.showConfirmDialogs(t)},revoke_dialog:function(){},returnApplyReturn:function(t){sessionStorage.setItem("refundReturnUrl",location.href),this.$router.push("/return/classify/"+this.busId+"/"+t)},returnToComment:function(t,e){this.$router.push("/comment/"+e+"/"+t)},returnToPay:function(t){var e=(t.orderPayWay,t.orderId),a=t.busId;sessionStorage.setItem("payUrl",location.href),this.$router.push("/order/settlement/"+a+"/3/"+e)},returnDaifu:function(t,e){this.$router.push("/daifu/"+e+"/"+t)},returnOrderDetail:function(t){this.$router.push("/order/detail/"+this.busId+"/"+t)},setTitle:function(){var t=this;this.commonFn.setTitle(Language.order_title_data_msg[t.type].name)},jumpBus:function(t){this.$router.push("/stores/"+t.busId)},jumpShop:function(t){this.$parent.getPageId(t.busId,t.shopId,!0)}}}),c=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"shop-wrapper  order-wrapper",attrs:{id:"app"}},[null!=t.homeNav&&t.homeNav.length>0&&t.isShowNav?a("header-nav",{attrs:{headers:t.homeNav,status:"order"}}):t._e(),t._v(" "),t.isShowNullContent?a("content-no",{attrs:{statu:t.statu,errorMsg:t.errorMsg}}):t._e(),t._v(" "),t.isShowNullContent||null==t.orderList?t._e():a("section",{staticClass:"shop-main order-main",class:{"order-main2":!t.isShowNav,"padding-bottom-clear":!t.$store.state.isShowFooter}},[a("div",{staticClass:"order-box"},t._l(t.orderList,function(e,o){return a("div",{key:o,staticClass:"order-item"},[a("div",{staticClass:"order-item-title fs40",on:{click:function(a){t.jumpBus(e)}}},[a("div",{staticClass:"order-title-img"},[a("default-img",{attrs:{background:e.busImageUrl,isHeadPortrait:1}})],1),t._v(" "),a("span",[t._v(t._s(e.busName))])]),t._v(" "),a("div",{staticClass:"order-shop border",on:{click:function(a){t.jumpShop(e)}}},[a("p",{staticClass:"order-shop-name"},[a("i",{staticClass:"iconfont icon-dianpu"}),t._v(" "),a("span",{staticClass:"fs36"},[t._v(t._s(e.shopName))]),t._v(" "),a("i",{staticClass:"iconfont icon-you"})]),t._v(" "),a("p",{staticClass:"shop-font fs42"},[t._v("\r\n                        "+t._s(e.orderStatusName)+"\r\n                    ")])]),t._v(" "),t._l(e.detailResultList,function(o,r){return 5!=e.orderPayWay?a("div",{key:r,staticClass:"order-item-box "},[a("div",{staticClass:"order-item-content  border",on:{click:function(a){t.returnOrderDetail(e.orderId)}}},[a("div",{staticClass:"order-item-img"},[a("default-img",{attrs:{background:t.imgUrl+o.productImageUrl,isHeadPortrait:0}})],1),t._v(" "),a("div",{staticClass:"order-item-txt"},[a("p",{staticClass:"fs42"},[t._v(t._s(o.productName))]),t._v(" "),a("p",{staticClass:"fs42 shop-font"},[t._v("¥"+t._s(o.productPrice[0])+"."),a("span",{staticClass:"fs32"},[t._v(t._s(o.productPrice[1]))])]),t._v(" "),a("p",{staticClass:"fs36 shopGray"},[""!=o.productSpecificaValue?a("span",[t._v(t._s(o.productSpecificaValue)+"/")]):t._e(),t._v("\r\n                                "+t._s(o.productNum)+"件")])])]),t._v(" "),1==o.isShowApplyReturnButton||1==o.isShowCommentButton?a("div",{staticClass:"order-item-button fs42 border"},[1==o.isShowApplyReturnButton?a("div",{staticClass:"order-button shop-bg",on:{click:function(e){t.returnApplyReturn(o.orderDetailId)}}},[t._v("\r\n                            申请退款\r\n                        ")]):t._e(),t._v(" "),1==o.isShowCommentButton?a("div",{staticClass:"order-button shop-bg",on:{click:function(a){t.returnToComment(o.orderDetailId,e.busId)}}},[t._v("\r\n                            去评论\r\n                        ")]):t._e()]):t._e()]):t._e()}),t._v(" "),5==e.orderPayWay?a("div",{staticClass:"order-item-box"},[a("div",{staticClass:"order-item-content  border",staticStyle:{padding:"0.5rem"}},[a("span",{staticClass:"fs42"},[t._v("扫码支付")]),t._v(" "),a("span",{staticClass:"fs42",staticStyle:{"margin-left":"0.5rem"}},[t._v("订单金额："+t._s(e.orderMoney[0])+"."+t._s(e.orderMoney[1]))])])]):t._e(),t._v(" "),a("div",{staticClass:"order-number-time border"},[a("div",{staticClass:"order-number fs42"},[t._v("\r\n                        订单号："),a("span",[t._v(t._s(e.orderNo))])]),t._v(" "),a("div",{staticClass:"order-time fs42"},[t._v("\r\n                        下单时间："),a("span",[t._v(t._s(e.orderCreateTime))])])]),t._v(" "),a("div",{staticClass:"order-item-total border fs42"},[t._v("\r\n                    共计"+t._s(e.totalNum)+"件商品 合计：￥"+t._s(e.orderMoney[0])+"."+t._s(e.orderMoney[1])+"\r\n                ")]),t._v(" "),1==e.isShowGoPayButton||1==e.isShowReceiveGoodButton||1==e.isShowDaifuButton?a("div",{staticClass:"order-item-button fs42"},[1==e.isShowGoPayButton?a("div",{staticClass:"order-button shop-bg",on:{click:function(a){t.returnToPay(e)}}},[t._v("\r\n                        去支付\r\n                    ")]):t._e(),t._v(" "),1==e.isShowReceiveGoodButton?a("div",{staticClass:"order-button shop-bg",on:{click:function(a){t.sure_dialog(e.orderId)}}},[t._v("\r\n                        确定收货\r\n                    ")]):t._e(),t._v(" "),1==e.isShowDaifuButton?a("div",{staticClass:"order-button shop-bg",on:{click:function(a){t.returnDaifu(e.orderId,e.busId)}}},[t._v("\r\n                        代付详情\r\n                    ")]):t._e()]):t._e()],2)})),t._v(" "),a("more",{attrs:{"is-more":t.isMore}})],1),t._v(" "),t.isShow?a("section",{staticClass:"shop-main-no fs40 my-bond"},[a("content-no",{attrs:{statu:t.bondStatu}})],1):t._e(),t._v(" "),a("shop-dialog",{ref:"dialog"})],1)},u=[],m={render:c,staticRenderFns:u},b=m,p=a("8AGX"),h=o,f=p(l,b,!1,h,"data-v-72f2a3ba",null);e.default=f.exports},izCW:function(t,e,a){e=t.exports=a("BkJT")(!1),e.push([t.i,".ik-box[data-v-91ea153c]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-91ea153c]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}.text-overflow[data-v-91ea153c]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.shop-header[data-v-91ea153c]{top:0}.shop-header .header-nav[data-v-91ea153c]{height:.98666667rem}.shop-header .header-nav em[data-v-91ea153c]{height:.04rem;width:100%;position:absolute;bottom:0;display:none}.shop-header[data-v-91ea153c]{position:fixed;width:100%;background:#fff}.shop-header .header-nav .header-itme[data-v-91ea153c],.shop-header .header-nav[data-v-91ea153c]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.shop-header .header-nav .header-itme[data-v-91ea153c]{position:relative;-webkit-box-align:center;-o-box-align:center;box-align:center;-webkit-box-pack:center;-o-box-pack:center;box-pack:center;-webkit-box-flex:1;-o-box-flex:1;box-flex:1;-webkit-box-orient:vertical;-o-box-orient:vertical;box-orient:vertical}.shop-header .header-nav a[data-v-91ea153c]{display:block;text-align:center}.style-main-font em[data-v-91ea153c]{display:block!important}",""])},kr9m:function(t,e,a){"use strict";function o(t){a("9wKw")}var r={props:["background","isHeadPortrait","size"],data:function(){return{}},mounted:function(){}},i=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"imgbox"},[a("div",{staticClass:"user-head-portrait",style:{backgroundImage:"url("+t.background+")"}}),t._v(" "),0==t.isHeadPortrait?a("div",{staticClass:"default-img1"},[a("i",{staticClass:"iconfont icon-tupianjiazaizhong-",style:{"font-size":t.size}})]):t._e(),t._v(" "),1==t.isHeadPortrait?a("div",{staticClass:"default-img2"},[a("i",{staticClass:"iconfont icon-ren1",style:{"font-size":t.size}})]):t._e()])},s=[],n={render:i,staticRenderFns:s},d=n,l=a("8AGX"),c=o,u=l(r,d,!1,c,"data-v-717623a4",null);e.a=u.exports},uotU:function(t,e,a){"use strict";function o(t){a("IALW")}var r=a("YuAX"),i=a.n(r),s=a("9k5c"),n=a("8AGX"),d=o,l=n(i.a,s.a,!1,d,null,null);e.default=l.exports}});