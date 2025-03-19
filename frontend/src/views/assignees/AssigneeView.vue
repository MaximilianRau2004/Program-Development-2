<script setup lang="ts">
/**
 * This component displays a list of assignees and provides functionalities to delete and update assignees.
 * It fetches the list of assignees on mount and allows editing via a modal.
 */

import { ref, onMounted, computed } from 'vue'
import config from '../../config'
import { showToast, Toast } from '../../ts/toasts'
import { faCheck, faXmark } from '@fortawesome/free-solid-svg-icons'
import type { Assignee } from '../../ts/Assignee'
import { fetchAllAssignees } from '../../ts/Assignee'
import { faTrash, faInfoCircle } from '@fortawesome/free-solid-svg-icons'

const assignees = ref<Assignee[]>([])
const searchPrename = ref('')

// Edit modal properties
const showEditModal = ref(false)
const editingAssignee = ref<Assignee | null>(null)
const editPrename = ref('')
const editName = ref('')
const editEmail = ref('')

/**
 * Filter assignees by name
 */
const filterAssignees = computed(() => {
  if (!searchPrename.value) {
    return assignees.value
  }

  const searchTerm = searchPrename.value.toLowerCase()

  return assignees.value.filter((assignee) => assignee.prename.toLowerCase().includes(searchTerm))
})

/**
 * Delete an assignee by ID
 * @param id - The ID of the assignee to delete
 */
const deleteAssignee = async (id: number) => {
  if (
    !confirm(
      `Bist du sicher, dass du den Assignee "${
        assignees.value.find((assignee) => assignee.id === id)?.prename
      }" löschen möchtest?`
    )
  ) {
    return
  }

  fetch(`${config.apiBaseUrl}/assignees/${id}`, { method: 'DELETE' })
    .then(() => {
      assignees.value = assignees.value.filter((assignee) => assignee.id !== id)
      showToast(new Toast('Erfolg', `Erfolgreich Assignee gelöscht!`, 'success', faCheck, 5))
    })
    .catch(() =>
      showToast(new Toast('Fehler', `Fehler beim Löschen des Assignees!`, 'error', faXmark, 5))
    )
}

/**
 * Open edit modal with assignee details
 * @param id - The ID of the assignee to update
 */
const openEditModal = async (id: number) => {
  try {
    const assignee = assignees.value.find((a) => a.id === id)
    if (assignee) {
      editingAssignee.value = { ...assignee }
      editPrename.value = assignee.prename
      editName.value = assignee.name
      editEmail.value = assignee.email
      showEditModal.value = true
    } else {
      throw new Error('Assignee nicht gefunden')
    }
  } catch (error) {
    showToast(new Toast('Fehler', 'Fehler beim Laden der Assignees', 'error', faXmark, 5))
  }
}

/**
 * Close edit modal
 */
const closeEditModal = () => {
  showEditModal.value = false
  editingAssignee.value = null
}

/**
 * Update assignee with edited values
 */
const updateAssignee = async () => {
  if (!editingAssignee.value) return

  const id = editingAssignee.value.id

  fetch(`${config.apiBaseUrl}/assignees/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      prename: editPrename.value,
      name: editName.value,
      email: editEmail.value
    })
  })
    .then((response) => {
      if (!response.ok) {
        return response.json().then((error) => {
          throw new Error(error.message)
        })
      }
      return response.json()
    })
    .then((updatedAssignee) => {
      // Update the assignee in the list
      assignees.value = assignees.value.map((a) =>
        a.id === updatedAssignee.id ? updatedAssignee : a
      )
      showToast(new Toast('Erfolg', `Erfolgreich Assignee aktualisiert!`, 'success', faCheck, 5))
      closeEditModal()
    })
    .catch((error) => {
      showToast(
        new Toast(
          'Fehler',
          `Fehler beim Aktualisieren des Assignees: ${error.message}`,
          'error',
          faXmark,
          10
        )
      )
    })
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
  <h2 class="text-light mb-3">Assignees</h2>
  <!-- search field -->
  <div class="p-3 mb-4 rounded bg-dark">
    <div class="d-flex flex-wrap gap-3 align-items-center">
      <div class="flex-grow-1">
        <input
          type="text"
          v-model="searchPrename"
          class="form-control bg-dark text-light border-secondary search-input"
          placeholder="Suche nach Name..."
        />
      </div>
    </div>
  </div>

  <div class="assignee-list">
    <div v-if="filterAssignees.length > 0" class="row row-cols-1 row-cols-md-4 row-cols-lg-5 g-2">
      <div v-for="assignee in filterAssignees" :key="assignee.id" class="col card-column">
        <div class="card bg-dark text-light h-100 border-secondary">
          <div class="card-body d-flex flex-column">
            <h5 class="card-title">Name: {{ assignee.prename }} {{ assignee.name }}</h5>
            <p class="card-title">ID: {{ assignee.id }}</p>
            <div class="d-flex gap-2 mt-3">
              <button @click="openEditModal(assignee.id)" class="btn btn-info">
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
    <p v-else class="text-light">Keine Assignees gefunden...</p>
  </div>

  <!-- Edit Modal -->
  <div v-if="showEditModal" class="modal fade show d-block" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content bg-dark text-light">
        <div class="modal-header border-secondary">
          <h5 class="modal-title">Assignee bearbeiten</h5>
          <button
            type="button"
            class="btn-close btn-close-white"
            @click="closeEditModal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <div class="mb-3">
            <label for="editPrename" class="form-label">Vorname</label>
            <input
              type="text"
              class="form-control bg-dark text-light border-secondary w-100"
              id="editPrename"
              v-model="editPrename"
              required
            />
          </div>
          <div class="mb-3">
            <label for="editName" class="form-label">Name</label>
            <input
              type="text"
              class="form-control bg-dark text-light border-secondary w-100"
              id="editName"
              v-model="editName"
              required
            />
          </div>
          <div class="mb-3">
            <label for="editPrename" class="form-label">Email</label>
            <input
              type="email"
              class="form-control bg-dark text-light border-secondary w-100"
              id="editPrename"
              v-model="editEmail"
              required
            />
          </div>
        </div>
        <div class="modal-footer border-secondary">
          <button @click="updateAssignee" type="button" class="btn btn-info">Speichern</button>
        </div>
      </div>
    </div>
  </div>
  <!-- Modal backdrop -->
  <div v-if="showEditModal" class="modal-backdrop fade show"></div>
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

.row {
  margin-right: -8px;
  margin-left: -8px;
}

.search-input::placeholder {
  color: rgba(255, 255, 255, 0.5) !important;
  opacity: 1;
}

.form-control {
  margin-bottom: 1%;
  width: 50%;
}

.modal.show {
  background-color: rgba(0, 0, 0, 0.5);
}

.form-control::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

.modal-content {
  border: 1px solid #6c757d;
}

.btn-close-white {
  filter: invert(1) grayscale(100%) brightness(200%);
}
</style>
