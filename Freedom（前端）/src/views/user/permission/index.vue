<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAddRole">New Role</el-button>

    <el-table :data="rolesList" style="width: 100%;margin-top:30px;" border>
      <el-table-column align="center" label="Role Key" width="220">
        <template slot-scope="scope">
          {{ scope.row.key }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="Role Name" width="220">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column align="header-center" label="Description">
        <template slot-scope="scope">
          {{ scope.row.description }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="Operations">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope)">Edit</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogVisible" :title="dialogType==='edit'?'Edit Role':'New Role'">
      <el-form :model="role" label-width="80px" label-position="left">
        <el-form-item label="Name">
          <el-input v-model="role.name" placeholder="Role Name" />
        </el-form-item>
        <el-form-item label="Desc">
          <el-input
            v-model="role.description"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="Role Description"
          />
        </el-form-item>
        <el-form-item label="Menus">
          <el-tree
            ref="tree"
            :check-strictly="checkStrictly"
            :data="routesData"
            :props="defaultProps"
            show-checkbox
            node-key="path"
            class="permission-tree"
          />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">Cancel</el-button>
        <el-button type="primary" @click="confirmRole">Confirm</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import path from 'path'
import { deepClone } from '@/utils'
import { getRoutes, getRoles, addRole, deleteRole, updateRole } from '@/api/role'

const defaultRole = {
  key: '',
  name: '',
  description: '',
  routes: []
}

export default {
  data() {
    return {
      role: Object.assign({}, defaultRole),
      routes: [],
      rolesList: [],
      dialogVisible: false,
      dialogType: 'new',
      checkStrictly: false,
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      per: '{\n' +
        '\t"code": 20000,\n' +
        '\t"data": [{\n' +
        '\t\t\t"path": "/redirect",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"hidden": true,\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t"path": "/redirect/:path*",\n' +
        '\t\t\t\t"component": "views/redirect/index"\n' +
        '\t\t\t}]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/login",\n' +
        '\t\t\t"component": "views/login/index",\n' +
        '\t\t\t"hidden": true\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/auth-redirect",\n' +
        '\t\t\t"component": "views/login/auth-redirect",\n' +
        '\t\t\t"hidden": true\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/404",\n' +
        '\t\t\t"component": "views/error-page/404",\n' +
        '\t\t\t"hidden": true\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/401",\n' +
        '\t\t\t"component": "views/error-page/401",\n' +
        '\t\t\t"hidden": true\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "dashboard",\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t"path": "/dashboard",\n' +
        '\t\t\t\t"component": "views/dashboard/index",\n' +
        '\t\t\t\t"name": "Dashboard",\n' +
        '\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t"title": "Dashboard",\n' +
        '\t\t\t\t\t"icon": "dashboard",\n' +
        '\t\t\t\t\t"affix": true\n' +
        '\t\t\t\t}\n' +
        '\t\t\t}]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/documentation",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t"path": "/documentation/index",\n' +
        '\t\t\t\t"component": "views/documentation/index",\n' +
        '\t\t\t\t"name": "数据提取系统",\n' +
        '\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t"title": "数据提取系统",\n' +
        '\t\t\t\t\t"icon": "documentation",\n' +
        '\t\t\t\t\t"affix": true\n' +
        '\t\t\t\t}\n' +
        '\t\t\t}]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/guide",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "/guide/index",\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t"path": "/guide/index",\n' +
        '\t\t\t\t"component": "views/guide/index",\n' +
        '\t\t\t\t"name": "Guide",\n' +
        '\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t"title": "Guide",\n' +
        '\t\t\t\t\t"icon": "guide",\n' +
        '\t\t\t\t\t"noCache": true\n' +
        '\t\t\t\t}\n' +
        '\t\t\t}]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/permission",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "/permission/index",\n' +
        '\t\t\t"alwaysShow": true,\n' +
        '\t\t\t"meta": {\n' +
        '\t\t\t\t"title": "Permission",\n' +
        '\t\t\t\t"icon": "lock",\n' +
        '\t\t\t\t"roles": [\n' +
        '\t\t\t\t\t"admin",\n' +
        '\t\t\t\t\t"editor"\n' +
        '\t\t\t\t]\n' +
        '\t\t\t},\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t\t"path": "page",\n' +
        '\t\t\t\t\t"component": "views/permission/page",\n' +
        '\t\t\t\t\t"name": "PagePermission",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Page Permission",\n' +
        '\t\t\t\t\t\t"roles": [\n' +
        '\t\t\t\t\t\t\t"admin"\n' +
        '\t\t\t\t\t\t]\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "directive",\n' +
        '\t\t\t\t\t"component": "views/permission/directive",\n' +
        '\t\t\t\t\t"name": "DirectivePermission",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Directive Permission"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "role",\n' +
        '\t\t\t\t\t"component": "views/permission/role",\n' +
        '\t\t\t\t\t"name": "RolePermission",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Role Permission",\n' +
        '\t\t\t\t\t\t"roles": [\n' +
        '\t\t\t\t\t\t\t"admin"\n' +
        '\t\t\t\t\t\t]\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t}\n' +
        '\t\t\t]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/icon",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t"path": "/icon/index",\n' +
        '\t\t\t\t"component": "views/icons/index",\n' +
        '\t\t\t\t"name": "Icons",\n' +
        '\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t"title": "Icons",\n' +
        '\t\t\t\t\t"icon": "icon",\n' +
        '\t\t\t\t\t"noCache": true\n' +
        '\t\t\t\t}\n' +
        '\t\t\t}]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/components",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "noRedirect",\n' +
        '\t\t\t"name": "ComponentDemo",\n' +
        '\t\t\t"meta": {\n' +
        '\t\t\t\t"title": "Components",\n' +
        '\t\t\t\t"icon": "component"\n' +
        '\t\t\t},\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t\t"path": "tinymce",\n' +
        '\t\t\t\t\t"component": "views/components-demo/tinymce",\n' +
        '\t\t\t\t\t"name": "TinymceDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Tinymce"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "markdown",\n' +
        '\t\t\t\t\t"component": "views/components-demo/markdown",\n' +
        '\t\t\t\t\t"name": "MarkdownDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Markdown"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "json-editor",\n' +
        '\t\t\t\t\t"component": "views/components-demo/json-editor",\n' +
        '\t\t\t\t\t"name": "JsonEditorDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Json Editor"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "split-pane",\n' +
        '\t\t\t\t\t"component": "views/components-demo/split-pane",\n' +
        '\t\t\t\t\t"name": "SplitpaneDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "SplitPane"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "avatar-upload",\n' +
        '\t\t\t\t\t"component": "views/components-demo/avatar-upload",\n' +
        '\t\t\t\t\t"name": "AvatarUploadDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Avatar Upload"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "dropzone",\n' +
        '\t\t\t\t\t"component": "views/components-demo/dropzone",\n' +
        '\t\t\t\t\t"name": "DropzoneDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Dropzone"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "sticky",\n' +
        '\t\t\t\t\t"component": "views/components-demo/sticky",\n' +
        '\t\t\t\t\t"name": "StickyDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Sticky"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "count-to",\n' +
        '\t\t\t\t\t"component": "views/components-demo/count-to",\n' +
        '\t\t\t\t\t"name": "CountToDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Count To"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "mixin",\n' +
        '\t\t\t\t\t"component": "views/components-demo/mixin",\n' +
        '\t\t\t\t\t"name": "ComponentMixinDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "componentMixin"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "back-to-top",\n' +
        '\t\t\t\t\t"component": "views/components-demo/back-to-top",\n' +
        '\t\t\t\t\t"name": "BackToTopDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Back To Top"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "drag-dialog",\n' +
        '\t\t\t\t\t"component": "views/components-demo/drag-dialog",\n' +
        '\t\t\t\t\t"name": "DragDialogDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Drag Dialog"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "drag-select",\n' +
        '\t\t\t\t\t"component": "views/components-demo/drag-select",\n' +
        '\t\t\t\t\t"name": "DragSelectDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Drag Select"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "dnd-list",\n' +
        '\t\t\t\t\t"component": "views/components-demo/dnd-list",\n' +
        '\t\t\t\t\t"name": "DndListDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Dnd List"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "drag-kanban",\n' +
        '\t\t\t\t\t"component": "views/components-demo/drag-kanban",\n' +
        '\t\t\t\t\t"name": "DragKanbanDemo",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Drag Kanban"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t}\n' +
        '\t\t\t]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/charts",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "noRedirect",\n' +
        '\t\t\t"name": "Charts",\n' +
        '\t\t\t"meta": {\n' +
        '\t\t\t\t"title": "Charts",\n' +
        '\t\t\t\t"icon": "chart"\n' +
        '\t\t\t},\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t\t"path": "keyboard",\n' +
        '\t\t\t\t\t"component": "views/charts/keyboard",\n' +
        '\t\t\t\t\t"name": "KeyboardChart",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Keyboard Chart",\n' +
        '\t\t\t\t\t\t"noCache": true\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "line",\n' +
        '\t\t\t\t\t"component": "views/charts/line",\n' +
        '\t\t\t\t\t"name": "LineChart",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Line Chart",\n' +
        '\t\t\t\t\t\t"noCache": true\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "mixchart",\n' +
        '\t\t\t\t\t"component": "views/charts/mixChart",\n' +
        '\t\t\t\t\t"name": "MixChart",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Mix Chart",\n' +
        '\t\t\t\t\t\t"noCache": true\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t}\n' +
        '\t\t\t]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/nested",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "/nested/menu1/menu1-1",\n' +
        '\t\t\t"name": "Nested",\n' +
        '\t\t\t"meta": {\n' +
        '\t\t\t\t"title": "Nested",\n' +
        '\t\t\t\t"icon": "nested"\n' +
        '\t\t\t},\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t\t"path": "menu1",\n' +
        '\t\t\t\t\t"component": "views/nested/menu1/index",\n' +
        '\t\t\t\t\t"name": "Menu1",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Menu1"\n' +
        '\t\t\t\t\t},\n' +
        '\t\t\t\t\t"redirect": "/nested/menu1/menu1-1",\n' +
        '\t\t\t\t\t"children": [{\n' +
        '\t\t\t\t\t\t\t"path": "menu1-1",\n' +
        '\t\t\t\t\t\t\t"component": "views/nested/menu1/menu1-1",\n' +
        '\t\t\t\t\t\t\t"name": "Menu1-1",\n' +
        '\t\t\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t\t\t"title": "Menu1-1"\n' +
        '\t\t\t\t\t\t\t}\n' +
        '\t\t\t\t\t\t},\n' +
        '\t\t\t\t\t\t{\n' +
        '\t\t\t\t\t\t\t"path": "menu1-2",\n' +
        '\t\t\t\t\t\t\t"component": "views/nested/menu1/menu1-2",\n' +
        '\t\t\t\t\t\t\t"name": "Menu1-2",\n' +
        '\t\t\t\t\t\t\t"redirect": "/nested/menu1/menu1-2/menu1-2-1",\n' +
        '\t\t\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t\t\t"title": "Menu1-2"\n' +
        '\t\t\t\t\t\t\t},\n' +
        '\t\t\t\t\t\t\t"children": [{\n' +
        '\t\t\t\t\t\t\t\t\t"path": "menu1-2-1",\n' +
        '\t\t\t\t\t\t\t\t\t"component": "views/nested/menu1/menu1-2/menu1-2-1",\n' +
        '\t\t\t\t\t\t\t\t\t"name": "Menu1-2-1",\n' +
        '\t\t\t\t\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t\t\t\t\t"title": "Menu1-2-1"\n' +
        '\t\t\t\t\t\t\t\t\t}\n' +
        '\t\t\t\t\t\t\t\t},\n' +
        '\t\t\t\t\t\t\t\t{\n' +
        '\t\t\t\t\t\t\t\t\t"path": "menu1-2-2",\n' +
        '\t\t\t\t\t\t\t\t\t"component": "views/nested/menu1/menu1-2/menu1-2-2",\n' +
        '\t\t\t\t\t\t\t\t\t"name": "Menu1-2-2",\n' +
        '\t\t\t\t\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t\t\t\t\t"title": "Menu1-2-2"\n' +
        '\t\t\t\t\t\t\t\t\t}\n' +
        '\t\t\t\t\t\t\t\t}\n' +
        '\t\t\t\t\t\t\t]\n' +
        '\t\t\t\t\t\t},\n' +
        '\t\t\t\t\t\t{\n' +
        '\t\t\t\t\t\t\t"path": "menu1-3",\n' +
        '\t\t\t\t\t\t\t"component": "views/nested/menu1/menu1-3",\n' +
        '\t\t\t\t\t\t\t"name": "Menu1-3",\n' +
        '\t\t\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t\t\t"title": "Menu1-3"\n' +
        '\t\t\t\t\t\t\t}\n' +
        '\t\t\t\t\t\t}\n' +
        '\t\t\t\t\t]\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "menu2",\n' +
        '\t\t\t\t\t"name": "Menu2",\n' +
        '\t\t\t\t\t"component": "views/nested/menu2/index",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Menu2"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t}\n' +
        '\t\t\t]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/example",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "/example/list",\n' +
        '\t\t\t"name": "Example",\n' +
        '\t\t\t"meta": {\n' +
        '\t\t\t\t"title": "Example",\n' +
        '\t\t\t\t"icon": "example"\n' +
        '\t\t\t},\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t\t"path": "create",\n' +
        '\t\t\t\t\t"component": "views/example/create",\n' +
        '\t\t\t\t\t"name": "CreateArticle",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Create Article",\n' +
        '\t\t\t\t\t\t"icon": "edit"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "edit/:id(\\\\d+)",\n' +
        '\t\t\t\t\t"component": "views/example/edit",\n' +
        '\t\t\t\t\t"name": "EditArticle",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Edit Article",\n' +
        '\t\t\t\t\t\t"noCache": true\n' +
        '\t\t\t\t\t},\n' +
        '\t\t\t\t\t"hidden": true\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "list",\n' +
        '\t\t\t\t\t"component": "views/example/list",\n' +
        '\t\t\t\t\t"name": "ArticleList",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Article List",\n' +
        '\t\t\t\t\t\t"icon": "list"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t}\n' +
        '\t\t\t]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/tab",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t"path": "/tab/index",\n' +
        '\t\t\t\t"component": "views/tab/index",\n' +
        '\t\t\t\t"name": "Tab",\n' +
        '\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t"title": "Tab",\n' +
        '\t\t\t\t\t"icon": "tab"\n' +
        '\t\t\t\t}\n' +
        '\t\t\t}]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/error",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "noRedirect",\n' +
        '\t\t\t"name": "ErrorPages",\n' +
        '\t\t\t"meta": {\n' +
        '\t\t\t\t"title": "Error Pages",\n' +
        '\t\t\t\t"icon": "404"\n' +
        '\t\t\t},\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t\t"path": "401",\n' +
        '\t\t\t\t\t"component": "views/error-page/401",\n' +
        '\t\t\t\t\t"name": "Page401",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Page 401",\n' +
        '\t\t\t\t\t\t"noCache": true\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "404",\n' +
        '\t\t\t\t\t"component": "views/error-page/404",\n' +
        '\t\t\t\t\t"name": "Page404",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Page 404",\n' +
        '\t\t\t\t\t\t"noCache": true\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t}\n' +
        '\t\t\t]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/error-log",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "noRedirect",\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t"path": "/error-log/log",\n' +
        '\t\t\t\t"component": "views/error-log/index",\n' +
        '\t\t\t\t"name": "ErrorLog",\n' +
        '\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t"title": "Error Log",\n' +
        '\t\t\t\t\t"icon": "bug"\n' +
        '\t\t\t\t}\n' +
        '\t\t\t}]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/excel",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "/excel/export-excel",\n' +
        '\t\t\t"name": "Excel",\n' +
        '\t\t\t"meta": {\n' +
        '\t\t\t\t"title": "Excel",\n' +
        '\t\t\t\t"icon": "excel"\n' +
        '\t\t\t},\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t\t"path": "export-excel",\n' +
        '\t\t\t\t\t"component": "views/excel/export-excel",\n' +
        '\t\t\t\t\t"name": "ExportExcel",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Export Excel"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "export-selected-excel",\n' +
        '\t\t\t\t\t"component": "views/excel/select-excel",\n' +
        '\t\t\t\t\t"name": "SelectExcel",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Select Excel"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "export-merge-header",\n' +
        '\t\t\t\t\t"component": "views/excel/merge-header",\n' +
        '\t\t\t\t\t"name": "MergeHeader",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Merge Header"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t},\n' +
        '\t\t\t\t{\n' +
        '\t\t\t\t\t"path": "upload-excel",\n' +
        '\t\t\t\t\t"component": "views/excel/upload-excel",\n' +
        '\t\t\t\t\t"name": "UploadExcel",\n' +
        '\t\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t\t"title": "Upload Excel"\n' +
        '\t\t\t\t\t}\n' +
        '\t\t\t\t}\n' +
        '\t\t\t]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/zip",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "/zip/download",\n' +
        '\t\t\t"alwaysShow": true,\n' +
        '\t\t\t"meta": {\n' +
        '\t\t\t\t"title": "Zip",\n' +
        '\t\t\t\t"icon": "zip"\n' +
        '\t\t\t},\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t"path": "/zip/download",\n' +
        '\t\t\t\t"component": "views/zip/index",\n' +
        '\t\t\t\t"name": "ExportZip",\n' +
        '\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t"title": "Export Zip"\n' +
        '\t\t\t\t}\n' +
        '\t\t\t}]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/pdf",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "/pdf/index",\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t"path": "/pdf/index",\n' +
        '\t\t\t\t"component": "views/pdf/index",\n' +
        '\t\t\t\t"name": "PDF",\n' +
        '\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t"title": "PDF",\n' +
        '\t\t\t\t\t"icon": "pdf"\n' +
        '\t\t\t\t}\n' +
        '\t\t\t}]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/pdf/download",\n' +
        '\t\t\t"component": "views/pdf/download",\n' +
        '\t\t\t"hidden": true\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/theme",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "noRedirect",\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t"path": "/theme/index",\n' +
        '\t\t\t\t"component": "views/theme/index",\n' +
        '\t\t\t\t"name": "Theme",\n' +
        '\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t"title": "Theme",\n' +
        '\t\t\t\t\t"icon": "theme"\n' +
        '\t\t\t\t}\n' +
        '\t\t\t}]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/clipboard",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"redirect": "noRedirect",\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t"path": "/clipboard/index",\n' +
        '\t\t\t\t"component": "views/clipboard/index",\n' +
        '\t\t\t\t"name": "ClipboardDemo",\n' +
        '\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t"title": "Clipboard Demo",\n' +
        '\t\t\t\t\t"icon": "clipboard"\n' +
        '\t\t\t\t}\n' +
        '\t\t\t}]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "/i18n",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t"path": "/i18n/index",\n' +
        '\t\t\t\t"component": "views/i18n-demo/index",\n' +
        '\t\t\t\t"name": "I18n",\n' +
        '\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t"title": "I18n",\n' +
        '\t\t\t\t\t"icon": "international"\n' +
        '\t\t\t\t}\n' +
        '\t\t\t}]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "external-link",\n' +
        '\t\t\t"component": "layout/Layout",\n' +
        '\t\t\t"children": [{\n' +
        '\t\t\t\t"path": "/external-link/https:/github.com/PanJiaChen/vue-element-admin",\n' +
        '\t\t\t\t"meta": {\n' +
        '\t\t\t\t\t"title": "External Link",\n' +
        '\t\t\t\t\t"icon": "link"\n' +
        '\t\t\t\t}\n' +
        '\t\t\t}]\n' +
        '\t\t},\n' +
        '\t\t{\n' +
        '\t\t\t"path": "*",\n' +
        '\t\t\t"redirect": "/404",\n' +
        '\t\t\t"hidden": true\n' +
        '\t\t}\n' +
        '\t]\n' +
        '}'
    }
  },
  computed: {
    routesData() {
      return this.routes
    }
  },
  created() {
    // Mock: get all routes and roles list from server
    this.getRoutes()
    this.getRoles()
  },
  methods: {
    async getRoutes() {
      console.log('asd')
      const res = await getRoutes()
      console.log(res)
      this.serviceRoutes = res.data
      this.routes = this.generateRoutes(res.data)
      console.log(this.routes)
      this.routes[1].title = 'ajsisjai'
    },
    async getRoles() {
      const res = await getRoles()
      this.rolesList = res.data
    },

    // Reshape the routes structure so that it looks the same as the sidebar
    generateRoutes(routes, basePath = '/') {
      const res = []

      for (let route of routes) {
        // skip some route
        if (route.hidden) { continue }

        const onlyOneShowingChild = this.onlyOneShowingChild(route.children, route)

        if (route.children && onlyOneShowingChild && !route.alwaysShow) {
          route = onlyOneShowingChild
        }

        const data = {
          path: path.resolve(basePath, route.path),
          title: route.meta && route.meta.title

        }

        // recursive child routes
        if (route.children) {
          data.children = this.generateRoutes(route.children, data.path)
        }
        res.push(data)
      }
      return res
    },
    generateArr(routes) {
      let data = []
      routes.forEach(route => {
        data.push(route)
        if (route.children) {
          const temp = this.generateArr(route.children)
          if (temp.length > 0) {
            data = [...data, ...temp]
          }
        }
      })
      return data
    },
    handleAddRole() {
      this.role = Object.assign({}, defaultRole)
      if (this.$refs.tree) {
        this.$refs.tree.setCheckedNodes([])
      }
      this.dialogType = 'new'
      this.dialogVisible = true
    },
    handleEdit(scope) {
      this.dialogType = 'edit'
      this.dialogVisible = true
      this.checkStrictly = true
      this.role = deepClone(scope.row)
      this.$nextTick(() => {
        const routes = this.generateRoutes(this.role.routes)
        this.$refs.tree.setCheckedNodes(this.generateArr(routes))
        // set checked state of a node not affects its father and child nodes
        this.checkStrictly = false
      })
    },
    handleDelete({ $index, row }) {
      this.$confirm('Confirm to remove the role?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
        .then(async() => {
          await deleteRole(row.key)
          this.rolesList.splice($index, 1)
          this.$message({
            type: 'success',
            message: 'Delete succed!'
          })
        })
        .catch(err => { console.error(err) })
    },
    generateTree(routes, basePath = '/', checkedKeys) {
      const res = []

      for (const route of routes) {
        const routePath = path.resolve(basePath, route.path)

        // recursive child routes
        if (route.children) {
          route.children = this.generateTree(route.children, routePath, checkedKeys)
        }

        if (checkedKeys.includes(routePath) || (route.children && route.children.length >= 1)) {
          res.push(route)
        }
      }
      return res
    },
    async confirmRole() {
      const isEdit = this.dialogType === 'edit'

      const checkedKeys = this.$refs.tree.getCheckedKeys()
      this.role.routes = this.generateTree(deepClone(this.serviceRoutes), '/', checkedKeys)

      if (isEdit) {
        await updateRole(this.role.key, this.role)
        for (let index = 0; index < this.rolesList.length; index++) {
          if (this.rolesList[index].key === this.role.key) {
            this.rolesList.splice(index, 1, Object.assign({}, this.role))
            break
          }
        }
      } else {
        const { data } = await addRole(this.role)
        this.role.key = data.key
        this.rolesList.push(this.role)
      }

      const { description, key, name } = this.role
      this.dialogVisible = false
      this.$notify({
        title: 'Success',
        dangerouslyUseHTMLString: true,
        message: `
            <div>Role Key: ${key}</div>
            <div>Role Name: ${name}</div>
            <div>Description: ${description}</div>
          `,
        type: 'success'
      })
    },
    // reference: src/view/layout/components/Sidebar/SidebarItem.vue
    onlyOneShowingChild(children = [], parent) {
      let onlyOneChild = null
      const showingChildren = children.filter(item => !item.hidden)

      // When there is only one child route, the child route is displayed by default
      if (showingChildren.length === 1) {
        onlyOneChild = showingChildren[0]
        onlyOneChild.path = path.resolve(parent.path, onlyOneChild.path)
        return onlyOneChild
      }

      // Show parent if there are no child route to display
      if (showingChildren.length === 0) {
        onlyOneChild = { ... parent, path: '', noShowingChildren: true }
        return onlyOneChild
      }

      return false
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  .roles-table {
    margin-top: 30px;
  }
  .permission-tree {
    margin-bottom: 30px;
  }
}
</style>
