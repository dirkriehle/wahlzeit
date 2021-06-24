<template>
  <div class="d-grid gap-2">
    <Tell btnClass="btn btn-secondary" :photo="photo" />
    <template v-if="own()">
      <button class="btn btn-secondary" @click="edit">Edit</button>
      <button class="btn btn-danger" @click="delet">Delete</button>
      <button class="btn btn-secondary" @click="select">Select</button>
    </template>
    <template v-else>
      <Message btn-class="btn btn-secondary" />
      <Report btn-class="btn btn-danger" />
    </template>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Tell from "@/components/modals/Tell.vue";
import Message from "@/components/modals/Message.vue";
import Report from "@/components/modals/Report.vue";
import { wahlzeitApi, Photo, User } from "@/WahlzeitApi";

@Options({
  components: {
    Tell,
    Message,
    Report
  },
  props: {
    photo: null,
    owner: null
  }
})
export default class PhotoActions extends Vue {
  photo: Photo | null = null;
  owner: User | null = null;

  own(): boolean {
    return this.photo?.userId == wahlzeitApi.user?.id;
  }

  edit() {
    console.log("edit");
  }

  delet() {
    console.log("delete");
  }

  select() {
    console.error(
      "PhotoActions: profile pictures are not supported by the api"
    );
  }
}
</script>

<style scoped></style>
