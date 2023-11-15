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
      <!-- 페이지 제목 -->
      <h2>데이터 맵 페이지</h2>
      <!-- 여기에 필요한 거 추가 하면 됩니다 제목도 지워도 됩니다 -->
      <!-- <div id="chartdiv" ref="chartdiv"></div> // 맵 그리기-->
      <div class="catalog">
        <table>
          <tr>
            <th>테이블 논리명</th>
          </tr>
          <tr v-for="(item, index) in rawData" :key="index" @click="navigateToDetail(item)">
            <td>{{ index.logicalTableName   }}</td>
          </tr>
        </table>
      </div>
    </div>
</template>

<script>
  // 맵그리기
  // import * as am5 from "@amcharts/amcharts5";
  // import * as am5hierarchy from "@amcharts/amcharts5/hierarchy";
  // import am5themes_Animated from "@amcharts/amcharts5/themes/Animated";
  import axios from 'axios';

  export default {
    name: 'DataMap', // 컴포넌트 이름
    data() {
      return {
        rawData: [], // 서버로부터 받은 원본 데이터
      };
    },
    computed: {

    },
    mounted() {
      // // Create root element
      // // https://www.amcharts.com/docs/v5/getting-started/#Root_element
      // var root = am5.Root.new(this.$refs.chartdiv);

      // // Set themes
      // // https://www.amcharts.com/docs/v5/concepts/themes/
      // root.setThemes([am5themes_Animated.new(root)]);

      // // Create wrapper container
      // let container = root.container.children.push(am5.Container.new(root, {
      //   width: am5.percent(80),
      //   height: am5.percent(80),
      //   layout: root.verticalLayout
      // }));

      // var data = {
      //   children: [{
      //     name: "Fruity",
      //     children: [{
      //       name: "테스트용 베리종류",
      //       children: [
      //         {
      //           name: "테스트용 베리1",
      //           value: 2,
      //         },
      //         {
      //           name: "테스트용 베리2",
      //           value: 3,
      //         }
      //       ]
      //       },
      //       {
      //         name: "기타 과일",
      //         children: [
      //         {
      //           name: "코코넛",
      //           value: 1
      //         },
      //         {
      //           name: "체리",
      //           value: 1
      //         }
      //       ]
      //     }]
      //   }]
      // };

      // // Create series
      // // https://www.amcharts.com/docs/v5/charts/hierarchy/#Adding
      // let series = container.children.push(am5hierarchy.ForceDirected.new(root, {
      //   singleBranchOnly: false,
      //   downDepth: 2, //클릭시 한번에 출력되는 깊이
      //   topDepth: 1, // 출력시 상위 몇번째의 깊이에 있는 종류를 기점으로 출력시켜 줄것인가
      //   initialDepth: 2, // 첫 출력시 한번에 보여줄 깊이(몇단계 아래까지 보여줄 것인가)
      //   valueField: "value", // value로 하면 수치에 따라 표현, 이름으로 하면 해당 이름이 출력 숫자면 크기가 조절됨
      //   // valueField: 3, 크기 고정하고 수치만 그리는거 모르겠음
      //   categoryField: "name",
      //   childDataField: "children",
      //   idField: "value",
      //   linkWithField: "linkWith",
      //   manyBodyStrength: -15, // 서로간의 거리(-일수록 더 멀리 멀어짐)
      //   centerStrength: 0.3, // 멀어짐 정도 인거같은데 1보다 커지면 동그라미로 뭉침
      //   // 구체 크기조절
      //   minRadius: am5.percent(6),
      //   maxRadius: am5.percent(6),
      //   // 이미지 변경
      //   // series.circles.template.set("forceHidden", true);
      //   // series.outerCircles.template.set("forceHidden", true);
      //   // src: asdfg
      // }));

      // // // Disable circles
      // // series.circles.template.set("forceHidden", true);
      // // series.outerCircles.template.set("forceHidden", true);

      // // ... except for central node
      // series.circles.template.adapters.add("forceHidden", function(forceHidden, target) {
      // return target.dataItem.get("depth") == 0 ? false: forceHidden;
      // });
      // // Set up labels
      // series.labels.template.setAll({
      // fill: am5.color(0x000000),
      // y: 45,
      // oversizedBehavior: "none"
      // });

      // // Use adapter to leave central node label centered
      // series.labels.template.adapters.add("forceHidden", function(forceHidden, target) {
      // return target.dataItem.get("depth") == 0 ? false : forceHidden;
      // });

      // // Use template.setup function to prep up node with an image
      // series.nodes.template.setup = function(target) {
      //   target.events.on("dataitemchanged", function(ev) {
      //       target.children.push(am5.Picture.new(root, {
      //       width: 0,
      //       height: 0,
      //       centerX: am5.percent(50),
      //       centerY: am5.percent(50),
      //       src: ev.target.dataItem.dataContext.image
      //     }));
      //   });
      // }
      // series.get("colors").setAll({
      //   step: 2
      // });

      // series.links.template.set("strength", 1); // 모양 결정하는거 같음 0~4 까지 출력됨 0은 서로 꼬여있음 1이나 3은 같아보임
      // series.data.setAll([data]);
      // series.set("selectedDataItem", series.dataItems[0]);

      // // Make stuff animate on load
      // series.appear(1000, 300); // 출력속도, 처음 출현속도 같음
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
      async getData() {
        try {
          const token = localStorage.getItem('token'); // 로컬 스토리지에서 토큰 가져오기
          const response = await axios.get('http://localhost:3001/tableinfo', { //테이블 목록 가져오기 
            headers: { 'Authorization': `Bearer ${token}` } // 토큰을 헤더에 포함시켜 요청
          }).then(response => {
          this.rawData = response.data.data;
          });
          console.log(response.data.data);
          //html 요소에 목록을 채워넣기 
        } catch (error) {
          alert('데이터를 불러오는 중 에러가 발생했습니다.'); // 에러 처리
        }
      },
      navigateToDetail(item) {
        // DataDetail 컴포넌트로 보내기 전에 데이터를 콘솔에 출력합니다.
        console.log('Navigating to DataDetail with item:', item);
        this.$router.push({ name: 'DataDetail', query: { itemData: JSON.stringify(item) } });
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
