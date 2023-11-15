<template>
  <div>
    <h1>관리자 페이지</h1>
    <h2>데이터 맵</h2>
    <div class="button-group">
      <button @click="navigateToMain">표준 용어</button>
      <button :disabled="$route.path === '/admin-datamap'">데이터 맵</button>
      <button @click="navigateToUserRecord">사용자 이력 조회</button>
    </div>

    <div class="list-btn">
      <button @click="ListTable">테이블</button>
      <button @click="ListTerminology">용어</button>
    </div>

    <div v-if="showTableList">
      <h3>테이블 목록</h3>
      <ul>
        <li v-for="table in tableList" :key="table.no" @click="handleTableClick(table.logicalTableName)">
          {{ table.logicalTableName }}
        </li>
      </ul>
    </div>

    <div v-if="showTerminologyList">
      <h3>용어 목록</h3>
      <ul>
        <li v-for="term in terminologyList" :key="term" @click="handleTermClick(term)">
          {{ term }}
        </li>
      </ul>
    </div>

    <div ref="chartdiv" style="width: 100%; height: 500px;"></div>
  </div>
</template>

<script>
import axios from 'axios';
import * as am5 from "@amcharts/amcharts5";
import am5hierarchy from "@amcharts/amcharts5/hierarchy";
// 하위 모듈 및 테마 임포트 생략

export default {
  name: 'AdminDataMapPage',
  data() {
    return {
      tableData: [],
      tableList: [],
      terminologyList: [],
      showTableList: false,
      showTerminologyList: false,
      chart: null,
    };
  },
  created() {
    this.fetchTableData();
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose();
    }
  },
  methods: {
    // 데이터 가져오기 및 처리
    async fetchTableData() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:3001/tableInfo', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        this.tableData = response.data.data;
        this.tableList = this.tableData.map(item => ({
          no: item.no,
          logicalTableName: item.logicalTableName
        }));
        this.terminologyList = [...new Set(this.tableData.flatMap(item => item.stdTerminologyList))];
      } catch (error) {
        console.error('Error fetching table data:', error);
      }
    },

    // 차트 생성
    createChartData() {
      return this.tableData.map(table => ({
        name: table.logicalTableName,
        children: table.stdTerminologyList.map(term => ({ name: term }))
      }));
    },
    createChart() {
      const root = am5.Root.new(this.$refs.chartdiv);

      let chart = root.container.children.push(am5hierarchy.ForceDirected.new(root, {
        singleBranchOnly: false,
        downDepth: 1
      }));

      let series = chart.series.push(am5hierarchy.ForceDirectedSeries.new(root, {
        singleBranchOnly: false,
        downDepth: 1,
        valueField: "value",
        nameField: "name",
        childrenField: "children"
      }));

      series.data.setAll(this.createChartData());
      series.nodes.template.setAll({
        draggable: true,
        tooltipText: "{name}"
      });

      this.chart = chart;
    },

    // UI 상호작용
    navigateToMain() {
      this.$router.push('/admin-main');
    },
    navigateToUserRecord() {
      this.$router.push('/admin-user-record');
    },
    ListTable() {
      this.showTableList = true;
      this.showTerminologyList = false;
    },
    ListTerminology() {
      this.showTerminologyList = true;
      this.showTableList = false;
    },
    handleTableClick(logicalTableName) {
      console.log(logicalTableName);
    },
    handleTermClick(term) {
      console.log(term);
    }
  }
};
</script>


<style scoped>
body {
  font-family: 'Arial', sans-serif;
  color: #333;
  background-color: #f4f4f4;
}

h1, h2 {
  color: #333;
}

.button-group button {
  padding: 10px 15px;
  margin-right: 10px;
  border: none;
  color: #fff;
  background-color: #007bff;
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.button-group button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
