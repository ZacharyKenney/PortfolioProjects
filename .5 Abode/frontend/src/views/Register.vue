<template>
  <div id="register" class="text-center container is-fluid">
    <form class="form-register" @submit.prevent="register">
      <h1 class="title">Create Account</h1>
      <div class="alert alert-danger" role="alert" v-if="registrationErrors">
        There were problems registering this user.
      </div>
      <b-field label="Email">
      <b-input
        type="email"
        id="email"
        class="form-control"
        placeholder="Email"
        v-model="user.email"
        required
        autofocus
      />
      </b-field>
            <b-field label="First Name">
      <b-input
        type="text"
        id="firstName"
        class="form-control"
        placeholder="First Name"
        v-model="user.firstName"
        required
      />
            </b-field>
      <b-field label="Last Name">
      <b-input
        type="text"
        id="lastName"
        class="form-control"
        placeholder="Last Name"
        v-model="user.lastName"
        required
      />
      </b-field>
      <b-field label="Phone #">
      <b-input
        type="text"
        id="phoneNumber"
        class="form-control"
        placeholder="Phone #"
        v-model="user.phoneNumber"
        required
      />
      </b-field>
      <b-field label="Password">
      <b-input
        type="password"
        id="password"
        class="form-control"
        placeholder="Password"
        v-model="user.password"
        required
      />
      </b-field>
      <b-input
        type="password"
        id="confirmPassword"
        class="form-control"
        placeholder="Confirm Password"
        v-model="user.confirmPassword"
        required
      />
      <b-field label="Role">
             </b-field>
      <b-radio v-model="user.role" native-value="renter">Renter</b-radio>
      <b-radio v-model="user.role" native-value="maintenance">Maintenance</b-radio>
      <b-radio v-model="user.role" native-value="landlord">Landlord </b-radio>
    
     
      <b-button type="is-primary"
      native-type="submit">
        Create Account
      </b-button>
      <br>
       <router-link :to="{ name: 'login' }">
        Have an account?
      </router-link>
    </form>
  </div>
</template>

<script>
export default {
  name: 'register',
  data() {
    return {
      user: {
        email: '',
        firstName: '',
        lastName: '',
        phoneNumber: '',
        password: '',
        confirmPassword: '',
        role: 'renter'
      },
      registrationErrors: false,
    };
  },
  methods: {
    register() {
      fetch(`${process.env.VUE_APP_REMOTE_API}/register`, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.user),
      })
        .then((response) => {
          if (response.ok) {
            return (response.json());
          } else {
            this.registrationErrors = true;
            throw 'Register returned: ' + response.status;
          }
        })
        .then ((parsedData) => {
            if (parsedData.success) {
              this.$router.push({ path: '/login', query: { registration: 'success' } });
            } else {
              this.registrationErrors = true;
            }
        })
        .catch((err) => console.log(err));
    },
  },
};
</script>

<style scoped>
button {
  margin-left: 10px;
}
form {
  width: 50%;
}
</style>
