ERROR
Cannot read properties of undefined (reading 'push')
TypeError: Cannot read properties of undefined (reading 'push')
    at Proxy.navigateToDataMap (webpack-internal:///./node_modules/babel-loader/lib/index.js??clonedRuleSet-40.use[0]!./node_modules/vue-loader/dist/index.js??ruleSet[0].use[0]!./src/Page/AdminMain/AdminMain.vue?vue&type=script&lang=js:66:58)
    at onClick._cache.<computed>._cache.<computed> (webpack-internal:///./node_modules/babel-loader/lib/index.js??clonedRuleSet-40.use[0]!./node_modules/vue-loader/dist/templateLoader.js??ruleSet[1].rules[3]!./node_modules/vue-loader/dist/index.js??ruleSet[0].use[0]!./src/Page/AdminMain/AdminMain.vue?vue&type=template&id=771938ba:42:92)
    at callWithErrorHandling (webpack-internal:///./node_modules/@vue/runtime-core/dist/runtime-core.esm-bundler.js:288:18)
    at callWithAsyncErrorHandling (webpack-internal:///./node_modules/@vue/runtime-core/dist/runtime-core.esm-bundler.js:296:17)
    at HTMLButtonElement.invoker (webpack-internal:///./node_modules/@vue/runtime-dom/dist/runtime-dom.esm-bundler.js:759:82)
검색 후 전체 데이터에서 검색을 하는 게 아니라 검색 필터링 되어진 데이터에 관련하여 검색하게 된다 이를 해결
    <template>
    <div>
      <h1>커스텀 표준 용어 추가</h1>
      <form @submit.prevent="submitForm">
        <div>
          <label for="standardTerminology">표준용어명:</label>
          <input type="text" id="standardTerminology" v-model="form.standardTerminology" @keypress.enter.prevent>
        </div>
        <div>
          <label for="description">설명:</label>
          <input type="text" id="description" v-model="form.description" @keypress.enter.prevent>
        </div>
        <div>
          <label for="englishAbbreviation">영어 약어 명:</label>
          <input type="text" id="englishAbbreviation" v-model="form.englishAbbreviation" @keypress.enter.prevent>
        </div>
        <div>
          <label for="relevantOrganization">기관명:</label>
          <input type="text" id="relevantOrganization" v-model="form.relevantOrganization" @keypress.enter.prevent>
        </div>
        <div>
          <label for="domainSearch">도메인 검색:</label>
          <input type="text" id="domainSearch" v-model="domainSearchQuery" @input="searchDomain" @keypress.enter.prevent>
          <!-- 선택된 도메인 표시 -->
          <p v-if="form.domain">선택된 도메인: {{ form.domain.classificationName }}</p>
          <!-- 도메인 검색 결과 표시 -->
          <ul>
            <li v-for="domain in domainSearchResults" :key="domain.no" @click="selectDomain(domain)">
              {{ domain.classificationName }} - {{ domain.description }} - {{ domain.dataType }} - {{ domain.dataLength }}
            </li>
          </ul>
        </div>
        <button type="submit" :disabled="!isValid">등록</button>
      </form>
    </div>
  </template>
  
  <script>
import axios from 'axios';

export default {
  data() {
    return {
      form: {
        standardTerminology: '',
        description: '',
        englishAbbreviation: '',
        relevantOrganization: '',
        domain: null,
      },
      domainSearchQuery: '',
      allDomains: [],
      domainSearchResults: [],
    };
  },
  computed: {
    isValid() {
        // 모든 필드가 채워져 있는지 확인
      return this.form.standardTerminology && this.form.description && this.form.englishAbbreviation && this.form.relevantOrganization && this.form.domain;
    }
  },
    created() {
      this.fetchDomains();
    },
    watch: {
      // domainSearchQuery의 변화를 감시합니다
      domainSearchQuery(newQuery) {
        this.filterDomains(newQuery);
      }
    },
    methods: {
      fetchDomains() {
        axios.get(`http://localhost:3001/domain`, {
          headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
        })
        .then(response => {
          this.allDomains = response.data.data;
        })
        .catch(error => {
          console.error(error);
        });
      },
      filterDomains(query) {
        // 입력된 검색어를 기반으로 도메인 데이터를 필터링
        this.domainSearchResults = this.allDomains.filter(domain => 
          domain.classificationName.toLowerCase().includes(query.toLowerCase())
        );
      },
      selectDomain(domain) {
        this.form.domain = domain;
      },
      submitForm() {
        // domain 객체 내부의 모든 값을 문자열로 변환
        const stringifiedDomain = {};
        for (const key in this.form.domain) {
            stringifiedDomain[key] = String(this.form.domain[key]);
        }
  
        const postData = {
            "degree": '1차',
            "standardTerminology": String(this.form.standardTerminology),
            "description": String(this.form.description),
            "englishAbbreviation": String(this.form.englishAbbreviation),
            "relevantOrganization": String(this.form.relevantOrganization),
            "domain": stringifiedDomain,
            "standardCode": '-',
            "isCustom": true, 
        };
  
        axios.post('http://localhost:3001/terminology/add', postData, {
            headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
        })
        .then(response => {
            console.log(response, "데이터 등록 성공");
        })
        .catch(error => {
            console.error(error);
        });
      }
    }
  };
  </script>
  
  