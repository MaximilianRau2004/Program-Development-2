import type {Assignee} from "./Assignee";
import config from '@/config'
import { showToast, Toast } from '@/ts/toasts'
import { faXmark } from '@fortawesome/free-solid-svg-icons'
import { ref, computed, watch } from 'vue'
import { fetchAllAssignees } from '@/ts/Assignee'

// object for post and put requests
export interface TodoRequest {
    id: number;
    title: string;
    description: string;
    finished: boolean;
    assigneeIdList: number[];
    createdDate: Date;
    dueDate: Date;
    finishedDate: Date;
    category: string;
  }

// object for get requests
  export interface TodoResponse {
    id: number;
    title: string;
    description: string;
    finished: boolean;
    assigneeList: Assignee[];
    createdDate: string;
    dueDate: string;
    finishedDate: string;
    category: string;
  }

  /**
 * Fetch all todos
 * @returns {Promise<Assignee>} A promise that resolves to a all todos
 */
export function fetchAllTodos(): Promise<TodoResponse[]> {
  const url = `${config.apiBaseUrl}/todos`
  return fetch(url)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`Failed to fetch todos. Status: ${response.status}`)
      }
      return response.json()
    })
    .then((data) => data as TodoResponse[])
    .catch((error) => {
      console.error('Error fetching todos:', error.message)
      showToast(
        new Toast('Error', `Fehler beim Abrufen der Todos: ${error.message}`, 'error', faXmark, 10)
      )
      throw error
    })
}

/**
 * Fetch a single todo by ID
 * @param id - The ID of the todo to fetch
 * @returns {Promise<TodoResponse>} A promise that resolves to a single todo
 */
export function fetchTodo(id: number): Promise<TodoResponse> {
  const url = `${config.apiBaseUrl}/todos/${id}`
  return fetch(url)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`Failed to fetch todo. Status: ${response.status}`)
      }
      return response.json()
    })
    .then((data) => data as TodoResponse)
    .catch((error) => {
      console.error('Error fetching todo:', error.message)
      showToast(
        new Toast('Error', `Fehler beim Abrufen des Todos: ${error.message}`, 'error', faXmark, 10)
      )
      throw error
    })
}

export function useAssigneeSelection() {
  const assignees = ref<Assignee[]>([])
  const selectedAssignees = ref<Assignee[]>([])
  const assigneeListSelect = ref<number[]>([])
  const searchQuery = ref('')
  const dropdownOpen = ref(false)

  /**
   * Fetch all assignees for the select dropdown
   */
  const fetchAssignees = () => {
    fetchAllAssignees()
      .then((data) => {
        assignees.value = data
      })
      .catch((error) => {
        console.error('Error fetching assignees:', error)
        showToast(new Toast('Error', `Fehler beim Abrufen der Assignees`, 'error', faXmark, 10))
      })
  }

  /**
   * Filter the assignees based on the search query
   */
  const filteredAssignees = computed(() => {
    if (searchQuery.value) {
      return assignees.value.filter((assignee) =>
        `${assignee.prename} ${assignee.name}`
          .toLowerCase()
          .includes(searchQuery.value.toLowerCase())
      )
    }
    return assignees.value
  })

  /**
   * toggle the dropdown visibility
   */
  const toggleDropdown = () => {
    dropdownOpen.value = !dropdownOpen.value
  }

  /**
   * Remove an assignee from the selected assignees
   * @param id - The ID of the assignee to remove
   */
  const removeAssignee = (id: number) => {
    assigneeListSelect.value = assigneeListSelect.value.filter((assigneeId) => assigneeId !== id)
    selectedAssignees.value = selectedAssignees.value.filter((assignee) => assignee.id !== id)
  }

  /**
   * Watch for changes in the selected assignees and update the selectedAssignees ref
   */
  watch(assigneeListSelect, (newSelection) => {
    selectedAssignees.value = assignees.value.filter((assignee) =>
      newSelection.includes(assignee.id)
    )
  })

  return {
    assignees,
    selectedAssignees,
    assigneeListSelect,
    searchQuery,
    dropdownOpen,
    toggleDropdown,
    removeAssignee,
    filteredAssignees,
    fetchAssignees
  }
}