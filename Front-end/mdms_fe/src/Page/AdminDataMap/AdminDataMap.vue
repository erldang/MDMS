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
// 하위 모듈 및 테마 임포트 생략

export default {
  name: 'AdminDataMapPage',
  data() {
    return {
      // 테이블 데이터, 용어 목록 및 차트 관련 상태 관리
      tableData: [],
      tableList: [],
      terminologyList: [],
      showTableList: false,
      showTerminologyList: false,
      chart: null // 차트 인스턴스를 저장
    };
  },
  created() {
    this.fetchTableData(); // 컴포넌트 생성 시 데이터 로딩
  },
  methods: {
    // 서버에서 테이블 데이터를 가져오는 메소드
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
      const selectedTable = this.tableData.find(table => table.logicalTableName === logicalTableName);
      if (selectedTable) {
        this.createChart(selectedTable);
      }
    },
    handleTermClick(term) {
      this.createChartForTerm(term);
    },
    createChartForTerm(selectedTerm) {
      const tablesContainingTerm = this.tableData.filter(table =>
        table.stdTerminologyList.includes(selectedTerm)
      );

      const children = tablesContainingTerm.map(table => ({
        name: table.logicalTableName,
        value: table.num
      }));

      const chartData = {
        name: selectedTerm,
        children: children
      };

      this.createChart(chartData);
    },
    createChart(chartData) {
      // 기존에 생성된 차트가 있다면 제거합니다.
      if (this.chart) {
        this.chart.dispose();
        this.chart = null;
      }

      // 새로운 차트의 Root를 생성합니다. 여기서 this.$refs.chartdiv는 차트가 들어갈 DOM 요소를 참조합니다.
      let root = am5.Root.new(this.$refs.chartdiv);

      // amCharts의 기본 컨테이너 설정. 차트의 크기와 레이아웃을 정의합니다.
      let container = root.container.children.push(am5.Container.new(root, {
        width: am5.percent(80),
        height: am5.percent(80),
        layout: root.verticalLayout
      }));

      // ForceDirected 시리즈를 생성하고 설정합니다. 이 시리즈는 노드 간의 연결성과 관계를 표현하는 데 사용됩니다.
      let series = container.children.push(am5hierarchy.ForceDirected.new(root, {
        singleBranchOnly: false,
        downDepth: 2,
        topDepth: 1,
        initialDepth: 2,
        valueField: "value",
        categoryField: "name",
        childDataField: "children",
        idField: "value",
        manyBodyStrength: -15,
        centerStrength: 0.3,
        minRadius: am5.percent(6),
        maxRadius: am5.percent(6)
      }));

      // 차트에 데이터를 설정합니다. 여기서 chartData는 차트를 그릴 때 사용할 데이터 구조입니다.
      series.data.setAll([chartData]);

      // 생성된 차트를 인스턴스 변수에 저장합니다. 이를 통해 나중에 차트를 제거하거나 다시 렌더링할 수 있습니다.
      this.chart = series;
    },

    beforeDestroy() {
      if (this.chart) {
        this.chart.dispose(); // 컴포넌트 파괴 시 차트 정리
      }
    },
  },
  beforeUnmount() {
    if (this.chart) {
      this.chart.dispose(); // 컴포넌트가 파괴될 때 차트를 정리합니다.
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
