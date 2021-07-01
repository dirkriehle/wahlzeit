<template>
  <h1>Results</h1>
  <div class="container border border-3">
    <PhotoList :photos="photos" />
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import PhotoList from "@/components/PhotoList.vue";
import { wahlzeitApi, Photo } from "@/WahlzeitApi";

@Options({
  components: { PhotoList },
  props: { tags: "" }
})
export default class PhotoListView extends Vue {
  tags = "";
  photos: Photo[] | null = null;

  async mounted() {
    this.photos = await wahlzeitApi.listPhotos(
      null,
      this.tags.split(RegExp(", ?"))
    );
  }
}
</script>

<style scoped></style>
