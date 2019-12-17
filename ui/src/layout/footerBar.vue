<template>
    <div>
        <div class="myfooter">
            <div>
                <el-popover
                    placement="top-start"
                    title="帮助中心"
                    width="200"
                    trigger="hover"
                    content="客服400-100-1001。">
                    <el-button slot="reference" style="background-color:black;color: white">帮助中心</el-button>
                </el-popover>
                <el-popover
                    placement="top-start"
                    title="关于我们"
                    width="200"
                    trigger="hover"
                    content="官网：www.baidu.com。">
                    <el-button slot="reference" style="background-color:black;color: white">关于我们</el-button>
                </el-popover>
                <el-popover
                    placement="top-start"
                    title="联系我们"
                    width="200"
                    trigger="hover"
                    content="客服400-100-1001">
                    <el-button slot="reference" style="background-color:black;color: white">联系我们</el-button>
                </el-popover>
            </div>
            <div style="margin-top: 20px;">
                版权所有：MFW架构系统 沪ICP备xxxxxx号 增值电信业务经营许可证：沪xxxxx.</div>
        </div>
    </div>
</template>

<script>
    import {mapGetters} from 'vuex'
    import http from '../axios/http';
    import ElButton from "../../node_modules/element-ui/packages/button/src/button";

    export default {
        components: {ElButton},
        name: 'footerBar',
        data() {
            return {
                menus: [],
                left: 0,
                buttonList: [
                ],

            }
        },
        computed: {
            ...mapGetters([
                'footerBar'
            ]),
        },
        methods: {
        }
    }
</script>

<style>
    .myfooter {
        width: 100%;
        overflow: hidden;
        white-space: nowrap;
        background-color: black;
        color: white;
        left:0px; bottom:0px;
        padding-top: 25px;
        padding-bottom: 20px;
        text-align: center;
        font-size: 12px;
        height:100px;
        z-index:9999;
    }
</style>
