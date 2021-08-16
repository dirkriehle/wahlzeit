<template>
  <Modal
    :btn-class="btnClass"
    id="loginmodal"
    @confirm="login"
    modal-link="Log in"
    modal-header="Log in"
    modal-button="Log in"
  >
    <div class="failed" v-if="failed">Login failed: {{ error }}</div>
    <div class="form-floating mb-3">
      <input
        type="text"
        class="form-control"
        id="login-username"
        placeholder="Username"
        v-model="name"
        @keyup="loginEnter"
      />
      <label for="login-username">Username</label>
    </div>
    <div class="form-floating">
      <input
        type="password"
        class="form-control"
        id="login-password"
        placeholder="Password"
        v-model="password"
        @keyup="loginEnter"
      />
      <label for="login-password">Password</label>
    </div>
  </Modal>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Modal from "@/components/modals/Modal.vue";
import { wahlzeitApi } from "@/WahlzeitApi";

@Options({
  props: { btnClass: "" },
  components: { Modal }
})
export default class Login extends Vue {
  name = "";
  password = "";
  failed = false;
  error = "";

  loginEnter(event: KeyboardEvent) {
    if (event.key === "Enter") {
      this.login();
    }
  }

  async login() {
    try {
      await wahlzeitApi.login(this.name, this.password);
      location.reload();
    } catch (error) {
      this.failed = true;
      this.error = error;
    }
  }
}
</script>

<style scoped>
.failed {
  color: red;
}
</style>
