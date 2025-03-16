<script setup lang="ts">
/**
 * This component displays a list of todos and provides functionalities to delete, update by navigating to the update view
 * It also includes functions to format dates, toggle the finished status of todos and download a CSV file of the todos.
 */

import { Button } from 'agnostic-vue'
import config from '../../config'
import { showToast, Toast } from '../../ts/toasts'
import { faCheck, faXmark } from '@fortawesome/free-solid-svg-icons'
import { computed, onMounted, ref, type Ref } from 'vue'
import { useRouter } from 'vue-router'
import type { Assignee } from '../../ts/Assignee'
import type { TodoResponse } from '../../ts/Todo'
import '@/assets/buttons.css'
import '@/assets/table.css'
import { fetchAllAssignees } from '../../ts/Assignee'
import { fetchAllTodos } from '../../ts/Todo'
import { faTrash, faInfoCircle } from '@fortawesome/free-solid-svg-icons'

// properties
const router = useRouter()
const todos: Ref<TodoResponse[]> = ref([])
const assignees: Ref<Assignee[]> = ref([])
const searchTitle = ref('')
const sortByCategory = ref<'title' | 'dueDate'>('title')
const sortOrder = ref<'asc' | 'desc'>('asc')

/**
 * filter todos based on finished status for search title and duedate
 */
const filterTodos = (showFinished: boolean) => {
  return computed(() => {
    let filteredList = todos.value.filter((todo) => todo.finished === showFinished)

    // filter by search title
    if (searchTitle.value) {
      filteredList = filteredList.filter((todo) =>
        todo.title.toLowerCase().includes(searchTitle.value.toLowerCase())
      )
    }

    // filter by sort category and order
    return filteredList.sort((a, b) => {
      let comparison = 0
      if (sortByCategory.value === 'title') {
        comparison = a.title.localeCompare(b.title)
      } else if (sortByCategory.value === 'dueDate') {
        comparison = new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime()
      }

      return sortOrder.value === 'asc' ? comparison : -comparison
    })
  })
}

const filterOpenTodos = filterTodos(false)
const filterFinishedTodos = filterTodos(true)

/**
 * delete an assignee by id
 * @param id - The ID of the assignee to delete
 */
function deleteTodo(id: number) {
  if (!confirm(`Bist du sicher, dass du das Todo löschen möchtest?`)) {
    return
  }
  fetch(`${config.apiBaseUrl}/todos/${id}`, { method: 'DELETE' })
    .then(() => {
      todos.value = todos.value.filter((todo) => todo.id !== id)
      showToast(new Toast('Alert', `Erfolgreich Todo gelöscht! `, 'success', faCheck, 5))
    })
    .catch(() =>
      showToast(new Toast('Error', `Fehler beim Löschen des Todos!`, 'error', faXmark, 10))
    )
}

/**
 * navigate to update view
 * @param id - The ID of the todo to update
 */
function navigateToUpdate(id: number) {
  router.push({ name: 'update-todos', params: { id } })
}

/**
 * update finished status
 * @param todo - The todo to update
 */
 function setFinished(todo: TodoResponse) {
  fetch(`${config.apiBaseUrl}/todos/${todo.id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      title: todo.title,
      description: todo.description,
      finished: true,
      assigneeIdList: todo.assigneeList.map(a => a.id),
      dueDate: todo.dueDate
    })
  })
    .then(response => {
      if (!response.ok) throw new Error('Fehler beim Beenden des Todos!')
      return response.json()
    })
    .then(updated => {
      todos.value = todos.value.map(t => t.id === updated.id ? updated : t)
      showToast(new Toast('Success', 'Todo wurde beendet!', 'success', faCheck, 5))
    })
}

const isCompletedVisible = ref(false)

/**
 * download CSV file of todos
 */
async function downloadCSV() {
  try {
    const response = await fetch(`${config.apiBaseUrl}/csv-downloads/todos`)
    if (!response.ok) throw new Error('Download fehlgeschlagen!')

    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = 'todos.csv'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
  } catch (error) {
    showToast(new Toast('Error', 'CSV Download fehlgeschlagen!', 'error', faXmark, 10))
  }
}

function formatDate(inputDate: string): string {
    const [year, month, day] = inputDate.split("-");
    return `${day}.${month}.${year}`;
}

/**
 * fetch all assignees and todos on mount
 */
onMounted(() => {
  fetchAllAssignees().then((data) => {
    console.log('Assignees:', data)
    assignees.value = data
  })
  fetchAllTodos().then((data) => {
    console.log('Todos:', data)
    todos.value = data
  })
})
</script>
<template>
  <div class="container-fluid mt-4 px-4">
    <h1 class="text-light mb-4">Todo Manager</h1>

    <div class="p-3 mb-4 rounded" style="background-color: #1e1e1e;">
      <div class="d-flex flex-wrap gap-3 align-items-center">
        <div class="d-flex align-items-center">
          <select v-model="sortByCategory" class="form-select bg-dark text-light border-secondary me-2">
            <option value="title">Titel</option>
            <option value="dueDate">Fälligkeitsdatum</option>
          </select>
          
          <select v-model="sortOrder" class="form-select bg-dark text-light border-secondary">
            <option value="asc">Aufsteigend</option>
            <option value="desc">Absteigend</option>
          </select>
        </div>
        
        <div class="flex-grow-1">
          <input type="text" v-model="searchTitle" class="form-control bg-dark text-light border-secondary search-input" 
            placeholder="Suche nach Titel..." />
        </div>
        
        <button @click="isCompletedVisible = !isCompletedVisible" 
          class="btn px-3 py-2" 
          :class="isCompletedVisible ? 'btn-warning' : 'btn-info'">
          {{ isCompletedVisible ? 'Verstecke beendete Todos' : 'Zeige beendete Todos' }}
        </button>
        
        <button @click="downloadCSV" class="btn btn-success px-3 py-2">
          Download als CSV
        </button>
      </div>
    </div>

    <!-- Offene Todos -->
    <h2 class="text-light mb-3">Offene Todos</h2>
    <div class="todo-list">
      <div v-if="filterOpenTodos.length > 0" class="row row-cols-1 row-cols-md-4 row-cols-lg-5 g-2">
        <div v-for="todo in filterOpenTodos" :key="todo.id" class="col card-column">
          <div class="card bg-dark text-light h-100 border-secondary">
            <div class="card-body d-flex flex-column">
              <h5 class="card-title"> Titel: {{ todo.title }}</h5>
              <p class="card-text mb-auto">Fällig am: {{ formatDate(todo.dueDate) }}</p>
              <div class="d-flex gap-2 mt-3">
                <button @click="navigateToUpdate(todo.id)" class="btn btn-info text-white">
                  <font-awesome-icon :icon="faInfoCircle" /> Details
                </button>
                <button @click="deleteTodo(todo.id)" class="btn btn-danger">
                  <font-awesome-icon :icon="faTrash" /> Löschen
                </button>
              </div>
                <div class="form-check mt-2">
                <input class="form-check-input" type="checkbox" :checked="todo.finished" @change="setFinished(todo)" id="flexCheckDefault">
                <label class="form-check-label" for="flexCheckDefault">
                  Fertig
                </label>
                </div>
            </div>
          </div>
        </div>
      </div>
      <p v-else class="text">Keine offenen Todos gefunden...</p>
    </div>

    <!-- Beendete Todos -->
    <div v-if="isCompletedVisible" class="mt-4">
      <h2 class="text-light mb-3">Beendete Todos</h2>
      <div v-if="filterFinishedTodos.length > 0" class="row row-cols-1 row-cols-md-4 row-cols-lg-5 g-2">
        <div v-for="todo in filterFinishedTodos" :key="todo.id" class="col card-column">
          <div class="card border-success h-100" style="background-color: rgba(25, 135, 84, 0.1);">
            <div class="card-body d-flex flex-column">
              <h5 class="card-title text-success">{{ todo.title }}</h5>
              <p class="card-text mb-auto text-light">Beendet am: {{ formatDate(todo.finishedDate) }}</p>
              <div class="d-flex gap-2 mt-3">
                <button @click="navigateToUpdate(todo.id)" class="btn btn-info text-white">
                  <font-awesome-icon :icon="faInfoCircle" /> Details
                </button>
                <button @click="deleteTodo(todo.id)" class="btn btn-danger">
                  <font-awesome-icon :icon="faTrash" /> Löschen
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <p v-else class="text">Keine beendeten Todos gefunden...</p>
    </div>
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