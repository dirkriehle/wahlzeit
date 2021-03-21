<template>
  <div class="home">
    <h1>Hello World</h1>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";

@Options({
  components: {},
  props: {
    auth: ""
  }
})
export default class Home extends Vue {}
</script>
