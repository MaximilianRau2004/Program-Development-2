<script setup lang="ts">
/**
 * This component displays a list of todos and provides functionalities to delete, update by navigating to  the update view
 * It also includes functions to format dates, toggle the finished status of todos and donwload a CSV file of the todos.
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
  <h1>Todo Manager</h1>
  <!-- Sort select -->
  <div class="sort-buttons">
    <label for="sortByCategory">Sortiere nach:</label>
    <select id="sortByCategory" v-model="sortByCategory">
      <option value="title">Titel</option>
      <option value="dueDate">Fälligkeitsdatum</option>
    </select>

    <label for="sortOrder">Sortierung:</label>
    <select id="sortOrder" v-model="sortOrder">
      <option value="asc">Aufsteigend</option>
      <option value="desc">Absteigend</option>
    </select>

    <Button @click="isCompletedVisible = !isCompletedVisible" class="download-button">
      {{ isCompletedVisible ? 'Verstecke beendete Todos' : 'Zeige beendete Todos' }}
    </Button>
    <Button @click="downloadCSV" class="download-button">Download als CSV</Button>
  </div>

  <!-- search field -->
  <div class="search-container">
    <input type="text" v-model="searchTitle" placeholder="Suche nach Titel" />
  </div>

  <!-- container for open todos -->
  <h2>Offene Todos</h2>
  <table v-if="filterOpenTodos.length > 0" class="table">
    <thead>
      <tr>
        <th>Titel</th>
        <th>Beschreibung</th>
        <th>zugewiesene Assignees</th>
        <th>Erstellungsdatum</th>
        <th>Fälligkeitsdatum</th>
        <th>Beendet?</th>
        <th>Kategorie</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="todo in filterOpenTodos" :key="todo.id">
        <td>{{ todo.title }}</td>
        <td>{{ todo.description }}</td>
        <td>
          <span v-for="assignee in todo.assigneeList" :key="assignee.id">
            {{ assignee.prename }} {{ assignee.name }}
          </span>
        </td>
        <td>{{ formatDate(todo.createdDate) }}</td>
        <td>{{ formatDate(todo.dueDate) }}</td>
        <td>
          <input type="checkbox" :checked="todo.finished" @change="setFinished(todo)" />
        </td>
        <td>{{ todo.category }}</td>
        <Button @click="navigateToUpdate(todo.id)" class="update-button">Bearbeiten</Button>
        <Button @click="deleteTodo(todo.id)" class="delete-button">Löschen</Button>
      </tr>
    </tbody>
  </table>
  <Alert v-else> Keine offenen Todos gefunden... </Alert>

  <!-- container for finished todos-->
  <div v-if="isCompletedVisible">
    <h2>Beendete Todos</h2>
    <table v-if="filterFinishedTodos.length > 0" class="table">
      <thead>
        <tr>
          <th>Titel</th>
          <th>Beschreibung</th>
          <th>zugewiesene Assignees</th>
          <th>Erstellungsdatum</th>
          <th>Fälligkeitsdatum</th>
          <th>Beendet am</th>
          <th>Kategorie</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="todo in filterFinishedTodos" :key="todo.id">
          <td>{{ todo.title }}</td>
          <td>{{ todo.description }}</td>
          <td>
            <span v-for="assignee in todo.assigneeList" :key="assignee.id">
              {{ assignee.prename }} {{ assignee.name }}
            </span>
          </td>
          <td>{{ formatDate(todo.createdDate) }}</td>
          <td>{{ formatDate(todo.dueDate) }}</td>
          <td>{{ formatDate(todo.finishedDate) }}</td>
          <td>{{ todo.category }}</td>
          <Button @click="navigateToUpdate(todo.id)" class="update-button">Bearbeiten</Button>
          <Button @click="deleteTodo(todo.id)" class="delete-button">Löschen</Button>
        </tr>
      </tbody>
    </table>
    <Alert v-else> Keine beendeten Todos gefunden... </Alert>
  </div>
</template>
