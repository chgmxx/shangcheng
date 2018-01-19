<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/1/18 0018
  Time: 14:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <!-- 引入样式 -->
    <link rel="stylesheet" href="https://unpkg.com/element-ui@1.4/lib/theme-default/index.css">
    <link rel="stylesheet" href="/css/mall/element-dialog.css?<%=System.currentTimeMillis()%>">
</head>
<body>
<div id="el-app" style="display: none">
    <el-dialog :title="paramsData.title" :visible.sync="dialogTableVisible">
        <div v-if="paramsData.type != null">
            <el-form :inline="true" :model="formInline" class="form-inline" v-if="paramsData.type == 1 || paramsData.type == 3">
                <el-form-item label="商品名称：">
                    <el-input v-model.trim="formInline.content" placeholder="请输入商品名称" @keyup.enter.native="search"></el-input>
                </el-form-item>
                <el-form-item label="所属分类：">
                    <el-select v-model="formInline.groupId" placeholder="所属分类" v-if="groupList != null">
                        <el-option v-for="group in groupList" :label="group.groupName" :value="group.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search">查询</el-button>
                </el-form-item>
            </el-form>
            <table border="1" cellspacing="0" cellpadding="0" width="100%" class="el-table2" v-if="pageData != null">
                <tbody>
                <tr>
                    <th width="15%"></th>
                    <th width="50%">
                        <span v-if="paramsData.type == 1 || paramsData.type == 3">商品名称</span>
                        <span v-if="paramsData.type == 2">分类名称</span>
                        <span v-if="paramsData.type == 4">分组名称</span>
                    </th>
                    <th width="15%" v-if="paramsData.type == 1 || paramsData.type == 3">状态</th>
                    <th width="20%" v-if="paramsData.type == 2 || paramsData.type== 4">创建时间</th>
                </tr>
                <tr v-for="(pro,index) in pageData.subList" :key="index" @click="selectCheckOrRadio(pro,index)">
                    <td style="text-align: center">
                        <el-radio class="radio" v-model="radioSelectData" v-if="paramsData.isCheck == 1" :label="pro" @change="radioChange(pro)">&nbsp;</el-radio>
                        <el-checkbox v-model="pro.isSelect" v-else></el-checkbox>
                    </td>
                    <td v-if="paramsData.type== 4">{{pro.groupName}}</td>
                    <td v-else>{{pro.title}}</td>
                    <td v-if="paramsData.type== 1  || paramsData.type == 3">已审核</td>
                    <td v-if="paramsData.type== 2 || paramsData.type== 4">{{pro.time | format}}</td>
                </tr>
                </tbody>
            </table>
            <div class="footer_fixed ik-box ik-box-pack-justify ik-box-align_center " v-if="pageData != null">
                <div class="footer_fixed_left">
                    <el-button type="primary" @click="confirm">确认</el-button>
                    <el-button @click="closeDialog">取消</el-button>
                </div>
                <div class="footer_fixed_right" v-if="pageData.rowCount > 1">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page.sync="pageData.curPage"
                            :page-size="pageData.pageSize"
                            layout="prev, pager, next, jumper"
                            :total="pageData.rowCount">
                    </el-pagination>
                </div>
            </div>
        </div>
    </el-dialog>
</div>
<!-- 先引入 Vue -->
<script src="https://unpkg.com/vue@2.5/dist/vue.js"></script>
<!-- 引入组件库 -->
<script src="https://unpkg.com/element-ui@1.4/lib/index.js"></script>
<script>
    var vm = new Vue({
        el: '#el-app',
        data: function () {
            return {
                dialogTableVisible: false,
                pageData: null,//商品列表
                groupList: null,//分组列表
                titles: "选择商品",
                formInline: {
                    content: '',
                    groupId: ''
                },
                paramsData: {
                    shopId: null,
                    isCheck: -1,//check==0代表多选，check==1代表单选
                    curPage: 1
                },
                radioSelectData: null
            }
        },
        filters:{
            format: function (value) {
                if(value == "" ||value == undefined){
                    return "";
                }
                var add0 = (m) => m<10?'0'+m:m;
                var time = new Date(value);
                var y = time.getFullYear();
                var m = time.getMonth()+1;
                var d = time.getDate();
                var h = time.getHours();
                var mm = time.getMinutes();
                var s = time.getSeconds();
                return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
            }
        },
        methods: {
            //关闭弹出框
            closeDialog: function () {
                vm.dialogTableVisible = false;
                CloseParentShade();
            },
            //确认
            confirm: function () {
                var selectData = [];
                if (vm.paramsData.isCheck == 0) {//多选
                    var list = vm.pageData.subList;
                    for (var i = 0; i < list.length; i++) {
                        var item = list[i];
                        if (item.isSelect) {
                            delete item.isSelect;
                            selectData.push(item);
                        }
                    }
                } else if (vm.radioSelectData != null) {//单选
                    selectData.push(vm.radioSelectData);
                }

                console.log(selectData, "selectData")
                vm.dialogTableVisible = false;
                if (vm.paramsData.type == 1) {
                    returnProVal(selectData, vm.paramsData.type, vm.paramsData.isCheck);//方法回调
                } else if (vm.paramsData.type == 2) {
                    returnBranch(selectData);
                } else if (vm.paramsData.type == 3) {
                    returnProVal(selectData, vm.paramsData.type, vm.paramsData.isCheck);//方法回调
                } else if (vm.paramsData.type == 4) {
                    returnGroupArr(selectData);//方法回调
                }
            },
            selectCheckOrRadio:function(item,index){
                if(vm.paramsData.isCheck == 0){//多选
                    item.isSelect = true;
                    vm.$set( vm.pageData.subList,index,item);
                }else{//单选
                    vm.radioSelectData = item;
                }
            },
            radioChange: function (item) {
                vm.radioSelectData = item;
            },
            handleSizeChange: function (val) {
                console.log("每页 ${val} 条");
            },
            handleCurrentChange: function (val) {
                vm.paramsData.curPage = val;
                //获取商品列表
                if (vm.paramsData.type == 1) {
                    vm.getLoadProduct();
                } else if (vm.paramsData.type == 2) {
                    vm.getLoadGroup();
                } else if (vm.paramsData.type == 3) {
                    vm.getLoadPresale();
                } else if (vm.paramsData.type == 4) {
                    vm.getLoadGroups();
                }
                console.log("当前页: " + val);
            },
            //打开弹出框
            openDialog: function (data) {
                vm.paramsData = data;
                vm.pageData = null;
                if (vm.paramsData.type == 1) {
                    //获取商品列表
                    vm.getLoadProduct();
                    //获取分组列表
                    vm.getGroupData();
                } else if (vm.paramsData.type == 2) {
                    vm.getLoadGroup();
                } else if (vm.paramsData.type == 3) {
                    vm.getLoadPresale();
                    //获取分组列表
                    vm.getGroupData();
                } else if (vm.paramsData.type == 4) {
                    vm.getLoadGroups();
                }
            },
            //搜索
            search: function () {
                vm.paramsData.curPage = 1;
                vm.getLoadProduct();
            },
            //获取分组
            getLoadGroups: function () {
                $.ajax({
                    type: "post",
                    url: "/mallPage/E9lM9uM4ct/chooseGroup.do",
                    data: {
                        stoId: vm.paramsData.shopId,
                        curPage: vm.paramsData.curPage
                    },
                    async: true,
                    success: function (data) {
                        console.log(data, "choosePro");
                        if (data.code == 0) {
                            vm.pageData = data.data.page;
                            console.log(vm.pageData, "vm.pageData")
                        }
                        vm.dialogTableVisible = true;
                    }, error: function (data) {
//                        console.log(data,"dataerror")
                    }
                });
            },
            //获取预售列表
            getLoadPresale: function () {
                $.ajax({
                    type: "post",
                    url: "/mallPage/E9lM9uM4ct/choosePresalePro.do",
                    data: {
                        stoId: vm.paramsData.shopId,
                        curPage: vm.paramsData.curPage,
                    },
                    async: true,
                    success: function (data) {
                        console.log(data, "choosePro");
                        if (data.code == 0) {
                            vm.pageData = data.data.page;
                            console.log(vm.pageData, "vm.pageData")
                        }
                        vm.dialogTableVisible = true;
                    }, error: function (data) {
//                        console.log(data,"dataerror")
                    }
                });
            },
            //获取分类列表
            getLoadGroup: function () {
                $.ajax({
                    type: "post",
                    url: "/mallPage/E9lM9uM4ct/branchPage.do",
                    data: {
                        stoId: vm.paramsData.shopId,
                        curPage: vm.paramsData.curPage,
                    },
                    async: true,
                    success: function (data) {
                        console.log(data, "choosePro");
                        if (data.code == 0) {
                            vm.pageData = data.data.page;
                            console.log(vm.pageData, "vm.pageData")
                        }
                        vm.dialogTableVisible = true;
                    }, error: function (data) {
//                        console.log(data,"dataerror")
                    }
                });
            },
            //获取商品列表
            getLoadProduct: function () {
                $.ajax({
                    type: "post",
                    url: "/mallPage/E9lM9uM4ct/choosePro.do",
                    data: {
                        stoId: vm.paramsData.shopId,
                        curPage: vm.paramsData.curPage,
                        proName: vm.formInline.content || '',
                        groupId: vm.formInline.groupId || ''
                    },
                    async: true,
                    success: function (data) {
                        console.log(data, "choosePro");
                        if (data.code == 0) {
                            vm.pageData = data.data.page;
                            console.log(vm.pageData, "vm.pageData")
                        }
                        vm.dialogTableVisible = true;
                    }, error: function (data) {
//                        console.log(data,"dataerror")
                    }
                });
            },
            //获取分组信息
            getGroupData: function () {
                if (vm.groupList != null) {
                    return;
                }
                $.ajax({
                    type: "post",
                    url: "/mallProduct/E9lM9uM4ct/group/getGroups",
                    data: {
                        stoId: vm.paramsData.shopId
                    },
                    async: true,
                    success: function (data) {
                        if (data.code == 0) {
                            vm.groupList = data.data.groupList;
                        }
                        console.log(vm.groupList, "vm.groupList ");
                    }, error: function (data) {
//                        console.log(data,"dataerror")
                    }
                });
            },
        }
    });
    $("#el-app").show();
</script>
</body>
</html>
