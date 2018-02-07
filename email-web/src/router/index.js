import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'
import Router from 'vue-router'
import EmailSender from '@/components/EmailSender'
import 'bootstrap/dist/css/bootstrap.min.css'
import '../css/custom.css'

Vue.use(Router)
Vue.use(BootstrapVue)

Vue.prototype.$eventHub = new Vue()
Vue.prototype.$globalStore = new Vue()

// EMAIL REGEX match RFC 2822
Vue.prototype.$globalStore.EMAIL_REGEX = /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/

export default new Router({
    routes: [
        {
            path: '/',
            name: 'EmailSender',
            component: EmailSender
        }
    ]
})
