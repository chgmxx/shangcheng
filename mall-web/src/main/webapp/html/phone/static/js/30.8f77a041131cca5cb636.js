webpackJsonp([30],{"4T/n":function(t,e,a){"use strict";var i=a("E1vw"),o=a("a58N");e.a={name:"apply",data:function(){return{busId:this.$route.params.busId||sessionStorage.getItem("busId"),pfApplayRemark:"",name:"",companyName:"",telephone:"",remark:"",code:"",isSend:!1,waitTime:60,getCodeMsg:this.$t("get_validate_code_msg"),selectArea:""}},components:{shopDialog:o.a,areaCode:i.a},mounted:function(){this.loadDatas(),this.commonFn.setTitle(this.$t("title.title_pifa_apply_msg")),this.$store.commit("show_footer",!1)},beforeDestroy:function(){this.$store.commit("show_footer",!0)},methods:{loadDatas:function(){var t=this,e={busId:t.busId,url:location.href,browerType:t.$store.state.browerType};t.ajaxRequest({url:h5App.activeAPI.get_pf_apply_remark_post,data:e,success:function(e){var a=e.data;null!=a&&(t.pfApplayRemark=a.pfApplayRemark)}})},time:function(){var t=this,e=this.waitTime;0==e?(this.getCodeMsg=this.$t("get_validate_code_msg"),e=60):(this.getCodeMsg=e+"秒后重发",this.waitTime--,setTimeout(function(){t.time()},1e3))},getPhoneCode:function(){var t=this;if(this.blurValidate(this.telephone,3)&&!(this.waitTime>0&&this.waitTime<60)){var e={busId:this.busId,mobile:this.telephone,areaCode:this.selectArea.areacode};this.time(),this.ajaxRequest({url:h5App.activeAPI.pifa_send_msg_post,data:e,loading:!0,success:function(e){t.$store.commit("is_show_loading",!1)}})}},submitApply:function(){var t=this,e=this.blurValidate;if(e(this.name,1)&&e(this.companyName,2)&&e(this.telephone,3)&&e(this.code,4)&&e(this.remark,5)){var a={busId:t.busId,url:location.href,browerType:t.$store.state.browerType,name:t.name,companyName:t.companyName,telephone:t.selectArea.areacode+","+t.telephone,remark:t.remark,code:t.code};this.ajaxRequest({url:h5App.activeAPI.add_pifa_post,data:a,loading:!0,success:function(e){t.showDialogs(),t.$store.commit("is_show_loading",!1)}})}},showDialogs:function(){var t=this,e={btnNum:"1",dialogTitle:this.$t("submit_success_msg"),dialogMsg:this.$t("submit_apply_wait_msg"),btnOne:this.$t("good_msg"),callback:{btnOne:function(){t.toReturnMyApp()}}};t.$parent.$refs.dialog.showDialog(e)},blurValidate:function(t,e){var a=this.$parent.$refs.bubble.show_tips,i=this.commonFn,o=i.isNull;if(1==e){if(o(t))return a(this.$t("pifa_name_msg")),!1;if(t.length>25)return a(this.$t("pifa_name_length_msg")),!1}else if(2==e){if(o(t))return a(this.$t("pifa_company_name_msg")),!1;if(t.length>50)return a(this.$t("pifa_company_name_length_msg")),!1}else if(3==e){if(o(t)||!i.validPhone(t))return a(this.$t("pifa_telePhone_msg")),!1}else if(4==e){if(o(t))return a(this.$t("pifa_validate_code_msg")),!1}else if(5==e&&t.length>100)return a(this.$t("pifa_remark_length_msg")),!1;return!0},toReturnMyApp:function(){var t=this.$route.params.busId;this.$router.push("/my/center/"+t)},changeArea:function(t){this.selectArea=t}}}},E1vw:function(t,e,a){"use strict";function i(t){a("jcjX")}var o=a("NM2d"),s=a("v0nV"),n=a("0HdQ"),l=i,p=n(o.a,s.a,!1,l,"data-v-dea85248",null);e.a=p.exports},NM2d:function(t,e,a){"use strict";e.a={props:{dataStyle:{type:[Object,Number],default:0}},components:{},data:function(){return{content:"",codeArr:null,isShow:!1,style:{},select:null,defaultData:{}}},watch:{select:function(t,e){null==t&&(this.select=e),this.$emit("selectCode",this.select)}},computed:{searchData:function(){var t=this;return null!=t.content&&""!=t.content?t.codeArr.filter(function(e){return-1!==e.country.indexOf(t.content)||-1!==e.englishname.indexOf(t.content)}):this.codeArr}},mounted:function(){0!=this.dataStyle&&(this.style=this.dataStyle),this.load()},methods:{load:function(){var t=this;t.ajaxRequest({url:h5App.activeAPI.areaPhoneList_post,loading:!0,success:function(e){t.$store.commit("is_show_loading",!1),t.codeArr=e.data,e.data.forEach(function(e,a){"86"==e.areacode&&(t.select=e,t.defaultData=e)})}})},selectCode:function(t){this.isShow=!1,this.select=null==t?null:t}}}},awiy:function(t,e,a){"use strict";function i(t){a("s0uu")}Object.defineProperty(e,"__esModule",{value:!0});var o=a("4T/n"),s=a("ul2+"),n=a("0HdQ"),l=i,p=n(o.a,s.a,!1,l,"data-v-3846f88c",null);e.default=p.exports},crdx:function(t,e,a){e=t.exports=a("BkJT")(!1),e.push([t.i,".code-button[data-v-dea85248]{width:100%;height:100%}.code-button p[data-v-dea85248]{border-right:1px solid #d1d1d5}.code-button p i[data-v-dea85248]{color:#c7c7cc;font-size:12px;padding:0 3px;vertical-align:.03rem}.select-code[data-v-dea85248]{color:#333!important}.mint-popup-3[data-v-dea85248]{width:100%;height:100%}.code-main[data-v-dea85248]{width:100%;height:100%;background:#fff;z-index:100;position:fixed;top:0;left:0;border:solid #dbdbdb;border-top:1px;border-left:1px}.code-main .code-top[data-v-dea85248]{width:100%;height:1.38666667rem;line-height:1.38666667rem;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify;-webkit-box-align:center;-o-box-align:center;box-align:center}.code-main .code-top i[data-v-dea85248],.code-main .code-top input[data-v-dea85248]{display:block}.code-main .code-top input[data-v-dea85248]{width:84%;border:0;padding-left:.44rem}.code-main .code-top i[data-v-dea85248]{width:16%;height:100%;text-align:right;color:#333743;font-weight:bolder;font-size:.53333333rem;padding-right:.49333333rem}.code-main .code-content[data-v-dea85248]{padding-left:.41333333rem}.code-main .code-content .code-item[data-v-dea85248]{height:1.43333333rem;width:100%;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-pack:justify;-o-box-pack:justify;box-pack:justify;-webkit-box-align:center;-o-box-align:center;box-align:center}.code-main .code-content .code-item span[data-v-dea85248]{display:block;color:#333743}.code-main .code-content .code-item .span_left[data-v-dea85248]{width:80%}.code-main .code-content .code-item .span_right[data-v-dea85248]{width:20%;text-align:right;padding-right:.49333333rem}",""])},jcjX:function(t,e,a){var i=a("crdx");"string"==typeof i&&(i=[[t.i,i,""]]),i.locals&&(t.exports=i.locals);a("8bSs")("24c047c5",i,!0)},s0uu:function(t,e,a){var i=a("tlSy");"string"==typeof i&&(i=[[t.i,i,""]]),i.locals&&(t.exports=i.locals);a("8bSs")("fd9e885c",i,!0)},tlSy:function(t,e,a){e=t.exports=a("BkJT")(!1),e.push([t.i,".apply-wapper[data-v-3846f88c]{width:100%;height:100%;position:relative;background:#fff}.apply-wapper .apply-notice[data-v-3846f88c]{font-size:0;width:100%;padding:.13333333rem .33333333rem}.apply-wapper .apply-notice p[data-v-3846f88c]{width:88%;display:inline-block;color:#87858f}.apply-wapper .apply-notice i[data-v-3846f88c]{text-align:center;line-height:.52rem;display:inline-block;width:.53333333rem;height:.53333333rem;font-size:.32rem;background:#10aeff;border-radius:100%;color:#fff;margin-right:.26666667rem;margin-top:.13333333rem;vertical-align:top}.apply-wapper .apply-main[data-v-3846f88c]{width:100%;padding-left:.16666667rem}.apply-wapper .apply-main .apply-item[data-v-3846f88c]{width:100%;min-height:.8rem;display:-webkit-box;display:-moz-box;display:-o-box;display:box;-webkit-box-align:center;-o-box-align:center;box-align:center}.apply-wapper .apply-main .apply-item .apply-title[data-v-3846f88c]{width:20%}.apply-wapper .apply-main .apply-item .apply-txt[data-v-3846f88c]{width:80%}.apply-wapper .apply-main .apply-item .apply-txt input[data-v-3846f88c],.apply-wapper .apply-main .apply-item .apply-txt textarea[data-v-3846f88c]{width:100%;padding:.1rem 0;border:0}.apply-wapper .apply-main .apply-item .apply-txt .apply-tel[data-v-3846f88c]{width:50%;padding-left:.1rem}.apply-wapper .apply-main .apply-item .apply-txt .apply-tel-button[data-v-3846f88c]{display:inline-block;width:30%;margin:.2rem 0;text-align:center;border-left:1px solid #e8e8ed}.apply-wapper .apply-main .apply-buttom[data-v-3846f88c]{margin-top:25%;height:.98666667rem;padding-right:.16666667rem}.apply-wapper .apply-main .apply-buttom p[data-v-3846f88c]{margin-top:.18666667rem;color:#87858f}.table[data-v-3846f88c]{color:#666;width:98%;height:1.46666667rem;border:0;outline:0;word-wrap:break-word;overflow-x:hidden;overflow-y:auto}",""])},"ul2+":function(t,e,a){"use strict";var i=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"shop-wrapper apply-wapper",attrs:{id:"app"}},[t._m(0),t._v(" "),a("section",{staticClass:"apply-main"},[a("div",{staticClass:"apply-form"},[a("div",{staticClass:"apply-item border"},[a("div",{staticClass:"apply-title fs40"},[t._v("\r\n                    姓名\r\n                ")]),t._v(" "),a("div",{staticClass:"apply-txt fs40"},[a("input",{directives:[{name:"model",rawName:"v-model",value:t.name,expression:"name"}],attrs:{placeholder:"请输入本人真实姓名(必填)"},domProps:{value:t.name},on:{blur:function(e){t.blurValidate(t.name,1)},input:function(e){e.target.composing||(t.name=e.target.value)}}})])]),t._v(" "),a("div",{staticClass:"apply-item border"},[a("div",{staticClass:"apply-title fs40"},[t._v("\r\n                    公司名称\r\n                ")]),t._v(" "),a("div",{staticClass:"apply-txt fs40"},[a("input",{directives:[{name:"model",rawName:"v-model",value:t.companyName,expression:"companyName"}],attrs:{placeholder:"请输入公司名称(必填)"},domProps:{value:t.companyName},on:{blur:function(e){t.blurValidate(t.companyName,2)},input:function(e){e.target.composing||(t.companyName=e.target.value)}}})])]),t._v(" "),a("div",{staticClass:"apply-item border"},[a("div",{staticClass:"apply-title fs40"},[t._v("\r\n                    手机号\r\n                ")]),t._v(" "),a("div",{staticClass:"apply-txt fs40"},[a("div",{staticClass:"shop-fl ",staticStyle:{width:"18%"}},[a("area-code",{attrs:{dataStyle:{color:"#666",padding:"0.2rem 0"}},on:{selectCode:t.changeArea}})],1),t._v(" "),a("input",{directives:[{name:"model",rawName:"v-model",value:t.telephone,expression:"telephone"}],staticClass:"apply-tel",attrs:{placeholder:"请输入手机号(必填)"},domProps:{value:t.telephone},on:{blur:function(e){t.blurValidate(t.telephone,3)},input:function(e){e.target.composing||(t.telephone=e.target.value)}}}),t._v(" "),a("span",{staticClass:"apply-tel-button shop-font",domProps:{textContent:t._s(t.getCodeMsg)},on:{click:t.getPhoneCode}},[t._v("\r\n                      获取验证码\r\n                  ")])])]),t._v(" "),a("div",{staticClass:"apply-item border"},[a("div",{staticClass:"apply-title fs40"},[t._v("\r\n                    验证码\r\n                ")]),t._v(" "),a("div",{staticClass:"apply-txt fs40"},[a("input",{directives:[{name:"model",rawName:"v-model",value:t.code,expression:"code"}],attrs:{placeholder:"请输入验证码(必填)"},domProps:{value:t.code},on:{blur:function(e){t.blurValidate(t.code,4)},input:function(e){e.target.composing||(t.code=e.target.value)}}})])]),t._v(" "),a("div",{staticClass:"apply-item border"},[a("div",{staticClass:"apply-title fs40"},[t._v("\r\n                    备注\r\n                ")]),t._v(" "),a("div",{staticClass:"apply-txt fs40"},[a("textarea",{directives:[{name:"model",rawName:"v-model",value:t.remark,expression:"remark"}],staticClass:"table",attrs:{placeholder:"请输入备注"},domProps:{value:t.remark},on:{blur:function(e){t.blurValidate(t.remark,5)},input:function(e){e.target.composing||(t.remark=e.target.value)}}})])])]),t._v(" "),a("div",{staticClass:"apply-buttom fs52"},[a("div",{staticClass:"shop-max-button shop-bg",on:{click:t.submitApply}},[t._v("\r\n                申请\r\n            ")]),t._v(" "),null!=t.pfApplayRemark?a("p",{staticClass:"fs40"},[t._v(t._s(t.pfApplayRemark))]):t._e()])]),t._v(" "),a("section",{staticClass:"shop-footer-ab shop-footer"},[1==t.$store.state.isAdvert?a("technical-support"):t._e()],1),t._v(" "),a("shop-dialog",{ref:"dialog"})],1)},o=[function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("header",{staticClass:"apply-notice"},[a("i",[t._v("i")]),t._v(" "),a("p",{staticClass:"fs40"},[t._v("基本信息的有效性决定您能否成为批发商，以及最终可获得 的折扣")])])}],s={render:i,staticRenderFns:o};e.a=s},v0nV:function(t,e,a){"use strict";var i=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticStyle:{width:"100%"}},[a("div",{staticClass:"code-button",style:t.dataStyle,on:{click:function(e){t.isShow=!0}}},[null!=t.select?a("p",[t._v(t._s("+"+t.select.areacode)+"\n      "),a("i",{staticClass:"iconfont icon-jiantou fs50"})]):t._e()]),t._v(" "),a("mt-popup",{staticClass:"mint-popup-3",attrs:{position:"right",modal:!1},model:{value:t.isShow,callback:function(e){t.isShow=e},expression:"isShow"}},[a("div",{staticClass:"code-main"},[a("div",{staticClass:"code-top border"},[a("input",{directives:[{name:"model",rawName:"v-model",value:t.content,expression:"content"}],staticClass:"fs50",attrs:{placeholder:"搜索国家/地区"},domProps:{value:t.content},on:{input:function(e){e.target.composing||(t.content=e.target.value)}}}),t._v(" "),a("i",{staticClass:"iconfont icon-guanbi",on:{click:function(e){t.selectCode(null)}}})]),t._v(" "),null!=t.searchData?a("div",{staticClass:"code-content"},t._l(t.searchData,function(e,i){return a("div",{key:i,staticClass:"border code-item",on:{click:function(a){t.selectCode(e)}}},[a("span",{staticClass:"fs52 span_left"},[t._v(t._s(e.country))]),t._v(" "),a("span",{staticClass:"fs52 span_right"},[t._v("+"+t._s(e.areacode))])])})):t._e()])])],1)},o=[],s={render:i,staticRenderFns:o};e.a=s}});