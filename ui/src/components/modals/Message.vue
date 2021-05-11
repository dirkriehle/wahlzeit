<template>
  <Modal
    :btn-class="btnClass"
    id="messagemodal"
    @confirm="messag"
    modal-link="Message"
    modal-header="Message"
    modal-button="Message"
  >
    <div class="form-floating mb-3">
      <input
        type="email"
        class="form-control"
        id="toMail"
        placeholder="name@example.com"
        :value="toMail"
      />
      <label for="toMail">To</label>
    </div>

    <div class="form-floating mb-3">
      <input
        type="text"
        class="form-control"
        id="subject"
        placeholder="name@example.com"
      />
      <label for="subject">Subject</label>
    </div>

    <div class="input-group">
      <span class="input-group-text">Message</span>
      <textarea class="form-control" aria-label="Message" rows="4"></textarea>
    </div>
  </Modal>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Modal from "@/components/modals/Modal.vue";

@Options({
  components: { Modal },
  props: {
    btnClass: "",
    auth: "",
    toUser: ""
  }
})
export default class Message extends Vue {
  auth = "";
  toUser = "";

  get toMail() {
    return this.toUser;
  }

  messag() {
    console.log("TODO: tell");
  }
}
</script>

<style scoped></style>
