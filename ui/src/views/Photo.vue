<template>
  <div class="row">
    <div class="col-md-10 border border-3 rounded-3">
      <img class="img-fluid" :src="src" />
    </div>
    <div class="col-md-2 border border-3 rounded-3">
      <Praise :photo="id" :api="api" />
    </div>
  </div>
  <div class="row">
    <div class="col-md-10 border border-3 rounded-3">
      <Description />
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

  async mounted() {
    const image = await this.api?.getPhoto(this.id).then(photo => {
      return `http://localhost:8080${photo.path}`;
    });
    this.src = image ? image : "error";
  }
}
</script>

<style scoped></style>
