import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './index.css'
import { library } from "@fortawesome/fontawesome-svg-core";
import { fas } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

library.add(fas);

const app = createApp(App);

// Use VueAxios to configure Axios with Vue

app.use(router);
app.component("font-awesome-icon", FontAwesomeIcon);

app.mount('#app');