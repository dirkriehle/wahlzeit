<template>
  <h1>View Photo {{ id }}</h1>
</template>

<script>
import { Options, Vue } from "vue-class-component";

@Options({
  components: {},
  props: { id: "" }
})
export default class Photo extends Vue {}
</script>

<style scoped></style>
