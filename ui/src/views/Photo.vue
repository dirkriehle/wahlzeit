<template>
  <div class="container">
    <div class="row">
      <div class="col-8">
        <img class="image" :src="src" />
      </div>
      <div class="col-2 praisebox">
        Praise it
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";

@Options({
  components: {},
  props: { id: "", auth: "" }
})
export default class Photo extends Vue {
  src = "";
  id = "";
  auth = "";

  async mounted() {
    this.src = await fetch(`http://localhost:8080/api/photo/${this.id}`, {
      headers: {
        Authorization: `Basic ${this.auth}`
      }
    })
      .then(response => response.json())
      .then(data => {
        console.log(data["path"]);
        return `http://localhost:8080${data["path"]}`;
      });
  }
}
</script>

<style scoped>
.image {
  max-width: 100%;
}

.praisebox {
  background-color: black;
}
</style>
