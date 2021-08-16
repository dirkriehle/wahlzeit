<template>
  <h1>Results</h1>
  <div class="container border border-3">
    <PhotoList :photos="photos" />
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import PhotoList from '../components/PhotoList.vue';
import { Photo, wahlzeitApi } from '../WahlzeitApi';

@Options({
  components: { PhotoList },
  props: { tags: '' },
})
export default class PhotoListView extends Vue {
  tags: string[] | string = [];
  photos: Photo[] = [];

  async mounted(): Promise<void> {
    let tags = this.tags;
    if (!Array.isArray(tags)) {
      tags = [tags];
    }
    console.log(tags);
    this.photos = await wahlzeitApi.listPhotos(undefined, tags);
  }
}
</script>

<style scoped></style>
