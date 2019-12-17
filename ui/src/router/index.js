import Vue from 'vue';
import VueRouter from 'vue-router';
import Layout from '../layout/Layout';
//登录
import login from '../page/login/login.vue';
//注册
import register from '../page/login/register.vue';
//菜单展示页
import view from '../page/view/view.vue';
//首页
import home from '../page/home/home';
//参数管理页面
import paramSetting from "../page/setting/paramSetting";
import cnArea from "../page/setting/CnArea";
import location from "../page/setting/location";

//系统用户
import sysUser from '../page/systemConfig/sysUser';
//系统菜单
import sysMenu from '../page/systemConfig/sysMenu';
//系统角色
import sysRole from '../page/systemConfig/sysRole';
//系统部门
import sysDept from '../page/systemConfig/sysDept';
//系统操作日志
import syslog from '../page/systemConfig/syslog';
//系统接口日志
import apiRecordLog from '../page/systemConfig/apiRecordLog.vue'

import sysGenerator from '../page/systemConfig/sysGenerator';
//组件展示
import sysAnnounce from '../page/component/sysAnnounce';
import components from '../page/component/components';
import icons from '../page/component/icons/index';
import server from '../page/component/server.vue'
import Chat from '../page/chat/FriendChat.vue';

Vue.use(VueRouter);

const router = new VueRouter({
    //mode: 'history',
    scrollBehavior: () => ({y: 0}),
    routes: [
        {
            name: '',
            path: '/',
            redirect: '/login',
            hidden: true
        },
        {
            name: '登录',
            path: '/login',
            component: login,
            redirect: '/login',
            meta: { title: '登录'},
            children: [
                {
                    name: 'login',
                    path: '/login',
                    component: home,
                    meta: { title: '登录'}
                }
            ]
        },
        {
            name: '注册',
            path: '/register',
            component: register,
            redirect: '/register',
            meta: { title: '注册'},
            children: [
                {
                    name: 'register',
                    path: '/register',
                    component: home,
                    meta: { title: '注册'}
                }
            ]
        },
        {
            name: 'view',
            path: '/view',
            component: view,
            redirect: '/view',
            meta: { title: 'view'},
            children: [
                {
                    name: 'view',
                    path: '/view',
                    component: view,
                    meta: { title: 'view'}
                }
            ]
        },
        {
            name: 'home',
            path: '/home',
            component: Layout,
            redirect: '/home',
            meta: { title: '首页'},
            children: [
                {
                    name: 'home',
                    path: '/home',
                    component: home,
                    meta: { title: '首页'}
                }
            ]
        },

        {
            path: '/redirect',
            component: Layout,
            hidden: true,
            children: [
                {
                    path: '/redirect/:path*',
                    component: () => import('../page/redirect/index')
                }
            ]
        },
        {
            name: '权限系统管理',
            path: '/systemModel',
            component: Layout,
            redirect: '/systemModel',
            meta: { title: '权限系统管理'},
            children: [
                {
                    name: 'sysUser',
                    path: '/sysUser',
                    component: sysUser,
                    meta: { title: '系统用户'}
                },
                {
                    name: 'sysMenu',
                    path: '/sysMenu',
                    component: sysMenu,
                    meta: { title: '系统菜单'}
                },
                {
                    name: 'sysRole',
                    path: '/sysRole',
                    component: sysRole,
                    meta: { title: '系统角色'}
                },
                {
                    name: 'sysDept',
                    path: '/sysDept',
                    component: sysDept,
                    meta: { title: '系统部门'}
                },
                {
                    name: 'sysGenerator',
                    path: '/sysGenerator',
                    component: sysGenerator,
                    meta: { title: '代码生成器'}
                }
            ]
        },
        {
            name: 'component',
            path: '/component',
            component: Layout,
            redirect: '/component',
            meta: { title: '组件管理'},
            children: [
                {
                    name: 'sysAnnounce',
                    path: '/sysAnnounce',
                    component: sysAnnounce,
                    meta: { title: '富文本编辑'}
                },{
                    name: 'components',
                    path: '/components',
                    component: components,
                    meta: { title: '小组件'}
                },{
                    name: 'icons',
                    path: '/icons',
                    component: icons,
                    meta: { title: '按钮集'}
                }
                ,{
                    name: 'server',
                    path: '/server',
                    component: server,
                    meta: { title: '服务监控'}
                }
                ,{
                    name: 'Chat',
                    path: '/Chat',
                    component: Chat,
                    meta: { title: '聊天'}
                }

            ]
        },
        {
            name: 'logManage',
            path: '/logManage',
            component: Layout,
            redirect: '/logManage',
            meta: { title: '日志管理'},
            children: [
                {
                    name: 'syslog',
                    path: '/syslog',
                    component: syslog,
                    meta: { title: '系统操作日志'}
                },
                {
                    name: 'apiRecordLog',
                    path: '/apiRecordLog',
                    component: apiRecordLog,
                    meta: { title: '系统接口日志'}
                }
            ]
        },
        {
            name: 'param',
            path: '/param',
            component: Layout,
            redirect: '/paramSetting',
            meta: { title: '系统参数'},
            children: [
                {
                    name: 'paramSetting',
                    path: '/paramSetting',
                    component: paramSetting,
                    meta: { title: '系统参数'}
                },{
                    name: 'cnArea',
                    path: '/cnArea',
                    component: cnArea,
                    meta: { title: '中国行政地区'}
                },{
                    name: 'location',
                    path: '/location',
                    component: location,
                    meta: { title: '百度定位'}
                }
            ]
        }

    ]
});

export default router;
