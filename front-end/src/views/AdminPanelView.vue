<template>
    <div class="xl:mx-32 lg:mx-16 mt-14">
        <div v-if="isLoggedIn">
            <h1 class="text-left ml-10 text-3xl">Admin dashboard</h1>
            <!-- Whole box !-->
            <div class="m-10 rounded-2xl md:shadow-xl text-left md:pl-5 py-5 md:flex">
                <!-- Left side of the box !-->
                <div class="lg:w-1/6 md:border-r-2 border-b-2 md:border-b-0 mb-10 md:mr-10">
                    <ul>
                        <li :class="[currentComponent === 'Gebruikers' ? 'bg-red-100 mb-2 px-4 py-2 w-fit text-red-600 rounded-3xl italic' : 'mb-2 px-4 py-2 w-fit hover:bg-red-100 rounded-3xl ease-in-out duration-150 cursor-pointer']"
                            @click="showUsers('Gebruikers')">
                            Gebruikers
                        </li>
                        <li :class="[currentComponent === 'Laptops' ? 'bg-red-100 mb-2 px-4 py-2 w-fit text-red-600 rounded-3xl italic' : 'mb-2 px-4 py-2 w-fit hover:bg-red-100 rounded-3xl ease-in-out duration-150 cursor-pointer']"
                            @click="this.currentComponent = 'AdminPanelLaptops'">
                            Laptops
                        </li>
                        <li :class="[currentComponent === 'Vestigingen' ? 'bg-red-100 mb-2 px-4 py-2 w-fit text-red-600 rounded-3xl italic' : 'mb-2 px-4 py-2 w-fit hover:bg-red-100 rounded-3xl ease-in-out duration-200 cursor-pointer']"
                            @click="showLocations('Vestigingen')">
                            Vestigingen
                        </li>
                    </ul>
                </div>
                <!-- Right side of the box !-->
                <div class="flex-1">
                    <!-- Add your components here based on currentComponent -->
                    <component :is="currentComponent" v-model="childMessage" />
                </div>
            </div>
        </div>
        <div v-else>
            <div class="container flex flex-col justify-center items-center gap-2 max-w-full h-96 m-auto">
                <h1 class="text-2xl text-center bold">Je moet ingelogd zijn om naar het admin dashboard te gaan!</h1>
                <RouterLink to="/"><button class="themed-button">Klik hier om in te loggen</button></RouterLink>
            </div>
        </div>
    </div>
</template>


<script>
import AdminPanelUsers from "@/components/AdminPanelUsers.vue";
import AdminPanelLaptops from "@/components/AdminPanelLaptops.vue"
import AdminPanelLocations from "@/components/AdminPanelLocations.vue";

export default {
    inject: ["sessionService"],

    data() {
        return {
            isLoggedIn: false,
            currentComponent: 'AdminPanelUsers', // Corrected capitalization
            message: '',
            childMessage: ''
        };
    },

    components: {
        AdminPanelUsers,
        AdminPanelLaptops,
        AdminPanelLocations
    },

    methods: {
        showUsers() { // Corrected method name
            this.currentComponent = 'AdminPanelUsers';
        },
        showLocations() { // Corrected method name
            this.currentComponent = 'AdminPanelLocations';
        }
    },

    created() {
        this.isLoggedIn = !!this.sessionService.currentToken;
    }
};
</script>