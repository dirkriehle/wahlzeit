<template>
  <Modal
    :btn-class="btnClass"
    :id="uid('')"
    @confirm="tell"
    modal-link="Tell"
    modal-header="Tell"
    modal-button="Tell"
  >
    <div class="form-floating mb-3">
      <input
        type="email"
        class="form-control"
        :id="uid('toMail')"
        placeholder="name@example.com"
        v-model="toMail"
      />
      <label :for="uid('toMail')">To</label>
    </div>

    <div class="form-floating mb-3">
      <input
        type="text"
        class="form-control"
        :id="uid('subject')"
        placeholder="name@example.com"
        v-model="subject"
      />
      <label :for="uid('subject')">Subject</label>
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
import { Photo, wahlzeitApi } from '../../WahlzeitApi';

@Options({
  components: { Modal },
  props: {
    btnClass: '',
    photo: undefined,
  },
})
export default class Tell extends Vue {
  photo?: Photo;
  toMail = '';
  subject = '';
  text = '';

  uid(id: string): string {
    return `tell-modal-${this.photo?.id ?? '??'}-${id}`;
  }

  get fromMail(): string | undefined {
    return wahlzeitApi.user?.email;
  }

  tell(): void {
    console.log('TODO: tell');
  }
}
</script>

<style scoped></style>
