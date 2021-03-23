import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import Home from "@/views/Home.vue";
import About from "@/views/About.vue";
import User from "@/views/User.vue";
import Photo from "@/views/Photo.vue";
import Upload from "@/views/Upload.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/about",
    name: "About",
    component: About
  },
  {
    path: "/user/:id",
    name: "User",
    component: User,
    props: true
  },
  {
    path: "/photo/:id",
    name: "Photo",
    component: Photo,
    props: true
  },
  {
    path: "/upload",
    name: "Upload",
    component: Upload,
    props: true
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes: routes,
  linkActiveClass: "active"
});

export default router;
