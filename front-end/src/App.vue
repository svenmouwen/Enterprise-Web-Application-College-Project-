<!-- HTML Code !-->
<template>
    <nav>
        <!-- Left side of the navbar -->
        <section>
            <RouterLink :to="{ path: loggedIn ? '/laptops' : '/' }">
              <img class="nav-logo" src="./assets/-1.svg" alt="">
            </RouterLink>
        </section>

        <!-- Right side of the navbar -->
        <section>
            <RouterLink :to="{ path: loggedIn ? '/profile' : '/' }">
                <section v-if="loggedIn" class="nav-item rounded-r-[0] py-[13px] px-[20px] max-[600px]:rounded-r-[10px]">
                    <p class="flex items-center gap-3">
                        <font-awesome-icon icon="user" size="lg" style="color: var(--theme)" />
                        {{ this.theSessionService._currentAccount.name }}
                        <font-awesome-icon icon="chevron-right" size="lg" style="color: var(--theme)" />
                    </p>
                </section>

                <section v-else class="nav-item rounded-r-[0] py-[13px] px-[20px] max-[600px]:rounded-r-[10px]">
                    <p class="flex items-center gap-3">Inloggen
                        <font-awesome-icon icon="chevron-right" size="lg" style="color: var(--theme)" />
                    </p>
                </section>

            </RouterLink>

            <section>
                <RouterLink to="/laptops">
                    <section class="nav-item py-[13px] px-[20px]">
                        <p class="flex items-center gap-3">
                            <font-awesome-icon icon="laptop" size="lg" style="color: var(--theme)" />
                            Laptops
                        </p>
                    </section>
                </RouterLink>
                <div v-if="loggedIn" class="flex gap-[10px]">
                    <RouterLink to="/admin">
                        <section class="nav-item py-[13px] px-[20px]">
                            <p class="flex items-center gap-3">
                                <font-awesome-icon icon="lock" size="lg" style="color: var(--theme)" />
                                Admin
                            </p>
                        </section>
                    </RouterLink>
                </div>
            </section>
        </section>

    </nav>
    <router-view />
</template>

<script>
import { RouterLink } from 'vue-router';
import { shallowReactive } from "vue";
import { SessionSbService } from "@/services/SessionSbService.js";
import { FetchInterceptor } from "@/services/FetchInterceptor.js";

export default {
    name: "App",
    components: { RouterLink },
    data() {
        return {
            loggedIn: this.theSessionService && this.theSessionService._currentAccount !== null ? true : null
        };
    },
    provide() {
        this.theSessionService = shallowReactive(
            new SessionSbService(process.env.VUE_APP_API_URL + "/authentication", "SESS"));
        this.theFetchInterceptor =
            new FetchInterceptor(this.theSessionService);
        return {
            accountsService: new SessionSbService(process.env.VUE_APP_API_URL + "/scooters"),
            sessionService: this.theSessionService,
        }
    },
    watch: {
        '$route'(to, from) {
            if (from.path == "/" && to.path == "/laptops") {
                this.loggedIn = this.theSessionService && this.theSessionService._currentAccount !== null ? true : null;
            }

        }
    },
    beforeUnmount() {
        this.theFetchInterceptor.unregister();
    }
}
</script>


<style>
nav {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: var(--theme);
    padding: 30px 0 30px 30px;
    margin: 15px 0;
}

nav section {
    display: flex;
    flex-direction: row-reverse;
    height: 100%;
    gap: 10px;
}

.nav-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 5px;
    border-radius: 10px;
    background-color: var(--white);
}

.nav-item p {
    color: var(--theme);
}

.nav-logo {
    width: 255px;
}

@media only screen and (max-width: 600px) {
    nav {
        flex-direction: column;
        flex-wrap: wrap;
        justify-content: center;
        gap: 15px;
        padding: 30px;
        margin: 15px 0;
    }

    .nav-item {
        border-radius: 10px;
    }
}

@media only screen and (max-width: 500px) {
    nav a:nth-child(2) {
        width: 100%;
    }

    nav a:nth-child(2) section {
        display: flex;
        justify-content: center;
    }

    nav section:last-child {
        flex-wrap: wrap;
        justify-content: center;
    }
}

/* ITC_Gothic_Bold */
@font-face {
    font-family: 'ITC Gothic Bold';
    src: url('@/assets/fonts/ITC_Gothic_Bold.woff2') format('opentype');
}

/* ITC_Gothic_Demi */
@font-face {
    font-family: 'ITC Gothic Demi';
    src: url('@/assets/fonts/ITC_Gothic_Demi.woff2') format('opentype');
}

/* ITC_Gothic_Medium */
@font-face {
    font-family: 'ITC Gothic Medium';
    src: url('@/assets/fonts/ITC_Gothic_Medium.woff2') format('opentype');
}
</style>
