import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'
import Router from 'vue-router'
import EmailSender from '@/components/EmailSender'
import 'bootstrap/dist/css/bootstrap.min.css'
import '../css/custom.css'

Vue.use(Router)
Vue.use(BootstrapVue)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'EmailSender',
            component: EmailSender
        }
    ]
})
