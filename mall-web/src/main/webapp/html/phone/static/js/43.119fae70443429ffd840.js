webpackJsonp([43],{"9wKw":function(a,t,o){var e=o("JrIC");"string"==typeof e&&(e=[[a.i,e,""]]),e.locals&&(a.exports=e.locals);o("8bSs")("247f03f1",e,!0)},JrIC:function(a,t,o){t=a.exports=o("BkJT")(!1),t.push([a.i,".ik-box[data-v-717623a4]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-717623a4]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}.text-overflow[data-v-717623a4]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.imgbox[data-v-717623a4],.user-head-portrait[data-v-717623a4]{width:100%;height:100%;position:relative}.user-head-portrait[data-v-717623a4]{background-size:100%;background-repeat:no-repeat;background-position:50%;z-index:1}.default-img1[data-v-717623a4],.default-img2[data-v-717623a4]{width:100%;height:100%;position:absolute;top:0;left:0;z-index:0;line-height:1;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:center;-o-box-pack:center;box-pack:center;-webkit-box-align:center;-o-box-align:center;box-align:center}.default-img1 .iconfont[data-v-717623a4],.default-img2 .iconfont[data-v-717623a4]{font-size:1.33333333rem;color:#d6d6d6}.default-img2[data-v-717623a4]{background:#eee}.default-img2 .iconfont[data-v-717623a4]{font-size:1rem;color:#cecece}",""])},ZCjY:function(a,t,o){var e=o("wsri");"string"==typeof e&&(e=[[a.i,e,""]]),e.locals&&(a.exports=e.locals);o("8bSs")("1767ba8c",e,!0)},kr9m:function(a,t,o){"use strict";function e(a){o("9wKw")}var i={props:["background","isHeadPortrait","size"],data:function(){return{}},mounted:function(){}},r=function(){var a=this,t=a.$createElement,o=a._self._c||t;return o("div",{staticClass:"imgbox"},[o("div",{staticClass:"user-head-portrait",style:{backgroundImage:"url("+a.background+")"}}),a._v(" "),0==a.isHeadPortrait?o("div",{staticClass:"default-img1"},[o("i",{staticClass:"iconfont icon-tupianjiazaizhong-",style:{"font-size":a.size}})]):a._e(),a._v(" "),1==a.isHeadPortrait?o("div",{staticClass:"default-img2"},[o("i",{staticClass:"iconfont icon-ren1",style:{"font-size":a.size}})]):a._e()])},d=[],n={render:r,staticRenderFns:d},s=n,b=o("8AGX"),f=e,l=b(i,s,!1,f,"data-v-717623a4",null);t.a=l.exports},lqXV:function(a,t,o){"use strict";function e(a){o("ZCjY")}Object.defineProperty(t,"__esModule",{value:!0});var i=o("kr9m"),r={data:function(){return{type:this.$route.params.type,busId:this.$route.params.busId||sessionStorage.getItem("busId"),qrCodePath:null,imageUrl:null,headImageUrl:null,imgUrl:null,shareObj:null}},components:{defaultImg:i.a},mounted:function(){this.commonFn.setTitle("推广海报"),this.$store.commit("show_footer",!1),this.loadDatas()},beforeDestroy:function(){this.$store.commit("show_footer",!0)},methods:{loadDatas:function(a){var t=this,o={busId:t.busId,url:location.href,browerType:t.$store.state.browerType};t.ajaxRequest({url:h5App.activeAPI.seller_promotion_post,data:o,success:function(a){var o=a.data;t.imgUrl=a.imgUrl,t.imageUrl=o.imageUrl,t.qrCodePath=o.qrCodePath,t.headImageUrl=o.headImagePath,t.getWxShare(o)}})},getWxShare:function(a){},back:function(){window.history.go(-1)}}},d=function(){var a=this,t=a.$createElement,o=a._self._c||t;return o("div",{staticClass:"shop-wrapper sale-wrapper"},[null!=a.imageUrl?o("div",{staticClass:"index-nav clearfix"},[o("img",{staticClass:"p-code",attrs:{src:a.imageUrl}}),a._v(" "),o("div",{staticClass:"p-footer"},[a._v("\n          好友通过扫描海报购买商品，您将获得佣金\n      ")])]):o("div",{staticClass:"index-nav clearfix"},[o("div",{staticClass:"p-scan"},[null!=a.qrCodePath?o("img",{staticClass:"p-code",attrs:{src:a.qrCodePath}}):a._e(),null!=a.headImageUrl?o("img",{staticClass:"p-headicon",attrs:{src:a.headImageUrl}}):a._e()]),a._v(" "),o("div",{staticClass:"p-footer"},[a._v("\n          好友通过扫描海报购买商品，您将获得佣金\n      ")])])])},n=[],s={render:d,staticRenderFns:n},b=s,f=o("8AGX"),l=e,p=f(r,b,!1,l,"data-v-a758fbaa",null);t.default=p.exports},pToG:function(a,t,o){a.exports=o.p+"static/img/tg-code.0557b9d.jpg"},wsri:function(a,t,o){t=a.exports=o("BkJT")(!1),t.push([a.i,".ik-box[data-v-a758fbaa]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-a758fbaa]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}body[data-v-a758fbaa]{background:#f0f2f5;color:#333}.shop-wrapper[data-v-a758fbaa]{width:100%;position:relative;max-width:1242px;margin:0 auto}.shop-main[data-v-a758fbaa]{width:100%}.shop-main .shop-list[data-v-a758fbaa]{padding:.26666667rem 0}.shop-main .shop-add-itme[data-v-a758fbaa]{width:100%;background:#fff;padding-top:.33333333rem;padding-left:.46666667rem;height:2.46666667rem}.shop-main .shop-add-itme .shop-add-txt[data-v-a758fbaa]{padding-bottom:.13333333rem}.shop-main .shop-add-itme .add-left[data-v-a758fbaa]{float:left;width:93%}.shop-main .shop-add-itme .add-left p[data-v-a758fbaa]{margin-bottom:.06666667rem}.shop-main .shop-add-itme .add-left p span[data-v-a758fbaa]{margin-left:.26666667rem}.shop-main .shop-add-itme .add-right[data-v-a758fbaa]{float:left;width:7%;color:#d1d2d4;height:100%;position:relative}.shop-main .shop-add-itme .add-right i[data-v-a758fbaa]{font-size:.32rem;position:absolute;top:.33333333rem}.shop-main .shop-add-itme .shop-add-footer[data-v-a758fbaa]{width:96%;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-align:center;-o-box-align:center;box-align:center;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify}.shop-main .shop-add-itme .shop-add-footer .shop-add-button1[data-v-a758fbaa]{padding:.18666667rem 0}.shop-main .shop-add-itme .shop-add-footer .shop-add-button1 i[data-v-a758fbaa]{width:.35333333rem;height:.35333333rem;background:#d1d2d4;color:#fff;vertical-align:0}.shop-main .shop-add-itme .shop-add-footer .shop-add-button2 span[data-v-a758fbaa]{display:inline-block;padding:.18666667rem 0}.shop-main .shop-add-itme .shop-add-footer .shop-add-button2 i[data-v-a758fbaa]{margin-right:.06666667rem;font-size:.32rem;color:#d1d2d4}.em-tag[data-v-a758fbaa]{background:#e4393c;margin-right:.06666667rem}.em-flag[data-v-a758fbaa],.em-tag[data-v-a758fbaa]{color:#fff;padding:3px 5px 2px;line-height:1;display:inline-block;border-radius:2px;font-size:.18666667rem}.em-flag[data-v-a758fbaa]{background:-moz-linear-gradient(right,#f85e65,#e7242c)}.em-choice[data-v-a758fbaa],.em-input[data-v-a758fbaa],.em-search[data-v-a758fbaa]{color:#333;padding:.17333333rem .25333333rem;line-height:1;margin-right:.24rem;display:inline-block;border-radius:2px;font-size:.18666667rem;background:#f3f2f8;border-radius:3px}.em-input[data-v-a758fbaa]{width:.8rem;vertical-align:bottom;height:.6rem;border:0;margin-right:3px;padding:2px;text-align:center}.em-search[data-v-a758fbaa]{margin-bottom:.18666667rem;background:#d7d9dc}.em-nav[data-v-a758fbaa]{background:#fbd3d3;padding:.13333333rem .2rem;border-radius:3px}.shop-max-button[data-v-a758fbaa]{width:100%;height:100%;border-radius:5px;color:hsla(0,0%,100%,.3);display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-align:center;-o-box-align:center;box-align:center;-webkit-box-pack:center;-o-box-pack:center;box-pack:center}.shop-footer-ab[data-v-a758fbaa]{position:absolute}.shop-footer-ab[data-v-a758fbaa],.shop-footer[data-v-a758fbaa]{width:100%;bottom:0}.shop-footer-ab .shop-logo[data-v-a758fbaa],.shop-footer .shop-logo[data-v-a758fbaa]{margin:0 auto;width:3rem;height:.46666667rem;background:url("+o("DDBx")+');background-size:100%;margin-bottom:.24rem}.shop-footer-fixed[data-v-a758fbaa]{position:fixed;bottom:0;left:0;width:100%}.shop-footer-fixed .footer-nav[data-v-a758fbaa]{width:100%;max-width:8.28rem;margin:0 auto;background:#fff;border-top:1px solid #e2e2e2;height:1.05333333rem}.shop-footer-fixed .footer-nav i[data-v-a758fbaa]{font-size:.4rem;margin-bottom:.06666667rem}.shop-header[data-v-a758fbaa]{top:0;background:#fff;z-index:2;max-width:8.25rem}.shop-header .header-nav[data-v-a758fbaa]{height:.98666667rem}.shop-header .header-nav em[data-v-a758fbaa]{height:.04rem;width:100%;position:absolute;bottom:0;display:none}.shop-fl[data-v-a758fbaa]{float:left}.shop-fr[data-v-a758fbaa]{float:right}.shop-hide[data-v-a758fbaa]{display:none}.shop-show[data-v-a758fbaa]{display:block}.shop-box[data-v-a758fbaa]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.shop-inblock[data-v-a758fbaa]{display:inline-block}.shop-box-center[data-v-a758fbaa],.shop-box-justify[data-v-a758fbaa]{display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify}.shop-box-center[data-v-a758fbaa]{-webkit-box-align:center;-o-box-align:center;box-align:center}.clearfix[data-v-a758fbaa]{zoom:1;_zoom:1;clear:both}.clearfix[data-v-a758fbaa]:after{clear:both;content:"";display:block;width:0;height:0;visibility:hidden}.overflow-x[data-v-a758fbaa]{overflow:hidden;overflow-x:hidden}.shop-textl[data-v-a758fbaa]{text-align:left}.shop-textr[data-v-a758fbaa]{text-align:right}.shop-textc[data-v-a758fbaa]{text-align:center}.text-overflow[data-v-a758fbaa]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.text-force-wrap[data-v-a758fbaa]{word-break:break-all;white-space:pre-wrap}.text-not-wrap[data-v-a758fbaa]{white-space:nowrap}.text-more-overflow[data-v-a758fbaa]{overflow:hidden;position:relative;text-align:justify}.text-more-overflow[data-v-a758fbaa]:after{content:" ... ";position:absolute;bottom:1px;right:0;padding:0 1px 1px 2px;background:#fff}.border-radius[data-v-a758fbaa],.border[data-v-a758fbaa]{position:relative;border-bottom:1px solid #e0e0e0}@media (-webkit-min-device-pixel-ratio:2){.border[data-v-a758fbaa]{border:none;background-image:-webkit-gradient(linear,left top,left bottom,from(0),color-stop(#fff),color-stop(50%,#d9d9d9),color-stop(50%,transparent));background-image:linear-gradient(0,#fff,#d9d9d9 50%,transparent 0);background-size:100% 1px;background-repeat:no-repeat;background-position:bottom}}.fs0[data-v-a758fbaa]{font-size:0}.fs26[data-v-a758fbaa]{font-size:.17333333rem}.fs28[data-v-a758fbaa]{font-size:.18666667rem!important}.fs30[data-v-a758fbaa]{font-size:.2rem!important}.fs32[data-v-a758fbaa]{font-size:.21333333rem!important}.fs34[data-v-a758fbaa]{font-size:.22666667rem!important}.fs36[data-v-a758fbaa]{font-size:.24rem}.fs38[data-v-a758fbaa]{font-size:.25333333rem}.fs40[data-v-a758fbaa]{font-size:.26666667rem}.fs42[data-v-a758fbaa]{font-size:.28rem}.fs43[data-v-a758fbaa]{font-size:.28666667rem}.fs44[data-v-a758fbaa]{font-size:.29333333rem}.fs45[data-v-a758fbaa]{font-size:.3rem}.fs46[data-v-a758fbaa]{font-size:.30666667rem}.fs48[data-v-a758fbaa]{font-size:.32rem!important}.fs50[data-v-a758fbaa]{font-size:.33333333rem}.fs52[data-v-a758fbaa]{font-size:.34666667rem}.fs54[data-v-a758fbaa]{font-size:.36rem}.fs56[data-v-a758fbaa]{font-size:.37333333rem}.fs60[data-v-a758fbaa]{font-size:.4rem}.fs64[data-v-a758fbaa]{font-size:.42666667rem}.fs66[data-v-a758fbaa]{font-size:.44rem}.fs76[data-v-a758fbaa]{font-size:.50666667rem}.fs70[data-v-a758fbaa]{font-size:.46666667rem}.fs100[data-v-a758fbaa]{font-size:.66666667rem}.shop-bg[data-v-a758fbaa]{background:#e4393c;color:#fff}.shop-gray-bg[data-v-a758fbaa]{background:#afadad;color:#fff}.shop-font[data-v-a758fbaa]{color:#e4393c}.shop-font em[data-v-a758fbaa]{display:block!important}.shopRgba[data-v-a758fbaa]{color:hsla(0,0%,100%,.5)!important}.shopGreen[data-v-a758fbaa]{color:#25ae5f}.shopFont[data-v-a758fbaa]{color:#e4393c}.shopBlue[data-v-a758fbaa]{color:#378ae8}.shopBlue-bg[data-v-a758fbaa]{background:#378ae8}.shopGray[data-v-a758fbaa]{color:#87858f}.shopRose-bg[data-v-a758fbaa]{background:#f63854}.shopFff[data-v-a758fbaa]{color:hsla(0,0%,100%,.4)!important}.shop-yellow[data-v-a758fbaa]{background:#ffb12e}.shopborder[data-v-a758fbaa]{color:#e4393c;border:1px solid #e4393c}.shop-border[data-v-a758fbaa]{color:#000;border:1px solid #87858f}.shop-switch[data-v-a758fbaa]:checked{border-color:#e4393c!important;background-color:#e4393c!important}.icon-fenglei[data-v-a758fbaa]{display:block;margin:0 auto;width:.32rem;height:.32rem;background:url('+o("EDvs")+") 0 0 no-repeat;background-size:100% 100%}.icon-fenglei2[data-v-a758fbaa]{display:block;margin:0 auto;width:.32rem;height:.32rem;background:url("+o("5sCz")+') 0 0 no-repeat;background-size:100% 100%}.el-checkbox__inner input[data-v-a758fbaa]{display:none}.el-checkbox__inner input:checked+span[data-v-a758fbaa]:after{-webkit-box-sizing:content-box;box-sizing:content-box;content:"";border:2px solid #e4393c;border-left:0;border-top:0;height:8px;left:5px;position:absolute;top:2px;-webkit-transform:rotate(45deg) scaleY(1);transform:rotate(45deg) scaleY(1);width:4px;-webkit-transition:-webkit-transform .15s cubic-bezier(.71,-.46,.88,.6) .05s;transition:-webkit-transform .15s cubic-bezier(.71,-.46,.88,.6) .05s;transition:transform .15s cubic-bezier(.71,-.46,.88,.6) .05s;transition:transform .15s cubic-bezier(.71,-.46,.88,.6) .05s,-webkit-transform .15s cubic-bezier(.71,-.46,.88,.6) .05s;-webkit-transform-origin:center;transform-origin:center}.el-checkbox__inner span[data-v-a758fbaa]{display:inline-block;position:relative;border:1px solid #bfcbd9;border-radius:4px;-webkit-box-sizing:border-box;box-sizing:border-box;width:18px;height:18px;background-color:#fff;z-index:1;-webkit-transition:border-color .25s cubic-bezier(.71,-.46,.29,1.46),background-color .25s cubic-bezier(.71,-.46,.29,1.46);transition:border-color .25s cubic-bezier(.71,-.46,.29,1.46),background-color .25s cubic-bezier(.71,-.46,.29,1.46);vertical-align:middle}@keyframes dialogShow-data-v-a758fbaa{0%{bottom:-100%}to{bottom:0}}@-webkit-keyframes dialogShow-data-v-a758fbaa{0%{bottom:-100%}to{bottom:0}}.switch[data-v-a758fbaa]{display:inline-block;text-align:start;text-indent:0;text-transform:none;text-shadow:none;word-spacing:normal;letter-spacing:normal;cursor:pointer;-webkit-tap-highlight-color:transparent;-webkit-rtl-ordering:logical;-webkit-user-select:text;text-rendering:auto;-webkit-writing-mode:horizontal-tb;position:relative;-webkit-box-sizing:border-box;box-sizing:border-box;outline:0;border:1px solid #d2d2d2;background-color:#d2d2d2;-webkit-appearance:none;-moz-appearance:none;appearance:none;vertical-align:middle}.small-switch[data-v-a758fbaa]{width:45px;height:24px;border-radius:14px}.switch[data-v-a758fbaa]:before{position:absolute;top:0;left:0;background-color:#d2d2d2;text-align:right;-webkit-transition:-webkit-transform .3s;transition:-webkit-transform .3s;transition:transform .3s;transition:transform .3s,-webkit-transform .3s}.small-switch[data-v-a758fbaa]:before{width:21px;height:21px;border-radius:15px;line-height:27px;padding-right:10px}.switch[data-v-a758fbaa]:after{position:absolute;top:0;left:0;background-color:#fff;-webkit-box-shadow:0 1px 3px rgba(0,0,0,.4);box-shadow:0 1px 3px rgba(0,0,0,.4);content:"";-webkit-transition:-webkit-transform .3s;transition:-webkit-transform .3s;transition:transform .3s;transition:transform .3s,-webkit-transform .3s}.small-switch[data-v-a758fbaa]:after{width:21px;height:21px;border-radius:15px;line-height:22px;padding-right:10px}.small-switch[data-v-a758fbaa]:checked:after{width:21px;height:21px;border-radius:20px;-webkit-transform:translateX(21px);transform:translateX(21px)}.style-main-bg[data-v-a758fbaa]{background:#e4393c;color:#fff}.style-main-font[data-v-a758fbaa]{color:#e4393c}.style-main-border[data-v-a758fbaa]{color:#e4393c;border:1px solid #e4393c}.style-witch[data-v-a758fbaa]:checked{border-color:#e4393c;background-color:#e4393c}.style-middle-bg[data-v-a758fbaa]{background:#ffb12e;color:#fff}.style-middle-font[data-v-a758fbaa]{color:#ffb12e}.style-right-bg[data-v-a758fbaa]{background:#fff;color:#000}.style-right-font[data-v-a758fbaa]{color:#fff}.dialog-input-main .dialog-input-box[data-v-a758fbaa]{width:100%;font-size:0;padding:0 .53333333rem}.dialog-input-main .dialog-input-box .dialog-input[data-v-a758fbaa]{width:100%;padding:.16666667rem;margin-bottom:.13333333rem;border:1px solid #e1e1e3;border-radius:3px}.dialog-input-main .dialog-input-box .dialog-input input[data-v-a758fbaa]{width:100%;height:100%;border:0}.dialog-input-main .dialog-input-box .dialog-code input[data-v-a758fbaa]{width:68%}.dialog-input-main .dialog-input-box .dialog-code span[data-v-a758fbaa]{padding:.16666667rem 0}.dialog-input-main .dialog-bottom[data-v-a758fbaa]{width:100%;font-size:0;display:-webkit-box}.dialog-input-main .dialog-bottom .dialog-button1[data-v-a758fbaa]{text-align:center;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-flex:1;-o-box-flex:1;box-flex:1;-webkit-box-pack:center;-o-box-pack:center;box-pack:center;color:#25ae5f;padding:.3rem 0}.dialog-input-main .dialog-bottom .dialog-button2[data-v-a758fbaa]{margin:8% auto;text-align:center;display:block;width:70%;background:#e4393c;color:#fff;padding:.2rem 0;border-radius:5px}.sale-wrapper[data-v-a758fbaa]{background:#fdecd2}.sale-wrapper .index-nav[data-v-a758fbaa]{position:relative;width:100%;background:url('+o("pToG")+");background-size:100%;background-repeat:no-repeat;background-position:top;background-color:#fdecd2}.sale-wrapper .p-scan[data-v-a758fbaa]{height:12.41333333rem;padding:6.6rem 2.33333333rem 0 1.12rem}.sale-wrapper .p-scan img[data-v-a758fbaa]{width:2.35333333rem;height:2.35333333rem}.sale-wrapper .p-scan .p-headicon[data-v-a758fbaa]{width:.96666667rem;height:.96666667rem;vertical-align:top;margin-left:.28666667rem}.sale-wrapper .p-footer[data-v-a758fbaa]{color:#333;background:#fff;height:1.03333333rem;line-height:1.03333333rem;width:100%;position:fixed;bottom:0;text-align:center;font-size:.26666667rem}",""])}});