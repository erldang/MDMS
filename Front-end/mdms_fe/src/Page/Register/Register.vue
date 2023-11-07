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
            <input type="text" id="phone" v-model="phone1" placeholder="010" required maxlength="3" @input="validateNumberInput" />
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
  //import { useRouter } from 'vue-router';
  
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
          const response = await axios.post('http://localhost:8080/user/authentication/send', {
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
          name: this.name,
          phone: this.phone,
          email: this.email,
          password: this.password,
        };
  
        try {
          const response = await axios.post('http://localhost:8080/user/register', userData);
          if (response.data.success) {
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
.phone-inputs {
    display: flex;
    align-items: center;
}

.phone-inputs input {
    width: calc((100% - 2rem) / 3);
    margin-right: 0.5rem;
}

.phone-inputs input:last-child {
    margin-right: 0;
}

.phone-inputs span {
    margin-right: 0.5rem;
}
.register-container {
    width: 100%;
    max-width: 400px;
    margin: 2rem auto;
    padding: 2rem;
    border: 1px solid #eaeaea;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h1 {
    text-align: center;
    margin-bottom: 2rem;
    font-size: 1.5rem;
    color: #333;
}

.input-group {
    margin-bottom: 1.5rem;
}

label {
    display: block;
    margin-bottom: 0.5rem;
    font-weight: 600;
    color: #555;
}

input {
    width: 100%;
    padding: 0.75rem;
    border: 1px solid #eaeaea;
    border-radius: 4px;
    font-size: 0.95rem;
    transition: border 0.3s;
}

input:focus {
    border-color: #007BFF;
    outline: none;
}

button {
    width: 100%;
    padding: 0.75rem;
    background-color: #007BFF;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
    font-weight: 600;
}

button:hover {
    background-color: #0056b3;
}

input-group>button {
    margin-top: 0.5rem;
}

.passwordMismatch {
    color: red;
    font-size: 0.85rem;
    margin-top: 0.25rem;
}
</style>
