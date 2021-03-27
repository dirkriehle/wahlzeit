<template>
  <nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
    <div class="container-fluid">
      <router-link class="navbar-brand" :to="{ name: 'Home' }">
        Wahlzeit
      </router-link>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarsExampleDefault"
        aria-controls="navbarsExampleDefault"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarsExampleDefault">
        <ul class="navbar-nav me-auto mb-2 mb-md-0">
          <li class="nav-item">
            <router-link
              class="nav-link"
              aria-current="page"
              :to="{ name: 'Home' }"
            >
              Home
            </router-link>
          </li>
          <li class="nav-item">
            <router-link
              class="nav-link"
              aria-current="page"
              :to="{ name: 'About' }"
            >
              About
            </router-link>
          </li>
          <li v-if="isLoggedIn" class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href="#"
              id="dropdown01"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              User
            </a>
            <ul class="dropdown-menu" aria-labelledby="dropdown01">
              <li>
                <router-link
                  class="dropdown-item"
                  :to="{ name: 'User', params: { id: 2 } }"
                >
                  My Page
                </router-link>
              </li>
              <li>
                <router-link
                  class="dropdown-item"
                  :to="{ name: 'Photo', params: { id: 1 } }"
                >
                  My Page
                </router-link>
              </li>
              <li>
                <router-link class="dropdown-item" :to="{ name: 'Upload' }">
                  Upload Photo
                </router-link>
              </li>
              <li><a class="dropdown-item" href="#">My Photos</a></li>
              <li><a class="dropdown-item" href="#">Settings</a></li>
            </ul>
          </li>
          <li v-if="isLoggedIn" class="nav-item">
            <a class="nav-link" @click="logout()" href="#">Log out</a>
          </li>
          <li v-if="!isLoggedIn" class="nav-item">
            <Login btnClass="nav-link" @login="login" />
          </li>
          <li v-if="!isLoggedIn" class="nav-item">
            <a class="nav-link" @click="login()" href="#">Sign up</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>

  <main class="container">
    <router-view :auth="auth" :isLoggedIn="isLoggedIn" />
  </main>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Login from "@/components/Login.vue";

@Options({
  components: { Login }
})
export default class App extends Vue {
  isLoggedIn = false;
  auth: string | null = "";

  login(auth: string) {
    this.auth = auth;
    this.isLoggedIn = true;
    sessionStorage.setItem("auth", auth);
    console.log(this.auth);
  }

  logout() {
    this.isLoggedIn = false;
    sessionStorage.removeItem("auth");
  }

  created() {
    this.auth = sessionStorage.getItem("auth");
    if (this.auth) {
      this.isLoggedIn = true;
    }
  }
}
</script>

<style>
body {
  padding-top: 5rem;
}
</style>
