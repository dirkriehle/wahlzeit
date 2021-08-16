<template>
  <Modal
    :btn-class="btnClass"
    :id="uid('')"
    @confirm="messag"
    modal-link="Message"
    modal-header="Message"
    modal-button="Message"
  >
    <div class="form-floating mb-3">
      <input
        type="text"
        class="form-control"
        :id="uid(subject)"
        placeholder="name@example.com"
        v-model="subject"
      />
      <label :for="uid(subject)">Subject</label>
    </div>

    <div class="input-group">
      <span class="input-group-text">Message</span>
      <textarea
        class="form-control"
        aria-label="Message"
        rows="4"
        v-model="text"
      ></textarea>
    </div>
  </Modal>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import Modal from '../../components/modals/Modal.vue';
import { Photo, User } from '../../WahlzeitApi';
@Options({
  components: { Modal },
  props: {
    btnClass: '',
    photo: undefined,
    owner: undefined,
  },
})
export default class Message extends Vue {
  photo?: Photo;
  owner?: User;
  subject = '';
  text = '';

  uid(id: string): string {
    return `message-modal-${this.photo?.id ?? '??'}-${id}`;
  }

  messag(): void {
    console.log('TODO: tell');
  }
}
</script>

<style scoped></style>
