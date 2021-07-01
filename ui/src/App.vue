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
          <li v-if="isLoggedIn" class="nav-item">
            <router-link
              class="nav-link"
              :to="{ name: 'User', params: { id: userid } }"
            >
              My Page
            </router-link>
          </li>
          <li v-if="isLoggedIn" class="nav-item">
            <Upload btn-class="nav-link" />
          </li>
          <li v-if="isLoggedIn" class="nav-item">
            <a class="nav-link" @click="logout()" href="#">Log out</a>
          </li>
          <li v-if="!isLoggedIn" class="nav-item">
            <Login btn-class="nav-link" />
          </li>
          <li v-if="!isLoggedIn" class="nav-item">
            <Signup btn-class="nav-link" />
          </li>
        </ul>
        <div class="d-flex">
          <input
            class="form-control me-2"
            type="search"
            placeholder="Search for tags"
            aria-label="Search for tags"
            v-model="searchTags"
          />
          <button class="btn btn-outline-success" @click="search">
            Search
          </button>
        </div>
      </div>
    </div>
  </nav>

  <main class="container">
    <router-view />
  </main>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Login from "@/components/modals/Login.vue";
import Signup from "@/components/modals/Signup.vue";
import Upload from "@/components/modals/Upload.vue";
import { wahlzeitApi } from "@/WahlzeitApi";

@Options({
  components: {
    Login,
    Upload,
    Signup
  }
})
export default class App extends Vue {
  searchTags = "";

  logout() {
    wahlzeitApi.logout();
    location.reload();
  }

  async search() {
    const tags = this.searchTags.split(RegExp(", ?"));
    await this.$router.push({
      name: "PhotoList",
      query: { tags: tags }
    });
  }

  get isLoggedIn() {
    return wahlzeitApi.isLoggedIn;
  }

  get userid() {
    return wahlzeitApi.user?.id;
  }
}
</script>

<style>
body {
  padding-top: 5rem;
}
</style>
