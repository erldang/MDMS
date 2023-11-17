<template>
  <div>
    <h1 class="page-title">커스텀 표준 용어 추가</h1>
    <form @submit.prevent="submitForm" class="standard-term-form">
      <div class="input-group">
        <label for="standardTerminology" class="input-label">표준용어명:</label>
        <input type="text" id="standardTerminology" class="input-field" v-model="form.standardTerminology"
          @keypress.enter.prevent>
      </div>
      <div class="input-group">
        <label for="description" class="input-label">설명:</label>
        <input type="text" id="description" class="input-field" v-model="form.description" @keypress.enter.prevent>
      </div>
      <div class="input-group">
        <label for="englishAbbreviation" class="input-label">영어 약어 명:</label>
        <input type="text" id="englishAbbreviation" class="input-field" v-model="form.englishAbbreviation"
          @keypress.enter.prevent>
      </div>
      <div class="input-group">
        <label for="relevantOrganization" class="input-label">기관명:</label>
        <input type="text" id="relevantOrganization" class="input-field" v-model="form.relevantOrganization"
          @keypress.enter.prevent>
      </div>
      <div class="input-group">
        <label for="domainSearch" class="input-label">도메인 검색:</label>
        <input type="text" id="domainSearch" class="input-field" v-model="domainSearchQuery" @input="searchDomain"
          @keypress.enter.prevent>
        <p v-if="form.domain" class="selected-domain">선택된 도메인: {{ form.domain.classificationName }}</p>
        <ul class="search-results">
          <li v-for="domain in domainSearchResults" :key="domain.no" class="search-result-item"
            @click="selectDomain(domain)">
            {{ domain.classificationName }} - {{ domain.description }} - {{ domain.dataType }} - {{ domain.dataLength }}
          </li>
        </ul>
      </div>
      <button type="submit" class="submit-button" :disabled="!isValid">등록</button>
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
  
<style scoped>
.page-title {
  text-align: center;
  color: #35495E;
  /* 모던한 다크 블루 색상 */
  margin-bottom: 20px;
}

.standard-term-form {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  /* 그림자 효과 */
  background-color: #fff;
  /* 백색 배경 */
}

.input-group {
  margin-bottom: 15px;
}

.input-label {
  display: block;
  margin-bottom: 5px;
  color: #34495e;
  /* 어두운 회색 계열 */
  font-weight: bold;
}

.input-field {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-sizing: border-box;
}

.phone-inputs {
  display: flex;
  align-items: center;
}

.phone-inputs input {
  flex: 1;
  margin-right: 10px;
  border-radius: 5px;
}

.phone-inputs input:last-child {
  margin-right: 0;
}

.phone-inputs span {
  color: #34495e;
}

.submit-button {
  width: 100%;
  padding: 12px;
  background-color: #27ae60;
  /* 녹색 계열 */
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background-color: #229954;
  /* 어두운 녹색 */
}

.search-results {
  list-style: none;
  padding: 0;
}

.search-result-item {
  padding: 8px;
  margin-bottom: 5px;
  background-color: #f2f2f2;
  /* 연한 회색 */
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-result-item:hover {
  background-color: #ddd;
  /* 약간 더 진한 회색 */
}</style>
