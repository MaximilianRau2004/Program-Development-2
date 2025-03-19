<script setup lang="ts">
import { RouterView, RouterLink } from 'vue-router'
import { Close, Toast, Toasts } from "agnostic-vue";
import { activeToasts } from "@/ts/toasts";
import { ref } from 'vue';
import 'agnostic-vue/dist/common.min.css';
import "agnostic-vue/dist/index.css";

// Set default state to closed
const sidebarOpen = ref(false);

const toggleSidebar = () => {
  sidebarOpen.value = !sidebarOpen.value;
};
</script>

<template>
  <div id="app" class="app-container">
    <!-- Sidebar -->
    <div class="sidebar" :class="{ 'sidebar-collapsed': !sidebarOpen }">
      <div class="sidebar-header">
        <h2 class="sidebar-title" v-if="sidebarOpen">Menu</h2>
        <button class="toggle-button" @click="toggleSidebar">
          {{ sidebarOpen ? '«' : '»' }}
        </button>
      </div>
      <nav class="sidebar-nav" v-if="sidebarOpen">
        <ul>
          <li>
            <RouterLink to="/">Home</RouterLink>
          </li>
          <li>
            <RouterLink to="/todos">Todos verwalten</RouterLink>
          </li>
          <li>
            <RouterLink to="/assignees">Assignees verwalten</RouterLink>
          </li>
          <li>
            <RouterLink to="/create-todos">Todo erstellen</RouterLink>
          </li>
          <li>
            <RouterLink to="/create-assignees">Assignee erstellen</RouterLink>
          </li>
        </ul>
      </nav>
    </div>

    <!-- Main Content -->
    <div class="main-content" :class="{ 'expanded': !sidebarOpen }">
      <RouterView />
    </div>
  </div>

  <!-- Toasts -->
  <Toasts vertical-position="top" horizontal-position="end">
    <template v-for="toast of activeToasts" :key="toast.key">
      <Toast :type="toast.type" class="alert alert-border-left alert-info">
        <div class="flex-fill flex flex-column">
          <div class="flex">
            <h3 class="flex-fill">{{ toast.title }}</h3>
            <Close @click="toast.close()" />
          </div>
          <div class="flex">
            <font-awesome-icon :icon="toast.icon" size="xl" class="mie8 pbs2"></font-awesome-icon>
            <div class="flex-fill">
              {{ toast.message }}
            </div>
          </div>
        </div>
      </Toast>
      <div class="mbe14" />
    </template>
  </Toasts>
</template>

<style scoped>
.app-container {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 10%;
  background-color: #333;
  color: var(--color-heading);
  border-right: 1px solid var(--color-border);
  transition: width 0.3s ease;
  overflow: hidden;
}

.sidebar-collapsed {
  width: 50px;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid var(--color-border);
}

.sidebar-title {
  margin: 0;
  font-size: 1.2rem;
}

.toggle-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: var(--color-text);
  width: 100%;
  text-align: center;
  padding: 5px;
}

.sidebar-collapsed .sidebar-header {
  justify-content: center;
  padding: 15px 0;
}

.sidebar-nav {
  padding: 15px 0;
}

.sidebar-nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-nav li {
  padding: 10px 15px;
}

.sidebar-nav a {
  color: var(--color-text);
  text-decoration: none;
  display: block;
}

.sidebar-nav a:hover {
  color: var(--color-heading);
}

.main-content {
  flex: 1;
  padding: 20px;
  transition: margin-left 0.3s ease;
}

.expanded {
  margin-left: 0;
}
</style>