<script setup lang="ts">
/**
 * This component displays a list of todos and provides functionalities to delete, update
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
import '@/assets/select-assignees.css'
import '@/assets/input-group.css'
import { fetchAllAssignees } from '../../ts/Assignee'
import { fetchAllTodos, fetchTodo } from '../../ts/Todo'
import { faTrash, faInfoCircle, faTimes } from '@fortawesome/free-solid-svg-icons'
import { useAssigneeSelection } from '@/ts/Todo'

// properties
const router = useRouter()
const todos: Ref<TodoResponse[]> = ref([])
const assignees: Ref<Assignee[]> = ref([])
const searchTitle = ref('')
const sortByCategory = ref<'title' | 'dueDate'>('title')
const sortOrder = ref<'asc' | 'desc'>('asc')

// Edit modal properties
const showEditModal = ref(false)
const editingTodo = ref<TodoResponse | null>(null)
const editTitle = ref('')
const editDescription = ref('')
const editFinished = ref(false)
const editDueDate = ref('')
const editCreatedDate = ref('')
const editCategory = ref('')

// Assignee Selection
const {
  assignees: assigneesList,
  selectedAssignees,
  assigneeListSelect,
  searchQuery,
  dropdownOpen,
  toggleDropdown,
  removeAssignee,
  filteredAssignees
} = useAssigneeSelection()

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
  if (!confirm(`Bist du sicher, dass du das Todo "${todos.value.find(todo => todo.id === id)?.title}" löschen möchtest?`)) {
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
 * Open edit modal with todo details
 * @param id - The ID of the todo to update
 */
async function openEditModal(id: number) {
  try {
    const todoData = await fetchTodo(id)
    editingTodo.value = todoData
    editTitle.value = todoData.title
    editDescription.value = todoData.description
    editFinished.value = todoData.finished
    editCreatedDate.value = todoData.createdDate ? new Date(todoData.createdDate).toISOString().split('T')[0] : ''
    editDueDate.value = todoData.dueDate ? new Date(todoData.dueDate).toISOString().split('T')[0] : ''
    editCategory.value = todoData.category || ''
    
    // Set assignees
    if (Array.isArray(todoData.assigneeList)) {
      assigneeListSelect.value = todoData.assigneeList.map((assignee) => assignee.id)
    }
    
    showEditModal.value = true
  } catch (error) {
    console.error('Error loading todo details:', error)
    showToast(new Toast('Error', 'Fehler beim Laden der Todo-Details', 'error', faXmark, 5))
  }
}

/**
 * Close edit modal
 */
function closeEditModal() {
  showEditModal.value = false
  editingTodo.value = null
}

/**
 * Update todo with edited values
 */
function updateTodo() {
  if (!editingTodo.value) return
  
  const id = editingTodo.value.id
  
  fetch(`${config.apiBaseUrl}/todos/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      title: editTitle.value,
      description: editDescription.value,
      finished: editFinished.value,
      assigneeIdList: assigneeListSelect.value,
      dueDate: editDueDate.value
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
    .then((updatedTodo) => {
      // Update the todo in the list
      todos.value = todos.value.map(t => t.id === updatedTodo.id ? updatedTodo : t)
      showToast(new Toast('Success', `Erfolgreich Todo aktualisiert!`, 'success', faCheck, 5))
      closeEditModal()
    })
    .catch((error) => {
      console.error('Error updating todo:', error)
      showToast(new Toast('Error', `Fehler beim Aktualisieren des Todos: ${error.message}`, 'error', faXmark, 10))
    })
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
    if (!inputDate) return ''
    const [year, month, day] = inputDate.split("-");
    return `${day}.${month}.${year}`;
}

/**
 * fetch all assignees and todos on mount
 */
onMounted(async () => {
  try {
    const [todoData, assigneeData] = await Promise.all([
      fetchAllTodos(),
      fetchAllAssignees()
    ])
    
    console.log('Todos:', todoData)
    todos.value = todoData
    
    console.log('Assignees:', assigneeData)
    assignees.value = assigneeData
    assigneesList.value = assigneeData
  } catch (error) {
    console.error('Error loading data:', error)
    showToast(new Toast('Error', 'Fehler beim Laden der Daten', 'error', faXmark, 5))
  }
})
</script>

<template>
  <div class="container-fluid mt-4 px-4">
    <div class="p-3 mb-4 rounded bg-dark">
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
          <input type="text" v-model="searchTitle" class="form-control bg-dark text-light border-secondary" 
            placeholder="Suche nach Titel..." />
        </div>
        
        <button @click="isCompletedVisible = !isCompletedVisible" 
          class="btn" 
          :class="isCompletedVisible ? 'btn-warning' : 'btn-info'">
          {{ isCompletedVisible ? 'Verstecke beendete Todos' : 'Zeige beendete Todos' }}
        </button>
        
        <button @click="downloadCSV" class="btn btn-success px-3 py-2">
          Download als CSV
        </button>
      </div>
    </div>

    <!-- open todos -->
    <h2 class="text-light mb-3">Offene Todos</h2>
    <div class="todo-list">
      <div v-if="filterOpenTodos.length > 0" class="row row-cols-1 row-cols-md-4 row-cols-lg-5 g-3">
        <div v-for="todo in filterOpenTodos" :key="todo.id" class="col">
          <div class="card bg-dark text-light h-100 border-secondary shadow">
            <div class="card-body d-flex flex-column">
              <h5 class="card-title">Titel: {{ todo.title }}</h5>
              <p class="card-text mb-auto">Fällig am: {{ formatDate(todo.dueDate) }}</p>
              <div class="d-flex gap-2 mt-3">
                <button @click="openEditModal(todo.id)" class="btn btn-info">
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
      <p v-else class="text-light">Keine offenen Todos gefunden...</p>
    </div>

    <!-- finished todos -->
    <div v-if="isCompletedVisible" class="mt-4">
      <h2 class="text-light mb-3">Beendete Todos</h2>
      <div v-if="filterFinishedTodos.length > 0" class="row row-cols-1 row-cols-md-4 row-cols-lg-5 g-3">
        <div v-for="todo in filterFinishedTodos" :key="todo.id" class="col">
          <div class="card border-success h-100 bg-success bg-opacity-10 shadow">
            <div class="card-body d-flex flex-column">
              <h5 class="card-title text-success">{{ todo.title }}</h5>
              <p class="card-text mb-auto text-light">Beendet am: {{ formatDate(todo.finishedDate) }}</p>
              <div class="d-flex gap-2 mt-3">
                <button @click="openEditModal(todo.id)" class="btn btn-info text-white">
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
      <p v-else class="text-light">Keine beendeten Todos gefunden...</p>
    </div>
    
    <!-- Edit Modal -->
    <div v-if="showEditModal" class="modal fade show d-block" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content bg-dark text-light">
          <div class="modal-header border-secondary">
            <h5 class="modal-title">Todo bearbeiten</h5>
            <button type="button" class="btn-close btn-close-white" @click="closeEditModal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label for="editTitle" class="form-label">Titel</label>
              <input type="text" class="form-control bg-dark text-light border-secondary" id="editTitle" v-model="editTitle" required />
            </div>
            <div class="mb-3">
              <label for="editDescription" class="form-label">Beschreibung</label>
              <textarea class="form-control bg-dark text-light border-secondary" id="editDescription" v-model="editDescription" rows="3"></textarea>
            </div>
            
            <!-- Assignees selection -->
            <div class="mb-3">
              <label class="form-label">Assignees</label>
              <div class="d-flex flex-wrap gap-2 mb-2">
                <span v-for="(assignee, index) in selectedAssignees" :key="index" class="badge bg-secondary rounded-pill px-3 py-2">
                  {{ assignee.prename }} {{ assignee.name }}
                  <button type="button" @click="removeAssignee(assignee.id)" class="btn-close btn-close-white ms-2 p-0" style="font-size: 0.65rem;" aria-label="Remove"></button>
                </span>
              </div>

              <div class="dropdown">
                <button type="button" @click="toggleDropdown" class="btn btn-secondary dropdown-toggle w-100 text-start">
                  Assignee auswählen
                </button>
                <div v-if="dropdownOpen" class="dropdown-menu show bg-dark text-light border-secondary w-100">
                  <div class="px-3 py-2">
                    <input type="text" v-model="searchQuery" placeholder="Suchen..." class="form-control bg-dark text-light border-secondary" />
                  </div>
                  <ul class="list-unstyled mb-0 px-0">
                    <li v-for="assignee in filteredAssignees" :key="assignee.id" class="dropdown-item text-light px-3 py-2">
                      <div class="form-check">
                        <input class="form-check-input" type="checkbox" :value="assignee.id" v-model="assigneeListSelect" :id="`assignee-${assignee.id}`">
                        <label class="form-check-label" :for="`assignee-${assignee.id}`">
                          {{ assignee.prename }} {{ assignee.name }}
                        </label>
                      </div>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            
            <div class="mb-3 form-check">
              <input type="checkbox" class="form-check-input" id="editFinished" v-model="editFinished" />
              <label class="form-check-label" for="editFinished">Fertig</label>
            </div>
            <div class="mb-3">
              <label for="editCreatedDate" class="form-label">Erstellungsdatum</label>
              <input type="date" class="form-control bg-dark text-light border-secondary" id="editCreatedDate" v-model="editCreatedDate" disabled />
            </div>
            <div class="mb-3">
              <label for="editDueDate" class="form-label">Fälligkeitsdatum</label>
              <input type="date" class="form-control bg-dark text-light border-secondary" id="editDueDate" v-model="editDueDate" required />
            </div>
            <div class="mb-3" v-if="editCategory">
              <label for="editCategory" class="form-label">Kategorie</label>
              <input type="text" class="form-control bg-dark text-light border-secondary" id="editCategory" v-model="editCategory" disabled />
            </div>
          </div>
          <div class="modal-footer border-secondary">
            <button @click="updateTodo" type="button" class="btn btn-info">Speichern</button>
          </div>
        </div>
      </div>
    </div>
    <!-- Modal backdrop -->
    <div v-if="showEditModal" class="modal-backdrop fade show"></div>
  </div>
</template>

<style>
.dropdown-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
  cursor: pointer;
}

.card {
  transition: transform 0.3s, box-shadow 0.3s;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2) !important;
}

.btn {
  transition: transform 0.2s, box-shadow 0.2s;
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.modal.show {
  background-color: rgba(0, 0, 0, 0.5);
}

.form-control::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

.form-control {
  width: 50%;
}

.form-select {
  width: auto;
}
</style>