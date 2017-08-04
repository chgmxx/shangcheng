/**
 * Created by Administrator on 2016/1/6.
 */

angular.module('animation', []).directive('animationDirective', ['$timeout', function ($timeout) {
    return{

        link:function(scope,element,attrs){
            /**
             * 填充动画
             */
            //入场动画
            var animatejsonon = [
                ["淡入", "anime-fadeIn", [["淡入", "fadeIn"], ["从上淡入", "fadeInDown"], ["从下淡入", "fadeInUp"], ["从左淡入", "fadeInLeft"], ["从右淡入", "fadeInRight"], ["从上由小","spaceInUp"], ["从下由小","spaceInDown"], ["从左由小","spaceInLeft"], ["从右由小","spaceInRight"]]],
                ["弹入", "anime-fadeIn", [["弹入", "bounceIn"], ["从上弹入", "bounceInDown"], ["从下弹入", "bounceInUp"], ["从左弹入", "bounceInLeft"], ["从右弹入", "bounceInRight"]]],
                ["翻转", "anime-fadeIn", [["水平翻转", "flipInX"], ["垂直翻转", "flipInY"], ["从下翻转","perspectiveDownRetourn"], ["从上翻转","perspectiveUpRetourn"], ["从左翻转","perspectiveLeftRetourn"], ["从右翻转","perspectiveRightRetourn"], ["翻入","boingInUp"]]],
                ["旋转", "anime-fadeIn", [["旋转", "rotateIn"], ["左上旋转", "rotateInDownLeft"], ["左下旋转", "rotateInUpLeft"], ["右上旋转", "rotateInDownRight"], ["右下旋转", "rotateInUpRight"], ["右上旋入", "twisterInUp"], ["左下旋入", "twisterInDown"]]],
                ["缩放", "anime-fadeIn", [["放大", "zoomIn"], ["从上放大", "zoomInDown"], ["从下放大", "zoomInUp"], ["从左放大", "zoomInLeft"], ["从右放大", "zoomInRight"], ["渐进缩小", "zoomRest"], ["Z移入","foolishIn"]]],
                ["聚集", "anime-fadeIn", [["模糊聚集","vanishIn"], ["淡入聚集", "puffIn"]]],
                ["摇晃", "anime-fadeIn", [["旋入摇晃", "wobble"]]],
            ];

            //出场动画
            var animatejsonout = [
                ["淡出", "anime-fadeIn", [["淡出", "fadeOut"], ["从上淡出", "fadeOutDown"], ["从下淡出", "fadeOutUp"], ["从左淡出", "fadeOutLeft"], ["从右淡出", "fadeOutRight"], ["从上由小","spaceOutUp"], ["从下由小","spaceOutDown"], ["从左由小","spaceOutLeft"], ["从右由小","spaceOutRight"]]],
                ["弹出", "anime-fadeIn", [["弹出", "bounceOut"], ["从上弹出", "bounceOutUp"], ["从下弹出", "bounceOutDown"], ["从左弹出", "bounceOutLeft"], ["从右弹出", "bounceOutRight"]]],
                ["翻出", "anime-fadeIn", [["水平翻出", "flipOutX"], ["垂直翻出", "flipOutY"], ["从下翻出","perspectiveDown"], ["从上翻出","perspectiveUp"], ["从左翻出","perspectiveLeft"], ["从右翻出","perspectiveRight"], ["从后翻出","boingOutDown"]]],
                ["旋出", "anime-fadeIn", [["旋出", "rotateOut"], ["左上旋出", "rotateOutUpLeft"], ["左下旋出", "rotateOutDownLeft"], ["右上旋出", "rotateOutUpRight"], ["右下旋出", "rotateOutDownRight"]]],
                ["渐进缩小", "anime-fadeIn", [["渐进缩小", "zoomOut"], ["向上缩小", "zoomOutUp"], ["向下缩小", "zoomOutDown"], ["向左缩小", "zoomOutRight"], ["向右缩小", "zoomOutLeft"], ["翻转缩小", "holeOut"], ["Z移出","foolishOut"]]],
                ["分散", "anime-fadeIn", [["模糊分散","vanishOut"], ["淡出分散", "puffOut"]]],
                ["摇晃", "anime-fadeIn", [["旋入摇晃", "wobble"]]],
            ];

            $timeout(function(){
                var stratanimate = eval(attrs.stratanimate), endanimate   = eval(attrs.endanimate);
                if(stratanimate.length>5){
                    var dom1 = element[0],
                        name = animatejsonon[stratanimate[0]][2][stratanimate[1]][1],
                        time = parseFloat(stratanimate[3]),
                        num  = parseFloat(stratanimate[4]),
                        xun = stratanimate[5],
                        str  = name + " " + time +"s cubic-bezier(0.39, 0.66, 0.57, 1) 0s " + num +" backwards";
                    if(xun){
                        str = name + " " + time +"s cubic-bezier(0.39, 0.66, 0.57, 1) 0s infinite backwards";
                    }
                    dom1.style.animation = str;
                    dom1.style.webkitAnimation = str;
                }
                if(endanimate.length>5){
                    if(xun == true)return;
                    var dom2 = element.children()[0],
                        _name = animatejsonout[endanimate[0]][2][endanimate[1]][1],
                        _time = parseFloat(endanimate[3]),
                        _delay= parseFloat(endanimate[2]),
                        _num  = endanimate[4],
                        _xun  = endanimate[5];
                    if(time>0){
                        _delay += time*num;
                    }
                    var _str = _name + " " + _time + "s " + _delay + "s " + _num +" forwards";
                    if(_xun){
                        _str = _name + " " + _time + "s " + _delay + "s infinite";
                    }
                    dom2.style.animation = _str;
                    dom2.style.webkitAnimation = _str;
                    if(_delay>0 && endanimate[0]!=6)element.hide();

                }

            });
        }
    }
}])
