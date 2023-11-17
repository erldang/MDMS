<template>
  <div>
    <h1 class="page-title">회원 정보 수정</h1>
    <form @submit.prevent="submitForm" class="user-form">
      <div class="input-group">
        <label for="email" class="input-label">Email:</label>
        <input type="email" class="input-field" v-model="user.email" required>
      </div>
      <div class="input-group">
        <label for="name" class="input-label">Name:</label>
        <input type="text" class="input-field" v-model="user.name" required>
      </div>
      <div class="input-group">
        <label for="password" class="input-label">Password:</label>
        <input type="password" class="input-field" v-model="user.password" required>
      </div>
      <div class="input-group">
        <label for="phone" class="input-label">Phone:</label>
        <input type="tel" class="input-field" v-model="user.phone" required>
      </div>
      <button type="submit" class="submit-button">변경 사항 저장</button>
    </form>
    <div v-if="message" class="form-message">{{ message }}</div>
  </div>
</template>

  
<script>
export default {
  data() {
    return {
      user: {
        "email": '',
        "name": '',
        "password": '',
        "phone": ''
      },
      message: ''
    };
  },
  methods: {
    async submitForm() {
      const token = localStorage.getItem('token');
      try {
        const response = await fetch('http://localhost:3001/user/modify', {
          method: 'POST',
          headers: { 'Authorization': `Bearer ${token}` },
          body: JSON.stringify(this.user)
        });
        if (response.ok) {
          const result = await response.json();
          this.message = result.message; // "유저 정보 변경 완료"
          this.router.push('/profile');
        } else {
          this.message = '오류가 발생했습니다.';
        }
      } catch (error) {
        console.error('An error occurred:', error);
        this.message = '오류가 발생했습니다.';
      }
    }
  }
};
</script>
  
<style scoped>
.page-title {
  text-align: center;
  color: #35495E;
  /* 모던한 다크 블루 색상 */
  font-size: 24px;
  margin-bottom: 30px;
}

.user-form {
  max-width: 500px;
  margin: 0 auto;
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  /* 그림자 효과 */
  border: 1px solid #ddd;
}

.input-group {
  margin-bottom: 20px;
}

.input-label {
  display: block;
  font-weight: bold;
  margin-bottom: 10px;
  color: #34495e;
  /* 어두운 회색 계열 */
}

.input-field {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #bdc3c7;
  border-radius: 5px;
  box-sizing: border-box;
}

.input-field:focus {
  border-color: #3498db;
  /* 밝은 파란색 */
  outline: none;
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
  margin-top: 10px;
}

.submit-button:hover {
  background-color: #229954;
  /* 어두운 녹색 */
}

.form-message {
  text-align: center;
  margin-top: 20px;
  color: #e74c3c;
  /* 경고, 오류 메시지에 적합한 색상 */
}</style>
