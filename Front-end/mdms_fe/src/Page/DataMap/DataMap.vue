<template>
    <!-- 컴포넌트의 HTML 마크업 -->
    <div>
      <!-- 페이지 이동 버튼 그룹 -->
      <div class="page_btn">
        <!-- 데이터 목록 페이지로 이동하는 버튼 -->
        <button :class="{ active: isCurrentPage('/') }" @click="navigateTo('/')">데이터 목록</button>
        <!-- 데이터 맵 페이지로 이동하는 버튼 -->
        <button :class="{ active: isCurrentPage('/user-data-map') }" @click="navigateTo('/user-data-map')">데이터 맵</button>
      </div>
      <!-- 여기에 필요한 거 추가 하면 됩니다 제목도 지워도 됩니다 -->
      <!-- 페이지 제목 -->
      <h2>데이터 맵 페이지</h2>
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
      <div id="chartdiv" ref="chartdiv"></div>
    </div>
</template>

<script>
  // 맵그리기
  import * as am5 from "@amcharts/amcharts5";
  import * as am5hierarchy from "@amcharts/amcharts5/hierarchy";
  import am5themes_Animated from "@amcharts/amcharts5/themes/Animated";
  import axios from 'axios';

  export default {
    name: 'DataMap', // 컴포넌트 이름
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
      this.fetchTableData();
    },
    mounted() {
    },
    methods: {
      // 경로(route)를 인자로 받아 해당 경로로 라우팅하는 메소드
      navigateTo(route) {
        this.$router.push(route);
      },
      // 현재 페이지 경로가 주어진 경로(route)와 일치하는지 확인하는 메소드
      isCurrentPage(route) {
        return this.$route.path === route;
      },
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
      // 테이블 항목 클릭 시 호출되는 메소드
      handleTableClick(logicalTableName) {
        const selectedTable = this.tableData.find(table => table.logicalTableName === logicalTableName);
        if (selectedTable) {
          this.createChart(selectedTable);
        }
      },
      // 차트를 생성하는 메소드
      createChart(tableData) {
        if (this.chart) {
          this.chart.dispose(); // 기존 차트가 있다면 제거합니다.
        }

        let root = am5.Root.new(this.$refs.chartdiv);

        root.setThemes([am5themes_Animated.new(root)]);

        let container = root.container.children.push(am5.Container.new(root, {
          width: am5.percent(80),
          height: am5.percent(80),
          layout: root.verticalLayout
        }));

        // 차트에 사용할 데이터 구조를 생성합니다.
        const data = {
          name: tableData.logicalTableName,
          children: tableData.stdTerminologyList.map(term => ({
            name: term,
            value: tableData.num
          }))
        };

        // 차트 생성 및 설정
        let series = container.children.push(am5hierarchy.ForceDirected.new(root, {
          singleBranchOnly: false,
          downDepth: 2,
          topDepth: 0,
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

        series.data.setAll([data]);

        this.chart = series; // 차트 인스턴스를 저장합니다.
      },
      ListTable() {
        this.showTableList = true;
        this.showTerminologyList = false;
      },
      ListTerminology() {
        this.showTerminologyList = true;
        this.showTableList = false;
      },
    },
    beforeUnmount() {
      if (this.chart) {
        this.chart.dispose(); // 컴포넌트가 파괴될 때 차트를 정리합니다.
      }
    }
  }
</script>

<style scoped>
/* 페이지 내 버튼에 대한 스타일 */
.page_btn button {
  background-color: #4CAF50; /* 버튼 배경 색상 변경 */
  color: white; /* 텍스트 색상 */
  padding: 10px 20px; /* 여백 */
  margin: 5px; /* 마진 */
  border: none; /* 테두리 없음 */
  border-radius: 4px; /* 둥근 모서리 */
  cursor: pointer; /* 마우스 오버시 커서 변경 */
  font-size: 1.1rem; /* 폰트 크기 */
}
/* 버튼에 마우스 호버 시의 스타일 */
.page_btn button:hover {
  background-color: #ddd; /* 호버 시 배경색 */
}

.page_btn button.active {
  background-color: #3e8e41; /* 활성화된 버튼 색상 변경 */
}
/* 현재 활성화된 페이지 버튼의 스타일 */
.page_btn .active {
  background-color: #bbb; /* 활성화된 배경색 */
  color: white; /* 글자 색상 */
  cursor: default; /* 마우스 커서 스타일 */
}
/* #chartdiv {
    width: 100%;
    max-width: 100%;
    height: 50rem;
} */
</style>
