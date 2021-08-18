<template>
  <h1>{{ user?.name }}</h1>
  <UserInfo :user="user" />

  <div class="container border border-3">
    <PhotoList :photos="photos" />
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import { Photo, photoApi } from '../api/PhotoApi';
import { User, userApi } from '../api/UserApi';
import PhotoList from '../components/PhotoList.vue';
import UserInfo from '../components/UserInfo.vue';

@Options({
  components: { UserInfo, PhotoList },
  props: { id: '' },
})
export default class UserView extends Vue {
  id = '';
  user?: User;
  photos: Photo[] = [];

  async mounted(): Promise<void> {
    this.user = await userApi.getUser(Number.parseInt(this.id, 10));
    this.photos = await photoApi.listPhotos(this.user.id);
  }
}
</script>

<style scoped></style>
