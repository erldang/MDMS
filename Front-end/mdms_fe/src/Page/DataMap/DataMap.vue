<template>
  <div>
    <div class="page-button-group">
      <!-- 데이터 목록 페이지로 이동하는 버튼 -->
      <button class="page-nav-button" :class="{ active: isCurrentPage('/') }" @click="navigateTo('/')">
        데이터 목록
      </button>
      <!-- 데이터 맵 페이지로 이동하는 버튼 -->
      <button class="page-nav-button" :class="{ active: isCurrentPage('/user-data-map') }"
        @click="navigateTo('/user-data-map')">
        데이터 맵
      </button>
    </div>
    <!-- 페이지 제목 -->
    <h2 class="page-title">데이터 맵</h2>

    <!-- 테이블 및 용어 목록을 토글하는 버튼 -->
    <div class="toggle-button-group">
      <button class="toggle-button" @click="ListTable">테이블</button>
      <button class="toggle-button" @click="ListTerminology">용어</button>
    </div>

    <!-- 테이블 목록을 표시하는 섹션 -->
    <div v-if="showTableList" class="table-list-section">
      <h3 class="section-title">테이블 목록</h3>
      <ul class="table-list">
        <!-- 테이블 목록의 각 항목 -->
        <li class="table-list-item" v-for="table in tableList" :key="table.no"
          @click="handleTableClick(table.logicalTableName)">
          {{ table.logicalTableName }}
        </li>
      </ul>
    </div>

    <!-- 용어 목록을 표시하는 섹션 -->
    <div v-if="showTerminologyList" class="terminology-list-section">
      <h3 class="section-title">용어 목록</h3>
      <ul class="terminology-list">
        <!-- 용어 목록의 각 항목 -->
        <li class="terminology-list-item" v-for="term in terminologyList" :key="term"
          @click="handleTerminologyClick(term)">
          {{ term }}
        </li>
      </ul>
    </div>

    <!-- 차트를 표시할 div 요소 -->
    <div v-show="showTableList || showTerminologyList" class="chart-container" ref="chartdiv"
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
    // 현재 페이지인지 확인하는 함수
    isCurrentPage(path) {
      return this.$route.path === path;
    },
    // 특정 페이지로 이동하는 함수
    navigateTo(path) {
      this.$router.push(path);
    },
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
.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 40px;
  background-color: #35495E;
  /* 모던한 다크 블루 */
  color: #FFFFFF;
}

.left-section {
  display: flex;
  align-items: center;
}

.logo-img {
  width: 60px;
  /* 로고 크기 조정 */
  height: auto;
  margin-right: 20px;
}

.title-section {
  display: flex;
  flex-direction: column;
}

.title {
  font-size: 24px;
  /* 적당한 크기의 제목 폰트 */
  font-weight: bold;
  /* 볼드체로 강조 */
  margin: 0;
}

.subtitle {
  font-size: 16px;
  /* 서브타이틀 폰트 크기 */
  opacity: 0.9;
  /* 약간 투명도 조정 */
}

.navbar-section {
  display: flex;
  align-items: center;
}

.username-text {
  margin-right: 20px;
  font-size: 18px;
  /* 사용자 이름 폰트 크기 */
  font-weight: medium;
  white-space: nowrap;
}

.nav-btn {
  padding: 10px 20px;
  margin-left: 10px;
  background-color: transparent;
  border: 2px solid #FFFFFF;
  /* 흰색 테두리 */
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 16px;
  color: #FFFFFF;
}

.nav-btn:hover {
  background-color: #4A6FA5;
  /* 버튼 호버 색상 */
  color: #FFFFFF;
}

.logout-btn {
  border-color: #FF5252;
  /* 로그아웃 버튼 테두리 색상 */
  color: #FF5252;
}

.logout-btn:hover {
  background-color: #FF5252;
  /* 로그아웃 버튼 호버 색상 */
  color: #FFFFFF;
}</style>
