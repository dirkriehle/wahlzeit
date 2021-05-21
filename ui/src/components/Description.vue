<template>
  By: username<br />
  Tags: tag1, tag2
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";

@Options({
  props: {}
})
export default class Description extends Vue {}
</script>

<style scoped></style>
