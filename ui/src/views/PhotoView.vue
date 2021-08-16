<template>
  <div class="row">
    <div class="col-md-10 border border-3 rounded-3">
      <img class="img-fluid" :src="src" />
    </div>
    <div class="col-md-2 border border-3 rounded-3">
      <Praise :photo-id="id" />
    </div>
  </div>
  <div class="row">
    <div class="col-md-10 border border-3 rounded-3">
      <Description :photo="photo" :user="user" />
    </div>
    <div class="col-md-2 border border-3 rounded-3">
      <PhotoActions :photo="photo" />
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import Description from '../components/Description.vue';
import PhotoActions from '../components/PhotoActions.vue';
import Praise from '../components/Praise.vue';
import { Photo, User, wahlzeitApi } from '../WahlzeitApi';

@Options({
  components: { Praise, PhotoActions, Description },
  props: { id: '' },
})
export default class PhotoView extends Vue {
  id = '';
  photo: Photo | null = null;
  user: User | null = null;

  async mounted(): Promise<void> {
    this.photo = await wahlzeitApi.getPhoto(this.id);
    this.user = await wahlzeitApi.getUser(this.photo.userId);
  }

  get src(): string | undefined {
    return this.photo?.path;
  }
}
</script>

<style scoped></style>
