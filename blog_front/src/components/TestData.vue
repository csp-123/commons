<script>
import axios from "axios"
import querystring from "querystring"


export default {
  name: "request_component",
  data() {
    return {
      msg:""
    }
  },
  mounted() {
    // // get请求方式
    // axios({
    //   method:"get",
    //   url:"https://limestart.cn/"
    // }).then(res =>{
    //   console.log(res.data);
    //   this.msg=res.data
    // })
    // post请求参数
    axios({
      method: "post",
      url: "http://127.0.0.1:8080/blog/web/article/page",
      data: {
        "pageNo": 1,
        "pageSize": 10
      }
    }).then(res => {
      console.log(res.data);
      this.msg = 'yyy'
    })
  },
}

</script>

<template>
  <div class="greetings">
    <h1 class="green">{{ msg }}</h1>
    <h3>
      You’ve successfully created a project with
      <a href="https://vitejs.dev/" target="_blank" rel="noopener">Vite</a> +
      <a href="https://vuejs.org/" target="_blank" rel="noopener">Vue 3</a>.
    </h3>
  </div>
</template>

<style scoped>
h1 {
  font-weight: 500;
  font-size: 2.6rem;
  position: relative;
  top: -10px;
}

h3 {
  font-size: 1.2rem;
}

.greetings h1,
.greetings h3 {
  text-align: center;
}

@media (min-width: 1024px) {
  .greetings h1,
  .greetings h3 {
    text-align: left;
  }
}
</style>
