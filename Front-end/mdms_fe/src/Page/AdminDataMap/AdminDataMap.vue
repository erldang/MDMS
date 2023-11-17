<template>
  <div>
    <!-- 페이지 제목 -->
    <h1 class="page-title">관리자 페이지</h1>
    <h2 class="sub-title">데이터 맵</h2>

    <!-- 탐색 버튼 그룹 -->
    <div class="navigation-buttons">
      <button class="nav-btn" @click="navigateToMain">표준 용어</button>
      <button class="nav-btn" :disabled="$route.path === '/admin-datamap'">데이터 맵</button>
      <button class="nav-btn" @click="navigateToUserRecord">사용자 이력 조회</button>
    </div>

    <!-- 테이블 및 용어 목록을 토글하는 버튼 -->
    <div class="toggle-buttons">
      <button class="toggle-btn" @click="ListTable">테이블</button>
      <button class="toggle-btn" @click="ListTerminology">용어</button>
    </div>

    <!-- 테이블 목록을 표시하는 섹션 -->
    <div v-if="showTableList" class="table-list-section">
      <h3 class="section-title">테이블 목록</h3>
      <ul class="list">
        <!-- 테이블 목록의 각 항목 -->
        <li class="list-item" v-for="table in tableList" :key="table.no"
          @click="handleTableClick(table.logicalTableName)">
          {{ table.logicalTableName }}
        </li>
      </ul>
    </div>

    <!-- 용어 목록을 표시하는 섹션 -->
    <div v-if="showTerminologyList" class="terminology-list-section">
      <h3 class="section-title">용어 목록</h3>
      <ul class="list">
        <!-- 용어 목록의 각 항목 -->
        <li class="list-item" v-for="term in terminologyList" :key="term" @click="handleTerminologyClick(term)">
          {{ term }}
        </li>
      </ul>
    </div>

    <!-- 차트를 표시할 div 요소 -->
    <div v-show="showTableList || showTerminologyList" class="chart-section" ref="chartdiv"
      style="width: 100%; height: 1000px"></div>
  </div>
</template>

  
  
  
<script>
import axios from "axios";
import * as am5 from "@amcharts/amcharts5";
import * as am5hierarchy from "@amcharts/amcharts5/hierarchy";
import am5themes_Animated from "@amcharts/amcharts5/themes/Animated";

export default {
  name: "AdminDataMapPage",
  data() {
    return {
      // 컴포넌트의 상태 데이터
      tableData: [],
      tableList: [],
      terminologyList: [],
      showTableList: false,
      showTerminologyList: false,
      chart: null, // 차트 인스턴스 저장
    };
  },
  created() {
    // 컴포넌트 생성 시 데이터 로드
    this.fetchTableData();
  },
  methods: {
    displayErrorMessage(message) {
      console.error(message);
      this.showToast(message);
    },
    // 서버에서 테이블 데이터를 가져오는 메소드
    async fetchTableData() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:3001/tableInfo", {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (response.data && response.data.data) {
          this.tableData = response.data.data;
          this.tableList = this.tableData.map((item) => ({
            no: item.no,
            logicalTableName: item.logicalTableName,
          }));
          this.terminologyList = [
            ...new Set(
              this.tableData.flatMap((item) => item.stdTerminologyList)
            ),
          ];
        } else {
          throw new Error("데이터 로드 실패");
        }
      } catch (error) {
        console.error("Error fetching table data:", error);
        this.displayErrorMessage("테이블 데이터를 가져오는 데 실패했습니다.");
      }
    },
    // 메인 페이지로 이동하는 메소드
    navigateToMain() {
      this.$router.push("/admin-main");
    },
    // 사용자 이력 페이지로 이동하는 메소드
    navigateToUserRecord() {
      this.$router.push("/admin-user-record");
    },
    // 테이블 목록을 표시하는 메소드
    ListTable() {
      this.showTableList = true;
      this.showTerminologyList = false;
    },
    // 용어 목록을 표시하는 메소드
    ListTerminology() {
      this.showTerminologyList = true;
      this.showTableList = false;
    },
    // 테이블 항목 클릭 시 호출되는 메소드
    handleTableClick(logicalTableName) {
      const selectedTable = this.tableData.find(
        (table) => table.logicalTableName === logicalTableName
      );
      if (selectedTable) {
        this.createChart(selectedTable);
      }
    },

    async fetchTerminologyUsage(terminology) {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          `http://localhost:3001/tableInfo/join/terminology?standardTerminology=${terminology}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        if (
          response.data.ok === "ok" &&
          response.data.data &&
          response.data.data[0].tableList.length > 0
        ) {
          return response.data.data[0].tableList;
        } else {
          throw new Error("용어 데이터가 비어있습니다.");
        }
      } catch (error) {
        console.error("Error fetching terminology usage:", error);
        this.displayErrorMessage("용어 데이터를 가져오는 데 실패했습니다.");
        return [];
      }
    },

    // 차트 생성 메소드
    async createChart(tableData) {
      if (this.chart) {
        am5.array.each(am5.registry.rootElements, (re, index) => {
          am5.registry.rootElements[index].dispose();
        });
      }
      // DOM 참조 유효성 확인
      if (this.$refs.chartdiv) {
        let root = am5.Root.new(this.$refs.chartdiv);
        root.setThemes([am5themes_Animated.new(root)]);

        let container = root.container.children.push(
          am5.Container.new(root, {
            width: am5.percent(80),
            height: am5.percent(80),
            layout: root.verticalLayout,
          })
        );

        const children = await Promise.all(
          tableData.stdTerminologyList.map(async (term) => {
            const usageData = await this.fetchTerminologyUsage(term);
            const filteredUsageData = usageData.filter(
              (u) => u.logicalTableName !== tableData.logicalTableName
            ); // 최상위 테이블 제외
            return {
              name: term,
              children: filteredUsageData.map((table) => ({
                name: table.tableName,
                value: parseInt(table.num, 10),
              })),
            };
          })
        );

        const data = {
          name: tableData.logicalTableName,
          children: children,
        };

        let series = container.children.push(
          am5hierarchy.ForceDirected.new(root, {
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
            maxRadius: am5.percent(6),
          })
        );

        series.data.setAll([data]);
        this.chart = series;
      } else {
        console.log("DOM 참조 유효성 확인 시 false error");
      }
    },
    async handleTerminologyClick(terminology) {
      const tableList = await this.fetchTerminologyUsage(terminology);
      if (tableList && tableList.length > 0) {
        this.createTerminologyChart({
          standardTerminology: terminology,
          tableList,
        });
      } else {
        console.error("Data fetch failed or empty data");
        this.displayErrorMessage("용어 데이터를 가져오는 데 실패했습니다.");
      }
    },
    createTerminologyChart(terminologyData) {
      if (this.chart) {
        am5.array.each(am5.registry.rootElements, (re, index) => {
          am5.registry.rootElements[index].dispose();
        });
      }

      if (this.$refs.chartdiv) {
        let root = am5.Root.new(this.$refs.chartdiv);
        root.setThemes([am5themes_Animated.new(root)]);
        // ... 차트 생성 로직
        let container = root.container.children.push(
          am5.Container.new(root, {
            width: am5.percent(80),
            height: am5.percent(80),
            layout: root.verticalLayout,
          })
        );

        // 첫 번째 레벨 자식 노드의 데이터 구성
        const children = terminologyData.tableList.map((table) => {
          // 각 테이블에서 사용된 용어를 두 번째 레벨 자식 노드로 구성
          const secondLevelChildren =
            this.tableData
              .find((t) => t.logicalTableName === table.tableName)
              ?.stdTerminologyList.filter(
                (term) => term !== terminologyData.standardTerminology
              ) // terminologyData.standardTerminology와 같은 용어 제외
              .map((term) => ({
                name: term,
                value: parseInt(table.num, 10), // 또는 다른 유의미한 값
              })) || [];

          return {
            name: table.tableName,
            children: secondLevelChildren,
            value: parseInt(table.num, 10),
          };
        });

        const data = {
          name: terminologyData.standardTerminology, // 최상위 노드: 선택된 용어
          children: children,
          value: parseInt(terminologyData.num, 10),
        };

        // 차트 생성 및 설정
        let series = container.children.push(
          am5hierarchy.ForceDirected.new(root, {
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
            maxRadius: am5.percent(6),
          })
        );

        series.data.setAll([data]);
        this.chart = series;
      } else {
        console.error("DOM 요소 참조 실패");
      }
    },
    beforeUnmount() {
      if (this.chart) {
        this.chart.dispose(); // 컴포넌트 파괴 시 차트 정리
      }
    },
  },
};
</script>
  
<style scoped>
.page-title, .sub-title {
  color: #35495E; /* 모던한 다크 블루 색상 */
  margin-bottom: 10px;
}

.navigation-buttons, .toggle-buttons {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.nav-btn, .toggle-btn {
  padding: 10px 20px;
  border: none;
  color: white;
  background-color: #007bff; /* 밝은 파란색 */
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s, box-shadow 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 버튼에 그림자 효과 추가 */
}

.nav-btn:hover, .toggle-btn:hover {
  background-color: #0056b3; /* 어두운 파란색 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 호버 시 그림자 효과 강화 */
}

.table-list-section, .terminology-list-section {
  margin-top: 20px;
  padding: 15px;
  background-color: white; /* 백색 배경 */
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 섹션에 그림자 효과 추가 */
}

.section-title {
  color: #34495e; /* 어두운 회색 계열 */
  margin-bottom: 10px;
}

.list {
  list-style: none;
  padding: 0;
}

.list-item {
  padding: 10px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: background-color 0.3s;
}

.list-item:hover {
  background-color: #f2f2f2; /* 약간 더 진한 회색 */
}

.chart-section {
  width: 100%;
  height: 1000px;
  margin-top: 20px;
}

@media (max-width: 768px) {
  .nav-btn, .toggle-btn {
    width: 100%; /* 모바일 화면에서 버튼을 전체 너비로 확장 */
    margin-bottom: 10px;
  }
}
</style>
