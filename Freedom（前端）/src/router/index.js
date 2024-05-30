import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/* Router Modules */
import componentsRouter from './modules/components'
import chartsRouter from './modules/charts'
import tableRouter from './modules/table'
import nestedRouter from './modules/nested'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },

  {
    path: '/',
    component: () => import('@/views/login/index'),
    hidden: true,
    children: [
      {
        path: 'login',
        component: () => import('@/views/dashboard/index'),
      }
    ]
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/dashboard',
    component: Layout,
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: '物流信息数据中台系统',
        meta: { title: '物流信息数据中台系统', icon: 'dashboard', affix: true }
      }
    ]
  },

]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  {
    path: '/documentation',
    component: Layout,
    meta: {
      title: '数据采集系统',
      icon: 'excel',
      roles: ['admin']
    },
    children: [
      {
        path: 'index',
        component: () => import('@/views/documentation/index'),
        name: '数据采集',
        meta: { title: '数据采集', affix: true ,roles: ['admin']}
      },
      {
        path: 'rules',
        component: () => import('@/views/documentation/rulesPage'),
        name: '规则配置',
        meta: { title: '规则配置', affix: true ,roles: ['admin']}
      }
    ]
  },

  {
    path: '/share',
    component: Layout,
    redirect: 'noRedirect',
    name: 'index1',
    meta: {
      title: '数据存储共享',
      icon: 'excel'
    },
    children: [
      {
        path: 'index1',
        component: () => import('@/views/share-data/dataStorage'),
        name: 'index1',
        meta: { title: '数据存储', noCache: true }
      },
      {
        path: 'index2',
        component: () => import('@/views/share-data/index2'),
        name: 'index2',
        meta: { title: '接口服务', noCache: true },
      },
      {
        path: 'sql-work',
        component: () => import('@/views/share-data/sql-work'),
        name: 'sql-work',
        meta: { title: 'SQL工作台', noCache: true },
      },
      {
        path: 'data-map',
        component: () => import('@/views/share-data/data-map'),
        name: 'data-map',
        meta: { title: '数据地图', noCache: true },
      },
      {
        path: 'show-JSON',
        component: () => import('@/views/share-data/show-JSON'),
        name: 'show-JSON',

        props($route) {
          return {
            jsonStr: $route.params.jsonStr,
          }
        }
      },
      {
        path: 'dataStorage',
        component: () => import('@/views/share-data/dataStorage'),
        name: 'dataStorage',
      },
    ]
  },
  {
    path: '/permission',
    component: Layout,
    redirect: '/permission/page',
    alwaysShow: true, // will always show the root menu
    name: '数据治理模块',
    meta: {
      title: '数据治理模块',
      icon: 'lock',
      roles: ['admin'] // you can set roles in root nav
    },
    children: [
      {
        path: 'page',
        component: () => import('@/views/permission/page'),
        name: '异常数据处理',
        meta: {
          title: '异常数据处理',
        },
        props($route) {
          return {
            allData: $route.params.allData,
          }
        }
      },
      {
        path: 'TabPane',
        component: () => import('@/views/permission/components/TabPane'),
        name: 'wlxx',
      }
    ]
  },

  {
    path: '/alz',
    component: Layout,
    meta: {
      title: '物流信息分析系统',
      icon: 'chart'
    },
    children: [
     
      {
        path: 'chart',
        component: () => import('@/views/guide/chart'),
        name: '物流信息分析系统',
        meta: { title: '物流信息分析系统', affix: true },
      },
     
    ]
  },

  {
    path: '/user',
    component: Layout,
    meta: {
      title: '用户管理',
      icon: 'chart',
      roles: ['admin']
    },
    children: [
      {
        path: 'manger',
        component: () => import('@/views/user/userdata/index'),
        name: '用户信息权限管理',
        meta: { title: '用户信息权限管理',  affix: true },
      },
      {
        path: 'reg',
        component: () => import('@/views/user/reg/index'),
        name: '注册确认',
        meta: { title: '注册确认', affix: true },
      },
    ]
  },


  /** when your routing map is too long, you can split it into small modules **/
  componentsRouter,
  chartsRouter,
  nestedRouter,
  tableRouter,




  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
