<template>
  <div v-if="isLoggedIn">
    <h1>Upload a Photo</h1>
    <input type="file" @change="onFileChange" ref="files" />
    <button @click="upload">Upload</button>
  </div>
  <div v-else>
    <h1>Please Log in first</h1>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";

@Options({
  components: {},
  props: {
    auth: "",
    isLoggedIn: false
  }
})
export default class Home extends Vue {
  file: File | null = null;
  auth = "";
  isLoggedIn = false;

  onFileChange(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target.files) {
      this.file = target.files[0];
    }
  }

  upload() {
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
      });
  }
}
</script>

<style scoped></style>
