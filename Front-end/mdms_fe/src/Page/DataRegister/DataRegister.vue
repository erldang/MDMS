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
  
<style scoped>
  .data-register-container {
    text-align: center;
  }
  
  .data-register-title {
    margin-bottom: 20px;
  }
  
  .process-steps {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .process-step {
    border: 2px solid #4CAF50;
    border-radius: 15px;
    padding: 20px;
    width: 150px;
    height: 100px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 10px;
  }
  
  .process-arrow {
    font-size: 24px;
    color: #333;
  }
  
  .step-content p {
    margin: 0;
  }
  
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 15px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.26);
    z-index: 100;
  }
  
  .data-columns-container {
    display: flex;
    justify-content: flex-start; /* Changed to flex-start to align children to the start */
    align-items: flex-start;
    flex-wrap: wrap;
    gap: 10px;
    padding-left: 20px; /* Adding padding to ensure "+" button does not stick to the side */
  }

  .add-button {
    margin: 0 auto; /* This will center the "+" button */
    display: flex; /* Use flex to center content inside the button */
    align-items: center; /* Center content vertically */
    justify-content: center; /* Center content horizontally */
    padding: 10px; /* Add some padding */
    background-color: #4CAF50; /* Use a distinct color */
    color: white; /* Text color */
    font-size: 24px; /* Make it larger */
    border-radius: 50%; /* Circle shape */
    width: 40px; /* Fixed width */
    height: 40px; /* Fixed height */
    cursor: pointer;
    border: none; /* No border */
    outline: none; /* No outline */
  }

  .data-column {
    margin: 10px;
    padding: 20px;
    flex: 1;
    min-width: 150px;
    max-width: 300px;
  }
  
  .data-column button {
    background-color: #f44336; /* A red color for visibility */
    color: white; /* White text for contrast */
    padding: 10px 24px;
    border: none;
    border-radius: 12px;
    cursor: pointer;
    outline: none; /* Removes the outline */
    margin-top: 10px; /* Add some space above the remove button */
    width: 100%; /* Make the button full width */
  }
  .process-step button {
    background-color: #4CAF50; /* Green */
    border: none;
    color: white;
    padding: 10px 24px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    margin: 4px 2px;
    cursor: pointer;
    border-radius: 12px;
    outline: none; /* Optional: removes the outline */
  }
</style>
  