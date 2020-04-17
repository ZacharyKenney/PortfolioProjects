<template>
    <div>
        <br/>
        <h1 class="title">Active Maintenance Requests</h1>
        <div class="tile is-ancestor">
            
            <div class="tile is-4 is-vertical" v-for="request in assignedUncompletedRequests" :key="request.requestId">
                <div class="tile">
                    <div class="tile is-parent">
                        <article class="tile is-child box">
                            <p class="subtitle">
                                {{ request.propertyName }}
                                <br/>{{request.streetAddress }}
                                <br/>Unit #: {{ request.unitNumber }}
                            </p>
                            <p v-if="request.priority === 3">Low Priority</p>
                            <p v-if="request.priority === 2">Medium Priority</p>
                            <p v-if="request.priority === 1" style="font-weight: bold; color: red;">High Priority</p>
                            <p>Submitted on {{ request.dateRequested}}</p>
                            <br/>
                            <p style="font-weight: bold;">{{ request.requestDesc }}</p>
                            <br/>
                            <form>
                            <p>Assign or Update Status: </p>
                            <select v-model="request.assignedUserId">
                                <option v-for="user in allMaintenanceUsers" :key="user.id" :value="user.id">{{ user.firstName }}</option>
                            </select>
                            <select v-model="request.completed">
                                <option value="true">Completed</option>
                                <option value="false">Incomplete</option>
                            </select>
                            <button type="button" @click="editRequest(request)">Update</button>
                            </form>
                        </article>
                    </div>
                </div>
            </div>
        </div>
        <br/>
        <h1 class="title">Maintenance Workers</h1>
        <b-table :data="allMaintenanceUsers" :columns="columns"></b-table>

    </div>
</template>

<script>

import auth from '../auth';

export default {
  props: {
    user: Object
  },
  name: 'assign',
  data(){
    return {
      assignedUncompletedRequests: [],
      allMaintenanceUsers: [],
      currentRequest: {
       requestId: '',
       unitId: '',
       requestUserId: '',
       requestDesc: '',
       priority: '',
       dateRequested: '',
       assignedUserId: '',
       completed: ''
      },
      maintenanceRequest:{
       assignedUserId: '',
       completed: ''
     },
     columns: [
       {
         field: "firstName",
         label: "First Name"
       },
       {
         field: "lastName",
         label: "Last Name"
       },
       {
         field: "numberOfAssignedRequests",
         label: "# of Requests"
       },
       {
         field: "phoneNumber",
         label: "Phone Number"
       }
     ],
      assignmentErrors: true,
    };
  },
   methods: {
      toast(){
      this.$buefy.toast.open({
        message: 'Updated maintenance request!',
        type: 'is-success'})
     },
      getAllUncompletedRequests(){
        fetch('http://localhost:8080/api/Landlord/assignMaintenance', {
          method: 'GET',
          headers: {
            Authorization: 'Bearer ' + auth.getToken()
          },
          credentials: 'same-origin'
        })
        .then(response => {
          if(response.ok){
            return response.json();
          }
        }).then(responseData => {
          this.assignedUncompletedRequests = responseData;
        })
        .catch(err => console.error(err));
      },

   getAllMaintenanceUsers(){
     fetch('http://localhost:8080/api/users/maintenance', {
       method: 'GET',
       headers: {
          Authorization: 'Bearer ' + auth.getToken()
       },
       credentials: 'same-origin'
     })
     .then(response => {
          if(response.ok){
            return response.json();
   }
     }).then(responseData => {
       this.allMaintenanceUsers = responseData;
     })
     .catch(err => console.error(err));
   },
      editRequest(request) {
         request.completed = request.completed === "true";
        fetch('http://localhost:8080/api/Landlord/assignMaintenance', {
          method: 'PUT',
          headers: {
            Authorization: 'Bearer ' + auth.getToken(),
            'Content-Type': 'application/json'
          },
          credentials: 'same-origin',
          body: JSON.stringify(request)
        })
        .then(() => {
          console.log('Updated');
          this.toast();
        })
        
      }
   },
   created(){
     this.getAllUncompletedRequests();
     this.getAllMaintenanceUsers();
   }

 }
 

</script>

<style scoped>
.is-ancestor{
  flex-wrap: wrap;
}
.bold {
    font-weight: bold;
}

</style>