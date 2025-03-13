import { createRouter, createWebHistory } from 'vue-router'
import HomeView from "@/views/HomeView.vue";
import TodoView from "@/views/todos/TodoView.vue";
import AssigneeView from "@/views/assignees/AssigneeView.vue";
import CreateAssignee from "@/views/assignees/CreateAssignee.vue";
import CreateTodo from "@/views/todos/CreateTodo.vue";
import UpdateAssignee from '@/views/assignees/UpdateAssignee.vue';
import UpdateTodo from '@/views/todos/UpdateTodo.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/todos',
      name: 'todos',
      component: TodoView
    },
    {
      path: '/assignees',
      name: 'assignees',
      component: AssigneeView
    },
    {
      path: '/create-assignees',
      name: 'create-assignees',
      component: CreateAssignee
    },
    {
      path: '/create-todos',
      name: 'create-todos',
      component: CreateTodo
    },
    {
      path: '/assignees/:id',
      name: 'update-assignees',
      component: UpdateAssignee
    },
    {
      path: '/todos/:id',
      name: 'update-todos',
      component: UpdateTodo
    }
  ]
})

export default router

