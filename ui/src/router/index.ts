import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import AboutView from "@/views/AboutView.vue";
import UserView from "@/views/UserView.vue";
import PhotoView from "@/views/PhotoView.vue";
import PhotoListView from "@/views/PhotoListView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "Home",
    component: HomeView
  },
  {
    path: "/about",
    name: "About",
    component: AboutView
  },
  {
    path: "/user/:id",
    name: "User",
    component: UserView,
    props: true
  },
  {
    path: "/photo/:id",
    name: "Photo",
    component: PhotoView,
    props: true
  },
  {
    path: "/list",
    name: "PhotoList",
    component: PhotoListView,
    props: route => ({ tags: route.query.tags || "" })
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes: routes,
  linkActiveClass: "active"
});

export default router;
