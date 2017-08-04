/**
 * Created by Administrator on 2015/12/26.
 * 切图
 */
angular.module('cutpic', []).directive('cutpicDirective', function ($timeout, $document) {
    var consWidth = 258,
        consHeight = 258,
        x = 0,
        y = 0,
        startX = 0,
        startY = 0,
        min_width = 8,
        min_height = 8,
        startW = 0,
        startH = 0,
        left = 0,
        num = 0,
        top = 0,
        ratios = [0, 1, 3 / 4, 9 / 16],
        moveState = true;

    return {
        scope: {
            datawidth: "=",  //图片显示部分的宽度
            dataheight: "=", //图片显示部分的高度
            datatop: "=",    //切图显示部分的y坐标
            dataleft: "=",   //切图显示部分的x坐标
            datasrc: "@",    //图片src
            dataratio: "=",  //调节 自由 正方形 4:3 16:9
            datamainw:"=",   //调节显示的宽度
            datamainh:"=",   //调节显示的高度
            dataimgmaxw:"=",
        },
        templateUrl: "js/SceneEdit/js/jcrop/jcrop.html",
        link: function (scope, element, attrs) {
            scope.dispic = false; //防止图片闪动
            var img = new Image();
            img.src = scope.datasrc;
            img.onload = function () {
                scope.$apply(function () {
                    scope.picW = img.width; //图片宽度
                    scope.picH = img.height; //图片高度
                    scope.picR = 1;
                    if (scope.picW > consWidth || scope.picH > consHeight) {
                        if (scope.picW > scope.picH) {
                            scope.picW = consWidth;
                            scope.picR = consWidth / img.width;
                            scope.picH = img.height * scope.picR;
                        } else {
                            scope.picH = consHeight;
                            scope.picR = consHeight / img.height;
                            scope.picW = img.width * scope.picR;
                        }
                    }

                    if (scope.dataheight == "" || scope.dataheight == null || scope.dataheight >= scope.picH) {
                        scope.dataheight = scope.picH;
                    }
                    if (scope.datawidth == "100%" || scope.datawidth >= scope.picW) {
                        scope.datawidth = scope.picW;
                    }
                    scope.dispic = true;
                    scope.dataimgmaxw = scope.picW;
                })
            };

            element.find(".jcrop-wrap").on("mousedown", function () {
                mouse_down();
                $document.on('mousemove', mouse_move);
                $document.on('mouseup', mouse_up);
            });
            element.find(".jh-left,.ord-m-l").on("mousedown", function () {
                mouse_down();
                $document.on('mousemove', zoom_move_left);
                $document.on('mouseup', zoom_up_left);
            });
            element.find(".jh-right,.ord-m-r").on("mousedown", function () {
                mouse_down();
                $document.on('mousemove', zoom_move_right);
                $document.on('mouseup', zoom_up_right);
            });
            element.find(".jh-top,.ord-t-c").on("mousedown", function () {
                mouse_down();
                $document.on('mousemove', zoom_move_top);
                $document.on('mouseup', zoom_up_top);
            });
            element.find(".jh-bottom,.ord-b-c").on("mousedown", function () {
                mouse_down();
                $document.on('mousemove', zoom_move_bottom);
                $document.on('mouseup', zoom_up_bottom);
            });
            element.find(".ord-t-l").on("mousedown",function(){
                mouse_down();
                $document.on("mousemove",zoom_move_top_left);
                $document.on("mouseup",zoom_up_top_left);
            });
            element.find(".ord-t-r").on("mousedown",function(){
                mouse_down();
                $document.on("mousemove",zoom_move_top_right);
                $document.on("mouseup",zoom_up_top_right);
            });
            element.find(".ord-b-l").on("mousedown",function(){
                mouse_down();
                $document.on("mousemove",zoom_move_bottom_left);
                $document.on("mouseup",zoom_up_bottom_left);
            });
            element.find(".ord-b-r").on("mousedown",function(){
                mouse_down();
                $document.on("mousemove",zoom_move_bottom_right);
                $document.on("mouseup",zoom_up_bottom_right);
            });

            /**
             * 鼠标按下事件
             * x为left
             * y为top
             * startX 鼠标按下x坐标
             * startY 鼠标按下y坐标
             * startW 鼠标按下宽度
             * startH 鼠标按下高度
             * moveState 判断是那条边缩放
             */
            function mouse_down() {
                x = scope.dataleft;
                y = scope.datatop;
                startX = event.pageX;
                startY = event.pageY;
                startW = scope.datawidth;
                startH = scope.dataheight;
                event.preventDefault();
                event.stopPropagation();
            }

            /**
             * 鼠标移动事件
             */
            function mouse_move() {
                var left = event.pageX - startX + x;
                var top = event.pageY - startY + y;
                if (left < 0)left = 0;
                if (top < 0)top = 0;
                if (left + scope.datawidth > scope.picW) left = scope.picW - scope.datawidth;
                if (top + scope.dataheight > scope.picH) top = scope.picH - scope.dataheight;
                scope.$apply(function () {
                    scope.dataleft = left;
                    scope.datatop = top;
                })
            }

            /**
             * 鼠标弹起事件
             */
            function mouse_up() {
                $document.unbind("mousemove", mouse_move);
                $document.unbind("mouseup", mouse_up);
            }

            /**
             * 左边放大事件
             */
            function zoom_move_left() {
                left = event.pageX - startX + x;
                num = startW + startX - event.pageX;
                if (num < min_width)return;
                if (left < 0) {
                    left = 0;
                    num = startW + x;
                }
                scope.$apply(function () {
                    scope.dataleft = left;
                    scope.datawidth = num;
                });
                if (scope.dataratio != 0) {
                    scope.$apply(function () {
                        scope.dataheight = num * ratios[scope.dataratio];
                        if(scope.dataheight + y > scope.picH){
                            scope.dataheight = scope.picH - y;
                            scope.datawidth = scope.dataheight / ratios[scope.dataratio];
                            scope.dataleft = x - scope.datawidth + startW ;
                        }
                    })
                }
                scope.$apply(function(){
                    scope.datamainw = num;
                    scope.datamainh = scope.dataheight;
                })
            }

            /**
             * 右边放大事件
             */
            function zoom_move_right() {
                num = event.pageX - startX + startW;
                if (num < min_width)return;
                if (num + x > scope.picW) {
                    num = scope.picW - x;
                }
                scope.$apply(function () {
                    scope.datawidth = num;
                });
                if (scope.dataratio != 0) {
                    scope.$apply(function () {
                        scope.dataheight = num * ratios[scope.dataratio];
                        if(scope.dataheight + y > scope.picH){
                            scope.dataheight = scope.picH - y;
                            scope.datawidth = scope.dataheight / ratios[scope.dataratio];
                        }
                    })
                }
                scope.$apply(function(){
                    scope.datamainw = num;
                    scope.datamainh = scope.dataheight;
                })
            }

            /**
             * 上边放大事件
             */
            function zoom_move_top() {
                top = event.pageY - startY + y;
                num = startH - event.pageY + startY;
                if (num < min_height)return;
                if (top < 0) {
                    top = 0;
                    num = startH + y;
                }
                scope.$apply(function () {
                    scope.datatop = top;
                    scope.dataheight = num;
                    scope.datamainh = num;
                    scope.datamainw = scope.datawidth;
                });
                if (scope.dataratio != 0) {
                    scope.$apply(function () {
                        scope.datawidth = num / ratios[scope.dataratio];
                        if(scope.datawidth + x > scope.picW){
                            scope.datawidth = scope.datamainw = scope.picW - x;
                            scope.dataheight = scope.datamainh = scope.datawidth * ratios[scope.dataratio];
                            scope.datatop = y - scope.dataheight + startH;
                        }
                    })
                }
            }

            /**
             * 下边放大事件
             */
            function zoom_move_bottom() {
                num = event.pageY - startY + startH;
                if (num < min_height)return;
                if (num + y > scope.picH)num = scope.picH - y;
                scope.$apply(function () {
                    scope.dataheight = num;
                    scope.datamainh = num;
                    scope.datamainw = scope.datawidth;
                });
                if (scope.dataratio != 0) {
                    scope.$apply(function () {
                        scope.datawidth = num / ratios[scope.dataratio];
                        if(scope.datawidth + x > scope.picW){
                            scope.datawidth = scope.datamainw = scope.picW - x;
                            scope.dataheight = scope.datamainh = scope.datawidth * ratios[scope.dataratio];
                        }
                    })
                }
            }

            /**
             * 左上角放大事件
             */
            function zoom_move_top_left(){
                left = event.pageX - startX + x;
                top = event.pageY - startY + y;
                var num1 = startW + startX - event.pageX,num2 = startH + startY - event.pageY;
                if (num1 < min_width || num2 < min_height)return false;
                if (left < 0) {
                    left = 0;
                    num1 = startW + x;
                };
                if(top < 0){
                    top = 0;
                    num2 = startH + y;
                };

                scope.$apply(function () {
                    scope.dataleft = left;
                    scope.datatop = top;
                    scope.datawidth = scope.datamainw = num1;
                    scope.dataheight = scope.datamainh = num2;
                });
                if (scope.dataratio != 0) {
                    scope.$apply(function () {
                        scope.datawidth = scope.datamainw = num2 / ratios[scope.dataratio];
                        scope.dataleft = x - num2 / ratios[scope.dataratio] + startW;
                        if(scope.dataleft < 0){
                            scope.datawidth = scope.datamainw = startW + x;
                            scope.dataheight = scope.datamainh = scope.datawidth * ratios[scope.dataratio];
                            scope.dataleft = 0;
                            scope.datatop = y - scope.dataheight + startH;
                        }
                    })
                }
            }

            /**
             * 右上角
             */
            function zoom_move_top_right(){
                top = event.pageY - startY + y;
                var num1 = startW - startX + event.pageX,
                    num2 = startH + startY - event.pageY;
                if (num1 < min_width || num2 < min_height)return false;
                if(top < 0){
                    top = 0;
                    num2 = startH + y;
                };
                if(num1 + x > scope.picW){
                    num1 = scope.picW - x;
                }

                scope.$apply(function () {
                    scope.datatop = top;
                    scope.datawidth = scope.datamainw = num1;
                    scope.dataheight = scope.datamainh = num2;
                });
                if (scope.dataratio != 0) {
                    scope.$apply(function () {
                        scope.datawidth = scope.datamainw = num2 / ratios[scope.dataratio];
                        if(scope.datawidth + x > scope.picW){
                            scope.datawidth = scope.datamainw = scope.picW - x;
                            scope.dataheight = scope.datamainh = scope.datawidth * ratios[scope.dataratio];
                            scope.datatop = y - scope.datawidth * ratios[scope.dataratio] + startH;
                        }
                    })
                }
            }

            /**
             * 左下角
             */
            function zoom_move_bottom_left(){
                left = event.pageX - startX + x;
                var num1 = startW + startX - event.pageX,num2 = startH - startY + event.pageY;
                if (num1 < min_width || num2 < min_height)return false;
                if (left < 0) {
                    left = 0;
                    num1 = startW + x;
                };
                if(num2 + y > scope.picH){
                    num2 = scope.picH-y;
                }

                scope.$apply(function () {
                    scope.dataleft = left;
                    scope.datawidth = scope.datamainw = num1;
                    scope.dataheight = scope.datamainh = num2;
                });
                if (scope.dataratio != 0) {
                    scope.$apply(function () {
                        scope.dataheight = scope.datamainh = num1 * ratios[scope.dataratio];
                        if (scope.dataheight + y > scope.picH){
                            scope.datawidth = scope.datamainw = (scope.picH - y) / ratios[scope.dataratio];
                            scope.dataheight = scope.datamainh = scope.picH - y;
                            scope.dataleft = x - scope.datawidth + startW;
                        }
                    })
                }


            }

            /**
             * 右下角
             */
            function zoom_move_bottom_right(){
                var num1 = startW - startX + event.pageX,num2 = startH - startY + event.pageY;
                if (num1 < min_width || num2 < min_height)return false;
                if (num1 + x > scope.picW) {
                    num1 = scope.picW - x;
                };
                if(num2 + y > scope.picH){
                    num2 = scope.picH-y;
                }

                scope.$apply(function () {
                    scope.datawidth = scope.datamainw = num1;
                    scope.dataheight = scope.datamainh = num2;
                });
                if (scope.dataratio != 0) {
                    scope.$apply(function () {
                        scope.dataheight = scope.datamainh = num1 * ratios[scope.dataratio];
                        if (scope.dataheight + y > scope.picH){
                            scope.datawidth = scope.datamainw = (scope.picH - y) / ratios[scope.dataratio];
                            scope.dataheight = scope.datamainh = scope.picH - y;
                        }
                    })
                }

            }

            /**
             * 移除事件
             * @param arguments1 鼠标移动函数名称
             * @param arguments2 鼠标弹起函数名称
             */
            function unbindmouse(arguments1, arguments2) {
                $document.unbind('mousemove', arguments1);
                $document.unbind('mouseup', arguments2);
            }

            function zoom_up_left() {
                unbindmouse(zoom_move_left, zoom_up_left);
            }

            function zoom_up_right() {
                unbindmouse(zoom_move_right, zoom_up_right);
            }

            function zoom_up_top() {
                unbindmouse(zoom_move_top, zoom_up_top);
            }

            function zoom_up_bottom() {
                unbindmouse(zoom_move_bottom, zoom_up_bottom);
            }

            function zoom_up_top_left(){
                unbindmouse(zoom_move_top_left, zoom_up_top_left);
            }
            function zoom_up_top_right(){
                unbindmouse(zoom_move_top_right, zoom_up_top_right);
            }
            function zoom_up_bottom_left(){
                unbindmouse(zoom_move_bottom_left, zoom_up_bottom_left);
            }
            function zoom_up_bottom_right(){
                unbindmouse(zoom_move_bottom_right, zoom_up_bottom_right);
            }

        },
    };
});