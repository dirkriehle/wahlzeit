<template>
  <div class="row">
    <div class="col-md-10 border border-3 rounded-3">
      <img class="img-fluid" :src="src" />
    </div>
    <div class="col-md-2 border border-3 rounded-3">
      <Praise :photo-id="id" :api="api" />
    </div>
  </div>
  <div class="row">
    <div class="col-md-10 border border-3 rounded-3">
      <Description :api="api" :photo="photo" />
    </div>
    <div class="col-md-2 border border-3 rounded-3">
      <PhotoActions :api="api" :photo="photo" />
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Praise from "@/components/Praise.vue";
import PhotoActions from "@/components/PhotoActions.vue";
import Description from "@/components/Description.vue";
import { ApiThing, Photo, User } from "@/ApiThing";

@Options({
  components: { Praise, PhotoActions, Description },
  props: { id: "", api: ApiThing }
})
export default class PhotoView extends Vue {
  id = "";
  api: ApiThing | null = null;
  photo: Photo | null = null;
  user: User | null = null;

  mounted() {
    this.api
      ?.getPhoto(this.id)
      .then(photo => {
        this.photo = photo;
        return photo.userId;
      })
      .then(userid => {
        if (this.api) return this.api.getUser(userid);
        throw "no api object";
      })
      .then(user => {
        this.user = user;
      });
  }

  get src() {
    return this.photo?.path;
  }
}
</script>

<style scoped></style>
