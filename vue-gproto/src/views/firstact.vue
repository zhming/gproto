<template>
  <div>
    <div>
      <h3>{{ message }}</h3>
    </div>

    <div>
      <table>
        <tr>
          <td >
            <div>
           <textarea v-model="protoData" placeholder="add multiple lines"></textarea>
            </div>
            
          </td>
          <td >
            <div>
              <textarea id="protobuf_tree"  class="main"></textarea>
            </div>
          </td>
          <td>
            <div>
              <textarea id="protobuf_json" class="rightBar"></textarea></div>
          </td>
        </tr>
      </table>
    </div>
    <!-- <li v-for="element in list" :key="element.id" class="square">
      {{ element.title }}
      <button style="margin-left:20px;" @click="click(element)">点击收藏</button>
    </li> -->
  <div>
    <tr>
         <td><button style="mergin-left:20px;" @click="click(protoData)">上传Protobuf</button></td>
     <td><button style="mergin-center:20px;" @click="click(2)">Protobuf转化JSON</button></td>
     <td><button style="mergin-right:20px;" @click="click(3)">JSON转Protobuf</button></td>
     <td><button style="mergin-right:20px;" @click="click(protoData)">重置</button></td>
    </tr>
  </div>
    
  </div>
</template>
<style>
.main {float: right;width: 300px; height: 400px; background-color: cyan}
.leftBar {float: left;width: 300px; height: 400px; background-color: rebeccapurple}
.rightBar {float: right;width: 300px;height: 400px; background-color: cornflowerblue}
</style>

<script>
import {getInformation} from "@/api/interactive";
import {collectPostLocal} from "../api/interactive";

export default {
  data() {
    return {
      list: {},
      message: "55555",
      protoData: "",
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      getInformation({
        page: 1,
        tab: "ask",
        limit: 20,
      }).then((res) => {
        // console.log(res,"res")
        this.list = res.data.data
        // console.log(this.list, "this.list")
      })
    },
    click(element) {
      console.log(element);
      collectPostLocal({
        // method: 'post',
        // url: '/saveProto',
        // data: {
        //   protoData: this.protoData
        // }
          // POST request using axios with async/await
      
        
        }
      ).then((res) => {
        this.message = res.data.result
        console.log(res, "res")
      })
    },
  },
}
</script>

<style scoped>

</style>