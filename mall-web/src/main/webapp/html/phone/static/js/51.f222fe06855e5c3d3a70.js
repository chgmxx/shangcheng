webpackJsonp([51],{"0eeV":function(t,o,e){o=t.exports=e("BkJT")(!1),o.push([t.i,".ik-box[data-v-692b52ba]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-692b52ba]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}.text-overflow[data-v-692b52ba]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.imgbox[data-v-692b52ba],.user-head-portrait[data-v-692b52ba]{width:100%;height:100%;position:relative}.user-head-portrait[data-v-692b52ba]{background-size:100%;background-repeat:no-repeat;background-position:50%;z-index:1}.default-img[data-v-692b52ba]{width:100%;height:100%;position:absolute;top:0;left:0;z-index:0;line-height:1;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:center;-o-box-pack:center;box-pack:center;-webkit-box-align:center;-o-box-align:center;box-align:center}.default-img .iconfont[data-v-692b52ba]{font-size:1.33333333rem;color:#fff}",""])},"13px":function(t,o,e){"use strict";o.a={props:["background","isHeadPortrait"],data:function(){return{}},mounted:function(){}}},"20qY":function(t,o,e){"use strict";var n=e("kr9m");o.a={components:{defaultImg:n.a},data:function(){return{countMap:{hao:0,zhong:0,cha:0},commentList:"",isShow:-1,textNo:"",imgUrl:"",path:""}},methods:{commentAjax:function(t,o,e){var n=this;n.textNo=o||"评论",0!==e&&n.ajaxRequest({url:h5App.activeAPI.phoneProduct_getProductComment_post,data:{busId:n.$route.params.busId,productId:n.$route.params.goodsId,feel:t||""},success:function(t){if(!t.data.commentList)return void(n.isShow=0);if(n.isShow=1,n.imgUrl=t.imgUrl,n.path=t.path,t.data.commentList.forEach(function(o,e){o.replyContent&&(t.data.commentList[e].replyContent=n.commonFn.emoji(o.replyContent,n.path))}),o)return void(n.commentList=t.data.commentList);n.commentList=t.data.commentList;var e=t.data.countMap;n.countMap={hao:e.hao||0,zhong:e.zhong||0,cha:e.cha||0},n.countMap.all=n.countMap.hao+n.countMap.zhong+n.countMap.cha||0,console.log(n.countMap,"2countMap")}})}},mounted:function(){this.commentAjax()}}},HOXF:function(t,o,e){o=t.exports=e("BkJT")(!1),o.push([t.i,'.ik-box{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}.text-overflow{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.comment-main{width:100%;position:relative}.comment-main .comment-nav{height:.77333333rem;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify;-webkit-box-align:center;-o-box-align:center;box-align:center;padding:0 .29333333rem}.comment-main .comment-no{width:100%;text-align:center;padding-top:.72666667rem;padding-bottom:.30666667rem}.comment-main .comment-content{width:100%;background:#fff}.comment-main .comment-content .comment-item{width:100%;padding:.26666667rem .18rem}.comment-main .comment-content .comment-user-img{width:.85333333rem;height:.85333333rem;background-size:cover;background-position:50%;float:left;border-radius:100%;overflow:hidden}.comment-main .comment-content .comment-user-fr{width:85%;float:right;padding:.1rem 0}.comment-main .comment-content .comment-user-name{width:100%}.comment-main .comment-content .comment-txt{width:100%;padding:.13333333rem 0}.comment-main .comment-content .comment-photo{max-width:200%;overflow-x:auto;-webkit-overflow-scrolling:touch;font-size:0;padding-bottom:.13333333rem;height:2.2rem;display:-webkit-box;display:-moz-box;display:-o-box;display:box;position:relative}.comment-main .comment-content .comment-photo .comment-img{width:2rem;height:2rem;border-radius:8px;background-size:cover;background-position:50%;margin-right:.2rem;border:1px solid #eee}.comment-main .comment-content .comment-choice{width:100%;font-size:0}.comment-main .comment-content .comment-choice span{margin-right:.38666667rem}.comment-main .comment-content .comment-reply{width:100%;margin-top:.32rem;background:#f2f2f2;padding:.2rem;text-align:justify;border-radius:2px;position:relative}.comment-main .comment-content .comment-reply:after{content:" ";position:absolute;top:-.13333333rem;left:.18666667rem;width:0;height:0;border-left:10px solid transparent;border-right:10px solid transparent;border-bottom:10px solid #f2f2f2}.comment-main .comment-content .comment-reply img{width:.28rem;height:.28rem}::-webkit-scrollbar{width:0;height:0;opacity:0}.scrollbar{scrollbar-face-color:#fff;scrollbar-arrow-color:#fff;scrollbar-highlight-color:#fff;scrollbar-3dlight-color:#fff;scrollbar-shadow-color:#fff;scrollbar-darkshadow-color:#fff;scrollbar-track-color:#fff;scrollbar-base-color:#fff}',""])},"W+QN":function(t,o,e){"use strict";function n(t){e("mIrn")}Object.defineProperty(o,"__esModule",{value:!0});var a=e("20qY"),i=e("sSJj"),c=e("8AGX"),s=n,r=c(a.a,i.a,!1,s,null,null);o.default=r.exports},hnjg:function(t,o,e){var n=e("0eeV");"string"==typeof n&&(n=[[t.i,n,""]]),n.locals&&(t.exports=n.locals);e("8bSs")("0edca35a",n,!0)},kr9m:function(t,o,e){"use strict";function n(t){e("hnjg")}var a=e("13px"),i=e("lJoL"),c=e("8AGX"),s=n,r=c(a.a,i.a,!1,s,"data-v-692b52ba",null);o.a=r.exports},lJoL:function(t,o,e){"use strict";var n=function(){var t=this,o=t.$createElement,e=t._self._c||o;return e("div",{staticClass:"imgbox"},[e("div",{staticClass:"user-head-portrait",style:{backgroundImage:"url("+t.background+")"}}),t._v(" "),0==t.isHeadPortrait&&void 0==t.background?e("div",{staticClass:"default-img"},[e("i",{staticClass:"iconfont icon-tupianjiazaizhong-",staticStyle:{color:"#d6d6d6"}})]):t._e(),t._v(" "),1==t.isHeadPortrait&&void 0==t.background?e("div",{staticClass:"default-img"},[e("i",{staticClass:"iconfont icon-ren1"})]):t._e()])},a=[],i={render:n,staticRenderFns:a};o.a=i},mIrn:function(t,o,e){var n=e("HOXF");"string"==typeof n&&(n=[[t.i,n,""]]),n.locals&&(t.exports=n.locals);e("8bSs")("6ae1c5a7",n,!0)},sSJj:function(t,o,e){"use strict";var n=function(){var t=this,o=t.$createElement,e=t._self._c||o;return e("div",{staticClass:"comment-main"},[1==t.isShow?e("div",{staticClass:"comment-nav border"},[e("div",{staticClass:"em-nav fs36",on:{click:function(o){t.commentAjax("","评论",t.countMap.all)}}},[t._v("全部("+t._s(t.countMap.all)+")")]),t._v(" "),e("div",{staticClass:"em-nav fs36",on:{click:function(o){t.commentAjax(1,"好评",t.countMap.hao)}}},[t._v("好评("+t._s(t.countMap.hao)+")")]),t._v(" "),e("div",{staticClass:"em-nav fs36",on:{click:function(o){t.commentAjax(0,"中评",t.countMap.zhong)}}},[t._v("中评("+t._s(t.countMap.zhong)+")")]),t._v(" "),e("div",{staticClass:"em-nav fs36",on:{click:function(o){t.commentAjax(-1,"差评",t.countMap.cha)}}},[t._v("差评("+t._s(t.countMap.cha)+")")])]):t._e(),t._v(" "),0==t.isShow?e("div",{staticClass:"comment-no"},[e("p",{staticClass:"fs40 shopGray"},[t._v("暂无"+t._s(t.textNo))])]):t._e(),t._v(" "),1==t.isShow?e("div",{staticClass:"comment-content"},t._l(t.commentList,function(o){return e("div",{staticClass:"comment-item border"},[e("div",{staticClass:"comment-user clearfix"},[e("div",{staticClass:"comment-user-img"},[e("default-img",{attrs:{background:o.headimgurl,isHeadPortrait:1}})],1),t._v(" "),e("div",{staticClass:"comment-user-fr"},[e("p",{staticClass:"comment-user-name fs40"},[t._v(t._s(o.nickname))]),t._v(" "),e("p",{staticClass:"comment-user-time fs32 shopGray"},[t._v(t._s(o.createTime))])])]),t._v(" "),e("div",{staticClass:"comment-txt fs40"},[t._v("\n                "+t._s(o.content)+"\n            ")]),t._v(" "),1==o.is_upload_image?e("div",{staticClass:"comment-photo scrollbar clearfix"},t._l(o.imageList,function(o){return e("div",{staticClass:"comment-img"},[e("default-img",{attrs:{background:t.imgUrl+o.imageUrl,isHeadPortrait:0}})],1)})):t._e(),t._v(" "),o.product_specificas?e("div",{staticClass:"comment-choice"},t._l(o.specList,function(o){return e("span",{staticClass:"fs36 shopGray"},[t._v("\n                    "+t._s(o.name)+": "+t._s(o.value)+" \n                  ")])})):t._e(),t._v(" "),1==o.is_rep?e("div",{staticClass:"comment-reply fs32",domProps:{innerHTML:t._s("[商家回复]:"+o.replyContent)}}):t._e()])})):t._e()])},a=[],i={render:n,staticRenderFns:a};o.a=i}});