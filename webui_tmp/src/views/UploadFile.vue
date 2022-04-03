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
        this.$message.warning(` Current restrictions on choice  3  File. ${ files.length } ${ fileList.length }`);
      },
      beforeRemove(file, fileList) {
        console.log(file, fileList);
        return this.$confirm(` Confirm removal  ${ file.name }？`);
      },
      handleChange(param) {
        const data = new FormData();
      
        let fileName = param.file.name;
        let file = param.file;
        data.append('file', file);
        data.append('uid', '0403');
        data.append('fileName', fileName);
        console.log(file)
        console.log(data)
        // axios.post(process.env + '/upload_file', data, {
        axios.post('http://gproto.cn:8080/gproto/v1/file/uploadProto', data, {
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