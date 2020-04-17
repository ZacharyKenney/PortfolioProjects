<template>
  <div class="container is-fluid">
      <h1 class="title">Maintenance Request</h1>
     
    <form>
      <p><strong>Unit</strong> </p> 
      <p>{{userUnitNumber}}</p>
      <br>
      <b-field label="Details">
      <b-input  type="textarea" v-model="maintenanceRequest.requestDesc"></b-input>
      </b-field>
       <div class="block">
         <b-field label="Priority Level">
                    </b-field>
            <b-radio v-model="maintenanceRequest.priority"
                name="name"
                native-value="3">
                Low
            </b-radio>
            <b-radio v-model="maintenanceRequest.priority"
                name="name"
                native-value="2">
                Medium
            </b-radio>
            <b-radio v-model="maintenanceRequest.priority"
                name="name"
                native-value="1">
                High
            </b-radio>
       
        </div>
      <b-button type="is-primary" native-type="submit" v-bind:disabled="!isValidForm" v-on:click="createRequest">
        Submit Request</b-button>
    </form>
  </div>
</template>

<script>
import auth from '../auth';


export default {
  props: {
    user: Object
  },
 data (){
   return {
     
     userUnitNumber: "",
     maintenanceRequest:{
       unitId: "",
      requestUserId: "",
       requestDesc: "",
       priority: ""
     }
     };
   },
   methods: {
     toast(){
      this.$buefy.toast.open({
        message: 'Maintenance request submitted!',
        type: 'is-success'})
     },
     createRequest(){
       console.log('starting post method...');
        fetch('http://localhost:8080/api/maintenance/request',{
         method: 'POST',
        headers: {
            Authorization: 'Bearer ' + auth.getToken(),
            'Content-Type': 'application/json'
          },
         body: JSON.stringify(this.maintenanceRequest)
       })
        .then(response => {
          if (response.ok) {
           console.log('maint request post method worked...YEEHAWW!');
           this.toast();
          }
        })
        .catch(err => console.error(err));
     
     },
         getUnitForUser(id) {
      fetch("http://localhost:8080/api/unit/renter/"+id, {
        method: 'GET',
          headers: {
            Authorization: 'Bearer ' + auth.getToken(),
            'Content-Type': 'application/json'
          },
          credentials: 'same-origin'
      })
        .then(response => {
          if(response.ok){
            return response.json();
          }
        })
        .then(responseData => {
          this.maintenanceRequest.requestUserId = id;
          this.maintenanceRequest.unitId = responseData[0].unitId;
          this.userUnitNumber = responseData[0].unitNumber;
          console.log(responseData[0].unitId);
        })
    }
   },
   computed: {
     isValidForm(){
       return (
       this.maintenanceRequest.unitId != "" &&
       this.maintenanceRequest.requestDescription != "" &&
       this.maintenanceRequest.priority != "" 
       );
     }
   },
   created(){
     this.getUnitForUser(this.user.id);
   }
 };
</script>

<style>
</style>