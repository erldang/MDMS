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
        <button @click="openEditProfileForm">프로필 수정</button>
        <button @click="confirmDeleteUser">탈퇴하기</button>
      </form>
    </div>

    <!-- 오류 메시지 -->
    <div v-if="error" class="error-message">{{ error }}</div>

    <!-- 프로필 수정 폼 -->
    <EditProfile
      v-if="showEditProfile"
      :initialProfile="profile.data"
      @profile-updated="fetchProfile"
      @profile-canceled="cancelEditProfileForm"
      class="edit-profile-form"
    />
    
    <!-- 탈퇴 다이얼로그 -->
    <div v-if="showDeleteConfirmation">
      <p>정말로 탈퇴하시겠습니까?</p>
      <button @click="deleteUser">확인</button>
      <button @click="cancelDelete">취소</button>
    </div>
  </div>
</template>
<script>
import EditProfile from './EditProfile.vue'; 
import './Profile.css';
export default {
  data() {
    return {
      profile: null,
      error: null,
      showEditProfile: false,
      showDeleteConfirmation: false, 
    };
  },
  methods: {
    async fetchProfile() {
      const token = localStorage.getItem('token'); 

      if (!token) {
        this.$router('/login');
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
          this.profile = data.data;
        } else {
          this.error = "프로필을 불러오는 데 문제가 발생했습니다.";
        }
      } catch (error) {
        this.error = "서버와 통신 중 오류가 발생했습니다.";
      }
    },
    openEditProfileForm() {
      this.showEditProfile = true;
    },
    confirmDeleteUser() {
      this.showDeleteConfirmation = true;
    },
    cancelDelete() {
      this.showDeleteConfirmation = false;
    },
    async deleteUser() {
      const token = localStorage.getItem('token');

      const url = 'http://localhost:3001/user/delete';
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      };
      const body = JSON.stringify({ email: this.profile.email });

      try {
        const response = await fetch(url, {
          method: 'DELETE',
          headers,
          body,
        });
        const data = await response.json();

        if (data.ok === 'ok') {
          // 탈퇴 성공 시 처리
          alert('탈퇴가 완료되었습니다.');
          // 로그아웃 처리 또는 리다이렉트 등을 추가하세요.
        } else {
          // 탈퇴 실패 시 처리
          alert('탈퇴에 실패했습니다. 다시 시도하세요.');
          // 오류 메시지를 표시하거나 페이지를 리로딩하십시오.
        }
      } catch (error) {
        // 서버와 통신 중 오류 발생 시 처리
        alert('서버와 통신 중 오류가 발생했습니다.');
      }
    },
    cancelEditProfileForm() {
      this.showEditProfile = false;
    },
  },
  mounted() {
    this.fetchProfile();
  },
  components: {
    EditProfile,
  },
};
</script>
