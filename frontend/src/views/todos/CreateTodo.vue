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
    console.log('Assignees:', data)
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
      showToast(new Toast('Success', `Erfolgreich Todo erstellt!`, 'success', faCheck, 5))
      router.push('/todos')
    })
    .catch((error) => {
      console.error(error)
      showToast(new Toast('Error', `Fehler beim Erstellen des Todos: ${error.message}`, 'error', faXmark, 10))
    })
}
</script>

<template>
  <!-- Create Todo Form -->
  <div class="container">
    <div class="create todo">
      <h2 class="login-header">Neues Todo erstellen</h2>
      <form @submit.prevent="createTodo">
        <div class="input-group">
          <label for="title">Titel</label>
          <input type="text" id="title" v-model="title" required />
        </div>
        <div class="input-group">
          <label for="description">Beschreibung</label>
          <input type="text" id="description" v-model="description"/>
        </div>

        <!-- Selected Assignees -->
        <div class="selected-assignees">
          <div
            v-for="(assignee, index) in selectedAssignees"
            :key="index"
            class="selected-assignee"
          >
            {{ assignee.prename }} {{ assignee.name }}
            <button type="button" @click="removeAssignee(assignee.id)" class="remove-btn">x</button>
          </div>
        </div>
        <!-- Dropdown menu for assignees -->
        <div class="dropdown">
          <label @click="toggleDropdown" class="dropdown-label"> Assignee auswählen </label>
          <div v-if="dropdownOpen" class="dropdown-menu">
            <input
              type="text"
              v-model="searchQuery"
              placeholder="Suchen..."
              class="dropdown-search"
            />
            <ul>
              <li v-for="assignee in filteredAssignees" :key="assignee.id" class="dropdown-item">
                <label>
                  <input type="checkbox" :value="assignee.id" v-model="assigneeListSelect" />
                  {{ assignee.prename }} {{ assignee.name }}
                </label>
              </li>
            </ul>
          </div>
        </div>

        <div class="input-group">
          <label for="dueDate">Fälligkeitsdatum</label>
          <input type="date" id="dueDate" v-model="dueDate" required />
        </div>
        <Button @click="createTodo" type="button" class="create-button">Erstellen</Button>
      </form>
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
