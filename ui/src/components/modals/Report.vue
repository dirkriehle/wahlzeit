<template>
  <Modal
    :btn-class="btnClass"
    :id="uid('')"
    @confirm="report"
    modal-link="Report"
    modal-header="Report"
    modal-button="Report"
  >
    <select class="form-select" aria-label="Reason" v-model="reason">
      <option disabled="disabled" value="">Select Reason</option>
      <option value="mismatch">Mismatch</option>
      <option value="offensive">Offensive</option>
      <option value="copyright">Copyright infringement</option>
      <option value="other">Other</option>
    </select>

    <div class="input-group">
      <span class="input-group-text">Explanation</span>
      <textarea
        class="form-control"
        aria-label="Explanation"
        rows="4"
        v-model="explanation"
      ></textarea>
    </div>
  </Modal>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Modal from "@/components/modals/Modal.vue";
import { Photo, wahlzeitApi } from "@/WahlzeitApi";

@Options({
  components: { Modal },
  props: {
    btnClass: "",
    photo: null
  }
})
export default class Report extends Vue {
  photo: Photo | null = null;
  reason = "";
  explanation = "";

  uid(id: string) {
    return `report-modal-${this.photo?.id}-${id}`;
  }

  report() {
    if (this.photo)
      wahlzeitApi.reportPhoto(this.photo, this.reason, this.explanation);
  }
}
</script>

<style scoped></style>
