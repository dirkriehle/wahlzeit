<template>
  <div
    class="modal fade"
    id="exampleModal"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Log in</h5>
          <button
            type="button"
            id="closeModal"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
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
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Close
          </button>
          <button type="button" class="btn btn-primary" @click="login">
            Log in
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";

@Options({
  emits: ["login"]
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
