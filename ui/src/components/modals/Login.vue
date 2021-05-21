<template>
  <Modal
    :btnClass="btnClass"
    id="loginmodal"
    @confirm="login"
    modal-link="Log in"
    modal-header="Log in"
    modal-button="Log in"
  >
    <div class="failed" v-if="failed">Login Failed: {{ error }}</div>
    <div class="form-floating mb-3">
      <input
        type="email"
        class="form-control"
        id="floatingInput"
        placeholder="name@example.com"
        v-model="name"
        @keyup="loginEnter"
      />
      <label for="floatingInput">Email address</label>
    </div>
    <div class="form-floating">
      <input
        type="password"
        class="form-control"
        id="floatingPassword"
        placeholder="Password"
        v-model="password"
        @keyup="loginEnter"
      />
      <label for="floatingPassword">Password</label>
    </div>
  </Modal>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Modal from "@/components/modals/Modal.vue";
import { ApiThing } from "@/ApiThing";

@Options({
  props: { btnClass: "", api: ApiThing },
  components: { Modal }
})
export default class Login extends Vue {
  name = "";
  password = "";
  failed = false;
  error = "";
  api: ApiThing | null = null;

  loginEnter(event: KeyboardEvent) {
    if (event.key === "Enter") {
      this.login();
    }
  }

  async login() {
    await this.api
      ?.login(this.name, this.password)
      .then(() => {
        document.getElementById("closeModal")?.click();
        location.reload();
      })
      .catch(error => {
        this.failed = true;
        this.error = error;
      });
  }
}
</script>

<style scoped>
.failed {
  color: red;
}
</style>
