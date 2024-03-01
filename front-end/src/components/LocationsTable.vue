<template>
  <div class="table-container mt-5">
    <div v-if="locations">
      <table class="colored">
        <tr>
          <th>Location</th>
          <th>Acties</th>
        </tr>
        <tr v-for="location in locations" :key="location.name">
          <td>{{ location.name }}</td>
          <td>
            <font-awesome-icon
                icon="trash"
                size="lg"
                @click="confirmDelete(location.name)" />
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Swal from 'sweetalert2';

export default {
  name: 'LocationsTable',
  props: ['locations'],
  methods: {
    async confirmDelete(name) {
      Swal.fire({
        title: 'Bevestiging',
        text: `Weet je zeker dat je de locatie: ${name} wilt verwijderen?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: 'var(--theme)',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Ja, verwijder locatie!',
        cancelButtonText: 'Nee, behoud locatie'
      }).then(async (result) => {
        if (result.isConfirmed) {
          try {
            await axios.delete(`http://localhost:8080/api/locations/delete/${name}`);
            this.$emit('location-deleted', name);
          } catch (error) {
            console.error('Error deleting location:', error);
            Swal.fire('Error', 'U kunt geen vestiging verwijderen wanneer er gebruikers geregistreerd staan op deze vestiging.', 'error');
          }
        }
      });
    },
  },
};
</script>
<style scoped>
table {
    border-spacing: 0;
    min-width: 350px;
    border-collapse: separate;
    border-radius: 10px;
}

table tr th,
table tr td {
    border-right: 1px solid var(--border);
    border-bottom: 1px solid var(--border);
}

table tr th:first-child,
table tr td:first-child {
    border-left: 1px solid var(--border);
}

table tr th {
    background: var(--tablerow-odd);
    border-top: 1px solid var(--border);
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
    padding: 10px 25px;
    text-align: center;
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

.delete-icon {
    width: 3vw;
    height: 3vh;
    margin: auto;
}

@media only screen and (max-width: 700px) {
    .table-container {
        overflow-x: auto;
        width: 100%;
    }
}
</style>