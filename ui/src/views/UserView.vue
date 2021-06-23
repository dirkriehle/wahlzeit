<template>
  <h1>{{ user?.name }}</h1>
  <UserInfo :user="user" />

  <div class="container border border-3">
    <template v-for="photo in photos" :key="photo.id">
      <PhotoSummary :api="api" :photo="photo" />
    </template>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import UserInfo from "@/components/UserInfo.vue";
import PhotoSummary from "@/components/PhotoSummary.vue";
import { ApiThing, Photo, User } from "@/ApiThing";

@Options({
  components: { UserInfo, PhotoSummary },
  props: {
    id: "",
    api: ApiThing
  }
})
export default class UserView extends Vue {
  id = "";
  api: ApiThing | null = null;
  user: User | null = null;
  photos: Photo[] | null = null;

  async mounted() {
    await this.api
      ?.getUser(parseInt(this.id))
      .then(user => {
        this.user = user;
        return this.api?.listPhotos(user.id);
      })
      .then(photos => {
        this.photos = photos;
      });
  }
}
</script>

<style scoped></style>
