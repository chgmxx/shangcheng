webpackJsonp([55],{"/CAm":function(t,s,a){"use strict";function e(t){a("sURt")}Object.defineProperty(s,"__esModule",{value:!0});var o={components:{},data:function(){return{isShow:-1,specData:""}},methods:{specAjax:function(){var t=this;t.textNo=name,t.ajaxRequest({status:!1,url:h5App.activeAPI.phoneProduct_getProductParams_post,data:{productId:t.$route.params.goodsId},success:function(s){if(1==s.code||!s.data.length>0)return void(t.isShow=0);t.isShow=1,t.specData=s.data}})}},mounted:function(){this.specAjax()}},p=function(){var t=this,s=t.$createElement,a=t._self._c||s;return a("div",{staticClass:"spec-wrapper"},[0==t.isShow?a("div",{staticClass:"spec-no "},[a("p",{staticClass:"fs40 shopGray"},[t._v("暂无规格")])]):t._e(),t._v(" "),1==t.isShow?a("div",{staticClass:"spec-content"},t._l(t.specData,function(s){return a("div",{staticClass:"spec-box"},[a("p",{staticClass:"spec-list boder"},[a("span",{staticClass:"fs40"},[t._v(t._s(s.paramsName))]),t._v(" "),a("span",{staticClass:"fs40"},[t._v(t._s(s.paramsValue))])])])})):t._e()])},d=[],i={render:p,staticRenderFns:d},c=i,r=a("8AGX"),n=e,l=r(o,c,!1,n,"data-v-6a18d845",null);s.default=l.exports},XlKx:function(t,s,a){s=t.exports=a("BkJT")(!1),s.push([t.i,".ik-box[data-v-6a18d845]{display:-webkit-box;display:-moz-box;display:-o-box;display:box}.box-sizing[data-v-6a18d845]{box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box}.text-overflow[data-v-6a18d845]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.spec-wrapper .spec-no[data-v-6a18d845]{width:100%;text-align:center;padding-top:.72666667rem;padding-bottom:.30666667rem}.spec-wrapper .spec-content[data-v-6a18d845]{background:#fff;padding:.2rem}.spec-wrapper .spec-content .spec-box[data-v-6a18d845]{width:100%;border:1px solid #ddd;border-bottom:0}.spec-wrapper .spec-content .spec-box[data-v-6a18d845]:last-child{border-bottom:1px solid #ddd}.spec-wrapper .spec-content .spec-list[data-v-6a18d845]{width:100%}.spec-wrapper .spec-content .spec-list span[data-v-6a18d845]{display:inline-block;padding:.2rem}.spec-wrapper .spec-content .spec-list span[data-v-6a18d845]:first-of-type{width:25%;border-right:1px solid #ddd}.spec-wrapper .spec-content .spec-list span[data-v-6a18d845]:last-of-type{width:75%}",""])},sURt:function(t,s,a){var e=a("XlKx");"string"==typeof e&&(e=[[t.i,e,""]]),e.locals&&(t.exports=e.locals);a("8bSs")("1277cf12",e,!0)}});