<!-- HTML Code !-->
<template>
  <h2>Beveiliging</h2>
  <div class="mt-5 mr-10 rounded-2xl border-2 px-6 py-5 mb-5 w-full md:w-auto">
    <h2 class="italic mb-2">Gegevens</h2>
    <p class="text-gray-400 text-sm mb-5">Houd deze gegevens up-to-date zodat je altijd toegang hebt tot je
      -account</p>
    <div
        class="flex w-full py-3 border-b-2 text-gray-500 hover:text-red-500 ease-in-out duration-100 cursor-pointer mb-2"
        @click="showOverlayPassword">
      <div class="w-4/5">
        <p class="text-sm">Wachtwoord</p>
      </div>
      <div class="w-1/5 text-center">
        <p class="text-sm text-right">></p>
      </div>
    </div>
  </div>

  <!-- Overlay Password !-->
  <div v-if="showingOverlayPassword"
       class="overlay fixed top-0 left-0 w-full h-full flex justify-center items-center z-1000 transition-transform">
    <div class="bg-white p-12 rounded-md shadow-md scale-100">
      <h2 class="text-xl bold mb-5 text-center">Wachtwoord wijzigen</h2>
      <input v-model="sessionService.currentAccount.password"
             :class="{ 'border-red-500': formSubmitted && (!email || showError) }"
             class="border border-gray-500 rounded-md p-3 mt-1 mb-5 text-sm w-full" placeholder="Nieuw wachtwoord"
             type="password"/>
      <input v-model="confirmPassword"
             :class="{ 'border-red-500': formSubmitted && (!email || showError) }"
             class="border border-gray-500 rounded-md p-3 mt-1 mb-5 text-sm w-full"
             placeholder="Herhaal nieuw wachtwoord"
             type="password"/>
      <p class="text-red-700 text-sm pb-5" v-if="errorMessage">{{ errorMessage }}</p>
      <button
          class="transition duration-200 bg-red-600 focus:shadow-sm text-white w-full py-2.5 rounded-lg text-sm shadow-sm hover:shadow-md hover:bg-red-700 font-semibold text-center inline-block mb-3"
          @click="changePassword">
        Bevestig
      </button>
      <button class="transition ease-in-out duration-100 hover:text-red-600" @click="hideOverlay">Terug</button>
    </div>
  </div>
</template>

<!-- JavaScript Code !-->
<script>
import axios from "axios";
import Swal from "sweetalert2";

export default {
  name: "ProfileInformation",
  inject: ["sessionService"],

  data() {
    return {
      showingOverlayPassword: false,
      showingOverlayEmail: false,
      showingOverlayUsername: false,
      errorMessage: '',
      showError: false,
      formSubmitted: false,
    };
  },
  methods: {
    showOverlayPassword() {
      this.showingOverlayPassword = true;
    },
    hideOverlay() {
      this.showingOverlayPassword = false;
      this.sessionService.currentAccount.password = '';
      this.confirmPassword = '';
    },

    changePassword() {
      this.errorMessage = '';
      this.showError = false;
      this.formSubmitted = true;

      if (this.sessionService.currentAccount.password === this.confirmPassword) {
        const updateUserPassword = {
          password: this.sessionService.currentAccount.password
        }

        axios.put(`http://localhost:8080/api/users/updatePassword/${this.sessionService.currentAccount.id}`, updateUserPassword)
            .then(response => {
              console.log("User password updated succesfully:", response.data);
              Swal.fire({
                title: 'Gelukt!',
                text: 'U heeft uw wachtwoord succesvol gewijzigd!.',
                icon: 'success',
                confirmButtonText: 'Oké',
                confirmButtonColor: 'var(--theme)',
                iconColor: 'var(--green)',
              })
              this.hideOverlay();
            })
            .catch(error => {
              console.log("Error updating user password", error);
            })
      } else {
        this.errorMessage = "Nieuw wachtwoord komt niet overeen!";
      }
    }
  }
}
</script>

<!-- CSS Code !-->
<style>
.overlay {
  background-color: rgba(0, 0, 0, 0.7);
}
</style>