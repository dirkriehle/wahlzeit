<template>
  <Modal
    :btn-class="btnClass"
    id="reportmodal"
    @confirm="report"
    modal-link="Report"
    modal-header="Report"
    modal-button="Report"
  >
    <select class="form-select" aria-label="Reason">
      <option selected>Reason</option>
      <option value="1">One</option>
      <option value="2">Two</option>
      <option value="3">Three</option>
    </select>

    <div class="form-floating mb-3">
      <input
        type="email"
        class="form-control"
        id="mail"
        placeholder="name@example.com"
      />
      <label for="mail">Email</label>
    </div>

    <div class="input-group">
      <span class="input-group-text">Explanation</span>
      <textarea
        class="form-control"
        aria-label="Explanation"
        rows="4"
      ></textarea>
    </div>
  </Modal>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Modal from "@/components/Modal.vue";

@Options({
  components: { Modal },
  props: {
    btnClass: "",
    auth: ""
  }
})
export default class Report extends Vue {
  auth = "";

  report() {
    console.log("TODO: report");
  }
}
</script>

<style scoped></style>
