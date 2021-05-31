<template>
  By: {{ username }}<br />
  Tags: {{ tags?.join(", ") }}<br />
  Praise Score: {{ praise }}
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";

@Options({
  props: { username: "", tags: Array, praise: 0 }
})
export default class Description extends Vue {}
</script>

<style scoped></style>
