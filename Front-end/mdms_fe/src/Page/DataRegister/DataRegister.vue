<template>
    <div class="data-register-container">
      <h1 class="data-register-title">데이터 제목(수정가능)</h1>
      <div class="process-steps">
        <div class="process-step">
          <div class="step-content">
            <p>편집 기능 추가 예정</p>
            <button @click="showModal = true">편집</button>
          </div>
        </div>
        <div class="process-arrow">
          <p>➡️</p>
        </div>
        <div class="process-step">
          <div class="step-content">
            <p>생성된 DDL 표기</p>
          </div>
        </div>
        <div class="process-arrow">
          <p>➡️</p>
        </div>
        <div class="process-step">
          <div class="step-content">
            <p>테이블 등록</p>
          </div>
        </div>
      </div>
      <div v-if="showModal" class="modal-overlay" @click="closeModal">
        <div class="modal-content" @click.stop>
        <h2>편집 창</h2>
        <div class="data-columns-container">
            <data-column v-for="(column, index) in dataColumns" :key="index">
            <button @click="removeDataColumn(index)">제거</button> 
            </data-column>
            <button class="add-button" @click="addDataColumn">+</button>
        </div>
        
        <button class="close-button" @click="closeModal">닫기</button>
        </div>
    </div>
  </div>
</template>
  
<script>
import './DataRegister.css'
import DataColumn from '@/Layout/DataColumn.vue';
export default {
    components: {
    DataColumn 
  },
  data() {
    return {
      showModal: false,
      dataColumns: [
        {}, {}, {} // 초기에 3개의 DataColumn을 표시
      ],
    };
    
  },
  methods: {
    closeModal() {
      this.showModal = false;
    },
    addDataColumn() {
      this.dataColumns.push({}); // 새 DataColumn 객체를 추가
    },
    removeDataColumn(index) {
        this.dataColumns.splice(index, 1); 
    }
  }
};
</script>
  