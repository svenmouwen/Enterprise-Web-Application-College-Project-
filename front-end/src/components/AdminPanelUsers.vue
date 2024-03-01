<template>
    <h1 class="text-xl semi-bold">Gebruikers</h1>

    <!-- Buttons section -->
    <div class="mt-5 w-full md:w-auto relative md:flex items-center">
        <!-- Button to open the overlay -->
        <button @click="openOverlay"
            class="rounded-2xl border-2 px-9 py-10 flex items-center space-x-2 hover:bg-gray-100 mr-2 md:w-fit w-full">
            <span>Nodig een gebruiker uit</span>
            <div class="flex items-center justify-center w-8 h-8 mr-2">
                <font-awesome-icon icon="user-plus" size="xl" />
            </div>
        </button>

        <!-- Button to go to the profile -->
        <router-link to="/profile">
            <button
                class="rounded-2xl border-2 px-9 py-10 flex items-center space-x-2 hover:bg-gray-100 w-full mt-5 md:mt-0">
                <span>Ga naar mijn eigen profiel</span>
                <div class="flex items-center justify-center w-8 h-8 mr-2">
                    <font-awesome-icon icon="user" size="xl" />
                </div>
            </button>
        </router-link>
    </div>

    <!-- Section for userlist -->
    <div class="mt-5 mr-10 rounded-2xl border-2 px-6 py-5 mb-5 w-full md:w-auto">
        <div class="md:flex justify-between items-center">
            <h3>Gebruikerslijst</h3>
            <div class="relative">
                <input type="text" class="border rounded-lg py-2 px-3 pr-10 w-full mt-2 md:mt-0" placeholder="Zoeken..."
                    v-model="searchTerm" />
            </div>
        </div>
        <div class="table-container">
            <UsersTable :users="filteredUsers" @user-deleted="handleUserDeleted" />
        </div>
    </div>

    <!-- Overlay Pop-up -->
    <!-- Overlay Pop-up -->
    <div v-if="showOverlay" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
        <div class="bg-white rounded-lg px-10 py-20 max-w-md">
            <!-- Content of the pop-up -->
            <h3 class="text-center italic mb-10">Maak een nieuwe moderator aan</h3>

            <!-- Full Name field -->
            <div class="mb-4">
                <label for="fullName" class="mb-2">Volledige naam van werknemer:</label>
                <input type="text" id="fullName" :class="{ 'border-red-500': !fullName && triedSubmit }"
                    class="w-full border rounded-lg py-2 px-3" v-model="fullName" />
            </div>

            <!-- Email field -->
            <div class="mb-4">
                <label for="email" class="mb-2">Email:</label>
                <input type="email" id="email" :class="{ 'border-red-500': !email && triedSubmit }"
                    class="w-full border rounded-lg py-2 px-3" v-model="email" />
            </div>

            <!-- Password field -->
            <div class="mb-4">
                <label for="password" class="mb-2">Wachtwoord:</label>
                <input type="password" id="password" :class="{ 'border-red-500': (!isPasswordValid && triedSubmit) }"
                    class="w-full border rounded-lg py-2 px-3" v-model="password" />
                <p v-if="!isPasswordValid && triedSubmit" class="text-red-500 text-xs italic">Wachtwoord moet minimaal 8
                    tekens lang zijn, met een cijfer en een hoofdletter.</p>
            </div>
            <div class="mb-4">
                <label for="vestiging" class="mb-2">Vestiging:</label>
                <select id="vestiging" class="w-full border rounded-lg py-2 px-3" v-model="vestiging">
                    <option v-for="location in locations" :key="location.name" :value="location.name">{{ location.name }}
                    </option>
                </select>

            </div>
            <p class="mb-4">Let op: U vult nu de gegevens in voor een nieuwe gebruiker. Deze gebruiker zal dus inloggen met
                deze gegevens. Hij kan zijn wachtwoord later zelf aanpassen</p>
            <div class="text-center mt-4">
                <button @click="sendInvite" class="mt-10 border-2 rounded-2xl border-gray-400 px-5 py-2">
                    Verstuur
                </button>
                <button @click="closeOverlay" class="mt-10 ml-4 border-2 rounded-2xl border-gray-400 px-5 py-2">
                    Annuleren
                </button>
            </div>
        </div>
    </div>
</template>

<script>
import UsersTable from "@/components/UsersTable.vue";
import axios from 'axios';
import Swal from "sweetalert2";

export default {
    components: { UsersTable },
    data() {
        return {
            searchTerm: '',
            showOverlay: false,
            users: [],
            locations: [],
            fullName: '',
            email: '',
            vestiging: '',
            password: '',
            triedSubmit: false,  // To track if the user has tried to submit the form
        };
    },

    computed: {
        // A computed property to validate the password based on a regex
        isPasswordValid() {
            const passwordRegex = /^(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
            return passwordRegex.test(this.password);
        },
        filteredUsers() {
            return this.users.filter(user =>
                user.name.toLowerCase().includes(this.searchTerm.toLowerCase())
            );
        },
    },

    async created() {
        // Fetch users
        try {
            const userResponse = await axios.get(process.env.VUE_APP_API_URL + '/api/users/getAll');
            this.users = userResponse.data;
        } catch (error) {
            console.error('Error fetching users:', error);
        }

        // Fetch locations
        try {
            const locationResponse = await axios.get(process.env.VUE_APP_API_URL + '/api/locations/getAll');
            this.locations = locationResponse.data;
        } catch (error) {
            console.error('Error fetching locations:', error);
        }
    },
    methods: {
        openOverlay() {
            this.showOverlay = true;
        },
        closeOverlay() {
            this.showOverlay = false;
        },
        async sendInvite() {
            this.triedSubmit = true;  // Indicate that the user tried to submit the form
            if (!this.fullName || !this.email || !this.isPasswordValid) {
                // If any field is invalid, prevent form submission
                return;
            }

            // Create a user object based on the input fields
            const newUser = {
                email: this.email,
                name: this.fullName,
                location: this.vestiging,
                password: this.password
            };

            try {
                // Make an HTTP POST request to your Spring Boot API
                const response = await axios.post(process.env.VUE_APP_API_URL + '/api/users/create', newUser);

                // Assuming your backend returns the newly created user
                const createdUser = response.data;

                // Update the frontend users list
                this.users.push(createdUser);

                // Confirmation
                await Swal.fire({
                    title: 'Gelukt!',
                    text: 'U heeft succesvol een account aangemaakt!',
                    icon: 'success',
                    confirmButtonText: 'Oké',
                    confirmButtonColor: 'var(--theme)',
                    iconColor: 'var(--green)',
                })

                // Close the overlay
                this.showOverlay = false;
            } catch (error) {
                console.error('Error creating user:', error);
                // Handle the error appropriately (show a message to the user, etc.)
            }
        },

        handleUserDeleted(deletedUserId) {
            this.users = this.users.filter(user => user.id !== deletedUserId);
        },
    },
};
</script>

<style scoped>
.border-red-500 {
    border-color: #f56565;
    /* Tailwind's red-500 */
}
</style>
