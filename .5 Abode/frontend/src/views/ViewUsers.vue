<template>
  <div class="container is-fluid">
     <b-table
        :data="users"
        :columns="columns">
      </b-table>
  </div>
</template>

<script>
import auth from '../auth';

export default {
  data() {
    return {
      users: [],
      columns: [
                {
                    field: 'id',
                    label: 'User ID',
                    width: '100',
                    searchable: true,
                },
                {
                    field: 'email',
                    label: 'Email',
                    width: '100',
                    searchable: true,
                },
                {
                    field: 'firstName',
                    label: 'First Name',
                    width: '100',
                    searchable: true,
                },
                {
                    field: 'lastName',
                    label: 'Last Name',
                    width: '100',
                    searchable: true,
                },
                {
                    field: 'phoneNumber',
                    label: 'Phone Number',
                    width: '100',
                    searchable: true,
                },
                {
                    field: 'role',
                    label: 'Role',
                    width: '100',
                    searchable: true,
                }
            ]
    }
  },
  created() {
    fetch("http://localhost:8080/api/users", {
        method: 'GET',
          headers: {
            Authorization: 'Bearer ' + auth.getToken(),
            'Content-Type': 'application/json'
          },
          credentials: 'same-origin'
    })
    .then(response => {
      if(response.ok) {
        return response.json();
      }
    })
    .then(responseData => {
      this.users = responseData;
    })
  }
}
</script>

<style>

</style>