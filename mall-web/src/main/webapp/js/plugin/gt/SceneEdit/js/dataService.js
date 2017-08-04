/**
 * Created by Administrator on 2016/1/8.
 */
angular.module('data',[]).factory('Data',function(){
    return{
        dataJson:dataJson,
        dataModuleBg:dataBg,
      /*  dataPic:dataPic,*/
        font_size:[14,16,18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50],
        font_line:[1.0, 1.15, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0],
        font_name:[
            ["SimHei", "黑体"],
            ["微软雅黑", "微软雅黑"]
        ],
        form_name:["姓名","性别","电话","邮箱","地址","留言","自定义"],
        form_name_num:[0,1,2,3,4,5,6],
        animatejsonon:[
            ["淡入", "anime-fadeIn", [["淡入", "fadeIn"], ["从上淡入", "fadeInDown"], ["从下淡入", "fadeInUp"], ["从左淡入", "fadeInLeft"], ["从右淡入", "fadeInRight"], ["从上由小","spaceInUp"], ["从下由小","spaceInDown"], ["从左由小","spaceInLeft"], ["从右由小","spaceInRight"]]],
            ["弹入", "anime-fadeIn", [["弹入", "bounceIn"], ["从上弹入", "bounceInDown"], ["从下弹入", "bounceInUp"], ["从左弹入", "bounceInLeft"], ["从右弹入", "bounceInRight"]]],
            ["翻转", "anime-fadeIn", [["水平翻转", "flipInX"], ["垂直翻转", "flipInY"], ["从下翻转","perspectiveDownRetourn"], ["从上翻转","perspectiveUpRetourn"], ["从左翻转","perspectiveLeftRetourn"], ["从右翻转","perspectiveRightRetourn"], ["翻入","boingInUp"]]],
            ["旋转", "anime-fadeIn", [["旋转", "rotateIn"], ["左上旋转", "rotateInDownLeft"], ["左下旋转", "rotateInUpLeft"], ["右上旋转", "rotateInDownRight"], ["右下旋转", "rotateInUpRight"], ["右上旋入", "twisterInUp"], ["左下旋入", "twisterInDown"]]],
            ["缩放", "anime-fadeIn", [["放大", "zoomIn"], ["从上放大", "zoomInDown"], ["从下放大", "zoomInUp"], ["从左放大", "zoomInLeft"], ["从右放大", "zoomInRight"], ["渐进缩小", "zoomRest"], ["Z移入","foolishIn"]]],
            ["聚集", "anime-fadeIn", [["模糊聚集","vanishIn"], ["淡入聚集", "puffIn"]]],
            ["摇晃", "anime-fadeIn", [["旋入摇晃", "wobble"]]],
        ],
        animatejsonout:[
            ["淡出", "anime-fadeIn", [["淡出", "fadeOut"], ["从上淡出", "fadeOutDown"], ["从下淡出", "fadeOutUp"], ["从左淡出", "fadeOutLeft"], ["从右淡出", "fadeOutRight"], ["从上由小","spaceOutUp"], ["从下由小","spaceOutDown"], ["从左由小","spaceOutLeft"], ["从右由小","spaceOutRight"]]],
            ["弹出", "anime-fadeIn", [["弹出", "bounceOut"], ["从上弹出", "bounceOutUp"], ["从下弹出", "bounceOutDown"], ["从左弹出", "bounceOutLeft"], ["从右弹出", "bounceOutRight"]]],
            ["翻出", "anime-fadeIn", [["水平翻出", "flipOutX"], ["垂直翻出", "flipOutY"], ["从下翻出","perspectiveDown"], ["从上翻出","perspectiveUp"], ["从左翻出","perspectiveLeft"], ["从右翻出","perspectiveRight"], ["从后翻出","boingOutDown"]]],
            ["旋出", "anime-fadeIn", [["旋出", "rotateOut"], ["左上旋出", "rotateOutUpLeft"], ["左下旋出", "rotateOutDownLeft"], ["右上旋出", "rotateOutUpRight"], ["右下旋出", "rotateOutDownRight"]]],
            ["渐进缩小", "anime-fadeIn", [["渐进缩小", "zoomOut"], ["向上缩小", "zoomOutUp"], ["向下缩小", "zoomOutDown"], ["向左缩小", "zoomOutRight"], ["向右缩小", "zoomOutLeft"], ["翻转缩小", "holeOut"], ["Z移出","foolishOut"]]],
            ["分散", "anime-fadeIn", [["模糊分散","vanishOut"], ["淡出分散", "puffOut"]]],
            ["摇晃", "anime-fadeIn", [["旋入摇晃", "wobble"]]],
        ],
        turnThePpage:[
            ["anime-page-slideZoomIn","缩放"],["anime-page-slideIn","滑动"],["anime-page-fadeIn","淡入"],["anime-page-rotateIn","旋入"],["anime-page-xSlideIn","断入"],["anime-page-reversal","翻转"]
        ],
        animateturnThePpage:[
            ["a_slideZoom_bottomIn 0.8s","a_slideZoom_topIn 0.8s"],["a_slide_bottomIn 0.8s","a_slide_topIn 0.8s"],
            ["a_fadeIn_bottomIn 0.8s","a_fadeIn_topIn 0.8s"],["a_rotate_bottomIn 0.8s","a_rotate_topIn 0.8s"],
            ["cutCard-bottom-upward 0.8s","cutCard-top-upward 0.8s"],["a_reversal_bottomIn 0.8s linear","a_reversal_topIn 0.8s linear"]
        ],
    }
});

Array.prototype.changeAt = function(num1,num2){
    if(isNaN(num1)||num1>this.length||isNaN(num2)||num2>this.length||num1==num2){return false;}
    if(num1>num2){
        var obj = this[num1];
        for(var i=parseInt(num1),n=i-1;i>num2;i--){
            this[i] = this[n--];
        }
        this[num2] = obj;
    }else{
        var obj = this[num1];
        for(var i=parseInt(num1),n=i+1;i<num2;i++){
            this[i] = this[n++];
        }
        this[num2] = obj;
    }
}
