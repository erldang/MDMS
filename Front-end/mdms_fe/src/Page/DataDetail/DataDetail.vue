<template>
    <div class="data-detail-header">
      <h1 class="data-title">데이터 제목</h1>
  
      <div class="action-buttons">
        <button @click="registerData" class="btn-action">데이터 등록</button>
        <button @click="editData" class="btn-action">수정하기</button>
        <button @click="deleteData" class="btn-action">삭제</button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'DataDetail',
    methods: {
      registerData() {
      },
      editData() {
      },
      deleteData() {
      }
    }
  }
  </script>
  
  <style scoped>
  .data-detail-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
  }
  
  .data-title {
    margin: 0;
    padding: 0;
  }
  
  .action-buttons {
    display: flex;
    justify-content: space-around;
    margin-bottom: 20px;
  }
  
  .btn-action {
  padding: 5px 15px;
  margin-left: 10px; /* adds spacing between buttons */
  border: none;
  cursor: pointer;
  border-radius: 4px;
  color: white;
}
  .btn-register,
  .btn-edit,
  .btn-delete {
    padding: 10px 20px;
    border: none;
    cursor: pointer;
    border-radius: 5px;
    color: white;
  }
  
  .btn-register {
    background-color: #4CAF50;
  }
  
  .btn-edit {
    background-color: #2196F3; 
  }
  
  .btn-delete {
    background-color: #f44336;
  }
  
  .btn-register:hover,
  .btn-edit:hover,
  .btn-delete:hover {
    opacity: 0.8;
  }
  </style>
  