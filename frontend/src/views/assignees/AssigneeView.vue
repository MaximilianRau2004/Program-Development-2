<script setup lang="ts">
/**
 * This component displays a list of assignees and provides functionalities to delete and update assignees.
 * It fetches the list of assignees on mount and allows navigation to the update view.
 */

import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import config from '../../config'
import { showToast, Toast } from '../../ts/toasts'
import { faCheck, faXmark } from '@fortawesome/free-solid-svg-icons'
import type { Assignee } from '../../ts/Assignee'
import '@/assets/buttons.css'
import '@/assets/table.css'
import { fetchAllAssignees } from '../../ts/Assignee'

const router = useRouter()
const assignees = ref<Assignee[]>([])
const searchId = ref('')

/**
 * Filter assignee by id
 */
const filterAssignees = computed(() => {
  if (!searchId.value) {
    return assignees.value
  }
  const id = Number(searchId.value)
  if (isNaN(id)) {
    return []
  }
  return assignees.value.filter((assignee) => assignee.id === id)
})

/**
 * Delete an assignee by ID
 * @param id - The ID of the assignee to delete
 */
const deleteAssignee = async (id: number) => {
  if (!confirm(`Bist du sicher, dass du den Assignee löschen möchtest?`)) {
    return
  }

  fetch(`${config.apiBaseUrl}/assignees/${id}`, { method: 'DELETE' })
    .then(() => {
      assignees.value = assignees.value.filter((assignee) => assignee.id !== id)
      showToast(new Toast('Alert', `Erflogreich Assignee gelöscht!`, 'success', faCheck, 5))
    })
    .catch(() =>
      showToast(new Toast('Error', `Fehler beim Löschen des Assignees!`, 'error', faXmark, 10))
    )
}

/**
 * Navigate to the update assignee view
 * @param id - The ID of the assignee to update
 */
const navigateToUpdate = (id: number) => {
  router.push({ name: 'update-assignees', params: { id } })
}

/**
 * Fetch all assignees on mount
 */
onMounted(() => {
  fetchAllAssignees().then((data) => {
    console.log('Assignees:', data)
    assignees.value = data
  })
})
</script>

<template>
  <h1>Assignee Manager</h1>

  <!-- search field -->
  <div class="search-container">
    <input type="text" v-model="searchId" placeholder="Suche nach ID" />
  </div>

  <!-- table for assignees -->
  <table v-if="filterAssignees.length > 0" class="table">
    <thead>
      <tr>
        <th>ID</th>
        <th>Vorname</th>
        <th>Nachname</th>
        <th>E-mail</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="assignee in filterAssignees" :key="assignee.id">
        <td>{{ assignee.id }}</td>
        <td>{{ assignee.prename }}</td>
        <td>{{ assignee.name }}</td>
        <td>{{ assignee.email }}</td>
        <Button @click="navigateToUpdate(assignee.id)" class="update-button">Bearbeiten</Button>
        <Button @click="deleteAssignee(assignee.id)" class="delete-button">Löschen</Button>
      </tr>
    </tbody>
  </table>
  <Alert v-else> Keine Assignees gefunden... </Alert>
</template>
