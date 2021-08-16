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
    <div v-else class="success">
      Upload successful!
    </div>
  </Modal>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Modal from "@/components/modals/Modal.vue";
import { wahlzeitApi } from "@/WahlzeitApi";

@Options({
  components: { Modal },
  props: {
    btnClass: ""
  }
})
export default class Upload extends Vue {
  file: File | null = null;
  tags = "";
  errors = "";
  success = false;

  onFileChange(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target.files) {
      this.file = target.files[0];
    }
  }

  async upload() {
    if (this.file == null) {
      this.errors = "No files selected";
      return;
    }
    try {
      let tags: string[] = [];
      if (this.tags) {
        tags = this.tags.split(RegExp(", ?"));
      }
      console.log(tags);
      const photo = await wahlzeitApi.uploadPhoto(this.file, tags);
      const id = photo.id;
      await this.$router.push({ name: "Photo", params: { id: id } });
      this.success = true;
    } catch (error) {
      this.errors = "Something went wrong when trying to upload the file.";
      console.error(`Error uploading file: ${error}`);
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
