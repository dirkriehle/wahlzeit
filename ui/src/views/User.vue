<template>
  <h1>Profile of User {{ id }}</h1>

  <table class="border">
    <tr>
      <td class="fiveCol2">User name:&nbsp;</td>
      <td class="fiveCol3"><b>admin</b></td>
      <td class="fiveCol4">No photos:&nbsp;</td>
      <td class="fiveCol5"><b>0</b></td>
    </tr>
    <tr>
      <td class="fiveCol2">Gender:&nbsp;</td>
      <td class="fiveCol3"><b>undefined</b></td>
      <td class="fiveCol4">Account status:&nbsp;</td>
      <td class="fiveCol5"><b>confirmed</b></td>
    </tr>
    <tr>
      <td class="fiveCol2">Language:&nbsp;</td>
      <td class="fiveCol3"><b>English</b></td>
      <td class="fiveCol4">Member since:&nbsp;</td>
      <td class="fiveCol5"><b>Jan 1, 1970</b></td>
    </tr>
    <tr>
      <td class="fiveCol2">Email address:&nbsp;</td>
      <td class="fiveCol3"><b>root@localhost</b></td>
      <td class="fiveCol4">Notify about praise:&nbsp;</td>
      <td class="fiveCol5"><b>no</b></td>
    </tr>
    <tr>
      <td class="fiveCol2">Home page:&nbsp;</td>
      <td class="fiveCol3x" colspan="3">
        <b
          ><a href="http://wahlzeit.org/filter?userName=admin" rel="nofollow"
            >http://wahlzeit.org/filter?userName=admin</a
          ></b
        >
      </td>
    </tr>
  </table>
</template>

<script>
import { Options, Vue } from "vue-class-component";

@Options({
  components: {},
  props: { id: "" }
})
export default class User extends Vue {}
</script>

<style scoped></style>
