<template>
  By:
  <router-link :to="{ name: 'User', params: { id: user?.id } }">
    {{ user?.name }}
  </router-link>
  <br />
  Tags: {{ photo?.tags.join(', ') }}<br />
  Praise Score: {{ photo?.praise }}
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import { Photo } from '../api/PhotoApi';
import { User } from '../api/UserApi';

@Options({
  props: {
    user: undefined,
    photo: undefined,
  },
})
export default class Description extends Vue {
  user?: User;
  photo?: Photo;
}
</script>

<style scoped></style>
