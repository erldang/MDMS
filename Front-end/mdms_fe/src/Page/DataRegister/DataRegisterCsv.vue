<template>
    <div>
      <h1>CSV 파일 업로드 테스트</h1>
      <input type="file" @change="readCsvFile" />
      <table v-if="tableData.length">
        <thead>
          <tr>
            <th v-for="(header, index) in headers" :key="index">
              {{ header }}
              <input type="text" v-model="headers[index]" placeholder="속성명 변경" @input="suggestHeader($event, index)" />
              <ul v-if="suggestedHeaders[index] && suggestedHeaders[index].length">
                <li v-for="(suggestedHeader, keyIndex) in suggestedHeaders[index]" :key="`suggested-${index}-${keyIndex}`" @click="selectHeader(index, suggestedHeader)">
                {{ suggestedHeader }}
                </li>

              </ul>
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
        serverHeaders: [
          { id: 1, name: '사용자ID' },
          { id: 2, name: '이름' },
          { id: 3, name: '나이' },
          { id: 4, name: '이메일' },
        ],
        suggestedHeaders: [],
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
      suggestHeader(event, index) {
        const query = event.target.value;
        this.suggestedHeaders[index] = this.serverHeaders
          .filter(header => header.name.toLowerCase().includes(query.toLowerCase()))
          .map(header => header.name);
      },
      selectHeader(index, headerName) {
        this.headers[index] = headerName;
        this.suggestedHeaders[index] = [];
      },
      updateHeader(index) {
        let newHeader = this.serverHeaders.find(header => header.name === this.headers[index]);
        if(newHeader) {
          this.headers.splice(index, 1, newHeader.name);
        } else {
            console.log("error");
        }
      }
    }
  };
  </script>
  
  <style>
    /* 스타일을 추가하거나 수정할 수 있습니다 */
    ul {
      list-style-type: none;
      padding: 0;
    }
    li {
      cursor: pointer;
      background-color: #f0f0f0;
      padding: 2px 4px;
      margin-bottom: 2px;
    }
    li:hover {
      background-color: #e0e0e0;
    }
  </style>
  