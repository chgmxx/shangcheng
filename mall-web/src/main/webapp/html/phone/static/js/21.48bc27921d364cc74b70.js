webpackJsonp([21],{"8UiK":function(e,t,a){"use strict";var o=a("kr9m"),i=a("JHeL"),r=a("uotU");t.a={data:function(){return{busId:this.$route.params.busId||sessionStorage.getItem("busId"),rankArr:null,type:1,curPage:1,pageCount:1,headerArr:[{id:1,name:"周榜"},{id:2,name:"月榜"},{id:3,name:"年榜"},{id:4,name:"总榜"}],isMore:-1}},components:{defaultImg:o.a,headerNav:i.a,more:r.a},watch:{type:function(){this.rankArr=null,this.isMore=2,this.curPage=1,this.loadDatas({curPage:this.curPage})}},mounted:function(){this.commonFn.setTitle("销售员排行榜"),this.$store.commit("show_footer",!1),this.loadDatas({curPage:this.curPage}),this.scroll()},beforeDestroy:function(){this.$store.commit("show_footer",!0)},methods:{scroll:function(){var e=this;$(window).bind("scroll",function(){$(window).scrollTop()>0&&$(window).scrollTop()>=$(document).height()-$(window).height()-1e3&&(this.isMore=-1,e.loadMore())})},loadMore:function(){var e=this.pageCount;if(this.curPage>=e)return void(this.isMore=3);2!=this.isMore&&(this.curPage++,this.isMore=2,this.loadDatas({curPage:this.curPage}))},loadDatas:function(e){var t=this,a={busId:t.busId,url:location.href,browerType:t.$store.state.browerType,type:this.type,curPage:e.curPage>0?e.curPage:1};t.ajaxRequest({url:h5App.activeAPI.seller_rank_post,data:a,success:function(e){var a=e.data;t.imgUrl=e.imgUrl;var o=a.page,i=o.subList;t.curPage=o.curPage,t.pageCount=o.pageCount,1===t.curPage?t.rankArr=i:t.rankArr=t.rankArr.concat(i)||[],t.isMore=1,t.curPage>=t.pageCount&&(t.isMore=3)}})},back:function(){window.history.go(-1)}}}},"91zx":function(e,t,a){var o=a("Fewj");"string"==typeof o&&(o=[[e.i,o,""]]),o.locals&&(e.exports=o.locals);a("8bSs")("7b864380",o,!0)},DTIE:function(e,t,a){"use strict";function o(e){a("O4KM")}Object.defineProperty(t,"__esModule",{value:!0});var i=a("8UiK"),r=a("ULlz"),s=a("0HdQ"),d=o,n=s(i.a,r.a,!1,d,"data-v-728c0e75",null);t.default=n.exports},EuJa:function(e,t,a){"use strict";var o=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"shop-header"},[a("div",{staticClass:"header-nav fs42"},e._l(e.headers,function(t){return a("div",{key:t.id,staticClass:"header-itme",class:{"shop-font":t.id==e.selectNav||e.selectId==t.id},on:{click:function(a){e.selects(t.id)}}},[a("p",{domProps:{textContent:e._s(t.name)}}),e._v(" "),a("em",{staticClass:"shop-bg"})])}))])},i=[],r={render:o,staticRenderFns:i};t.a=r},Fewj:function(e,t,a){t=e.exports=a("BkJT")(!1),t.push([e.i,".ik-box[data-v-049a7e75]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-049a7e75]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}.text-overflow[data-v-049a7e75]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.shop-header[data-v-049a7e75]{top:0}.shop-header .header-nav[data-v-049a7e75]{height:.98666667rem}.shop-header .header-nav em[data-v-049a7e75]{height:.04rem;width:100%;display:none}.shop-header[data-v-049a7e75]{width:100%;background:#fff}.shop-header .header-nav .header-itme[data-v-049a7e75],.shop-header .header-nav[data-v-049a7e75]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.shop-header .header-nav .header-itme[data-v-049a7e75]{position:relative;-webkit-box-align:center;-o-box-align:center;box-align:center;-webkit-box-pack:center;-o-box-pack:center;box-pack:center;-webkit-box-flex:1;-o-box-flex:1;box-flex:1;-webkit-box-orient:vertical;-o-box-orient:vertical;box-orient:vertical}.shop-header .header-nav a[data-v-049a7e75]{display:block;text-align:center}.shop-header .header-nav .shop-font[data-v-049a7e75]{color:#4e95ef}.shop-header .header-nav .shop-bg[data-v-049a7e75]{background:#4e95ef}",""])},GqgL:function(e,t,a){t=e.exports=a("BkJT")(!1),t.push([e.i,".more-main{width:100%;font-size:.3rem;color:#737373;text-align:center;padding:.25rem 0;margin-bottom:60px}.more-main p{margin-bottom:.05rem}",""])},JHeL:function(e,t,a){"use strict";function o(e){a("91zx")}var i=a("mhRB"),r=a("EuJa"),s=a("0HdQ"),d=o,n=s(i.a,r.a,!1,d,"data-v-049a7e75",null);t.a=n.exports},Lg5A:function(e,t){e.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAIAAAC2BqGFAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo5RDU5RkNFODZCNjYxMUU3OEFDNkZFRjc4RDI0NDJGNyIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo5RDU5RkNFOTZCNjYxMUU3OEFDNkZFRjc4RDI0NDJGNyI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjlENTlGQ0U2NkI2NjExRTc4QUM2RkVGNzhEMjQ0MkY3IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjlENTlGQ0U3NkI2NjExRTc4QUM2RkVGNzhEMjQ0MkY3Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+9wpSMQAAExdJREFUeNrsnQuYTdUe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"},NZon:function(e,t,a){var o=a("V10O");"string"==typeof o&&(o=[[e.i,o,""]]),o.locals&&(e.exports=o.locals);a("8bSs")("3941ff8a",o,!0)},O4KM:function(e,t,a){var o=a("Vb/t");"string"==typeof o&&(o=[[e.i,o,""]]),o.locals&&(e.exports=o.locals);a("8bSs")("1bc8d31d",o,!0)},OWAf:function(e,t){e.exports={props:["isMore"],data:function(){return{}}}},Pzjh:function(e,t,a){"use strict";var o=function(){var e=this,t=e.$createElement,o=e._self._c||t;return o("div",{staticClass:"imgbox"},[o("div",{staticClass:"user-head-portrait",style:{backgroundImage:"url("+e.background+")"}}),e._v(" "),1==e.isHeadPortrait&&void 0==e.background?o("img",{staticClass:"default-img",attrs:{src:a("Lg5A")}}):e._e(),e._v(" "),0==e.isHeadPortrait&&void 0==e.background?o("img",{staticClass:"default-img",attrs:{src:a("vl5z")}}):e._e()])},i=[],r={render:o,staticRenderFns:i};t.a=r},ULlz:function(e,t,a){"use strict";var o=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"shop-wrapper sale-wrapper"},[a("div",{staticClass:"seller-top"},[a("div",{staticClass:"seller-nav"},[a("i",{staticClass:"iconfont icon-jiantou-copy1",on:{click:e.back}}),e._v("销售员排行榜\n        ")]),e._v(" "),a("div",{staticClass:"seller-nav2 border"},[a("header-nav",{attrs:{headers:e.headerArr,selectId:e.type,type:e.type},on:{"update:type":function(t){e.type=t}}})],1)]),e._v(" "),null!=e.rankArr?a("div",{staticClass:"seller-content"},[e._l(e.rankArr,function(t,o){return a("div",{key:o,staticClass:"seller-item"},[a("div",{staticClass:"item-left"},[a("div",{staticClass:"blue-color index-div"},[e._v(e._s(o+1))]),e._v(" "),a("div",{staticClass:"img-div"},[a("default-img",{attrs:{background:t.headimgurl,isHeadPortrait:1}})],1),e._v(" "),a("div",{staticClass:"text-overflow"},[e._v(e._s(t.user_name||t.nickname))])]),e._v(" "),a("div",{staticClass:"item-right shop-font"},[e._v(e._s(t.sale_money))])])}),e._v(" "),a("more",{attrs:{"is-more":e.isMore}})],2):e._e()])},i=[],r={render:o,staticRenderFns:i};t.a=r},V10O:function(e,t,a){t=e.exports=a("BkJT")(!1),t.push([e.i,".ik-box[data-v-f4ba2e06]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-f4ba2e06]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}.text-overflow[data-v-f4ba2e06]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.imgbox[data-v-f4ba2e06]{width:101%;height:101%;position:relative}.user-head-portrait[data-v-f4ba2e06]{width:100%;height:100%;background-size:100%;background-repeat:no-repeat;background-position:50%;position:relative;z-index:1}.default-img[data-v-f4ba2e06]{width:100%;height:100%;position:absolute;top:0;left:0;z-index:0}",""])},"Vb/t":function(e,t,a){t=e.exports=a("BkJT")(!1),t.push([e.i,".ik-box[data-v-728c0e75]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-728c0e75]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}body[data-v-728c0e75]{background:#f0f2f5;color:#333}.shop-wrapper[data-v-728c0e75]{width:100%;position:relative;max-width:1242px;margin:0 auto}.shop-main[data-v-728c0e75]{width:100%}.shop-main .shop-list[data-v-728c0e75]{padding:.26666667rem 0}.shop-main .shop-add-itme[data-v-728c0e75]{width:100%;background:#fff;padding-top:.33333333rem;padding-left:.46666667rem;height:2.46666667rem}.shop-main .shop-add-itme .shop-add-txt[data-v-728c0e75]{padding-bottom:.13333333rem}.shop-main .shop-add-itme .add-left[data-v-728c0e75]{float:left;width:93%}.shop-main .shop-add-itme .add-left p[data-v-728c0e75]{margin-bottom:.06666667rem}.shop-main .shop-add-itme .add-left p span[data-v-728c0e75]{margin-left:.26666667rem}.shop-main .shop-add-itme .add-right[data-v-728c0e75]{float:left;width:7%;color:#d1d2d4;height:100%;position:relative}.shop-main .shop-add-itme .add-right i[data-v-728c0e75]{font-size:.32rem;position:absolute;top:.33333333rem}.shop-main .shop-add-itme .shop-add-footer[data-v-728c0e75]{width:96%;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-align:center;-o-box-align:center;box-align:center;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify}.shop-main .shop-add-itme .shop-add-footer .shop-add-button1[data-v-728c0e75]{padding:.18666667rem 0}.shop-main .shop-add-itme .shop-add-footer .shop-add-button1 i[data-v-728c0e75]{width:.35333333rem;height:.35333333rem;background:#d1d2d4;color:#fff;vertical-align:0}.shop-main .shop-add-itme .shop-add-footer .shop-add-button2 span[data-v-728c0e75]{display:inline-block;padding:.18666667rem 0}.shop-main .shop-add-itme .shop-add-footer .shop-add-button2 i[data-v-728c0e75]{margin-right:.06666667rem;font-size:.32rem;color:#d1d2d4}.em-tag[data-v-728c0e75]{background:#e4393c;margin-right:.06666667rem}.em-flag[data-v-728c0e75],.em-tag[data-v-728c0e75]{color:#fff;padding:3px 5px 2px;line-height:1;display:inline-block;border-radius:2px;font-size:.18666667rem}.em-flag[data-v-728c0e75]{background:-moz-linear-gradient(right,#f85e65,#e7242c)}.em-choice[data-v-728c0e75],.em-input[data-v-728c0e75],.em-search[data-v-728c0e75]{color:#333;padding:.17333333rem .25333333rem;line-height:1;margin-right:.24rem;display:inline-block;border-radius:2px;font-size:.18666667rem;background:#f3f2f8;border-radius:3px}.em-input[data-v-728c0e75]{width:.8rem;vertical-align:bottom;height:.6rem;border:0;margin-right:3px;padding:2px;text-align:center}.em-search[data-v-728c0e75]{margin-bottom:.18666667rem;background:#d7d9dc}.em-nav[data-v-728c0e75]{background:#fbd3d3;padding:.13333333rem .2rem;border-radius:3px}.shop-max-button[data-v-728c0e75]{width:100%;height:100%;border-radius:5px;color:hsla(0,0%,100%,.3);display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-align:center;-o-box-align:center;box-align:center;-webkit-box-pack:center;-o-box-pack:center;box-pack:center}.shop-footer-ab[data-v-728c0e75]{position:absolute}.shop-footer-ab[data-v-728c0e75],.shop-footer[data-v-728c0e75]{width:100%;bottom:0}.shop-footer-ab .shop-logo[data-v-728c0e75],.shop-footer .shop-logo[data-v-728c0e75]{margin:0 auto;width:3rem;height:.46666667rem;background:url("+a("DDBx")+');background-size:100%;margin-bottom:.24rem}.shop-footer-fixed[data-v-728c0e75]{position:fixed;bottom:0;left:0}.shop-footer-fixed .footer-nav[data-v-728c0e75]{border-top:1px solid #e2e2e2;height:1.05333333rem}.shop-footer-fixed .footer-nav i[data-v-728c0e75]{font-size:.4rem;margin-bottom:.06666667rem}.shop-header[data-v-728c0e75]{top:0;background:#fff;z-index:2}.shop-header .header-nav[data-v-728c0e75]{height:.98666667rem}.shop-header .header-nav em[data-v-728c0e75]{height:.04rem;width:100%;position:absolute;bottom:0;display:none}.shop-fl[data-v-728c0e75]{float:left}.shop-fr[data-v-728c0e75]{float:right}.shop-hide[data-v-728c0e75]{display:none}.shop-show[data-v-728c0e75]{display:block}.shop-box[data-v-728c0e75]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.shop-inblock[data-v-728c0e75]{display:inline-block}.shop-box-center[data-v-728c0e75],.shop-box-justify[data-v-728c0e75]{display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify}.shop-box-center[data-v-728c0e75]{-webkit-box-align:center;-o-box-align:center;box-align:center}.clearfix[data-v-728c0e75]{zoom:1;_zoom:1;clear:both}.clearfix[data-v-728c0e75]:after{clear:both;content:"";display:block;width:0;height:0;visibility:hidden}.overflow-x[data-v-728c0e75]{overflow:hidden;overflow-x:hidden}.shop-textl[data-v-728c0e75]{text-align:left}.shop-textr[data-v-728c0e75]{text-align:right}.shop-textc[data-v-728c0e75]{text-align:center}.text-overflow[data-v-728c0e75]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.text-force-wrap[data-v-728c0e75]{word-break:break-all;white-space:pre-wrap}.text-not-wrap[data-v-728c0e75]{white-space:nowrap}.text-more-overflow[data-v-728c0e75]{overflow:hidden;position:relative;text-align:justify}.text-more-overflow[data-v-728c0e75]:after{content:" ... ";position:absolute;bottom:1px;right:0;padding:0 1px 1px 2px;background:#fff}.border-radius[data-v-728c0e75],.border[data-v-728c0e75]{position:relative;border-bottom:1px solid #e0e0e0}@media (-webkit-min-device-pixel-ratio:2){.border[data-v-728c0e75]{border:none;background-image:-webkit-gradient(linear,left top,left bottom,from(0),color-stop(#fff),color-stop(50%,#d9d9d9),color-stop(50%,transparent));background-image:linear-gradient(0,#fff,#d9d9d9 50%,transparent 0);background-size:100% 1px;background-repeat:no-repeat;background-position:bottom}}.fs0[data-v-728c0e75]{font-size:0}.fs26[data-v-728c0e75]{font-size:.17333333rem}.fs28[data-v-728c0e75]{font-size:.18666667rem!important}.fs30[data-v-728c0e75]{font-size:.2rem!important}.fs32[data-v-728c0e75]{font-size:.21333333rem!important}.fs34[data-v-728c0e75]{font-size:.22666667rem!important}.fs36[data-v-728c0e75]{font-size:.24rem}.fs40[data-v-728c0e75]{font-size:.26666667rem}.fs42[data-v-728c0e75]{font-size:.28rem}.fs44[data-v-728c0e75]{font-size:.29333333rem}.fs45[data-v-728c0e75]{font-size:.3rem}.fs46[data-v-728c0e75]{font-size:.30666667rem}.fs48[data-v-728c0e75]{font-size:.32rem!important}.fs50[data-v-728c0e75]{font-size:.33333333rem}.fs52[data-v-728c0e75]{font-size:.34666667rem}.fs54[data-v-728c0e75]{font-size:.36rem}.fs56[data-v-728c0e75]{font-size:.37333333rem}.fs60[data-v-728c0e75]{font-size:.4rem}.fs64[data-v-728c0e75]{font-size:.42666667rem}.fs66[data-v-728c0e75]{font-size:.44rem}.fs76[data-v-728c0e75]{font-size:.50666667rem}.fs70[data-v-728c0e75]{font-size:.46666667rem}.fs100[data-v-728c0e75]{font-size:.66666667rem}.shop-bg[data-v-728c0e75]{background:#e4393c;color:#fff}.shop-font[data-v-728c0e75]{color:#e4393c}.shop-font em[data-v-728c0e75]{display:block!important}.shopRgba[data-v-728c0e75]{color:hsla(0,0%,100%,.5)!important}.shopGreen[data-v-728c0e75]{color:#25ae5f}.shopFont[data-v-728c0e75]{color:#e4393c}.shopBlue[data-v-728c0e75]{color:#378ae8}.shopBlue-bg[data-v-728c0e75]{background:#378ae8}.shopGray[data-v-728c0e75]{color:#87858f}.shopRose-bg[data-v-728c0e75]{background:#f63854}.shopFff[data-v-728c0e75]{color:hsla(0,0%,100%,.4)!important}.shop-yellow[data-v-728c0e75]{background:#ffb12e}.shopborder[data-v-728c0e75]{color:#e4393c;border:1px solid #e4393c}.shop-border[data-v-728c0e75]{color:#000;border:1px solid #87858f}.shop-switch[data-v-728c0e75]:checked{border-color:#e4393c!important;background-color:#e4393c!important}.icon-fenglei[data-v-728c0e75]{display:block;margin:0 auto;width:.32rem;height:.32rem;background:url('+a("EDvs")+") 0 0 no-repeat;background-size:100% 100%}.icon-fenglei2[data-v-728c0e75]{display:block;margin:0 auto;width:.32rem;height:.32rem;background:url("+a("5sCz")+') 0 0 no-repeat;background-size:100% 100%}.el-checkbox__inner input[data-v-728c0e75]{display:none}.el-checkbox__inner input:checked+span[data-v-728c0e75]:after{-webkit-box-sizing:content-box;box-sizing:content-box;content:"";border:2px solid #e4393c;border-left:0;border-top:0;height:8px;left:5px;position:absolute;top:2px;-webkit-transform:rotate(45deg) scaleY(1);transform:rotate(45deg) scaleY(1);width:4px;-webkit-transition:-webkit-transform .15s cubic-bezier(.71,-.46,.88,.6) .05s;transition:-webkit-transform .15s cubic-bezier(.71,-.46,.88,.6) .05s;transition:transform .15s cubic-bezier(.71,-.46,.88,.6) .05s;transition:transform .15s cubic-bezier(.71,-.46,.88,.6) .05s,-webkit-transform .15s cubic-bezier(.71,-.46,.88,.6) .05s;-webkit-transform-origin:center;transform-origin:center}.el-checkbox__inner span[data-v-728c0e75]{display:inline-block;position:relative;border:1px solid #bfcbd9;border-radius:4px;-webkit-box-sizing:border-box;box-sizing:border-box;width:18px;height:18px;background-color:#fff;z-index:1;-webkit-transition:border-color .25s cubic-bezier(.71,-.46,.29,1.46),background-color .25s cubic-bezier(.71,-.46,.29,1.46);transition:border-color .25s cubic-bezier(.71,-.46,.29,1.46),background-color .25s cubic-bezier(.71,-.46,.29,1.46);vertical-align:middle}@keyframes dialogShow-data-v-728c0e75{0%{bottom:-100%}to{bottom:0}}@-webkit-keyframes dialogShow-data-v-728c0e75{0%{bottom:-100%}to{bottom:0}}.switch[data-v-728c0e75]{display:inline-block;text-align:start;text-indent:0;text-transform:none;text-shadow:none;word-spacing:normal;letter-spacing:normal;cursor:pointer;-webkit-tap-highlight-color:transparent;-webkit-rtl-ordering:logical;-webkit-user-select:text;text-rendering:auto;-webkit-writing-mode:horizontal-tb;position:relative;-webkit-box-sizing:border-box;box-sizing:border-box;outline:0;border:1px solid #d2d2d2;background-color:#d2d2d2;-webkit-appearance:none;-moz-appearance:none;appearance:none;vertical-align:middle}.small-switch[data-v-728c0e75]{width:45px;height:24px;border-radius:14px}.switch[data-v-728c0e75]:before{position:absolute;top:0;left:0;background-color:#d2d2d2;text-align:right;-webkit-transition:-webkit-transform .3s;transition:-webkit-transform .3s;transition:transform .3s;transition:transform .3s,-webkit-transform .3s}.small-switch[data-v-728c0e75]:before{width:21px;height:21px;border-radius:15px;line-height:27px;padding-right:10px}.switch[data-v-728c0e75]:after{position:absolute;top:0;left:0;background-color:#fff;-webkit-box-shadow:0 1px 3px rgba(0,0,0,.4);box-shadow:0 1px 3px rgba(0,0,0,.4);content:"";-webkit-transition:-webkit-transform .3s;transition:-webkit-transform .3s;transition:transform .3s;transition:transform .3s,-webkit-transform .3s}.small-switch[data-v-728c0e75]:after{width:21px;height:21px;border-radius:15px;line-height:22px;padding-right:10px}.small-switch[data-v-728c0e75]:checked:after{width:21px;height:21px;border-radius:20px;-webkit-transform:translateX(21px);transform:translateX(21px)}.style-main-bg[data-v-728c0e75]{background:#e4393c;color:#fff}.style-main-font[data-v-728c0e75]{color:#e4393c}.style-main-border[data-v-728c0e75]{color:#e4393c;border:1px solid #e4393c}.style-witch[data-v-728c0e75]:checked{border-color:#e4393c;background-color:#e4393c}.style-middle-bg[data-v-728c0e75]{background:#ffb12e;color:#fff}.style-middle-font[data-v-728c0e75]{color:#ffb12e}.style-right-bg[data-v-728c0e75]{background:#fff;color:#000}.style-right-font[data-v-728c0e75]{color:#fff}.sale-wrapper[data-v-728c0e75]{width:100%;position:relative}.sale-wrapper .seller-top[data-v-728c0e75]{position:fixed;top:0;z-index:2;width:100%}.sale-wrapper .seller-top .seller-nav[data-v-728c0e75]{height:.79333333rem;line-height:.79333333rem;padding-left:.33333333rem;background:#f0f2f5;font-size:.3rem}.sale-wrapper .seller-top .seller-nav i[data-v-728c0e75]{color:#b2b2b2;margin-right:.16666667rem}.sale-wrapper .seller-content[data-v-728c0e75]{background:#fff;margin-top:1.8rem;z-index:1;position:relative;border-bottom:1px solid #e0e0e0}.sale-wrapper .seller-content .seller-item[data-v-728c0e75]{height:1.01333333rem;padding:0 .66666667rem 0 .46666667rem}.sale-wrapper .seller-content .seller-item .item-left[data-v-728c0e75],.sale-wrapper .seller-content .seller-item[data-v-728c0e75]{display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify;-webkit-box-align:center;-o-box-align:center;box-align:center}.sale-wrapper .seller-content .seller-item .item-left[data-v-728c0e75]{width:90%}.sale-wrapper .seller-content .seller-item .item-left .index-div[data-v-728c0e75]{width:5%}.sale-wrapper .seller-content .seller-item .item-left div[data-v-728c0e75]{font-size:.26666667rem}.sale-wrapper .seller-content .seller-item .item-left .img-div[data-v-728c0e75]{width:.8rem;height:.8rem;background-position:50%;background-size:cover;border-radius:100%;overflow:hidden;margin:0 .6rem 0 .46666667rem}.sale-wrapper .seller-content .seller-item .item-left .text-overflow[data-v-728c0e75]{width:60%}.sale-wrapper .seller-content .seller-item .item-right[data-v-728c0e75]{width:10%;font-size:.26666667rem}.sale-wrapper .blue-color[data-v-728c0e75]{color:#4e95ef}',""])},f3qj:function(e,t,a){"use strict";var o=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("section",{staticClass:"more-main"},[a("p",{directives:[{name:"show",rawName:"v-show",value:2==e.isMore,expression:"isMore == 2"}]},[e._v("加载中，请稍等……")]),e._v(" "),a("p",{directives:[{name:"show",rawName:"v-show",value:3==e.isMore,expression:"isMore == 3"}]},[e._v("抱歉,没有更多了")])])},i=[],r={render:o,staticRenderFns:i};t.a=r},jHJh:function(e,t,a){var o=a("GqgL");"string"==typeof o&&(o=[[e.i,o,""]]),o.locals&&(e.exports=o.locals);a("8bSs")("0654995c",o,!0)},kr9m:function(e,t,a){"use strict";function o(e){a("NZon")}var i=a("lGvr"),r=a("Pzjh"),s=a("0HdQ"),d=o,n=s(i.a,r.a,!1,d,"data-v-f4ba2e06",null);t.a=n.exports},lGvr:function(e,t,a){"use strict";t.a={props:["background","isHeadPortrait"],data:function(){return{}},mounted:function(){}}},mhRB:function(e,t,a){"use strict";t.a={props:["headers","status","selectId","isCanSelect"],data:function(){return{selectNav:""}},mounted:function(){this.selectNav=this.$route.params.type||0},watch:{$route:function(){this.selectNav=this.$route.params.type||0,this.onValue(this.selectNav)}},methods:{selects:function(e){if(-1!=e){if(!this.isCanSelect)return void this.$emit("isCanSelect",e);this.selectNav=e,this.onValue(e)}},onValue:function(e){this.$emit("update:type",e),this.$emit("type-change",e)}}}},uotU:function(e,t,a){"use strict";function o(e){a("jHJh")}var i=a("OWAf"),r=a.n(i),s=a("f3qj"),d=a("0HdQ"),n=o,c=d(r.a,s.a,!1,n,null,null);t.a=c.exports},vl5z:function(e,t,a){e.exports=a.p+"static/img/kuaidiyuan.21c5411.png"}});