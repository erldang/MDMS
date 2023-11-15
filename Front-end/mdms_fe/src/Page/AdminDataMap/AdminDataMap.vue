<template>
  <div>
    <h1>관리자 페이지</h1>
    <h2>데이터 맵</h2>
    <div class="button-group">
      <button @click="navigateToMain">표준 용어</button>
      <button :disabled="$route.path === '/admin-datamap'">데이터 맵</button>
      <button @click="navigateToUserRecord">사용자 이력 조회</button>
    </div>
    <p>전체 테이블 목록, 전체 용어 목록 | 테이블 or 용어 선택 시 데이터 맵 생성 공간</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'AdminDataMapPage',
  data() {
    return {
    };
  },
  created() {
  },
  methods: {
    navigateToMain() {
      this.$router.push('/admin-main');
    },
    navigateToUserRecord() {
      this.$router.push('/admin-user-record');
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

h1, h2 {
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
</style>
