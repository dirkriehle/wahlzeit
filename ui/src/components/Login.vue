<template>
  <Modal :btnClass="btnClass" id="loginmodal" @confirm="login">
    <template v-slot:link>
      Login
    </template>
    <template v-slot:header>
      Log in
    </template>
    <template v-slot:body>
      <div class="failed" v-if="failed">Login Failed</div>
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
    </template>
    <template v-slot:confirm>
      Log in
    </template>
  </Modal>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Modal from "@/components/Modal.vue";

@Options({
  emits: ["login"],
  props: { btnClass: "" },
  components: { Modal }
})
export default class Login extends Vue {
  name = "";
  password = "";
  failed = false;

  loginEnter(event: KeyboardEvent) {
    if (event.key === "Enter") {
      this.login();
    }
  }

  async login() {
    await fetch("http://localhost:8080/api/user/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        email: this.name,
        password: this.password
      })
    })
      .then(response => response.json())
      .then(data => {
        const name = data["email"];
        const password = data["password"];
        console.log(`${name}:${password}`);
        const auth = btoa(`${name}:${password}`);
        this.$emit("login", auth);
        document.getElementById("closeModal")?.click();
      })
      .catch(() => {
        this.failed = true;
      });
  }
}
</script>

<style scoped>
.failed {
  color: red;
}
</style>
