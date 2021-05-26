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
      <Description :username="username" :tags="tags" :praise="praise" />
    </div>
    <div class="col-md-2 border border-3 rounded-3">
      <PhotoActions :photo="id" :api="api" />
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Praise from "@/components/Praise.vue";
import PhotoActions from "@/components/PhotoActions.vue";
import Description from "@/components/Description.vue";
import { ApiThing } from "@/ApiThing";

@Options({
  components: { Praise, PhotoActions, Description },
  props: { id: "", api: ApiThing }
})
export default class Photo extends Vue {
  src = "";
  id = "";
  api: ApiThing | null = null;
  praise = 0;
  username = "";
  tags: string[] = [];

  mounted() {
    this.api
      ?.getPhoto(this.id)
      .then(photo => {
        this.src = `http://localhost:8080${photo.path}`;
        this.praise = photo.praise;
        this.tags = photo.tags;
        return photo.userId;
      })
      .then(userid => {
        // TODO get username
      });
  }
}
</script>

<style scoped></style>
