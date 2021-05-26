<template>
  <div class="">
    <h3>Praise it</h3>
    <div v-for="n in 10" :key="n" class="form-check fs-5">
      <input
        class="form-check-input"
        type="radio"
        name="exampleRadios"
        :id="'exampleRadios' + n"
        :value="'option' + n"
        @click="praise(n)"
      />
      <label class="form-check-label" :for="'exampleRadios' + n">
        {{ n }}
      </label>
    </div>
    <a href="#" @click="skip">skip</a>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import { ApiThing } from "@/ApiThing";

@Options({
  props: {
    photoId: 0,
    api: ApiThing
  }
})
export default class Praise extends Vue {
  api: ApiThing | null = null;
  photoId = 0;

  praise(n: number) {
    this.api?.praisePhoto(this.photoId, n);
    console.log(`TODO: praise ${n}`);
  }

  skip() {
    console.log("TODO: skip praise");
  }
}
</script>

<style scoped></style>
