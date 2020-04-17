<template>
    <div>
        <h1 class="title">Active Maintenance Requests</h1>
        <div class="tile is-ancestor">
            <div class="tile is-4 is-vertical" v-for="request in assignedUncompletedRequests" :key="request.RequestId">
                <div class="tile">
                    <div class="tile is-parent">
                        <article class="tile is-child box">
                            <p class="subtitle">{{ request.propertyName }}
                            <br/>{{ request.streetAddress }} {{ request.city }} {{ request.state }} {{ request.zipCode }}
                            <br/>Unit #: {{ request.unitNumber }}</p>
                            <p v-if="request.priority === 3">Low Priority</p>
                            <p v-if="request.priority === 2">Medium Priority</p>
                            <p v-if="request.priority === 1" style="font-weight: bold; color: red;">High Priority</p>
                            <p>Submitted on {{ request.dateRequested}}</p>
                            <p>Requested by {{ request.requesterFullName }}</p>
                            <p>{{ request.requesterEmail }}</p>
                            <br/>
                            <p style="font-weight: bold;">{{ request.requestDesc }}</p>
                            <br/>
                    
                            <form>
                                <label for="priority"> Change Priority Level </label>
                                <select id="priority" v-model="request.priority">
                                    <option value="3">Low</option>
                                    <option value="2">Medium</option>
                                    <option value="1">High</option>
                                </select>
                                    <br>
                                <label> Change Status </label>
                                <select v-model="request.completed">
                                    <option value="true">Completed</option>
                                    <option value="false">Incomplete</option>
                                </select>
                                <button type="button" @click="editRequestStatus(request)">Submit</button>
                            </form>

                        </article>
                    </div>
                </div>
            </div>
        </div>  
        <br/>
        <h1 class="title">Completed Requests</h1>
        <b-table :data="assignedCompletedRequests" :columns="columns"></b-table>  
    </div>
</template>

<script>

import auth from '../auth';

export default {
  props: {
    user: Object
  },
  data(){
    return {
      assignedUncompletedRequests: [],
      assignedCompletedRequests: [],
      columns: [
          {
             field: "propertyName",
            label: "Property"
         },
         {
             field: "streetAddress",
            label: "Address"
         },
         {
             field: "unitNumber",
            label: "Unit #"
         },
        {
          field: "requestDesc",
          label: "Problem"
        },
        {
          field: "dateRequested",
          label: "Date Requested"
        }
      ]
    }
  },

methods: {
   toast(){
      this.$buefy.toast.open({
        message: 'Updated maintenance request!',
        type: 'is-success'})
     },
  viewAllCompletedRequests(){
    fetch('http://localhost:8080/api/maintenance/completed/' + this.user.id, {
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
      this.assignedCompletedRequests = responseData;
      
    })
    .catch(err => console.error(err));
  },

 viewAllUncompletedRequests(){
    fetch('http://localhost:8080/api/maintenance/uncompleted/' + this.user.id, {
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
 editRequestStatus(request){
   request.completed = request.completed === "true";
   fetch('http://localhost:8080/api/maintenance/requests', {
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
       this.viewAllCompletedRequests();
       this.viewAllUncompletedRequests();
       this.toast();
     })
   }
   },
created(){
  this.viewAllCompletedRequests();
  this.viewAllUncompletedRequests();
}
}
</script>

<style>

.tile{
  flex-wrap: wrap;
}

</style>