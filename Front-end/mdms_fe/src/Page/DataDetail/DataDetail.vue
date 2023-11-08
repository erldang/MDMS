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
  import './DataDetail.css'
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
  
  