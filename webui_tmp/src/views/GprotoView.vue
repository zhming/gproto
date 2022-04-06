<template>
  <el-container>
    <!-- 顶栏 -->
    <el-header height="20px">
      <!-- <h2>Google Protobuf Convert Tool</h2> -->
    </el-header>
    <!-- 嵌套容器 -->
    <el-container>
      <!-- 侧边导航菜单 -->
      <el-aside width="150px">
        <el-tree class="el-aside" :data="jsonTreedata" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
      </el-aside>
      <!-- 内容 -->
      <el-main >
        <!-- 第一列栅格布局 -->
        <el-row>
          <el-col :span="10" class="col1">
            <el-form ref="form" :model="formJson" label-width="80px">
          <br>
          <h2>Json Data</h2>
          <el-input v-model="formJson.data" type="textarea" :rows="12">{{formJson.data}}</el-input>
            </el-form>
          </el-col>
          <el-col :span="4" class="col2">
            <el-row>
              &nbsp;
            </el-row>
            <el-row>
            <el-button type="primary" @click="getJsonTree('com.saic.val.proto.CloudSceneSyncProxy_SOA20_SOA_0_2')">to protobuf data</el-button>
            </el-row>
            <el-row>
              &nbsp;
            </el-row>
            <el-row>
            <el-button >to json data</el-button>
            </el-row>
             <el-row>
              &nbsp;
            </el-row>
            <el-row>
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
  <div slot="tip" class="el-upload__tip"> Can only upload proto file , And no more than 1M</div>
</el-upload>
            </el-row>
          </el-col>
          <el-col :span="10" class="col3">
              <el-form ref="form" :model="formProto" label-width="80px">
          <br>
          <h2>Protobuf Data</h2>
            <el-radio-group v-model="formProto.encode">
              <el-radio label="Base64" ></el-radio>
              <el-radio label="Bytes"></el-radio>
              <el-radio label="HEX16"> </el-radio>
            </el-radio-group>
          <el-input v-model="formProto.data" type="textarea" :rows="12"></el-input>
            </el-form>
          </el-col>
        </el-row>
        <!-- 第二列布局 -->
        <!-- <el-row>
          <el-col :span="24" class="col4"></el-col>
        </el-row>-->
      </el-main> 
    </el-container>
    <!-- 底栏 -->
    <el-footer height="30px">&copy;gproto.cn  📧gproto@163.com</el-footer>
  </el-container>
</template>

<script>
import {mapGetters} from 'vuex'
import axios from 'axios'

export default {
  name: "GprotoTool",
  components: {
  },
  props: {},
  data() {
    return {
      fileList: [],
      formJson: {
        data: ''
      },
      formProto: {
        data: ''
      },
      jsonTreedata: [],
      protoInfo: {
        packageName: 'com.saic.val.proto',
        outerClassName: 'CloudSceneSyncProxyEnum_SOA20_SOA_0_3'
      },
      defaultProps: {
        children: "children",
        label: "label",
      },
    }
  },
  methods: {
    onSubmit() {
      console.log('submit!');
    },
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
        axios.post('http://127.0.0.1:8090/gproto/v1/file/uploadProto', data, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        }).then(res => {
          console.log(res);
          console.log(res.data.code)
          console.log(res.data.data)
        }).catch(err => {
          console.log(err);
        });
      },
      handleNodeClick(data, e) {
        console.log(data.label)
        console.log(e.parent.data.label)

        if(e.parent.data.label == undefined){
          console.log("根节点")
          const dataJsonReq = {
            // "className": this.protoInfo.packageName + "." + this.protoInfo.outerClassName + "$" + data.label
            "className": "com.saic.val.proto.CloudSceneSyncProxy_SOA20_SOA_0_2" + "$" + data.label
          };
          axios.post('http://127.0.0.1:8090/gproto/v1/getDefaultJson', dataJsonReq
          ).then(res => {
            console.log(res);
            let resData = res.data;
            this.formJson.data = JSON.stringify(resData, null, 4);
          }).catch(err => {
            console.log(err);
          });  

        }else{
          let dataFieldJsonReq;
          if(e.parent.parent.data.label == undefined){
             dataFieldJsonReq = {
                        "className": "com.saic.val.proto.CloudSceneSyncProxy_SOA20_SOA_0_2" + "$" + e.parent.data.label,
                        "fieldName": e.data.label
                      };
          }else{
            if(e.parent.parent.parent.parent != undefined){
              console.log("只能处理到前三级属性")
              this.formJson.data = "超出限制： 只能处理到前三级属性";
              return;
            }
            dataFieldJsonReq = {
                    "className": "com.saic.val.proto.CloudSceneSyncProxy_SOA20_SOA_0_2" + "$" + e.parent.parent.data.label,
                    "fieldName": e.parent.data.label,
                    "subFieldName": data.label
                  };
          }
          

          console.log(dataFieldJsonReq);
          axios.post('http://127.0.0.1:8090/gproto/v1/getFieldDefaultJson', dataFieldJsonReq
          ).then(res => {
            console.log(res);
            let resData = res.data;
            console.log(resData);
            this.formJson.data = JSON.stringify(resData, null, 4);
          }).catch(err => {
            console.log(err);
          });
        }

        
      },
      getJsonTree(messageName) {
        const data = {
          "className": messageName
        };
        // let className = "com.saic.val.proto.CloudSceneSyncProxyEnum_SOA20_SOA_0_3$CloudSceneSyncProxyEnumInfo";
        // data.append('className', className);
        console.log(data)
        // axios.post(process.env + '/upload_file', data, {
        axios.post('http://127.0.0.1:8090/gproto/v1/getJsonTree', data
        // {
        //   headers: {
        //     'Content-Type': 'application/json',
        //   },
        // }
        ).then(res => {
          console.log(res);
          let resData = res.data;
          console.log(resData)

          this.jsonTreedata = resData;
        }).catch(err => {
          console.log(err);
        });
      }
  }
  ,
  mounted() {

  }
  ,
  computed: {
    ...
        mapGetters([]),
    getComponentName() {
      return "ElementuiTest"
    }
  }
}

</script>


<style>
.el-header {
  background-color: #409EFF;
  color: white;
  height: 5%;
}
.el-footer {
  background-color: white;
  color: black;
  text-align: center;
}
.el-aside {
  background-color: rgb(42, 190, 183);
}
.el-main {
  background-color: white;
}
.el-col {
  height: 400px;
}
.col1 {
  background-color: teal;
}
.col2 {
  background-color: whitesmoke;
}
.col3 {
  background-color: rgb(247, 233, 247);
}

.col4 {
  background-color: rgb(15, 168, 155);
}


</style>
