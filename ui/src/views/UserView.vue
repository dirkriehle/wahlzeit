<template>
  <h1>{{ user?.name }}</h1>
  <UserInfo :user="user" />

  <div class="container border border-3">
    <template v-for="photo in photos" :key="photo.id">
      <PhotoSummary :photo="photo" />
    </template>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import UserInfo from "@/components/UserInfo.vue";
import PhotoSummary from "@/components/PhotoSummary.vue";
import { wahlzeitApi, Photo, User } from "@/WahlzeitApi";

@Options({
  components: { UserInfo, PhotoSummary },
  props: { id: "" }
})
export default class UserView extends Vue {
  id = "";
  user: User | null = null;
  photos: Photo[] | null = null;

  async mounted() {
    this.user = await wahlzeitApi.getUser(parseInt(this.id));
    this.photos = await wahlzeitApi.listPhotos(this.user.id);
  }
}
</script>

<style scoped></style>
