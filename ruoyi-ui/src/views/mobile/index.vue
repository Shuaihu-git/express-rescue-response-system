<template>
    <div class="app-container">
      <!-- <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="处理人" prop="dealer">
          <el-input
            v-model="queryParams.dealer"
            placeholder="请输入用户名称"
            clearable
            style="width: 240px;"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="处理状态" prop="type">
          <el-select
            v-model="queryParams.type"
            placeholder="处理状态"
            clearable
            style="width: 240px"
          >
            <el-option
              v-for="dict in dict.type.scan_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="扫码时间">
          <el-date-picker
            v-model="dateRange"
            style="width: 240px"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="['00:00:00', '23:59:59']"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form> -->

  
      <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="救援电话1" align="center" prop="number1" :show-overflow-tooltip="true" />
        <el-table-column label="救援电话2" align="center" prop="number2" :show-overflow-tooltip="true" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['rrqc:mobile:edit']"
          >修改</el-button>
        </template>
      </el-table-column>
      </el-table>
    
  
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
      <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
    
        <el-form-item label="救援电话1" prop="number1">
          <el-input v-model="form.number1" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="救援电话2" prop="number2">
          <el-input v-model="form.number2" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    </div>
  </template>
  
  <script>
  import { list,getTels,updateTels} from "@/api/rrqc/mobile";

  
  export default {
    name: "Mobile",
    dicts: ['scan_type'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 选择用户名
        selectName: "",
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        open: false,
        // 表格数据
        list: [],
        // 日期范围
        dateRange: [],
        form: {},
        // 默认排序
        defaultSort: {prop: 'time', order: 'descending'},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
        },
         // 表单校验
      rules: {
        number1: [
          { required: true, message: "救援电话不能为空", trigger: "blur" }
        ],
        number2: [
          { required: true, message: "救援电话不能为空", trigger: "blur" }
        ]
        
      }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询登录日志列表 */
      getList() {
        this.loading = true;
        list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.list = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
        console.log(this.list);
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.queryParams.pageNum = 1;
        this.$refs.tables.sort(this.defaultSort.prop, this.defaultSort.order)
      },
      /** 多选框选中数据 */
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.logId)
        this.single = selection.length!=1
        this.multiple = !selection.length
        this.selectName = selection.map(item => item.logId);
      },
      /** 排序触发事件 */
      handleSortChange(column, prop, order) {
        this.queryParams.orderByColumn = column.prop;
        this.queryParams.isAsc = column.order;
        this.getList();
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const infoIds = row.infoId || this.ids;
        this.$modal.confirm('是否确认删除访问编号为"' + infoIds + '"的数据项？').then(function() {
          return delLogininfor(infoIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 清空按钮操作 */
      handleClean() {
        this.$modal.confirm('是否确认清空所有登录日志数据项？').then(function() {
          return cleanLogininfor();
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("清空成功");
        }).catch(() => {});
      },
      /** 解锁按钮操作 */
      handleUnlock() {
        const username = this.selectName;
        this.$modal.confirm('是否确认解锁用户"' + username + '"数据项?').then(function() {
          return unlockLogininfor(username);
        }).then(() => {
          this.$modal.msgSuccess("用户" + username + "解锁成功");
        }).catch(() => {});
      },
      
      /** 导出按钮操作 */
      handleExport() {
        this.download('/qrlog/export', {
          ...this.queryParams
        }, `扫码记录_${new Date().getTime()}.xlsx`)
      },
      // 表单重置
    reset() {
      this.form = {
        number1: undefined,
        number2: undefined
      };
      this.resetForm("form");
    },
      /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      console.log("救援电话更新",row);
      // const logId = row.logId || this.ids
      getTels().then(response => {
        this.form = response.data;
        console.log('查询赋值',this.form);
        this.open = true;
        this.title = "救援电话更新";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        
        if (valid) {
          console.log('表单',this.form);
          if (this.form.number1 != undefined && this.form.number2 != undefined) {
            console.log('表格',this.form);
            updateTels(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            console.log("不处理更新");
            // addConfig(this.form).then(response => {
            //   this.$modal.msgSuccess("新增成功");
            //   this.open = false;
            //   this.getList();
            // });
          }
        }
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    }
  };
  </script>
  
  