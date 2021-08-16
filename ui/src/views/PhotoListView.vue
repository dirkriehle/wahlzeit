<template>
  <h1>Results</h1>
  <div class="container border border-3">
    <PhotoList :photos="photos" />
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import { Photo, photoApi } from '../api/PhotoApi';
import PhotoList from '../components/PhotoList.vue';

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
    this.photos = await photoApi.listPhotos(undefined, tags);
  }
}
</script>

<style scoped></style>
