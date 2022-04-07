<template>
  <el-container>
    <!-- 顶栏 -->
    <el-header height="20px">
      <!-- <h2>Google Protobuf Convert Tool</h2> -->
    </el-header>
    <!-- 嵌套容器 -->
    <el-container>
      <!-- 侧边导航菜单 -->
      <el-aside width="180px">
        <h3>JSONTREE</h3>
        <el-tree
          class="el-aside"
          :data="jsonTreedata"
          :props="defaultProps"
          @node-click="handleNodeClick"
        ></el-tree>
      </el-aside>
      <!-- 内容 -->
      <el-main>
        <!-- 第一列栅格布局 -->
        <el-row>
          <el-col :span="12" class="col1">
             <h2>Json Data</h2>
            <el-form ref="form" :model="formJson" label-width="80px">
              <el-input v-model="formJson.data" type="textarea" :rows="12">{{
                formJson.data
              }}</el-input>
            </el-form>
          </el-col>
          <el-col :span="4" class="col2">
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
                :limit="1"
              >
                <el-button size="small" type="primary">
                  CLICK UPLOAD PROTO
                </el-button>
                <div slot="tip" class="el-upload__tip">
                  Can only upload proto file , And no more than 1M
                </div>
                <div slot="tip" class="el-upload__tip">
                  className: {{ currentProtoInfo.outerClassName }} packageName:
                  {{ currentProtoInfo.packageName }}
                </div>
              </el-upload>
            </el-row>
            <el-row> &nbsp; </el-row>
            <el-row>
              <el-button
                type="primary"
                @click="getJsonTree(currentProtoInfo.fullClassName)"
                >GET JSON TREE</el-button
              >
            </el-row>
            <el-row> &nbsp; </el-row>
            <el-row>
              <el-button type="primary" disabled>JSON TO PROTO</el-button>
            </el-row>
            <el-row> &nbsp; </el-row>
            <el-row>
              <el-button type="primary" disabled>PROTO TO JSON</el-button>
            </el-row>
            <el-row> &nbsp; </el-row>
          </el-col>
          <el-col :span="12" class="col3">
            <el-form ref="form" :model="formProto" label-width="80px">
              <br />
              <h2>Protobuf Data</h2>
              <el-radio-group v-model="formProto.encode">
                <el-radio label="Base64"></el-radio>
                <el-radio label="Bytes"></el-radio>
                <el-radio label="HEX16"> </el-radio>
              </el-radio-group>
              <el-input
                v-model="formProto.data"
                type="textarea"
                :rows="12"
              ></el-input>
            </el-form>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
    <!-- 底栏 -->
    <el-footer height="30px">
      <div>&copy;gproto.cn 📧gproto@163.com</div>
      <div>
        <p class="footer-copyright">Copyright © 2022-  All rights
                reserved. &nbsp;&nbsp;&nbsp;&nbsp;<a rel="nofollow" href="https://beian.miit.gov.cn/" style="color: #CCC;" target="_blank">辽ICP备2022001085号-1</a>&nbsp;&nbsp;&nbsp;&nbsp;</p>
        </div>
      
      </el-footer>
  </el-container>
</template>

<script>
import { mapGetters } from "vuex";
import gportoApi from "@/utils/request";

export default {
  name: "GprotoTool",
  components: {},
  props: {},
  data() {
    return {
      fileList: [],
      formJson: {
        data: "",
      },
      formProto: {
        data: "",
      },
      jsonTreedata: [],
      protoInfoList: [],
      currentProtoInfo: {
        packageName: "",
        outerClassName: "",
        fullClassName: "",
      },
      defaultProps: {
        children: "children",
        label: "label",
      },
    };
  },
  methods: {
    onSubmit() {
      console.log("submit!");
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleExceed(files, fileList) {
      this.$message.warning(
        ` Current restrictions on choice  1  File. ${files.length} ${fileList.length}`
      );
    },
    beforeRemove(file, fileList) {
      console.log(file, fileList);
      return this.$confirm(` Confirm removal  ${file.name}？`);
    },
    handleChange(param) {
      const data = new FormData();

      let fileName = param.file.name;
      let file = param.file;
      data.append("file", file);
      data.append("uid", "0403");
      data.append("fileName", fileName);
      uploadProto(data)
        .then((res) => {
          console.log(res);
          this.currentProtoInfo.packageName = res.data.data.packageName;
          this.currentProtoInfo.outerClassName = res.data.data.className;
          this.currentProtoInfo.fullClassName =
            res.data.data.packageName + "." + res.data.data.className;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    handleNodeClick(data, e) {
      console.log(data.label);
      console.log(e.parent.data.label);

      if (e.parent.data.label == undefined) {
        console.log("根节点");
        const dataJsonReq = {
          className: this.currentProtoInfo.fullClassName + "$" + data.label,
        };
        getDefaultJson(dataJsonReq)
          .then((res) => {
            console.log(res);
            let resData = res.data;
            this.formJson.data = JSON.stringify(resData, null, 4);
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        let dataFieldJsonReq;
        if (e.parent.parent.data.label == undefined) {
          dataFieldJsonReq = {
            className:
              this.currentProtoInfo.fullClassName + "$" + e.parent.data.label,
            fieldName: e.data.label,
          };
        } else {
          if (e.parent.parent.parent.parent != undefined) {
            console.log("只能处理到前三级属性");
            this.formJson.data = "超出限制： 只能处理到前三级属性";
            return;
          }
          dataFieldJsonReq = {
            className:
              this.currentProtoInfo.fullClassName +
              "$" +
              e.parent.parent.data.label,
            fieldName: e.parent.data.label,
            subFieldName: data.label,
          };
        }

        console.log(dataFieldJsonReq);
        getFieldDefaultJson(dataFieldJsonReq)
          .then((res) => {
            console.log(res);
            let resData = res.data;
            console.log(resData);
            this.formJson.data = JSON.stringify(resData, null, 4);
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    handleRadioChanges() {
      console.log(1111);
    },
    getJsonTree(messageName) {
      if (messageName == "") {
        console.warn("no class info");
        return;
      }
      const data = {
        className: messageName,
      };
      console.log(data);
      getJsonTree(data)
        .then((res) => {
          console.log(res);
          let resData = res.data;
          console.log(resData);
          this.jsonTreedata = resData;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  mounted() {},
  computed: {
    ...mapGetters([]),
    getComponentName() {
      return "ElementuiTest";
    },
  },
};

export function getJsonTree(data) {
  return gportoApi({
    url: "/gproto/v1/getJsonTree",
    method: "post",
    data: data,
  });
}

export function getFieldDefaultJson(data) {
  return gportoApi({
    url: "/gproto/v1/getFieldDefaultJson",
    method: "post",
    data: data,
  });
}

export function getDefaultJson(data) {
  return gportoApi({
    url: "/gproto/v1/getDefaultJson",
    method: "post",
    data: data,
  });
}

export function uploadProto(data) {
  return gportoApi({
    url: "/gproto/v1/file/uploadProto",
    method: "post",
    data: data,
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}
</script>


<style>
.el-header {
  background-color: #409eff;
  color: white;
  height: 5%;
}
.el-footer {
  background-color: white;
  color: black;
  text-align: center;
}
.el-aside {
  background-color: rgb(239, 239, 239);
}
.el-main {
  background-color: white;
}
.el-col {
  height: 400px;
}
.col1 {
  background-color: rgb(239, 239, 239);
}
.col2 {
  background-color: whitesmoke;
}
.col3 {
  background-color: rgb(239, 239, 239);
}

.col4 {
  background-color: rgb(239, 239, 239);
}
</style>
