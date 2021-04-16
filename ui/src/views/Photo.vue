<template>
  <div class="row">
    <div class="col-md-10 border border-3 rounded-3">
      <img class="img-fluid" :src="src" />
    </div>
    <div class="col-md-2 border border-3 rounded-3">
      <Praise :photo="id" :auth="auth" />
    </div>
  </div>
  <div class="row">
    <div class="col-md-10 border border-3 rounded-3">
      <Description />
    </div>
    <div class="col-md-2 border border-3 rounded-3">
      <PhotoActions :photo="id" :auth="auth" />
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Praise from "@/components/Praise.vue";
import PhotoActions from "@/components/PhotoActions.vue";
import Description from "@/components/Description.vue";

@Options({
  components: { Praise, PhotoActions, Description },
  props: { id: "", auth: "" }
})
export default class Photo extends Vue {
  src = "";
  id = "";
  auth = "";

  async mounted() {
    this.src = await fetch(`http://localhost:8080/api/photo/${this.id}`, {
      method: "get",
      headers: {
        Authorization: `Basic ${this.auth}`
      }
    })
      .then(response => response.json())
      .then(data => {
        return `http://localhost:8080${data["path"]}`;
      });
  }
}
</script>

<style scoped></style>
