<template>
    <el-menu class="navbar" mode="horizontal">
        <!--<Hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened"></Hamburger>-->
        <!--<levelbar></levelbar>-->

        <tags-view></tags-view>

        <div class="fr" style="line-height: 50px;">

            <size-select class="international"></size-select>
            <span style="color: #B5BDB9;">|</span>
            <!-- 暂时不使用多语言，此处注释 -->
            <!--<lang-select class="international"></lang-select><span style="color: #B5BDB9;">|</span>-->
            <span class="international">
                <el-button size="mini" type="text" @click="goHome">
                    <i class="iconfont">&#xe607;</i>返回首页
                </el-button>
            </span>
            <span style="color: #B5BDB9;">|</span>

            <el-popover class="international"
                        placement="left" trigger="click">
                <el-form :inline="true" :model="userObj"  label-width="110px" ref="reasonForm">
                    <table class="productListTable productListTable-col4">
                        <tr>
                            <td>
                                <el-form-item label="姓名" prop="userName">
                                    {{userObj.userName}}
                                </el-form-item>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <el-form-item label="邮箱" prop="email">
                                    {{userObj.email}}
                                </el-form-item>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <el-form-item label="手机" prop="mobile">
                                    {{userObj.mobile}}
                                </el-form-item>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <el-form-item label="性别" prop="sex">
                                    {{userObj.sex|paramCode2ParamCname("sexStatus")}}
                                </el-form-item>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-form-item label="状态" prop="status">
                                    {{userObj.status|paramCode2ParamCname("statusType")}}
                                </el-form-item>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-form-item label="部门" prop="deptName">
                                    {{userObj.deptId|deptId2DeptName}}
                                </el-form-item>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-form-item label="地址" prop="address">
                                    <el-popover placement="top-start" trigger="hover">
                                        <div>
                                            <p style="width: 500px; font-size: 14px; margin-bottom: 5px; word-break: break-all;"> {{userObj.address}}</p>
                                        </div>
                                        <span slot="reference"> {{userObj.address}}</span>
                                    </el-popover>
                                </el-form-item>
                            </td>
                        </tr>
                    </table>
                </el-form>
                <el-avatar size="small" :src="userObj.avatarImg" slot="reference"></el-avatar>
            </el-popover>
        </div>
    </el-menu>
</template>

<script>
    import {mapGetters} from 'vuex'
    // import Hamburger from '../components/Hamburger';
    // import Levelbar from './Levelbar';
    // import LangSelect from '../components/LangSelect';
    import SizeSelect from '../components/SizeSelect';
    import TagsView from './TagsView';
    import http from '../axios/http';


    export default {

        components: {
            // Hamburger,
            // Levelbar,
            // LangSelect,
            SizeSelect,
            TagsView
        },
        data() {
            return {
                userObj: {
                },
            }
        },
        computed: {
            ...mapGetters([
                'sidebar',
                'name',
                'avatar'
            ])
        },
        methods: {
            toggleSideBar() {
                this.$store.dispatch('ToggleSideBar')
            },
            queryUserInfo(){
                let url="api/interface/userInfo";
                http.get(url).then(response => {
                    if(response.code==='00000'){
                        this.userObj=response.data;
                    }else{
                        this.$message(response.message);
                    }

                }).catch(function (error) {
                    this.isLoading = true;
                    console.log(JSON.stringify(error));
                });
            },
            goHome(){
                window.location.href = "#/view";
            }
        },
        created() {
            this.queryUserInfo();
        }
    }
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
    .navbar {
        height: 35px;
        /*background-color: #eef1f6;*/
        background-color: #eef1f6;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .15);

        /*line-height: 35px;*/
        border-radius: 0 !important;
        /*.hamburger-container {
            line-height: 35px;
            height: 35px;
            float: left;
            padding-top: 5px;
            padding-left: 5px;
        }*/
    }
    .international{
        vertical-align: top;
        margin: 0 15px;
    }
    .breadcrumb-container {
        float: left;
    }

</style>
