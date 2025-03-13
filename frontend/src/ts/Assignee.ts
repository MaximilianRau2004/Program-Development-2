import config from '@/config'
import { showToast, Toast } from '@/ts/toasts'
import { faXmark } from '@fortawesome/free-solid-svg-icons'

export interface Assignee {
    id: number;
    prename: string;
    name: string;
    email: string;
  }

  /**
   * Fetch all assignees
   * @returns {Promise<Assignee>} A promise that resolves to a all assignees
   */
  export function fetchAllAssignees(): Promise<Assignee[]> {
    const url = `${config.apiBaseUrl}/assignees`
    return fetch(url)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Fehler beim fetchen der Assignees Status: ${response.status}`)
        }
        return response.json()
      })
      .then((data) => data as Assignee[])
      .catch((error) => {
        console.error('Error fetching assignees:', error.message)
        showToast(
          new Toast(
            'Error', `Fehler beim Abrufen der Assignees`, 'error', faXmark, 10)
        )
        throw error
      })
  }
  
  /**
   * Fetch a single assignee by ID
   * @param id - The ID of the assignee to fetch
   * @returns {Promise<Assignee>} A promise that resolves to a single assignee
   */
  export function fetchAssignee(id: number): Promise<Assignee> {
    const url = `${config.apiBaseUrl}/assignees/${id}`
    return fetch(url)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Failed to fetch assignee. Status: ${response.status}`)
        }
        return response.json()
      })
      .then((data) => data as Assignee)
      .catch((error) => {
        console.error('Error fetching assignee:', error.message)
        showToast(
          new Toast('Error', `Fehler beim Abrufen des Assignees`, 'error', faXmark, 10)
        )
        throw error
      })
  }