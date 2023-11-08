<template>
    <div class="profile-container">
      <h1>회원정보</h1>
      <form @submit.prevent="changeProfile">
        <div class="input-group">
          <label for="name" class="kind">이름</label>
          <input type="text" id="name" v-model="name" :disabled="!isEditing" />
        </div>
  
        <div class="input-group">
          <label for="email" class="kind">이메일</label>
          <label for="email">{{ email }}</label>
        </div>
  
        <div class="input-group">
          <label for="password" class="kind">비밀번호</label>
          <button @click.prevent="updatePassword">변경하기</button>
          <div v-if="showUpdatePassword">
            <input type="password" v-model="newPassword" placeholder="새로운 비밀번호 입력" />
            <input type="password" v-model="confirmNewPassword" placeholder="비밀번호 재입력" />
            <button @click="checkChangePassword">확인하기</button>
          </div>
        </div>
  
        <div class="input-group">
          <label for="Phone" class="kind">전화번호</label>
          <input type="text" id="phone" v-model="phone" :disabled="!isEditing" />
        </div>
  
        <div>
          <button id="edit" type="button" @click="toggleEditing">{{ isEditing ? '수정 완료' : '수정하기' }}</button>
          <button id="withdraw" @click="showDeleteConfirmation">회원탈퇴</button>
        </div>
      </form>
  
      <div v-if="showDeleteDialog" class="delete-dialog">
        <p>비밀번호를 입력해 주세요.</p>
        <input type="password" v-model="deletePassword" placeholder="비밀번호 입력" />
        <div>
          <button @click="confirmDelete">확인</button>
          <button @click="cancelDelete">취소</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import './Profile.css'
  export default {
    name: 'ProfilePage',
    data() {
      return {
        name: '',
        email: '',
        password: '', 
        showUpdatePassword: false,
        phone: '',
        newPassword: '',
        confirmNewPassword: '',
        isEditing: false,
        errorMessage: '',
        showDeleteDialog: false,
        deletePassword: '',
      }
    },
    methods: {
      logout() {
        return new Promise((resolve) => {
          localStorage.removeItem('token');
          resolve();
        });
      },
      async fetchUserProfile() {
        try {
          const token = localStorage.getItem('token');
          console.log("token", token);

          const requestOptions = {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${token}`,
            },
          };

          const apiUrl = `http://localhost:3001/user/specific?token=${token}`;

          const response = await fetch(apiUrl, requestOptions);
          const data = await response.json();

          console.log(token, "token");

          if (data && data.data) {
            this.name = data.data.name;
            this.email = data.data.email;
            this.phone = data.data.phone;
          }
        } catch (error) {
          console.error('프로필을 가져오는 데 오류가 발생하였습니다:', error);
        }
      },
      async changeProfile() {
        try {
          const token = localStorage.getItem('token');
          const dataToUpdate = {
            "email": this.email,
            "name": this.name,
            "phone": this.phone,
            "password": this.password
          };
  
          if (this.showUpdatePassword) {
            dataToUpdate.password = this.newPassword;
          }
  
          const response = await fetch('/user/modify', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(dataToUpdate)
          });
  
          const data = await response.json();
          if (data && data.ok === 'ok') {
            alert('프로필이 성공적으로 업데이트되었습니다.');
            this.isEditing = false;
            this.errorMessage = '';
          } else {
            this.errorMessage = '프로필 업데이트 중 오류가 발생하였습니다.';
          }
        } catch (error) {
          console.error('프로필 업데이트 중 오류가 발생하였습니다:', error);
          this.errorMessage = '프로필 업데이트 중 오류가 발생하였습니다.';
        }
      },
      checkChangePassword() {
        this.changeProfile();
      },
      updatePassword() {
        this.showUpdatePassword = true;
      },
      toggleEditing() {
        this.isEditing = !this.isEditing;
      },
      showDeleteConfirmation() {
        this.showDeleteDialog = true;
      },
      confirmDelete() {
        if (this.deletePassword === this.password) {
          this.deleteUser().then(() => {
            this.logout().then(() => {
              alert('회원 탈퇴가 완료되었습니다.');
              this.$router.push('/login');
            }).catch(error => {
              console.error('Logout failed', error);
            });
          });
        } else {
          alert('비밀번호가 일치하지 않습니다.');
        }
      },
      cancelDelete() {
        this.showDeleteDialog = false;
      },
      async deleteUser() {
        try {
          const token = localStorage.getItem('token');
          const response = await fetch('localhost:3001/user/delete', {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
              email: this.email
            })
          });
          const data = await response.json();
          if (data && data.ok === 'ok') {
            alert('회원 탈퇴가 완료되었습니다.');
            this.router.push('/login');
          } else {
            this.errorMessage = '회원 탈퇴 중 오류가 발생하였습니다.';
          }
        } catch (error) {
          console.error('회원 탈퇴 중 오류가 발생하였습니다:', error);
          this.errorMessage = '회원 탈퇴 중 오류가 발생하였습니다.';
        }
      },
    },
    created() {
      this.fetchUserProfile();
    }
  }
  </script>
  