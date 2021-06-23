<template>
  <div class="home">
    <h1>Hello World</h1>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import { ApiThing } from "@/ApiThing";

@Options({
  components: {},
  props: {
    api: ApiThing
  }
})
export default class HomeView extends Vue {
  api: ApiThing | null = null;
}
</script>
