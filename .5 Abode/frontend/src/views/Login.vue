<template>
  <div id="login" class="text-center">
      <form class="form-signin" @submit.prevent="login">
        <div class="alert alert-danger" role="alert" v-if="invalidCredentials">
          Invalid username and password!
        </div>
        <div class="alert alert-success" role="alert" v-if="this.$route.query.registration">
          Thank you for registering, please sign in.
        </div>
        <div v-if="showLogIn" class="credentials">
          <b-field>
            <b-input
              type="text"
              id="email"
              class="form-control"
              placeholder="Email"
              v-model="user.email"
              required
            /></b-field>
            <b-field>
            <b-input
              type="password"
              id="password"
              class="form-control"
              placeholder="Password"
              v-model="user.password"
              required
            ></b-input>
            </b-field>
            <b-button type="is-primary" outlined native-type="submit">Sign in</b-button>
          </div>
            
      </form>
      <div class="align">
        <browse-properties :user="auth.user"/>
      </div>
  </div>
</template>

<script>
import auth from '../auth';
import BrowseProperties from '../components/BrowseProperties';

export default {
  name: 'login',
  components: {
    BrowseProperties
  },
  props: {
    showLogIn: Boolean
  },
  data() {
    return {
      user: {
        email: '',
        password: '',
      },
      invalidCredentials: false,
      auth: auth
    };
  },
  methods: {
    login() {
      fetch(`${process.env.VUE_APP_REMOTE_API}/login`, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.user),
      })
        .then((response) => {
          if (response.ok) {
            return response.text();
          } else {
            this.invalidCredentials = true;
          }
        })
        .then((token) => {
          if (token != undefined) {
            if (token.includes('"')) {
              token = token.replace(/"/g, '');
            }
            auth.saveToken(token);
            this.$emit('user-login');
            this.$router.push('/');
          }
        })
        .catch((err) => console.error(err));
    },
  },
};
</script>

<style scoped>
button {
  margin-left: 10px;
}
form{

  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: flex-end;
}
.credentials {
  display: flex;
  flex-direction: row;
}

</style>
