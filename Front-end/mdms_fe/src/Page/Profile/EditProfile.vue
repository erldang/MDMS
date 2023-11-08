<template>
    <div class="edit-profile-form"> <!-- 클래스 추가 -->
      <h2>프로필 수정</h2>
      <form @submit.prevent="submitProfile">
        <!-- 이름, 이메일, 전화번호, 비밀번호 입력 필드 -->
        <div>
          <label for="name">이름:</label>
          <input type="text" id="name" v-model="editedProfile.name" />
        </div>
        <div>
          <label for="email">이메일:</label>
          <input type="text" id="email" v-model="editedProfile.email" />
        </div>
        <div>
          <label for="phone">전화번호:</label>
          <input type="text" id="phone" v-model="editedProfile.phone" />
        </div>
        <div>
          <label for="password">비밀번호:</label>
          <input type="password" id="password" v-model="editedProfile.password" />
        </div>
        <button type="submit">저장</button>
        <button @click="cancel">취소</button>
      </form>
      <!-- 오류 메시지 표시 -->
      <div v-if="error" class="error-message">{{ error }}</div>
  </div>
</template>
  
<script>
import './Profile.css';
export default {
  props: {
    initialProfile: Object,
  },
  data() {
    return {
      editedProfile: {
        name: '',
        email: '',
        phone: '',
        password: '',
      },
      error: null, // 오류 메시지를 저장할 변수 추가
    };
  },
  mounted() {
    if (this.initialProfile) {
      this.editedProfile = { ...this.initialProfile };
    }
  },
  methods: {
      async submitProfile() {
        const token = localStorage.getItem('token');
  
        const url = 'http://localhost:3001/user/modify';
        const headers = {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        };
        const body = JSON.stringify(this.editedProfile);
  
        try {
          const response = await fetch(url, {
            method: 'POST',
            headers,
            body,
          });
          const data = await response.json();
  
          if (data.ok === 'ok') {
            this.$emit('profile-updated');
            this.cancel();
          } else {
            this.error = '프로필 수정에 실패했습니다. 다시 시도하세요.'; // 오류 메시지 설정
          }
        } catch (error) {
          this.error = '서버와 통신 중 오류가 발생했습니다.'; // 오류 메시지 설정
        }
      },
      cancel() {
        this.$emit('profile-canceled');
      },
    },
  };
  </script>
  