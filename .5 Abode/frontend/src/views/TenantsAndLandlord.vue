<template>
  <div>
    <h1 class="title">Renter Details</h1>
    <!-- <div>
      <label for="propertyName">Property:</label>
      <select id="propertyName" v-model="selectedPropertyName">
        <option value disabled selected>Select a Property</option>
        <option
          v-for="propName in getUniquePropNames()"
          :key="propName"
          :value="propName"
        >{{propName}}</option>
      </select> -->
      <b-table :data="leases">
        <template slot-scope="props">
          <b-table-column field="propertyName" label="Property Name" width="125" :searchable="true">{{ props.row.propertyName }}</b-table-column>
          <b-table-column field="unitNumber" label="Unit #" width="125">{{ props.row.unitNumber }}</b-table-column>
          <b-table-column field="renterName" label="Renter Name" width="125">{{ props.row.renterName }}</b-table-column>
          <b-table-column field="renterEmail" label="Renter Email" width="125">{{ props.row.renterEmail }}</b-table-column>
          <b-table-column field="currentRentStatus" label="Rent Status" width="125">{{ props.row.currentRentStatus }}</b-table-column>

          <b-table-column label="Monthly Rent" width="125">
            <span>
              <div class="block">
                <b-icon pack="fas" icon="dollar-sign" size="is-small"></b-icon>
                {{ props.row.rentAmount}}.00
              </div>
            </span>
          </b-table-column>
          <b-table-column label="Overdue Balance">
            <span>
              <div class="block">
                <b-icon pack="fas" icon="dollar-sign" size="is-small" width="125"></b-icon>
                {{ props.row.overdueBalance}}.00
              </div>
            </span>
          </b-table-column>
        </template>
      </b-table>
    </div>
</template>

<script>
import auth from '../auth';
export default {
   props: {
    user: Object
  },
data () {
  return {
            leases: [],
            columns: [
                {
                  field: "propertyName",
                  label: "Property",
                  searchable: true
                },
                {
                field: "unitNumber",
                label: "Unit #",
                searchable: true,
                width: 75,
                },
              {
                field: "renterName",
                label: "Renter",
                searchable: true,
                width: 150,
              },
              {
                field: "renterEmail",
                label: "Renter Email",
                searchable: true
              },
              {
                field: "currentRentStatus",
                label: "Rent Status",
                searchable: true,
                width: 125,
                centered: true
              },
              {
                field: "rentAmount",
                label: "Monthly Rent",
                searchable: true,
                width: 125,
                centered: true
              },
              
              {
                field: "overdueBalance",
                label: "Balance",
                searchable: true,
                width: 125,
                centered: true
              }


            ]
          }},
    methods: {
        getLeasesForLandlord() {
            fetch('http://localhost:8080/api/leases/landlord/' + this.user.id, {
              method: 'GET',
              headers: {
                Authorization: 'Bearer ' + auth.getToken()
              },
              credentials: 'same-origin'
            })
            .then (response => {
              if(response.ok) {
                  return response.json();
              }
            }).then(responseData => {
              this.leases = responseData;
            });
                                },
        getUniquePropNames() {
          let uniqueNames = [];
          this.leases.forEach(lease => {
            if(!uniqueNames.includes(lease.propertyName)){
              uniqueNames.push(lease.propertyName);
            }
          });
          return uniqueNames;
                            },
        getLeasesByProperty() {
          return this.leases.filter(lease => {
            return lease.propertyName === this.selectedPropertyName; 
          })
                             }
            },
   created() {
        this.getLeasesForLandlord(this.user.sub);
             }
        }


</script>

<style>
</style>