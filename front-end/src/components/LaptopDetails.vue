<!-- eslint-disable no-unused-vars -->
<template>
    <div v-if="laptop && !creating && !editing && !updating" class="flex flex-col justify-center m-5 items-center">

        <!-- Laptop Information -->
        <div class="flex flex-col text-center items-center">
            <section class="flex items-center gap-2">
                <p class="text-3xl text-center bold">{{ laptop.ean }}</p>
                <div v-if="laptop.image.status === 'DONE'" class='status-icon green' />
                <div v-else-if="laptop.image.status === 'WIP'" class='status-icon orange' />
                <div v-else class='status-icon red' />
            </section>
            <p v-if="laptop.image.status === 'DONE'" class="text-sm">{{ getImageName() }}</p>
        </div>

        <!-- Image Information -->
        <div class="my-1">
            <div>
                <section class="flex items-center justify-center gap-2 m-3">
                    <p class="text-center">Image Status: </p>
                    <p v-if="laptop.image.status === 'WIP'" class="text-center text-[var(--orange)] font-bold">
                        Work in progress</p>
                    <p v-else-if="laptop.image.status === 'DONE'" class="text-center text-[var(--green)] font-bold">
                        Done</p>
                    <p v-else class="text-center text-[var(--red)] font-bold">No Image</p>
                </section>
                <section v-if="laptop.image.updated_by">
                    <p class="text-center m-3">Image Location:
                        {{ laptop.image.updated_by.location }}
                    </p>
                    <div v-if="laptop.image.status !== 'NONE'" class="flex flex-row items-center justify-center m-3">
                        <p class="text-center  mr-2">Last updated: </p>
                        <p class="text-center"> {{
                            new Date(laptop.image.datetime).toLocaleString('nl-NL', {
                                year: 'numeric', month: '2-digit', day:
                                    '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit', timeZoneName: 'short'
                            })
                        }}</p>
                    </div>
                </section>
            </div>
            <section v-if="laptop.image.notes">
                <p class="text-center mt-3">Notes:</p>
                <p class="text-center mb-3"> {{ laptop.image.notes }}</p>
            </section>
        </div>

        <!-- Laptop Specs Table -->
        <div class="mt-3">
            <table>
                <thead>
                    <tr>
                        <th scope="col">EAN</th>
                        <th scope="col">Art. Nr.</th>
                        <th scope="col">Merk</th>
                        <th scope="col">Type</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td data-label="EAN">{{ laptop.ean }}</td>
                        <td data-label="Art. Nr.">{{ laptop.articleNr }}</td>
                        <td data-label="Merk">{{ laptop.brand }}</td>
                        <td data-label="Type">{{ laptop.model }}</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div v-if="currentUser" class="w-full flex flex-row gap-2">
            <button v-if="laptop.image.status === 'NONE' || laptop.image.status === 'OLD' && currentUser"
                @click="updating = true" class="themed-button w-full mt-3">
                <p class="semi-bold"><font-awesome-icon icon="hard-drive" size="1x" /> Image aanmaken </p>
            </button>
            <button
                v-else-if="laptop.image.status !== 'NONE' && currentUser && currentUser.id === laptop.image.updated_by.id"
                @click="updating = true" class="themed-button w-full mt-3">
                <p class="semi-bold"><font-awesome-icon icon="hard-drive" size="1x" /> &nbsp;Image bijwerken </p>
            </button>
            <button v-if="currentUser" @click="editing = true; resetLaptop()" class="themed-button w-full mt-3">
                <p class="semi-bold"><font-awesome-icon icon="pen" size="1x" /> &nbsp;Laptop bewerken </p>
            </button>
        </div>
    </div>

    <!-- ADDING NEW IMAGE -->
    <div v-if="updating && currentUser && (laptop && laptop.image.status == 'NONE' || laptop.image.status == null)"
        class="creation-container flex flex-col justify-center m-5 items-center">
        <form @submit.prevent="addImage" class="w-full">
            <p class="text-2xl bold text-center">NIEUWE IMAGE TOEVOEGEN</p>
            <p class="text-xl text-center">{{ laptop.brand }} - {{ laptop.ean }} </p>
            <p class="text-1xl text-center">{{ laptop.model }}</p>

            <div class="flex flex-wrap mt-10">
                <div class="w-full px-3 flex flex-col mb-3">
                    <label class="semi-bold" for="medewerkerNaam">Medewerker</label>
                    <input id="userName" type="text" placeholder="Volledige Naam" v-model="image.user"
                        :readonly="image.user !== ''" />
                </div>
                <div class="w-full px-3 flex flex-col mb-3">
                    <label class="semi-bold" for="status">Image Status</label>
                    <select id="status" placeholder="Huidige status" v-model="image.status">
                        <option :value="null" selected disabled>Kies de huidige status</option>
                        <option v-for="status in imageStatus" :key="status.id" :value="status.value">{{ status.description
                        }}
                        </option>
                    </select>
                </div>
                <div class="w-full px-3 flex flex-col mb-3">
                    <label class="semi-bold" for="notes">Notities</label>
                    <textarea class="h-20" id="notes" placeholder="Schrijf hier uw notities of opmerkingen..."
                        v-model="image.notes"></textarea>
                </div>
                <section class="flex justify-end gap-3 items-center w-full mt-10 px-3">
                    <div>
                        <button class="themed-button alt h-12" type="button" @click="updating = false">Annuleren</button>
                    </div>
                    <div>
                        <button class="themed-button h-12" type="submit">Bevestigen</button>
                    </div>
                </section>
            </div>
        </form>
    </div>

    <!-- UPDATE EXISTING IMAGE -->
    <div v-if="updating && currentUser && (laptop && laptop.image.status !== 'NONE')"
        class="creation-container flex flex-col justify-center m-5 items-center">
        <form @submit.prevent="updateImage" class="w-full">
            <p class="text-2xl bold text-center">IMAGE BIJWERKEN</p>
            <p class="text-xl text-center">{{ laptop.brand }} - {{ laptop.ean }} </p>
            <p class="text-1xl text-center">{{ laptop.model }}</p>

            <div class="flex flex-wrap mt-10">
                <div class="w-full px-3 flex flex-col mb-3">
                    <label class="semi-bold" for="medewerkerNaam">Medewerker</label>
                    <input id="userName" type="text" placeholder="Volledige Naam" v-model="image.user"
                        :readonly="image.user !== ''" />
                </div>
                <div class="w-full px-3 flex flex-col mb-3">
                    <label class="semi-bold" for="status">Image Status</label>
                    <select id="status" placeholder="Huidige status" v-model="image.status">
                        <option :value="null" selected disabled>Kies de huidige status</option>
                        <option v-for="status in imageStatus" :key="status.id" :value="status.value">{{ status.description
                        }}
                        </option>
                    </select>
                </div>
                <div class="w-full px-3 flex flex-col mb-3">
                    <label class="semi-bold" for="notes">Notities</label>
                    <textarea class="h-20" id="notes" placeholder="Schrijf hier uw notities of opmerkingen..."
                        v-model="image.notes"></textarea>
                </div>

                <div class="flex flex-row items-center mt-10 justify-between w-full px-3">
                    <section>
                        <button class="themed-button h-12" type="button" @click="deleteImage"><font-awesome-icon
                                icon="trash" size="1x" style="color: var(--white)" /> &nbsp;&nbsp;Verwijderen</button>
                    </section>
                    <section class="flex justify-end gap-1 items-center w-full">
                        <div>
                            <button class="themed-button alt h-12" type="button"
                                @click="updating = false">Annuleren</button>
                        </div>
                        <div>
                            <button class="themed-button h-12" type="submit">Bevestigen</button>
                        </div>
                    </section>
                </div>
            </div>
        </form>
    </div>

    <!-- ADDING NEW LAPTOP -->
    <div v-if="creating && currentUser" class="creation-container flex flex-col justify-center m-5 items-center">
        <form @submit.prevent="createLaptop" class="w-full">
            <p class="text-2xl bold text-center">NIEUWE LAPTOP TOEVOEGEN</p>

            <div class="flex flex-wrap mt-10">
                <div class="w-full px-3 flex flex-col mb-3">
                    <label class="semi-bold" for="ean">EAN</label>
                    <input id="ean" type="text" placeholder="EAN (Europees Artikel Nummer)" maxlength="13"
                        v-model="newLaptop.ean">
                </div>
                <div class="w-full px-3 flex flex-col mb-3">
                    <label class="semi-bold" for="articleNr">Artikel Nr.</label>
                    <input id="articleNr" type="text" placeholder="Artikelnummer" maxlength="10" pattern="\d*"
                        v-model.trim="newLaptop.articleNr">
                </div>
                <div class="w-full px-3 flex flex-col mb-3">
                    <label class="semi-bold" for="brand">Merk</label>
                    <input id="brand" type="text" placeholder="Merk" maxlength="100" v-model="newLaptop.brand">
                </div>
                <div class="w-full px-3 flex flex-col">
                    <label class="semi-bold" for="model">Model</label>
                    <input id="model" type="text" placeholder="Type/Model" maxlength="255" v-model="newLaptop.model">
                </div>

                <section class="flex justify-end gap-3 items-center w-full mt-10 px-3">
                    <div>
                        <button class="themed-button alt h-12" type="button"
                            @click="creating = false; this.$emit('toggle-modal')">Annuleren</button>
                    </div>
                    <div>
                        <button class="themed-button alt h-12" type="reset">Resetten</button>
                    </div>
                    <div>
                        <button class="themed-button h-12" type="submit">Bevestigen</button>
                    </div>
                </section>
            </div>
        </form>
    </div>

    <!-- EDITING AN EXISTING LAPTOP -->
    <div v-if="editing && currentUser" class="creation-container flex flex-col justify-center m-5 items-center">
        <form @submit.prevent="editLaptop" class="w-full">
            <p class="text-2xl bold text-center">LAPTOP BEWERKEN</p>
            <p class="text-xl text-center">{{ laptop.brand }} - {{ laptop.ean }} </p>
            <p class="text-1xl text-center">{{ laptop.model }}</p>

            <div class="flex flex-wrap mt-10">
                <div class="w-full px-3 flex flex-col mb-3">
                    <label class="semi-bold" for="ean">EAN</label>
                    <input id="ean" type="text" placeholder="EAN (Europees Artikel Nummer)" maxlength="13"
                        :value="editedLaptop.ean" readonly>
                </div>
                <div class="w-full px-3 flex flex-col mb-3">
                    <label class="semi-bold" for="articleNr">Artikel Nr.</label>
                    <input id="articleNr" type="text" placeholder="Artikelnummer" maxlength="10"
                        v-model.lazy.trim="editedLaptop.articleNr">
                </div>
                <div class="w-full px-3 flex flex-col mb-3">
                    <label class="semi-bold" for="brand">Merk</label>
                    <input id="brand" type="text" placeholder="Merk" maxlength="100" v-model.lazy.trim="editedLaptop.brand">
                </div>
                <div class="w-full px-3 flex flex-col">
                    <label class="semi-bold" for="model">Model</label>
                    <input id="model" type="text" placeholder="Type/Model" maxlength="255"
                        v-model.lazy.trim="editedLaptop.model">
                </div>

                <div class="flex flex-row items-center mt-10 justify-between w-full px-3">
                    <section>
                        <button class="themed-button h-12" type="button" @click="deleteLaptop"><font-awesome-icon
                                icon="trash" size="1x" style="color: var(--white)" /> &nbsp;&nbsp;Verwijderen</button>
                    </section>
                    <section class="flex justify-end gap-1 items-center w-full">
                        <div>
                            <button class="themed-button alt h-12" type="button" @click="editing = false">Annuleren</button>
                        </div>
                        <div>
                            <button class="themed-button alt h-12" type="button" @click="resetLaptop">Resetten</button>
                        </div>
                        <div>
                            <button class="themed-button h-12" type="submit">Bevestigen</button>
                        </div>
                    </section>
                </div>

            </div>
        </form>
    </div>

    <!-- Error Notification -->
    <div v-if="!laptop && !error && !creating && !editing && !updating"
        class="flex justify-center items-center flex-col gap-10 m-5">
        <div class="loader"></div>
    </div>
    <div v-if="error" class="text-center">Probleem opgetreden bij het ophalen van de data...</div>
</template>

<script>
// import { Images } from"@/models/Images";
import { Laptop } from "@/models/Laptop";
import axios from "axios";
import Swal from "sweetalert2";

export default {
    inject: ["sessionService"],
    name: "LaptopDetails",
    props: {
        currentLaptop: Laptop,
        currentUser: Object,
    },
    emits: [
        'toggle-modal',
        'select-laptop',
        'request-reload',
    ],
    data() {
        return {
            test: false,
            laptop: null,
            laptopImage: null,
            error: false,

            // API
            users: [],
            locations: [],
            imageStatus: {
                'OLD': {
                    value: 'OLD',
                    description: 'OLD (Niet Up-to-date)'
                },
                'WIP': {
                    value: 'WIP',
                    description: 'WIP (Work In Progress)'
                },
                'DONE': {
                    value: 'DONE',
                    description: 'DONE (Image Gereed)'
                }
            },

            creating: false,
            newLaptop: {
                ean: '',
                articleNr: '',
                brand: '',
                model: '',
            },
            editing: false,
            editedLaptop: {
                ean: '',
                articleNr: '',
                brand: '',
                model: '',
            },
            updating: false,
            image: {
                user: this.sessionService.currentAccount !== null ? this.sessionService.currentAccount.name : '',
                location: this.sessionService.currentAccount !== null ? this.sessionService.currentAccount.location : '',
                status: this.currentLaptop && this.currentLaptop.image.status !== 'NONE' ? this.currentLaptop.image.status : '',
                notes: this.currentLaptop && this.currentLaptop.image.notes || '',
            },
        }
    },
    beforeMount() {
        if (this.$route.fullPath.includes("new") && this.currentUser) {
            this.creating = true;
        }
    },
    async mounted() {
        this.laptop = this.currentLaptop;
    },
    methods: {
        resetLaptop() {
            this.editedLaptop = JSON.parse(JSON.stringify(this.laptop));
        },
        getWeekNumber(d) {
            d = new Date(d);
            d.setHours(0, 0, 0, 0);
            d.setDate(d.getDate() + 3 - (d.getDay() + 6) % 7);
            var week1 = new Date(d.getFullYear(), 0, 4);
            return 1 + Math.round(((d.getTime() - week1.getTime()) / 86400000 - 3 + (week1.getDay() + 6) % 7) / 7);
        },
        getImageName() {
            const date = new Date;

            return `${this.laptop.brand} ${this.laptop.model} (${this.laptop.ean}) MM WIN11-10H1 WK${this.getWeekNumber(date)}-${date.getFullYear().toString()}`
        },
        async deleteImage() {
            try {
                const result = await Swal.fire({
                    title: 'Weet je het zeker?',
                    text: 'Je kunt dit niet ongedaan maken!',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: 'var(--theme)',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Ja, verwijderen!',
                    cancelButtonText: 'Nee, annuleer!',
                    reverseButtons: true
                });

                // If confirmed, proceed with deletion
                if (result.isConfirmed) {
                    await axios.delete(process.env.VUE_APP_API_URL + '/api/images/' + encodeURIComponent(this.laptop.image.id));

                    Swal.fire({
                        title: 'Verwijderd!',
                        text: 'De image is succesvol verwijderd.',
                        icon: 'success',
                        confirmButtonText: 'Oké',
                        confirmButtonColor: 'var(--theme)',
                        iconColor: 'var(--theme)',
                    });

                    this.$emit('toggle-modal');
                    this.$emit('request-reload');

                    this.$router.push('/laptops');
                }
            } catch (error) {
                this.showValidationError('Fout bij het verwijderen van de image: ' + error.message);
            }
        },
        async updateImage() {
            // Validation
            if (!this.image.user.trim()) {
                return this.showValidationError('Medewerker naam mag niet leeg zijn!');
            }
            if (!this.image.status) {
                return this.showValidationError('Status mag niet leeg zijn!');
            }


            // Prepare the request data
            const imageData = {
                laptop: {
                    ean: this.laptop.ean,
                    articleNr: this.laptop.articleNr,
                    brand: this.laptop.brand,
                    model: this.laptop.model
                },
                datetime: new Date().toISOString(),
                updated_by: {
                    id: this.currentUser.id
                },
                status: this.image.status,
                notes: this.image.notes || null // Notes or null if empty
            };

            // Execute the PUT request
            try {
                await axios.put(process.env.VUE_APP_API_URL + '/api/images/' + this.laptop.image.id, imageData);
                Swal.fire({
                    title: 'Gelukt!',
                    text: 'De image is succesvol aangepast.',
                    icon: 'success',
                    confirmButtonText: 'Ok',
                    confirmButtonColor: 'var(--theme)',
                    iconColor: 'var(--theme)',
                });
                this.updating = false;

                this.laptop.image.notes = this.image.notes;
                this.laptop.image.status = this.image.status;
                this.laptop.image.updated_by = this.currentUser;

                this.$emit('toggle-modal');
                this.$emit('request-reload');
            } catch (error) {
                this.showValidationError('Fout bij het bijwerken van de image: ' + error.message);
            }
        },
        async addImage() {
            // Validation
            if (!this.image.user.trim()) {
                return this.showValidationError('Medewerker naam mag niet leeg zijn!');
            }
            if (!this.image.status) {
                return this.showValidationError('Status mag niet leeg zijn!');
            }

            // Prepare the request data
            const imageData = {
                laptop: {
                    ean: this.laptop.ean,
                    articleNr: this.laptop.articleNr,
                    brand: this.laptop.brand,
                    model: this.laptop.model
                },
                datetime: new Date().toISOString(),
                updated_by: {
                    id: this.currentUser.id
                },
                status: this.image.status,
                notes: this.image.notes || null // Notes or null if empty
            };

            // Execute the POST request
            try {
                await axios.post(process.env.VUE_APP_API_URL + '/api/images/new', imageData);

                Swal.fire({
                    title: 'Gelukt!',
                    text: 'De image is succesvol toegevoegd.',
                    icon: 'success',
                    confirmButtonText: 'Oké',
                    confirmButtonColor: 'var(--theme)',
                    iconColor: 'var(--theme)',
                });
                this.updating = false;

                this.laptop.image.notes = this.image.notes;
                this.laptop.image.status = this.image.status;
                this.laptop.image.updated_by = this.currentUser;
                this.laptop.image.datetime = new Date().toISOString();

                this.$emit('toggle-modal');
                this.$emit('request-reload');
            } catch (error) {
                this.showValidationError('Fout bij het toevoegen van de image:' + error.message);
            }
        },
        async deleteLaptop() {
            // Confirmation dialog using Swal
            try {
                const result = await Swal.fire({
                    title: 'Weet je het zeker?',
                    text: 'Je kunt dit niet ongedaan maken!',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: 'var(--theme)',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Ja, verwijderen!',
                    cancelButtonText: 'Nee, annuleer!',
                    reverseButtons: true
                });

                // If confirmed, proceed with deletion
                if (result.isConfirmed) {
                    await axios.delete(process.env.VUE_APP_API_URL + '/api/laptops/' + encodeURIComponent(this.laptop.ean.toString().trim()));

                    Swal.fire({
                        title: 'Verwijderd!',
                        text: 'De laptop is succesvol verwijderd.',
                        icon: 'success',
                        confirmButtonText: 'Oké',
                        confirmButtonColor: 'var(--theme)',
                        iconColor: 'var(--theme)',
                    });

                    this.$emit('toggle-modal');
                    this.$emit('request-reload');

                    this.$router.push('/laptops');
                }
            } catch (error) {
                this.showValidationError('Fout bij het verwijderen van de laptop: ' + error.message);
            }
        },
        async editLaptop() {
            // Check for empty fields
            if (!this.editedLaptop.ean.toString().trim()) return this.showValidationError('EAN mag niet leeg zijn');
            if (!this.editedLaptop.articleNr.toString().trim()) return this.showValidationError('Artikelnummer mag niet leeg zijn');
            if (!this.editedLaptop.brand.trim()) return this.showValidationError('Merk mag niet leeg zijn');
            if (!this.editedLaptop.model.trim()) return this.showValidationError('Model mag niet leeg zijn');

            if (!/^\d{12,13}$/.test(this.editedLaptop.ean.toString().trim())) {
                return this.showValidationError('EAN moet een 13-cijferig nummer zijn');
            }

            if (isNaN(Number(this.editedLaptop.articleNr))) {
                return this.showValidationError('Artikelnummer moet een nummer zijn');
            }

            // If all inputs are validated, put the laptop to the backend
            try {
                await axios.put(process.env.VUE_APP_API_URL + '/api/laptops/' + encodeURIComponent(this.editedLaptop.ean.toString().trim()), this.editedLaptop);

                Swal.fire({
                    title: 'Gelukt!',
                    text: 'De laptop is succesvol geüpdatet.',
                    icon: 'success',
                    confirmButtonText: 'Oké',
                    confirmButtonColor: 'var(--theme)',
                    iconColor: 'var(--theme)',
                });
                this.laptop = this.editedLaptop;
                this.editing = false;

                this.$emit('toggle-modal');
                this.$emit('request-reload');
            } catch (error) {
                this.showValidationError('Fout bij het bijwerken van de laptop: ' + error.message);
            }
        },
        async createLaptop() {
            // Check for empty fields
            if (!this.newLaptop.ean.trim()) return this.showValidationError('EAN mag niet leeg zijn');
            if (!this.newLaptop.articleNr.trim()) return this.showValidationError('Artikelnummer mag niet leeg zijn');
            if (!this.newLaptop.brand.trim()) return this.showValidationError('Merk mag niet leeg zijn');
            if (!this.newLaptop.model.trim()) return this.showValidationError('Model mag niet leeg zijn');

            if (!/^\d{12,13}$/.test(this.newLaptop.ean.trim())) {
                return this.showValidationError('EAN moet een 13-cijferig nummer zijn');
            }

            if (isNaN(Number(this.newLaptop.articleNr))) {
                return this.showValidationError('Artikelnummer moet een nummer zijn');
            }

            // Check for duplicates
            try {
                const checkDuplicate = await axios.get(process.env.VUE_APP_API_URL + '/api/laptops/search/' + encodeURIComponent(this.newLaptop.ean.trim()));
                if (checkDuplicate.data.length > 0) {
                    return this.showValidationError('Er is al een laptop met dit EAN nummer.');
                }
            } catch (error) {
                return this.showValidationError('Fout bij het controleren op duplicaten: ' + error.message);
            }

            // If all inputs are validated, post the laptop to the backend
            try {
                await axios.post(process.env.VUE_APP_API_URL + '/api/laptops/new', this.newLaptop);

                Swal.fire({
                    title: 'Gelukt!',
                    text: 'De nieuwe laptop is succesvol aangemaakt.',
                    icon: 'success',
                    confirmButtonText: 'Oké',
                    confirmButtonColor: 'var(--theme)',
                    iconColor: 'var(--theme)',
                });
                this.creating = false;

                this.$emit('toggle-modal');
                this.$emit('request-reload');

                this.$router.push('/laptops');
            } catch (error) {
                this.showValidationError('Fout bij het aanmaken van de laptop: ' + error.message);
            }
        },
        showValidationError(message) {
            Swal.fire({
                title: 'Fout!',
                text: message,
                icon: 'error',
                confirmButtonText: 'Oké',
                confirmButtonColor: 'var(--theme)',
                iconColor: 'var(--theme)',
            });
        }
    }
}
</script>

<style scoped>
.creation-container {
    width: 100%;
}

.creation-container input,
.creation-container select,
.creation-container textarea {
    border-radius: 10px;
    padding: 8px 10px;
    box-sizing: border-box;
    background-color: #e0e0e0;
}

.creation-container form section {
    display: flex;
    flex-wrap: wrap;
    align-self: flex-end;
}

th,
td {
    padding: 10px 25px;
    text-align: center;
}

details>summary,
summary {
    list-style: none;
}

details>summary::-webkit-details-marker {
    display: none;
}

.themed-button.alt {
    background-color: var(--white);
    border: 2px solid var(--theme);
    color: var(--theme);
}

.table-container {
    overflow-y: scroll;
    max-height: 50vh;
}

thead {
    position: sticky;
    top: 0;
    width: 100%;
}

table {
    width: 100%;
    border-spacing: 0;
    border-collapse: separate;
    border-radius: 10px;
}

table tr td {
    border-top: 1px solid var(--border);
    border-bottom: 1px solid var(--border);
}

table tr th {
    border-top: 1px solid var(--border);
    border-bottom: 1px solid var(--border);
}

table tr th:first-child,
table tr td:first-child {
    border-left: 1px solid var(--border);
}

table tr th:last-child,
table tr td:last-child {
    border-right: 1px solid var(--border);
}

table tr th {
    background: var(--tablerow-odd);
}

table tr:first-child th:first-child {
    border-top-left-radius: 6px;
}

table tr:first-child th:last-child {
    border-top-right-radius: 6px;
}

table tr:last-child td:first-child {
    border-bottom-left-radius: 6px;
}

table tr:last-child td:last-child {
    border-bottom-right-radius: 6px;
}

th,
td {
    padding: 10px 25px;
    text-align: center;
}

th:last-child,
td:last-child {
    width: 150px;
}

tr {
    color: var(--textcolor);
    height: fit-content;
}

tr:nth-child(even) {
    background-color: var(--tablerow-odd);
}

tr:nth-child(odd) {
    background-color: var(--tablerow-even);
}

.status-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 5px;
}

.status-icon {
    width: 10px;
    height: 10px;
    border-radius: 50%;
}

.status-message {
    margin: 5px 0 0 0;
    user-select: none;
}

.green {
    background-color: var(--green);
}

.red {
    background-color: var(--red);
}

.editIcons a {
    margin-right: 3px;
}

.orange {
    background-color: var(--orange);
}

table {
    border: 1px solid var(--border);
    border-collapse: collapse;
    margin: 0;
    padding: 0;
    width: 100%;
    table-layout: fixed;
}

table caption {
    font-size: 1.5em;
    margin: .5em 0 .75em;
}

table tr th,
table tr td:first-child,
table tr td {
    border: unset;
    border-bottom: 1px solid var(--border);
}

table tr th:last-child,
table tr td:last-child {
    border-right: unset;
}

table tr {
    border: 1px solid var(--border);
    padding: .35em;
}

table th,
table td {
    padding: .625em;
    text-align: center;
}

table th {
    font-size: .85em;
    letter-spacing: .1em;
    text-transform: uppercase;
}

table {
    border: 0;
}

table caption {
    font-size: 1.3em;
}

table thead {
    border: none;
    clip: rect(0 0 0 0);
    height: 1px;
    margin: -1px;
    overflow: hidden;
    padding: 0;
    position: absolute;
    width: 1px;
}

table tr {
    border-radius: 10px;
    display: block;
}

table td {
    border-bottom: 1px solid var(--border);
    display: block;
    font-size: .8em;
    text-align: right;
}

th:last-child,
td:last-child {
    width: unset;
}

td.detail-btn {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.detail-btn {
    pointer-events: none;
}

.detail-btn details {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
}

summary.detail-btn,
.status-message {
    width: fit-content;
    margin: 0;
}

table td::before {
    content: attr(data-label);
    float: left;
    font-weight: bold;
    text-transform: uppercase;
}

table td:last-child {
    border-bottom: 0;
}

@media only screen and (max-width: 500px) {
    .creation-container {
        width: 100%;
    }

    .creation-container input,
    .creation-container select,
    .creation-container textarea {
        border-radius: 10px;
        padding: 8px 10px;
        box-sizing: border-box;
        background-color: #e0e0e0;
    }

    .creation-container form section {
        display: flex;
        flex-wrap: wrap;
        align-self: flex-end;
    }

    .creation-container form div div:last-child {
        display: flex;
        flex-direction: column;
        gap: 10px;
    }

    .creation-container form section div {
        width: 100%;
    }

    .creation-container form section button {
        width: 100%;
    }
}
</style>