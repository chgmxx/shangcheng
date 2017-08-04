/**
 * Created by zhangmz on 2017/6/9.
 */
(function () {

  var DFLodop = (function () {
    "use strict";

    /*** LODOP ***/
    var DF_Lodop = null;
    var LODOP_READY_STATUS = 'init';
    var LODOP_VERSION = '6.2.1.8';// Lodop 版本
    var CLODOP_VERSION = '2.1.3.0';// CLodop 版本
    var IS_OS_WIN = navigator.userAgent.indexOf('Windows') >= 0;
    var DOWN_URL = {
      LODOP32: 'http://maint.duofriend.com/upload/image/common/driver/install_lodop32.zip',
      // LODOP32: '/file/lodop_driver/install_lodop32.exe',
      LODOP64: 'http://maint.duofriend.com/upload/image/common/driver/install_lodop64.zip',
      // LODOP64: '/file/lodop_driver/install_lodop64.exe',
      CLODOP32: 'http://maint.duofriend.com/upload/image/common/driver/CLodop.zip '
      // CLODOP32: '/file/lodop_driver/CLodop_Setup_for_Win32NT.exe'
    };

    function lodopReady(callback) {
      var readyFunc = function () {
        if (callback && typeof callback == 'function') {
          callback(DF_Lodop);
        }
      };
      if (LODOP_READY_STATUS != 'success') {
        LodopEvents.addEvent('LodopReady', readyFunc);
      } else {
        readyFunc();
      }
    }

    /**
     * 获取打印机设备列表
     * @param selectId 下拉框ID
     *
     */
    function getPrinterList(selectId) {
      var LODOP = getLodop();
      if (LODOP == undefined) {
        alert("LODOP 驱动未安装或启动");
        return;
      }
      LODOP.Create_Printer_List(document.getElementById(selectId));
    }

    /**
     * 获取当前打印机设备纸张大小
     * @param selectId 下拉框ID
     * @param printerIndex 打印机序号
     */
    function getPrinterPageSize(selectId, printerIndex) {
      var LODOP = getLodop();
      if (LODOP == undefined) {
        alert("LODOP 驱动未安装或启动");
        return;
      }
      LODOP.Create_PageSize_List(document.getElementById(selectId), printerIndex);
    }

    /**
     * 本函数根据浏览器类型决定采用哪个页面元素作为Lodop对象：
     * IE系列、IE内核系列的浏览器采用oOBJECT，
     * 其它浏览器(Firefox系列、Chrome系列、Opera系列、Safari系列等)采用oEMBED,
     * 如果页面没有相关对象元素，则新建一个或使用上次那个,避免重复生成。
     * 64位浏览器指向64位的安装程序install_lodop64.exe。
     * 对应32位和64位浏览器（这里是浏览器位数不是操作系统位数）
     * 首先确认chrome版本号必须<v45,否者无解;另,部分插件不支持64位版的chrome,请注意
     * 进入chrome:flags#enable-npapi  ,将其启用后,重启chrome,然后进入chrome:plugins  ,启用相关插件即可
     */
    //====获取LODOP对象的主过程：====
    function getLodop(oOBJECT, oEMBED) {
      var CreatedOKLodop7766 = null;

      var style = "<style>.print-top{position: absolute;top: 0;left: 0;width: 100%;height: 40px;line-height:40px;text-align:center;background-color: red; color: #fff;} body{padding-top: 30px;}.print-top a{color: #01b9fe;padding: 0 2px;text-decoration: underline;}</style>";
      var strHtmInstall = "<div class='print-top error-block'>打印控件未安装!点击这里<a class='text-info' href='" + DOWN_URL.LODOP32 + "'>执行安装</a>,安装后请刷新页面或重新进入即可。</div>" + style;
      var strHtmUpdate = "<div class='print-top error-block'>打印控件需要升级!点击这里<a class='text-info' href='" + DOWN_URL.LODOP32 + "'>执行升级</a>,升级后请重新进入。</div>" + style;
      var strHtm64_Install = "<div class='print-top error-block'>打印控件未安装!点击这里<a class='text-info' href='" + DOWN_URL.LODOP64 + "'>执行安装</a>,安装后请刷新页面或重新进入即可</div>" + style;
      var strHtm64_Update = "<div class='print-top error-block'>打印控件需要升级!点击这里<a class='text-info' href='" + DOWN_URL.LODOP64 + "'>执行升级</a>,升级后请重新进入。</div>" + style;
      var strHtmFireFox = "<div class='print-top error-block'>注意：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它。</div>" + style;
      var strHtmChrome = "<div class='print-top error-block'>(如果此前正常，仅因浏览器升级或重安装而出问题，需重新执行以上安装）</div>";
      var strCLodopInstall = "<div class='print-top error-block'>CLodop云打印服务未安装启动!点击这里<a class='text-info' href='" + DOWN_URL.CLODOP32 + "'>执行安装</a>,安装后请刷新页面。</div>" + style;
      var strCLodopUpdate = "<div class='print-top error-block'>CLodop云打印服务需升级!点击这里<a class='text-info' href='" + DOWN_URL.CLODOP32 + "'>执行升级</a>,升级后请刷新页面。</div>" + style;

      var LODOP;
      try {
        //=====判断浏览器类型:===============
        var isIE = navigator.userAgent.indexOf('MSIE') >= 0
            || navigator.userAgent.indexOf('Trident') >= 0;
        var notice = document.createElement("span");
        if (IS_NEED_CLODOP) {
          if (CLODOP_READY_STATUS == 'success') {
            try {
              LODOP = getCLodop();
              console.log('使用CLodop 版本:' + LODOP.CVERSION);
            } catch (err) {
              console.log(err)
            }
            if (!LODOP && document.readyState !== "complete") {
              alert("C-Lodop没准备好，请稍后再试！");
              return;
            }
          }
          if (!LODOP) {
            notice.innerHTML = strCLodopInstall;
            document.documentElement.appendChild(notice);
            return null;
          } else {
            if (CLODOP.CVERSION < CLODOP_VERSION) {
              notice.innerHTML = strCLodopUpdate;
              document.documentElement.appendChild(notice);
            }
            if (oEMBED && oEMBED.parentNode) {
              oEMBED.parentNode.removeChild(oEMBED);
            }
            if (oOBJECT && oOBJECT.parentNode) {
              oOBJECT.parentNode.removeChild(oOBJECT);
            }
          }
        } else {
          var is64IE = isIE && (navigator.userAgent.indexOf('x64') >= 0);
          //=====如果页面有Lodop就直接使用，没有则新建:==========
          if (oOBJECT != undefined || oEMBED != undefined) {
            if (isIE) {
              LODOP = oOBJECT;
            } else {
              LODOP = oEMBED;
            }
          } else {
            if (CreatedOKLodop7766 == null) {
              LODOP = document.createElement("object");
              LODOP.setAttribute("width", 0);
              LODOP.setAttribute("height", 0);
              LODOP.setAttribute("style",
                  "position:absolute;left:0px;top:-100px;width:0px;height:0px;");
              if (isIE) {
                LODOP.setAttribute("classid",
                    "clsid:2105C259-1E0C-4534-8141-A753534CB4CA");
              } else {
                LODOP.setAttribute("type", "application/x-print-lodop");
              }
              document.documentElement.appendChild(LODOP);
              CreatedOKLodop7766 = LODOP;
            } else {
              LODOP = CreatedOKLodop7766;
            }
          }
          //=====判断Lodop插件是否安装过，没有安装或版本过低就提示下载安装:==========
          if (LODOP == null || typeof(LODOP.VERSION) == "undefined") {
            if (navigator.userAgent.indexOf('Chrome') >= 0) {
              // document.documentElement.innerHTML=strHtmChrome+document.documentElement.innerHTML;
            }
            if (navigator.userAgent.indexOf('Firefox') >= 0) {
              // document.documentElement.innerHTML=strHtmFireFox+document.documentElement.innerHTML;
            }
            if (is64IE) {
              notice.innerHTML = strHtm64_Install;
            } else if (isIE) {
              notice.innerHTML = strHtmInstall;
            } else {
              notice.innerHTML = strHtmInstall;
            }
            document.documentElement.appendChild(notice);
            return null;
          }

          if (LODOP.VERSION < LODOP_VERSION) {
            console.log('当前版本:' + LODOP.VERSION + ',最新版本:' + LODOP_VERSION);
            if (is64IE) {
              notice.innerHTML = strHtm64_Update;
            } else if (isIE) {
              notice.innerHTML = strHtmUpdate;
            } else {
              notice.innerHTML = strHtmUpdate;
            }
            document.documentElement.appendChild(notice);
            return LODOP;
          }
        }

        //=====如下空白位置适合调用统一功能(如注册码、语言选择等):====
        LODOP.SET_LICENSES("广东谷通科技有限公司", "13601BD5AB0D5B544607D05CDFBE1565", "",
            "");
        //============================================================
        return LODOP;
      } catch (err) {
        if (is64IE) {
          notice.innerHTML = "Error:" + strHtm64_Install
              + document.documentElement.innerHTML;
        } else {
          notice.innerHTML = "Error:" + strHtmInstall
              + document.documentElement.innerHTML;
        }
        document.documentElement.appendChild(notice);
        return LODOP;
      }
    }

    function LodopInit() {
      var LODOP = getLodop();
      if (LODOP == null || typeof(LODOP.VERSION) == "undefined") {
        // 未安装
        console.warn('未安装Lodop打印机控件或需要升级');
      } else {
        // 已安装
        DF_Lodop = LODOP;
        LODOP_READY_STATUS = 'success';
        LodopEvents.fireEvent('LodopReady');
        console.log('已安装Lodop打印机控件, 版本:' + LODOP.VERSION);
      }
    }

    /*** CLODOP ***/
    var CLODOP_READY_STATUS = 'init';
    // 是否开启clodop云打印
    var IS_USE_CLODOP = true;
    var CLODOP_JS_URL = {
      REMOTE: '/CLodopfuncs.js',
      LOCAL: 'http://localhost:18000/CLodopfuncs.js?priority=0'
    };
    var CLODOP_JS_STATUS = {
      REMOTE: null,
      LOCAL: null
    };
    //====判断是否需要安装CLodop云打印服务器:====
    var IS_NEED_CLODOP = (function () {
      if (!IS_USE_CLODOP) {
        return false;
      }

      try {
        var ua = navigator.userAgent;
        if (ua.match(/Windows\sPhone/i) != null) {
          return true;
        }
        if (ua.match(/iPhone|iPod/i) != null) {
          return true;
        }
        if (ua.match(/Android/i) != null) {
          return true;
        }
        if (ua.match(/Edge\D?\d+/i) != null) {
          return true;
        }

        var verTrident = ua.match(/Trident\D?\d+/i);
        var verIE = ua.match(/MSIE\D?\d+/i);
        var verOPR = ua.match(/OPR\D?\d+/i);
        var verFF = ua.match(/Firefox\D?\d+/i);
        var x64 = ua.match(/x64/i);
        if ((verTrident == null) && (verIE == null) && (x64 !== null)) {
          return true;
        } else if (verFF !== null) {
          verFF = verFF[0].match(/\d+/);
          if ((verFF[0] >= 42) || (x64 !== null)) {
            return true;
          }
        } else if (verOPR !== null) {
          verOPR = verOPR[0].match(/\d+/);
          if (verOPR[0] >= 32) {
            return true;
          }
        } else if ((verTrident == null) && (verIE == null)) {
          var verChrome = ua.match(/Chrome\D?\d+/i);
          if (verChrome !== null) {
            verChrome = verChrome[0].match(/\d+/);
            if (verChrome[0] >= 42) {
              return true;
            }
          }
          ;
        }
        ;
        return false;
      } catch (err) {
        return true;
      }
      ;
    })();

    function clodopReady(callback) {
      var readyFunc = function () {
        LodopEvents.removeEvent('CLodopReady');
        if (callback && typeof callback == 'function') {
          callback();
        }
      };
      if (CLODOP_READY_STATUS != 'success') {
        LodopEvents.addEvent('CLodopReady', readyFunc);
      } else {
        readyFunc();
      }
    }

    function CLodopInit(type, status) {
      try {
        // local加载成功
        if (CLODOP_JS_STATUS.LOCAL == true) {
          CLODOP_READY_STATUS = 'success';
          LodopEvents.fireEvent('CLodopReady');
        } else {
          CLODOP_READY_STATUS = 'fail';
          LodopEvents.fireEvent('CLodopReady');
        }
      } catch (err) {
        console.warn(err);
        CLODOP_READY_STATUS = 'fail';
        LodopEvents.fireEvent('CLodopReady');
      }
    }

    //====页面引用CLodop云打印必须的JS文件：====
    function CLodopLoad() {
      //让其它电脑的浏览器通过本机打印：TODO 暂不实现
      //loadScript(CLODOP_JS_URL.REMOTE, function(){
      //    CLODOP_JS_STATUS.REMOTE = true;
      //    CLodopInit('remote');
      //}, function(){
      //    CLODOP_JS_STATUS.REMOTE = false;
      //    CLodopInit('remote');
      //});
      /*
       var head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
       var oscript = document.createElement("script");
       oscript.src = "http://localhost:8000/CLodopfuncs.js?priority=1";
       head.insertBefore(oscript, head.firstChild);

       //引用双端口(8000和18000）避免其中某个被占用：
       oscript = document.createElement("script");
       oscript.src = "http://localhost:18000/CLodopfuncs.js?priority=0";
       head.insertBefore(oscript, head.firstChild);
       */
      //让本机浏览器打印(更优先)：
      loadScript(CLODOP_JS_URL.LOCAL, function () {
        CLODOP_JS_STATUS.LOCAL = true;
        CLodopInit('local');
      }, function () {
        CLODOP_JS_STATUS.LOCAL = false;
        CLodopInit('local');
      });
    }

    function init() {
      if (IS_NEED_CLODOP) {
        clodopReady(function () {
          LodopInit();
        });
      } else {
        LodopInit();
      }
    }

    function start() {
      if (!IS_OS_WIN) {
        console.warn('非win系统不能安装Lodop打印机控件');
        return;
      }

      if (IS_NEED_CLODOP) {
        CLodopLoad();
      }
      // 所有html节点加载后初始化
      if (document.addEventListener) {
        document.addEventListener('DOMContentLoaded', init, false);
      } else if (document.attachEvent) {
        document.attachEvent('DOMContentLoaded', init);
      }
    }

    // 启动打印机
    start();

    /*** 工具方法 ***/
    function loadScript(url, successCallback, failCallback) {
      var script = document.createElement('script');
      script.type = 'text/javascript';
      if (typeof successCallback == 'function') {
        script.onload = script.onreadystatechange = function () {
          if (script.readyState && script.readyState != 'loaded'
              && script.readyState != 'complete') {
            return;
          }
          script.onreadystatechange = script.onload = null;
          successCallback();
        };
      }
      if (typeof failCallback == 'function') {
        // onerror事件在IE6-IE8不支持,但是由于CLODOP只在高级浏览器使用,所以可以放心使用此方法
        script.onerror = function (Event) {
          console.warn('load script fail:' + Event.target.src);
          failCallback();
        };
      }
      script.src = url;
      var head = document.head || document.getElementsByTagName('head')[0]
          || document.documentElement;
      head.appendChild(script);
    }

    // 自定义事件管理器
    var EventTarget = function () {
      this._listener = {};
    };
    EventTarget.prototype = {
      constructor: this,
      addEvent: function (type, fn) {
        if (typeof type === "string" && typeof fn === "function") {
          if (typeof this._listener[type] === "undefined") {
            this._listener[type] = [fn];
          } else {
            this._listener[type].push(fn);
          }
        }
        return this;
      },
      fireEvent: function (type) {
        if (type && this._listener[type]) {
          var events = {
            type: type,
            target: this
          };

          for (var length = this._listener[type].length, start = 0;
              start < length; start += 1) {
            this._listener[type][start].call(this, events);
          }
        }
        return this;
      },
      removeEvent: function (type, key) {
        var listeners = this._listener[type];
        if (listeners instanceof Array) {
          if (typeof key === "function") {
            for (var i = 0, length = listeners.length; i < length; i += 1) {
              if (listeners[i] === key) {
                listeners.splice(i, 1);
                break;
              }
            }
          } else if (key instanceof Array) {
            for (var lis = 0, lenkey = key.length; lis < lenkey; lis += 1) {
              this.removeEvent(type, key[lenkey]);
            }
          } else {
            delete this._listener[type];
          }
        }
        return this;
      }
    };

    var LodopEvents = new EventTarget();

    return {
      version: LODOP_VERSION,
      cversion: CLODOP_VERSION,
      ready: lodopReady,
      getLodop: getLodop,
      getPrinterPageSize: getPrinterPageSize,
      getPrinterList: getPrinterList
      // getLodopDrive: getLodopDrive
    };
  })();
  window.DFLodop = DFLodop;
  if (typeof define === "function" && define.amd) {
    define("DFLodop", [], function () {
      return DFLodop;
    })
  }
}());

