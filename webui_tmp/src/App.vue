<template>
  <div id="app">
    <!-- <h1>Google Protobuf Tool</h1> -->
    <img alt="Vue logo" src="./assets/logo.png">
    <nav>
      <router-link to="/">Home</router-link>
      <router-link to="/about">About</router-link>
      <router-link to="/elementuiTest">Element UI 测试页</router-link>
      <router-link to="/uploadTest">Upload 测试页</router-link>
      <router-link to="/gprotoView">Gproto</router-link>
      <router-link to="/treeView">TreeView</router-link>
    </nav>
    <router-view/>
  </div>
</template>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;
    padding: 20px;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
