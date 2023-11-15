<!-- 도메인 검색 후 선택했을 때 선택된 도메인이 무엇인지 사용자에게 보여주기 
각 입력 창에서 엔터를 눌렀을 때 폼이 제출되는 것을 방지하기
각 입력 창이 비었다면 제출하지 못하게 하고 유효성 검사 로직 추가하기 -->
<template>
    <div>
      <h1>커스텀 표준 용어 추가</h1>
      <form @submit.prevent="submitForm">
        <div>
          <label for="standardTerminology">표준용어명:</label>
          <input type="text" id="standardTerminology" v-model="form.standardTerminology">
        </div>
        <div>
          <label for="description">설명:</label>
          <input type="text" id="description" v-model="form.description">
        </div>
        <div>
          <label for="englishAbbreviation">영어 약어 명:</label>
          <input type="text" id="englishAbbreviation" v-model="form.englishAbbreviation">
        </div>
        <div>
          <label for="relevantOrganization">기관명:</label>
          <input type="text" id="relevantOrganization" v-model="form.relevantOrganization">
        </div>
        <div>
          <label for="domainSearch">도메인 검색:</label>
          <input type="text" id="domainSearch" v-model="domainSearchQuery" @input="searchDomain">
          <!-- 도메인 검색 결과 표시 -->
          <ul>
            <li v-for="domain in domainSearchResults" :key="domain.no" @click="selectDomain(domain)">
              {{ domain.classificationName }} - {{ domain.description }} - {{ domain.dataType }} - {{ domain.dataLength }}
            </li>
          </ul>
        </div>
        <button type="submit">등록</button>
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
        allDomains: [], // 서버로부터 가져온 전체 도메인 데이터를 저장
        domainSearchResults: [],
      };
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
  
  