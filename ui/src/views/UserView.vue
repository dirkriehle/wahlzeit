<template>
  <h1>{{ user?.name }}</h1>
  <UserInfo :user="user" />

  <div class="container border border-3">
    <PhotoList :photos="photos" />
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import UserInfo from "@/components/UserInfo.vue";
import PhotoList from "@/components/PhotoList.vue";
import { wahlzeitApi, Photo, User } from "@/WahlzeitApi";

@Options({
  components: { UserInfo, PhotoList },
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
