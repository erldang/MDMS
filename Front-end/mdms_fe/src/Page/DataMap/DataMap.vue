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
          <!-- 테이블 및 용어 목록을 토글하는 버튼 -->
        <div class="list-btn">
          <button @click="ListTable">테이블</button>
          <button @click="ListTerminology">용어</button>
        </div>

        <!-- 테이블 목록을 표시하는 섹션 -->
        <div v-if="showTableList" class="table-list">
          <h3>테이블 목록</h3>
          <ul>
            <!-- 테이블 목록의 각 항목 -->
            <li
              v-for="table in tableList"
              :key="table.no"
              @click="handleTableClick(table.logicalTableName)"
            >
              {{ table.logicalTableName }}
            </li>
          </ul>
        </div>

        <!-- 용어 목록을 표시하는 섹션 -->
        <div v-if="showTerminologyList" class="terminology-list">
          <h3>용어 목록</h3>
          <ul>
            <!-- 용어 목록의 각 항목 -->
            <li
              v-for="term in terminologyList"
              :key="term"
              @click="handleTerminologyClick(term)"
            >
              {{ term }}
            </li>
          </ul>
        </div>

        <!-- 차트를 표시할 div 요소 -->
        <div
          v-show="showTableList || showTerminologyList"
          ref="chartdiv"
          style="width: 100%; height: 1000px"
        ></div>
    </div>
</template>
<script>
import axios from "axios";
import * as am5 from "@amcharts/amcharts5";
import * as am5hierarchy from "@amcharts/amcharts5/hierarchy";
import am5themes_Animated from "@amcharts/amcharts5/themes/Animated";

export default {
  name: "DataMapPage",
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
      // 기존 차트가 존재하는지 확인 및 제거
      if (this.chart) {
        console.log("기존 차트가 있습니다. 차트를 삭제합니다.");
        this.chart.dispose();
        this.chart = null;
        console.log("기존 차트 삭제 완료.");
      }
      // 새 차트 생성
      let root = am5.Root.new(this.$refs.chartdiv);
      root.setThemes([am5themes_Animated.new(root)]);
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
    // 용어 클릭 핸들러
    async handleTerminologyClick(terminology) {
      // 차트 인스턴스 제거
      if (this.chart) {
        this.chart.dispose();
        this.chart = null;
      }
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
    //용어 차트 생성 메소드
    createTerminologyChart(terminologyData) {
      // 차트 인스턴스 제거
      if (this.chart) {
        this.chart.dispose();
        this.chart = null;
      }
      // 새 차트 생성
      let root = am5.Root.new(this.$refs.chartdiv);
      root.setThemes([am5themes_Animated.new(root)]);
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
body {
  font-family: "Arial", sans-serif;
  color: #333;
  background-color: #f4f4f4;
}

h1,
h2 {
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

/* CSS 개선 */
.button-group button:hover {
  background-color: #0056b3;
  /* 어두운 파란색으로 변경 */
}

.list-btn button {
  padding: 8px 12px;
  margin: 5px;
  background-color: #28a745;
  /* 녹색 계열 */
  color: white;
}

.list-btn button:hover {
  background-color: #218838;
  /* 어두운 녹색으로 변경 */
}

.table-list,
.terminology-list {
  margin-top: 20px;
  padding: 10px;
  background-color: #f8f9fa;
  /* 연한 회색 */
  border: 1px solid #ddd;
  border-radius: 5px;
}

@media (max-width: 768px) {
  .button-group button,
  .list-btn button {
    width: 100%;
    /* 모바일 화면에서 버튼을 전체 너비로 확장 */
    margin-bottom: 10px;
  }
}
</style>
