webpackJsonp([29],{"7n74":function(t,e,o){"use strict";function a(t){o("TA0g")}Object.defineProperty(e,"__esModule",{value:!0});var i=o("Bgf5"),d=o("RNrj"),r=o("0HdQ"),n=a,s=r(i.a,d.a,!1,n,"data-v-7d3f20e7",null);e.default=s.exports},Bgf5:function(t,e,o){"use strict";var a=o("kr9m"),i=o("DhIC");e.a={name:"succeed",data:function(){return{background:null,busId:this.$route.params.busId||sessionStorage.getItem("busId"),imgUrl:"",integralObj:null,integralList:null,imageList:[],memberId:0,memberIntegral:0,curPage:1,pageCount:1}},components:{defaultImg:a.a,banner:i.a},mounted:function(){this.commonFn.setTitle("积分商城"),this.$store.commit("show_footer",!1),this.loadDatasImage(),this.loadProduct(this.curPage);var t=this;$(window).bind("scroll",function(){$(window).scrollTop()>0&&$(window).scrollTop()>=$(document).height()-$(window).height()-1e3&&t.loadMore()})},beforeDestroy:function(){this.$store.commit("show_footer",!0)},methods:{loadMore:function(){var t=this.pageCount;if(this.curPage>=t)return void(this.isMore=3);2!=this.isMore&&(console.log(this.isMore,"this.isMore"),this.curPage++,this.isMore=2,this.loadProduct({curPage:this.curPage}))},loadDatasImage:function(){var t=this,e={busId:t.busId,url:location.href,browerType:t.$store.state.browerType,ucLogin:1};t.ajaxRequest({url:h5App.activeAPI.integral_image_post,data:e,success:function(e){var o=e.data;t.imgUrl=e.imgUrl,t.memberId=o.memberId,t.memberIntegral=o.memberIntegral,t.imageList=o.imageList,console.log(o,"myData")}})},loadProduct:function(t){var e=this,o={busId:e.busId,url:location.href,browerType:e.$store.state.browerType,ucLogin:1,curPage:t.curPage||this.curPage||1};e.ajaxRequest({url:h5App.activeAPI.integral_product_list_post,data:o,success:function(t){var o=t.data,a=o.page;if(null==a)return e.isMore=3,void $(window).unbind("scroll");e.imgUrl=t.imgUrl,e.curPage=a.curPage,e.pageCount=a.pageCount,1===e.curPage?e.integralList=a.subList:e.integralList=e.integralList.concat(a.subList)||[],console.log("_this.integralList",e.integralList),e.isMore=1,e.curPage>=e.pageCount&&(e.isMore=3,$(window).unbind("scroll"))}})},toIntegralDetail:function(){this.$router.push("/integral/detail/"+this.busId)},toIntegralRecord:function(){this.$router.push("/integral/record/"+this.busId)},toIntegralProduct:function(t){this.$router.push("/integral/product/"+t.busId+"/"+t.product_id+"/"+t.shop_id)}}}},DhIC:function(t,e,o){"use strict";function a(t){o("R3b7")}var i=o("Xr3i"),d=o("Y0kE"),r=o("0HdQ"),n=a,s=r(i.a,d.a,!1,n,"data-v-61addf18",null);e.a=s.exports},Huuc:function(t,e,o){"use strict";var a=function(){var t=this,e=t.$createElement,o=t._self._c||e;return o("div",{staticClass:"imgbox"},[o("div",{staticClass:"user-head-portrait",style:{backgroundImage:"url("+t.background+")"}}),t._v(" "),0==t.isHeadPortrait&&void 0==t.background?o("div",{staticClass:"default-img"},[o("i",{staticClass:"iconfont icon-tupianjiazaizhong-",staticStyle:{color:"#d6d6d6"}})]):t._e(),t._v(" "),1==t.isHeadPortrait&&void 0==t.background?o("div",{staticClass:"default-img"},[o("i",{staticClass:"iconfont icon-ren1"})]):t._e()])},i=[],d={render:a,staticRenderFns:i};e.a=d},R3b7:function(t,e,o){var a=o("v+vZ");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);o("8bSs")("0d7b25be",a,!0)},RNrj:function(t,e,o){"use strict";var a=function(){var t=this,e=t.$createElement,o=t._self._c||e;return o("div",{staticClass:"shop-wrapper integral-wrapper",attrs:{id:"app"}},[o("div",{staticClass:"integral-top"},[o("div",{staticClass:"bg-div"},[o("p",{staticClass:"fs40"},[t._v("我的积分")]),t._v(" "),0==t.memberId?o("p",{staticClass:"fs70"},[t._v("还未登陆，请先登陆")]):t._e(),t._v(" "),t.memberId>0?o("p",[t._v(t._s(t.memberIntegral))]):t._e()]),t._v(" "),o("div",{staticClass:"bg-color"}),t._v(" "),o("div",{staticClass:"bg-tab"},[o("div",{on:{click:function(e){t.toIntegralRecord()}}},[o("i",{staticClass:"icon-event iconfont"}),t._v("兑换记录")]),t._v(" "),o("div",{on:{click:function(e){t.toIntegralDetail()}}},[o("i",{staticClass:"icon-asset iconfont"}),t._v("积分明细")])])]),t._v(" "),null!=t.imageList&&t.imageList.length>0?o("div",{staticClass:"integral-middle"},[o("banner",{attrs:{banner:t.imageList,imgUrl:t.imgUrl,height:"1.77rem",imgCla:"cla-img"}})],1):t._e(),t._v(" "),null!=t.integralList?o("div",{staticClass:"integral-product"},t._l(t.integralList,function(e,a){return o("div",{key:a,staticClass:"product-item",on:{click:function(o){t.toIntegralProduct(e)}}},[o("div",{staticClass:"product-content"},[o("div",{staticClass:"product-img"},[o("default-img",{attrs:{background:t.imgUrl+e.image_url,isHeadPortrait:0}})],1),t._v(" "),o("div",{staticClass:"content-div"},[o("p",{staticClass:"product-title"},[t._v(t._s(e.pro_name))]),t._v(" "),o("p",{staticClass:"div-text"},[t._v("微会员积分兑换")]),t._v(" "),o("p",{staticClass:"shop-font"},[t._v(t._s(e.money)+"积分")])])]),t._v(" "),o("div",{staticClass:"div-icon iconfont icon-you"})])})):t._e()])},i=[],d={render:a,staticRenderFns:i};e.a=d},TA0g:function(t,e,o){var a=o("ke++");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);o("8bSs")("31795142",a,!0)},Xr3i:function(t,e,o){"use strict";var a=o("facN"),i=(o.n(a),o("UhSw"));o.n(i);e.a={components:{Swipe:a.Swipe,SwipeItem:a.SwipeItem},props:["banner","imgUrl","height","imgCla","colorStyle"],data:function(){return{imgSelecte:0,swipeOptions:{speed:300,auto:4e3,defaultIndex:0,continuous:!0,showIndicators:!1,prevent:!1,stopPropagation:!1},style:"img-selecte"}},methods:{handleChange:function(t){this.imgSelecte=t}},watch:{},computed:{},mounted:function(){var t="";t=null==this.height?$("body").width():this.height,$("#banner").css({height:t}),this.style=this.colorStyle||"img-selecte"}}},Y0kE:function(t,e,o){"use strict";var a=function(){var t=this,e=t.$createElement,o=t._self._c||e;return o("div",{staticClass:"goods-img-display",attrs:{id:"banner"}},[o("swipe",{attrs:{auto:t.swipeOptions.auto,speed:t.swipeOptions.speed,defaultIndex:t.swipeOptions.defaultIndex,continuous:t.swipeOptions.continuous,showIndicators:t.swipeOptions.showIndicators,prevent:t.swipeOptions.prevent,stopPropagation:t.swipeOptions.stopPropagation},on:{change:t.handleChange}},t._l(t.banner,function(e,a){return o("swipe-item",{key:e[a]},[o("div",{staticClass:"goods-img"},[""!=e.imageUrl?o("img",{class:["cla-img"==t.imgCla?"cla-img":"img"],attrs:{src:t.imgUrl+e.imageUrl}}):o("div",[o("i",{staticClass:"iconfont icon-tupianjiazaizhong-"})])])])})),t._v(" "),t.banner.length>1?o("p",{staticClass:"goods-origin-box"},t._l(t.banner,function(e,a){return o("i",{key:a,staticClass:"goods-origin ",class:[a==t.imgSelecte?t.style:""]})})):t._e()],1)},i=[],d={render:a,staticRenderFns:i};e.a=d},"ke++":function(t,e,o){e=t.exports=o("BkJT")(!1),e.push([t.i,".ik-box[data-v-7d3f20e7]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-7d3f20e7]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}body[data-v-7d3f20e7]{background:#f0f2f5;color:#333}.shop-wrapper[data-v-7d3f20e7]{width:100%;position:relative;max-width:1242px;margin:0 auto}.shop-main[data-v-7d3f20e7]{width:100%}.shop-main .shop-list[data-v-7d3f20e7]{padding:.26666667rem 0}.shop-main .shop-add-itme[data-v-7d3f20e7]{width:100%;background:#fff;padding-top:.33333333rem;padding-left:.46666667rem;height:2.46666667rem}.shop-main .shop-add-itme .shop-add-txt[data-v-7d3f20e7]{padding-bottom:.13333333rem}.shop-main .shop-add-itme .add-left[data-v-7d3f20e7]{float:left;width:93%}.shop-main .shop-add-itme .add-left p[data-v-7d3f20e7]{margin-bottom:.06666667rem}.shop-main .shop-add-itme .add-left p span[data-v-7d3f20e7]{margin-left:.26666667rem}.shop-main .shop-add-itme .add-right[data-v-7d3f20e7]{float:left;width:7%;color:#d1d2d4;height:100%;position:relative}.shop-main .shop-add-itme .add-right i[data-v-7d3f20e7]{font-size:.32rem;position:absolute;top:.33333333rem}.shop-main .shop-add-itme .shop-add-footer[data-v-7d3f20e7]{width:96%;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-align:center;-o-box-align:center;box-align:center;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify}.shop-main .shop-add-itme .shop-add-footer .shop-add-button1[data-v-7d3f20e7]{padding:.18666667rem 0}.shop-main .shop-add-itme .shop-add-footer .shop-add-button1 i[data-v-7d3f20e7]{width:.35333333rem;height:.35333333rem;background:#d1d2d4;color:#fff;vertical-align:0}.shop-main .shop-add-itme .shop-add-footer .shop-add-button2 span[data-v-7d3f20e7]{display:inline-block;padding:.18666667rem 0}.shop-main .shop-add-itme .shop-add-footer .shop-add-button2 i[data-v-7d3f20e7]{margin-right:.06666667rem;font-size:.32rem;color:#d1d2d4}.em-tag[data-v-7d3f20e7]{background:#e4393c;margin-right:.06666667rem}.em-flag[data-v-7d3f20e7],.em-tag[data-v-7d3f20e7]{color:#fff;padding:3px 5px 2px;line-height:1;display:inline-block;border-radius:2px;font-size:.18666667rem}.em-flag[data-v-7d3f20e7]{background:-moz-linear-gradient(right,#f85e65,#e7242c)}.em-choice[data-v-7d3f20e7],.em-input[data-v-7d3f20e7],.em-search[data-v-7d3f20e7]{color:#333;padding:.17333333rem .25333333rem;line-height:1;margin-right:.24rem;display:inline-block;border-radius:2px;font-size:.18666667rem;background:#f3f2f8;border-radius:3px}.em-input[data-v-7d3f20e7]{width:.8rem;vertical-align:bottom;height:.6rem;border:0;margin-right:3px;padding:2px;text-align:center}.em-search[data-v-7d3f20e7]{margin-bottom:.18666667rem;background:#d7d9dc}.em-nav[data-v-7d3f20e7]{background:#fbd3d3;padding:.13333333rem .2rem;border-radius:3px}.shop-max-button[data-v-7d3f20e7]{width:100%;height:100%;border-radius:5px;color:hsla(0,0%,100%,.3);display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-align:center;-o-box-align:center;box-align:center;-webkit-box-pack:center;-o-box-pack:center;box-pack:center}.shop-footer-ab[data-v-7d3f20e7]{position:absolute}.shop-footer-ab[data-v-7d3f20e7],.shop-footer[data-v-7d3f20e7]{width:100%;bottom:0}.shop-footer-ab .shop-logo[data-v-7d3f20e7],.shop-footer .shop-logo[data-v-7d3f20e7]{margin:0 auto;width:3rem;height:.46666667rem;background:url("+o("DDBx")+');background-size:100%;margin-bottom:.24rem}.shop-footer-fixed[data-v-7d3f20e7]{position:fixed;bottom:0;left:0}.shop-footer-fixed .footer-nav[data-v-7d3f20e7]{border-top:1px solid #e2e2e2;height:1.05333333rem}.shop-footer-fixed .footer-nav i[data-v-7d3f20e7]{font-size:.4rem;margin-bottom:.06666667rem}.shop-header[data-v-7d3f20e7]{top:0;background:#fff;z-index:2}.shop-header .header-nav[data-v-7d3f20e7]{height:.98666667rem}.shop-header .header-nav em[data-v-7d3f20e7]{height:.04rem;width:100%;position:absolute;bottom:0;display:none}.shop-fl[data-v-7d3f20e7]{float:left}.shop-fr[data-v-7d3f20e7]{float:right}.shop-hide[data-v-7d3f20e7]{display:none}.shop-show[data-v-7d3f20e7]{display:block}.shop-box[data-v-7d3f20e7]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.shop-inblock[data-v-7d3f20e7]{display:inline-block}.shop-box-center[data-v-7d3f20e7],.shop-box-justify[data-v-7d3f20e7]{display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify}.shop-box-center[data-v-7d3f20e7]{-webkit-box-align:center;-o-box-align:center;box-align:center}.clearfix[data-v-7d3f20e7]{zoom:1;_zoom:1;clear:both}.clearfix[data-v-7d3f20e7]:after{clear:both;content:"";display:block;width:0;height:0;visibility:hidden}.overflow-x[data-v-7d3f20e7]{overflow:hidden;overflow-x:hidden}.shop-textl[data-v-7d3f20e7]{text-align:left}.shop-textr[data-v-7d3f20e7]{text-align:right}.shop-textc[data-v-7d3f20e7]{text-align:center}.text-overflow[data-v-7d3f20e7]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.text-force-wrap[data-v-7d3f20e7]{word-break:break-all;white-space:pre-wrap}.text-not-wrap[data-v-7d3f20e7]{white-space:nowrap}.text-more-overflow[data-v-7d3f20e7]{overflow:hidden;position:relative;text-align:justify}.text-more-overflow[data-v-7d3f20e7]:after{content:" ... ";position:absolute;bottom:1px;right:0;padding:0 1px 1px 2px;background:#fff}.border-radius[data-v-7d3f20e7],.border[data-v-7d3f20e7]{position:relative;border-bottom:1px solid #e0e0e0}@media (-webkit-min-device-pixel-ratio:2){.border[data-v-7d3f20e7]{border:none;background-image:-webkit-gradient(linear,left top,left bottom,from(0),color-stop(#fff),color-stop(50%,#d9d9d9),color-stop(50%,transparent));background-image:linear-gradient(0,#fff,#d9d9d9 50%,transparent 0);background-size:100% 1px;background-repeat:no-repeat;background-position:bottom}}.fs0[data-v-7d3f20e7]{font-size:0}.fs26[data-v-7d3f20e7]{font-size:.17333333rem}.fs28[data-v-7d3f20e7]{font-size:.18666667rem!important}.fs30[data-v-7d3f20e7]{font-size:.2rem!important}.fs32[data-v-7d3f20e7]{font-size:.21333333rem!important}.fs34[data-v-7d3f20e7]{font-size:.22666667rem!important}.fs36[data-v-7d3f20e7]{font-size:.24rem}.fs40[data-v-7d3f20e7]{font-size:.26666667rem}.fs42[data-v-7d3f20e7]{font-size:.28rem}.fs44[data-v-7d3f20e7]{font-size:.29333333rem}.fs45[data-v-7d3f20e7]{font-size:.3rem}.fs46[data-v-7d3f20e7]{font-size:.30666667rem}.fs48[data-v-7d3f20e7]{font-size:.32rem!important}.fs50[data-v-7d3f20e7]{font-size:.33333333rem}.fs52[data-v-7d3f20e7]{font-size:.34666667rem}.fs54[data-v-7d3f20e7]{font-size:.36rem}.fs56[data-v-7d3f20e7]{font-size:.37333333rem}.fs60[data-v-7d3f20e7]{font-size:.4rem}.fs64[data-v-7d3f20e7]{font-size:.42666667rem}.fs66[data-v-7d3f20e7]{font-size:.44rem}.fs76[data-v-7d3f20e7]{font-size:.50666667rem}.fs70[data-v-7d3f20e7]{font-size:.46666667rem}.fs100[data-v-7d3f20e7]{font-size:.66666667rem}.shop-bg[data-v-7d3f20e7]{background:#e4393c;color:#fff}.shop-font[data-v-7d3f20e7]{color:#e4393c}.shop-font em[data-v-7d3f20e7]{display:block!important}.shopRgba[data-v-7d3f20e7]{color:hsla(0,0%,100%,.5)!important}.shopGreen[data-v-7d3f20e7]{color:#25ae5f}.shopFont[data-v-7d3f20e7]{color:#e4393c}.shopBlue[data-v-7d3f20e7]{color:#378ae8}.shopBlue-bg[data-v-7d3f20e7]{background:#378ae8}.shopGray[data-v-7d3f20e7]{color:#87858f}.shopRose-bg[data-v-7d3f20e7]{background:#f63854}.shopFff[data-v-7d3f20e7]{color:hsla(0,0%,100%,.4)!important}.shop-yellow[data-v-7d3f20e7]{background:#ffb12e}.shopborder[data-v-7d3f20e7]{color:#e4393c;border:1px solid #e4393c}.shop-border[data-v-7d3f20e7]{color:#000;border:1px solid #87858f}.shop-switch[data-v-7d3f20e7]:checked{border-color:#e4393c!important;background-color:#e4393c!important}.icon-fenglei[data-v-7d3f20e7]{display:block;margin:0 auto;width:.32rem;height:.32rem;background:url('+o("EDvs")+") 0 0 no-repeat;background-size:100% 100%}.icon-fenglei2[data-v-7d3f20e7]{display:block;margin:0 auto;width:.32rem;height:.32rem;background:url("+o("5sCz")+') 0 0 no-repeat;background-size:100% 100%}.el-checkbox__inner input[data-v-7d3f20e7]{display:none}.el-checkbox__inner input:checked+span[data-v-7d3f20e7]:after{-webkit-box-sizing:content-box;box-sizing:content-box;content:"";border:2px solid #e4393c;border-left:0;border-top:0;height:8px;left:5px;position:absolute;top:2px;-webkit-transform:rotate(45deg) scaleY(1);transform:rotate(45deg) scaleY(1);width:4px;-webkit-transition:-webkit-transform .15s cubic-bezier(.71,-.46,.88,.6) .05s;transition:-webkit-transform .15s cubic-bezier(.71,-.46,.88,.6) .05s;transition:transform .15s cubic-bezier(.71,-.46,.88,.6) .05s;transition:transform .15s cubic-bezier(.71,-.46,.88,.6) .05s,-webkit-transform .15s cubic-bezier(.71,-.46,.88,.6) .05s;-webkit-transform-origin:center;transform-origin:center}.el-checkbox__inner span[data-v-7d3f20e7]{display:inline-block;position:relative;border:1px solid #bfcbd9;border-radius:4px;-webkit-box-sizing:border-box;box-sizing:border-box;width:18px;height:18px;background-color:#fff;z-index:1;-webkit-transition:border-color .25s cubic-bezier(.71,-.46,.29,1.46),background-color .25s cubic-bezier(.71,-.46,.29,1.46);transition:border-color .25s cubic-bezier(.71,-.46,.29,1.46),background-color .25s cubic-bezier(.71,-.46,.29,1.46);vertical-align:middle}@keyframes dialogShow-data-v-7d3f20e7{0%{bottom:-100%}to{bottom:0}}@-webkit-keyframes dialogShow-data-v-7d3f20e7{0%{bottom:-100%}to{bottom:0}}.switch[data-v-7d3f20e7]{display:inline-block;text-align:start;text-indent:0;text-transform:none;text-shadow:none;word-spacing:normal;letter-spacing:normal;cursor:pointer;-webkit-tap-highlight-color:transparent;-webkit-rtl-ordering:logical;-webkit-user-select:text;text-rendering:auto;-webkit-writing-mode:horizontal-tb;position:relative;-webkit-box-sizing:border-box;box-sizing:border-box;outline:0;border:1px solid #d2d2d2;background-color:#d2d2d2;-webkit-appearance:none;-moz-appearance:none;appearance:none;vertical-align:middle}.small-switch[data-v-7d3f20e7]{width:45px;height:24px;border-radius:14px}.switch[data-v-7d3f20e7]:before{position:absolute;top:0;left:0;background-color:#d2d2d2;text-align:right;-webkit-transition:-webkit-transform .3s;transition:-webkit-transform .3s;transition:transform .3s;transition:transform .3s,-webkit-transform .3s}.small-switch[data-v-7d3f20e7]:before{width:21px;height:21px;border-radius:15px;line-height:27px;padding-right:10px}.switch[data-v-7d3f20e7]:after{position:absolute;top:0;left:0;background-color:#fff;-webkit-box-shadow:0 1px 3px rgba(0,0,0,.4);box-shadow:0 1px 3px rgba(0,0,0,.4);content:"";-webkit-transition:-webkit-transform .3s;transition:-webkit-transform .3s;transition:transform .3s;transition:transform .3s,-webkit-transform .3s}.small-switch[data-v-7d3f20e7]:after{width:21px;height:21px;border-radius:15px;line-height:22px;padding-right:10px}.small-switch[data-v-7d3f20e7]:checked:after{width:21px;height:21px;border-radius:20px;-webkit-transform:translateX(21px);transform:translateX(21px)}.style-main-bg[data-v-7d3f20e7]{background:#e4393c;color:#fff}.style-main-font[data-v-7d3f20e7]{color:#e4393c}.style-main-border[data-v-7d3f20e7]{color:#e4393c;border:1px solid #e4393c}.style-witch[data-v-7d3f20e7]:checked{border-color:#e4393c;background-color:#e4393c}.style-middle-bg[data-v-7d3f20e7]{background:#ffb12e;color:#fff}.style-middle-font[data-v-7d3f20e7]{color:#ffb12e}.style-right-bg[data-v-7d3f20e7]{background:#fff;color:#000}.style-right-font[data-v-7d3f20e7]{color:#fff}.dialog-input-main .dialog-input-box[data-v-7d3f20e7]{width:100%;font-size:0;padding:0 .53333333rem}.dialog-input-main .dialog-input-box .dialog-input[data-v-7d3f20e7]{width:100%;padding:.16666667rem;margin-bottom:.13333333rem;border:1px solid #e1e1e3;border-radius:3px}.dialog-input-main .dialog-input-box .dialog-input input[data-v-7d3f20e7]{width:100%;height:100%;border:0}.dialog-input-main .dialog-input-box .dialog-code input[data-v-7d3f20e7]{width:68%}.dialog-input-main .dialog-input-box .dialog-code span[data-v-7d3f20e7]{padding:.16666667rem 0}.dialog-input-main .dialog-bottom[data-v-7d3f20e7]{width:100%;font-size:0;display:-webkit-box}.dialog-input-main .dialog-bottom .dialog-button1[data-v-7d3f20e7]{text-align:center;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-flex:1;-o-box-flex:1;box-flex:1;-webkit-box-pack:center;-o-box-pack:center;box-pack:center;color:#25ae5f;padding:.3rem 0}.dialog-input-main .dialog-bottom .dialog-button2[data-v-7d3f20e7]{margin:8% auto;text-align:center;display:block;width:70%;background:#e4393c;color:#fff;padding:.2rem 0;border-radius:5px}.integral-top[data-v-7d3f20e7]{width:100%}.integral-top .bg-div[data-v-7d3f20e7]{height:2.66666667rem;background:url('+o("uVkj")+");color:#fff}.integral-top .bg-div p[data-v-7d3f20e7]{text-align:center}.integral-top .bg-div p[data-v-7d3f20e7]:first-child{padding:.66666667rem 0 .26666667rem}.integral-top .bg-div p[data-v-7d3f20e7]:last-child{font-size:1rem}.integral-top .bg-div .fs70[data-v-7d3f20e7]{font-size:.46666667rem!important}.integral-top .bg-color[data-v-7d3f20e7]{background:#f63854;height:.06666667rem}.integral-top .bg-tab[data-v-7d3f20e7]{background:#f86076;height:1.13333333rem;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify;-webkit-box-align:center;-o-box-align:center;box-align:center;color:#fff}.integral-top .bg-tab div[data-v-7d3f20e7]{font-size:.3rem;width:50%;height:100%;line-height:1.13333333rem;text-align:center}.integral-top .bg-tab div i[data-v-7d3f20e7]{margin-right:.3rem}.integral-middle[data-v-7d3f20e7]{width:100%;padding:.06666667rem 0;background:#fff}.integral-product .product-item[data-v-7d3f20e7]{margin-top:.06666667rem;height:2rem;background:#fff;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify;-webkit-box-align:center;-o-box-align:center;box-align:center}.integral-product .product-item .product-content[data-v-7d3f20e7]{width:90%;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:left;-o-box-pack:left;box-pack:left;-webkit-box-align:left;-o-box-align:left;box-align:left}.integral-product .product-item .product-content .product-img[data-v-7d3f20e7]{height:2rem;width:2rem;background-size:cover}.integral-product .product-item .product-content .content-div[data-v-7d3f20e7]{padding:0 .26666667rem;width:74%}.integral-product .product-item .product-content .content-div .product-title[data-v-7d3f20e7]{padding:.2rem 0;font-size:.33333333rem;white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.integral-product .product-item .product-content .content-div p[data-v-7d3f20e7]{font-size:.26666667rem}.integral-product .product-item .product-content .content-div .div-text[data-v-7d3f20e7]{color:#a9a9a9;padding-bottom:.2rem}.integral-product .product-item .div-icon[data-v-7d3f20e7]{width:10%;text-align:center;height:100%;line-height:2rem}",""])},kr9m:function(t,e,o){"use strict";function a(t){o("ydrn")}var i=o("lGvr"),d=o("Huuc"),r=o("0HdQ"),n=a,s=r(i.a,d.a,!1,n,"data-v-6bd4cf5e",null);e.a=s.exports},lGvr:function(t,e,o){"use strict";e.a={props:["background","isHeadPortrait"],data:function(){return{}},mounted:function(){}}},pbdT:function(t,e,o){e=t.exports=o("BkJT")(!1),e.push([t.i,".ik-box[data-v-6bd4cf5e]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-6bd4cf5e]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}.text-overflow[data-v-6bd4cf5e]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.imgbox[data-v-6bd4cf5e]{width:101%;height:101%;position:relative}.user-head-portrait[data-v-6bd4cf5e]{width:100%;height:100%;background-size:100%;background-repeat:no-repeat;background-position:50%;position:relative;z-index:1}.default-img[data-v-6bd4cf5e]{width:100%;height:100%;position:absolute;top:0;left:0;z-index:0;line-height:1;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:center;-o-box-pack:center;box-pack:center;-webkit-box-align:center;-o-box-align:center;box-align:center}.default-img .iconfont[data-v-6bd4cf5e]{font-size:1.33333333rem;color:#fff}",""])},uVkj:function(t,e,o){t.exports=o.p+"static/img/integral-bg.a4e6169.jpg"},"v+vZ":function(t,e,o){e=t.exports=o("BkJT")(!1),e.push([t.i,".ik-box[data-v-61addf18]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-61addf18]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}.text-overflow[data-v-61addf18]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.goods-img-display[data-v-61addf18]{position:relative;width:100%;margin-bottom:0}.goods-img-display .goods-img[data-v-61addf18]{width:100%;height:100%;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:center;-o-box-pack:center;box-pack:center;-webkit-box-align:center;-o-box-align:center;box-align:center;position:relative}.goods-img-display .goods-img img[data-v-61addf18]{margin:0 auto;display:block;max-width:100%}.goods-img-display .goods-img .img[data-v-61addf18]{max-height:100%}.goods-img-display .goods-img .cla-img[data-v-61addf18]{-webkit-transform:translateY(50% 50%)!important;transform:translateY(50% 50%)!important;position:absolute;top:50%;left:50%;-webkit-transform:translate(-50%,-50%)!important;transform:translate(-50%,-50%)!important}.goods-img-display .goods-img .icon-tupianjiazaizhong-[data-v-61addf18]{font-size:2rem;color:#d6d6d6}.goods-img-display .goods-origin-box[data-v-61addf18]{width:100%;text-align:center;font-size:0;position:absolute;bottom:5%;z-index:1}.goods-img-display .goods-origin-box i[data-v-61addf18]{display:inline-block;width:8px;height:8px;background:#ccc;border-radius:100%;margin:0 3px}.goods-img-display .goods-origin-box .img-selecte[data-v-61addf18]{background:#e4393c;width:14px;border-radius:20px}.goods-img-display .goods-origin-box .style-main-bg[data-v-61addf18]{width:14px;border-radius:20px}",""])},ydrn:function(t,e,o){var a=o("pbdT");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);o("8bSs")("c8df441a",a,!0)}});