<template>
  <div class="container border border-3">
    <div class="row">
      <div class="col-3">
        Profile Photo
      </div>
      <div class="col-6">
        <div>Number of Photos: <b>n</b></div>
        <div>Account status: <b>state</b></div>
        <div>Member since: <b>date</b></div>
        <div>Email address: <b>mail</b></div>
      </div>
      <div class="col-3">
        <UserActions />
      </div>
    </div>
  </div>
</template>

<script>
import { Options, Vue } from "vue-class-component";
import UserActions from "@/components/UserActions.vue";

@Options({
  components: { UserActions },
  props: { id: "" }
})
export default class User extends Vue {}
</script>

<style scoped></style>
