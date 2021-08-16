<template>
  <div class="container border border-3">
    <div class="row">
      <div class="col-3">No Profile Photo</div>
      <div class="col-6">
        <div>Number of Photos: <b>n</b></div>
        <div>Account status: <b>state</b></div>
        <div>Member since: <b>date</b></div>
        <div>
          Email address: <b>{{ user?.email }}</b>
        </div>
      </div>
      <div class="col-3">
        <UserActions />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import UserActions from '../components/UserActions.vue';
import { User } from '../WahlzeitApi';

@Options({
  components: { UserActions },
  props: { user: undefined },
})
export default class UserInfo extends Vue {
  user?: User;
}
</script>

<style scoped></style>
