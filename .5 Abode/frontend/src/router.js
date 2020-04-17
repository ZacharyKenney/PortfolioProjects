import Vue from 'vue'
import Router from 'vue-router';
import auth from './auth';
import Home from './views/Home.vue';
import Login from './views/Login.vue';
import Register from './views/Register.vue';
import Payment from './views/Payment.vue';
import Lease from './views/Lease.vue';
import MaintenanceRequest from './views/MaintenanceRequest.vue';
import viewMaintenanceRequest from './views/ViewMaintenanceRequests.vue';
import TenantsAndLandlord from './views/TenantsAndLandlord.vue';
import AddOrUpdateLease from './views/AddOrUpdateLease.vue';
import AssignMaintenance from './views/AssignMaintenance';
import AddOrUpdateProperties from './views/AddOrUpdateProperties.vue';
import viewUsers from './views/ViewUsers.vue';
import AboutUs from './views/AboutUs.vue';

Vue.use(Router)

/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login
 * If they have (or don't need to) they're allowed to go about their way.
 * 
 * Some routes check for the user's role before routing to the requested page.
 */
let user = auth.getUser();

const router = new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            name: 'home',
            component: Home,
            meta: {
                requiresAuth: true
            }
        },
        {
            path: "/about",
            name: "about",
            component: AboutUs,
            meta: {
                requiresAuth: false
            }
        },
        {
            path: "/login",
            name: "login",
            component: Login,
            meta: {
                requiresAuth: false
            }
        },
        {
            path: "/register",
            name: "register",
            component: Register,
            meta: {
                requiresAuth: false
            }
        },
        {
            path: "/payment",
            name: "payment",
            component: Payment,
            beforeEnter: (to, from, next) => {
                user = auth.getUser();
                // If user doesn't have role of 'renter' or 'admin', send to "/"
                if (user.rol !== 'renter' && user.rol !== 'admin') {
                    next("/");
                } else {
                    // Else let them go to their next destination
                    next();
                }
            },
            meta: {
                requiresAuth: true
            },
        },
        {
            path: "/lease",
            name: "lease",
            component: Lease,
            beforeEnter: (to, from, next) => {
                user = auth.getUser();
                // If user doesn't have role of 'renter' or 'admin', send to "/"
                if (user.rol !== 'renter' && user.rol !== 'admin') {
                    next("/");
                } else {
                    // Else let them go to their next destination
                    next();
                }
            },
            meta: {
                requiresAuth: true
            }
        },
        {
            path: "/maintenance/request",
            name: "request",
            component: MaintenanceRequest,
            meta: {
                requiresAuth: true
            }
        },
        {
            path: "/maintenance/view",
            name: "view",
            component: viewMaintenanceRequest,
            beforeEnter: (to, from, next) => {
                user = auth.getUser();
                // If user doesn't have role of 'maintenance' or 'admin', send to "/"
                if (user.rol !== 'maintenance' && user.rol !== 'admin') {
                    next("/");
                } else {
                    // Else let them go to their next destination
                    next();
                }
            },
            meta: {
                requiresAuth: true
            }
        },
        {
            path: "/Landlord/tenants",
            name: "tenants",
            component: TenantsAndLandlord,
            beforeEnter: (to, from, next) => {
                user = auth.getUser();
                // If user doesn't have role of 'landlord' or 'admin', send to "/"
                if (user.rol !== 'landlord' && user.rol !== 'admin') {
                    next("/");
                } else {
                    // Else let them go to their next destination
                    next();
                }
            },
            meta: {
                requiresAuth: true
            }
        },
        {
            path: "/Landlord/AddOrUpdateLease",
            name: "addOrUpdateLease",
            component: AddOrUpdateLease,
            beforeEnter: (to, from, next) => {
                user = auth.getUser();
                // If user doesn't have role of 'landlord' or 'admin', send to "/"
                if (user.rol !== 'landlord' && user.rol !== 'admin') {
                    next("/");
                } else {
                    // Else let them go to their next destination
                    next();
                }
            },
            meta: {
                requiresAuth: true,
                user: user
            }
        },
        {
            path: "/Landlord/assignMaintenance",
            name: "assignMaintenance",
            component: AssignMaintenance,
            beforeEnter: (to, from, next) => {
                user = auth.getUser();
                // If user doesn't have role of 'landlord' or 'admin', send to "/"
                if (user.rol !== 'landlord' && user.rol !== 'admin') {
                    next("/");
                } else {
                    // Else let them go to their next destination
                    next();
                }
            },
            meta: {
                requiresAuth: true
            }
        },
        {
            path: "/Landlord/AddOrUpdateProperties",
            name: "addOrUpdate",
            component: AddOrUpdateProperties,
            beforeEnter: (to, from, next) => {
                user = auth.getUser();
                // If user doesn't have role of 'landlord' or 'admin', send to "/"
                if (user.rol !== 'landlord' && user.rol !== 'admin') {
                    next("/");
                } else {
                    // Else let them go to their next destination
                    next();
                }
            },
            meta: {
                requiresAuth: true
            }
        },
        {
            path: "/Admin/viewUsers",
            name: "viewUsers",
            component: viewUsers,
            beforeEnter: (to, from, next) => {
                user = auth.getUser();
                // If user doesn't have role of 'admin', send to "/"
                if (user.rol !== 'admin') {
                    next("/");
                } else {
                    // Else let them go to their next destination
                    next();
                }
            },
            meta: {
                requiresAuth: true
            }
        },

    ]
}
)

router.beforeEach((to, from, next) => {
    // Determine if the route requires Authentication
    const requiresAuth = to.matched.some(x => x.meta.requiresAuth);
    user = auth.getUser();

    // If it does and they are not logged in, send the user to "/login"
    if (requiresAuth && !user) {
        next("/login");
    } else {
        // Else let them go to their next destination
        next();
    }
});

export default router;
