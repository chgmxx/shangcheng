/*
 * LODOP 打印机API 接口封装
 * @author zhangmz
 * @create 2017-08-24
 */

/**
 * 打印机参数配置
 * @type {{busType: string, moduleType: string, macAddress: string, shopid: number}}
 * LODOP  全局参数
 * macAddress 全局参数
 */
let LODOP;
let macAddress;
let default_url = window.location.host;

/**
 * 初始化LodopApi對象
 * window.LodopApi
 */
(function () {
    // LodopApi 
    let LodopApi = (function () {
        "use strict";
        /*** LODOP ***/
        let LODOP_API = null;
        let LODOP_READY_STATUS = 'init';
        let LODOP_VERSION = '6.2.2.0'; // Lodop 版本
        let CLODOP_VERSION = '3.0.0.8'; // CLodop 版本
        let IS_OS_WIN = navigator.userAgent.indexOf('Windows') >= 0;
        // 判断当前请求地址是否是https
        let isHttps = ('https:' === document.location.protocol ? true : false);
        /**
         * Lodop驱动文件下载地址
         */
        let DOWN_URL = {
            LODOP32: '//maint.duofriend.com/upload/image/common/driver/install_lodop32.zip',
            LODOP64: '//maint.duofriend.com/upload/image/common/driver/install_lodop64.zip',
            CLODOP32: '//maint.duofriend.com/upload/image/common/driver/CLodop.zip',
            CLODOP32S: '//maint.duofriend.com/upload/image/common/driver/CLodops.zip'
        };

        function lodopReady(callback) {
            let readyFunc = function () {
                if (callback && typeof callback == 'function') callback(LODOP_API);
            };
            if (LODOP_READY_STATUS != 'success') {
                LodopEvents.addEvent('LodopReady', readyFunc);
            } else {
                readyFunc();
            }
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
        function getLodop(oOBJECT, oEMBED) {
            let CreatedOKLodop7766 = null;
            let style = "<style>.print-top{position: absolute;top: 0;left: 0;width: 100%;height: 40px;line-height:40px;text-align:center;background-color: red; color: #fff;} body{padding-top: 30px;}.print-top a{color: #01b9fe;padding: 0 2px;text-decoration: underline;}</style>";
            let newStyle = "<style>.public-print-tips p{margin: 0;padding:0} .public-print-tips {position: fixed;right: 30px;top: 60px;width: 330px;height: 95px;border: 1px solid #e6ebf5;background-color: #ffffff;box-shadow: 0 2px 4px rgba(0, 0, 0, .10), 0 0 6px rgba(0, 0, 0, .01);transition: opacity .3s, transform .3s, right .3s, top .4s;box-sizing: border-box;" +
                "border-radius: 10px;z-index: 99999;}.public-print-tips .public-print-left-img {float: left;padding-top: 20px;padding-left: 15px;}.public-print-tips .print-box {float: left;max-width: 243px;padding-left: 15px;padding-top: 15px;}.public-print-tips .print-box p:nth-of-type(1) {color: #2d2f33;" +
                "font-size: 16px;padding-bottom: 5px;}.public-print-tips .print-box p:nth-of-type(2) {color: #5a5e66;font-size: 14px;}.public-print-tips .public-print-right-img {padding: 9px;cursor: pointer;}.public-print-tips .print-link {color: #20a0ff;text-decoration: none;}</style>";
            // let strHtmInstall = "<div class='print-top error-block'>打印控件未安装!点击这里<a class='text-info' href='" + DOWN_URL.LODOP32 + "' download>执行安装</a>,安装后请刷新页面或重新进入即可。</div>" + style;
            let strHtmInstall = "<div class='public-print-tips'><img class='public-print-left-img' src='//maint.duofriend.com/upload/images/yj/warning.png' alt=''><div class='print-box'><p>打印控件未安装!</p><p><a class='print-link' href='" + DOWN_URL.LODOP32 + "' download>点击这里</a> " +
                "下载并安装，安装成功后刷新页面即可</p></div><img class='public-print-right-img' src='//maint.duofriend.com/upload/images/yj/close.png' alt='关闭' onclick=\"javascript:document.querySelectorAll('.public-print-tips')[0].style.right = '-500px';\" /></div>" + newStyle;            // let strHtmUpdate = "<div class='print-top error-block'>打印控件需要升级!点击这里<a class='text-info' href='" + DOWN_URL.LODOP32 + "' download>执行升级</a>,升级后请重新进入。</div>" + style;
            let strHtmUpdate = "<div class='public-print-tips'><img class='public-print-left-img' src='//maint.duofriend.com/upload/images/yj/warning.png' alt=''><div class='print-box'><p>打印控件需要升级!</p><p><a class='print-link' href='" + DOWN_URL.LODOP32 + "' download>点击这里</a> " +
                "下载并安装，安装成功后刷新页面即可</p></div><img class='public-print-right-img' src='//maint.duofriend.com/upload/images/yj/close.png' alt='关闭' onclick=\"javascript:document.querySelectorAll('.public-print-tips')[0].style.right = '-500px';\" /></div>" + newStyle;            // let strHtm64_Install = "<div class='print-top error-block'>打印控件未安装!点击这里<a class='text-info' href='" + DOWN_URL.LODOP64 + "' download>执行安装</a>,安装后请刷新页面或重新进入即可</div>" + style;
            let strHtm64_Install = "<div class='public-print-tips'><img class='public-print-left-img' src='//maint.duofriend.com/upload/images/yj/warning.png' alt=''><div class='print-box'><p>打印控件未安装!</p><p><a class='print-link' href='" + DOWN_URL.LODOP64 + "' download>点击这里</a> " +
                "下载并安装，安装成功后刷新页面即可</p></div><img class='public-print-right-img' src='//maint.duofriend.com/upload/images/yj/close.png' alt='关闭' onclick=\"javascript:document.querySelectorAll('.public-print-tips')[0].style.right = '-500px';\" /></div>" + newStyle;            // let strHtm64_Update = "<div class='print-top error-block'>打印控件需要升级!点击这里<a class='text-info' href='" + DOWN_URL.LODOP64 + "' download>执行升级</a>,升级后请重新进入。</div>" + style;
            let strHtm64_Update = "<div class='public-print-tips'><img class='public-print-left-img' src='//maint.duofriend.com/upload/images/yj/warning.png' alt=''><div class='print-box'><p>打印控件需要升级!</p><p><a class='print-link' href='" + DOWN_URL.LODOP64 + "' download>点击这里</a> " +
                "下载并安装，安装成功后刷新页面即可</p></div><img class='public-print-right-img' src='//maint.duofriend.com/upload/images/yj/close.png' alt='关闭' onclick=\"javascript:document.querySelectorAll('.public-print-tips')[0].style.right = '-500px';\" /></div>" + newStyle;
            let strHtmFireFox = "<div class='print-top error-block'>注意：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它。</div>" + style;
            let strHtmChrome = "<div class='print-top error-block'>(如果此前正常，仅因浏览器升级或重安装而出问题，需重新执行以上安装）</div>";
            let strCLodopInstall = "";
            let strCLodopUpdate = "";
            if (isHttps) {
                // strCLodopInstall = "<div class='print-top error-block'>CLodop云打印服务未安装启动!点击这里<a class='text-info' href='" + DOWN_URL.CLODOP32S + "' download >执行安装</a>,安装后请刷新页面。</div>" + style;
                strCLodopInstall = "<div class='public-print-tips'><img class='public-print-left-img' src='//maint.duofriend.com/upload/images/yj/warning.png' alt=''><div class='print-box'><p>CLodop云打印服务未安装启动!</p><p><a class='print-link' href='" + DOWN_URL.CLODOP32S + "' download>点击这里</a> " +
                    "下载并安装，安装成功后刷新页面即可</p></div><img class='public-print-right-img' src='//maint.duofriend.com/upload/images/yj/close.png' alt='关闭' onclick=\"javascript:document.querySelectorAll('.public-print-tips')[0].style.right = '-500px';\" /></div>" + newStyle;
                // strCLodopUpdate = "<div class='print-top error-block'>CLodop云打印服务需升级!点击这里<a class='text-info' href='" + DOWN_URL.CLODOP32S + "' download >执行升级</a>,升级后请刷新页面。</div>" + style;
                strCLodopUpdate = "<div class='public-print-tips'><img class='public-print-left-img' src='//maint.duofriend.com/upload/images/yj/warning.png' alt=''><div class='print-box'><p>CLodop云打印服务需升级!</p><p><a class='print-link' href='" + DOWN_URL.CLODOP32S + "' download>点击这里</a> " +
                    "下载并安装，安装成功后刷新页面即可</p></div><img class='public-print-right-img' src='//maint.duofriend.com/upload/images/yj/close.png' alt='关闭' onclick=\"javascript:document.querySelectorAll('.public-print-tips')[0].style.right = '-500px';\" /></div>" + newStyle;
            } else {
                // strCLodopInstall = "<div class='print-top error-block'>CLodop云打印服务未安装启动!点击这里<a class='text-info' href='" + DOWN_URL.CLODOP32 + "' download >执行安装</a>,安装后请刷新页面。</div>" + style;
                strCLodopInstall = "<div class='public-print-tips'><img class='public-print-left-img' src='//maint.duofriend.com/upload/images/yj/warning.png' alt=''><div class='print-box'><p>CLodop云打印服务未安装启动!</p><p><a class='print-link' href='" + DOWN_URL.CLODOP32 + "' download>点击这里</a> " +
                    "下载并安装，安装成功后刷新页面即可</p></div><img class='public-print-right-img' src='//maint.duofriend.com/upload/images/yj/close.png' alt='关闭' onclick=\"javascript:document.querySelectorAll('.public-print-tips')[0].style.right = '-500px';\" /></div>" + newStyle;
                // strCLodopUpdate = "<div class='print-top error-block'>CLodop云打印服务需升级!点击这里<a class='text-info' href='" + DOWN_URL.CLODOP32 + "' download >执行升级</a>,升级后请刷新页面。</div>" + style;
                strCLodopUpdate = "<div class='public-print-tips'><img class='public-print-left-img' src='//maint.duofriend.com/upload/images/yj/warning.png' alt=''><div class='print-box'><p>CLodop云打印服务需升级!</p><p><a class='print-link' href='" + DOWN_URL.CLODOP32 + "' download>点击这里</a> " +
                    "下载并安装，安装成功后刷新页面即可</p></div><img class='public-print-right-img' src='//maint.duofriend.com/upload/images/yj/close.png' alt='关闭' onclick=\"javascript:document.querySelectorAll('.public-print-tips')[0].style.right = '-500px';\" /></div>" + newStyle;
            }
            try {
                //=====判断浏览器类型:===============
                let isIE = navigator.userAgent.indexOf('MSIE') >= 0 || navigator.userAgent.indexOf('Trident') >= 0;
                let notice = document.createElement("span");
                if (IS_NEED_CLODOP) { // 需要 CLODOP
                    if (CLODOP_READY_STATUS == 'success') {
                        try {
                            LODOP = getCLodop();
                            console.log('使用CLodop 版本:' + LODOP.CVERSION);
                        } catch (err) {
                            console.log(err);
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

                } else { // 不需要 CLODOP
                    let is64IE = isIE && (navigator.userAgent.indexOf('x64') >= 0);
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
                            LODOP.setAttribute("style", "position:absolute;left:0px;top:-100px;width:0px;height:0px;");
                            if (isIE) {
                                LODOP.setAttribute("classid", "clsid:2105C259-1E0C-4534-8141-A753534CB4CA");
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
                    if (LODOP == null || typeof (LODOP.VERSION) == "undefined") {
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
                LODOP.SET_LICENSES("广东谷通科技有限公司", "13601BD5AB0D5B544607D05CDFBE1565", "", "");
                //============================================================
                return LODOP;
            } catch (err) {
                if (is64IE) {
                    notice.innerHTML = "Error:" + strHtm64_Install + document.documentElement.innerHTML;
                } else {
                    notice.innerHTML = "Error:" + strHtmInstall + document.documentElement.innerHTML;
                }
                document.documentElement.appendChild(notice);
                return LODOP;
            }
            ;
        }

        function LodopInit() {
            LODOP = getLodop();
            if ((LODOP == null || typeof (LODOP.VERSION) == "undefined")) {
                if (CLODOP == null || typeof (CLODOP.VERSION) == "undefined") {
                    console.warn('未安装CLodop打印机控件或需要升级');
                } else {
                    LODOP = getCLodop();
                }
            } else {
                // 已安装
                LODOP_API = LODOP;
                LODOP_READY_STATUS = 'success';
                LodopEvents.fireEvent('LodopReady');
                console.log('已安装Lodop打印机控件, 版本:' + LODOP.VERSION);
            }
        }

        /*** CLODOP ***/
        let CLODOP_READY_STATUS = 'init';
        // 是否开启clodop云打印
        let IS_USE_CLODOP = true;

        let CLODOP_JS_URL_8000 = {
            REMOTE: '/CLodopfuncs.js',
            LOCAL: '//localhost:8000/CLodopfuncs.js?priority=1'
        };
        let CLODOP_JS_URL_18000 = {
            REMOTE: '/CLodopfuncs.js',
            LOCAL: '//localhost:18000/CLodopfuncs.js?priority=0'
        };
        // https 使用版本
        let CLODOP_JS_URL_8443 = {
            REMOTE: '/CLodopfuncs.js',
            LOCAL: '//localhost:8443/CLodopfuncs.js'
        };
        // 8000 & 1800 端口地址 双开状态
        let CLODOP_JS_STATUS_8000_18000 = {
            REMOTE: null,
            LOCAL: null
        };
        // 8443 https 端口 协议
        let CLODOP_JS_STATUS_8443 = {
            REMOTE: null,
            LOCAL: null
        };

        //====判断是否需要安装CLodop云打印服务器:====
        let IS_NEED_CLODOP = (function () {
            if (!IS_USE_CLODOP) return false;
            try {
                if (isHttps) {
                    return true;
                } else {
                    let ua = navigator.userAgent;
                    if (ua.match(/Windows\sPhone/i) != null) return true;
                    if (ua.match(/iPhone|iPod/i) != null) return true;
                    if (ua.match(/Android/i) != null) return true;
                    if (ua.match(/Edge\D?\d+/i) != null) return true;
                    if (ua.match(/QQBrowser/i) != null) return false;
                    let verTrident = ua.match(/Trident\D?\d+/i),
                        verIE = ua.match(/MSIE\D?\d+/i),
                        verOPR = ua.match(/OPR\D?\d+/i),
                        verFF = ua.match(/Firefox\D?\d+/i),
                        x64 = ua.match(/x64/i);
                    if (verTrident == null && verIE == null && x64 !== null) {
                        return true;
                    } else if (verFF !== null) {
                        verFF = verFF[0].match(/\d+/);
                        if (verFF[0] >= 42) {
                            return true;
                        }
                    } else if (verOPR !== null) {
                        verOPR = verOPR[0].match(/\d+/);
                        if (verOPR[0] >= 32) {
                            return true;
                        }
                    } else if (verTrident == null && verIE == null) {
                        let verChrome = ua.match(/Chrome\D?\d+/i);
                        if (verChrome !== null) {
                            verChrome = verChrome[0].match(/\d+/);
                            if (verChrome[0] >= 46) {
                                return true;
                            }
                        }
                    }
                    ;
                    return false;
                }
            } catch (err) {
                return true;
            }
        })();

        function clodopReady(callback) {
            let readyFunc = function () {
                LodopEvents.removeEvent('CLodopReady');
                if (callback && typeof callback == 'function') callback();
            };
            if (CLODOP_READY_STATUS != 'success') {
                LodopEvents.addEvent('CLodopReady', readyFunc);
            } else {
                readyFunc();
            }
        }

        function CLodopInit() {
            try {
                // local加载成功
                if (CLODOP_JS_STATUS_8000_18000.LOCAL == true) {
                    CLODOP_READY_STATUS = 'success';
                    LodopEvents.fireEvent('CLodopReady');
                } else if (CLODOP_JS_STATUS_8443.LOCAL == true) {
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
        //====加入Https判断====
        function CLodopLoad(type, status) {
            if (!isHttps) {
                loadScript(CLODOP_JS_URL_8000.LOCAL, function () {
                    CLODOP_JS_STATUS_8000_18000.LOCAL = true;
                    CLodopInit('local');
                }, function () {
                    CLODOP_JS_STATUS_8000_18000.LOCAL = false;
                    CLodopInit('local');
                });
            } else {
                loadScript(CLODOP_JS_URL_8443.LOCAL, function () {
                    CLODOP_JS_STATUS_8443.LOCAL = true;
                    CLodopInit('local');
                }, function () {
                    CLODOP_JS_STATUS_8443.LOCAL = false;
                    CLodopInit('local');
                });
            }
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
            // 非win系统无法安装、使用Lodop打印机
            if (!IS_OS_WIN) {
                console.warn('非win系统不能安装Lodop打印机控件');
                return;
            }
            if (IS_NEED_CLODOP) CLodopLoad();
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
            if (!isHttps) {
                let script_8000 = createScript(successCallback, failCallback);
                let script_18000 = createScript(successCallback, failCallback);
                script_8000.src = url;
                let head = document.head || document.getElementsByTagName('head')[0] || document.documentElement;
                head.insertBefore(script_8000, head.firstChild);
                // 创建两个端口 避免其中一个端口被占用
                script_18000.src = CLODOP_JS_URL_18000.LOCAL;
                head.insertBefore(script_18000, head.firstChild);
            } else {
                let script_8443 = createScript(successCallback, failCallback);
                let head = document.head || document.getElementsByTagName('head')[0] || document.documentElement;
                script_8443.src = url;
                head.insertBefore(script_8443, head.firstChild);
            }
        }

        // 创建一个script 标签
        function createScript(successCallback, failCallback) {
            let script = document.createElement('script');
            script.type = 'text/javascript';
            if (typeof successCallback == 'function') {
                script.onload = script.onreadystatechange = function () {
                    if (script.readyState && script.readyState != 'loaded' && script.readyState != 'complete') return;
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
            return script;
        }

        // 自定义事件管理器
        let EventTarget = function () {
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
                    let events = {
                        type: type,
                        target: this
                    };

                    for (let length = this._listener[type].length, start = 0; start < length; start += 1) {
                        this._listener[type][start].call(this, events);
                    }
                }
                return this;
            },
            // 移除监听事件
            removeEvent: function (type, key) {
                let listeners = this._listener[type];
                if (listeners instanceof Array) {
                    if (typeof key === "function") {
                        for (let i = 0, length = listeners.length; i < length; i += 1) {
                            if (listeners[i] === key) {
                                listeners.splice(i, 1);
                                break;
                            }
                        }
                    } else if (key instanceof Array) {
                        for (let lis = 0, lenkey = key.length; lis < lenkey; lis += 1) {
                            this.removeEvent(type, key[lenkey]);
                        }
                    } else {
                        delete this._listener[type];
                    }
                }
                return this;
            }
        };
        // lodop 监听事件
        let LodopEvents = new EventTarget();
        return {
            version: LODOP_VERSION,
            cversion: CLODOP_VERSION,
            ready: lodopReady
        };
    })();
    window.LodopApi = LodopApi;
    if (typeof define === "function" && define.amd) {
        define("LodopApi", [], function () {
            return LodopApi;
        })
    }
}());

// 打印机具体参数设置
let PrintConfig = {
    printerIndex: '',
    printerPaperType: '',
    printerName: '',
    reqData: '',// 临时数据包 用完自动清除
    relationId: '',// 临时
    //显示打印配置
    show: function () {
        console.log(`printerName:${PrintConfig.printerName} , printerIndex:${PrintConfig.printerIndex}, printerPaperType:${PrintConfig.printerPaperType}`);
    }
};

/**
 * PrintAPI 打印API对象
 *
 * 属性：
 *
 * -- HOSTNAME 请求域名地址
 * -- busType  业务类型
 * -- moduleType 模块类型
 * -- shopId 门店ID
 * -- macAddress mac地址
 * -- currentUid 当前用户ID
 * -- initFlag  初始化是否成功标识 默认false
 * -- isPreview 是否需要显示预览窗口 默认false 不显示
 *
 */
let PrintAPI = {
    HOSTNAME: '',
    busType: '',
    moduleType: '',
    shopId: '',
    macAddress: '',
    currentUid: '',
    initFlag: false, // 初始化是否成功
    isPreview: false, // 是否预览 当开启后，会开启打印预览功能。默认关闭，直接打印
    /**
     * @return {string}
     */
    APIURL_CONFIG: function () {
        return PrintAPI.HOSTNAME + '/api/printer/config';
    },
    /**
     * @return {string}
     */
    APIURL_PRINT: function () {
        return PrintAPI.HOSTNAME + '/api/printer/print';
    },
    /**
     * 初始化init
     * @param busType 业务类型
     * @param moduleType 模块类型
     * @param shopId 门店id
     * @param currentUid 当前用户ID
     */
    init: function (busType, moduleType, shopId, currentUid) {
        // mac 缓存
        if (isEmpty(macAddress)) {
            let mac = localStorage.getItem("mac");
            if (isEmpty(mac)) {
                // 等待CLODOP 加载完执行
                window.On_CLodop_Opened = function () {
                    // 获取硬件信息 mac
                    PrintAPI.getMacAddress();
                }
            } else {
                macAddress = mac;
            }
        }
        if (isEmpty(busType) || isEmpty(moduleType) || isEmpty(shopId) || isEmpty(currentUid)) {
            alert("初始化打印配置失败");
            let msg = `busType: ${busType}, moduleType: ${moduleType}, shopId: ${shopId},currentUid: ${currentUid}`;
            console.log(msg);
            return;
        }
        if (isEmpty(PrintAPI.HOSTNAME)) {
            PrintAPI.HOSTNAME = default_url;
        }
        PrintAPI.busType = busType;
        PrintAPI.moduleType = moduleType;
        PrintAPI.shopId = shopId;
        PrintAPI.currentUid = currentUid;
        PrintAPI.macAddress = macAddress;
        PrintAPI.initFlag = true;
    },
    /**
     * 初始化init 带回调方法
     * @param busType 业务类型
     * @param moduleType 模块类型
     * @param shopId 门店id
     * @param currentUid 当前用户ID
     * @param callback 回调方法名
     */
    initCallBack: function (busType, moduleType, shopId, currentUid, callback) {
        // 等待CLODOP 加载完执行
        window.On_CLodop_Opened = function () {
            // 获取硬件信息 mac
            PrintAPI.getMacAddress();
        }
        if (isEmpty(busType) || isEmpty(moduleType) || isEmpty(shopId) || isEmpty(currentUid)) {
            alert("初始化打印配置失败");
            let msg = `busType: ${busType}, moduleType: ${moduleType}, shopId: ${shopId},currentUid: ${currentUid}`;
            console.log(msg);
            return;
        }
        PrintAPI.busType = busType;
        PrintAPI.moduleType = moduleType;
        PrintAPI.shopId = shopId;
        PrintAPI.currentUid = currentUid;
        PrintAPI.macAddress = macAddress;
        PrintAPI.initFlag = true;
        /*if (PrintAPI.initFlag) {
            if (PrintAPI.detectionPrinterConfig) {
                if (callback && typeof(callback) === "function")
                    callback();
            }
        }*/
    },
    /**
     * 获取PC 设备 mac地址
     */
    getMacAddress: function () {
        // 空时才获取
        if (isEmpty(macAddress)) {
            // 获取硬件信息 mac
            LODOP.GET_SYSTEM_INFO('NetworkAdapter.1.PhysicalAddress');
            if (CLODOP.CVERSION) {
                CLODOP.On_Return = function (TaskID, Value) {
                    if (Value) {
                        macAddress = Value;
                        PrintAPI.macAddress = macAddress;
                        // 存储进浏览器
                        localStorage.setItem("mac", macAddress);
                    }
                };
            }
        } else {
            PrintAPI.macAddress = macAddress;
        }
    },
    /**
     * 打印
     * @param reqData 模板数据包
     */
    print: function (reqData) {
        if (isEmpty(PrintAPI.macAddress)) {
            alert("打印参数缺失，请刷新页面后尝试!");
            console.warn("macAddress : " + PrintAPI.macAddress);
            return;
        }
        // 获取打印机配置并打印
        if (!PrintAPI.initFlag) {
            alert("打印机初始化失败，请刷新页面后重试或联系我们");
            return;
        }
        $.ajax({
            type: 'get',
            url: PrintAPI.APIURL_CONFIG(),
            data: {
                busType: PrintAPI.busType,
                moduleType: PrintAPI.moduleType,
                macAddress: PrintAPI.macAddress,
                shopId: PrintAPI.shopId
            },
            dataType: 'json',
            success: function (data) {
                PrintConfig.reqData = reqData;
                if (data.code === 0) {
                    let result = data.data;
                    // 设置打印机参数
                    PrintConfig.printerPaperType = result.printerPaperType;
                    PrintConfig.printerName = result.printerName;
                    PrintConfig.printerIndex = result.printerIndex;
                    PrintConfig.relationId = result.relationId;
                    //  判断小票 or 不干胶打印 功能
                    if (!PrintAPI.labelPrint(PrintConfig.reqData)) {
                        //  获取打印模板HTML模板数据
                        PrintAPI.printTemplatesHtml(PrintConfig.reqData, result.relationId);
                    }
                } else if (data.code === 2001) { // 打印机配置不存在
                    alert("请先设置打印机");
                    _LayerSet.open();
                } else {
                    alert(data.msg);
                }
            }
        });

    },
    /**
     * 临时打印
     * 不修改打印机默认设置，直接使用客户选择的打印机配置进行打印
     * 注：前提 设置过打印机与模板 关联配置 ，如果没有设置，请使用Print方法。
     * TODO：功能未开发完成 切勿使用 2017-09-01
     */
    printTemplate: function () {
        // LODOP = getLodop();
        // if (LODOP === undefined) {
        // alert("LODOP 驱动未安装或启动");
        // return;
        // }
        if (isEmpty(macAddress)) {
            PrintAPI.getMacAddress();
            return;
        }
        macAddress = 1;
        // 获取打印机配置并打印
        if (!PrintAPI.initFlag /*||isEmpty(reqData)*/) {
            alert("打印机初始化失败，请刷新页面后重试或联系我们");
            return;
        }
        $.ajax({
            type: 'get',
            url: PrintAPI.APIURL_CONFIG(),
            data: {
                busType: PrintAPI.busType,
                moduleType: PrintAPI.moduleType,
                macAddress: PrintAPI.macAddress,
                shopId: PrintAPI.shopId
            },
            dataType: 'json',
            success: function (data) {
                if (data.code === 0) {
                    alert("请先设置打印机");
                    _LayerSet.open();
                    let result = data.data;
                    // 设置打印机参数
                    PrintConfig.printerPaperType = result.printerPaperType;
                    PrintConfig.printerName = result.printerName;
                    PrintConfig.printerIndex = result.printerIndex;
                    // TODO: 判断小票 or 不干胶打印 功能
                    if (!PrintAPI.labelPrint(reqData)) {
                        // TODO: 获取打印模板HTML模板数据
                        PrintAPI.printTemplatesHtml(reqData, result.relationId);
                    }
                } else if (data.code === 2001) { // 打印机配置不存在

                } else {
                    alert(data.msg);
                }
            }
        });
    },
    /**
     * 小票标签打印等功能
     * @returns {boolean}
     */
    labelPrint: function (reqData) {
        let success = false;
        // 判断业务 模块类型 针对打印功能设计
        if (PrintAPI.busType === 'jxc' && PrintAPI.moduleType === 'barCode') {
            success = PrintAPI.labelBarCodePrint(reqData);
        }
        return success;
    },
    /**
     * 不干胶(标签)+条码打印功能
     */
    labelBarCodePrint: function (reqData) {
        let jsonObj = JSON.parse(reqData);
        let goods = jsonObj['goods'];
        let price = goods['price'];
        let goodsName = goods['goodsName'];
        LODOP.PRINT_INIT("不干胶条码打印");
        LODOP.SET_PRINT_MODE("PRINT_NOCOLLATE", 1);
        LODOP.ADD_PRINT_BARCODE("42pt", "17.25pt", "90pt", "37.5pt", "128Auto", jsonObj['code']);
        LODOP.SET_PRINT_STYLEA(0, "FontSize", 10);
        LODOP.ADD_PRINT_TEXT("5.25pt", "15.75pt", "90pt", "18.75pt", goodsName);
        LODOP.SET_PRINT_STYLEA(0, "FontSize", 11);
        LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
        LODOP.ADD_PRINT_TEXT("24.75pt", "19.5pt", "82.5pt", "15pt", "￥" + price);
        LODOP.SET_PRINT_STYLEA(0, "FontSize", 10);
        LODOP.SET_PRINT_STYLEA(0, "Alignment", 2);
        LODOP.SET_PRINTER_INDEXA(PrintConfig.printerIndex);
        LODOP.SET_PRINT_PAGESIZE(0, 0, 0, PrintConfig.printerPaperType);
        if (PrintAPI.isPreview) {
            LODOP.PREVIEW();
        } else {
            LODOP.PRINT();
        }
        // 清空打印数据
        PrintConfig.reqData = '';
        PrintConfig.relationId = '';
        return true;
    },
    /**
     * 检测打印机配置
     * 调用前，先初始化参数 PrintAPI.init(busType,moduleType,macAddress,shopId);
     * @return {boolean} true 成功 false 失败
     */
    detectionPrinterConfig: function () {
        // 初始化是否成功
        if (!PrintAPI.initFlag) {
            alert("打印机初始化失败，请刷新页面后重试或联系我们");
            return;
        }
        $.ajax({
            type: 'get',
            url: PrintAPI.APIURL_CONFIG(),
            data: {
                busType: PrintAPI.busType,
                moduleType: PrintAPI.moduleType,
                macAddress: PrintAPI.macAddress,
                shopId: PrintAPI.shopId
            },
            dataType: 'json',
            success: function (data) {
                if (data.code === 0) {
                    let result = data.data;
                    // 设置打印机参数
                    PrintConfig.printerPaperType = result.printerPaperType;
                    PrintConfig.printerName = result.printerName;
                    PrintConfig.printerIndex = result.printerIndex;
                    return true;
                } else if (data.code === 2001) { // 打印机配置不存在
                    return false;
                } else {
                    console.warn(data.msg);
                    return false;
                }
            }
        });
    },
    /**
     * 想更新打印机配置，请先调用 _LayerSet.open() 方法
     * 保存或更新打印机配置
     */
    saveOrUpdatePrinterConfig: function () {
        //保存或更新
        // 检查LODOP 是否安装或启动
        // LODOP = getLodop();
        // if (LODOP === undefined) {
        // alert("LODOP 驱动未安装或启动");
        // return;
        // }
        // 采集打印机数据
        let selectObj = document.getElementById("printerList");
        let printIndex = selectObj.options[selectObj.selectedIndex].value;
        let printName = selectObj.options[selectObj.selectedIndex].text;
        let printPageSize = document.getElementById("paperPageList").value;
        if (isEmpty(printIndex) || isEmpty(printName) ||
            isEmpty(printPageSize) || isEmpty(PrintAPI.busType) || isEmpty(PrintAPI.moduleType) ||
            isEmpty(PrintAPI.shopId)) {
            alert("参数缺失");
            let msg = `printIndex: ${printIndex},printName:${printName},printPageSize:${printPageSize},busType: ${PrintAPI.busType}, moduleType: ${PrintAPI.moduleType}, shopId: ${PrintAPI.shopId}`;
            console.log(msg);
            return;
        }
        let options = {
            url: PrintAPI.APIURL_CONFIG(),
            type: 'post',
            data: {
                printerName: printName,
                printerIndex: printIndex,
                printerPaperType: printPageSize,
                macAddress: PrintAPI.macAddress,
                busType: PrintAPI.busType,
                moduleType: PrintAPI.moduleType,
                shopId: PrintAPI.shopId,
                uid: PrintAPI.currentUid
            },
            dataType: 'json',
            success: function (data) {
                if (data.code === 0) {
                    alert('设置成功!');
                    PrintConfig.printerIndex = printIndex;
                    PrintConfig.printerName = printName;
                    PrintConfig.printerPaperType = printPageSize;
                    PrintConfig.relationId = data.data.relationId;
                    _LayerSet.close();
                    if (!isEmpty(PrintConfig.reqData)) {
                        //  判断小票 or 不干胶打印 功能
                        if (!PrintAPI.labelPrint(PrintConfig.reqData)) {
                            //  获取打印模板HTML模板数据
                            PrintAPI.printTemplatesHtml(PrintConfig.reqData, PrintConfig.relationId);
                        }
                    }
                } else {
                    alert(data.msg);
                }
            }
        };
        $.ajax(options);

    },
    // 打印 模板与Json数据
    printTemplatesHtml: function (reqData, relationId) {
        //保存或更新
        if (isEmpty(reqData) || isEmpty(relationId)) {
            alert("参数缺失");
            let msg = `reqData: ${reqData},relationId:${relationId}`;
            console.log(msg);
            return;
        }
        let options = {
            url: PrintAPI.APIURL_PRINT(),
            type: 'post',
            data: JSON.stringify({
                json: reqData,
                relationId: relationId
            }),
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            success: function (data) {
                if (data.code === 0) {
                    let result = data.data;
                    console.log("=====>>" + result);
                    // 直接开始打印
                    // top.layer.alert("正在打印请稍等!");
                    LODOP.SET_PRINTER_INDEXA(PrintConfig.printerIndex);
                    LODOP.SET_PRINT_PAGESIZE(0, 0, 0, PrintConfig.printerPaperType);
                    LODOP.ADD_PRINT_HTM(0, 0, "100%", "100%", result);
                    if (PrintAPI.isPreview) {
                        LODOP.PREVIEW();
                    } else {
                        LODOP.PRINT();
                    }
                    // 清空打印数据
                    PrintConfig.reqData = '';
                    PrintConfig.relationId = '';
                } else {
                    alert(data.msg);
                }
            }
        };
        $.ajax(options);
    },
    /**
     * 获取打印机设备列表
     *
     * @param selectId 下拉框ID 例子: document.getElementById(selectId)
     *
     */
    getPrinterList: function (selectId) {
        // LODOP = getLodop();
        // if (LODOP === undefined) {
        //     alert("LODOP 驱动未安装或启动");
        //     return;
        // }
        LODOP.Create_Printer_List(selectId);
    },
    /**
     * 获取当前打印机设备纸张大小
     * @param selectId 下拉框ID
     * @param printerIndex 打印机序号
     */
    getPrinterPageList: function (selectId, printerIndex) {
        // LODOP = getLodop();
        // if (LODOP === undefined) {
        //     alert("LODOP 驱动未安装或启动");
        //     return;
        // }
        LODOP.Create_PageSize_List(selectId, printerIndex);
    },
    /**
     * 重新设置打印机
     * 如果需要替换或更新 别的打印机
     *
     */
    printReset: function () {
        _LayerSet.open();
    }
};

let _LayerSet = {
    /**
     * 打开打印机设置
     */
    open: function () {
        if ($('body').find('#lodopLayerContent').length === 0) {
            this.appLayerHtml();
        }
        if ("" === document.querySelectorAll('.lodop-layer-parent')[0].style.cssText) {
            this.appendStyle();
        } else {
            if ($('body').find('#lodopLayerContent').is(":hidden")) {
                $('body').find('#lodopLayerContent').show();
            }
        }
        // 获取打印机列表
        PrintAPI.getPrinterList(document.getElementById('printerList'));
        // 默认加载第一个打印机的 纸张列表
        PrintAPI.getPrinterPageList(document.getElementById('paperPageList'), 0);
    },
    /**
     * 关闭打印机设置
     */
    close: function () {
        $('body').find('#lodopLayerContent').hide();
    },
    /**
     * 提交打印机设置
     */
    submit: function () {
        // 保存打印机配置
        PrintAPI.saveOrUpdatePrinterConfig();

    },
    /**
     * 弹框样式
     */
    appendStyle: function () {
        let nodeHead = document.createElement("style")
        let html = '        .lodop-layer-parent {' +
            '            z-index: 2003;' +
            '            top: 0;' +
            '            right: 0;' +
            '            bottom: 0;' +
            '            left: 0;' +
            '            position: fixed;' +
            '            overflow: auto;' +
            '            margin: 0;' +
            '            font-family: \'Microsoft Yahei\';' +
            '            background-color: rgba(0, 0, 0, .4);' +
            '        }' +
            '        .lodop-layer-parent .lodop-layer-content{' +
            '            background-color: #fff;' +
            '            width: 559px;min-height:350px;top:50%;' +
            '            margin-top: -300px;' +
            '            position: absolute;' +
            '            left: 50%;' +
            '            -ms-transform: translateX(-50%);' +
            '            transform: translateX(-50%);' +
            '            background: #fff;' +
            '            border-radius: 2px;' +
            '            box-shadow: 0 1px 3px rgba(0,0,0,.3);' +
            '            box-sizing: border-box;' +
            '            margin-bottom: 50px;' +
            '        }' +
            '        .lodop-layer-parent .lodop-layer-title{' +
            '            padding: 15px 20px;border-bottom: 1px solid #eeeeee;line-height: 1;margin: 0' +
            '        }' +
            '        .lodop-layer-parent .lodop-layer-close{' +
            '            float: right;' +
            '            cursor: pointer;' +
            '            font-size: 25px;' +
            '            margin-top: -5px;' +
            '            color: #bfcbd9;' +
            '        }' +
            '        .lodop-layer-parent .lodop-layer-close:hover{' +
            '            color: #20a0ff;' +
            '        }' +
            '        .lodop-layer-parent .lodop-layer-button {' +
            '            display: inline-block;' +
            '            line-height: 1;' +
            '            white-space: nowrap;' +
            '            cursor: pointer;' +
            '            background: #fff;' +
            '            border: 1px solid #c4c4c4;' +
            '            color: #1f2d3d;' +
            '            margin: 0;' +
            '            padding: 10px 15px;' +
            '            border-radius: 4px;' +
            '            -webkit-appearance: none;' +
            '            -moz-user-select: none;' +
            '            -webkit-user-select: none;' +
            '            -ms-user-select: none;' +
            '            outline: 0;' +
            '        }' +
            '        .lodop-layer-parent .lodop-layer-button:hover{' +
            '            opacity:0.9;' +
            '            border-color: #20a0ff;' +
            '        }' +
            '        .lodop-layer-parent .lodop-layer-select-title{' +
            '            float: left;min-width: 150px;text-align: right;line-height: 36px;' +
            '            padding-right: 20px;' +
            '            font-size: 15px;' +
            '            color: #48576a;' +
            '        }' +
            '        .lodop-layer-parent .lodop-layer-select{' +
            '            webkit-appearance: none;' +
            '            -moz-appearance: none;' +
            '            appearance: none;' +
            '            background-color: #fff;' +
            '            background-image: none;' +
            '            border-radius: 4px;' +
            '            border: 1px solid #bfcbd9;' +
            '            box-sizing: border-box;' +
            '            color: #1f2d3d;' +
            '            font-size: inherit;' +
            '            height: 36px;' +
            '            line-height: 1;' +
            '            padding: 3px 10px;' +
            '            width: 250px;' +
            '            outline: none;' +
            '            cursor: pointer;' +
            '            transition: border-color .2s cubic-bezier(.645,.045,.355,1);' +
            '        }' +
            '        .lodop-layer-parent .lodop-layer-select option{' +
            '            line-height: 36px;' +
            '        }' +
            '        .lodop-layer-parent .lodop-layer-select:hover{' +
            '            border-color: #8391a5;' +
            '            outline: none;' +
            '        }';
        nodeHead.type = "text/css"
        if (nodeHead.styleSheet) {
            nodeHead.styleSheet.cssText = html;
        } else {
            nodeHead.innerHTML = html;
        }
        document.getElementsByTagName("head")[0].appendChild(nodeHead);
    },
    /**
     * 弹框Html
     */
    appLayerHtml: function () {
        let html = '<section class="lodop-layer-parent" id="lodopLayerContent">' +
            '    <div class="lodop-layer-content">' +
            '        <div class="lodop-layer-title">' +
            '            <span style="font-size: 14px;font-weight: 700;color: #333;">打印设置</span>' +
            '            <span class="lodop-layer-close" onclick="_LayerSet.close()">×</span>' +
            '        </div>' +
            '        <div class="lodop-layer-items" style="padding-top: 30px;">' +
            '            <div style="padding-bottom: 30px;">' +
            '                <div class="lodop-layer-select-title">打印机列表：</div>' +
            '                <div>' +
            '                    <select id="printerList" onchange="PrintAPI.getPrinterPageList(document.getElementById(\'paperPageList\'),this.value);" class="lodop-layer-select">' +

            '                    </select>' +
            '                </div>' +
            '            </div>' +
            '            <div style="padding-bottom: 30px;">' +
            '                <div class="lodop-layer-select-title">纸张类型：</div>' +
            '                <div>' +
            '                    <select id="paperPageList" class="lodop-layer-select">' +

            '                    </select>' +
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '        <div style="text-align: right;padding: 60px 20px 10px">' +
            '            <button onclick="_LayerSet.submit()" class="lodop-layer-button" style=" color: #fff; background-color: #20a0ff; border-color: #20a0ff;">确定</button>' +
            '            <button class="lodop-layer-button" onclick="_LayerSet.close()">取消</button>' +
            '        </div>' +
            '    </div>' +
            '</section>';
        $('body').append(html)
    }
}


/**
 * 判断Js变量为空的通用方法
 * <pre>
 *     undefined， null， ''， NaN，false，0，[]，{} ，空白字符串，都返回true，否则返回false
 * </pre>
 * isEmpty()              //true
 * isEmpty([])            //true
 * isEmpty({})            //true
 * isEmpty(0)             //true
 * isEmpty(Number("abc")) //true
 * isEmpty("")            //true
 * isEmpty("   ")         //true
 * isEmpty(false)         //true
 * isEmpty(null)          //true
 * isEmpty(undefined)     //true
 * @param v
 * @returns {boolean}
 */
function isEmpty(v) {
    switch (typeof v) {
        case 'undefined':
            return true;
        case 'string':
            if (v.replace(/(^[ \t\r]*)|([ \t\r]*$)/g, '').length ===
                0) {
                return true;
            }
            break;
        case 'boolean':
            if (!v) {
                return true;
            }
            break;
        case 'number':
            if (0 === v || isNaN(v)) {
                return true;
            }
            break;
        case 'object':
            if (null === v || v.length === 0) {
                return true;
            }
            for (let i in v) {
                return false;
            }
            return true;
    }
    return false;
}