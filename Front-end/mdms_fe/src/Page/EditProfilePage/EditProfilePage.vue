<template>
    <div>
      <h1>회원 정보 수정</h1>
      <form @submit.prevent="submitForm">
        <div>
          <label for="email">Email:</label>
          <input type="email" v-model="user.email" required>
        </div>
        <div>
          <label for="name">Name:</label>
          <input type="text" v-model="user.name" required>
        </div>
        <div>
          <label for="password">Password:</label>
          <input type="password" v-model="user.password" required>
        </div>
        <div>
          <label for="phone">Phone:</label>
          <input type="tel" v-model="user.phone" required>
        </div>
        <button type="submit">변경 사항 저장</button>
      </form>
      <div v-if="message">{{ message }}</div>
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
  /* Scoped styles here */
  form {
    display: flex;
    flex-direction: column;
  }
  form div {
    margin-bottom: 1rem;
  }
  label {
    margin-bottom: 0.5rem;
  }
  input {
    padding: 0.5rem;
    font-size: 1rem;
  }
  button {
    padding: 0.5rem 1rem;
    font-size: 1rem;
    cursor: pointer;
  }
  </style>
  