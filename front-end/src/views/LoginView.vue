<!-- HTML code !-->
<template>
    <!-- Shows if the user is logged-in !-->
    <div v-if="isLoggedIn" class="flex flex-col items-center mt-10">
        <div class="container flex flex-col justify-center items-center gap-2 max-w-full h-96 m-auto">
            <h1 class="text-2xl bold">U bent al ingelogd! U kunt niet nog een keer inloggen!</h1>
            <RouterLink to="/laptops">
                <button class="themed-button">Klik hier om naar de homepagina te gaan</button>
            </RouterLink>
        </div>
    </div>

    <!-- Shows if the user isn't logged-in !-->
    <div v-else class="p-10 xs:p-0 mx-auto md:w-full md:max-w-lg">
        <div class="p-10 bg-white w-full rounded-2xl border border-[var(--border)]">
            <h1 class="text-color font-black text-center text-3xl bold">Inloggen</h1>
            <div class="mt-12">
                <form method="post" v-on:submit.prevent="onSignIn()">
                    <label :class="{ 'border-red-500': !email }"
                           class="text-sm text-[var(--fill)] block">E-mailadres</label>
                    <input v-model="email" :class="{ 'border-red-500': formSubmitted && (!email || showError) }"
                           class="border border-[var(--border)] rounded-lg px-3 py-2 mb-5 text-sm w-full"
                           name="email"
                           type="email">
                    <label :class="{ 'border-red-500': !password }"
                           class="text-sm text-[var(--fill)] block">Wachtwoord</label>
                    <input v-model="password" :class="{ 'border-red-500': formSubmitted && (!password || showError) }"
                           class="border border-[var(--border)] rounded-lg px-3 py-2 mb-5 text-sm w-full"
                           name="password"
                           type="password"/>
                    <button class="themed-button w-full h-10 rounded-[10px] semi-bold" type="submit">Login</button>
                    <p v-if="errorMessage" class="text-red-600 text-sm pt-5">{{ errorMessage }}</p>
                </form>
            </div>
        </div>
    </div>
</template>

<!-- JavaScript !-->
<script>
// Import
import Swal from 'sweetalert2'

export default {
    name: 'LoginView',
    inject: ['sessionService'],

    data() {
        return {
            email: '',
            password: '',
            errorMessage: '',
            formSubmitted: false,
            showError: false,
            isLoggedIn: false,
        };
    },

    methods: {
        async onSignIn() {
            this.errorMessage = '';
            this.formSubmitted = true;
            this.showError = false;

            if (!this.email || !this.password) {
                this.errorMessage = "Vul alstublieft beide velden in!";
                return;
            }

            const account = await this.sessionService.asyncSignIn(this.email, this.password);

            if (account == null) {
                this.errorMessage = "Verkeerde inloggegevens! Probeer het nog een keer.";
                this.showError = true;
            } else {
                let targetRoute = this.$route.query?.targetRoute;
                if (targetRoute === undefined) targetRoute = this.targetRoute;
                console.log("Current Token:", this.sessionService.currentToken);
                Swal.fire({
                    title: 'Gelukt!',
                    text: 'U bent succesvol ingelogd.',
                    icon: 'success',
                    confirmButtonText: 'Oké',
                    confirmButtonColor: 'var(--theme)',
                    iconColor: 'var(--green)',
                })
                this.$router.push("/laptops");
            }
        },
    },

    created() {
        this.isLoggedIn = !!this.sessionService.currentToken;
    }
}
</script>