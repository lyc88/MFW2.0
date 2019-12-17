<template>
    <el-button class="copy-btn" type="primary" size="mini"
               :content="content"
               v-clipboard:copy='content'
               v-clipboard:success='clipboardSuccess'>
        <i class="iconfont">&#xe60e;</i>
    </el-button>
</template>
<script>
    export default {
        name: 'sdCopyButton',
        props: {
            content: {
                required: true
            }
        },
        data () {
            return {
            }
        },
        model: {
            prop: 'value',
            event: 'change'
        },
        methods: {
            // 复制成功
            clipboardSuccess() {
                this.$message({
                    message: "复制成功",
                    type: 'success',
                    duration: 1500
                })
            }
        }
    }
</script>
<style scoped>
    /* el-button 复制按钮 */
    .copy-btn {
        padding: 3px;
    }
    .copy-btn .iconfont {
        margin-right: 0;
    }
</style>
