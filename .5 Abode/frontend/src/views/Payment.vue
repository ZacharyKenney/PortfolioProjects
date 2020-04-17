<template>
  <div class="payments">
    <h1 class="title">Manage Your Rent Payments</h1>
    <div class="overview">
        <b-message class="balance"
                    :type="overdueBalance > 0 ? 'is-danger' : 'is-success'">
            Overdue Balance: ${{ overdueBalance }}
        </b-message>
        <b-message class="balance">
            Current Balance: ${{ currentBalance }}
        </b-message>
    </div>
    <div class="content">
      <rent-cycles
        v-if="!showNewPaymentForm && !showPaymentHistory"
        @getPaymentHistory="getPaymentHistory"
        @newPayment="newPayment"
        :currentLeaseId="currentLeaseId"
      />
      <payment-history
        v-if="!showNewPaymentForm && showPaymentHistory"
        @getRentCycles="getRentCycles"
        @newPayment="newPayment"
        :currentLeaseId="currentLeaseId"
      />
      <new-payment
        v-if="showNewPaymentForm && !showPaymentHistory"
        @getPaymentHistory="getPaymentHistory"
        @getRentCycles="getRentCycles"
        :currentLeaseId="currentLeaseId"
        :user="user"
      />
    </div>
  </div>
</template>

<script>
import auth from '../auth';
import PaymentHistory from "@/components/PaymentHistory.vue";
import RentCycles from "@/components/RentCycles.vue";
import NewPayment from "@/components/NewPayment.vue";

export default {
  props: {
    user: Object
  },
  components: {
    PaymentHistory,
    RentCycles,
    NewPayment
  },
  data() {
    return {
        showPaymentHistory: false,
        showNewPaymentForm: true,
        currentLeaseId: 0,
        overdueBalance: 0,
        currentBalance: 0
    };
  },
  methods: {
    getCurrentLease() {
        fetch('http://localhost:8080/api/leases/active/renter/'+this.user.id, {
            method: 'GET',
            headers: { Authorization: 'Bearer ' + auth.getToken() },
            credentials: 'same-origin'
        }).then (response => { if(response.ok) { return response.json(); }
            }).then(responseData => { 
                    this.currentLeaseId = responseData.leaseId;
                    this.overdueBalance = responseData.overdueBalance;
                    fetch('http://localhost:8080/api/rent/currentlease/'+responseData.leaseId, {
                        method: 'GET',
                        headers: { Authorization: 'Bearer ' + auth.getToken() }
                    }).then (response => { if(response.ok) { return response.json(); }
                        }).then(responseData => { 
                                this.currentBalance = responseData.balance;
                            })
            })
            .catch(err => console.error(err));
    },
    newPayment() {
        this.showNewPaymentForm = true;
        this.showPaymentHistory = false;
    },
    getPaymentHistory() {
        this.showNewPaymentForm = false;
        this.showPaymentHistory = true;
        this.getCurrentLease();
    },
    getRentCycles() {
        this.showNewPaymentForm = false;
        this.showPaymentHistory = false;
    }
  },
  created() {
        this.getCurrentLease();
  }
};
</script>

<style>
.overview {
    height: 200px;
}
.balance {
    width: 250px;
    font-weight: bold;
}
.payment-nav-item {
    margin: 10px;

}
.payment-nav {
    height: 50px;
}
</style>