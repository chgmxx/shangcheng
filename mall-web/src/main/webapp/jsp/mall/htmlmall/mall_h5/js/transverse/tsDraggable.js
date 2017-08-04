/**
 * Created by Administrator on 2016/6/13.
 */

angular.module('ts', []).directive('tsDraggable',['$document',function($document) {
    return {
        scope:{
            dataattrs:"=",
        },
        link:function($scope, element) {
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
                width = 0,
                height = 0,
                scale = $scope.$parent.scale || 1;
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
                offset = element.offset();
                offsetX = parseInt(event.pageX - offset.left);   //鼠标相对于所选模块x轴
                offsetY = parseInt(event.pageY - offset.top);	   //鼠标现对于所选模块y轴
                width = parseInt($scope.dataattrs.width * scale);   //模板宽度
                height = parseInt($scope.dataattrs.height * scale);   //模板宽度

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
            }

            //鼠标移动
            function mouse_move(event) {
                top = event.pageY - startY + y;
                left = event.pageX - startX + x;

                element.css({
                    top: top + 'px',
                    left: left + 'px'
                });
                $scope.dataattrs.top = top / scale;
                $scope.dataattrs.left = left / scale;
            }

            //鼠标放开
            function mouse_up() {
                unbindmouse(mouse_move, mouse_up);
            }

            //缩放移动
            function zoom_move_left(event) {
                left = event.pageX - startX + x;
                var num = width - event.pageX + startX;
                if (num < min_width)return;
                $scope.$apply(function () {
                    $scope.dataattrs.left = left / scale;
                    $scope.dataattrs.width = num / scale;
                });
            }

            function zoom_move_right(event) {
                var num = event.pageX - startX + width;
                if (num < min_width)return;
                $scope.$apply(function () {
                    $scope.dataattrs.width = num / scale;
                });
            }

            function zoom_move_top(event) {
                top = event.pageY - startY + y;
                var num = height - event.pageY + startY;
                if (num < min_height)return;
                $scope.$apply(function () {
                    $scope.dataattrs.top = top / scale;
                    $scope.dataattrs.height = num / scale;
                });
            }

            function zoom_move_bottom(event) {
                var num = event.pageY - startY + height;
                if (num < min_height)return;
                $scope.$apply(function () {
                    $scope.dataattrs.height = num / scale;
                });
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