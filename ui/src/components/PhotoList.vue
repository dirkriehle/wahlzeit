<template>
  <template v-for="photo in photos" :key="photo.id">
    <PhotoSummary :photo="photo" />
  </template>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import { Photo } from '../api/PhotoApi';
import PhotoSummary from '../components/PhotoSummary.vue';

@Options({
  components: {
    PhotoSummary,
  },
  props: {
    photos: [],
  },
})
export default class PhotoList extends Vue {
  photos: Photo[] = [];
}
</script>

<style scoped></style>
