<template>
  <div>
    <h1>관리자 페이지</h1>
    <h2>사용자 이력 조회</h2>
    <div class="button-group">
      <button @click="navigateToMain">표준 용어</button>
      <button @click="navigateToDataMap">데이터 맵</button>
      <button :disabled="$route.path === '/admin-user-record'">사용자 이력 조회</button>
    </div>

    <!-- 여기에 사용자 이력 조회를 위한 추가적인 내용을 배치할 수 있습니다 -->

  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'AdminUserRecordPage',
  data() {
    return {
      // 여기에 필요한 데이터 속성들을 정의합니다
    };
  },
  created() {
    // 필요한 데이터를 가져오는 로직을 여기에 작성합니다
  },
  methods: {
    navigateToMain() {
      this.$router.push('/admin-main');
    },
    navigateToDataMap() {
      this.$router.push('/admin-datamap');
    },
    // 여기에 필요한 추가 메서드들을 정의합니다
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