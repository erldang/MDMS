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
        domainSearchResults: [],
      };
    },
    methods: {
      searchDomain() {
        // 도메인 검색 로직
        axios.get(`localhost:3001/domain?query=${this.domainSearchQuery}`, {
          headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
        })
        .then(response => {
          this.domainSearchResults = response.data.data;
        })
        .catch(error => {
          console.error(error);
        });
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

        axios.post('localhost:3001/terminology/add', postData, {
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
  