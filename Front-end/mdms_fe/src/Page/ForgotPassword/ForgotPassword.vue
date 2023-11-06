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

<style scoped>
/* 공통 스타일을 register-container 클래스에 추가 */
.forgot-container {
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

.phone-inputs {
    display: flex;
    align-items: center;
}

.phone-inputs input {
    width: calc((100% - 2rem) / 3);
    margin-right: 0.5rem;
}

.phone-inputs span {
    margin-right: 0.5rem;
}

.phone-inputs input:last-child {
    margin-right: 0;
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
    width: 48%; 
    padding: 0.75rem 1.25rem; 
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

.tabs {
    display: flex;
    justify-content: space-between;
    margin-bottom: 1rem;
    gap: 1rem; 
}

button.active {
    background-color: #0056b3;
}

.action-buttons {
    display: flex;
    justify-content: space-between;
    margin-top: 1rem;
}

.error-message {
    color: red;
    font-size: 0.8rem;
    margin-top: -0.5rem;
    margin-bottom: 1rem;
}
</style>
