<template>
    <div class="home m-">
        <p class="text-3xl text-center bold">Waar ben je naar opzoek?</p>
        <p class="text-center m-3 max-w-xl font-sans">Zoek op EAN-Code, artikelnummer, type of startklaar versie. U kunt
            ook
            filteren
            op verschillende filters om de juiste startklare laptop te vinden.</p>

        <!-- Searchbar -->
        <div class="searchbar">
            <!-- Add new laptop button -->
            <button v-if="currentUser" @click="$router.push('/laptops/new'); this.toggleModal('details');"
                class="searchbar-filters">
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="var(--fill)" class="bi bi-plus-lg"
                    viewBox="0 0 16 16">
                    <path fill-rule="evenodd"
                        d="M8 2a.5.5 0 0 1 .5.5v5h5a.5.5 0 0 1 0 1h-5v5a.5.5 0 0 1-1 0v-5h-5a.5.5 0 0 1 0-1h5v-5A.5.5 0 0 1 8 2" />
                </svg>
            </button>

            <!-- Reload laptop button -->
            <button @click="reloadLaptops" class="searchbar-filters">
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="var(--fill)"
                    class="bi bi-arrow-clockwise" viewBox="0 0 16 16">
                    <path fill-rule="evenodd" d="M8 3a5 5 0 1 0 4.546 2.914.5.5 0 0 1 .908-.417A6 6 0 1 1 8 2z" />
                    <path
                        d="M8 4.466V.534a.25.25 0 0 1 .41-.192l2.36 1.966c.12.1.12.284 0 .384L8.41 4.658A.25.25 0 0 1 8 4.466" />
                </svg>
            </button>
            <button @click="currentFilters.showOptions = !currentFilters.showOptions" class="searchbar-filters">
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="var(--fill)">
                    <path
                        d="M1,4.75H3.736a3.728,3.728,0,0,0,7.195,0H23a1,1,0,0,0,0-2H10.931a3.728,3.728,0,0,0-7.195,0H1a1,1,0,0,0,0,2ZM7.333,2a1.75,1.75,0,1,1-1.75,1.75A1.752,1.752,0,0,1,7.333,2Z" />
                    d="M1,4.75H3.736a3.728,3.728,0,0,0,7.195,0H23a1,1,0,0,0,0-2H10.931a3.728,3.728,0,0,0-7.195,0H1a1,1,0,0,0,0,2ZM7.333,2a1.75,1.75,0,1,1-1.75,1.75A1.752,1.752,0,0,1,7.333,2Z"
                    />
                    <path
                        d="M23,11H20.264a3.727,3.727,0,0,0-7.194,0H1a1,1,0,0,0,0,2H13.07a3.727,3.727,0,0,0,7.194,0H23a1,1,0,0,0,0-2Zm-6.333,2.75A1.75,1.75,0,1,1,18.417,12,1.752,1.752,0,0,1,16.667,13.75Z" />
                    d="M23,11H20.264a3.727,3.727,0,0,0-7.194,0H1a1,1,0,0,0,0,2H13.07a3.727,3.727,0,0,0,7.194,0H23a1,1,0,0,0,0-2Zm-6.333,2.75A1.75,1.75,0,1,1,18.417,12,1.752,1.752,0,0,1,16.667,13.75Z"
                    />
                    <path
                        d="M23,19.25H10.931a3.728,3.728,0,0,0-7.195,0H1a1,1,0,0,0,0,2H3.736a3.728,3.728,0,0,0,7.195,0H23a1,1,0,0,0,0-2ZM7.333,22a1.75,1.75,0,1,1,1.75-1.75A1.753,1.753,0,0,1,7.333,22Z" />
                    d="M23,19.25H10.931a3.728,3.728,0,0,0-7.195,0H1a1,1,0,0,0,0,2H3.736a3.728,3.728,0,0,0,7.195,0H23a1,1,0,0,0,0-2ZM7.333,22a1.75,1.75,0,1,1,1.75-1.75A1.753,1.753,0,0,1,7.333,22Z"
                    />
                </svg>
            </button>

            <!-- Searchbar input -->
            <section class="searchbar-input">
                <svg class="searchbar-icon" xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 24 24">
                    <path fill="var(--fill)"
                        d="M23.707,22.293l-5.969-5.969a10.016,10.016,0,1,0-1.414,1.414l5.969,5.969a1,1,0,0,0,1.414-1.414ZM10,18a8,8,0,1,1,8-8A8.009,8.009,0,0,1,10,18Z" />
                </svg>
                <input v-model="searchInput" placeholder="Zoeken...">
                <button @click="clearInput" :hidden="searchInput.length == 0">
                    <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 512 512">
                        <path fill="var(--border)"
                            d="M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM175 175c9.4-9.4 24.6-9.4 33.9 0l47 47 47-47c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9l-47 47 47 47c9.4 9.4 9.4 24.6 0 33.9s-24.6 9.4-33.9 0l-47-47-47 47c-9.4 9.4-24.6 9.4-33.9 0s-9.4-24.6 0-33.9l47-47-47-47c-9.4-9.4-9.4-24.6 0-33.9z" />
                    </svg>
                </button>
            </section>
            <button @click="toggleModal('scanner')" class="searchbar-qr">Scannen
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="var(--white)" class="bi bi-upc-scan"
                    viewBox="0 0 16 16">
                    <path
                        d="M1.5 1a.5.5 0 0 0-.5.5v3a.5.5 0 0 1-1 0v-3A1.5 1.5 0 0 1 1.5 0h3a.5.5 0 0 1 0 1h-3zM11 .5a.5.5 0 0 1 .5-.5h3A1.5 1.5 0 0 1 16 1.5v3a.5.5 0 0 1-1 0v-3a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 1-.5-.5zM.5 11a.5.5 0 0 1 .5.5v3a.5.5 0 0 0 .5.5h3a.5.5 0 0 1 0 1h-3A1.5 1.5 0 0 1 0 14.5v-3a.5.5 0 0 1 .5-.5zm15 0a.5.5 0 0 1 .5.5v3a1.5 1.5 0 0 1-1.5 1.5h-3a.5.5 0 0 1 0-1h3a.5.5 0 0 0 .5-.5v-3a.5.5 0 0 1 .5-.5zM3 4.5a.5.5 0 0 1 1 0v7a.5.5 0 0 1-1 0v-7zm2 0a.5.5 0 0 1 1 0v7a.5.5 0 0 1-1 0v-7zm2 0a.5.5 0 0 1 1 0v7a.5.5 0 0 1-1 0v-7zm2 0a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-.5.5h-1a.5.5 0 0 1-.5-.5v-7zm3 0a.5.5 0 0 1 1 0v7a.5.5 0 0 1-1 0v-7z" />
                </svg>
            </button>

            <!-- Add new laptop button -->
            <button v-if="searchInput.length > 0" @click="loadAllSearchResults" class="searchbar-filters ml-[5px]">
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="var(--fill)" class="bi bi-search"
                    viewBox="0 0 16 16">
                    <path
                        d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0" />
                </svg>
            </button>
        </div>

        <div v-if="currentFilters.showOptions" class="filter-overlay flex flex-col justify-center">
            <section class="filter-section">
                <input type="checkbox" id="show-only" v-model="currentFilters.showOnly" />
                <label for="show-only"> Toon alleen:</label>
                <select v-model="currentFilters.showValue">
                    <option value="NONE">No Image</option>
                    <option value="WIP">WIP</option>
                    <option value="DONE">Done</option>
                </select>
            </section>
            <section v-if="!mobile" class="filter-section">
                <input type="checkbox" id="status-info" v-model="this.currentFilters.showStatusText" />
                <label for="status-info"> Toon status info</label>
            </section>
            <section class="filter-section">
                <input type="checkbox" id="autoscroll" v-model="currentFilters.autoScroll" />
                <label for="autoscroll"> Auto Scroll</label>
            </section>
        </div>

        <!-- Laptop Table Section -->
        <section class="flex flex-col items-center" v-if="this.laptops">
            <!-- Laptops Table import through Vue.js -->
            <div id="laptopTable">
                <section>
                    <div v-if="laptopsLoading" class="loader"></div>
                    <LaptopTable :class="{ 'loading': this.laptopsLoading }" :laptops="sortedAndFilteredLaptops"
                        :currentSort="this.currentSort" :searchInput="searchInput" :mobile="this.mobile"
                        @sort-requested="sortLaptops" :toggleDetails="this.currentFilters.showStatusText"
                        @request-search="addFromSearch(searchInput)" @toggle-modal="toggleModal('details')"
                        @select-laptop="selectLaptop"></LaptopTable>
                </section>
                <button class="themed-button" @click="toggleModal('info')">
                    <p>Klik hier voor meer info</p>
                    <p v-if="this.currentFilters.showOnly || this.searchInput.length > 0">
                        {{ sortedAndFilteredLaptops.length + ' van de ' + laptops.length }}
                        {{ sortedAndFilteredLaptops.length === 1 ? 'laptop voldoet aan de zoekcriteria.' :
                            'laptops voldoen aan de zoekcriteria.' }}</p>
                    <p v-else>Er zijn {{ this.laptops.length }} laptops geladen.</p>
                </button>
            </div>

            <!-- Load more and less laptops from the Laptop table -->
            <div class="button-wrapper flex-col gap-2 mt-3">
                <section class="button-wrapper flex gap-2">
                    <button class="themed-button w-[200px]" @click="loadLess" :disabled="laptops.length <= 10">Laad
                        minder...</button>
                    <input ref="stepsizeInput" class="themed-button" type="number" min="0" max="50"
                        placeholder="Stapgrootte" v-model.number.lazy="stepSize">
                    <button class="themed-button w-[200px]" @click="loadMore">Laad meer...</button>
                </section>
                <section class="w-full mt-2">
                    <button class="themed-button w-full" @click="this.laptops.length = 0"
                        :disabled="laptops.length === 0"><font-awesome-icon icon="trash" size="1x"
                                                                            style="color: var(--fill)" />
                        &nbsp;&nbsp;Leeg lijst</button>
                </section>
            </div>
        </section>
        <div class="loader" v-else></div>
    </div>

    <!-- Scanner and LaptopDetails modals -->
    <div @click="toggleModal()" class="modal-overlay"></div>

    <div class="info modal modal-inactive flex flex-col">
        <section>
            <h3 class="text-2xl semi-bold">Tabel informatie</h3>
        </section>
        <section class="text-center flex flex-col flex-1 justify-center">
            <p class="font-sans">Er zijn in totaal {{ this.laptops.length }} laptops ingeladen.</p>
            <p class="font-sans">Waarvan {{ this.laptops.filter(laptop => laptop.image.status === 'NONE').length }} nog geen
                image hebben,</p>
            <p class="font-sans">{{ this.laptops.filter(laptop => laptop.image.status === 'WIP').length }} een image in
                behandeling hebben.</p>
            <p class="font-sans">En {{ this.laptops.filter(laptop => laptop.image.status === 'DONE').length }} een
                startklare
                image hebben.</p>
        </section>
    </div>

    <div class="scanner modal modal-inactive">
        <CameraScanner @result="handleScanResult" v-if="scannerActive" />
    </div>

    <div class="details modal modal-inactive">
        <LaptopDetails v-if="currentLaptop || currentUser && $route.fullPath.includes('new')" :currentLaptop="currentLaptop"
            :currentUser="currentUser" @toggle-modal="toggleModal()" @select-laptop="selectLaptop"
            @request-reload="reloadLaptops">
        </LaptopDetails>
    </div>
</template>

<script>
import CameraScanner from "@/components/CameraScanner";
import Swal from 'sweetalert2'
import { watch } from "vue";
import { Laptop } from "@/models/Laptop";
import { Images } from "@/models/Images";
import LaptopTable from "@/components/LaptopTable";
import LaptopDetails from "@/components/LaptopDetails";
import axios from "axios";

export default {
    inject: ["sessionService"],
    name: 'HomeView',
    components: {
        LaptopDetails,
        LaptopTable,
        CameraScanner
    },
    data() {
        return {
            // Users
            users: [],
            currentUser: false,

            // Laptops
            laptops: [],
            filteredLaptops: [],
            stepSize: localStorage.getItem("stepSize") || 10,
            maxStepSize: 50,
            laptopsLoading: false,

            // Scrolling
            showScrollUp: false,
            showScrollDown: true,
            mobile: false,

            // Searchbar
            searchbar: null,
            searchInput: '',

            // Modals
            modal: null,
            modalOpened: true,
            overlay: null,
            modals: {
                filters: { name: 'filters', active: false },
                scanner: { name: 'scanner', active: false },
                details: { name: 'details', active: false },
                info: { name: 'info', active: false }
            },
            scannerActive: false,
            currentLaptop: null,

            // For filters
            currentSort: {
                key: '',
                isAsc: true
            },
            currentFilters: {
                showOptions: false,
                showStatusText: true,
                showOnly: false,
                showValue: "NONE",
                autoScroll: localStorage.getItem("autoScroll") === "true",
            }
        };
    },
    watch: {
        'currentFilters.autoScroll': function (newValue) {
            localStorage.setItem("autoScroll", newValue)
        },
        'stepSize': function (newValue) {
            localStorage.setItem("stepSize", newValue)
        }
    },
    computed: {
        sortedAndFilteredLaptops() {
            let filteredLaptops = this.laptops.filter(laptop => {
                if (this.currentFilters.showOnly && laptop.image.status !== this.currentFilters.showValue) {
                    return false;
                }

                let ean = laptop.ean.toString() || null;
                let articleNr = laptop.articleNr.toString() || null;
                let brand = laptop.brand.trim().toLowerCase().replace(/\s/g, '') || null;
                let model = laptop.model.trim().toLowerCase().replace(/\s/g, '') || null;
                let searchInput = this.searchInput.trim().replace(/\s/g, '').toLowerCase();

                return (ean && ean.includes(searchInput)) ||
                    (articleNr && articleNr.includes(searchInput)) ||
                    (brand && brand.includes(searchInput)) ||
                    (model && model.includes(searchInput));
            });

            return filteredLaptops.sort((a, b) => {
                if (this.currentSort.key) {
                    if (a[this.currentSort.key] < b[this.currentSort.key]) return this.currentSort.isAsc ? -1 : 1;
                    if (a[this.currentSort.key] > b[this.currentSort.key]) return this.currentSort.isAsc ? 1 : -1;
                }
                return 0;
            });
        }
    },
    mounted() {
        // Setting up variables
        this.searchbar = document.querySelector('.searchbar input');
        this.modal = document.querySelector('.scan-modal');
        this.overlay = document.querySelector('.modal-overlay');

        if (window.innerWidth <= 740) this.mobile = true;

        window.addEventListener('resize', () => {
            if (window.innerWidth <= 740) {
                this.mobile = true;
                this.currentFilters.showStatusText = true;
            } else {
                this.mobile = false;
            }
        });

        this.sortLaptops("ean");

        axios.get(process.env.VUE_APP_API_URL + '/api/laptops/load', {
            params: { offset: this.laptops.length, limit: 30 }
        }).then(response => {
            const loadedLaptops = response.data.map(item => {
                let laptop = new Laptop(item.articleNr, item.ean, item.brand, item.model);
                laptop.image = new Images(item.ean, "NONE", null, null);
                return laptop;
            });

            if (loadedLaptops.length > 0) {
                this.laptops = [...this.laptops, ...loadedLaptops];
                return axios.get(process.env.VUE_APP_API_URL + '/api/images/all');
            } else {
                throw new Error("Geen nieuwe laptops te laden...");
            }
        }).then(response => {
            const images = response.data;

            // Loop through the loaded images and check for matching laptop EAN's
            for (let image of images) {
                let laptop = this.laptops.find(l => l.ean === image.laptop.ean);
                if (laptop) {
                    delete image['laptop'];
                    laptop.image = image;
                }
            }
        }).catch(error => {
            Swal.fire({
                title: error.message || 'Fout!',
                text: 'Er is een fout opgetreden tijdens het laden.',
                icon: 'error',
                confirmButtonText: 'Oké',
                confirmButtonColor: 'var(--theme)',
                iconColor: 'var(--red)',
            })
        }).finally(() => {
            if (this.$route.params.ean !== undefined && this.$route.params.ean > 0) {
                this.selectLaptop(parseInt(this.$route.params.ean));
            } else if (this.$route.fullPath.includes("new") && this.currentUser) {
                this.toggleModal('details');
            }

            this.laptopsLoading = false;
        });

        this.$refs.stepsizeInput.addEventListener("input", () => {
            const inputValue = this.$refs.stepsizeInput;

            if (inputValue.value > this.maxStepSize) inputValue.value = this.maxStepSize;
            if (inputValue.value < 0) inputValue.value = 0;
        });
    },
    created() {
        this.currentUser = this.sessionService.currentAccount;
    },
    updated() {
        watch(() => this.searchbar.value, async (_oldInput, newInput) => {
            if (newInput !== _oldInput) this.laptops = await fetch("/api/laptops/search?query=" + newInput);
        })
        watch(() => this.$refs.stepsizeInput.value, async (_oldInput, newInput) => {
            if (newInput !== _oldInput) await fetch("/api/laptops/step?size=" + newInput);
        })
    },
    methods: {
        /**
         * Toggles a modal based on the provided type.
         * @param {string} type - The type of modal to toggle.
         */
        toggleModal(type) {
            if (type) {
                this.modal = document.querySelector(`.${type}.modal`);
                if (!this.modal) return;
                this.modals[type].active = true;

                if (type === "scanner") this.scannerActive = true;

                this.modal.classList.remove("modal-inactive");

                if (this.overlay) this.overlay.style.display = "block";

            } else {
                Object.values(this.modals).forEach(modal => {
                    if (modal.active) {
                        const element = document.querySelector(`.${modal.name}.modal`);
                        if (element) {
                            element.classList.add("modal-inactive");
                        }
                        this.overlay.style.display = "none";
                    }
                });

                this.$router.push("/laptops")
                this.currentLaptop = null;
                this.scannerActive = false;
            }
        },
        /**
         * Sorts the list of laptops based on the specified sorting key and order.
         * @param {string} sortKey - The key to sort the laptops by.
         * @param {boolean} [reload=false] - Indicates whether to reload the list or not.
         */
        sortLaptops(sortKey, reload = false) {
            if (!reload) {
                if (this.currentSort.key === sortKey) {
                    // Toggle sorting order only if it's not a reload
                    this.currentSort.isAsc = !this.currentSort.isAsc;
                } else {
                    // Reset to ascending order for a new sort key
                    this.currentSort = { key: sortKey, isAsc: true };
                }
            }

            // Define the order for the statuses
            const statusOrder = { 'NONE': 1, 'WIP': 2, 'DONE': 3 };

            this.laptops.sort((laptopA, laptopB) => {
                const nestedKeys = sortKey.split('.');
                let valueA = nestedKeys.reduce((currentValue, key) => currentValue?.[key], laptopA);
                let valueB = nestedKeys.reduce((currentValue, key) => currentValue?.[key], laptopB);

                // Check if the sorting is based on status
                if (nestedKeys.includes('status')) {
                    // Adjust the values for sorting based on the specified order
                    valueA = this.currentSort.isAsc ? statusOrder[valueA] : -statusOrder[valueA];
                    valueB = this.currentSort.isAsc ? statusOrder[valueB] : -statusOrder[valueB];
                }

                if (valueA < valueB) return -1;
                if (valueA > valueB) return 1;
                return 0;
            });
        },
        /**
         * Validates the step size for pagination and displays an error message if invalid.
         * @returns {boolean} - Returns true if the step size is valid, otherwise false.
         */
        validateStepSize() {
            // Step size validation
            if (this.stepSize <= 0) {
                Swal.fire({
                    title: 'Fout!',
                    text: 'Voer een waarde groter dan 0 in.',
                    icon: 'error',
                    confirmButtonText: 'Oké',
                    confirmButtonColor: 'var(--theme)',
                    iconColor: 'var(--theme)',
                })
                this.$refs.stepsizeInput.focus();
                return false;
            } else if (this.stepSize > this.maxStepSize) {
                Swal.fire({
                    title: 'Fout!',
                    text: `Voer een waarde kleiner dan ${this.maxStepSize} in.`,
                    icon: 'error',
                    confirmButtonText: 'Oké',
                    confirmButtonColor: 'var(--theme)',
                    iconColor: 'var(--theme)',
                })
                this.$refs.stepsizeInput.focus();
                return false;
            }

            return true;
        },
        /**
         * Reloads the list of laptops from the API and updates the component's data.
         */
        reloadLaptops() {
            axios.get(process.env.VUE_APP_API_URL + '/api/laptops/load', {
                params: { offset: 0, limit: this.laptops.length }
            }).then(response => {
                const loadedLaptops = response.data.map(item => {
                    let laptop = new Laptop(item.articleNr, item.ean, item.brand, item.model);
                    laptop.image = new Images(item.ean, "NONE", null, null);
                    return laptop;
                });

                if (loadedLaptops.length > 0) {
                    this.laptops.length = 0;
                    this.laptops = [...this.laptops, ...loadedLaptops];

                    this.currentSort.isAsc = !this.currentSort.isAsc;
                    this.sortLaptops('ean');

                    return axios.get(process.env.VUE_APP_API_URL + '/api/images/all');
                } else {
                    throw new Error("Geen nieuwe laptops te laden...");
                }
            }).then(response => {
                const images = response.data;

                // Loop through the loaded images and check for matching laptop EAN's
                for (let image of images) {
                    let laptop = this.laptops.find(l => l.ean === image.laptop.ean);
                    if (laptop) {
                        delete image['laptop'];
                        laptop.image = image;
                    }
                }
            }).catch(error => {
                console.log(error);
                Swal.fire({
                    title: error.message || 'Fout!',
                    text: 'Er is een fout opgetreden tijdens het laden.',
                    icon: 'error',
                    confirmButtonText: 'Oké',
                    confirmButtonColor: 'var(--theme)',
                    iconColor: 'var(--red)',
                })
            }).finally(() => {
                this.laptopsLoading = false;
                document.body.style.pointerEvents = "unset";

            });
        },
        /**
         * Loads more laptops from the API and appends them to the existing list.
         */
        loadMore() {
            if (!this.validateStepSize()) return;

            this.laptopsLoading = true;

            document.body.style.pointerEvents = "none";

            axios.get(process.env.VUE_APP_API_URL + '/api/laptops/load', {
                params: { offset: this.laptops.length, limit: this.stepSize }
            }).then(response => {
                const loadedLaptops = response.data.map(item => {
                    let laptop = new Laptop(item.articleNr, item.ean, item.brand, item.model);
                    laptop.image = new Images(item.ean, "NONE", null, null);
                    return laptop;
                });

                if (loadedLaptops.length > 0) {
                    this.laptops = [...this.laptops, ...loadedLaptops];
                    return axios.get(process.env.VUE_APP_API_URL + '/api/images/all');
                } else {
                    throw new Error("Geen nieuwe laptops te laden...");
                }
            }).then(response => {
                const images = response.data;

                // Loop through the loaded images and check for matching laptop EAN's
                for (let image of images) {
                    let laptop = this.laptops.find(l => l.ean === image.laptop.ean);
                    if (laptop) {
                        delete image['laptop'];
                        laptop.image = image;
                    }
                }
            }).catch(error => {
                Swal.fire({
                    title: error.message || 'Fout!',
                    text: 'Er is een fout opgetreden tijdens het laden.',
                    icon: 'error',
                    confirmButtonText: 'Oké',
                    confirmButtonColor: 'var(--theme)',
                    iconColor: 'var(--red)',
                })
            }).finally(() => {
                if (this.$route.params.ean !== undefined && this.$route.params.ean > 0) {
                    this.selectLaptop(parseInt(this.$route.params.ean));
                }

                this.currentFilters.autoScroll && document.querySelector(".table-container").scrollTo({
                    top: document.querySelector(".table-container").scrollHeight,
                    behavior: "smooth"
                })

                this.laptopsLoading = false;
                document.body.style.pointerEvents = "unset";

            });
        },
        /**
         * Loads fewer laptops by removing some from the list.
         */
        loadLess() {
            if (!this.validateStepSize()) return;

            document.body.style.pointerEvents = "none";
            if (this.laptops.length > this.stepSize) this.laptops.splice(this.laptops.length - this.stepSize, this.stepSize);

            setTimeout(() => {
                window.scrollTo({
                    top: document.body.scrollHeight || document.documentElement.scrollHeight,
                    behavior: 'smooth'
                });
                document.body.style.pointerEvents = "unset";
            }, 100)
        },
        loadAllSearchResults() {
            this.laptopsLoading = true;

            document.body.style.pointerEvents = "none";

            axios.get(process.env.VUE_APP_API_URL + '/api/laptops/search/' + this.searchInput).then(response => {
                const loadedLaptops = response.data.map(item => {
                    let laptop = new Laptop(item.articleNr, item.ean, item.brand, item.model);
                    laptop.image = new Images(item.ean, "NONE", null, null);
                    return laptop;
                });

                // Filter out laptops that are already in the list
                const newLaptops = loadedLaptops.filter(newLaptop =>
                    !this.laptops.some(existingLaptop => existingLaptop.ean === newLaptop.ean));

                if (newLaptops.length > 0) {
                    this.laptops = [...this.laptops, ...newLaptops];
                    return axios.get(process.env.VUE_APP_API_URL + '/api/images/all');
                } else {
                    throw new Error("Geen nieuwe laptops te laden...");
                }
            }).then(response => {
                const images = response.data;

                // Loop through the loaded images and check for matching laptop EAN's
                for (let image of images) {
                    let laptop = this.laptops.find(l => l.ean === image.laptop.ean);
                    if (laptop) {
                        delete image['laptop'];
                        laptop.image = image;
                    }
                }
            }).catch(error => {
                Swal.fire({
                    title: error.message || 'Fout!',
                    text: 'Er is een fout opgetreden tijdens het laden.',
                    icon: 'error',
                    confirmButtonText: 'Oké',
                    confirmButtonColor: 'var(--theme)',
                    iconColor: 'var(--red)',
                })
            }).finally(() => {
                if (this.$route.params.ean !== undefined && this.$route.params.ean > 0) {
                    this.selectLaptop(parseInt(this.$route.params.ean));
                }

                this.currentFilters.autoScroll && document.querySelector(".table-container").scrollTo({
                    top: document.querySelector(".table-container").scrollHeight,
                    behavior: "smooth"
                })

                this.laptopsLoading = false;
                document.body.style.pointerEvents = "unset";

            });
        },
        /**
         * Selects a laptop based on its EAN and displays its details in a modal.
         * @param {number} ean - The EAN of the selected laptop.
         */
        async selectLaptop(ean) {
            this.currentLaptop = this.laptops.find(laptop => laptop.ean === ean);

            if (this.currentLaptop === undefined) {
                try {
                    await this.addFromSearch(ean)

                    const existingLaptopIndex = this.laptops.findIndex(laptop => laptop.ean === ean);

                    this.currentLaptop = this.laptops[existingLaptopIndex];

                    if (!this.currentLaptop) throw Error;

                    this.toggleModal('details');
                } catch (err) {
                    this.toggleModal();
                }
            } else {
                this.toggleModal('details');
            }
        },
        /**
         * Searches for a laptop by EAN and adds it to the list if found.
         * Displays an error message if the laptop is not found.
         * @param {number} ean - The EAN of the laptop to search for.
         */
        async addFromSearch(ean) {
            try {
                const response = await axios.get(process.env.VUE_APP_API_URL + '/api/laptops/search/' + ean);
                const result = response.data[0];

                if (!result) throw Error

                const images = await axios.get(process.env.VUE_APP_API_URL + '/api/images/all');
                let relatedImage = null;

                images.data.forEach(image => {
                    if (image.laptop.ean === result.ean) relatedImage = image;
                });

                const newLaptop = new Laptop(result.articleNr, result.ean, result.brand, result.model);
                newLaptop.image = relatedImage || new Images(newLaptop.ean, "NONE", null, null);
                this.laptops.push(newLaptop);

            } catch (error) {
                console.log(error);
                Swal.fire({
                    title: 'Fout!',
                    text: 'De gezochte laptop staat (nog) niet in onze systemen.',
                    icon: 'error',
                    confirmButtonText: 'Oké',
                    confirmButtonColor: 'var(--theme)',
                    iconColor: 'var(--theme)',
                });
            }
        },
        /**
         * Clears the search input and redirects to the default laptop view.
         */
        clearInput() {
            this.searchInput = '';
            this.$router.push("/laptops");
        },
        /**
         * Handles the result of scanning a QR code, updating the search input and closing the scanner modal.
         * @param {string} decodedText - The decoded text from the QR code.
         * @param {string} decodedResult - The decoded result from the QR code.
         */
        handleScanResult(decodedText, decodedResult) {
            this.searchInput = decodedText;

            console.log("Gescande QR-code tekst:", decodedText);
            console.log("Gescande QR-code resultaat:", decodedResult);

            setTimeout(() => {
                this.scannerActive = false;
                this.toggleModal();
            }, 500);
        },
    }
}
</script>

<style scoped>
* {
    transition: all ease-out 0.3s;
}

*:disabled {
    opacity: 0.5;
    cursor: default;
}

.filter-overlay {
    margin-bottom: 20px;
    display: flex;
}

.filter-content {
    padding: 5px;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
}

.filter-content select {
    padding: 10px;
    border-radius: 10px;
}

.filter-content div {
    padding: 10px;
    display: flex;
    flex-direction: column;
}

img,
button {
    user-select: none;
}

.themed-button {
    background-color: white;
    color: var(--fill);
    border: 1px solid var(--border);
    padding: 0 15px;
    border-radius: 10px;
    height: 45px;
}

#laptopTable section {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

#laptopTable .loading {
    filter: brightness(0.8);
    user-select: none;
    pointer-events: none;
}

#laptopTable .loader {
    position: absolute;
    z-index: 99;
}

#laptopTable .themed-button {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    margin: 10px 0 10px 0;
}

input.themed-button {
    width: fit-content;
    text-align: center;
}

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
}

.scroll-buttons {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    position: fixed;
    height: 100%;
    bottom: 0;
    right: 0px;
    gap: 5px;
    padding: 15px 0;
}

.scroll-button {
    opacity: 1;
    padding: 0 15px;
    background-color: var(--theme);
    color: #fff;
    border: none;
    border-radius: 10px 0 0 10px;
    cursor: pointer;
    height: 40px;
    text-align: center;
}

.scroll-button:hover {
    background-color: var(--theme-hover);
}

.home {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 30px;
}

.searchbar {
    display: flex;
    max-width: 700px;
    width: 100%;
    align-items: center;
    justify-content: center;
    box-sizing: border-box;
    border-radius: 20px;
    margin: 25px;
}

.searchbar-filters {
    margin-right: 5px;
    padding: 15px;
    box-sizing: border-box;
    border: 1px solid var(--border);
    border-radius: 20px;
}

.searchbar-input {
    border-radius: 20px 0 0 20px;
    border: 1px solid var(--border);
    border-right: none;
    color: var(--fill);
    width: 100%;
}

.searchbar-input input {
    background-color: unset;
    width: 100%;
}

input::placeholder {
    color: var(--border);
}

.searchbar-qr {
    border-radius: 0 20px 20px 0;
    border: 1px solid var(--border);
    background-color: var(--theme);
    color: var(--white);
}

.searchbar-input,
.searchbar-qr {
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 15px;
    padding: 15px 20px;
    white-space: nowrap;
}

.modal {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    min-height: 30vh;
    max-width: 700px;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: #fff;
    padding: 20px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    z-index: 1000;
    opacity: 1;
    border-radius: 30px;
}

.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
    display: none;
}

.applyFilterBtn {
    background-color: var(--theme);
    color: white;
    padding: 10px;
    height: 75%;
    border-radius: 10px;
}

.modal .close-button {
    position: absolute;
    top: 10px;
    right: 10px;
    cursor: pointer;
}

.modal-inactive {
    opacity: 0;
    user-select: none;
    pointer-events: none;
}

.disabled {
    pointer-events: none;
    user-select: none;
    opacity: 0;
}

@media only screen and (max-width: 740px) {
    #laptopTable {
        display: flex;
        flex-direction: column;
        gap: 10px;
    }

    .scroll-buttons {
        display: none;
    }

    #laptopTable .themed-button {
        justify-content: center;
    }

    #laptopTable .themed-button p:first-child {
        display: none;
    }

    .button-wrapper {
        width: 100%;
    }

    .themed-button {
        margin: 0;
    }

    .themed-button {
        width: 100%;
    }
}

@media only screen and (max-width: 860px) {
    .modal {
        width: 93%;
    }
}

@media only screen and (max-width: 500px) {
    .searchbar {
        flex-direction: row;
        flex-wrap: wrap;
        margin-bottom: 15px;
    }

    .searchbar-input {
        order: 2;
        border: 1px solid var(--border);
        border-radius: 20px 0 0 20px;
        flex: 1;
    }

    .searchbar-filters:first-child {
        order: 3;
        border-radius: 0 20px 20px 0;
        border-left: none;
        margin: 0;
    }

    .searchbar-filters:nth-child(2) {
        order: 4;
        margin-left: 5px;
        margin-right: 0;
    }

    .searchbar-filters:nth-child(3) {
        order: 5;
        margin-left: 5px;
        margin-right: 0;
    }

    .searchbar-qr {
        order: 1;
        display: flex;
        justify-content: center;
        border-radius: 20px;
        width: 100%;
        margin: 10px 0 10px 0;
    }
}
</style>
