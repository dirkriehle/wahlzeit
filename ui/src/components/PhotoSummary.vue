<template>
  <div class="row border border-3">
    <div class="col-4">
      <router-link :to="{ name: 'Photo', params: { id: photo?.id } }">
        <img class="img-fluid" :src="src" />
      </router-link>
    </div>
    <div class="col-5">
      <div>
        praise: <b>{{ photo.praise }}</b>
      </div>
      <div>status: <b>status</b></div>
      <div>date: <b>date</b></div>
      <div>
        tags: <b>{{ photo.tags.join(', ') }}</b>
      </div>
      <div>
        <b>
          <router-link :to="{ name: 'Photo', params: { id: photo?.id } }">
            link
          </router-link>
        </b>
      </div>
    </div>
    <div class="col-3">
      <PhotoActions :photo="photo" />
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import PhotoActions from '../components/PhotoActions.vue';
import { Photo } from '../WahlzeitApi';

@Options({
  components: { PhotoActions },
  props: { photo: undefined },
})
export default class PhotoSummary extends Vue {
  photo?: Photo;

  get src(): string | undefined {
    return this.photo?.path;
  }
}
</script>

<style scoped></style>
