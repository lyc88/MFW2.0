<template>
    <div class="app-container">
        <baidu-map class="map" :center="{lng: 116.404, lat: 39.915}" :zoom="11">
            <bm-marker :position="{lng: 116.404, lat: 39.915}" :dragging="true" animation="BMAP_ANIMATION_BOUNCE">
                <bm-label content="我爱北京天安门" :labelStyle="{color: 'red', fontSize : '24px'}" :offset="{width: -35, height: 30}"/>
            </bm-marker>
        </baidu-map>
    </div>
</template>
<script>
    import http from '../../axios/http';
    import BaiduMap from 'vue-baidu-map/components/Map/Map.vue'

    export default {
        name: 'location',
        data() {
            return {
            }
        },
        methods: {
            initData(){
                // 计算表格高度
                let self = this;
                // 2.0版本特殊处理
                setTimeout(function () {
                    self.heightNum = self.utils.calcTableHeight();
                    self.containerHeightNum = (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight) - 45;
                },10);
                // 响应窗口大小改变
                window.onresize = function () {
                    self.heightNum = self.utils.calcTableHeight();
                    self.containerHeightNum = (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight) - 45;
                };
            }
        },
        mounted() {
            this.initData();
        }
    }
</script>

<style scoped>
    .map {
        width: 100%;
        height: 300px;
    }
    @media screen and (max-width: 1350px) {
        .prd-menu-container{ width: 1170px; overflow-x: auto; overflow-y: hidden;}
        .prd-menu-container-left{width: 240px;}
        .prd-menu-container-right{width: 900px;}
    }
</style>
