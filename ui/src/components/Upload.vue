<template>
  <Modal
    :btn-class="btnClass"
    id="uploadmodal"
    @confirm="upload"
    modal-link="Upload Photo"
    modal-header="UploadPhoto"
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
import Modal from "@/components/Modal.vue";

@Options({
  components: { Modal },
  props: {
    btnClass: "",
    auth: ""
  }
})
export default class Home extends Vue {
  file: File | null = null;
  auth = "";
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
    fetch("http://localhost:8080/api/photo", {
      method: "post",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Basic ${this.auth}`
      },
      body: this.file
    })
      .then(response => response.json())
      .then(data => {
        const id = data["id"];
        this.$router.push({ name: "Photo", params: { id: id } });
        this.success = true;
      })
      .catch(reason => {
        this.errors = "Something went wrong when trying to upload the file.";
        console.log(`Error uploading file: ${reason}`);
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
