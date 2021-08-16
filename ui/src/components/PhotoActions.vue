<template>
  <div class="d-grid gap-2">
    <Tell btnClass="btn btn-secondary" :photo="photo" />
    <template v-if="own()">
      <!-- editing photo info not supported by API
      <button class="btn btn-secondary" @click="edit">Edit</button>
      -->
      <button class="btn btn-danger" @click="deletePhoto">Delete</button>
      <!-- profile picture not supported by API
      <button class="btn btn-secondary" @click="select">Select</button>
      -->
    </template>
    <template v-else>
      <Message btn-class="btn btn-secondary" :photo="photo" :owner="owner" />
      <Report btn-class="btn btn-danger" :photo="photo" />
    </template>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import Message from '../components/modals/Message.vue';
import Report from '../components/modals/Report.vue';
import Tell from '../components/modals/Tell.vue';
import { Photo, User, wahlzeitApi } from '../WahlzeitApi';

@Options({
  components: {
    Tell,
    Message,
    Report,
  },
  props: {
    photo: undefined,
    owner: undefined,
  },
})
export default class PhotoActions extends Vue {
  photo?: Photo;
  owner?: User;

  own(): boolean {
    return this.photo?.userId === wahlzeitApi.user?.id;
  }

  edit(): void {
    console.log('edit');
  }

  async deletePhoto(): Promise<void> {
    if (this.photo !== undefined) {
      await wahlzeitApi.removePhoto(this.photo.id);
    }
  }

  select(): void {
    console.error(
      'PhotoActions: profile pictures are not supported by the api',
    );
  }
}
</script>

<style scoped></style>
