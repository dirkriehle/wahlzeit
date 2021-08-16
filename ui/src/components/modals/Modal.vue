<template>
  <a :class="btnClass" href="#" data-bs-toggle="modal" :data-bs-target="refid">
    {{ modalLink }}
  </a>

  <teleport to="body">
    <div
      class="modal fade"
      :id="id"
      tabindex="-1"
      aria-labelledby="exampleModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">
              {{ modalHeader }}
            </h5>
            <button
              type="button"
              id="closeModal"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <slot />
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              data-bs-dismiss="modal"
            >
              Close
            </button>
            <button
              v-if="modalButton"
              type="button"
              class="btn btn-primary"
              @click="confirm"
            >
              {{ modalButton }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </teleport>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";

@Options({
  emits: ["confirm"],
  props: {
    btnClass: "",
    id: "",
    modalLink: "",
    modalHeader: "",
    modalButton: ""
  }
})
export default class Modal extends Vue {
  id = "";

  confirm() {
    this.$emit("confirm");
  }

  get refid() {
    return `#${this.id}`;
  }
}
</script>

<style scoped></style>
