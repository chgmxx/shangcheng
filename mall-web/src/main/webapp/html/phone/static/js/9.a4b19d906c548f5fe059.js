webpackJsonp([9],{"+7we":function(t,i,e){var a=e("NueU");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);e("8bSs")("7a025c7a",a,!0)},"/jM1":function(t,i,e){"use strict";var a=e("3cXf"),o=e.n(a),s=e("DhIC"),n=e("gbwz");i.a={name:"succeed",data:function(){return{background:null,busId:this.$route.params.busId||sessionStorage.getItem("busId"),productId:this.$route.params.productId,shopId:this.$route.params.shopId,imgUrl:"",mydata:null,product:null,productDetail:null,specificaList:null,integral:null,isMember:0,guige:null,guigePriceList:null,imageList:[{imageUrl:""}],recordNum:0,buyNum:1,guigePriceObj:null,selectGuigePrice:"",isDisabledButton:!1,disabledMsg:"",stockNum:0,flowPhone:"",isShowFlowPhone:!1,isShowSpec:!0}},components:{banner:s.a,dialogModular:n.a},mounted:function(){this.commonFn.setTitle("积分商品详情"),this.$store.commit("show_footer",!1),this.loadDatas(),this.detailsAjax()},beforeDestroy:function(){this.$store.commit("show_footer",!0)},methods:{loadDatas:function(){var t=this,i={busId:t.busId,url:location.href,browerType:t.$store.state.browerType,productId:t.productId,ucLogin:1};t.ajaxRequest({url:h5App.activeAPI.integral_product_post,data:i,success:function(i){var e=i.data;t.imgUrl=i.imgUrl,t.mydata=e,t.product=e.product,t.specificaList=e.specificaList,t.integral=e.integral,t.isMember=e.isMember,t.guige=e.guige,t.guigePriceList=e.guigePriceList,t.imageList=e.imageList,t.recordNum=e.recordNum,t.stockNum=e.proStockTotal,null!=t.specificaList&&(t.specificaList.forEach(function(t,i){var e=t.specValues;if(null!=e&&e.length>0)for(var a=0;a<e.length;a++){var o=e[a];if(0==a){o.select=!0;break}}}),t.guigePriceObj={},t.guigePriceList.forEach(function(i,e){t.guigePriceObj[i.xsid]=i}),t.getGuige(),t.isDisabledButtons()),console.log(e,"myData")}})},detailsAjax:function(){var t=this;t.ajaxRequest({url:h5App.activeAPI.phoneProduct_getProductDetail_post,data:{productId:t.productId},success:function(i){t.productDetail=i.data}})},selectSpecificaValue:function(t,i,e){var a=this,o=this.specificaList[i];if(null!=o){var s=o.specValues;s.forEach(function(t,i){t.select=!1,a.$set(s,i,t)}),s[e].select=!0,a.$set(s,e,s[e]),a.getGuige()}},getGuige:function(){var t=this,i=this.specificaList,e=[];if(null!=i)for(var a=0;a<i.length;a++){for(var o=i[a],s=0,n=0;n<o.specValues.length;n++){var d=o.specValues[n];if(d.select){s=d.specValueId;break}}s>0&&e.push(s)}null!=e&&e.length>0&&(this.selectGuigePrice=this.guigePriceObj[e.toString()],t.stockNum=1*this.selectGuigePrice.inv_num),this.buyNum>=this.stockNum&&(this.buyNum=this.stockNum)},jian:function(){1!=this.buyNum&&this.buyNum--},jia:function(){if(this.buyNum>=this.stockNum)return void(this.buyNum=this.stockNum);this.buyNum++},blurNum:function(){var t=/^[0-9]+$/,i=this.buyNum;return t.test(i)?i>=this.stockNum?void(this.buyNum=1*this.stockNum):void 0:(this.$store.commit("error_msg","请输入大于0的整数"),void(this.buyNum=1))},confirmPhone:function(){var t=this.flowPhone,i=this.commonFn;if((0,i.isNull)(t)||!i.validPhone(t))return void this.$store.commit("error_msg",this.$t("flow_phone_msg"));this.submitData()},isDisabledButtons:function(){var t=this.mydata,i=this.integral,e=this.commonFn.isNull;this.member;return 1==i.isDelete||0==i.isUse?(this.isDisabledButton=!0,this.disabledMsg="积分商品已失效或被删除",!1):1!=t.isMember?(this.isDisabledButton=!0,this.disabledMsg="您还不是会员，不能兑换商品",!1):1==t.isNoStart?(this.isDisabledButton=!0,this.disabledMsg="兑换时间还未开始，请耐心等待",!1):1==t.isEnd?(this.isDisabledButton=!0,this.disabledMsg="兑换时间已结束，请重新选择商品进行兑换",!1):e(t.memberId)?(this.isDisabledButton=!0,this.disabledMsg="还未登陆",!1):t.memberIntegral<=0&&t.memberIntegral<i.money?(this.isDisabledButton=!0,this.disabledMsg="您的积分不足，不能兑换该商品",!1):0==this.stockNum||this.stockNum<this.buyNum?(this.isDisabledButton=!0,this.disabledMsg="库存不够，请重新选择",!1):(this.isDisabledButton=!1,this.disabledMsg="",!0)},submitData:function(){var t=this,i=this.commonFn,e=i.isNull,a=t.$parent.$refs.bubble.show_tips;if(!t.isDisabledButtons())return""!=this.disabledMsg&&a(this.disabledMsg),!1;var s=this.integral.id,n={busId:t.busId,url:location.href,browerType:t.$store.state.browerType,productId:this.productId,integralId:s,productNum:this.buyNum};null!=this.selectGuigePrice&&(n.productSpecificas=this.selectGuigePrice.xsid);var d=this.product.proTypeId;if(4==d&&""!=this.flowPhone&&(n.flowPhone=this.flowPhone),4==d&&(e(n.flowPhone)||!i.validPhone(n.flowPhone)))return this.isShowFlowPhone=!0,!1;console.log("_data",n),t.ajaxRequest({url:h5App.activeAPI.record_integral_post,loading:!0,data:n,success:function(i){t.$store.commit("is_show_loading",!1),0==d?(sessionStorage.setItem("addressBeforeUrl",location.href),delete n.url,delete n.browerType,delete n.busId,sessionStorage.setItem("integralData",o()(n)),t.$router.push("/address/"+t.busId+"/"+s)):t.showDialog(d,i.data.url)}})},showDialog:function(t,i){this.mydata.memberId;console.log(t,"protypeId3");var e=this,a={btnNum:2,btnTow:"关闭",btnOne:3==t?"查看详情":"确定",dialogTitle:"兑换成功",dialogMsg:"恭喜获取"+this.product.proName,callback:{btnOne:function(){3==t?location.href=i:e.$router.push("/integral/record/"+e.busId)}}};e.$parent.$refs.dialog.showDialog(a)},back:function(){this.$router.push("/integral/index/"+this.busId)}}}},"3cXf":function(t,i,e){t.exports={default:e("I4CF"),__esModule:!0}},BI0A:function(t,i,e){"use strict";var a=function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("div",{staticClass:"goods-img-display",attrs:{id:"banner"}},[e("swipe",{attrs:{auto:t.swipeOptions.auto,speed:t.swipeOptions.speed,defaultIndex:t.swipeOptions.defaultIndex,continuous:t.swipeOptions.continuous,showIndicators:t.swipeOptions.showIndicators,prevent:t.swipeOptions.prevent,stopPropagation:t.swipeOptions.stopPropagation},on:{change:t.handleChange}},t._l(t.banner,function(i,a){return e("swipe-item",{key:i[a]},[e("div",{staticClass:"goods-img"},[""!=i.returnUrl?e("a",{attrs:{href:i.returnUrl}},[""!=i.imageUrl?e("img",{class:["cla-img"==t.imgCla?"cla-img":"img"],attrs:{src:t.imgUrl+i.imageUrl}}):t._e()]):""!=i.imageUrl?e("img",{class:["cla-img"==t.imgCla?"cla-img":"img"],attrs:{src:t.imgUrl+i.imageUrl}}):t._e(),t._v(" "),e("i",{staticClass:"iconfont icon-tupianjiazaizhong-"})])])})),t._v(" "),t.banner.length>1?e("p",{staticClass:"goods-origin-box"},t._l(t.banner,function(i,a){return e("i",{key:a,staticClass:"goods-origin ",class:[a==t.imgSelecte?t.style:""]})})):t._e()],1)},o=[],s={render:a,staticRenderFns:o};i.a=s},DhIC:function(t,i,e){"use strict";function a(t){e("+7we")}var o=e("Xr3i"),s=e("BI0A"),n=e("0HdQ"),d=a,r=n(o.a,s.a,!1,d,"data-v-181aa487",null);i.a=r.exports},HNT0:function(t,i,e){var a=e("wa31");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);e("8bSs")("0ca44af9",a,!0)},I4CF:function(t,i,e){var a=e("0nnt"),o=a.JSON||(a.JSON={stringify:JSON.stringify});t.exports=function(t){return o.stringify.apply(o,arguments)}},JNYj:function(t,i,e){var a=e("skZm");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);e("8bSs")("0e9ebb48",a,!0)},McX2:function(t,i,e){"use strict";var a=function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("transition",{attrs:{name:"dialog"}},[e("section",{directives:[{name:"show",rawName:"v-show",value:t.isShow,expression:"isShow"}],staticClass:"shop-dialog"},[e("div",{staticClass:"dialog-main"},[e("div",{staticClass:"dialog-content"},[e("p",{staticClass:"dialog-title fs50",domProps:{textContent:t._s(t.dialogTitle)}})]),t._v(" "),t._t("default")],2)])])},o=[],s={render:a,staticRenderFns:o};i.a=s},NueU:function(t,i,e){i=t.exports=e("BkJT")(!1),i.push([t.i,".goods-img-display[data-v-181aa487]{position:relative;width:100%;margin-bottom:0}.goods-img-display .goods-img[data-v-181aa487]{width:100%;height:100%;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:center;-o-box-pack:center;box-pack:center;-webkit-box-align:center;-o-box-align:center;box-align:center;position:relative}.goods-img-display .goods-img img[data-v-181aa487]{position:relative;margin:0 auto;display:block;max-width:100%;z-index:2}.goods-img-display .goods-img i.icon-tupianjiazaizhong-[data-v-181aa487]{position:absolute;z-index:1;top:50%;left:50%;margin-left:-1rem;margin-top:-1rem}.goods-img-display .goods-img .img[data-v-181aa487]{max-height:100%}.goods-img-display .goods-img .cla-img[data-v-181aa487]{-webkit-transform:translateY(50% 50%)!important;transform:translateY(50% 50%)!important;position:absolute;top:50%;left:50%;-webkit-transform:translate(-50%,-50%)!important;transform:translate(-50%,-50%)!important}.goods-img-display .goods-img .icon-tupianjiazaizhong-[data-v-181aa487]{font-size:2rem;color:#d6d6d6}.goods-img-display .goods-origin-box[data-v-181aa487]{width:100%;text-align:center;font-size:0;position:absolute;bottom:5%;z-index:5}.goods-img-display .goods-origin-box i[data-v-181aa487]{display:inline-block;width:8px;height:8px;background:#ccc;border-radius:100%;margin:0 3px}.goods-img-display .goods-origin-box .img-selecte[data-v-181aa487]{background:#e4393c;width:14px;border-radius:20px}.goods-img-display .goods-origin-box .style-main-bg[data-v-181aa487]{width:14px;border-radius:20px}",""])},VAOg:function(t,i,e){"use strict";i.a={props:["dialogTitle"],data:function(){return{isShow:!0}},methods:{}}},Xr3i:function(t,i,e){"use strict";var a=e("facN");e.n(a);i.a={components:{Swipe:a.Swipe,SwipeItem:a.SwipeItem},props:["banner","imgUrl","height","imgCla","colorStyle"],data:function(){return{imgSelecte:0,swipeOptions:{speed:300,auto:4e3,defaultIndex:0,continuous:!0,showIndicators:!1,prevent:!1,stopPropagation:!1},style:"img-selecte"}},methods:{handleChange:function(t){this.imgSelecte=t}},watch:{},computed:{},mounted:function(){var t="";t=null==this.height?$("body").width():this.height,$("#banner").css({height:t}),this.style=this.colorStyle||"img-selecte"}}},dNQT:function(t,i,e){"use strict";function a(t){e("HNT0")}Object.defineProperty(i,"__esModule",{value:!0});var o=e("/jM1"),s=e("yTvR"),n=e("0HdQ"),d=a,r=n(o.a,s.a,!1,d,"data-v-6f500b98",null);i.default=r.exports},gbwz:function(t,i,e){"use strict";function a(t){e("JNYj")}var o=e("VAOg"),s=e("McX2"),n=e("0HdQ"),d=a,r=n(o.a,s.a,!1,d,"data-v-315aa4a4",null);i.a=r.exports},skZm:function(t,i,e){i=t.exports=e("BkJT")(!1),i.push([t.i,".shop-dialog[data-v-315aa4a4]{position:fixed;width:100%;height:100%;top:0;left:0;z-index:99;background:rgba(0,0,0,.5);display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-align:center;-o-box-align:center;box-align:center;-webkit-box-pack:center;-o-box-pack:center;box-pack:center}.shop-dialog .dialog-main[data-v-315aa4a4]{width:90%;background:#fff;margin:0 auto;border-radius:5px;animation:dialogShow-data-v-315aa4a4 .25s;-moz-animation:dialogShow-data-v-315aa4a4 .25s;-webkit-animation:dialogShow-data-v-315aa4a4 .25s}.shop-dialog .dialog-main .dialog-content[data-v-315aa4a4]{text-align:center;padding:.4rem}.shop-dialog .dialog-main .dialog-bottom[data-v-315aa4a4]{width:100%;font-size:0;display:-webkit-box}.shop-dialog .dialog-main .dialog-bottom .dialog-button[data-v-315aa4a4]{text-align:center;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-flex:1;-o-box-flex:1;box-flex:1;-webkit-box-pack:center;-o-box-pack:center;box-pack:center;color:#25ae5f;padding:.3rem 0}.shop-dialog .dialog-main .dialog-bottom>span[data-v-315aa4a4]:nth-child(2){border-left:1px solid #e2e2e2}@keyframes dialogShow-data-v-315aa4a4{0%{-webkit-transform:scale(0);transform:scale(0)}to{-webkit-transform:scale(1);transform:scale(1)}}@-webkit-keyframes dialogShow-data-v-315aa4a4{0%{-webkit-transform:scale(0);transform:scale(0)}to{-webkit-transform:scale(1);transform:scale(1)}}",""])},wa31:function(t,i,e){i=t.exports=e("BkJT")(!1),i.push([t.i,'.section-content[data-v-6f500b98]{margin-bottom:1.6rem}.section-content .integral-banner[data-v-6f500b98]{width:100%;background:#fff}.section-content .icon-yuanq[data-v-6f500b98]{display:block;width:.46666667rem;height:.46666667rem;line-height:.46666667rem;border:0 solid #c9c9c9;color:#fff;background:#ababab;border-radius:100%;text-align:center;position:absolute;top:.18rem;left:.18rem;z-index:2}.section-content .integral-title[data-v-6f500b98]{width:100%;height:1.32rem;padding:0 .2rem;background:#fff;line-height:1}.section-content .integral-title .title-1[data-v-6f500b98]{font-size:.33333333rem;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;width:100%;padding:.26666667rem 0}.section-content .integral-title .title-2[data-v-6f500b98]{display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify}.section-content .integral-title .title-2 p[data-v-6f500b98]:first-child{color:#e4393c}.section-content .integral-title .title-2 p:first-child em[data-v-6f500b98]{display:block!important}.section-content .integral-title .title-2 p[data-v-6f500b98]{font-size:.26666667rem;color:#a9a9a9}.section-content .integral-title .title-2 p span[data-v-6f500b98]{color:#000}.section-content .guige-div[data-v-6f500b98]{background:#fff}.section-content .guige-div .guige-title[data-v-6f500b98]{padding:.4rem .2rem;position:relative;border-bottom:1px solid #e0e0e0;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify;-webkit-box-align:center;-o-box-align:center;box-align:center}.section-content .guige-div .guige-title p[data-v-6f500b98]{font-size:.28rem}.section-content .guige-div .guige-title p[data-v-6f500b98]:first-child{font-weight:bolder}.section-content .guige-div .guige-title .icon-jiantou[data-v-6f500b98]{color:#c7c7cc}.section-content .guige-div .guige-item2[data-v-6f500b98],.section-content .guige-div .guige-item[data-v-6f500b98]{width:100%;padding:.2rem;line-height:1;zoom:1;_zoom:1;clear:both}.section-content .guige-div .guige-item2[data-v-6f500b98]:after,.section-content .guige-div .guige-item[data-v-6f500b98]:after{clear:both;content:"";display:block;width:0;height:0;visibility:hidden}.section-content .guige-div .guige-item2 div[data-v-6f500b98],.section-content .guige-div .guige-item div[data-v-6f500b98]{float:left;font-size:0}.section-content .guige-div .guige-item2 div em.em-choice[data-v-6f500b98],.section-content .guige-div .guige-item2 div span[data-v-6f500b98],.section-content .guige-div .guige-item div em.em-choice[data-v-6f500b98],.section-content .guige-div .guige-item div span[data-v-6f500b98]{font-size:.26666667rem;display:inline-block;padding:.17333333rem .22666667rem;margin:0 .13333333rem}.section-content .guige-div .guige-item2 div span.nav[data-v-6f500b98],.section-content .guige-div .guige-item div span.nav[data-v-6f500b98]{background:#e4393c;color:#fff;border-radius:3px}.section-content .guige-div .guige-item2 .name-div[data-v-6f500b98],.section-content .guige-div .guige-item .name-div[data-v-6f500b98]{color:#87858f;width:20%;height:auto}.section-content .guige-div .guige-item2 .right-div[data-v-6f500b98],.section-content .guige-div .guige-item .right-div[data-v-6f500b98]{width:80%}.section-content .guige-div .guige-item2 .right-div em[data-v-6f500b98],.section-content .guige-div .guige-item .right-div em[data-v-6f500b98]{width:.66rem;color:#87858f;margin:0!important}.section-content .guige-div .guige-item2 .right-div input[data-v-6f500b98],.section-content .guige-div .guige-item .right-div input[data-v-6f500b98]{width:.8rem;border:0;margin:0 .02rem!important;vertical-align:top;padding:0}.section-content .guige-div .guige-item2 .right-div em[data-v-6f500b98],.section-content .guige-div .guige-item2 .right-div input[data-v-6f500b98],.section-content .guige-div .guige-item .right-div em[data-v-6f500b98],.section-content .guige-div .guige-item .right-div input[data-v-6f500b98]{height:.6rem;text-align:center;display:inline-block;background:#f3f2f8;border-radius:0}.section-content .guige-div .guige-item2[data-v-6f500b98]{padding-bottom:.26666667rem}.section-content .guige-div .guige-item2 .right-div[data-v-6f500b98]{float:right;margin-right:.26666667rem;width:31%}.section-content .integral-remark[data-v-6f500b98]{padding:.2rem;margin:.13333333rem 0;background:#fff}.section-content .integral-remark div[data-v-6f500b98]{font-size:.26666667rem;color:#a9a9a9}.section-content .integral-remark .title[data-v-6f500b98]{font-weight:bolder;color:#000;margin-bottom:.2rem}.section-content .integral-detail-image[data-v-6f500b98]{background:#fff}.section-content .integral-detail-image .title[data-v-6f500b98]{font-weight:bolder;color:#000;margin:0 0 .2rem .2rem;font-size:.26666667rem;height:.73333333rem;line-height:.73333333rem}.section-content .integral-detail-image .integral-detail[data-v-6f500b98]{overflow-x:auto;width:100%!important;font-size:.26666667rem}.section-content .integral-detail-image .integral-detail img[data-v-6f500b98]{width:100%!important;height:auto}.shop-footer-fixed[data-v-6f500b98]{width:100%;padding:.31333333rem .33333333rem;background:hsla(0,0%,80%,.68)}.shop-footer-fixed .bottom-bottom[data-v-6f500b98]{background:#f63854;font-size:.34666667rem;color:#fff;text-align:center;width:100%;height:.97333333rem;line-height:.97333333rem}.shop-footer-fixed .disbled[data-v-6f500b98]{background:#666!important}.dialog-input-main .dialog-input-box[data-v-6f500b98]{width:100%;font-size:0;padding:0 .53333333rem;height:1.43333333rem;position:relative;border-bottom:1px solid #e0e0e0}.dialog-input-main .dialog-input-box .dialog-input[data-v-6f500b98]{width:100%;padding:.16666667rem;margin-bottom:.13333333rem;border:1px solid #e1e1e3;border-radius:3px}.dialog-input-main .dialog-input-box .dialog-input input[data-v-6f500b98]{width:100%;height:100%;border:0}.dialog-input-main .dialog-input-box .dialog-code input[data-v-6f500b98]{width:68%}.dialog-input-main .dialog-input-box .dialog-code span[data-v-6f500b98]{padding:.16666667rem 0}.dialog-input-main .dialog-bottom[data-v-6f500b98]{width:100%;font-size:0;display:-webkit-box}.dialog-input-main .dialog-bottom .dialog-button[data-v-6f500b98]{text-align:center;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-flex:1;-o-box-flex:1;box-flex:1;-webkit-box-pack:center;-o-box-pack:center;box-pack:center;color:#25ae5f;padding:.3rem 0}.dialog-input-main .dialog-bottom>span[data-v-6f500b98]:first-child{color:#000}.dialog-input-main .dialog-bottom>span[data-v-6f500b98]:nth-child(2){border-left:1px solid #e2e2e2}',""])},yTvR:function(t,i,e){"use strict";var a=function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("div",{staticClass:"shop-wrapper integral-wrapper",attrs:{id:"app"}},[e("section",{staticClass:"section-content"},[e("div",{staticClass:"bg-back icon-yuanq iconfont icon-jiantou-copy1",on:{click:t.back}}),t._v(" "),null!=t.imageList&&t.imageList.length>0?e("div",{staticClass:"integral-banner"},[e("banner",{attrs:{banner:t.imageList,imgUrl:t.imgUrl}})],1):t._e(),t._v(" "),null!=t.product?e("div",{staticClass:"integral-title"},[e("div",{staticClass:"title-1"},[t._v(t._s(t.product.proName))]),t._v(" "),null!=t.integral?e("div",{staticClass:"title-2"},[e("p",[t._v(t._s(t.integral.money)+" 积分")]),t._v(" "),e("p",[e("span",[t._v(t._s(t.recordNum))]),t._v("人兑换")])]):t._e()]):t._e(),t._v(" "),e("div",{staticClass:"guige-div"},[e("div",{staticClass:"guige-title"},[e("p",[t._v("规格")]),t._v(" "),e("p",{staticClass:"iconfont icon-jiantou",on:{click:function(i){t.isShowSpec?t.isShowSpec=!1:t.isShowSpec=!0}}})]),t._v(" "),t._l(t.specificaList,function(i,a){return null!=t.specificaList?e("div",{key:a,staticClass:"guige-item"},[e("div",{staticClass:"name-div fs40"},[e("span",[t._v(t._s(i.specName))])]),t._v(" "),null!=i.specValues?e("div",{staticClass:"right-div"},t._l(i.specValues,function(i,o){return e("span",{key:o,class:{nav:i.select},on:{click:function(e){t.selectSpecificaValue(i.specValueId,a,o)}}},[t._v(t._s(i.specValue))])})):t._e()]):t._e()}),t._v(" "),e("div",{directives:[{name:"show",rawName:"v-show",value:t.isShowSpec,expression:"isShowSpec"}],staticClass:"guige-item2"},[t._m(0),t._v(" "),e("div",{staticClass:"right-div"},[e("em",{staticClass:"em-choice",on:{click:t.jian}},[t._v("-")]),t._v(" "),e("input",{directives:[{name:"model",rawName:"v-model",value:t.buyNum,expression:"buyNum"}],staticClass:"em-choice",attrs:{type:"text"},domProps:{value:t.buyNum},on:{blur:t.blurNum,input:function(i){i.target.composing||(t.buyNum=i.target.value)}}}),t._v(" "),e("em",{staticClass:"em-choice",on:{click:t.jia}},[t._v("+")])])])],2),t._v(" "),e("div",{staticClass:"integral-remark"},[e("div",{staticClass:"title"},[t._v("兑换说明")]),t._v(" "),e("div",[t._v("1、点击【立即兑换】，即可兑换成功；")]),t._v(" "),e("div",[t._v("2、在【兑换记录】可查询已兑换的物品；")]),t._v(" "),null!=t.integral?e("div",[t._v("3、兑换时间"+t._s(t.integral.startTime)+"至"+t._s(t.integral.endTime)+".")]):t._e()]),t._v(" "),null!=t.productDetail?e("div",{staticClass:"integral-detail-image"},[e("div",{staticClass:"title"},[t._v("商品详情")]),t._v(" "),e("div",{staticClass:"integral-detail",domProps:{innerHTML:t._s(t.productDetail)}})]):t._e()]),t._v(" "),e("section",{staticClass:"shop-footer-fixed"},[e("div",{staticClass:"bottom-bottom",class:{disbled:t.isDisabledButton},on:{click:t.submitData}},[t._v(t._s(""==t.disabledMsg?"立即兑换":t.disabledMsg))])]),t._v(" "),e("section",{directives:[{name:"show",rawName:"v-show",value:t.isShowFlowPhone,expression:"isShowFlowPhone"}]},[e("dialog-modular",{attrs:{dialogTitle:"流量充值"}},[e("div",{staticClass:"dialog-input-main"},[e("div",{staticClass:"dialog-input-box"},[e("div",{staticClass:"dialog-input"},[e("input",{directives:[{name:"model",rawName:"v-model",value:t.flowPhone,expression:"flowPhone"}],staticClass:"fs50",attrs:{placeholder:"请输入手机号码"},domProps:{value:t.flowPhone},on:{input:function(i){i.target.composing||(t.flowPhone=i.target.value)}}})])]),t._v(" "),e("div",{staticClass:"dialog-bottom"},[e("span",{staticClass:"fs50 dialog-button",on:{click:function(i){t.isShowFlowPhone=!1}}},[t._v("取消 ")]),t._v(" "),e("span",{staticClass:"fs50 dialog-button",on:{click:t.confirmPhone}},[t._v("确定")])])])])],1)])},o=[function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("div",{staticClass:"name-div fs40"},[e("span",[t._v("数量")])])}],s={render:a,staticRenderFns:o};i.a=s}});