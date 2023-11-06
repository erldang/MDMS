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
                <button id="changePassword" @click.prevent="updatePassword">변경하기</button>
                <div v-if="showUpdatePassword">
                    <input style="margin-bottom='1px'" type="text" placeholder="변경시킬 비밀번호 입력" />
                    <input type="text" placeholder="비밀번호 재입력" />
                    <button @click.prevent="checkChangePassword">변경하기</button>
                </div>
            </div>

            <!-- 전화번호 정보 -->
            <div class="input-group">
                <label for="Phone" class="kind">전화번호</label>
                <input type="text" id="phone" placeholder="현재 전화번호 출력될 것" />
            </div>

            <div >
                <button id="confirm" type="submit" @click.prevent="checkChangeProfile">수정하기</button>
                <button id="withdraw" @click.prevent="withdrawMember">회원탈퇴</button>
            </div>
        </form>
    </div>
</template>


<script>
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
            // 회원 탈퇴 버튼
            if(confirm('회원탈퇴를 하시겠습니까?')) {
                // 수락버튼 처리
            } else {
                //취소버튼 처리
            }
        },
        checkChangeProfile() {
            // 회원정보 수정 버튼
            if(confirm('회원정보를 수정하시겠습니까?')) {
                // 수락버튼 처리
            } else {
                //취소버튼 처리
            }
        },
        checkChangePassword(){
            // 암호변경 버튼
            if(confirm('비밀번호를 변경 하시겠습니까?')) {
                // 수락버튼 처리
                this.showUpdatePassword = false;
            } else {
                //취소버튼 처리
            }
        },
        updatePassword() {
            this.showUpdatePassword = true;
        }
    }
}
</script>

<style scoped>
.profile-container {
    width: 100%;
    max-width: 400px;
    margin: 2rem auto;
    padding: 2rem;
    background-color: #fff;
    border: 1px solid #ddd;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    text-align: center;
}

.input-group {
    margin-bottom: 1.5rem;
    text-align: left;
}

.kind {
    display: inline-block;
    width: 5rem;
    margin-right: 1rem;
    font-weight: bold;
}

label {
    display: inline-block;
    font-weight: bold;
    margin-bottom: 0.5rem;
}

input[type="text"] {
    width: 70%;
    padding: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 4px;
}

button {
    width: 100%;
    height: 3rem;
    border: none;
    border-radius: 4px;
    background-color: #007BFF;
    color: #fff;
    cursor: pointer;
    transition: background-color 0.3s ease-in-out;
    font-weight: bold;
}

div>button{
    width: 40%;
    margin: 5%;
}

div>div>input{
    margin-left: 5px;
    margin: 2px;
}

button:hover {
    background-color: #0056b3;
}

#withdraw {
    background-color: #DC3545;
}

#withdraw:hover {
    background-color: #ba2533;
}

#changePassword{
    width: 25%;
    height: 2rem;
}
</style>
