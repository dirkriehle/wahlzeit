<template>
  <Modal
    :btn-class="btnClass"
    id="uploadmodal"
    @confirm="upload"
    modal-link="Upload Photo"
    modal-header="Upload Photo"
    :modal-button="success ? false : 'Upload'"
  >
    <div v-if="!success">
      <input
        class="form-control mb-3"
        type="file"
        @change="onFileChange"
        ref="files"
      />
      <div class="form-floating mb-3">
        <input
          type="text"
          class="form-control"
          id="floatingInput"
          placeholder="tags"
          v-model="tags"
        />
        <label for="floatingInput">tags</label>
      </div>
      <div class="errors">
        {{ errors }}
      </div>
    </div>
    <div v-else class="success">Upload successful!</div>
  </Modal>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import { photoApi } from '../../api/PhotoApi';
import Modal from '../../components/modals/Modal.vue';

@Options({
  components: { Modal },
  props: {
    btnClass: '',
  },
})
export default class Upload extends Vue {
  file?: File;
  tags = '';
  errors = '';
  success = false;

  onFileChange(event: Event): void {
    const target = event.target as HTMLInputElement;
    if (target.files != null) {
      this.file = target.files[0];
    }
  }

  async upload(): Promise<void> {
    if (this.file === undefined) {
      this.errors = 'No files selected';
      return;
    }
    try {
      let tags: string[] = [];
      if (this.tags) {
        tags = this.tags.split(RegExp(', ?'));
      }
      console.log(tags);
      const photo = await photoApi.uploadPhoto(this.file, tags);
      const id = photo.id;
      await this.$router.push({ name: 'Photo', params: { id: id } });
      this.success = true;
    } catch (error) {
      this.errors = 'Something went wrong when trying to upload the file.';
      console.error(`Error uploading file: ${JSON.stringify(error)}`);
    }
  }
}
</script>

<style scoped>
.errors {
  color: red;
}

.success {
  color: green;
}
</style>
