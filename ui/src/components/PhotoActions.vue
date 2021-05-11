<template>
  <div class="d-grid gap-2">
    <Tell btnClass="btn btn-secondary" />
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

@Options({
  components: { Tell, Message, Report },
  props: {
    auth: "",
    photo: 0
  }
})
export default class PhotoActions extends Vue {
  own() {
    return false;
  }

  edit() {
    console.log("edit");
  }

  delet() {
    console.log("delete");
  }

  select() {
    console.log("select");
  }
}
</script>

<style scoped></style>
