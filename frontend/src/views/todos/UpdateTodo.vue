<script setup lang="ts">
/**
 * This component allows updating an existing todo.
 * It includes a form for entering the title, description, and status of the todo.
 * Additionally, it provides a dropdown menu for selecting and managing assignees.
 */

import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import config from '@/config'
import { showToast, Toast } from '@/ts/toasts'
import { faCheck, faXmark } from '@fortawesome/free-solid-svg-icons'
import type { Assignee } from '@/ts/Assignee'
import '@/assets/buttons.css'
import '@/assets/select-assignees.css'
import '@/assets/input-group.css'
import { fetchAllAssignees } from '@/ts/Assignee'
import { fetchTodo } from '@/ts/Todo'
import { useAssigneeSelection } from '@/ts/Todo'

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

const router = useRouter()
const route = useRoute()

// todo attributes
const id = Number(route.params.id)
const title = ref('')
const description = ref('')
const finished = ref(false)
const createdDate = ref('')
const dueDate = ref('')
const category = ref('')
const assigneeList = ref<Assignee[]>([])

// Error message
const errorMessage = ref('')

/**
 * Fetch todo by id and assignees on mount
 */
onMounted(async () => {
  try {
    const [todoData, assigneeData] = await Promise.all([fetchTodo(id), fetchAllAssignees()])

    title.value = todoData.title
    description.value = todoData.description
    finished.value = todoData.finished
    createdDate.value = todoData.createdDate ? new Date(todoData.createdDate).toISOString().split('T')[0] : ''
    dueDate.value = todoData.dueDate ? new Date(todoData.dueDate).toISOString().split('T')[0] : ''
    category.value = todoData.category

    if (Array.isArray(todoData.assigneeList)) {
      assigneeList.value = todoData.assigneeList
      assigneeListSelect.value = todoData.assigneeList.map((assignee) => assignee.id)
    }

    if (Array.isArray(assigneeData)) {
      assignees.value = assigneeData
    }
  } catch (error) {
    console.error('Error by fetching:', error)
    showToast(new Toast('Error', `Fehler beim Laden des Todos`, 'error', faXmark, 5))
  }
})

/**
 * Update a todo by id
 * @param event - The form submit event
 */
function updateTodo(event: Event) {
  event.preventDefault()
  errorMessage.value = ''

  const dueTimestamp = new Date(dueDate.value).getTime()

  // PUT request to update the todo
  fetch(`${config.apiBaseUrl}/todos/${id}`, {
    method: 'PUT',
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
      showToast(new Toast('Success', `Erfolgreich Todo aktualisiert!`, 'success', faCheck, 5))
      router.push('/todos')
    })
    .catch((error) => {
      console.error('Error updating todo:', error)
      showToast(new Toast('Error', `Fehler beim Erstellen des Todos: ${error.message}`, 'error', faXmark, 10))
    })
}

/**
 * Navigate back to the todos view
 */
const goBack = () => {
  router.push('/todos')
}
</script>

<template>
  <form @submit="updateTodo">
    <!-- container for updating todo -->
    <h1>Todo bearbeiten</h1>

    <div class="input-group">
      <label for="title">Titel</label>
      <input type="text" id="title" v-model="title" required />
    </div>
    <div class="input-group">
      <label for="description">Beschreibung</label>
      <input type="text" id="description" v-model="description"/>
    </div>
    <div class="selected-assignees">
      <div v-for="(assignee, index) in selectedAssignees" :key="index" class="selected-assignee">
        {{ assignee.prename }} {{ assignee.name }}
        <button type="button" @click="removeAssignee(assignee.id)" class="remove-btn">x</button>
      </div>
    </div>

    <div class="dropdown">
      <label @click="toggleDropdown" class="dropdown-label"> Assignee auswählen </label>
      <div v-if="dropdownOpen" class="dropdown-menu">
        <input type="text" v-model="searchQuery" placeholder="Suchen..." class="dropdown-search" />
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
    <div class="input-group checkbox-group">
      <label for="finished">Beendet?</label>
      <input type="checkbox" id="finished" v-model="finished" />
    </div>
    <div class="input-group">
      <label for="createdDate">Erstellungsdatum</label>
      <input type="date" id="createdDate" v-model="createdDate" disabled />
    </div>
    <div class="input-group">
      <label for="dueDate">Fälligkeitsdatum</label>
      <input type="date" id="dueDate" v-model="dueDate" required />
    </div>
    <div class="input-group">
      <label for="category">Kategorie</label>
      <input type="text" id="category" v-model="category" disabled />
    </div>
    <button @click="updateTodo" type="button" class="update-button">Todo aktualisieren</button>
    <button @click="goBack" type="button" class="cancel-button">Abbrechen</button>
  </form>
</template>

<style scoped>
.container {
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f9f9f9;
}

form {
  max-width: 500px;
  width: 100%;
  background: #000000;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

h1 {
  margin-bottom: 20px;
  font-size: 24px;
  text-align: center;
  color: #ffffff;
}
</style>
