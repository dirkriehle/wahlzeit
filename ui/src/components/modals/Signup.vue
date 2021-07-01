<template>
  <Modal
    :btn-class="btnClass"
    id="signupmodal"
    @confirm="signup"
    modal-link="Sign up"
    modal-header="Sign up"
    modal-button="Sign up"
  >
    <div class="failed" v-if="failed">Sign up failed: {{ error }}</div>
    <div class="form-floating mb-3">
      <input
        type="text"
        class="form-control"
        id="signup-username"
        placeholder="Username"
        v-model="name"
        @keyup="signupEnter"
      />
      <label for="signup-username">Username</label>
    </div>
    <div class="form-floating mb-3">
      <input
        type="text"
        class="form-control"
        id="signup-email"
        placeholder="Email"
        v-model="email"
        @keyup="signupEnter"
      />
      <label for="signup-email">Email</label>
    </div>
    <div class="form-floating">
      <input
        type="password"
        class="form-control"
        id="signup-password"
        placeholder="Password"
        v-model="password"
        @keyup="signupEnter"
      />
      <label for="signup-password">Password</label>
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
  email = "";
  password = "";
  failed = false;
  error = "";

  signupEnter(event: KeyboardEvent) {
    if (event.key === "Enter") {
      this.signup();
    }
  }

  async signup() {
    try {
      await wahlzeitApi.signup(this.name, this.email, this.password);
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
