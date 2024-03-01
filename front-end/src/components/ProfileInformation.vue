<!-- HTML Code !-->
<template>
    <!-- Profile information !-->
    <h2>Mijn profiel</h2>
    <div class="mt-5 mr-10 rounded-2xl border-2 px-5 py-2 w-full md:w-auto flex">
        <div class="mr-5">
            <img alt="avatar icon" class="mt-2.5 md:mt-0 mb-0" src="../assets/avatar.png" width="100">
        </div>
        <div class="flex-1 mt-3">
            <h3 class="italic mb-2">{{ sessionService.currentAccount.name }}</h3>
            <p class="mb-2 text-gray-400 text-sm">Admin</p>
            <p class="mb-2 text-gray-400 text-sm">{{ sessionService.currentAccount.location }}</p>
        </div>
    </div>

    <!-- Other information !-->
    <div v-if="currentComponent === 'currentInformation'" class="flex-1">
        <div class="mt-5 mr-10 rounded-2xl border-2 px-6 py-5 mb-5 w-full md:w-auto">
            <h2 class="italic mb-5">Persoonlijke informatie</h2>
            <div class="mb-5">
                <h3 class="italic text-sm">Naam</h3>
                <div contenteditable v-text="sessionService.currentAccount.name" class="text-gray-400 text-sm"
                    @input="updateEdit"></div>
            </div>
            <div class="mb-5">
                <h3 class="italic text-sm">E-mailadres</h3>
                <div contenteditable v-text="sessionService.currentAccount.email" class="text-gray-400 text-sm"
                    @input="updateEdit"></div>
            </div>
            <div class="mb-5">
                <h3 class="italic text-sm">Locatie</h3>
                <div contenteditable v-text="sessionService.currentAccount.location" class="text-gray-400 text-sm"
                    @input="updateEdit"></div>
            </div>
            <div class="md:flex">
                <div>
                    <h3 class="italic text-sm">Gebruikersrechten</h3>
                    <p class="text-gray-400 text-sm">Admin</p>
                </div>
                <div class="md:flex-1 mt-5">
                    <p :class="[currentComponent === 'changeInformation' ? '' : 'text-xs text-red-500 hover:text-red-700 ease-in-out duration-100 cursor-pointer italic md:float-right']"
                        @click="showChanges">Wijzig gegevens
                    </p>
                </div>
            </div>
        </div>
        <div class="mb-5">
            <button @click="deleteAccount" class="themed-button">
                Verwijder
                account
            </button>
        </div>
    </div>

    <!-- Change profile information !-->
    <div v-if="currentComponent === 'changeInformation'" class="flex-1">
        <div class="mt-5 mr-10 rounded-2xl border-2 px-6 py-5 mb-5 w-full md:w-auto">
            <h2 class="italic mb-5">Persoonlijke informatie</h2>
            <div class="mb-5">
                <h3 class="italic text-sm">Naam</h3>
                <input v-model="sessionService.currentAccount.name"
                    class="text-gray-400 text-sm border-2 p-1 rounded-md w-full">
            </div>
            <div class="mb-5">
                <h3 class="italic text-sm">E-mailadres</h3>
                <input v-model="sessionService.currentAccount.email"
                    class="text-gray-400 text-sm border-2 p-1 rounded-md w-full">
            </div>
            <div class="mb-5">
                <h3 class="italic text-sm">Locatie</h3>
                <select v-model="sessionService.currentAccount.location"
                    class="text-gray-400 text-sm border-2 p-1 rounded-md w-full">
                    <option value=" Almere"> Almere</option>
                    <option value=" Amsterdam West"> Amsterdam West</option>
                </select>
            </div>
            <div class="mb-5">
                <h3 class="italic text-sm">Gebruikersrechten</h3>
                <p class="text-gray-400 text-sm">Admin</p>
            </div>
            <div>
                <p :class="[currentComponent === 'currentInformation' ? '' : 'text-sm border-2 py-2 px-4 bg-red-600 text-white cursor-pointer hover:bg-red-700 ease-in-out duration-100 w-fit']"
                    @click="updateUserDataInDataBase">Opslaan
                </p>
            </div>
        </div>
    </div>
</template>

<!-- JavaScript Code !-->
<script>
import axios from "axios";
import Swal from "sweetalert2";

export default {
    name: "ProfileComponent",
    inject: ["sessionService"],

    data() {
        return {
            currentComponent: "currentInformation",
            message: '',
            childMessage: '',
        };
    },

    methods: {
        showChanges() {
            this.currentComponent = 'changeInformation'
        },
        updateEdit(event) {
            this.sessionService.currentAccount.name = event.target.innerText;
            this.sessionService.currentAccount.email = event.target.innerText;
            this.sessionService.currentAccount.location = event.target.innerText;
        },
        updateUserDataInDataBase() {
            const updatedUserData = {
                name: this.sessionService.currentAccount.name,
                email: this.sessionService.currentAccount.email,
                location: this.sessionService.currentAccount.location,
            }

            axios.put(`http://localhost:8080/api/users/update/${this.sessionService.currentAccount.id}`, updatedUserData)
                .then(response => {
                    console.log("User data updated succesfully:", response.data);
                    Swal.fire({
                        title: 'Gelukt!',
                        text: 'U heeft uw gegevens succesvol gewijzigd!',
                        icon: 'success',
                        confirmButtonText: 'Oké',
                        confirmButtonColor: 'var(--theme)',
                        iconColor: 'var(--green)',
                    })
                })
                .catch(error => {
                    console.log("Error updating user data:", error);
                });

            this.currentComponent = 'currentInformation';
        },

        deleteAccount() {
            Swal.fire({
                title: 'Bevestiging',
                text: 'Weet je zeker dat je je account wilt verwijderen?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: 'var(--theme)',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Ja, verwijder mijn account!',
                cancelButtonText: 'Nee, behoud mijn account'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.sendDeleteRequest();
                }
            });
        },

        async sendDeleteRequest() {
            const deleteEndpoint = `http://localhost:8080/api/users/delete/${this.sessionService.currentAccount.id}`;

            axios.delete(deleteEndpoint)
                .then(response => {
                    console.log("User account deleted on the server:", response.data);
                    Swal.fire({
                        title: 'Gelukt!',
                        text: 'U heeft uw account succesvol verwijderd!',
                        icon: 'success',
                        confirmButtonText: 'Oké',
                        confirmButtonColor: 'var(--theme)',
                        iconColor: 'var(--green)',
                    });
                    this.sessionService.signOut();
                    this.$router.push("/");
                })
                .catch(error => {
                    console.error("Error deleting user account on the server:", error);
                });
        }
    },
}
</script>