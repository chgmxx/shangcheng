webpackJsonp([53],{CeXZ:function(t,e,a){"use strict";var s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("section",{staticClass:"shop-main fs40 my-add"},[a("div",{staticClass:"address-div-2",class:{rose_div:null!=t.roseColor}},[t._l(t.addressArr,function(e,s){return null!=t.addressArr&&t.addressArr.length>0?a("div",{key:s,staticClass:"shop-add-itme-1 fs36 border",attrs:{itmeid:"1"}},[a("div",{staticClass:"shop-add-txt clearfix shop-box-center"},[a("div",{staticClass:"i-add-left",on:{click:function(a){t.defaultAddress(e.memberId,e,s)}}},[a("i",{staticClass:"iconfont icon-yes fs52",class:[1==e.memberDefault?"shop-pink":"shop-gray"]})]),t._v(" "),a("div",{staticClass:"i-add-right fs36"},[a("div",{staticClass:"f_p "},[t._v(t._s(e.memberAddress))]),t._v(" "),a("div",{staticClass:"shop-box-center ad-bottom"},[a("div",[a("span",[t._v(t._s(e.memberName))]),a("span",[t._v(t._s(e.memberPhone))])]),t._v(" "),a("div",[a("span",{on:{click:function(a){t.editAddress(e.id)}}},[a("i",{staticClass:"iconfont icon-bianji"}),t._v("\n                         编辑\n                       ")])])])])])]):t._e()}),t._v(" "),a("div",{staticClass:"add-address-div",on:{click:function(e){t.editAddress(0)}}},[a("i",{staticClass:"iconfont icon-jiaimg fs52"}),a("span",{staticClass:"fs36"},[t._v("新增收货地址")])]),t._v(" "),t.isShow?a("section",{staticClass:"shop-main-no fs40 my-bond"},[a("content-no",{attrs:{statu:4,errorMsg:t.error}})],1):t._e(),t._v(" "),a("section",{staticClass:"bottom-div-fixed"},[a("div",{staticClass:"bottom-div clearfix"},[a("div",{staticClass:"address-add-button fs52",on:{click:t.submitIntegralData}},[t._v("\n               立即兑换\n             ")])])])],2)])},i=[],d={render:s,staticRenderFns:i};e.a=d},KDK2:function(t,e,a){"use strict";e.a={name:"myAddress",data:function(){return{busId:this.$route.params.busId||sessionStorage.getItem("busId"),integralId:this.$route.params.integralId||0,addressArr:[],isShowBottom:!1,isShow:!0,bondStatu:2,error:this.$t("address_null_error_msg"),integralObj:sessionStorage.getItem("integralData")||null,roseColor:null}},mounted:function(){this.loadDatas(),this.commonFn.setTitle(this.$t("title.title_my_address_msg")),this.$store.commit("show_footer",!1),this.commonFn.isNotNull(sessionStorage.getItem("integralData"))&&(this.integralObj=JSON.parse(sessionStorage.getItem("integralData")),this.roseColor="rose_cla")},beforeDestroy:function(){this.$store.commit("show_footer",!0)},methods:{loadDatas:function(){var t=this,e={busId:t.busId,url:location.href,browerType:t.$store.state.browerType};t.ajaxRequest({url:h5App.activeAPI.address_list_post,data:e,success:function(e){var a=e.data;if(null==a||0==a.length)return void(t.isShowBottom=!0);t.imgUrl=e.imgUrl,t.addressArr=a,a.length<5&&(t.isShowBottom=!0),t.isShow=!1,console.log(a,"myData")}})},editAddress:function(t){var e=this.busId,a="/address/edit/"+e+"/"+t;this.commonFn.isNotNull(this.integralId)&&(a+="/"+this.integralId),this.$router.push(a)},defaultAddress:function(t,e,a){var s=this,i={busId:s.busId,url:location.href,browerType:s.$store.state.browerType,addressId:e.id,upMemberId:t};s.ajaxRequest({url:h5App.activeAPI.default_address_post,data:i,success:function(t){var a=(sessionStorage.getItem("addressBeforeUrl"),s.$store.state.orderData);null!=a&&(a.memberAddressDTO=e),s.addressArr.forEach(function(t,a){t.id==e.id?t.memberDefault=1:t.memberDefault=0,s.$set(s.addressArr,a,t)})}})},submitIntegralData:function(){var t=this.integralObj;if(null!=t){for(var e=0,a=0;a<this.addressArr.length;a++){var s=this.addressArr[a];if(1==s.memberDefault){e=s.id;break}}if(0==e)return void this.$parent.$refs.bubble.show_tips("请选择您的收货地址");var i=this,d={busId:i.busId,url:location.href,browerType:i.$store.state.browerType,productId:t.productId,integralId:t.integralId,productNum:t.productNum,receiveId:e};null!=t.productSpecificas&&(d.productSpecificas=t.productSpecificas),i.ajaxRequest({url:h5App.activeAPI.record_integral_post,loading:!0,data:d,success:function(t){i.$store.commit("is_show_loading",!1),i.$router.push("/integral/record/"+i.busId)}})}}}}},Lca8:function(t,e,a){e=t.exports=a("BkJT")(!1),e.push([t.i,".ik-box[data-v-2a4e4e66]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-2a4e4e66]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}.text-overflow[data-v-2a4e4e66]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.shop-main[data-v-2a4e4e66]{background:#fff;min-height:100%}.shop-main .shop-add-itme-1[data-v-2a4e4e66]{height:1.66666667rem;padding:.33333333rem .12666667rem .36666667rem .50666667rem}.shop-main .shop-add-itme-1 .i-add-left[data-v-2a4e4e66]{width:4%;text-align:right}.shop-main .shop-add-itme-1 .i-add-left .shop-pink[data-v-2a4e4e66]{color:#f63854}.shop-main .shop-add-itme-1 .i-add-left .shop-gray[data-v-2a4e4e66]{color:#c3c3c3}.shop-main .shop-add-itme-1 .i-add-right[data-v-2a4e4e66]{width:90%}.shop-main .shop-add-itme-1 .i-add-right .f_p[data-v-2a4e4e66]{max-height:.44666667rem;width:100%;padding-bottom:.06666667rem;white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.shop-main .shop-add-itme-1 .i-add-right .ad-bottom div[data-v-2a4e4e66]:first-child{width:68%}.shop-main .shop-add-itme-1 .i-add-right .ad-bottom div:first-child span[data-v-2a4e4e66]:first-child{display:inline-block;margin-right:.26666667rem;max-width:50%;white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.shop-main .shop-add-itme-1 .i-add-right .ad-bottom div[data-v-2a4e4e66]:last-child{width:32%}.shop-main .shop-add-itme-1 .i-add-right .ad-bottom div:last-child span[data-v-2a4e4e66]:first-child{margin-right:.38666667rem}.shop-add-itme[data-v-2a4e4e66]{margin-bottom:.06666667rem}.address-div-2[data-v-2a4e4e66]{padding-bottom:2rem}.address-div-2 .bottom-div-fixed[data-v-2a4e4e66]{position:fixed;bottom:0;padding-bottom:1.06666667rem;width:100%;background:#fff}.address-div-2 .bottom-div-fixed .bottom-div[data-v-2a4e4e66]{width:100%;padding:0 .33333333rem}.address-div-2 .bottom-div-fixed .bottom-div .address-add-button[data-v-2a4e4e66]{width:100%;height:.97333333rem;line-height:.97333333rem;text-align:center;border-radius:5px}.address-div-2 .bottom-div-fixed .bottom-div .address-add-button .fs52[data-v-2a4e4e66]{font-size:.34666667rem;font-weight:700;margin-right:.13333333rem}.address-div-2 .add-address-div[data-v-2a4e4e66]{height:.88rem;line-height:.88rem;padding-left:.50666667rem}.address-div-2 .add-address-div i[data-v-2a4e4e66]{color:#c7c7cc;margin-right:.18rem;font-weight:bolder}.address-div-2 .add-address-div span[data-v-2a4e4e66]{color:#333}.rose_div .address-add-button[data-v-2a4e4e66]{background:#f63854;color:#fff}.rose_div .shop-font[data-v-2a4e4e66]{color:#f63855!important}",""])},kJ6s:function(t,e,a){var s=a("Lca8");"string"==typeof s&&(s=[[t.i,s,""]]),s.locals&&(t.exports=s.locals);a("8bSs")("9c0370ea",s,!0)},xUHs:function(t,e,a){"use strict";function s(t){a("kJ6s")}Object.defineProperty(e,"__esModule",{value:!0});var i=a("KDK2"),d=a("CeXZ"),o=a("0HdQ"),r=s,n=o(i.a,d.a,!1,r,"data-v-2a4e4e66",null);e.default=n.exports}});