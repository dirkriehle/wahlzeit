<template>
  <h1>Username</h1>
  <UserInfo />

  <div class="container border border-3">
    <PhotoSummary />
    <PhotoSummary />
    <PhotoSummary />
  </div>
</template>

<script>
import { Options, Vue } from "vue-class-component";
import UserInfo from "@/components/UserInfo.vue";
import PhotoSummary from "@/components/PhotoSummary.vue";

@Options({
  components: { UserInfo, PhotoSummary },
  props: { id: "" }
})
export default class User extends Vue {}
</script>

<style scoped></style>
