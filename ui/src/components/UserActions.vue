<template>
  <div class="d-grid gap-2">
    <template v-if="own()">
      <EditUser btn-class="btn btn-secondary" />
      <button class="btn btn-danger" @click="deleteUser">
        Change Password
      </button>
    </template>
    <template v-else></template>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import EditUser from '../components/modals/EditUser.vue';

@Options({
  components: { EditUser },
})
export default class UserActions extends Vue {
  own(): boolean {
    return true;
  }

  edit(): void {
    console.log('edit');
  }

  deleteUser(): void {
    console.log('delete');
  }

  select(): void {
    console.log('select');
  }
}
</script>

<style scoped></style>
