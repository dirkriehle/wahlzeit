import { RouteRecordRaw, createRouter, createWebHistory } from 'vue-router';

import AboutView from '../views/AboutView.vue';
import HomeView from '../views/HomeView.vue';
import PhotoListView from '../views/PhotoListView.vue';
import PhotoView from '../views/PhotoView.vue';
import UserView from '../views/UserView.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: HomeView,
  },
  {
    path: '/about',
    name: 'About',
    component: AboutView,
  },
  {
    path: '/user/:id',
    name: 'User',
    component: UserView,
    props: true,
  },
  {
    path: '/photo/:id',
    name: 'Photo',
    component: PhotoView,
    props: true,
  },
  {
    path: '/list',
    name: 'PhotoList',
    component: PhotoListView,
    props: (route): Record<string, unknown> => ({
      tags: route.query.tags || '',
    }),
  },
];

const router = createRouter({
  // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
  history: createWebHistory(process.env.BASE_URL),
  routes: routes,
  linkActiveClass: 'active',
});

export default router;
