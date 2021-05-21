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
        class="form-control"
        type="file"
        @change="onFileChange"
        ref="files"
      />
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
import { ApiThing } from "@/ApiThing";

@Options({
  components: { Modal },
  props: {
    btnClass: "",
    api: ApiThing
  }
})
export default class Upload extends Vue {
  file: File | null = null;
  api: ApiThing | null = null;
  errors = "";
  success = false;

  onFileChange(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target.files) {
      this.file = target.files[0];
    }
  }

  upload() {
    if (this.file == null) {
      this.errors = "No files selected";
      return;
    }
    this.api
      ?.uploadPhoto(this.file)
      .then(photo => {
        const id = photo.id;
        this.$router.push({ name: "Photo", params: { id: id } });
        this.success = true;
      })
      .catch(reason => {
        this.errors = "Something went wrong when trying to upload the file.";
        console.error(`Error uploading file: ${reason}`);
      });
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
