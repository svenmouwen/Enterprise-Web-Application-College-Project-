<template>
    <h1 class="text-xl semi-bold">Locaties</h1>

    <!-- Buttons section -->
    <div class="mt-5 w-full md:w-auto relative md:flex items-center">
        <!-- Button to add a new location -->
        <button @click="openOverlay"
            class="rounded-2xl border-2 px-9 py-10 flex items-center space-x-2 hover:bg-gray-100 mr-2 md:w-fit w-full">
            <span>Voeg een locatie toe</span>
            <div class="flex items-center justify-center w-8 h-8 mr-2">
                <font-awesome-icon icon="location-dot" size="xl" />
            </div>
        </button>
    </div>

    <!-- Section for location list -->
    <div class="mt-5 mr-10 rounded-2xl border-2 px-6 py-5 mb-5 w-full md:w-auto">
        <div class="md:flex justify-between items-center">
            <h3>Locatielijst</h3>
            <div class="relative">
                <input type="text" class="border rounded-lg py-2 px-3 pr-10 w-full mt-2 md:mt-0" placeholder="Zoeken..."
                    v-model="searchTerm" />
            </div>
        </div>
        <div class="table-container">
            <LocationsTable :locations="filteredLocations" @location-deleted="handleLocationDeleted" />
        </div>
    </div>

    <!-- Overlay Pop-up -->
    <div v-if="showOverlay" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
        <div class="bg-white rounded-lg px-10 py-20 max-w-md">
            <!-- Content of the pop-up -->
            <h3 class="text-center italic mb-10">Voeg een nieuwe locatie toe</h3>

            <!-- Location Name field -->
            <div class="mb-4">
                <label for="locationName" class="mb-2">Naam van de locatie:</label>
                <input type="text" id="locationName" class="w-full border rounded-lg py-2 px-3" v-model="locationName" />
            </div>
            <div class="text-center mt-4">
                <button @click="addLocation" class="mt-10 border-2 rounded-2xl border-gray-400 px-5 py-2">
                    Toevoegen
                </button>
                <button @click="closeOverlay" class="mt-10 ml-4 border-2 rounded-2xl border-gray-400 px-5 py-2">
                    Annuleren
                </button>
            </div>
        </div>
    </div>
</template>

<script>
import LocationsTable from "@/components/LocationsTable.vue";
import axios from 'axios';

export default {
    components: { LocationsTable },
    data() {
        return {
            searchTerm: '',
            showOverlay: false,
            locations: [],
            locationName: '',
        };
    },
    computed: {
        filteredLocations() {
            return this.locations.filter(location => location.name.toLowerCase().includes(this.searchTerm.toLowerCase()));
        }
    },
    async created() {
        try {
            const response = await axios.get(process.env.VUE_APP_API_URL + '/api/locations/getAll');
            this.locations = response.data;
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
        async addLocation() {
            if (!this.locationName) {
                // If location name is empty, prevent form submission
                return;
            }

            // Create a location object based on the input field
            const newLocation = {
                name: this.locationName,
            };

            try {
                // Make an HTTP POST request to your backend API
                const response = await axios.post(process.env.VUE_APP_API_URL + '/api/locations/create', newLocation);

                // Assuming your backend returns the newly created location
                const createdLocation = response.data;

                // Update the frontend locations list
                this.locations.push(createdLocation);

                // Close the overlay
                this.showOverlay = false;
            } catch (error) {
                console.error('Error adding location:', error);
                // Handle the error appropriately (show a message to the user, etc.)
            }
        },

        handleLocationDeleted(deletedLocationId) {
            this.locations = this.locations.filter(location => location.id !== deletedLocationId);
        },
    },
};
</script>