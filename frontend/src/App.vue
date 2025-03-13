<script setup lang="ts">
import {RouterView} from 'vue-router'
import {Close, Header, HeaderNav, HeaderNavItem, Toast, Toasts} from "agnostic-vue";
import {activeToasts} from "@/ts/toasts";
import 'agnostic-vue/dist/common.min.css';
import "agnostic-vue/dist/index.css";
</script>

<template>
  <!-- Header -->
  <div id="app">
    <Header isHeaderContentStart class="header">
      <template v-slot:headernav>
        <HeaderNav>
          <HeaderNavItem>
            <RouterLink to="/">Home</RouterLink>
          </HeaderNavItem>
          <HeaderNavItem>
            <RouterLink to="/todos">Todos verwalten</RouterLink>
          </HeaderNavItem>
          <HeaderNavItem>
            <RouterLink to="/assignees">Assignees verwalten</RouterLink>
          </HeaderNavItem>
          <HeaderNavItem>
            <RouterLink to="/create-todos">Todo erstellen</RouterLink>
          </HeaderNavItem>
          <HeaderNavItem>
            <RouterLink to="/create-assignees">Assignee erstellen</RouterLink>
          </HeaderNavItem>
        </HeaderNav>
      </template>
    </Header>

    <div class="main">
      <RouterView/>
    </div>
  </div>

  <Toasts vertical-position="top" horizontal-position="end">
    <template v-for="toast of activeToasts" :key="toast.key">
      <Toast :type="toast.type" class="alert alert-border-left alert-info">
        <div class="flex-fill flex flex-column">
          <div class="flex">
            <h3 class="flex-fill">{{ toast.title }}</h3>
            <Close @click="toast.close()"/>
          </div>
          <div class="flex">
            <font-awesome-icon :icon="toast.icon" size="xl" class="mie8 pbs2"></font-awesome-icon>
            <div class="flex-fill">
              {{ toast.message }}
            </div>
          </div>
        </div>
      </Toast>
      <div class="mbe14"/>
    </template>
  </Toasts>
</template>

<style scoped>
.main {
  padding: 10px 20px;
}

header {
  background-color: var(--color-background-soft) !important;
  color: var(--color-heading) !important;
  border-bottom: 1px solid var(--color-border) !important;
}

header a {
  color: var(--color-text) !important;
}

header a:hover {
  color: var(--color-heading) !important;
}
</style>
