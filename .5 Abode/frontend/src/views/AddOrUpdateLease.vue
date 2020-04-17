<template>
    <div class="container is-fluid" id="addOrUpdateLease">
        <div class="content">
            <h1 class="title"> Lease Management Console</h1>
            <br/>
            <section class="form">
                <form>
                    <div>
                        <b-field>
                            <b-radio name="addOrUpdate"
                                checked
                                v-model="newLease"
                                :native-value="true"
                                @input="resetLeaseValues">
                                Add New Lease
                            </b-radio>
                            <b-radio name="addOrUpdate"
                                v-model="newLease"
                                :native-value="false"
                                @input="resetLeaseValues">
                                Update Existing Lease
                            </b-radio>
                        </b-field>
                    </div>
                    <br/>

                    <!-- Prompt for Lease when 'Updating Existing Lease' chosen above -->
                    <div v-show="!newLease">
                        <b-field grouped>
                            <b-field label="Lease ID">
                                <b-select v-model="currentLease.leaseId" @input="getCurrentLease" placeholder="Select a Lease">
                                    <option v-for="lease in leasesForLandlord" :key="lease.leaseId" :value="lease.leaseId">
                                        {{ `${lease.leaseId}: ${lease.propertyName} - ${lease.unitNumber} (${lease.status })` }}
                                    </option>
                                </b-select>
                            </b-field>
                            <b-field v-show="currentLease.leaseId != ''" label="Lease Status">
                                <b-select v-model="currentLease.status" placeholder="Select a name">
                                    <option>Active</option>
                                    <option>Expired</option>
                                    <option>Terminated</option>
                                </b-select>
                            </b-field>
                        </b-field>
                    </div>

                    <!-- Prompt for Property and Unit for new leases only -->
                    <div v-show="newLease">
                        <b-field grouped>
                            <b-field label="Property">
                                <b-select v-model="selectedPropertyId" placeholder="Select a Property">
                                <!-- shows only the properties for current Landlord user -->
                                    <option v-for="prop in propertiesForLandlord" 
                                            :key="prop.propertyId" 
                                            :value="prop.propertyId">
                                        {{ prop.propertyName }}
                                    </option>
                                </b-select>
                            </b-field>

                            <b-field label="Unit">
                                <b-select v-model="currentLease.unitId" placeholder="Select a Unit Number">
                                <!-- shows only the Units for the Property selected above -->
                                    <option v-for="unit in getUnitsForSelectedProperty" 
                                            :key="unit.unitId" 
                                            :value="unit.unitId">
                                        {{ unit.unitNumber }}
                                    </option>
                                </b-select>
                            </b-field>
                        </b-field>
                    </div>

                    <!-- Show remaining fields for new leases, or when an existing lease has been selected -->
                    <div v-show="currentLease.unitId !== ''">

                        <b-field label="Renter Name">
                            <b-select v-model="currentLease.userId" placeholder="Select a Renter" icon="account">
                            <!-- shows all Renter users -->
                                <option v-for="renter in allRenters"
                                        :key="renter.id"
                                        :value="renter.id">
                                    {{ `${renter.lastName}, ${renter.firstName} (${renter.email})` }}
                                </option>
                            </b-select>
                        </b-field>
                        
                        <b-field grouped>
                            <b-field label="Signed Date">
                                <b-datepicker
                                    v-model="currentLease.signedDate"
                                    placeholder="Type or select a date..."
                                    icon="calendar-today"
                                    editable
                                    >
                                </b-datepicker>
                            </b-field>
                            <b-field label="Start Date">
                                <b-datepicker
                                    v-model="currentLease.startDate"
                                    placeholder="Type or select a date..."
                                    icon="calendar-today"
                                    editable
                                    >
                                </b-datepicker>
                            </b-field>
                        </b-field>

                        <b-field grouped>

                        <!-- Landlord can specify any positive integer value -->
                            <b-field label="Lease Term (Months)">
                                <b-numberinput min="1" max="99999" v-model="currentLease.rentLength" controls-position="compact">
                                </b-numberinput>
                            </b-field>

                            <!-- Landlord can specify any positive dollar value -->
                            <b-field label="Rent Amount">
                                <b-numberinput min="0" max="99999" v-model="currentLease.rentAmount" controls-position="compact">
                                </b-numberinput>
                            </b-field>

                            <!-- Landlord can specify any positive value between 1-100 -->
                            <b-field label="Late Fee %">
                                <b-numberinput min="0" max="100" v-model="currentLease.lateFee" controls-position="compact">
                                </b-numberinput>
                            </b-field>
                        </b-field>

                    </div>
                    <div >
                        <b-field v-show="isValidForm">
                            <b-button type="is-primary" v-show="newLease"  @click.prevent="createLease">Submit New Lease</b-button>
                            <b-button type="is-primary" v-show="!newLease" @click.prevent="updateLease">Submit Updates</b-button>
                        </b-field>
                    </div>
                </form>
            </section>
            <br/><br/>
            <section class="existing-leases">
                <h2 class="subtitle">All Existing Leases:</h2>
                  <b-table :data="leasesForLandlord">
        <template slot-scope="props">
          <b-table-column field="propertyName" label="Property Name" width="125" :searchable="true">{{ props.row.propertyName }}</b-table-column>
          <b-table-column field="unitNumber" label="Unit #" width="125" :searchable="true">{{ props.row.unitNumber }}</b-table-column>
          <b-table-column field="status" label="Status" width="125" :searchable="true">{{ props.row.status }}</b-table-column>
          <b-table-column field="signedDate" label="Signed Date" width="125" :searchable="true">{{ props.row.signedDate }}</b-table-column>
          <b-table-column field="rentLength" label="Term (Months)" width="125">{{ props.row.rentLength }}</b-table-column>

          <b-table-column label="Monthly Rent" width="125">
            <span>
              <div class="block">
                <b-icon pack="fas" icon="dollar-sign" size="is-small"></b-icon>
                {{ props.row.rentAmount}}.00
              </div>
            </span>
          </b-table-column>
          <b-table-column field="renterName" label="Renter Name" :searchable="true">
              {{props.row.renterName}}
              
            
          </b-table-column>
        </template>
      </b-table>
            </section>

        </div>
    </div>
</template>

<script>
import auth from '../auth';

export default {
    props: {
        user: Object
    },
    data() {
        return {
            newLease: true,
            propertiesForLandlord: [],
            selectedPropertyId: '',
            allRenters: [],
            leasesForLandlord: [],
            currentLease: {
                leaseId: 0,
                userId: 0,
                unitId: '',
                signedDate: new Date(),
                startDate: new Date(),
                rentLength: 1,
                rentAmount: 0,
                lateFee: 0,
                status: 'Active'
            },
            columns: [
                {
                    field: 'propertyName',
                    label: 'Property',
                    width: '250',
                    searchable: true,
                },
                {
                    field: 'unitNumber',
                    label: 'Unit',
                    width: '100',
                    searchable: true,
                },
                {
                    field: 'status',
                    label: 'Status',
                    width: '100',
                    searchable: true,
                },
                {
                    field: 'signedDate',
                    label: 'Signed',
                    width: '125',
                    searchable: true,
                },
                {
                    field: 'rentLength',
                    label: 'Term (Months)',
                    width: '100',
                    numeric: true,
                    centered: true,
                },
                {
                    field: 'rentAmount',
                    label: 'Rent',
                    width: '100',
                    numeric: true,
                    centered: true,
                },
                {
                    field: 'renterName',
                    label: 'Renter Name',
                    width: '250',
                    searchable: true,
                }
            ]
        }
    },
    computed: {
        getUnitsForSelectedProperty() {
            let selectedProp = {};
            this.propertiesForLandlord.forEach((property) => {
                if (property.propertyId === this.selectedPropertyId) {
                    selectedProp = property;
                }
            });
            return selectedProp.units;
        },
        isValidForm() {
            return (
                this.currentLease.userId != 0 &&
                this.currentLease.unitId != "" &&
                this.currentLease.signedDate != "" &&
                this.currentLease.rentLength != 0 &&
                this.currentLease.rentAmount != 0
            );
        }
    },
    methods: {
        getLeasesForLandlord() {
            fetch('http://localhost:8080/api/leases/landlord/'+this.user.id, {
                method: 'GET',
                headers: { Authorization: 'Bearer ' + auth.getToken() },
                credentials: 'same-origin'
            }).then (response => { if(response.ok) { return response.json(); }
                }).then(responseData => { this.leasesForLandlord = responseData; })
                    .catch(err => console.error(err));
        },
        getPropertiesForLandlord() {
            fetch('http://localhost:8080/api/properties/landlord/'+this.user.id, {
                method: 'GET',
                headers: { Authorization: 'Bearer ' + auth.getToken() },
                credentials: 'same-origin'
            }).then (response => { if(response.ok) { return response.json(); }
                }).then(responseData => { this.propertiesForLandlord = responseData; })
                    .catch(err => console.error(err));
        },
        getAllRenters() {
            fetch('http://localhost:8080/api/users/renters', {
                method: 'GET',
                headers: { Authorization: 'Bearer ' + auth.getToken() },
                credentials: 'same-origin'
            }).then (response => { if(response.ok) { return response.json(); }
                }).then(responseData => { this.allRenters = responseData; })
                    .catch(err => console.error(err));
        },
        getCurrentLease() {
            this.leasesForLandlord.forEach((lease) => {
                if (lease.leaseId === this.currentLease.leaseId) {
                    this.currentLease.userId = lease.userId;
                    this.currentLease.unitId = lease.unitId;
                    this.currentLease.signedDate = new Date(lease.signedDate);
                    this.currentLease.rentLength = lease.rentLength;
                    this.currentLease.rentAmount = lease.rentAmount;
                    this.currentLease.lateFee = lease.lateFee;
                    this.currentLease.status = lease.status;
                }
            });         
        },
        createLease() {
            console.log(JSON.stringify(this.currentLease));
            fetch('http://localhost:8080/api/leases', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + auth.getToken(),
                    'Content-Type': 'application/json'
                },
                credentials: 'same-origin',
                body: JSON.stringify(this.currentLease)
            }).then(response => {
                if (response.ok) {
                    this.newToast();
                    this.resetLeaseValues();
                }
            }).then(this.getLeasesForLandlord())
                .catch(err => console.error(err));
        },
        updateLease() {
            console.log(JSON.stringify(this.currentLease));
            fetch('http://localhost:8080/api/leases/'+this.currentLease.leaseId, {
                method: 'PUT',
                headers: {
                    Authorization: 'Bearer ' + auth.getToken(),
                    'Content-Type': 'application/json'
                },
                credentials: 'same-origin',
                body: JSON.stringify(this.currentLease)
            }).then(response => {
                if (response.ok) {
                    this.updateToast();
                    this.resetLeaseValues();
                }
            }).then(this.getLeasesForLandlord())
                .catch(err => console.error(err));
        },
        resetLeaseValues() {
            this.currentLease.userId = 0;
            this.currentLease.unitId = '';
            this.currentLease.signedDate = new Date();
            this.currentLease.rentLength = 1;
            this.currentLease.rentAmount = 0;
            this.currentLease.lateFee = 0;
            this.currentLease.status = 'Active';

            // pull in updates to lease table
            this.getLeasesForLandlord();
        },
         newToast(){
      this.$buefy.toast.open({
        message: 'Lease Created',
        type: 'is-success'})
     },
      updateToast(){
      this.$buefy.toast.open({
        message: 'Lease Updated',
        type: 'is-success'})
     }
    },
    created() {
        this.getLeasesForLandlord();
        this.getPropertiesForLandlord(this.user.id);
        this.getAllRenters();
    }
}
</script>

<style scoped>

.content {
    margin: 2%;
}


</style>