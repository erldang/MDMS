<template>
    <div>
      <h1>CSV 파일 업로드 및 데이터 등록</h1>
      <input type="file" @change="readCsvFile" />
      <table v-if="tableData.length">
        <thead>
          <tr>
            <th v-for="(header, index) in headers" :key="index">
              {{ header }}
              <input type="text" v-model="headers[index]" placeholder="속성명 변경" @change="updateHeader(index)" />
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, index) in tableData" :key="index">
            <td v-for="cell in row" :key="cell">{{ cell }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        headers: [],
        tableData: [],
        // 더미 데이터로 서버의 응답을 모방합니다.
        serverHeaders: [
          { id: 1, name: '사용자ID' },
          { id: 2, name: '이름' },
          { id: 3, name: '나이' },
          { id: 4, name: '이메일' },
          // 추가적인 더미 헤더 목록...
        ],
      };
    },
    methods: {
      readCsvFile(event) {
        const file = event.target.files[0];
        const reader = new FileReader();
        reader.onload = (e) => {
          const data = e.target.result;
          const parsedData = this.parseCsvData(data);
          this.tableData = parsedData.data;
          this.headers = parsedData.headers;
        };
        reader.readAsText(file);
      },
      parseCsvData(data) {
        const rows = data.split('\n');
        const headers = rows[0].split('|').map((header) => header.trim());
        const tableData = rows.slice(1).map((row) => row.split('|').map((cell) => cell.trim()));
        return { headers, data: tableData };
      },
      updateHeader(index) {
        // 입력된 속성명으로 더미 데이터에서 검색합니다.
        let newHeader = this.serverHeaders.find(header => header.name === this.headers[index]);
        if(newHeader) {
          this.headers.splice(index, 1, newHeader.name);
        } else {
          // 일치하는 항목이 없을 경우 경고 메시지를 표시합니다.
          alert('해당 속성명이 목록에 없습니다.');
        }
      }
    }
  };
  </script>
  
  <style>
  /* 여기에 CSS 스타일을 추가할 수 있습니다. */
  </style>
  