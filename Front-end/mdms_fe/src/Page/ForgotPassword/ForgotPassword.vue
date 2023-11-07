<template>
    <div class="forgot-container">
      <h1>아이디/비밀번호 찾기</h1>
      <div class="tabs">
        <button @click="showID" :class="{ active: isID }">아이디 찾기</button>
        <button @click="showPassword" :class="{ active: isPassword }">비밀번호 찾기</button>
      </div>
  
      <!-- 아이디 찾기 폼 -->
      <form v-if="isID" @submit.prevent="submitID">
        <div class="input-group">
          <label for="name">이름</label>
          <input type="text" id="name" v-model="name" placeholder="이름 입력" required>
        </div>
  
        <div class="input-group">
          <label for="phone">전화번호</label>
          <div class="phone-inputs">
            <input type="text" id="phone" v-model="phone1" placeholder="010" required maxlength="3" @input="validateNumberInput" />
            <span>-</span>
            <input type="text" v-model="phone2" placeholder="1234" required maxlength="4" @input="validateNumberInput" />
            <span>-</span>
            <input type="text" v-model="phone3" placeholder="5678" required maxlength="4" @input="validateNumberInput" />
          </div>
        </div>
  
        <div class="action-buttons">
          <button type="submit">확인</button>
          <button type="button" @click="cancel">취소</button>
        </div>
      </form>
  
      <!-- 비밀번호 찾기 폼 -->
      <form v-if="isPassword" @submit.prevent="submitPassword">
        <div class="input-group">
          <label for="email">아이디</label>
          <input type="email" id="email" v-model="email" placeholder="아이디 입력" required>
        </div>
  
        <div class="input-group">
          <label for="name">이름</label>
          <input type="text" id="name" v-model="name" placeholder="이름 입력" required>
        </div>
  
        <div class="input-group">
          <label for="phone">전화번호</label>
          <div class="phone-inputs">
            <input type="text" id="phone" v-model="phone1" placeholder="010" required maxlength="3" @input="validateNumberInput" />
            <span>-</span>
            <input type="text" v-model="phone2" placeholder="1234" required maxlength="4" @input="validateNumberInput" />
            <span>-</span>
            <input type="text" v-model="phone3" placeholder="5678" required maxlength="4" @input="validateNumberInput" />
          </div>
        </div>
  
        <div class="action-buttons">
          <button type="submit">확인</button>
          <button type="button" @click="cancel">취소</button>
        </div>
      </form>
  
      <div v-if="resultMessage" class="result-message">{{ resultMessage }}</div>
    </div>
  </template>
  
  <script>
  import './ForgotPassword.css'
  import axios from 'axios'; 
  
  export default {
    name: 'ForgotPassword',
    data() {
      return {
        isID: false,
        isPassword: false,
        name: '',
        email: '',
        phone1: '',
        phone2: '',
        phone3: '',
        resultMessage: '', // 결과 메시지를 표시하기 위한 변수 추가
      };
    },
    methods: {
      validateNumberInput(event) {
        // 전화번호 칸에는 숫자만 가능하게
        event.target.value = event.target.value.replace(/[^0-9]/g, '');
      },
      showID() {
        this.isID = true;
        this.isPassword = false;
        this.resultMessage = ''; // 결과 메시지 초기화
      },
      showPassword() {
        this.isPassword = true;
        this.isID = false;
        this.resultMessage = ''; // 결과 메시지 초기화
    },
        async submitID() {
        try {
            const response = await axios.post('http://localhost:3001/user/findId', { // 엔드포인트 수정
            name: this.name,
            phone: `${this.phone1}-${this.phone2}-${this.phone3}`,
            });

            const data = response.data;

            if (data.data && data.data.length > 0) {
            const foundEmail = data.data[0].email;
            this.resultMessage = `아이디는 ${foundEmail} 입니다.`;
            } else {
            this.resultMessage = '일치하는 아이디가 없습니다.';
            }
        } catch (error) {
            console.error('아이디 찾기 에러:', error);
            this.resultMessage = '서버 오류로 아이디를 찾을 수 없습니다.';
        }
        },
        async submitPassword() {
        try {
            const response = await axios.post('http://localhost:3001/user/findPwd', { // 엔드포인트 수정
            email: this.email,
            name: this.name,
            phone: `${this.phone1}-${this.phone2}-${this.phone3}`,
            });

            const data = response.data;

            if (data.data && data.data.password) {
            const foundPassword = data.data.password;
            this.resultMessage = `비밀번호는 ${foundPassword} 입니다.`;
            } else {
            this.resultMessage = '일치하는 비밀번호가 없습니다.';
            }
        } catch (error) {
            console.error('비밀번호 찾기 에러:', error);
            this.resultMessage = '서버 오류로 비밀번호를 찾을 수 없습니다.';
        }
        },
        cancel() {
        this.$router.go(-1); // 이전 페이지로 돌아감
        },
    },
  };
  </script>
  