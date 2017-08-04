// JavaScript Document
angular.module('dragModule', []).directive('myDraggable', ['$document','$timeout', function ($document,$timeout) {
    return {
        link: function ($scope, element, attrs) {
            var startX = 0,
                startY = 0,
                x = 0,
                y = 0,
                left = 0,
                top = 0,
                offsetX = 0,
                offsetY = 0,
                min_height = 10,
                min_width = 10,
                min_bor_zoom = 8,
                offset = null,
                moveState = true,
                scale = $scope.scale;

            /**
             * 填充动画
             */
            $timeout(function(){
                var stratanimate = eval(attrs.stratanimate);
                var endanimate   = eval(attrs.endanimate);
                if(stratanimate.length>5){
                    var dom1 = element.find(".autoWh")[0];
                    var name = $scope.animatejsonon[stratanimate[0]][2][stratanimate[1]][1];
                    var time = parseFloat(stratanimate[3]);
                    var delay = parseFloat(stratanimate[2]);
                    var num = stratanimate[4];
                    var str  = name + " " + time +"s " + num;
                    if(delay > 0){
                        element.hide();
                        setTimeout((function(param){
                            return function(){
                                param.show();
                            }
                        })(element),delay*1000);
                    }
                    dom1.style.animation = str;
                    dom1.style.webkitAnimation = str;
                }
                if(endanimate.length>5){
                    var dom2 = element.find(".preview-container")[0],
                        _name = $scope.animatejsonout[endanimate[0]][2][endanimate[1]][1],
                        _time = parseFloat(endanimate[3]),
                        _delay = parseFloat(endanimate[2]),
                        _num = endanimate[4];
                    if(time>0){
                        _delay += time*num;
                    }
                    var _str = _name + " " + _time + "s " + _delay + "s " + _num;
                    dom2.style.animation = _str;
                    dom2.style.webkitAnimation = _str;
                }
            });



            /**
             * 移动模块
             */
            element.on('mousedown', function (event) {
                x = element[0].offsetLeft; //模板相对于容器left距离
                y = element[0].offsetTop;  //模板相对于容器top距离
                startX = event.pageX;  //鼠标按下x坐标
                startY = event.pageY;  //鼠标按下y坐标
                $document.unbind('mousemove', init_mouse_move);
                switch (moveState) {
                    case 0:
                        $document.on('mousemove', zoom_move_left);
                        $document.on('mouseup', zoom_up_left);
                        break;
                    case 1:
                        $document.on('mousemove', zoom_move_right);
                        $document.on('mouseup', zoom_up_right);
                        break;
                    case 2:
                        $document.on('mousemove', zoom_move_top);
                        $document.on('mouseup', zoom_up_top);
                        break;
                    case 3:
                        $document.on('mousemove', zoom_move_bottom);
                        $document.on('mouseup', zoom_up_bottom);
                        break;
                    default:
                        $document.on('mousemove', mouse_move);
                        $document.on('mouseup', mouse_up);
                        break;
                }
                event.preventDefault();
            });

            //初始移动，判断点击那条边缩放
            function init_mouse_move(event) {
                element.css({cursor: "move"});
                if ($scope.$parent == null || $scope.$parent == "") {
                    moveState = 5;
                    return;
                } else if (!$scope.$parent.config_dom || $scope.$parent.config_dom != element || $scope.$parent.showBg) {
                    moveState = 5;
                    return;
                } else {
                    width = parseInt($scope.MAIN_SELECT.attrs.width * scale);   //模板宽度
                    height = parseInt($scope.MAIN_SELECT.attrs.height * scale);   //模板宽度
                }
                offset = element.offset();
                offsetX = parseInt(event.pageX - offset.left);   //鼠标相对于所选模块x轴
                offsetY = parseInt(event.pageY - offset.top);	   //鼠标现对于所选模块y轴

                if (offsetX < min_bor_zoom) {
                    moveState = 0;
                    element.css({
                        cursor: "e-resize"
                    });
                } else if (offsetX > width - min_bor_zoom) {
                    moveState = 1;
                    element.css({
                        cursor: "e-resize"
                    });
                } else if (offsetY < min_bor_zoom) {
                    moveState = 2;
                    element.css({
                        cursor: "ns-resize"
                    });
                } else if (offsetY > height - min_bor_zoom) {
                    moveState = 3;
                    element.css({
                        cursor: "ns-resize"
                    });
                } else {
                    moveState = 5;
                    element.css({
                        cursor: "move"
                    });
                }
            }

            //移除事件
            function unbindmouse(arguments1, arguments2) {
                $document.unbind('mousemove', arguments1);
                $document.unbind('mouseup', arguments2);
                $document.on('mousemove', init_mouse_move);

                $scope.$parent.config_dom = element;
                element.children().click();
            }

            //鼠标移动
            function mouse_move(event) {
                top = event.pageY - startY + y;
                left = event.pageX - startX + x;
                element.css({
                    top: top + 'px',
                    left: left + 'px'
                });
                $scope.NAV_SELECT[parseInt(attrs.index)].attrs.top = top / scale;
                $scope.NAV_SELECT[parseInt(attrs.index)].attrs.left = left / scale;
            }

            //鼠标放开
            function mouse_up() {
                unbindmouse(mouse_move, mouse_up);
            }

            //缩放移动
            function zoom_move_left(event) {
                left = event.pageX - startX + x;
                num = width - event.pageX + startX;
                if (num < min_width)return;
                $scope.$apply(function () {
                    $scope.MAIN_SELECT.attrs.left = left / scale;
                    $scope.MAIN_SELECT.attrs.width = num / scale;
                });
                if ($scope.showPic || $scope.showMap) {
                    $scope.$apply(function () {
                        $scope.MAIN_SELECT.attrs.height = $scope.MAIN_SELECT.attrs.width / width * height;
                    })
                }
            }

            function zoom_move_right(event) {
                num = event.pageX - startX + width;
                if (num < min_width)return;
                $scope.$apply(function () {
                    $scope.MAIN_SELECT.attrs.width = num / scale;
                });
                if ($scope.showPic || $scope.showMap) {
                    $scope.$apply(function () {
                        $scope.MAIN_SELECT.attrs.height = $scope.MAIN_SELECT.attrs.width / width * height;
                    })
                }
            }

            function zoom_move_top(event) {
                top = event.pageY - startY + y;
                num = height - event.pageY + startY;
                if (num < min_height)return;
                $scope.$apply(function () {
                    $scope.MAIN_SELECT.attrs.top = top / scale;
                    $scope.MAIN_SELECT.attrs.height = num / scale;
                });
                if ($scope.showPic || $scope.showMap) {
                    $scope.$apply(function () {
                        $scope.MAIN_SELECT.attrs.width = $scope.MAIN_SELECT.attrs.height / height * width;
                    })
                }
            }

            function zoom_move_bottom(event) {
                num = event.pageY - startY + height;
                if (num < min_height)return;
                $scope.$apply(function () {
                    $scope.MAIN_SELECT.attrs.height = num / scale;
                });
                if ($scope.showPic || $scope.showMap) {
                    $scope.$apply(function () {
                        $scope.MAIN_SELECT.attrs.width = $scope.MAIN_SELECT.attrs.height / height * width;
                    })
                }
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
        }
    }
}]);