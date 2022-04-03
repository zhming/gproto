<template>
<el-upload
  class="upload-demo"
  action="#"
  :http-request="handleChange"
  :on-preview="handlePreview"
  :on-remove="handleRemove"
  :before-remove="beforeRemove"
  :on-exceed="handleExceed"
  :file-list="fileList"
    :limit="3"
  >
  <el-button size="small" type="primary"> Click upload </el-button>
  <div slot="tip" class="el-upload__tip"> Can only upload zip file , And no more than 100M</div>
</el-upload>
</template>
<script>
import axios from 'axios'
  export default {
    data() {
      return {
        fileList: []
      };
    },
    methods: {
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      handleExceed(files, fileList) {
        this.$message.warning(` Current restrictions on choice  3  File , This time I chose  ${files.length}  File , A total of  ${files.length + fileList.length}  File `);
      },
      beforeRemove(file, fileList) {
        console.log(file, fileList);
        return this.$confirm(` Confirm removal  ${ file.name }？`);
      },
      handleChange(param) {
        const data = new FormData();
        data.append('file', param.file.name);
        console.log(data.file)
        console.log(param.file.name)
        axios.post(process.env + '/upload_file', data, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        }).then(res => {
          console.log(res);
        }).catch(err => {
          console.log(err);
        });
      }
    }
  }
</script>