<template>
    <div class="data-column-container">
      <div class="data-column" v-for="column in columns" :key="column.id">
        <div class="column-type">
          <select v-model="column.type">
            <option value="normal">일반 칼럼</option>
            <option value="key">키 칼럼</option>
          </select>
        </div>
        <div class="data-type">{{ column.dataType }}</div>
        <div class="attribute-name">
          <input type="text" v-model="column.name" placeholder="속성 이름(표준)" />
        </div>

        <div class="real-data-list">
        <div v-for="data in column.dataList.split(', ')" :key="data">
    {{ data }}
  </div>
  
</div>
<button class="remove-button" @click="removeColumn(column.id)">제거</button>
      </div>
      
    </div>
  </template>
  
  <script>
  export default {
    methods: {
        removeColumn(columnId) {
            // Logic to remove the column from the columns array
            this.columns = this.columns.filter(column => column.id !== columnId);
        }
    },
    data() {
      return {
        columns: [
          {
            id: 1,
            type: 'normal',
            dataType: 'VARCHAR(255)',
            name: '이름',
            code: '[코드]',
            dataList: '홍길동, 장보고, 이순신',
          }
        ],
      };
    },
  };
  </script>
  
  <style scoped>
  .data-column-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 1rem;
    padding: 0.1rem;
  }

  .data-column {
    border: 2px solid #4CAF50;
    border-radius: 15px;
    padding: 20px;
    margin: 10px 0;
    width: 100%;
  }

  .column-type select {
    width: 200px; /* 입력 칸 너비를 200px로 설정 */
    padding: 10px;
    margin-bottom: 10px;
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 5px;
  }

  .attribute-name input {
    width: 200px; /* 입력 칸 너비를 200px로 설정 */
    padding: 10px;
    margin-bottom: 10px;
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 5px;
  }

  .real-data-list {
    background-color: #f7f7f7;
    padding: 10px;
    border-radius: 8px;
    overflow-y: auto;
    max-height: 200px;
    margin-bottom: 10px;
  }

  .real-data-list > div {
    display: table;
    width: 100%;
  }

  .real-data-list div:not(:last-child) {
    border-bottom: 1px solid #ccc; /* Add a border to all but the last item */
  }

  .real-data-list div > div {
    display: table-row;
  }

  .real-data-list div > div > div {
    display: table-cell;
    padding: 10px; /* Add some padding inside each 'cell' */
    text-align: left;
  }
</style>
