<template>
    <div class="table-wrapper">
        <div class="table-container">
            <div v-if="laptops">
                <table class="colored">
                    <thead>
                        <tr>
                            <th v-for="column in columns" :key="column.sortKey" @click="requestSort(column.sortKey)"
                                :class="{ 'active': currentSort.key == column.sortKey }">
                                {{ column.name }}
                                <font-awesome-icon v-if="currentSort.key === column.sortKey"
                                    :icon="['fas', currentSort.isAsc ? 'arrow-up' : 'arrow-down']"
                                    class="text-color ml-1" />
                            </th>
                            <th v-if="adminActive">Edit</th>
                        </tr>
                    </thead>
                    <tbody v-if="laptops.length > 0">
                        <tr v-for="laptop in laptops" :key="laptop.id" :id="laptop.ean">
                            <td data-label="EAN" @click="showDetails(laptop.ean, $event)">{{ laptop.ean }}</td>
                            <td data-label="Art. Nr.">{{ laptop.articleNr }}</td>
                            <td data-label="Merk">{{ laptop.brand }}</td>
                            <td data-label="Model">{{ laptop.model }}</td>
                            <td data-label="Status" class="detail-btn">
                                <details>
                                    <summary class="detail-btn">
                                        <div :hidden="mobile">
                                            <div v-if="laptop.image.status === 'DONE'" class='status-icon green'>
                                            </div>
                                            <div v-if="laptop.image.status === 'WIP'" class='status-icon orange'>
                                            </div>
                                            <div v-if="laptop.image.status === 'NONE' || laptop.image.status === 'OLD'"
                                                class='status-icon red'>
                                            </div>
                                        </div>
                                    </summary>
                                    <div class="status-wrapper">
                                        <div :hidden="!mobile">
                                            <div v-if="laptop.image.status === 'DONE'" class='status-icon green'>
                                            </div>
                                            <div v-if="laptop.image.status === 'WIP'" class='status-icon orange'>
                                            </div>
                                            <div v-if="laptop.image.status === 'NONE' || laptop.image.status === 'OLD'"
                                                class='status-icon red'>
                                            </div>
                                        </div>
                                        <p v-if="laptop.image.status === 'DONE'" class="status-message" :id="laptop.ean">
                                            Done.
                                        </p>
                                        <p v-if="laptop.image.status === 'WIP'" class="status-message" :id="laptop.ean">Work
                                            in
                                            progress.</p>
                                        <p v-if="laptop.image.status === 'NONE' || laptop.image.status === 'OLD'"
                                            class="status-message" :id="laptop.ean">
                                            Outdated.</p>
                                    </div>
                                </details>
                            </td>
                            <td v-if="adminActive" class="editIcons">
                                <a><i class="fa-solid fa-pen-to-square"></i></a>
                                <a><i class="fa-solid fa-trash"></i></a>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div v-if="laptops.length < 1 && searchInput !== ''" class="m-10 text-center">
                    <h4>Er zijn geen resultaten met je huidige zoekopdracht en of filters.</h4>
                    <button class="flex m-auto themed-button mt-5" @click="$emit('request-search')">Toevoegen vanuit
                        database</button>
                </div>
                <div v-else-if="laptops.length < 1" class="m-10 text-center">
                    <h4>Er zijn geen laptops die geladen kunnen worden op het moment.</h4>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import router from '@/router';

export default {
    name: 'LaptopTable',
    props: {
        laptops: Array,
        adminActive: Boolean,
        currentSort: Object,
        toggleDetails: Boolean,
        mobile: Boolean,
        searchInput: String
    },
    emits: [
        'toggle-modal',
        'select-laptop',
        'sort-requested',
        'request-search'
    ],
    data() {
        return {
            columns: [
                { name: 'EAN', sortKey: 'ean' },
                { name: 'Art. Nr.', sortKey: 'articleNr' },
                { name: 'Merk', sortKey: 'brand' },
                { name: 'Model', sortKey: 'model' },
                { name: 'Status', sortKey: 'image.status' }
            ],
            modal: null,
        }
    },
    updated() {
        this.toggleStatusMsg();
    },
    mounted() {
        this.$nextTick(() => {
            if (this.mobile) this.toggleStatusMsg();
            this.modal = document.querySelector('.scan-modal');
        });
    },
    methods: {
        requestSort(key) {
            this.$emit('sort-requested', key);
        },
        toggleStatusMsg() {
            document.querySelectorAll('details').forEach((details) => {
                details.open = this.toggleDetails;
            });
        },
        showDetails(ean) {
            this.$emit('toggle-modal', 'details');
            this.$emit('select-laptop', ean);
            router.push("/laptops/" + ean);
        }
    },
}
</script>

<style scoped>
details>summary,
summary {
    list-style: none;
}

details>summary::-webkit-details-marker {
    display: none;
}

.table-wrapper {
    border: 1px solid var(--border);
    border-radius: 10px;
    overflow: hidden;
}

.table-container {
    width: 100%;
    min-width: 65vw;
    overflow-y: scroll;
    overflow-x: hidden;
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
    min-width: 400px;
    border-collapse: separate;
}

table tbody tr:hover {
    background-color: var(--gray);
}

table tr:first-child th {
    border-bottom: 1px solid var(--border);
}

table tr th,
table tr td {
    width: 150px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

table tr th {
    background: var(--tablerow-odd);
    user-select: none;
}

table tr th.active {
    border-bottom: 1px solid black;
}

.sort-arrow.asc::after {
    content: ' ↑';
}

.sort-arrow.desc::after {
    content: ' ↓';
}

th,
td {
    padding: 10px 0;
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
    background-color: var(--tablerow-even);
}

tr:nth-child(odd) {
    background-color: var(--tablerow-odd);
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
    margin: auto;
}

.status-icon:hover {
    cursor: pointer;
}

.status-message {
    margin: 5px 0 0 0;
    user-select: none;
}

.editIcons a {
    margin-right: 3px;
}

*.hovered,
tr:nth-child(even).hovered,
tr:nth-child(odd).hovered {
    background-color: var(--gray);
}

@media screen and (min-width: 740px) {
    table tbody tr td:not(table tbody tr:last-child td) {
        border-bottom: 1px solid var(--border);
    }
}

@media screen and (max-width: 740px) {
    .table-wrapper {
        border: none;
        border-radius: unset;
    }

    table {
        border: 1px solid var(--border);
        border-collapse: collapse;
        margin: 0;
        padding: 0;
        width: 100%;
        min-width: unset;
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
        max-width: unset;
        width: unset;
        border-bottom: 1px solid var(--border);
    }

    table tr th:last-child,
    table tr td:last-child {
        border-right: unset;
    }

    table tr {
        display: block;
        border: 1px solid var(--border);
        border-radius: 10px;
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

    table tr:not(table tr:last-child) {
        margin-bottom: .625em;
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
}
</style>
