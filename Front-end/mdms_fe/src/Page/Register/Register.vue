<template>
  <div class="register-container">
    <h1>회원 가입</h1>
    <form @submit.prevent="handleRegister">
      <!-- 이름 입력 -->
      <div class="input-group">
        <label for="name">이름</label>
        <input type="text" id="name" v-model="name" placeholder="이름 입력" required />
      </div>

      <!-- 전화번호 입력 -->
      <div class="input-group">
        <label for="phone">전화번호</label>
        <div class="phone-inputs">
          <input type="text" id="phone" v-model="phone1" placeholder="010" required maxlength="3"
            @input="validateNumberInput" />
          <span>-</span>
          <input type="text" v-model="phone2" placeholder="1234" required maxlength="4" @input="validateNumberInput" />
          <span>-</span>
          <input type="text" v-model="phone3" placeholder="5678" required maxlength="4" @input="validateNumberInput" />
        </div>
      </div>

      <!-- 이메일 입력 및 인증 -->
      <div class="input-group">
        <label for="email">이메일</label>
        <input type="email" id="email" v-model="email" placeholder="이메일 입력" required />
        <button @click.prevent="handleEmailVerification">인증하기</button>
        <div v-if="showEmailVerification">
          <input type="text" v-model="verificationCode" placeholder="코드 입력" />
          <button @click.prevent="handleVerifyCode">확인하기</button>
        </div>
      </div>

      <!-- 비밀번호 입력 -->
      <div class="input-group">
        <label for="password">비밀번호</label>
        <input type="password" id="password" v-model="password" placeholder="비밀번호 입력" required />
      </div>

      <!-- 비밀번호 확인 입력 -->
      <div class="input-group">
        <label for="confirmPassword">비밀번호 확인</label>
        <input type="password" id="confirmPassword" v-model="confirmPassword" placeholder="비밀번호 확인" required />
        <div v-if="passwordMismatch">비밀번호가 일치하지 않습니다.</div>
      </div>

      <button type="submit">회원가입</button>
    </form>
  </div>
</template>
  
<script>
import axios from 'axios';


export default {
  name: 'RegisterPage',
  data() {
    return {
      name: '',
      phone1: '',
      phone2: '',
      phone3: '',
      email: '',
      password: '',
      confirmPassword: '',
      showEmailVerification: false,
      verificationCode: '',
      emailVerificationCode: '',
      isEmailVerified: false
    };
  },
  computed: {
    phone() {
      return `${this.phone1}-${this.phone2}-${this.phone3}`;
    },
    passwordMismatch() {
      return this.password && this.confirmPassword && this.password !== this.confirmPassword;
    }
  },
  methods: {
    validateNumberInput(event) {
      event.target.value = event.target.value.replace(/[^0-9]/g, '');
    },
    async handleEmailVerification() {
      try {
        const response = await axios.post('http://localhost:3001/user/authentication/send', {
          email: this.email
        });

        if (response.data.ok === 'ok') {
          this.emailVerificationCode = response.data.message;
          this.showEmailVerification = true;
          alert('이메일로 인증 코드가 전송되었습니다. 코드를 확인해 주세요.');
        } else {
          alert(`인증 오류: ${response.data.message}`);
        }
      } catch (error) {
        console.error('이메일 인증 오류', error);
        alert('이메일 인증 중 오류가 발생했습니다.');
      }
    },
    handleVerifyCode() {
      if (this.verificationCode === this.emailVerificationCode) {
        this.isEmailVerified = true;
        alert('이메일 인증이 완료되었습니다.');
      } else {
        alert('잘못된 인증 코드입니다. 다시 확인해 주세요.');
      }
    },
    async handleRegister() {
      if (!this.isEmailVerified) {
        alert('이메일 인증을 완료해주세요.');
        return;
      }
      if (this.passwordMismatch) {
        alert('비밀번호가 일치하지 않습니다. 다시 확인해 주세요.');
        return;
      }

      const userData = {
        "name": this.name,
        "phone": this.phone,
        "email": this.email,
        "password": this.password,
      };

      try {
        const response = await axios.post('http://localhost:3001/user/signup', userData);
        if (response.data.message === "ok") {
          alert('회원가입이 완료되었습니다.');
          this.$router.push('/login');
        } else {
          alert(`회원가입 실패: ${response.data.message}`);
        }
      } catch (error) {
        console.error('회원가입 처리 중 오류가 발생했습니다.', error);
        alert('회원가입 중 오류가 발생했습니다.');
      }
    }
  }
};
</script>

<style scoped>
.register-container {
  width: 100%;
  max-width: 400px;
  margin: 2rem auto;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  /* 강화된 그림자 효과 */
  background-color: #fff;
  /* 백색 배경 */
  border: 1px solid #ddd;
  /* 약간 더 진한 회색 */
}

h1 {
  text-align: center;
  margin-bottom: 2rem;
  font-size: 1.5rem;
  color: #35495E;
  /* 모던한 다크 블루 색상 */
}

.input-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #34495e;
  /* 어두운 회색 계열 */
}

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 0.95rem;
  transition: border-color 0.3s;
}

input:focus {
  border-color: #3498db;
  /* 밝은 파란색 */
  outline: none;
}

.phone-inputs {
  display: flex;
  align-items: center;
}

.phone-inputs input {
  flex: 1;
  margin-right: 0.5rem;
  border-radius: 5px;
}

.phone-inputs input:last-child {
  margin-right: 0;
}

.phone-inputs span {
  margin-right: 0.5rem;
  color: #34495e;
}

button {
  width: 100%;
  padding: 0.75rem;
  background-color: #27ae60;
  /* 녹색 계열 */
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-weight: 600;
}

button:hover {
  background-color: #229954;
  /* 어두운 녹색 */
}

.passwordMismatch {
  color: red;
  font-size: 0.85rem;
  margin-top: 0.25rem;
}</style>
