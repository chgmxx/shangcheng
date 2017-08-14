// var winParents = window.parent;
// var winScreen = window.screen;
// var leftNav = $(window.parent.document.getElementById('dao-hang0')).height();
// $(function () {
//     $("#ifr", window.parent.document).load(function () {
//         loadWindow();
//     });
// });
//
// function loadWindow() {
//     //清空内容高度
//     $(winParents.document.getElementById('ifr')).height(0);
//     $(winParents.document.getElementById('content')).height(0);
//     var rightHeight = document.body.scrollHeight;//网页正文全文高，包括有滚动条时的未见区域
//     if (rightHeight > leftNav) {
//         leftHeight = rightHeight;
//     } else {
//         leftHeight = leftNav;
//     }
//     var screenHeight = winScreen.availHeight;//屏幕可用工作区的高
//     if (leftHeight < screenHeight) {
//         leftHeight = screenHeight;
//     }
//     //改变内容高度
//     $(winParents.document.getElementById('ifr')).height(leftHeight);
//     $(winParents.document.getElementById('content')).height(leftHeight);
//     //改变菜单栏的高度
//     $(winParents.document.getElementById('nav')).height(leftHeight + 167);
//
// }
//
