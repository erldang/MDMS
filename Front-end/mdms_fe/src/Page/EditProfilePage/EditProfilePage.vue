<template>
  <div>
    <h2>프로필 수정</h2>
    <form @submit.prevent="submitProfile">
      <div>
        <label for="name">이름:</label>
        <input type="text" id="name" v-model="profile.name" />
      </div>
      <div>
        <label for="phone">전화번호:</label>
        <input type="text" id="phone" v-model="profile.phone" />
      </div>
      <div>
        <label for="email">이메일:</label>
        <input type="email" id="email" v-model="profile.email" />
        <button @click.prevent="sendEmailVerification">인증하기</button>
        <input type="text" v-if="showEmailVerificationInput" v-model="emailVerificationCode" placeholder="인증 코드 입력" />
      </div>
      <div>
        <button type="button" @click="enablePasswordChange">비밀번호 수정</button>
        <div v-if="showPasswordFields">
          <input type="password" v-model="newPassword" placeholder="새 비밀번호" />
          <input type="password" v-model="newPasswordConfirmation" placeholder="비밀번호 확인" />
        </div>
      </div>
      <button type="submit">수정하기</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      profile: {
        email: '',
        name: '',
        phone: '',
      },
      newPassword: '',
      newPasswordConfirmation: '',
      emailVerificationCode: '',
      receivedEmailVerificationCode: '',
      showEmailVerificationInput: false,
      showPasswordFields: false,
      originalEmail: '',
    };
  },
  methods: {
    displayError(message) {
      alert(message);
    },
    async fetchProfile() {
      const token = localStorage.getItem('token');
      if (!token) {
        this.$router.push('/login');
        return;
      }
      const url = `http://localhost:3001/user/specific?token=${token}`;
      try {
        const response = await fetch(url, { 
          headers: { 
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });
        const { ok, data } = await response.json();
        if (ok === "ok") {
          this.profile = { ...data }; 
          this.originalEmail = this.profile.email;
        } else {
          this.displayError("프로필을 불러오는 데 문제가 발생했습니다.");
        }
      } catch (error) {
        this.displayError("서버와 통신 중 오류가 발생했습니다.");
      }
    },
    async sendEmailVerification() {
      // 이메일 인증 코드 전송 로직
      try {
        const response = await fetch('http://localhost:3001/user/authentication/send', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ email: this.profile.email })
        });
        const { ok, message } = await response.json();
        if (ok === "ok") {
          this.receivedEmailVerificationCode = message;
          this.showEmailVerificationInput = true;
        } else {
          this.displayError("이메일 인증 코드 전송 실패");
        }
      } catch (error) {
        this.displayError("이메일 인증 중 오류가 발생했습니다.");
      }
    },
    enablePasswordChange() {
      this.showPasswordFields = true;
    },
    async submitProfile() {
      if (this.showEmailVerificationInput && this.emailVerificationCode !== this.receivedEmailVerificationCode) {
        this.displayError("이메일 인증 코드가 일치하지 않습니다.");
        return;
      }
      if (this.showPasswordFields && this.newPassword !== this.newPasswordConfirmation) {
        this.displayError("비밀번호가 일치하지 않습니다.");
        return;
      }
      const submitData = {
        name: this.profile.name,
        phone: this.profile.phone
      };
      if (this.profile.email !== this.originalEmail) {
        submitData.email = this.profile.email;
      }
      if (this.showPasswordFields) {
        submitData.password = this.newPassword;
      }
      try {
        const response = await fetch('http://localhost:3001/user/modify', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(submitData)
        });
        const { ok } = await response.json();
        if (ok === "ok") {
          this.$router.push('/profile');
        } else {
          this.displayError("유저 프로필 변경에 실패하였습니다.");
        }
      } catch (error) {
        this.displayError("서버와의 통신 중 오류가 발생했습니다.");
      }
    }
  },
  mounted() {
    this.fetchProfile();
  },
};
</script>

<style scoped>
div {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  background-color: #fff;
}

h2 {
  color: #333;
  text-align: center;
  margin-bottom: 20px;
}

form {
  display: flex;
  flex-direction: column;
}

/* 각 입력 필드와 레이블에 대한 스타일 */
form > div {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  color: #666;
}

input[type="text"],
input[type="email"],
input[type="password"] {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box; /* input 크기가 부모 요소를 기준으로 설정됩니다. */
}

/* 버튼에 대한 스타일 */
button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  background-color: #5c6bc0;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #3f51b5;
}

button[type="submit"] {
  margin-top: 10px;
}

/* 인증 버튼은 기본 버튼과 다르게 보이게 합니다. */
button[type="button"] {
  background-color: #eee;
  color: #333;
}

button[type="button"]:hover {
  background-color: #ddd;
}

/* 인증 코드 입력이 나타날 때의 스타일 */
input[type="text"][placeholder="인증 코드 입력"] {
  margin-top: 10px;
}

/* 오류 메시지 표시를 위한 기본 스타일 (에러 핸들링 함수에 의존) */
.error-message {
  color: #d32f2f;
  margin: 10px 0;
  text-align: center;
}
</style>
