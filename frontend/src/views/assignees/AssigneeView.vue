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
import { faTrash, faInfoCircle } from '@fortawesome/free-solid-svg-icons'

const router = useRouter()
const assignees = ref<Assignee[]>([])
const searchPrename = ref('')

/**
 * Filter assignees by name
 */
 const filterAssignees = computed(() => {
  if (!searchPrename.value) {
    return assignees.value
  }
  
  const searchTerm = searchPrename.value.toLowerCase()

  return assignees.value.filter((assignee) => 
    assignee.prename.toLowerCase().includes(searchTerm)
  )
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
  <div class="flex-grow-1">
    <input
      type="text"
      v-model="searchPrename"
      class="form-control bg-dark text-light border-secondary search-input"
      placeholder="Suche nach Name..."
    />
  </div>

  <h2 class="text-light mb-3">Assignees</h2>
  <div class="assignee-list">
      <div v-if="filterAssignees.length > 0" class="row row-cols-1 row-cols-md-4 row-cols-lg-5 g-2">
        <div v-for="assignee in filterAssignees" :key="assignee.id" class="col card-column">
          <div class="card bg-dark text-light h-100 border-secondary">
            <div class="card-body d-flex flex-column">
              <h5 class="card-title">Name: {{ assignee.prename }}</h5>
              <div class="d-flex gap-2 mt-3">
                <button @click="navigateToUpdate(assignee.id)" class="btn btn-info text-white">
                  <font-awesome-icon :icon="faInfoCircle" /> Details
                </button>
                <button @click="deleteAssignee(assignee.id)" class="btn btn-danger">
                  <font-awesome-icon :icon="faTrash" /> Löschen
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <p v-else class="text">Keine offenen Todos gefunden...</p>
    </div>
</template>

<style scoped>
.btn {
  transition: all 0.2s ease;
}
.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
.card {
  transition: all 0.3s ease;
}
.card:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.btn-primary {
  width: 10%;
}

.card-column {
  width: 100%;
  padding-right: 8px;
  padding-left: 8px;
}

/* Responsive adjustments */
@media (min-width: 768px) {
  .row-cols-md-4 > .card-column {
    width: 25%;
  }
}

@media (min-width: 992px) {
  .row-cols-lg-5 > .card-column {
    width: 20%;
  }
}

/* Remove the unnecessary gap between cards */
.row {
  margin-right: -8px;
  margin-left: -8px;
}

.search-input::placeholder {
  color: rgba(255, 255, 255, 0.5) !important;
  opacity: 1;
}

</style>
