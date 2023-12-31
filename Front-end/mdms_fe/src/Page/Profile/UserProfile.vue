<template>
  <div>
    <!-- 프로필 폼 -->
    <div v-if="profile">
      <h2>프로필 정보</h2>
      <form>
        <!-- 프로필 정보 표시 부분 -->
        <div>
          <label for="email">이메일:</label>
          <input type="text" id="email" v-model="profile.data.email" readonly />
        </div>
        <div>
          <label for="name">이름:</label>
          <input type="text" id="name" v-model="profile.data.name" readonly />
        </div>
        <div>
          <label for="phone">전화번호:</label>
          <input type="text" id="phone" v-model="profile.data.phone" readonly />
        </div>
        <!-- 수정 버튼 -->
        <button type="button" @click="openEditProfileForm">프로필 수정</button>
        <button type="button" @click="confirmDeleteUser">탈퇴하기</button>
      </form>
    </div>

    <!-- 오류 메시지 -->
    <div v-if="error" class="error-message">{{ error }}</div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      profile: {
        data: {
          email: '',
          name: '',
          phone: '',
          password: '',
          admin: ''
        }
      },
      error: null,
    };
  },
  methods: {
    async fetchProfile() {
      const token = localStorage.getItem('token');

      if (!token) {
        this.$router.push('/login');
        return;
      }

      const url = `http://localhost:3001/user/specific?token=${token}`;
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      };

      try {
        const response = await fetch(url, { headers });
        const data = await response.json();

        if (data.ok === "ok") {
          this.profile.data = data.data;
        } else {
          this.error = "프로필을 불러오는 데 문제가 발생했습니다.";
        }
      } catch (error) {
        this.error = "서버와 통신 중 오류가 발생했습니다.";
      }
    },
    openEditProfileForm() {
      this.$router.push('/edit-profile'); // 페이지 이동을 위한 라우터 push
    },
    confirmDeleteUser() {
      // confirm 함수를 사용하여 사용자에게 탈퇴 확인
      if (confirm("정말로 탈퇴하시겠습니까?")) {
        this.deleteUser();
      }
    },
    async deleteUser() {
      const token = localStorage.getItem('token'); // 인증을 위한 토큰을 로컬 스토리지에서 가져옴

      if (!token) {
        this.error = "인증 토큰이 없습니다. 다시 로그인해주세요.";
        return;
      }

      const url = `http://localhost:3001/user/delete`;
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      };

      const body = JSON.stringify({
        email: this.profile.data.email // 프로필에서 이메일을 가져와 요청 본문에 포함
      });

      try {
        const response = await fetch(url, {
          method: 'DELETE',
          headers: headers,
          body: body
        });
        const data = await response.json();

        if (data.ok === "ok") {
          alert(data.message); // 성공 메시지를 alert로 보여줌
          // 여기에서 로그아웃 로직을 실행하거나, 로그인 페이지로 리다이렉트 등의 후속 조치를 취할 수 있음
          this.$router.push('/login');
        } else {
          this.error = data.message || "삭제 과정에 문제가 발생했습니다.";
        }
      } catch (error) {
        this.error = "서버와의 통신 중 오류가 발생했습니다.";
      }
    }
  },
  mounted() {
    this.fetchProfile();
  },
};
</script>
<style scoped>
h2 {
  text-align: center;
  color: #35495E;
  /* 모던한 다크 블루 색상 */
  margin-bottom: 20px;
}

form {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  /* 그림자 효과 */
  background-color: #fff;
  /* 백색 배경 */
}

div {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  color: #34495e;
  /* 어두운 회색 계열 */
  font-weight: bold;
}

input[type="text"] {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-sizing: border-box;
  background-color: #f2f2f2;
  /* 연한 회색 */
  color: #333;
}

button {
  padding: 10px 20px;
  margin-right: 10px;
  background-color: #27ae60;
  /* 녹색 계열 */
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #229954;
  /* 어두운 녹색 */
}

.error-message {
  color: #e74c3c;
  /* 경고, 오류 메시지에 적합한 색상 */
  text-align: center;
  margin-top: 20px;
}</style>
