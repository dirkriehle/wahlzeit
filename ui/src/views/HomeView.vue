<template>
  <h1>It's Wahlzeit</h1>
  <PhotoView :id="randomPhotoId" />
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import PhotoView from '../views/PhotoView.vue';

@Options({
  components: { PhotoView },
  props: {},
})
export default class HomeView extends Vue {
  get randomPhotoId(): number {
    return 5;
  }
}
</script>
