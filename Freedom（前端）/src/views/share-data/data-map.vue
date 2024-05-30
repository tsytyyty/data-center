<template>
<div>
	<div style="width: 94%;margin-left: 3%;margin-top: 2%">
	  <aside>达梦数据库-数据地图</aside>
	</div>
	
	<div style="margin-left: 20px;">
		<div style="width: 174.63px;float: left;">
			<el-tag style="width: 174.63px;font-size: 15px;text-align:center;color: black;" effect="plain">数据表选择</el-tag>
		</div>
		<div>
			<el-tag style="width:1015px;font-weight: 600;font-size: 15px;text-align:center;color: black;margin-left: 5px;" effect="plain">字段信息</el-tag>
		</div>
	</div>
	
	<div class="tab-map" style="margin-left: 20px;">
		<el-tabs :tab-position="tabPosition" style="height: 100%;font-weight: 600;font-size: 30px;">
				<el-tab-pane label="customer_information">
					<el-table :data="customer_information" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
						<el-table-column
							  prop="date"
							  label="字段名"
							  width="500px">
						</el-table-column>
						<el-table-column
							  prop="name"
							  label="注释"
							  width="500px">
						</el-table-column>
					</el-table>
				</el-tab-pane>
				<el-tab-pane label="goods_data">
				<el-table :data="goods_data" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
					<el-table-column
						  prop="date"
						  label="字段名"
						  width="500px">
					</el-table-column>
					<el-table-column
						  prop="name"
						  label="注释"
						  width="500px">
					</el-table-column>
				</el-table>
				</el-tab-pane>
				<el-tab-pane label="goods_port_put">
				<el-table :data="goods_port_put" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
					<el-table-column
						  prop="date"
						  label="字段名"
						  width="500px">
					</el-table-column>
					<el-table-column
						  prop="name"
						  label="注释"
						  width="500px">
					</el-table-column>
				</el-table>
				</el-tab-pane>
				<el-tab-pane label="goods_throughput">
				<el-table :data="goods_throughput" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
					<el-table-column
						  prop="date"
						  label="字段名"
						  width="500px">
					</el-table-column>
					<el-table-column
						  prop="name"
						  label="注释"
						  width="500px">
					</el-table-column>
				</el-table>
				</el-tab-pane>
				<el-tab-pane label="goods_time">
				<el-table :data="goods_time" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
					<el-table-column
						  prop="date"
						  label="字段名"
						  width="500px">
					</el-table-column>
					<el-table-column
						  prop="name"
						  label="注释"
						  width="500px">
					</el-table-column>
				</el-table>
				</el-tab-pane>
				<el-tab-pane label="loading_table">
				<el-table :data="loading_table" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
					<el-table-column
						  prop="date"
						  label="字段名"
						  width="500px">
					</el-table-column>
					<el-table-column
						  prop="name"
						  label="注释"
						  width="500px">
					</el-table-column>
				</el-table>
				</el-tab-pane>
				<el-tab-pane label="logistics_information">
				<el-table :data="logistics_information" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
					<el-table-column
						  prop="date"
						  label="字段名"
						  width="500px">
					</el-table-column>
					<el-table-column
						  prop="name"
						  label="注释"
						  width="500px">
					</el-table-column>
				</el-table>
				</el-tab-pane>
				<el-tab-pane label="port_data">
				<el-table :data="port_data" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
					<el-table-column
						  prop="date"
						  label="字段名"
						  width="500px">
					</el-table-column>
					<el-table-column
						  prop="name"
						  label="注释"
						  width="500px">
					</el-table-column>
				</el-table>
				</el-tab-pane>
				<el-tab-pane label="port_goods_data">
				<el-table :data="port_goods_data" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
					<el-table-column
						  prop="date"
						  label="字段名"
						  width="500px">
					</el-table-column>
					<el-table-column
						  prop="name"
						  label="注释"
						  width="500px">
					</el-table-column>
				</el-table>
				</el-tab-pane>
				<el-tab-pane label="port_goods_trend">
				<el-table :data="port_goods_trend" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
					<el-table-column
						  prop="date"
						  label="字段名"
						  width="500px">
					</el-table-column>
					<el-table-column
						  prop="name"
						  label="注释"
						  width="500px">
					</el-table-column>
				</el-table>
				</el-tab-pane>
				<el-tab-pane label="port_throughput">
				<el-table :data="port_throughput" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
					<el-table-column
						  prop="date"
						  label="字段名"
						  width="500px">
					</el-table-column>
					<el-table-column
						  prop="name"
						  label="注释"
						  width="500px">
					</el-table-column>
				</el-table>
				</el-tab-pane>
				<el-tab-pane label="unloading_table">
				<el-table :data="unloading_table" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
					<el-table-column
						  prop="date"
						  label="字段名"
						  width="500px">
					</el-table-column>
					<el-table-column
						  prop="name"
						  label="注释"
						  width="500px">
					</el-table-column>
				</el-table>
				</el-tab-pane>
		</el-tabs>
	</div>
</div>
</template>

<script>
export default {
  name: "data-map",
	data() {
		return {
			tabPosition: 'left',
			customer_information: [{
			          date: 'name',
			          name: '客户名称',
			        }, {
			          date: 'id',
			          name: '客户编号',
			        }, {
			          date: 'phone_number',
			          name: '手机号',
			        }, {
			          date: 'address',
			          name: '省市区',
			        },{
			          date: 'remark',
			          name: '备注',
					}],
			goods_data: [{
			          date: 'goods',
			          name: '货物',
			        }, {
			          date: 'year',
			          name: '年份',
			        }, {
			          date: 'month',
			          name: '月份',
			        }, {
			          date: 'throughput',
			          name: '吞吐量',
			        }],
			goods_port_put: [{
			          date: 'goods',
			          name: '货物',
			        }, {
			          date: 'port',
			          name: '港口',
			        }, {
			          date: 'put',
			          name: '流量',
			        }, {
			          date: 'percentage',
			          name: '占比',
			        }],
			goods_throughput: [{
			          date: 'goods',
			          name: '货物',
			        }, {
			          date: 'percentage',
			          name: '占比',
			        }, {
			          date: 'throughput',
			          name: '吞吐量',
			        }],
			goods_time: [{
			          date: 'goods',
			          name: '货物',
			        }, {
			          date: 'time',
			          name: '平均流转时间',
			        }],
			loading_table: [{
			          date: 'ship_companies',
			          name: '船公司',
			        }, {
			          date: 'ship_name',
			          name: '船名称',
			        }, {
			          date: 'work_begin_time',
			          name: '作业开始时间',
			        },{
					  date: 'departure',
					  name: '始发时间',
					},{
					  date: 'arrive_time',
					  name: '到达时间',
					},{
					  date: 'port',
					  name: '作业港口',
					},{
					  date: 'order_id',
					  name: '提单号',
					},{
					  date: 'container_id',
					  name: '集装箱号',
					},{
					  date: 'container_size',
					  name: '箱尺寸',
					},{
					  date: 'departure_place',
					  name: '启运地',
					},{
					  date: 'destination_place',
					  name: '目的地',
					},{
					  date: 'remar',
					  name: '备注',
					}],
			logistics_information: [{
			          date: 'id',
			          name: '提单号',
			        }, {
			          date: 'owner',
			          name: '货主名称',
			        }, {
			          date: 'owner_id',
			          name: '货主代码',
			        },{
					  date: 'company_id',
					  name: '物流公司',
					},{
					  date: 'container_id',
					  name: '集装箱号',
					},{
					  date: 'goods_name',
					  name: '货物名称',
					},{
					  date: 'weight',
					  name: '货重',
					},{
					  date: 'remar',
					  name: '备注',
					}],
			port_data: [{
			          date: 'port',
			          name: '港口',
			        }, {
			          date: 'year',
			          name: '年份',
			        }, {
			          date: 'month',
			          name: '月份',
			        }, {
			          date: 'throughput',
			          name: '吞吐量',
			        },{
			          date: 'yoy',
			          name: '同比',
			        }, {
			          date: 'qoq',
			          name: '环比',
			        }],
			port_goods_data: [{
			          date: 'port',
			          name: '港口',
			        }, {
			          date: 'goods',
			          name: '货物',
			        }, {
			          date: 'year',
			          name: '年份',
			        }, {
			          date: 'month',
			          name: '月份',
			        }, {
			          date: 'throughput',
			          name: '吞吐量',
			        }],
			port_goods_trend: [{
			          date: 'port',
			          name: '港口',
			        }, {
			          date: 'goods',
			          name: '货物',
			        }, {
			          date: 'trend',
			          name: '趋势',
			        }],
			port_throughput: [{
			          date: 'port',
			          name: '港口',
			        }, {
			          date: 'percentage',
			          name: '占比',
			        }, {
			          date: 'throughput',
			          name: '吞吐量',
			        }],
			unloading_table: [{
			          date: 'ship_companies',
			          name: '船公司',
			        }, {
			          date: 'ship_name',
			          name: '船名称',
			        }, {
			          date: 'work_begin_time',
			          name: '作业开始时间',
			        },{
					  date: 'departure',
					  name: '始发时间',
					},{
					  date: 'arrive_time',
					  name: '到达时间',
					},{
					  date: 'port',
					  name: '作业港口',
					},{
					  date: 'order_id',
					  name: '提单号',
					},{
					  date: 'container_id',
					  name: '集装箱号',
					},{
					  date: 'container_size',
					  name: '箱尺寸',
					},{
					  date: 'departure_place',
					  name: '启运地',
					},{
					  date: 'destination_place',
					  name: '目的地',
					},{
					  date: 'remar',
					  name: '备注',
					}],
			

		};
	}
}
</script>

<style scoped>

</style>
