<template>
  By:
  <router-link :to="{ name: 'User', params: { id: user?.id } }">
    {{ user?.name }}
  </router-link>
  <br />
  Tags: {{ photo?.tags.join(", ") }}<br />
  Praise Score: {{ photo?.praise }}
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import { ApiThing, Photo, User } from "@/ApiThing";

@Options({
  props: {
    api: null,
    photo: null
  }
})
export default class Description extends Vue {
  api: ApiThing | null = null;
  user: User | null = null;
  photo: Photo | null = null;

  async updated() {
    if (!this.photo) {
      // TODO show some error on website
      console.error("Description: no photo given");
    } else {
      await this.api?.getUser(this.photo.userId).then(user => {
        this.user = user;
      });
    }
  }
}
</script>

<style scoped></style>
