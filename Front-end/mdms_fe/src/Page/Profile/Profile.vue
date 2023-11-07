<template>
    <div class="profile-container">
        <h1>회원정보</h1>
        <form>
            <!-- 이름 정보 -->
            <div class="input-group">
                <label for="name" class="kind">이름</label>
                <input type="text" id="name" placeholder="현재이름 출력됨"/>
            </div>

            <!-- 이메일 정보 -->
            <div class="input-group">
                <label for="email" class="kind">이메일</label>
                <label for="email">현재이메일 출력됨</label>
            </div>

            <!-- 비밀번호 수정 -->
            <div class="input-group">
                <label for="password" class="kind">비밀번호</label>
                <button @click.prevent="updatePassword">변경하기</button>
                <div v-if="showUpdatePassword">
                    <input type="text" placeholder="비밀번호 입력" />
                    <input type="text" placeholder="비밀번호 재입력" />
                    <button @click="checkChangePassword">확인하기</button>
                </div>
            </div>

            <!-- 전화번호 정보 -->
            <div class="input-group">
                <label for="Phone" class="kind">전화번호</label>
                <input type="text" id="phone" placeholder="현재 전화번호 출력될 것" />
            </div>

            <div >
                <button id="confirm" type="submit" @click="changeProfile">수정하기</button>
                <button id="withdraw" @click.prevent="withdrawMember">회원탈퇴</button>
            </div>
        </form>
    </div>
</template>


<script>
import './Profile.css'
export default {
    name: 'ProfilePage',
    data() {
        return{
            name: '',
            email: '',
            password: '',
            showUpdatePassword: false,
            phone: ''
        }
    },
    methods: {
        withdrawMember() {
            // 회원 탈퇴 처리
        },
        changeProfile() {
            // 프로필 변경 처리
        },
        checkChangePassword(){
            // 암호 변경 처리
        },
        updatePassword() {
            this.showUpdatePassword = true;
        }
    }
}
</script>
