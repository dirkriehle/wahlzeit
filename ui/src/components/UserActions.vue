<template>
  <div class="d-grid gap-2">
    <template v-if="own()">
      <EditUser btn-class="btn btn-secondary" />
      <button class="btn btn-danger" @click="delet">Change Password</button>
    </template>
    <template v-else></template>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import EditUser from "@/components/modals/EditUser.vue";

@Options({
  components: { EditUser },
  props: { photo: 0 }
})
export default class UserActions extends Vue {
  own() {
    return true;
  }

  edit() {
    console.log("edit");
  }

  delet() {
    console.log("delete");
  }

  select() {
    console.log("select");
  }
}
</script>

<style scoped></style>
