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
  tags: string[] | string = [];
  photos: Photo[] | null = null;

  async mounted() {
    let tags = this.tags;
    if (!Array.isArray(tags)) {
      tags = [tags];
    }
    console.log(tags);
    this.photos = await wahlzeitApi.listPhotos(null, tags);
  }
}
</script>

<style scoped></style>
