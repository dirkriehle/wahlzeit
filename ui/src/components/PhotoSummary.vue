<template>
  <div class="row border border-3">
    <div class="col-4">Thumbnail</div>
    <div class="col-5">
      <div>praise: <b>f</b></div>
      <div>status: <b>status</b></div>
      <div>date: <b>date</b></div>
      <div>tags: <b>tag1, tag2</b></div>
      <div>link: <b>url</b></div>
    </div>
    <div class="col-3">
      <PhotoActions />
    </div>
  </div>
</template>

<script>
import { Options, Vue } from "vue-class-component";
import PhotoActions from "@/components/PhotoActions.vue";

@Options({
  components: { PhotoActions },
  props: { id: "" }
})
export default class PhotoSummary extends Vue {}
</script>

<style scoped></style>
