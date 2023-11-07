<template>
    <div class="forgot-container">
        <h1>아이디/비밀번호 찾기</h1>
        <div class="tabs">
            <button @click="showID" :class="{active: isID}">아이디 찾기</button>
            <button @click="showPassword" :class="{active: isPassword}">비밀번호 찾기</button>
        </div>
        
        <form v-if="isID" @submit.prevent="submitID">
            <!-- Name input -->
            <div class="input-group">
                <label for="name">이름</label>
                <input type="text" id="name" v-model="name" placeholder="이름 입력" required>
            </div>
            
            <!-- Phone input -->
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
        
        <form v-if="isPassword" @submit.prevent="submitPassword">
            <!-- Email input -->
            <div class="input-group">
                <label for="email">아이디</label>
                <input type="email" id="email" v-model="email" placeholder="아이디 입력" required>
            </div>
            
            <!-- Name input -->
            <div class="input-group">
                <label for="name">이름</label>
                <input type="text" id="name" v-model="name" placeholder="이름 입력" required>
            </div>
            
            <!-- Phone input -->
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
    </div>
</template>


<script>
import './ForgotPassword.css'
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
            phone3: ''
        };
    },
    methods: {
        validateNumberInput(event) {
            //전화번호 칸에는 숫자만 가능하게 
            event.target.value = event.target.value.replace(/[^0-9]/g, '');
        },
        showID() {
            this.isID = true;
            this.isPassword = false;
        },
        showPassword() {
            this.isPassword = true;
            this.isID = false;
        },
        submitID() {
            // 아이디 찾기 처리 로직
        },
        submitPassword() {
            // 비밀번호 찾기 처리 로직
        },
        cancel() {
            this.$router.go(-1);  // 이전 페이지로 돌아감
        }
    }
}
</script>