<template>
  <div class="home container is-fluid">
    <br/><br/> 
    <browse-properties :user="user" />
  </div>
</template>

<script>
import BrowseProperties from '../components/BrowseProperties';

export default {
  name: 'home',
  props: {
    user: Object
  },
  components: {
    BrowseProperties
  },
  data() {
    return {
    }
  },
  methods: {
    allProps() {
      return fetch('http://localhost:8080/api/properties')
        .then((response) => {
          if(response.ok) {
            console.log(JSON.stringify(response));
          }
        });
    }
  }
}
</script>
