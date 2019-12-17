<template>
    <div>
        <div style="padding:20px 20px 20px 20px;margin-bottom:50px;">
            <el-divider>小组件之返回顶部</el-divider>
            <div>
                1：在Layout.vue中设置类main-container中添加：overflow-y: auto; overflow-x: hidden; height: 600px;
                <br>
                2:接着el-backtop组件（element-ui2.11后出现）其中top是在哪个类中执行（.main-container）
            </div>
            <el-divider>小组件之小功能</el-divider>
            <el-row>
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span>Buttons</span>
                    </div>
                    <div style="margin-bottom:50px;">
                        <el-col :span="4" class="text-center">
                            <a href="http://www.zhm.com:9001/actuator" class="pan-btn blue-btn" target="_blank">
                                <span style="color:white;">actuator监控</span>
                            </a>
                        </el-col>
                        <el-col :span="4" class="text-center">
                            <router-link class="pan-btn light-blue-btn" to="/icons">
                                按钮集
                            </router-link>
                        </el-col>
                        <el-col :span="4" class="text-center">
                            <router-link class="pan-btn pink-btn" to="/sysUser">
                                图片导入举例（用户头像）
                            </router-link>
                        </el-col>
                        <el-col :span="4" class="text-center">
                            <router-link class="pan-btn green-btn" to="/home">
                                图表举例（首页）
                            </router-link>
                        </el-col>
                        <el-col :span="4" class="text-center">
                            <router-link class="pan-btn tiffany-btn" to="/sysUser">
                                导出操作（用户）
                            </router-link>
                        </el-col>
                        <el-col :span="4" class="text-center">
                            <router-link class="pan-btn yellow-btn" to="/sysMenu">
                                Tree举例（菜单）
                            </router-link>
                        </el-col>
                    </div>
                </el-card>
            </el-row>
            <el-divider>小组件之计数组件</el-divider>
            <div>
                计算器组件：vue-count-to，使用的时候需要npm install vue-count-to
            </div>
            <div>
               <count-to  ref="example"
                          :start-val="_startVal"
                          :end-val="_endVal"
                          :duration="_duration"
                          :decimals="_decimals"
                          :separator="_separator"
                          :prefix="_prefix"
                          :suffix="_suffix"
                          :autoplay="false"
                          class="example">

               </count-to>
                <div style="margin-left: 25%;margin-top: 40px;">
                    <label class="label" for="startValInput">startVal:
                        <input v-model.number="setStartVal" type="number" name="startValInput">
                    </label>
                    <label class="label" for="endValInput">endVal:
                        <input v-model.number="setEndVal" type="number" name="endVaInput">
                    </label>
                    <label class="label" for="durationInput">duration:
                        <input v-model.number="setDuration" type="number" name="durationInput">
                    </label>
                    <div class="startBtn example-btn" @click="start">
                        Start
                    </div>
                    <div class="pause-resume-btn example-btn" @click="pauseResume">
                        pause/resume
                    </div>
                    <br>
                    <label class="label" for="decimalsInput">decimals:
                        <input v-model.number="setDecimals" type="number" name="decimalsInput">
                    </label>
                    <label class="label" for="separatorInput">separator:
                        <input v-model="setSeparator" name="separatorInput">
                    </label>
                    <label class="label" for="prefixInput">prefix:
                        <input v-model="setPrefix" name="prefixInput">
                    </label>
                    <label class="label" for="suffixInput">suffix:
                        <input v-model="setSuffix" name="suffixInput">
                    </label>
                </div>
            </div>
            <el-divider>小组件之actuator监控说明</el-divider>
            <table>
                    <tr>
                        <td>
                            路径
                        </td>
                        <td>
                            作用
                        </td>
                    </tr>
                    <tr>
                        <td>
                            /actuator/beans
                        </td>
                        <td>
                            显示应用程序中所有Spring bean的完整列表。
                        </td>
                    </tr>
                    <tr>
                        <td>
                            /actuator/configprops
                        </td>
                        <td>
                            显示所有配置信息。
                        </td>
                    </tr>
                    <tr>
                        <td>
                            /actuator/env
                        </td>
                        <td>
                            陈列所有的环境变量。
                        </td>
                    </tr>
                    <tr>
                        <td>
                            /actuator/mappings
                        </td>
                        <td>
                            显示所有@RequestMapping的url整理列表。
                        </td>
                    </tr>
                    <tr>
                        <td>
                            /actuator/health
                        </td>
                        <td>
                            显示应用程序运行状况信息 up表示成功 down失败
                        </td>
                    </tr>
                    <tr>
                        <td>
                            /actuator/info
                        </td>
                        <td>
                            查看自定义应用信息
                        </td>
                    </tr>
                </table>
            <el-divider>小组件之技术链接</el-divider>
            <div>
                <a href="https://element.eleme.cn/2.11/#/zh-CN/component" style="color: #3a8ee6" target="_blank">Element-ui</a>
                <br>
                Element-ui是饿了么公司提供的基于vue2.0的前端架构
                <br>
                <a href="https://panjiachen.github.io/vue-element-admin" style="color: #3a8ee6"  target="_blank">vue-element-admin</a>
                <br>
                vue-element-admin这是一款比较不错的前端架构,可用于参考
                <br>
                <a href="https://demo.renren.io/security-enterprise/#/home" style="color: #3a8ee6"  target="_blank">人人权限系统</a>
                <br>
                人人权限系统这是一款比较不错的权限系统架构,可用于业务参考
                <a href="https://dafrok.github.io/vue-baidu-map/#/zh/index" style="color: #3a8ee6"  target="_blank">vue-baidu-map百度地图组件</a>
                <br>
                vue-baidu-map是百度地图的用于vue.js的组件
            </div>
            <el-divider>小组件之Steps 步骤条</el-divider>
            <div>
                <el-steps :active="active" finish-status="success">
                    <el-step title="验证码">
                    </el-step>
                    <el-step title="重新设置密码"></el-step>
                    <el-step title="完成"></el-step>
                </el-steps>
                <div style="margin-bottom: 15px;">
                    <div v-if="active=='0'">
                        <el-form>
                            <table>
                                <tr>
                                    <td>
                                        <el-form-item label="邮箱" prop="email">
                                            <el-input  auto-complete="off"></el-input>
                                        </el-form-item>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <el-form-item label="验证码" prop="code">
                                            <el-input  auto-complete="off"></el-input>
                                        </el-form-item>
                                    </td>
                                </tr>
                            </table>
                        </el-form>
                    </div>
                    <div v-if="active=='1'">
                        <el-input type="text"></el-input>
                    </div>
                    <div v-if="active=='2'">
                        完成
                    </div>
                </div>
                <el-button  type="primary" @click="addInfo">下一步</el-button>
            </div>
            <el-divider>小组件之列表里的复制</el-divider>
            <div>
                <router-link class="pan-btn light-blue-btn" to="/syslog">
                    日志列表中请求参数可以复制
                </router-link>
            </div>
            <el-divider>小组件之百度地图组件</el-divider>
            <div>
                <router-link class="pan-btn light-blue-btn" to="/location">
                    百度地图组件
                </router-link>
            </div>

        </div>
        <el-backtop target=".main-container" :bottom="100">
            <div class="top">
                UP
            </div>
        </el-backtop>
    </div>
</template>

<script>
    import countTo from 'vue-count-to'
    export default {
        components: {
            countTo},
        name: 'components',
        data() {
            return {
                active: 0,
                setStartVal: 0,
                setEndVal: 2017,
                setDuration: 4000,
                setDecimals: 0,
                setSeparator: ',',
                setSuffix: ' rmb',
                setPrefix: '¥ '
            }
        },

        computed: {
            _startVal() {
                if (this.setStartVal) {
                    return this.setStartVal
                } else {
                    return 0
                }
            },
            _endVal() {
                if (this.setEndVal) {
                    return this.setEndVal
                } else {
                    return 0
                }
            },
            _duration() {
                if (this.setDuration) {
                    return this.setDuration
                } else {
                    return 100
                }
            },
            _decimals() {
                if (this.setDecimals) {
                    if (this.setDecimals < 0 || this.setDecimals > 20) {
                        alert('digits argument must be between 0 and 20')
                        return 0
                    }
                    return this.setDecimals
                } else {
                    return 0
                }
            },
            _separator() {
                return this.setSeparator
            },
            _suffix() {
                return this.setSuffix
            },
            _prefix() {
                return this.setPrefix
            }
        },
        methods: {
            start() {
                this.$refs.example.start()
            },
            pauseResume() {
                this.$refs.example.pauseResume()
            },
            addInfo() {
                if (this.active++ > 2) this.active = 0;
            }
        }
    }
</script>

<style>
    .top {
        height: 100%;
        width: 100%;
        font-size: 8px;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .12);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
    }
    .example-btn {
        display: inline-block;
        margin-bottom: 0;
        font-weight: 500;
        text-align: center;
        -ms-touch-action: manipulation;
        touch-action: manipulation;
        cursor: pointer;
        background-image: none;
        border: 1px solid transparent;
        white-space: nowrap;
        line-height: 1.5;
        padding: 4px 15px;
        font-size: 12px;
        border-radius: 4px;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
        -webkit-transition: all .3s cubic-bezier(.645, .045, .355, 1);
        transition: all .3s cubic-bezier(.645, .045, .355, 1);
        position: relative;
        color: rgba(0, 0, 0, .65);
        background-color: #fff;
        border-color: #d9d9d9;
    }

    .example-btn:hover {
        color: #4AB7BD;
        background-color: #fff;
        border-color: #4AB7BD;
    }
    .example {
        font-size: 50px;
        color: #F6416C;
        display: block;
        margin: 10px 0;
        text-align: center;
        font-size: 80px;
        font-weight: 500;
    }

    .label {
        color: #2f4f4f;
        font-size: 16px;
        display: inline-block;
        margin: 15px 30px 15px 0;
    }

    input {
        position: relative;
        display: inline-block;
        padding: 4px 7px;
        width: 70px;
        height: 28px;
        cursor: text;
        font-size: 12px;
        line-height: 1.5;
        color: rgba(0, 0, 0, .65);
        background-color: #fff;
        background-image: none;
        border: 1px solid #d9d9d9;
        border-radius: 4px;
        -webkit-transition: all .3s;
        transition: all .3s;
    }

    .startBtn {
        margin-left: 20px;
        font-size: 20px;
        color: #30B08F;
        background-color: #fff;
    }

    .startBtn:hover {
        background-color: #30B08F;
        color: #fff;
        border-color: #30B08F;
    }

    .pause-resume-btn {
        font-size: 20px;
        color: #E65D6E;
        background-color: #fff;
    }

    .pause-resume-btn:hover {
        background-color: #E65D6E;
        color: #fff;
        border-color: #E65D6E;
    }
</style>
