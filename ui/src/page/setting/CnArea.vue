<template>
    <div class="app-container">
        <div class="prd-menu-container" :style="{height: containerHeightNum + 'px'}">
            <div class="prd-menu-container-left">
                <el-tree :data="dataInfo" :props="defaultProps" accordion @node-click="handleNodeClick"></el-tree>
            </div>
            <div class="prd-menu-container-right">
                <el-row style="padding-bottom: 0;">
                    <el-col :span="24" class="toolbar">
                        <el-form :inline="true" :model="filters" ref="filterForm">
                            <el-form-item label="行政编号" prop="areaCode">
                                <el-input v-model="filters.areaCode" placeholder="支持模糊查询"></el-input>
                            </el-form-item>
                            <el-form-item label="名称" prop="name">
                                <el-input v-model="filters.name" placeholder="支持模糊查询"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="handleSearch('filterForm')">查询</el-button>
                            </el-form-item>
                        </el-form>
                    </el-col>
                </el-row>
               <!-- <el-row>
                    <el-col :span="24" class="toolbar">
                        <el-form :inline="true">
                            <el-form-item>
                                <el-button type="primary" size="small" @click="handleAdd">添加菜單</el-button>
                            </el-form-item>
                        </el-form>
                    </el-col>
                </el-row>-->
                <el-table :data="pagination.content" :height="heightNum" highlight-current-row v-loading="isLoading" border>
                    <el-table-column type="index" label="NO" width="80"></el-table-column>
                    <el-table-column type="expand" label="其他信息" width="150">
                        <template slot-scope="props">
                            <el-form label-position="left" inline class="demo-table-expand">
                                <el-form-item label="邮政编码">
                                    <span>{{ props.row.zipCode }}</span>
                                </el-form-item>
                                <el-form-item label="区号">
                                    <span>{{ props.row.cityCode }}</span>
                                </el-form-item>
                                <el-form-item label="拼音">
                                    <span>{{ props.row.pinyin }}</span>
                                </el-form-item>
                                <el-form-item label="简称">
                                    <span>{{ props.row.shortName }}</span>
                                </el-form-item>
                                <el-form-item label="经度">
                                    <span>{{ props.row.lng }}</span>
                                </el-form-item>
                                <el-form-item label="纬度">
                                    <span>{{ props.row.lat }}</span>
                                </el-form-item>
                            </el-form>
                        </template>
                    </el-table-column>
                    <el-table-column prop="areaCode" label="行政编号" width="220"></el-table-column>
                    <el-table-column prop="areaLevel" label="行政级别" width="150">
                        <template slot-scope="scope">
                            {{scope.row.areaLevel | paramCode2ParamCname("level")}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="name" label="名称" width="150"></el-table-column>
                    <el-table-column show-overflow-tooltip prop="mergerName" label="全称" width="250"></el-table-column>
                    <el-table-column label="操作" min-width="150" fixed="right">
                        <template slot-scope="scope">
                            <el-button v-if="scope.row.areaLevel=='2'" type="primary" @click="handleView(scope.$index, scope.row)">查看辖下区域（二级）</el-button>
                            <el-button type="primary" @click="handleLocation(scope.$index, scope.row)">查看地理位置</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pagination" v-if='pagination.totalElements > 0'>
                    <el-pagination layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange" :current-page.sync="pagination.page"
                                   :page-size="size" :page-sizes="[10, 20, 50, 100]"
                                   :total="pagination.totalElements" :disabled="isLoading">
                    </el-pagination>
                </div>
            </div>
        </div>
        <!--查询县级以下行政区划--->
        <section>
            <el-dialog custom-class="full-screen-dialog dialog-mt2vh dialog-mb2vh" :title="formObj.title" :visible.sync="formObj.formVisible" :close-on-click-modal="false">
                <div class="prd-menu-container" :style="{height: containerHeightNum + 'px'}">
                    <div class="prd-menu-container-left">
                       <el-tree :data="sundataInfo" :props="sundefaultProps" accordion @node-click="handleSunNodeClick"></el-tree>
                    </div>
                    <div class="prd-menu-container-right">
                        <el-table :data="sunpagination.content" :height="heightNum" highlight-current-row v-loading="sunisLoading" border>
                            <el-table-column type="index" label="NO" width="80"></el-table-column>
                            <el-table-column type="expand" label="其他信息" width="150">
                                <template slot-scope="props">
                                    <el-form label-position="left" inline class="demo-table-expand">
                                        <el-form-item label="邮政编码">
                                            <span>{{ props.row.zipCode }}</span>
                                        </el-form-item>
                                        <el-form-item label="区号">
                                            <span>{{ props.row.cityCode }}</span>
                                        </el-form-item>
                                        <el-form-item label="拼音">
                                            <span>{{ props.row.pinyin }}</span>
                                        </el-form-item>
                                        <el-form-item label="简称">
                                            <span>{{ props.row.shortName }}</span>
                                        </el-form-item>
                                        <el-form-item label="经度">
                                            <span>{{ props.row.lng }}</span>
                                        </el-form-item>
                                        <el-form-item label="纬度">
                                            <span>{{ props.row.lat }}</span>
                                        </el-form-item>
                                    </el-form>
                                </template>
                            </el-table-column>
                            <el-table-column prop="areaCode" label="行政编号" width="220"></el-table-column>
                            <el-table-column prop="areaLevel" label="行政级别" width="150">
                                <template slot-scope="scope">
                                    {{scope.row.areaLevel | paramCode2ParamCname("level")}}
                                </template>
                            </el-table-column>
                            <el-table-column prop="name" label="名称" width="150"></el-table-column>
                            <el-table-column show-overflow-tooltip prop="mergerName" label="全称" width="200"></el-table-column>
                            <el-table-column label="操作" min-width="150" fixed="right">
                                <template slot-scope="scope">
                                    <el-button type="primary" @click="handleLocation(scope.$index, scope.row)">查看地理位置</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="handleClose()">取消</el-button>
                </div>
            </el-dialog>
        </section>
        <!--查询百度地址地址-->
        <section>
            <el-dialog custom-class="full-screen-dialog dialog-mt2vh dialog-mb2vh" :title="mapObj.title" :visible.sync="mapObj.formVisible" :close-on-click-modal="false">
                <div class="prd-menu-container" :style="{height: containerHeightNum + 'px'}">
                    <baidu-map class="map" :center="mapObj.formModel.center" :zoom="mapObj.formModel.zoom">
                        <bm-marker :position="mapObj.formModel.position" :dragging="true" title="hello world" animation="BMAP_ANIMATION_BOUNCE">
<!--
                            <bm-navigation anchor="BMAP_ANCHOR_TOP_RIGHT" :content="mapObj.formModel.content"></bm-navigation>
-->

                            <bm-label anchor="BMAP_ANCHOR_TOP_RIGHT" :content="mapObj.formModel.content" :labelStyle="{color: 'red', fontSize : '24px'}" :offset="{width: -35, height: 30}"/>

                        </bm-marker>
                    </baidu-map>
                </div>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="handleMapClose()">取消</el-button>
                </div>
            </el-dialog>
        </section>
    </div>
</template>


<script>
    import http from '../../axios/http';
    import SdParamGroupForTest from "../../components/sdpmb/sdParamGroupForTest";
    import BaiduMap from 'vue-baidu-map/components/Map/Map.vue'

    export default {
        components: {SdParamGroupForTest},
        name: 'cnArea',
        data() {
            return {
                heightNum: 0,
                containerHeightNum: 0,
                dataInfo: [],
                sundataInfo:[],
                sundefaultProps: {
                    children: 'children',
                    label: 'name'
                },
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                filters: {
                    name: '',
                    status:0,
                    areaCode: ''
                },
                pagination: {
                    totalElements: 0,
                    page: 1,
                    content: []
                },
                size: 20,
                data:{
                    id:'',
                    areaLevel:'',
                    name:''
                },
                sundata:{
                    id:'',
                    areaLevel:'',
                    name:''
                },
                sunpagination: {
                    totalElements: 0,
                    page: 1,
                    content: []
                },
                isLoading: false,
                sunisLoading:false,
                selecteds: [],//列表选中列
                formObj: {
                    title: '',
                    action: '',
                    formModel: {
                        areaLevel:'',
                        areaCode:'',
                        menuUrl:'',
                        status:0,
                        menuOrder:0,
                        name:''
                    },
                    formVisible: false//编辑界面是否显示
                },
                mapObj:{
                    title: '',
                    action: '',
                    formModel: {
                        center:{lng: 116.99524, lat:32.84315},
                        position:{lng: 116.99524, lat:32.84315},
                        content:'北京',
                        zoom:'15'
                    },
                    formVisible: false//编辑界面是否显示
                }
            }
        },
        methods: {
            handleNodeClick(data) {
                console.log(data.id+","+data.name+","+data.areaLevel);
                this.data.id=data.id;
                this.data.name=data.name;
                this.data.areaLevel=data.areaLevel;
                let params = {
                    page: this.pagination.page === 0 ? 1 : this.pagination.page,
                    size: this.size === 0 ? 20 : this.size
                };
                if (this.sort) {
                    params.sort = this.sort;
                }
                if(data.areaLevel==3){
                    alert("查询县里的信息")
                }
                params['search_eq_id'] = data.id;

                http.get("api/cnArea/", {params: params}).then(response => {
                    this.isLoading = false;
                    this.pagination = response;
                    window.console.log("=======================this.pagination: " + JSON.stringify(this.pagination));
                });
            },
            handleSunNodeClick(data){
                console.log(data.id+","+data.name+","+data.areaLevel);
                this.sundata.id=data.id;
                this.sundata.name=data.name;
                this.sundata.areaLevel=data.areaLevel;
                let params = {
                    page: this.pagination.page === 0 ? 1 : this.pagination.page,
                    size: this.size === 0 ? 20 : this.size
                };
                if (this.sort) {
                    params.sort = this.sort;
                }
                if(data.areaLevel==3){
                    alert("查询县里的信息")
                }
                params['search_eq_id'] = data.id;

                http.get("api/cnArea/", {params: params}).then(response => {
                    this.sunisLoading = false;
                    this.sunpagination = response;
                    window.console.log("=======================this.pagination: " + JSON.stringify(this.sunpagination));
                });
            },
            handleLocation(index,row){
                let zoom='15';
                if(row.areaLevel=='0'){
                    zoom='8'
                }else if(row.areaLevel=='1'){
                    zoom='10'
                }else if(row.areaLevel=='2'){
                    zoom='11'
                }else if(row.areaLevel=='3'){
                    zoom='13'
                }else if(row.areaLevel=='4'){
                    zoom='15'
                }
                this.mapObj = {
                    title: row.name+"百度地图位置",
                    formVisible: true,
                    formModel: {
                        center:{lng: row.lng, lat:row.lat},
                        position:{lng: row.lng, lat:row.lat},
                        content:row.name,
                        zoom:zoom
                    },
                };
            },
            loadPagination() {
                const self = this;
                window.console.log("this.pagination.page:" + self.pagination.page);
                let params = {
                    page: this.pagination.page === 0 ? 1 : this.pagination.page,
                    size: this.size === 0 ? 20 : this.size
                };
                if (this.sort) {
                    params.sort = this.sort;
                }
                if (this.filters.areaLevel && this.filters.areaLevel !== '') {
                    params['search_eq_areaLevel'] = this.filters.areaLevel;
                }
                if (this.filters.areaCode && this.filters.areaCode !== '') {
                    params['search_like_areaCode'] = this.filters.areaCode.trim();
                }
                if (this.filters.name && this.filters.name !== '') {
                    params['search_like_name'] = this.filters.name.trim();
                }
                self.isLoading = true;
                http.get("api/cnArea/query", {params: params}).then(response => {
                    console.log("dataInfo="+JSON.stringify(response))
                    this.isLoading = false;
                    this.dataInfo = response;
                    window.console.log("=======================all this.pagination: " + JSON.stringify(this.pagination));
                });
            },
            searchPagination() {
                const self = this;
                this.data={};
                window.console.log("this.pagination.page:" + self.pagination.page);
                let params = {
                    page: this.pagination.page === 0 ? 1 : this.pagination.page,
                    size: this.size === 0 ? 20 : this.size
                };
                if (this.sort) {
                    params.sort = this.sort;
                }
                if (this.filters.areaLevel && this.filters.areaLevel !== '') {
                    params['search_eq_areaLevel'] = this.filters.areaLevel;
                }
                if (this.filters.name && this.filters.name !== '') {
                    params['search_like_name'] = this.filters.name.trim();
                }
                if (this.filters.areaCode && this.filters.areaCode !== '') {
                    params['search_like_areaCode'] = this.filters.areaCode.trim();
                }
                self.isLoading = true;
                http.get("api/cnArea", {params: params}).then(response => {
                    this.isLoading = false;
                    this.pagination = response;
                    window.console.log("=======================this.pagination: " + JSON.stringify(this.pagination));
                });
            },
            handleSearch(ref) {
                this.$refs[ref].validate((valid) => {
                    if (valid) {
                        this.searchPagination();
                    }
                });
            },
            handleSizeChange(val) {
                console.log(`每页 ${val} 条`);
                this.size = val;
                this.searchPagination();
            },
            handleCurrentChange(val) {
                //正在加载数据
                if(this.isLoading){
                    return;
                }
                window.console.log('this.pagination.page:' + val);
                this.pagination.page = val;
                window.console.log('排序动作:' + this.sort);
                this.searchPagination();
            },

            //菜单编辑
            handleView(index,row){
                http.get("api/cnArea/querySecondArea/"+row.areaCode).then(response => {
                    this.formObj = {
                        title: row.name+"辖下区域",
                        formVisible: true,
                        formModel: response,
                    };
                    this.sundataInfo=response
                });

            },
            handleClose() {//关闭表单
                this.formObj.formVisible = false;
            },
            handleMapClose(){
                this.mapObj.formVisible = false;
            },
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
            this.loadPagination();
        }
    }
</script>

<style scoped>
    .map {
        width: 100%;
        height: 100%;
    }
    .prd-menu-container {
        width: 100%; overflow-x: auto;
    }
    .prd-menu-container-left{
        width: 20%; height: 100%; float: left; overflow-y: auto; overflow-x: hidden; border-right: 3px solid #d1dbe5;
    }
    .prd-menu-container-right{
        width: 78%; height: 100%; float: left; overflow-y: hidden; overflow-x: hidden; margin-left: 20px;
    }
    @media screen and (max-width: 1350px) {
        .prd-menu-container{ width: 1170px; overflow-x: auto; overflow-y: hidden;}
        .prd-menu-container-left{width: 240px;}
        .prd-menu-container-right{width: 900px;}
    }
</style>
