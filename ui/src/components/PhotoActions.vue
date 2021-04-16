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
import Tell from "@/components/Tell.vue";
import Message from "@/components/Message.vue";
import Report from "@/components/Report.vue";

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

  messag() {
    console.log("message");
  }

  report() {
    console.log("report");
  }
}
</script>

<style scoped></style>
