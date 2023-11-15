<!-- 테이블 목록 클릭 시, 테이블 데이터 맵 2단계 
용어 목록 클릭 시, 용어 데이터 맵 1단계  -->
<template>
    <div>
        <!-- 페이지 제목 -->
        <h1>관리자 페이지</h1>
        <h2>데이터 맵</h2>

        <!-- 탐색 버튼 그룹 -->
        <div class="button-group">
            <button @click="navigateToMain">표준 용어</button>
            <button :disabled="$route.path === '/admin-datamap'">데이터 맵</button>
            <button @click="navigateToUserRecord">사용자 이력 조회</button>
        </div>

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
                <li v-for="table in tableList" :key="table.no" @click="handleTableClick(table.logicalTableName)">
                    {{ table.logicalTableName }}
                </li>
            </ul>
        </div>

        <!-- 용어 목록을 표시하는 섹션 -->
        <div v-if="showTerminologyList" class="terminology-list">
            <h3>용어 목록</h3>
            <ul>
                <!-- 용어 목록의 각 항목 -->
                <li v-for="term in terminologyList" :key="term" @click="handleTerminologyClick(term)">
                    {{ term }}
                </li>
            </ul>
        </div>

        <!-- 차트를 표시할 div 요소 -->
        <div ref="chartdiv" style="width: 100%; height: 500px;"></div>
    </div>
</template>
    
    
    
<script>
import axios from 'axios';
import * as am5 from "@amcharts/amcharts5";
import * as am5hierarchy from "@amcharts/amcharts5/hierarchy";
import am5themes_Animated from "@amcharts/amcharts5/themes/Animated";

export default {
    name: 'AdminDataMapPage',
    data() {
        return {
            // 컴포넌트의 상태 데이터
            tableData: [],
            tableList: [],
            terminologyList: [],
            showTableList: false,
            showTerminologyList: false,
            chart: null // 차트 인스턴스 저장
        };
    },
    created() {
        // 컴포넌트 생성 시 데이터 로드
        this.fetchTableData();
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
                this.displayErrorMessage("테이블 데이터를 가져오는 데 실패했습니다.");
            }
        },
        // 메인 페이지로 이동하는 메소드
        navigateToMain() {
            this.$router.push('/admin-main');
        },
        // 사용자 이력 페이지로 이동하는 메소드
        navigateToUserRecord() {
            this.$router.push('/admin-user-record');
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
            const selectedTable = this.tableData.find(table => table.logicalTableName === logicalTableName);
            if (selectedTable) {
                this.createChart(selectedTable);
            }
        },
        // 서버에서 용어별 사용된 테이블 목록을 가져오는 비동기 함수
        async fetchTerminologyUsage(terminology) {
            try {
                const token = localStorage.getItem('token');
                const response = await axios.get(`http://localhost:3001/tableInfo/join/terminology?standardTerminology=${terminology}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                return response.data.ok === "ok" ? response.data.data[0].tableList : [];
            } catch (error) {
                console.error('Error fetching terminology usage:', error);
                return [];
            }
        },

        // 차트 생성 메소드
        async createChart(tableData) {
            if (this.chart) {
                this.chart.dispose(); // 기존 차트 제거
            }

            let root = am5.Root.new(this.$refs.chartdiv);
            root.setThemes([am5themes_Animated.new(root)]);

            let container = root.container.children.push(am5.Container.new(root, {
                width: am5.percent(80),
                height: am5.percent(80),
                layout: root.verticalLayout
            }));

            const children = await Promise.all(tableData.stdTerminologyList.map(async (term) => {
                const usageData = await this.fetchTerminologyUsage(term);
                const filteredUsageData = usageData.filter(u => u.logicalTableName !== tableData.logicalTableName); // 최상위 테이블 제외
                return {
                    name: term,
                    children: filteredUsageData.map(table => ({
                        name: table.logicalTableName,
                        value: parseInt(table.num, 10)
                    }))
                };
            }));

            const data = {
                name: tableData.logicalTableName,
                children: children
            };

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
            this.chart = series;
        },
        // 용어 선택 시 호출될 메소드
        async handleTerminologyClick(terminology) {
            try {
                const token = localStorage.getItem('token');
                const response = await axios.get(`http://localhost:3001/tableInfo/join/terminology?standardTerminology=${terminology}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (response.data && response.data.ok === "ok") {
                    this.createTerminologyChart(response.data.data[0]);
                    //여기 데이터 형식을 보고 확인해야겠다
                } else {
                    // 에러 처리
                    console.error('Data fetch failed');
                    this.displayErrorMessage("용어 데이터를 가져오는 데 실패했습니다.");
                }
            } catch (error) {
                console.error('Error fetching terminology data:', error);
                this.displayErrorMessage("용어 데이터를 가져오는 데 실패했습니다.");
            }
        },
        // 용어 데이터를 기반으로 차트 생성
        createTerminologyChart(terminologyData) {
            if (this.chart) {
                this.chart.dispose(); // 기존 차트 제거
            }

            let root = am5.Root.new(this.$refs.chartdiv);
            root.setThemes([am5themes_Animated.new(root)]);

            let container = root.container.children.push(am5.Container.new(root, {
                width: am5.percent(80),
                height: am5.percent(80),
                layout: root.verticalLayout
            }));

            // 차트에 사용할 데이터 구조 생성
            const data = {
                name: terminologyData.standardTerminology, // 최상위 노드: 용어
                children: terminologyData.tableList.map(table => ({
                    name: table.logicalTableName, // 자식 노드: 용어가 사용된 테이블들
                    value: table.num //테이블에 튜플 수 
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
            this.chart = series; // 차트 인스턴스 저장
        },
        beforeUnmount() {
            if (this.chart) {
                this.chart.dispose(); // 컴포넌트 파괴 시 차트 정리
            }
        },
        displayErrorMessage(message) {
            alert(message);
        },
    }
};
</script>
    
<style scoped>
body {
    font-family: 'Arial', sans-serif;
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
    