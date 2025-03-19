<script setup lang="ts">
/**
 * This component allows the creation of a new todo.
 * It includes a form for entering the attributes of the todo.
 * Additionally, it provides a dropdown menu for selecting and managing assignees.
 */

import { ref, onMounted } from 'vue'
import { Button } from 'agnostic-vue'
import config from '../../config'
import { showToast, Toast } from '../../ts/toasts'
import { faCheck, faXmark } from '@fortawesome/free-solid-svg-icons'
import { useRouter } from 'vue-router'
import '@/assets/buttons.css'
import '@/assets/select-assignees.css'
import '@/assets/input-group.css'
import { fetchAllAssignees } from '../../ts/Assignee'
import { useAssigneeSelection } from '../../ts/Todo'

// Assignee Selection
const {
  assignees,
  selectedAssignees,
  assigneeListSelect,
  searchQuery,
  dropdownOpen,
  toggleDropdown,
  removeAssignee,
  filteredAssignees
} = useAssigneeSelection()

// Todo attributes
const title = ref('')
const description = ref('')
const dueDate = ref('')
const finished = ref(false)

const router = useRouter()

// error message for validation
const errorMessage = ref('')

/**
 * fetch all assignees on mount
 */
onMounted(() => {
  fetchAllAssignees().then((data) => {
    assignees.value = data
  })
})

/**
 * create a new todo
 */
function createTodo() {
  errorMessage.value = ''
  const dueTimestamp = new Date(dueDate.value).getTime()

  fetch(`${config.apiBaseUrl}/todos`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      title: title.value,
      description: description.value,
      finished: finished.value,
      assigneeIdList: assigneeListSelect.value,
      dueDate: dueTimestamp
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
    .then(() => {
      showToast(new Toast('Erfolg', `Erfolgreich Todo erstellt!`, 'success', faCheck, 5))
      router.push('/todos')
    })
    .catch((error) => {
      console.error(error)
      showToast(
        new Toast(
          'Fehler',
          `Fehler beim Erstellen des Todos: ${error.message}`,
          'error',
          faXmark,
          5
        )
      )
    })
}
</script>

<template>
  <!-- Create Todo Form -->
  <div class="container p-4 bg-dark text-light rounded">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h5>Todo erstellen</h5>
    </div>

    <div class="mb-4">
      <div class="mb-3">
        <label for="title" class="form-label">Titel</label>
        <input
          type="text"
          class="form-control bg-dark text-light border-secondary w-100"
          id="title"
          v-model="title"
          required
        />
      </div>
      <div class="mb-3">
        <label for="description" class="form-label">Beschreibung</label>
        <textarea
          class="form-control bg-dark text-light border-secondary w-100"
          id="description"
          v-model="description"
          rows="3"
        ></textarea>
      </div>

      <!-- Assignees selection -->
      <div class="mb-3">
        <label class="form-label">Assignees</label>
        <div class="d-flex flex-wrap gap-2 mb-2">
          <span
            v-for="(assignee, index) in selectedAssignees"
            :key="index"
            class="badge bg-secondary rounded-pill px-3 py-2"
          >
            {{ assignee.prename }} {{ assignee.name }}
            <button
              type="button"
              @click="removeAssignee(assignee.id)"
              class="btn-close btn-close-white ms-2 p-0"
              style="font-size: 0.65rem"
              aria-label="Remove"
            ></button>
          </span>
        </div>

        <div class="dropdown">
          <button
            type="button"
            @click="toggleDropdown"
            class="btn btn-secondary dropdown-toggle w-100 text-start"
          >
            Assignee auswählen
          </button>
          <div
            v-if="dropdownOpen"
            class="dropdown-menu show bg-dark text-light border-secondary w-100"
          >
            <div class="px-3 py-2">
              <input
                type="text"
                v-model="searchQuery"
                placeholder="Suchen..."
                class="form-control bg-dark text-light border-secondary"
              />
            </div>
            <ul class="list-unstyled mb-0 px-0">
              <li
                v-for="assignee in filteredAssignees"
                :key="assignee.id"
                class="dropdown-item text-light px-3 py-2"
              >
                <div class="form-check">
                  <input
                    class="form-check-input"
                    type="checkbox"
                    :value="assignee.id"
                    v-model="assigneeListSelect"
                    :id="`assignee-${assignee.id}`"
                  />
                  <label class="form-check-label" :for="`assignee-${assignee.id}`">
                    {{ assignee.prename }} {{ assignee.name }}
                  </label>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="mb-3">
        <label for="dueDate" class="form-label">Fälligkeitsdatum</label>
        <input
          type="date"
          class="form-control bg-dark text-light border-secondary w-100"
          id="dueDate"
          v-model="dueDate"
          required
        />
      </div>
      <div class="d-flex justify-content-end">
        <button @click="createTodo" type="button" class="btn btn-info">Erstellen</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  padding: 20px;
  background-color: #000000;
  color: #ffffff;
  border: 1px solid #ffffff;
  border-radius: 8px;
  max-width: 600px;
  margin: 0 auto;
}
</style>
