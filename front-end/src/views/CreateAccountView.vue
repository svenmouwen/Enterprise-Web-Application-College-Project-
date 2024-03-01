<template>
    <div v-if="!accountCreated" class="p-10 xs:p-0 mx-auto md:w-full md:max-w-lg">
        <div class="p-10 bg-white w-full rounded-2xl border border-[var(--border)]">
            <h1 class="text-color font-black text-center text-3xl bold">Profiel aanmaken</h1>
            <div class="mt-12">
                <form @submit.prevent="createAccount">
                    <label class="text-sm text-[var(--fill)] block">Voornaam</label>
                    <input v-model="firstName"
                        class="border border-[var(--border)] rounded-lg px-3 py-2 mb-5 text-sm w-full" required type="text">

                    <label class="text-sm text-[var(--fill)] block">Achternaam</label>
                    <input v-model="lastName" class="border border-[var(--border)] rounded-lg px-3 py-2 mb-5 text-sm w-full"
                        required type="text">

                    <label class="text-sm text-[var(--fill)] block">Gebruikersnaam</label>
                    <input v-model="username" class="border border-[var(--border)] rounded-lg px-3 py-2 mb-5 text-sm w-full"
                        required type="text">

                    <label class="text-sm text-[var(--fill)] block">E-mailadres</label>
                    <input v-model="email" class="border border-[var(--border)] rounded-lg px-3 py-2 mb-5 text-sm w-full"
                        required type="email">

                    <label class="text-sm text-[var(--fill)] block">Wachtwoord</label>
                    <input v-model="password" class="border border-[var(--border)] rounded-lg px-3 py-2 mb-5 text-sm w-full"
                        required type="password">

                    <label class="text-sm text-[var(--fill)] block">Herhaal wachtwoord</label>
                    <input v-model="confirmPassword"
                        class="border border-[var(--border)] rounded-lg px-3 py-2 mb-5 text-sm w-full" required
                        type="password">

                    <button type="submit" class="themed-button w-full h-10 rounded-[10px] semi-bold mt-10">
                        Versturen
                    </button>
                </form>
                <section class="flex justify-between mt-2">
                    <router-link class="text-xs text-main forgotPassword" to="/">
                        <p class="text-[var(--theme)]">Terug</p>
                    </router-link>
                </section>
            </div>
        </div>
    </div>
    <!-- Child component -->
    <div v-if="accountCreated" class="p-10 xs:p-0 mx-auto md:w-full md:max-w-lg">
        <div class="p-10 bg-white w-full rounded-2xl border border-[var(--border)]">
            <h1 class="text-color font-black text-center text-3xl bold">Profiel aangemaakt</h1>
            <div class="mt-12">
                <p class="text-center mb-5">Uw account is succesvol aangemaakt!</p>
                <router-link class="themed-button w-full h-10 rounded-[10px] semi-bold mt-10" to="/">
                    Inloggen
                </router-link>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "CreateAccountView",
    data() {
        return {
            firstName: '',
            lastName: '',
            username: '',
            email: '',
            password: '',
            confirmPassword: '',
            accountCreated: false
        };
    },
    methods: {
        createAccount() {
            this.accountCreated = true;
        }
    }
}
</script>