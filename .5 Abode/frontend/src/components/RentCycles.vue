<template>
    <div class="rent-cycles">
        <div class="payment-nav">
            <a href="#" class="payment-nav-item" @click="goToPaymentHistory">
                    <i class="fas fa-list-ul"></i> View Payment History
                </a>
            <a href="#" class="payment-nav-item" @click="goToNewPayment">
                <i class="fas fa-address-card"></i> Submit New Payment
            </a>
        </div>
        <div class="cycles">
        <h3>Rent Cycles</h3>
          <b-table :data="allRentCycles">
        <template slot-scope="props">
          <b-table-column field="startDate" label="Start Date" width="125">{{ props.row.startDate }}</b-table-column>
          <b-table-column field="dueDate" label="Due Date" width="125">{{ props.row.dueDate }}</b-table-column>
          <b-table-column label="Balance" width="1">
            <span>
              <div class="block">
                <b-icon pack="fas" icon="dollar-sign" size="is-small"></b-icon>
                {{ props.row.balance}}.00
              </div>
            </span>
          </b-table-column>
          <b-table-column field="rentStatus" label="Rent Status" width="125">{{ props.row.rentStatus }}</b-table-column>
        </template>
      </b-table>
           
            <!-- <table>
                <th>Start Date</th>
                <th>Due Date</th>
                <th>Balance</th>
                <th>Status</th>
                <tr v-for="cycle in allRentCycles" :key="cycle.id" :value="cycle.id">
                    <td>{{cycle.startDate}}</td>
                    <td>{{cycle.dueDate}}</td>
                    <td>{{cycle.balance}}</td>
                    <td>{{cycle.rentStatus}}</td>
                </tr>
            </table> -->
        </div>
    </div>
</template>

<script>
import auth from '../auth';

export default {
    props: {
        currentLeaseId: Number
    },
    data() {
        return {
            allRentCycles: [],
            columns: [
                {
                    field: "startDate",
                    label: "Start Date"
                },
                {
                    field: "dueDate",
                    label: "Due Date"
                },
                {
                    field: "balance",
                    label: "Balance"
                },
                {
                    field: "rentStatus",
                    label: "Rent Status"
                }
            ]
        }
    },
    methods: {
        getAllRentCycles() {
            fetch('http://localhost:8080/api/rent/lease/'+this.currentLeaseId, {
                method: 'GET',
                headers: { Authorization: 'Bearer ' + auth.getToken() },
                credentials: 'same-origin'
            }).then (response => { if(response.ok) { return response.json(); }
                }).then(responseData => { this.allRentCycles = responseData; })
                    .catch(err => console.error(err));
        },
        goToPaymentHistory() {
            this.$emit("getPaymentHistory");
        },
        goToNewPayment() {
            this.$emit("newPayment");
        }
    },
    created() {
        this.getAllRentCycles();
    }
}
</script>

<style>

</style>