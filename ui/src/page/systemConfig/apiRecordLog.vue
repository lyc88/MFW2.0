<template>
    <div class="app-container">
        <!--工具条-->
        <el-row>
            <el-col :span="20" class="toolbar">
                <el-form :inline="true" :model="filters" label-width="100px">
                    <el-form-item label="请求编号" prop="appId">
                        <el-select v-model="filters.appId" placeholder="请选择响应码">
                            <el-option value="" label="全部"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="url前缀" prop="urlPrefix">
                        <el-input size="small" v-model="filters.urlPrefix" placeholder="url前缀"/>
                    </el-form-item>
                    <el-form-item label="响应码" prop="responseCode">
                        <el-select v-model="filters.responseCode" placeholder="请选择响应码">
                            <el-option value="" label="全部"></el-option>
                            <el-option value="200" label="200"></el-option>
                            <el-option value="400" label="400"></el-option>
                            <el-option value="404" label="404"></el-option>
                            <el-option value="405" label="405"></el-option>
                            <el-option value="500" label="500"></el-option>
                            <el-option value="502" label="502"></el-option>
                            <el-option value="9999" label="9999"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="请求参数" prop="requestParams">
                        <el-input size="small" v-model="filters.requestParams" placeholder="请求参数"/>
                    </el-form-item>
                    <el-form-item label="返回结果" prop="responseBody">
                        <el-input size="small" v-model="filters.responseBody" placeholder="返回结果"/>
                    </el-form-item>
                    <el-form-item label="创建时间" prop="dateRange">
                        <el-date-picker v-model="filters.dateRange" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange" :default-time="['00:00:00', '23:59:59']"></el-date-picker>
                    </el-form-item>
                    <el-form-item label="操作人" prop="userName">
                        <el-input size="small" v-model="filters.userName" placeholder="操作人"/>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" size="small" icon="el-icon-search" @click="handleSearch">查询</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="24" class="toolbar">
                <el-form :inline="true">
                    <el-form-item>
                        <el-button  type="primary" @click="handleBatchSend">批量推送</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <el-table :data="pagination.content" :height="heightNum" highlight-current-row v-loading="isLoading" border fixed>
            <el-table-column type="index" width="60" label="序号" align="center"></el-table-column>
            <el-table-column prop="userName" label="操作人" width="200" align="center"></el-table-column>
            <el-table-column prop="appId" label="请求编号" width="200" align="center">
                <template slot-scope="scope">
                    <p v-if="scope.row.appId==='recordProductSuccess'">推送OMS备案记录成功</p>
                    <p v-else-if="scope.row.appId==='recordProductFailure'">推送OMS备案记录失败</p>
                    <p v-else-if="scope.row.appId==='taiSengUrl'">推送泰腾日志</p>
                    <p v-else-if="scope.row.appId==='cancelBondProduct'">保税商品推送OMS作废</p>
                    <p v-else-if="scope.row.appId==='recordHHY'">商检上传汇海源</p>
                    <p v-else-if="scope.row.appId==='queryHHY'">查询商品商检结果</p>
                    <p v-else-if="scope.row.appId==='queryCustomerCode'">根据客户名查询客户编号</p>
                    <p v-else-if="scope.row.appId==='queryCustomerInfo'">根据用户编号查询用户名</p>
                    <p v-else-if="scope.row.appId==='omsAddProduct'">商品创建信息</p>
                    <p v-else-if="scope.row.appId==='queryDeclare'">查询申报单位</p>
                </template>
            </el-table-column>
            <el-table-column prop="apiUrl" label="请求地址" min-width="200" align="center">
                <template slot-scope="scope">
                    <el-popover popper-class="popover-style" placement="left" trigger="hover" width="300">
                        <div style="word-break:break-all;">
                            {{scope.row.apiUrl}}
                        </div>
                        <span slot="reference">{{scope.row.apiUrl}}</span>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column prop="requestMethod" label="请求方式" width="100" align="center"></el-table-column>
            <el-table-column prop="requestParams" label="请求入参" min-width="250" align="center">
                <template slot-scope="scope">
                    <el-popover popper-class="popover-style" placement="left" trigger="hover" width="400">
                        <div style="word-break:break-all;">
                            {{scope.row.requestParams}}
                        </div>
                        <span slot="reference">{{scope.row.requestParams}}</span>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column prop="responseStatus" label="响应码" width="80" align="center"></el-table-column>
            <el-table-column prop="responseBody" label="返回结果" min-width="250" align="center">
                <template slot-scope="scope">
                    <el-popover popper-class="popover-style" placement="left" trigger="hover" width="400">
                        <div style="word-break:break-all;">
                            {{scope.row.responseBody}}
                        </div>
                        <span slot="reference">{{scope.row.responseBody}}</span>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" show-overflow-tooltip width="150" align="center">
                <template slot-scope="scope">
                    <span>{{ scope.row.createTime | datetime}}</span>
                </template>
            </el-table-column>
            <el-table-column  prop="responseBody" label="操作" width="120" fixed="right" align="center">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.appId==='recordProductSuccess'" type="primary" size="mini" @click="handlePush(scope.row)"><i class="iconfont">&#xe633;</i>重新推送</el-button>
                    <el-button v-if="scope.row.appId==='recordProductFailure'" type="primary" size="mini" @click="handlePush(scope.row)"><i class="iconfont">&#xe633;</i>重新推送</el-button>
                    <el-button v-if="scope.row.appId==='cancelBondProduct'" type="primary" size="mini" @click="handlePush(scope.row)"><i class="iconfont">&#xe633;</i>重新推送</el-button>
                </template>

            </el-table-column>
        </el-table>
        <!--分页开始-->
        <div class="pagination" v-if='pagination.totalElements > 0'>
            <el-pagination layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
                           @current-change="handleCurrentChange" :current-page.sync="pagination.page"
                           :page-size="size" :page-sizes="[10, 20, 50, 100]"
                           :total="pagination.totalElements" :disabled="isLoading">
            </el-pagination>
        </div>
        <!--分页结束-->
    </div>
</template>


<script>
    import http from '../../axios/http';
    import {isNotEmpty} from "../../filter/index";

    export default {
        name: 'ApiRecordLog',
        data() {
            return {
                heightNum: 0,
                filters: {
                    appId: '',
                    urlSuffix: '',
                    responseStatus: '',
                    responseCode: '',
                    requestParams: '',
                    responseBody: '',
                    urlPrefix: '',
                    userName:'',
                    dateRange: []
                },
                pagination: {
                    totalElements: 0,
                    page: 1,
                    content: []
                },
                size: 20,
                isLoading: false,
            }
        },
        methods: {
            //检索
            handleSearch() {
                this.handleCurrentChange(1);
            },
            // 页面跳转
            handleSizeChange(val) {
                this.size = val;
                this.loadPagination();
            },
            // 当前页
            handleCurrentChange(val) {
                console.log(val)
                this.pagination.page = val;
                this.loadPagination();
            },
            //获取页面数据
            loadPagination() {
                let params = {
                    page: this.pagination.page === 0 ? 1 : this.pagination.page,
                    size: this.size
                };
                if (isNotEmpty(this.filters.dateRange) && this.filters.dateRange.length === 2) {
                    params['search_GTE_startTime'] = this.filters.dateRange[0];
                    params['search_LTE_endTime'] = this.filters.dateRange[1];
                }
                if (isNotEmpty(this.filters.appId)) {
                    params['search_like_appId'] = this.filters.appId;
                }
                if (isNotEmpty(this.filters.userName)) {
                    params['search_like_userName'] = this.filters.userName;
                }
                if (isNotEmpty(this.filters.urlSuffix)) {
                    params['search_like_urlSuffix'] = this.filters.urlSuffix;
                }
                if (isNotEmpty(this.filters.urlPrefix)) {
                    params['search_like_urlPrefix'] = this.filters.urlPrefix;
                }
                if (isNotEmpty(this.filters.responseStatus)) {
                    params['search_like_responseStatus'] = this.filters.responseStatus;
                }
                if (isNotEmpty(this.filters.responseCode)) {
                    params['search_like_responseCode'] = this.filters.responseCode;
                }
                if (isNotEmpty(this.filters.requestParams)) {
                    params['search_like_requestParams'] = this.filters.requestParams;
                }
                if (isNotEmpty(this.filters.responseBody)) {
                    params['search_like_responseBody'] = this.filters.responseBody;
                }
                this.isLoading = true;
                http.get("api/invokeApiLog/queryApiLogInfo", {params: params}).then(response => {
                    this.isLoading = false;
                    response.page = response.number + 1;
                    this.pagination = response;
                });
            },
            //重新推送请求
            handlePush(row) {
                this.isLoading = true;
                console.log(row);
                http.put("api/invokeApiLog/againPushApi", row).then(response => {
                    console.log(response);
                    if (response.stateCode==='00000') {
                        console.log(response);
                        this.isLoading = false;
                        this.loadPagination();
                    }else{
                        this.$alert("推送不成功，原因："+response.message);
                        this.isLoading = false;
                        this.loadPagination();
                    }
                });
            },

            handleBatchSend(){
                let params = {
                    page: this.pagination.page === 0 ? 1 : this.pagination.page,
                    size: this.size
                };
                if (isNotEmpty(this.filters.dateRange) && this.filters.dateRange.length === 2) {
                    params['search_GTE_startTime'] = this.filters.dateRange[0];
                    params['search_LTE_endTime'] = this.filters.dateRange[1];
                }
                if (isNotEmpty(this.filters.appId)) {
                    params['search_like_appId'] = this.filters.appId;
                }
                if (isNotEmpty(this.filters.urlSuffix)) {
                    params['search_like_urlSuffix'] = this.filters.urlSuffix;
                }
                if (isNotEmpty(this.filters.urlPrefix)) {
                    params['search_like_urlPrefix'] = this.filters.urlPrefix;
                }
                if (isNotEmpty(this.filters.responseStatus)) {
                    params['search_like_responseStatus'] = this.filters.responseStatus;
                }
                if (isNotEmpty(this.filters.responseCode)) {
                    params['search_like_responseCode'] = this.filters.responseCode;
                }
                if (isNotEmpty(this.filters.requestParams)) {
                    params['search_like_requestParams'] = this.filters.requestParams;
                }
                if (isNotEmpty(this.filters.responseBody)) {
                    params['search_like_responseBody'] = this.filters.responseBody;
                }
                this.isLoading = true;
                http.get("api/invokeApiLog/batchPush", {params: params}).then(response => {
                    console.log(response);
                    if (response.stateCode==='00000') {
                        console.log(response);
                        this.isLoading = false;
                        this.loadPagination();
                    }else{
                        this.$alert("推送不成功，原因："+response.message);
                        this.isLoading = false;
                        this.loadPagination();
                    }
                });
            },
            // 计算表格高度
            initData() {
                let self = this;
                // 2.0版本特殊处理
                setTimeout(function () {
                    self.heightNum = self.utils.calcTableHeight();
                }, 10);
                // 响应窗口大小改变
                window.onresize = function () {
                    self.heightNum = self.utils.calcTableHeight();
                };
            },
        },
        mounted() {
            this.initData();
            this.loadPagination();
        }
    }
</script>
<style>
</style>

