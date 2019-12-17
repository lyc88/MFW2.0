<template>
    <div class="app-wrapper">
        <sidebar></sidebar>
        <div class="main-container">
            <navbar></navbar>
            <app-main></app-main>
            <footer-bar></footer-bar>
        </div>
    </div>
</template>

<script>
    import Navbar from './Navbar';
    import Sidebar from './Sidebar';
    // import TagsView from './TagsView';
    import AppMain from './AppMain';
    import FooterBar from "./footerBar";

    export default {
        name: 'layout',
        components: {
            FooterBar,
            Navbar,
            Sidebar,
            AppMain,
            // TagsView
        },
        computed: {
            sidebar() {
                return this.$store.state.app.sidebar
            }
        }
    }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
    @import "../../static/styles/mixin";
    .app-wrapper {
        @include clearfix;
        position: relative;
        height: 100%;
        width: 100%;
    }

    // 主体区域
    .main-container {
        min-height: 100%;
        transition: margin-left .28s;
        overflow-y: auto; overflow-x: hidden; height: 600px;
        //margin-left: 180px;
    }
</style>
