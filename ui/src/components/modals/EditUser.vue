<template>
  <Modal
    :btn-class="btnClass"
    id="editusermodal"
    @confirm="edit"
    modal-link="Edit"
    modal-header="Edit"
    modal-button="Edit"
  >
    <div class="form-floating mb-3">
      <input
        type="text"
        class="form-control"
        id="username"
        placeholder="name@example.com"
        :value="username"
      />
      <label for="username">Username</label>
    </div>

    <div class="form-floating mb-3">
      <input
        type="email"
        class="form-control"
        id="email"
        placeholder="name@example.com"
        :value="email"
      />
      <label for="email">email</label>
    </div>
  </Modal>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';

import Modal from '../../components/modals/Modal.vue';

@Options({
  components: { Modal },
  props: {
    btnClass: '',
  },
})
export default class EditUser extends Vue {
  get email(): string {
    return 'todo mail';
  }

  get username(): string {
    return 'todo name';
  }

  edit(): void {
    console.log('TODO: edit user');
  }
}
</script>

<style scoped></style>
