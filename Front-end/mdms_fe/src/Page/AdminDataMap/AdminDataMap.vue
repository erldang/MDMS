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
        <li v-for="term in terminologyList" :key="term">{{ term }}</li>
      </ul>
    </div>

    <div ref="chartdiv" style="width: 100%; height: 500px;"></div>
  </div>
</template>


<script>
import axios from 'axios';
// amCharts 관련 라이브러리 임포트
import * as am5 from "@amcharts/amcharts5";
import * as am5hierarchy from "@amcharts/amcharts5/hierarchy";
import am5themes_Animated from "@amcharts/amcharts5/themes/Animated";

export default {
  name: 'AdminDataMapPage',
  data() {
    return {
      tableData: [],
      tableList: [],
      terminologyList: [],
      showTableList: false,
      showTerminologyList: false,
      chart: null // 차트 인스턴스를 저장할 변수
    };
  },
  created() {
    this.fetchTableData(); // 컴포넌트가 생성될 때 데이터를 가져옵니다.
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
    // 테이블 항목 클릭 시 호출되는 메소드
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
