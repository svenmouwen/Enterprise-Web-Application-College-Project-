<!-- HTML Code !-->
<template>
    <div class="xl:mx-32 lg:mx-16 mt-14">

        <!-- Shows if the user is logged-in !-->
        <div v-if="isLoggedIn">
            <h1 class="text-left ml-10 text-3xl">Profiel</h1>
            <!-- Whole box !-->
            <div class="m-10 rounded-2xl md:shadow-xl text-left md:pl-5 py-5 md:flex">
                <!-- Left side of the box !-->
                <div class="lg:w-1/6 md:border-r-2 border-b-2 md:border-b-0 mb-10 md:mr-10">
                    <ul>
                        <li :class="[currentComponent === 'profile' ? 'bg-red-100 mb-2 px-4 py-2 w-fit text-red-600 rounded-3xl italic' : 'mb-2 px-4 py-2 w-fit hover:bg-red-100 rounded-3xl ease-in-out duration-150 cursor-pointer']"
                            @click="showProfile">Mijn Profiel
                        </li>
                        <li :class="[currentComponent === 'safety' ? 'bg-red-100 mb-2 px-4 py-2 w-fit text-red-600 rounded-3xl italic' : 'mb-2 px-4 py-2 w-fit hover:bg-red-100 rounded-3xl ease-in-out duration-150 cursor-pointer']"
                            @click="showSafety">Beveiliging
                        </li>
                        <button
                            class="text-sm border-2 py-2 px-4 bg-red-600 text-white cursor-pointer hover:bg-red-700 ease-in-out duration-100 w-fit ml-2"
                            @click="signOut">
                            Uitloggen
                        </button>
                    </ul>
                </div>
                <!-- Right side of the box !-->
                <div v-if="currentComponent === 'profile'" class="flex-1">
                    <ProfileInformation/>
                </div>
                <div v-if="currentComponent === 'safety'" class="flex-1">
                    <ProfileSafety v-model="childMessage"/>
                </div>
            </div>
        </div>

        <!-- Shows if the user isn't logged in !-->
        <div v-else>
            <div class="container flex flex-col justify-center items-center gap-2 max-w-full h-96 m-auto">
                <h1 class="text-2xl text-center bold">Je moet ingelogd zijn om je profiel te kunnen bekijken!</h1>
                <RouterLink to="/">
                    <button class="themed-button">Klik hier om in te loggen</button>
                </RouterLink>
            </div>
        </div>
    </div>
</template>

<!-- JavaScript Code !-->
<script>
// Imports
import ProfileInformation from "@/components/ProfileInformation";
import ProfileSafety from "@/components/ProfileSafety";

export default {
    inject: ["sessionService"],

    data() {
        return {
            isLoggedIn: false,
            currentComponent: 'profile',
            message: '',
            childMessage: ''
        };
    },
    components: {
        ProfileInformation,
        ProfileSafety,
    },
    methods: {
        showProfile() {
            this.currentComponent = 'profile';
        },

        showSafety() {
            this.currentComponent = 'safety';
        },

        signOut() {
            this.sessionService.signOut();
            window.location.reload();

            // Wacht tot de pagina is herladen voordat je doorverwijst
            window.onload = () => {
                this.$router.push("/");
            };
        }
    },

    created() {
        this.isLoggedIn = !!this.sessionService.currentToken;
    }
};
</script>